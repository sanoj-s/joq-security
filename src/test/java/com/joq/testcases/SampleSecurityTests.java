package com.joq.testcases;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.zaproxy.clientapi.core.ClientApiException;

import com.joq.exception.AutomationException;
import com.joq.keywords.SecurityActions;
import com.joq.runner.TestRunner;

public class SampleSecurityTests extends TestRunner {
	@BeforeClass
	public void setup() throws IOException, InterruptedException {
		// new SecurityActions().zapStarter();
	}

	@Test(enabled = true, description = "Security Scan - Spider")
	public void TC001_spiderScanOnTarget()
			throws AutomationException, IOException, InterruptedException, ClientApiException {
		String target = new SecurityActions().loadURLForScan(zapClient, "https://reqres.in/");
		new SecurityActions().startSpider(zapClient, target, false);
	}

	@Test(enabled = false, description = "Security Scan - Spider with list of targets")
	public void TC002_spiderScanOnTargets()
			throws AutomationException, IOException, InterruptedException, ClientApiException {
		List<String> sites = new ArrayList<String>();
		sites.add("https://reqres.com/");
		sites.add("https://openweathermap.in/api");
		new SecurityActions().startSpider(zapClient, sites, false);
	}

	@Test(enabled = false, description = "Security Scan - Active Scan")
	public void TC003_activeScanOnTarget()
			throws AutomationException, IOException, InterruptedException, ClientApiException {
		String target = new SecurityActions().loadURLForScan(zapClient, "https://reqres.in/");
		new SecurityActions().startActiveScan(zapClient, target);
	}

	@Test(enabled = false, description = "Security Scan - Active Scan with list of targets")
	public void TC004_activeScanOnTargets()
			throws AutomationException, IOException, InterruptedException, ClientApiException {
		List<String> sites = new ArrayList<String>();
		sites.add("https://reqres.com/");
		sites.add("https://openweathermap.in/api");
		new SecurityActions().startActiveScan(zapClient, sites);
	}

}
