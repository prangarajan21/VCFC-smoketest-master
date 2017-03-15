package com.pluribus.vcf.test;
import com.pluribus.vcf.helper.TestSetup;
import com.pluribus.vcf.helper.IperfSetup;
import com.pluribus.vcf.helper.SwitchMethods;
import com.pluribus.vcf.helper.PageInfra;
import com.pluribus.vcf.pagefactory.VCFLoginPage;
import com.pluribus.vcf.pagefactory.VCFHomePage;
import com.pluribus.vcf.pagefactory.VCFIaIndexPage;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;
import org.testng.annotations.Parameters;
import org.apache.log4j.Logger;

public class IATest extends TestSetup {
	private VCFHomePage home1;
	private VCFIaIndexPage iaIndex;
	private VCFLoginPage login;
	private IperfSetup perf;
	private SwitchMethods cli;
	private String user = "network-admin";
	private String passwd = "test123";
	final static Logger logger = Logger.getLogger(IATest.class);
	
	@Parameters({"clientIp","serverIp","mgmtIp"})
	@BeforeClass(alwaysRun = true)
	public void init(String clientIp, String serverIp, String mgmtIp) {
		login = new VCFLoginPage(getDriver());
		home1 = new VCFHomePage(getDriver());
		iaIndex = new VCFIaIndexPage(getDriver());
		perf = new IperfSetup(clientIp,serverIp);
		cli = new SwitchMethods(mgmtIp);
		cli.restartTomcat();
	}
	
	@Parameters({"password"}) 
	@Test(alwaysRun = true)
	public void logintoIA(@Optional("test123") String password) {
		login.login("admin", password);
		home1.gotoIA();
	}
	
	@Parameters({"switchName"}) 
	@Test(groups={"smoke","regression"},dependsOnMethods={"logintoIA"},description="Add collector in IA Page")
	public void addCollectorTest(String switchName) throws Exception{
		if(!iaIndex.addCollector(switchName,user,passwd)) {
			logger.error("Collector addition failed");
			throw new Exception("Collector addition failed");
		}
	}
	
	@Parameters({"trafficDestIp","trafficSrcIp","trafficNumSessions","trafficInterval"}) 
	@Test(groups={"smoke","regression"},dependsOnMethods={"addCollectorTest"},description="Send traffic and verify stats")
	public void simpleTrafficTest(String trafficDestIp, String trafficSrcIp, int trafficNumSessions, int trafficInterval) throws Exception{
		/* Clearing switch before test*/
		cli.clearSessions();
		
		/* Iperf setup */
		perf.startServer();
		perf.sendTraffic(trafficNumSessions, trafficInterval, trafficDestIp);
		
		/* Verify on switch first */
		int connCount = cli.getConnectionCount(trafficDestIp);
		if(connCount == trafficNumSessions) {
			logger.debug("Switch connection count test passed");
		} else {
			logger.error("Switch connection count test failed");
		}
		boolean status = false;
		iaIndex.gotoIADashboard();
		/* Verify on VCFC */
		
		status = verifyVCFCount(trafficNumSessions);
		if(status == true) {
			logger.debug("VCFC count verification passed");
		} else {
			logger.error("VCFC count verification failed");			
		}
		
		//Apply search filter for srcIp
		iaIndex.applySearchFilter("srcIp: "+trafficSrcIp);
		status = verifyVCFCount(trafficNumSessions);
		if(status == true) {
			logger.debug("VCFC count verification after applying srcIp filter passed");
		} else {
			logger.error("VCFC count verification after applying srcIP filter failed");			
		}
		
		//Apply search filter for dstIp
		iaIndex.applySearchFilter("dstIp: "+trafficDestIp);
		status = verifyVCFCount(trafficNumSessions);
		if(status == true) {
			logger.debug("VCFC count verification after applying dstIp filter passed");
		} else {
			logger.error("VCFC count verification after applying dstIP filter failed");			
		}
	}
	
	@Test(groups={"smoke","regression"},dependsOnMethods={"simpleTrafficTest"},description="Logout of VCFC")
	public void logout() {
		login.logout();
	}
	public boolean verifyVCFCount(int trafficNumSessions) {
		boolean status = true;
		int vcfcConnCount = iaIndex.getConnectionCount();
		logger.error("vcfcConnCount:"+vcfcConnCount);
		int vcfcAppCount = iaIndex.getAppCount();
		logger.error("vcfcAppCount:"+vcfcAppCount);

		if (vcfcConnCount != trafficNumSessions) {
			logger.error("vcfcConnCount:"+vcfcConnCount+"expected:"+trafficNumSessions);
			status = false;
		} 
		if (vcfcAppCount != 1) {
			logger.error("vcfcAppCount:"+vcfcAppCount);
			status = false;
		}
		return status;
	}
}
