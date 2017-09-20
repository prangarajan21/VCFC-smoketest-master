package com.pluribus.vcf.test;
import com.pluribus.vcf.helper.SwitchMethods;
import com.pluribus.vcf.helper.TestSetup;
import com.pluribus.vcf.pagefactory.LicenseTypes;
import com.pluribus.vcf.pagefactory.VCFLoginPage;
import com.pluribus.vcf.pagefactory.VCFHomePage;
import com.pluribus.vcf.pagefactory.VcfSettingsPage;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.annotations.Parameters;
import org.testng.annotations.Optional;

/**
 *
 * @author Haritha
 */


public class InitialSetup extends TestSetup {
    private VCFLoginPage login;
    private VCFHomePage home;
    private VcfSettingsPage settings;
    private String dataNodeName  = "Node1";
    private String heapSize = "30";
    private String nodePassword = "changeme";
    private String nodeUserName= "vcf";
    private String pncPwd = "test123";
    private String pncuName= "pn-vcf";
    private String vcfUserName = "admin";
    private String switchUserName = "network-admin";
    private String switchPassword = "test123";
    private SwitchMethods cli;
    
    @Parameters({"mgmtIp"})  
    @BeforeClass(alwaysRun = true)
    public void init(String mgmtIp) {
       cli = new SwitchMethods(mgmtIp);
       cli.assignFabric();
       login = new VCFLoginPage(getDriver());
       home = new VCFHomePage(getDriver());
       settings = new VcfSettingsPage(getDriver());
    }
    
    @Parameters({"password"})
    @Test(groups = {"smoke","regression"}, description = "Login to VCF as admin  and Change Password")
    public void loginAsAdmin(@Optional("test123")String password) throws Exception {
        login.firstlogin(vcfUserName,password);
        login.waitForLogoutButton();
        Thread.sleep(30000);
        login.logout();
        Thread.sleep(60000);
    }
   
    @Parameters({"password"})  
    @Test(groups = {"smoke","regression"},description = "Login to VCF as test123 After Password Change")
    public void loginAsTest123(@Optional("test123")String password) throws Exception{
        login.login(vcfUserName, password);
        home.waitForHomeLogo();
    }
   
    @Parameters({"switchName","mgmtIp"})  
    @Test(groups = {"smoke","regression"}, dependsOnMethods = { "activateLicense" },description = "Add Seed Switch & verify")
    public void addSeedSwitch(String switchName,String mgmtIp) throws Exception{
    	settings.addSeedSwitch(switchName, switchUserName, mgmtIp, switchPassword);
    	if(!settings.verifySeedSwitch(switchName, switchUserName, mgmtIp, switchPassword)) {
    		printLogs("error","addSeedSwitch", "Seed switch addition failed");
    		throw new Exception(" Seed Switch addition failed");
    	} else {
    		printLogs("info","addSeedSwitch", "Successfully added & verified seed switch"+switchName);
    	}  
    }    

    @Parameters({"dataNodeHost"}) 
    @Test(groups = {"smoke","regression"}, dependsOnMethods = { "addSeedSwitch" },description = "Add data node & verify")
    public void addDataNode(@Optional("") String dataNodeHost) throws Exception{
    	if(!dataNodeHost.isEmpty()) {
    		settings.addDataNode(dataNodeName, dataNodeHost, nodeUserName, heapSize, nodePassword);
    	    if(!settings.verifyDataNode(dataNodeName)) {
    	    	printLogs("error","addDataNode","Data Node addition failed");
    	    	throw new Exception(" Add Data Node failed ");
    	    }
    	    else {
    	    	printLogs("info","addDataNode", "Successfully added & verified data node"+dataNodeHost); 
    	    }
    	}
    }
    
    @Parameters({"licenseKey"})
    @Test(groups = {"smoke","regression"}, dependsOnMethods = { "loginAsTest123" },description = "Activate License")
    public void activateLicense(String licenseKey) throws Exception{
    	
    	if(!settings.activateLicense(pncuName, pncPwd, LicenseTypes.VCFC_SSC_1YR_10B)) {
    		printLogs("error","activateLicense","License activation failed");
    		throw new Exception("Activate License failed");
    	}
    	else {
    		printLogs("info","activateLicense","License activation was successful");
    	}
    }
    
    @Parameters({"collectorName","switchName"})
	@Test(groups={"smoke","regression"},dependsOnMethods={"addSeedSwitch"},description="Edit collector and update switch information")
	public void editCollectorTest(@Optional("default-netvisor-collector") String collectorName, String switchName) throws Exception{
		if(!settings.editCollector(collectorName, switchName)) {
			printLogs ("error","editCollector","Editing collector config failed");
			throw new Exception("Collector edit failed");
		} else {
			printLogs("info","editCollector","Collector edit was successful");
		}
	}
	
	@Parameters({"collectorName"}) 
	@Test(groups={"smoke","regression"},dependsOnMethods={"editCollectorTest"},description="Activate collector Test")
	public void activateNvosCollectorTest(@Optional("default-netvisor-collector") String collectorName) throws Exception{
		if(!settings.toggleCollState(collectorName,true)) {
			printLogs ("error","activateCollector","Collector activation failed");
			throw new Exception("Collector activation failed");
		} else {
			printLogs("info","addCollector","Collector activation was successful");
		}
	}
	
	/* Adding additional collector (if required)
	@Parameters({"switchName","collectorName"}) 
	@Test(groups={"smoke","regression"},dependsOnMethods={"logintoIA"},description="Add collector in IA Page")
	public void addCollectorTest(String switchName,@Optional("coll1") String collectorName) throws Exception{
		if(!iaIndex.addCollector(collectorName,switchName,user,passwd)) {
			printLogs ("error","addCollector","Collector addition failed");
			throw new Exception("Collector addition failed");
		} else {
			printLogs("info","addCollector","Collector addition was successful");
		}
	}
	*/

	@Test(groups={"smoke","regression"}, dependsOnMethods = {"activateNvosCollectorTest"}, description = "Logout of VCFC")
    public void logout() {
        login.logout();
    }
}
