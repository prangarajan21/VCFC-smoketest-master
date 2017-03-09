package com.pluribus.vcf.test;
import com.pluribus.vcf.helper.TestSetup;
import com.pluribus.vcf.helper.IperfSetup;
import com.pluribus.vcf.helper.SwitchMethods;
import com.pluribus.vcf.helper.PageInfra;
import com.pluribus.vcf.pagefactory.VCFLoginPage;
import com.pluribus.vcf.pagefactory.VCFHomePage;
import com.pluribus.vcf.pagefactory.VCFIaIndexPage;
import org.testng.annotations.BeforeClass;
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
	}
	
	@Parameters("switchName") 
	@Test(groups={"smoke","regression"},description="Add collector in IA Page")
	public void addCollectorTest(String switchName){
		login.login("admin","test123");
		home1.gotoIA();
		iaIndex.addCollector(switchName,user,passwd);
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
			logger.error("VCFC count verification failed");			
		} else {
			logger.debug("VCFC count verification passed");
		}
		
		
		//Apply search filter for srcIp
		iaIndex.applySearchFilter("srcIp: "+trafficSrcIp);
		status = verifyVCFCount(trafficNumSessions);
		if(status == true) {
			logger.error("VCFC count verification after applying srcIP filter failed");			
		} else {
			logger.debug("VCFC count verification after applying srcIp filter passed");
		}
		
		//Apply search filter for dstIp
		iaIndex.applySearchFilter("dstIp: "+trafficDestIp);
		status = verifyVCFCount(trafficNumSessions);
		if(status == true) {
			logger.error("VCFC count verification after applying dstIP filter failed");			
		} else {
			logger.debug("VCFC count verification after applying dstIp filter passed");
		}
	}
	
	public boolean verifyVCFCount(int trafficNumSessions) {
		boolean status = true;
		
		int vcfcConnCount = iaIndex.getConnectionCount();
		logger.debug("vcfcConnCount:"+vcfcConnCount);
		int vcfcAppCount = iaIndex.getAppCount();
		logger.debug("vcfcAppCount:"+vcfcAppCount);

		if (vcfcConnCount != trafficNumSessions) {
			logger.error("VCFC connection count test failed");
			status = false;
		} 
		if (vcfcAppCount != 1) {
			logger.error("VCFC app count test failed");
			status = false;
		}
		return status;
	}
}
