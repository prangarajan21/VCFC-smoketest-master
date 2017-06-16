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
    private String firstPassword = "admin";
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
    @Test(groups = {"smoke","regression"},dependsOnMethods = {"loginAsAdmin"}, description = "Login to VCF as test123 After Password Change")
    public void loginAsTest123(@Optional("test123")String password) {
        login.login(vcfUserName, password);
        login.waitForLogoutButton();
        assertEquals(getTitle(), "Pluribus Networks VCFcenter");
    }
    
    @Parameters({"switchName","mgmtIp"})  
    @Test(groups = {"smoke","regression"}, dependsOnMethods = { "vcfsettingsPagenavigations" },description = "Add Seed Switch & verify")
    public void addSeedSwitch(String switchName,String mgmtIp) throws Exception{
    	settings.addSeedSwitch(switchName, switchUserName, mgmtIp, switchPassword);
    	if(!settings.verifySeedSwitch(switchName, switchUserName, mgmtIp, switchPassword)) {
    		printLogs("error","addSeedSwitch", "Seed switch addition failed");
    		throw new Exception(" Seed Switch addition failed");
    	} else {
    		printLogs("info","addSeedSwitch", "Successfully added & verified seed switch"+switchName);
    	}  
    }    
    
    @Test(groups = {"smoke","regression"}, dependsOnMethods = { "addSeedSwitch" }, description = "Authorize seed switches")
    public void authSeedSwitch() throws InterruptedException {
    	settings.authSeedSwitches(switchUserName,switchPassword);
    	Thread.sleep(1000); //Waiting for success message to go away
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
    
    @Test(groups = {"smoke","regression"}, dependsOnMethods = { "loginAsTest123" },description = "Activate License")
    public void activateLicense() throws Exception{
    	if(!settings.activateLicense(pncuName, pncPwd, LicenseTypes.VCFC_SSC_3YR_100M)) {
    		printLogs("error","activateLicense","License activation failed");
    		throw new Exception("Activate License failed");
    	}
    	else {
    		printLogs("info","activateLicense","License activation was successful");
    	}
    }
   
    
    @Test(groups = {"smoke","regression"}, dependsOnMethods = {"loginAsTest123"},description = "Navigate all pages in VCF settings page")
    public void vcfsettingsPagenavigations() {
    	settings.navigateToSwitchMenu();
    	settings.navigateToSystemhealthMenu();
    	settings.navigateToServerMenu();
    	settings.navigateTocertsMenu();
    	settings.navigateToadminMenu();
    	settings.navigateToAppMenu();
    }
   
    @Parameters({"password"})
    @Test(groups = {"smoke","regression"}, description = "Login to VCF as admin  and Change Password")
    public void loginAsAdmin(@Optional("test123")String password) {
        login.firstlogin(vcfUserName,password);
        login.waitForLogoutButton();
        login.logout();
    }
   
    @Test(groups={"smoke","regression"}, dependsOnMethods = {"addDataNode"}, description = "Logout of VCFC")
    public void logout() {
        login.logout();
    }
}
