package com.pluribus.vcf.test;
import com.pluribus.vcf.helper.TestSetup;
import com.jcabi.ssh.Shell;
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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class IATest extends TestSetup {
	private VCFHomePage home1;
	private VCFIaIndexPage iaIndex;
	private VCFLoginPage login;
	private IperfSetup perf;
	private SwitchMethods cli;
	private String user = "network-admin";
	private String passwd = "test123";
	
	@Parameters({"clientIp","serverIp","mgmtIp"})
	@BeforeClass(alwaysRun = true)
	public void init(String clientIp, String serverIp, String mgmtIp) {
		cli = new SwitchMethods(mgmtIp);
		//cli.restartTomcat();//Workaround for bug 15007
		login = new VCFLoginPage(getDriver());
		home1 = new VCFHomePage(getDriver());
		iaIndex = new VCFIaIndexPage(getDriver());
		perf = new IperfSetup(clientIp,serverIp);
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
			com.jcabi.log.Logger.error("addCollector","Collector addition failed");
			throw new Exception("Collector addition failed");
		} else {
			com.jcabi.log.Logger.info("addSeedSwitch", "Successfully added & verified collector"+switchName);
		}
	}
	
	@Parameters({"vcfIp","switchName","trafficDestIp","trafficSrcIp","trafficNumSessions","trafficInterval"}) 
	@Test(groups={"smoke","regression"},dependsOnMethods={"addCollectorTest"},description="Send traffic and verify stats")
	public void simpleTrafficTest(String vcfIp, String switchName, String trafficDestIp, String trafficSrcIp, int trafficNumSessions, int trafficInterval) throws Exception{
		// Clearing switch before test
		cli.clearSessions();
		
		// Iperf setup 
		perf.startServer();
		perf.sendTraffic(trafficNumSessions, trafficInterval, trafficDestIp);
		
		//Verify on switch first
		int connCount = cli.getConnectionCount(trafficDestIp);
		if(connCount == trafficNumSessions) {
			com.jcabi.log.Logger.info("simpleTrafficTest","Connection count test passed on switch"+switchName);
		} else {
			com.jcabi.log.Logger.error("simpleTrafficTest","Connection count test failed on switch"+switchName);
		}
		
		boolean status = false;
		iaIndex.gotoIADashboard();
		// Verify on VCFC 
		status = verifyVCFCount(trafficNumSessions,vcfIp);
		if(status == true) {
			com.jcabi.log.Logger.info("simpleTrafficTest","VCFC count verification passed");
		} else {
			com.jcabi.log.Logger.error("simpleTrafficTest","VCFC count verification failed");			
		}
		
		//Apply search filter for srcIp
		iaIndex.applySearchFilter("srcIp: "+trafficSrcIp);
		status = verifyVCFCount(trafficNumSessions,vcfIp);
		if(status == true) {
			com.jcabi.log.Logger.info("simpleTrafficTest","VCFC count verification after applying srcIp filter passed");
		} else {
			com.jcabi.log.Logger.error("simpleTrafficTest","VCFC count verification after applying srcIP filter failed");			
		}
		
		//Apply search filter for dstIp
		iaIndex.applySearchFilter("dstIp: "+trafficDestIp);
		status = verifyVCFCount(trafficNumSessions,vcfIp);
		if(status == true) {
			com.jcabi.log.Logger.info("simpleTrafficTest","VCFC count verification after applying dstIp filter passed");
		} else {
			com.jcabi.log.Logger.error("simpleTrafficTest","VCFC count verification after applying dstIP filter failed");			
		}
		if(status == false) {
			throw new Exception(" Simple traffic test failed");
		}
	}
	/*
	@Test(groups={"smoke","regression"},dependsOnMethods={"addCollectorTest"},description="Tagging test")
	public void tagTest() throws Exception{
		String fileLoc = "C:\\Desktop\\srcIp.csv";
		writeToFile(fileLoc,"item_srcip,item_dstip,Owner,Device,Group,Function,Name,Security_List\n4.4.4.129,,,,,,,");
		iaIndex.uploadTag(fileLoc);
	}
	
	@Test(groups={"smoke","regression"},dependsOnMethods={"tagTest"},description="Logout of VCFC")
	public void logout() {
		login.logout();
	}
	*/
	public boolean verifyVCFCount(int trafficNumSessions, String vcfIp) {
		boolean status = true;
		int vcfcConnCount = iaIndex.getConnectionCount();
		com.jcabi.log.Logger.info("verifyVCFCCount","vcfcConnCount:"+vcfcConnCount);
		int vcfcAppCount = iaIndex.getAppCount();
		com.jcabi.log.Logger.info("verifyVCFCCount","vcfcAppCount:"+vcfcAppCount);

		if (vcfcConnCount != trafficNumSessions) {
			com.jcabi.log.Logger.error("verifyVCFCCount","vcfcConnCount:"+vcfcConnCount+"expected:"+trafficNumSessions);
			status = false;
		} 
		if (vcfcAppCount != 1) {
			com.jcabi.log.Logger.error("verifyVCFCCount","vcfcAppCount:"+vcfcAppCount);
			status = false;
		}
		if(status == false) {
			Shell sh1 = getVcfShell(vcfIp);
			String out1;
			try {
				out1 = new Shell.Plain(sh1).exec("docker exec -it vcf-elastic bash");	
				com.jcabi.log.Logger.info("verifyVCFCount",out1);
				out1 = new Shell.Plain(sh1).exec("curl localhost:9200/_cat/indices?v");
				com.jcabi.log.Logger.info("verifyVCFCount",out1);
			} catch(Exception e) {
				com.jcabi.log.Logger.error("verifyVCFCount",e.toString());
			}
		}
		return status;
	}
	
	
}
