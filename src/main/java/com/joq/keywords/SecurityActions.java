package com.joq.keywords;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ApiResponseElement;
import org.zaproxy.clientapi.core.ClientApi;

import com.joq.exception.AutomationException;
import com.joq.utils.AutomationConstants;

public class SecurityActions {

	DataHandler dataHandler = new DataHandler();
	Utilities utilities = new Utilities();

	/**
	 * Method to start the OWASP ZAP server
	 * 
	 * @author sanoj.swaminathan
	 * @since 28-04-2023
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws AutomationException
	 */
	public void zapStarter() throws IOException, InterruptedException, AutomationException {
		try {
			// Create ProcessBuilder instance for ZAP.bat
			String zapBatchFilePath = dataHandler.getProperty(AutomationConstants.SECURITY_TEST_CONFIG,
					AutomationConstants.ZAP_BATCH_FILE_PATH);
			ProcessBuilder processBuilder = new ProcessBuilder(zapBatchFilePath, "-daemon");

			// Start the process
			Process process = processBuilder.start();

			// Read the output from the process and print to console
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}

			// Check if ZAP is ready to receive requests
			boolean zapReady = false;
			while (!zapReady) {
				zapReady = isZapReady("localhost", 8080);
				Thread.sleep(1000);
			}

			// Print a message to indicate that ZAP has started
			if (zapReady) {
				System.out.println("ZAP has started!");
			}

		} catch (Exception e) {
			throw new AutomationException(utilities.getExceptionMessage(), e);
		}
	}

	/**
	 * Method to check whether the ZAP server started or not
	 * 
	 * @author sanoj.swaminathan
	 * @since 02-05-2023
	 * @param host
	 * @param port
	 * @return
	 */
	private static boolean isZapReady(String host, int port) {
		try (Socket socket = new Socket()) {
			socket.connect(new InetSocketAddress(host, port), 2000);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Method to create the ZAP client. Use this method as a precondition prior
	 * start the active or spider scan activities.
	 * 
	 * @author sanoj.swaminathan
	 * @since 28-04-2023
	 * @return
	 * @throws AutomationException
	 */
	public ClientApi createZapClient() throws AutomationException {
		ClientApi zapClient = null;
		try {
			final String ZAP_ADDRESS = dataHandler.getProperty(AutomationConstants.SECURITY_TEST_CONFIG,
					AutomationConstants.ZAP_ADDRESS_OR_IP);
			final int ZAP_PORT = Integer.valueOf(
					dataHandler.getProperty(AutomationConstants.SECURITY_TEST_CONFIG, AutomationConstants.ZAP_PORT));
			final String ZAP_API_KEY = dataHandler.getProperty(AutomationConstants.SECURITY_TEST_CONFIG,
					AutomationConstants.ZAP_API_KEY);
			zapClient = new ClientApi(ZAP_ADDRESS, ZAP_PORT, ZAP_API_KEY);
		} catch (Exception e) {
			throw new AutomationException(utilities.getExceptionMessage(), e);
		}
		return zapClient;
	}

	/**
	 * Add the URL to ZAP for scanning it
	 * 
	 * @author sanoj.swaminathan
	 * @since 02-05-2023
	 * @param zapClient
	 * @param targetURL
	 * @return
	 * @throws AutomationException
	 */
	public String loadURLForScan(ClientApi zapClient, String targetURL) throws AutomationException {
		try {
			zapClient.core.accessUrl(targetURL, null);
			System.out.println("URL: " + targetURL + " added to the ZAP for scan operations");
		} catch (Exception e) {
			throw new AutomationException(utilities.getExceptionMessage(), e);
		}
		return targetURL;
	}

	/**
	 * Add the URLs to ZAP for scanning it
	 * 
	 * @author sanoj.swaminathan
	 * @since 02-05-2023
	 * @param zapClient
	 * @param targetURL
	 * @return
	 * @throws AutomationException
	 */
	public List<String> loadURLsForScan(ClientApi zapClient, List<String> targetURLs) throws AutomationException {
		try {
			for (int i = 0; i < targetURLs.size(); i++) {
				zapClient.core.accessUrl(targetURLs.get(i), null);
				System.out.println("URL: " + targetURLs.get(i) + " added to the ZAP for scan operations");
			}

		} catch (Exception e) {
			throw new AutomationException(utilities.getExceptionMessage(), e);
		}
		return targetURLs;
	}

	/**
	 * Method to perform the active scan for the given target
	 * 
	 * @author sanoj.swaminathan
	 * @since 02-05-2023
	 * @param zapClient
	 * @param target
	 * @throws AutomationException
	 */
	public void startActiveScan(ClientApi zapClient, String target) throws AutomationException {
		try {
			String scanid;
			int progress;

			// =========================================
			System.out.println("Active scan on " + target);
			ApiResponse resp = zapClient.ascan.scan(target, "True", "False", null, null, null);

			// The scan now returns a scan id to support concurrent scanning
			scanid = ((ApiResponseElement) resp).getValue();

			// Poll the status until it completes
			while (true) {
				Thread.sleep(5000);
				progress = Integer.parseInt(((ApiResponseElement) zapClient.ascan.status(scanid)).getValue());
				System.out.println("Active scan progress : " + progress + "%");
				if (progress >= 100) {
					break;
				}
			}
			System.out.println("Active scan complete");

			System.out.println("Generating the report");
			String reportLocation = generateVulnerabilityScanReport(zapClient);
			System.out.println("Report generated successfully at " + reportLocation);

		} catch (Exception e) {
			throw new AutomationException(utilities.getExceptionMessage(), e);
		}
	}

	/**
	 * Method to perform the active scan for the given targets
	 * 
	 * @author sanoj.swaminathan
	 * @since 02-05-2023
	 * @param zapClient
	 * @param targets
	 * @param needPassiveScan
	 * @throws AutomationException
	 */
	public void startActiveScan(ClientApi zapClient, List<String> targets) throws AutomationException {
		try {
			String scanid;
			int progress;

			// Starting the spider scan on target
			List<String> sites = targets;

			for (String site : sites) {
				System.out.println("Active scan on " + site);
				ApiResponse resp = zapClient.ascan.scan(site, "True", "False", null, null, null);
				scanid = ((ApiResponseElement) resp).getValue();

				// Poll the status until it completes
				while (true) {
					Thread.sleep(1000);
					progress = Integer.parseInt(((ApiResponseElement) zapClient.ascan.status(scanid)).getValue());
					System.out.println("Active scan progress : " + progress + "%");
					if (progress >= 100) {
						break;
					}
				}
				System.out.println("Active scan completed successfully");
			}
			System.out.println("Generating the report");
			String reportLocation = generateVulnerabilityScanReport(zapClient);
			System.out.println("Report generated successfully at " + reportLocation);

		} catch (Exception e) {
			throw new AutomationException(utilities.getExceptionMessage(), e);
		}
	}

	/**
	 * Method to start the Spider scan for the given target
	 * 
	 * @author sanoj.swaminathan
	 * @since 28-04-2023
	 * @param zapClient
	 * @param target
	 * @param needPassiveScan
	 * @throws AutomationException
	 */
	public void startSpider(ClientApi zapClient, String target, boolean needPassiveScan) throws AutomationException {
		try {
			String scanid;
			int progress;
			// Starting the spider scan on target
			System.out.println("Spider on " + target);
			ApiResponse resp = zapClient.spider.scan(target, null, null, null, null);

			// The scan now returns a scan id to support concurrent scanning
			scanid = ((ApiResponseElement) resp).getValue();

			// Poll the status until it completes
			while (true) {
				Thread.sleep(1000);
				progress = Integer.parseInt(((ApiResponseElement) zapClient.spider.status(scanid)).getValue());
				System.out.println("Spider progress : " + progress + "%");
				if (progress >= 100) {
					break;
				}
			}
			System.out.println("Spider completed successfully");

			// Poll the number of records the passive scanner still has to scan until it
			// completes
			if (needPassiveScan) {

				while (true) {
					Thread.sleep(1000);
					progress = Integer.parseInt(((ApiResponseElement) zapClient.pscan.recordsToScan()).getValue());
					System.out.println("Passive Scan progress : " + progress + " records left");
					if (progress < 1) {
						break;
					}
				}
				System.out.println("Passive Scan completed successfully");
			}

			System.out.println("Generating the report");
			String reportLocation = generateVulnerabilityScanReport(zapClient);
			System.out.println("Report generated successfully at " + reportLocation);

		} catch (Exception e) {
			throw new AutomationException(utilities.getExceptionMessage(), e);
		}
	}

	/**
	 * Method to start the spider on the given targets
	 * 
	 * @author sanoj.swaminathan
	 * @since 28-04-2023
	 * @param zapClient
	 * @param targets
	 * @param needPassiveScan
	 * @throws AutomationException
	 */
	public void startSpider(ClientApi zapClient, List<String> targets, boolean needPassiveScan)
			throws AutomationException {
		try {
			String scanid;
			int progress;

			// Starting the spider scan on target
			List<String> sites = targets;

			for (String site : sites) {
				System.out.println("Spider on " + site);
				ApiResponse resp = zapClient.spider.scan(site, null, null, null, null);
				scanid = ((ApiResponseElement) resp).getValue();

				// Poll the status until it completes
				while (true) {
					Thread.sleep(1000);
					progress = Integer.parseInt(((ApiResponseElement) zapClient.spider.status(scanid)).getValue());
					System.out.println("Spider progress : " + progress + "%");
					if (progress >= 100) {
						break;
					}
				}
				System.out.println("Spider completed successfully");

				// Poll the number of records the passive scanner still has to scan until it
				// completes
				if (needPassiveScan) {
					while (true) {
						Thread.sleep(1000);
						progress = Integer.parseInt(((ApiResponseElement) zapClient.pscan.recordsToScan()).getValue());
						System.out.println("Passive Scan progress : " + progress + " records left");
						if (progress < 1) {
							break;
						}
					}
					System.out.println("Passive Scan completed successfully");
				}
			}
			System.out.println("Generating the report");
			String reportLocation = generateVulnerabilityScanReport(zapClient);
			System.out.println("Report generated successfully at " + reportLocation);

		} catch (Exception e) {
			throw new AutomationException(utilities.getExceptionMessage(), e);
		}
	}

	/**
	 * Method to start the Ajax Spider scan for the given target
	 * 
	 * @author sanoj.swaminathan
	 * @since 28-04-2023
	 * @modified 02-05-2023
	 * @param zapClient
	 * @param target
	 * @throws AutomationException
	 */
	public void startAjaxSpider(ClientApi zapClient, String target) throws AutomationException {
		try {
			// Starting the Ajax spidering on target
			System.out.println("Ajax Spider on " + target);
			zapClient.ajaxSpider.scan(target, null, null, null);
			// Wait for the Ajax spider to complete
			while (true) {
				ApiResponse status = zapClient.ajaxSpider.status();
				if ("stopped".equals(status.toString()) || "stopped-external".equals(status.toString())) {
					break;
				}
				Thread.sleep(1000);
			}

			System.out.println("Ajax Spider completed successfully");

			System.out.println("Generating the report");
			String reportLocation = generateVulnerabilityScanReport(zapClient);
			System.out.println("Report generated successfully at " + reportLocation);

		} catch (Exception e) {
			throw new AutomationException(utilities.getExceptionMessage(), e);
		}
	}

	/**
	 * Method to start the Ajax spider on the given targets
	 * 
	 * @author sanoj.swaminathan
	 * @since 28-04-2023
	 * @param zapClient
	 * @param targets
	 * @throws AutomationException
	 */
	public void startAjaxSpider(ClientApi zapClient, List<String> targets) throws AutomationException {
		try {
			// Starting the ajax spidering on target
			List<String> sites = targets;

			for (String site : sites) {
				System.out.println("Ajax Spider on " + site);
				zapClient.ajaxSpider.scan(site, null, null, null);

				// Wait for the Ajax spider to complete
				while (true) {
					ApiResponse status = zapClient.ajaxSpider.status();
					if ("stopped".equals(status.toString()) || "stopped-external".equals(status.toString())) {
						break;
					}
					Thread.sleep(1000);
				}
				System.out.println("Ajax Spider completed successfully");
			}
			System.out.println("Generating the report");
			String reportLocation = generateVulnerabilityScanReport(zapClient);
			System.out.println("Report generated successfully at " + reportLocation);

		} catch (Exception e) {
			throw new AutomationException(utilities.getExceptionMessage(), e);
		}
	}

	/**
	 * Method to generate the Vulnerability Scan Report
	 * 
	 * @author sanoj.swaminathan
	 * @since 28-04-2023
	 * @param zapClient
	 * @throws AutomationException
	 */
	public String generateVulnerabilityScanReport(ClientApi zapClient) throws AutomationException {
		String reportPath = null;
		try {
			reportPath = System.getProperty("user.dir") + "\\Reports\\Security_Execution_Reports\\" + "ReportName" + "_"
					+ getCurrentDateAndTime() + ".html";

			BufferedWriter bw = new BufferedWriter(new FileWriter(reportPath));
			bw.write(new String(zapClient.core.htmlreport(), StandardCharsets.UTF_8));
			bw.close();
		} catch (Exception e) {
			throw new AutomationException(utilities.getExceptionMessage(), e);
		}
		return reportPath;
	}

	/**
	 * To get the current date and time
	 * 
	 * @author sanoj.swaminathan
	 * @since 28-04-2023
	 * @return
	 * @throws AutomationException
	 */
	private String getCurrentDateAndTime() throws AutomationException {
		String dataAndTime;
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			DateFormat timeFormat = new SimpleDateFormat("HH-mm-ss");
			Date date = new Date();
			String currdate = dateFormat.format(date);
			String currtime = timeFormat.format(date);
			dataAndTime = currdate + "_" + currtime;
		} catch (Exception e) {
			throw new AutomationException(utilities.getExceptionMessage(), e);
		}
		return dataAndTime;
	}

}
