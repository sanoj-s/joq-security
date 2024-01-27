package com.joq.reporting;

import java.io.File;
import java.io.IOException;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.joq.keywords.DataHandler;
import com.joq.utils.AutomationConstants;
import com.joq.utils.AutomationMail;
import com.joq.utils.KeyManagement;

public class AutomationReport implements ITestListener {

	/**
	 * Method to set up the Execution Report
	 * 
	 * @author sanoj.swaminathan
	 * @since 03-05-2023
	 * @throws Exception
	 */
	public void onStart(ITestContext testContext) {
		try {

			// Checking the execution expire
			KeyManagement.checkExpiry();

			// Creating the report directory
			if (!new File(System.getProperty("user.dir") + "\\Reports\\Security_Execution_Reports").exists()) {
				(new File(System.getProperty("user.dir") + "\\Reports")).mkdir();
				(new File(System.getProperty("user.dir") + "\\Reports\\Security_Execution_Reports")).mkdir();
			}
		} catch (Exception lException) {
			lException.printStackTrace();
		}
	}

	/**
	 * Method to collect the test names
	 * 
	 * @author sanoj.swaminathan
	 * @since 03-05-2023
	 * @param result
	 */
	public void onTestStart(ITestResult result) {
		try {
			String testName = result.getMethod().getMethodName();
			System.out.println("********************************************");
			System.out.println("Test case execution started for : " + testName);
			System.out.println("********************************************");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to get the pass result of the execution
	 * 
	 * @author sanoj.swaminathan
	 * @since 03-05-2023
	 * @throws IOException
	 */
	public void onTestSuccess(ITestResult result) {
		try {
			System.out.println("********************************************");
			System.out.println("Test case: " + result.getName() + " is pass");
			System.out.println("********************************************");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to get the fail result of the execution
	 * 
	 * @author sanoj.swaminathan
	 * @since 03-05-2023
	 * @throws IOException
	 */
	public void onTestFailure(ITestResult result) {
		try {
			System.out.println("********************************************");
			System.out.println("Test case: " + result.getName() + " is failed");
			System.out.println("Failure Reason: " + result.getThrowable());
			System.out.println("********************************************");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to get the skip result of the execution
	 * 
	 * @author sanoj.swaminathan
	 * @since 03-05-2023
	 * @throws IOException
	 */
	public void onTestSkipped(ITestResult result) {
		try {
			System.out.println("********************************************");
			System.out.println("Test case: " + result.getName() + " is skipped");
			System.out.println("********************************************");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to tear down the report and to call the send mail method
	 * 
	 * @author sanoj.swaminathan
	 * @since 03-05-2023
	 */
	public void onFinish(ITestContext testContext) {
		try {
			String isMailReportNeed = new DataHandler().getProperty(AutomationConstants.FRAMEWORK_CONFIG,
					"isMailReportNeed");
			if (isMailReportNeed.toLowerCase().equals("yes")) {
				new AutomationMail().sendMailReport();
			}
			System.out.println("********************************************");
			System.out.println("TEST CASE EXECUTION COMPLETED");
			System.out.println("********************************************");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
