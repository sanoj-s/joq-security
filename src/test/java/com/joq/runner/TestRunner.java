package com.joq.runner;

import java.io.IOException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.zaproxy.clientapi.core.ClientApi;

import com.joq.keywords.SecurityActions;
import com.joq.reporting.AutomationReport;

@Listeners({ AutomationReport.class })
public class TestRunner {
	public ClientApi zapClient;

	@BeforeClass(alwaysRun = true)
	public void SetUp() throws InterruptedException, IOException {
		try {
			zapClient = new SecurityActions().createZapClient();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
