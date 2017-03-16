package com.pluribus.vcf.test;
import com.pluribus.vcf.helper.SwitchMethods;
import com.pluribus.vcf.helper.TestSetup;
import junit.framework.Assert;
import com.pluribus.vcf.pagefactory.LicenseTypes;
import com.pluribus.vcf.pagefactory.VCFLoginPage;
import com.pluribus.vcf.pagefactory.VCFHomePage;
import com.pluribus.vcf.pagefactory.VcfSettingsPage;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Optional;
import org.apache.log4j.Logger;

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
    final static Logger logger = Logger.getLogger(InitialSetup.class);
    
    @Parameters({"mgmtIp"})  
    @BeforeClass(alwaysRun = true)
    public void init(String mgmtIp) {
    	cli = new SwitchMethods(mgmtIp);
        cli.restartTomcat();//Workaround for bug 15007	
       login = new VCFLoginPage(getDriver());
       home = new VCFHomePage(getDriver());
       settings = new VcfSettingsPage(getDriver());
    }
    
    @Parameters({"password"})  
    @Test(groups = {"smoke","regression"},dependsOnMethods = {"loginAsAdmin"},description = "Login to VCF as test123 After Password Change")
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
    		logger.error("Seed Switch addition failed");
    		throw new Exception(" Seed Switch addition failed");
    	} 
    }
    
    @Test(groups = {"smoke","regression"}, dependsOnMethods = { "addSeedSwitch" }, description = "Authorize seed switches")
    public void authSeedSwitch() throws InterruptedException {
    	settings.authSeedSwitches(switchUserName,switchPassword);
    	Thread.sleep(1000); //Waiting for success message to go away
    }

    @Parameters({"dataNodeHost"}) 
    @Test(groups = {"smoke","regression"}, dependsOnMethods = { "authSeedSwitch" },description = "Add data node & verify")
    public void addDataNode(@Optional("") String dataNodeHost) throws Exception{
    	System.out.println("dataNodeHost"+dataNodeHost);
    	if(!dataNodeHost.isEmpty()) {
    		settings.addDataNode(dataNodeName, dataNodeHost, nodeUserName, heapSize, nodePassword);
    	    if(!settings.verifyDataNode(dataNodeName)) {
    	    	logger.error("Add Data Node failed");
    	    	throw new Exception(" Add Data Node failed ");
    	    }
    	}
    }
    
    @Test(groups = {"smoke","regression"}, dependsOnMethods = { "loginAsTest123" },description = "Add data node & verify")
    public void activateLicense() throws Exception{
    	settings.activateLicense(pncuName, pncPwd, LicenseTypes.VCFC_SSC_1YR_10M);
    }

    
    @Test(groups = {"smoke","regression"}, dependsOnMethods = { "loginAsTest123" },description = "Navigate all pages in VCF settings page")
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
