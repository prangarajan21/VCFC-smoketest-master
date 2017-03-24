package com.pluribus.vcf.test;
import com.pluribus.vcf.helper.TestSetup;
import com.pluribus.vcf.pagefactory.VCFLoginPage;
import com.pluribus.vcf.pagefactory.VCFHomePage;
import com.pluribus.vcf.pagefactory.VCFPaIndexPage;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;
import org.testng.annotations.Parameters;

public class PATest extends TestSetup{
	private VCFHomePage home1;
	private VCFPaIndexPage paIndex;
	private VCFLoginPage login;
	private String pcapName = "localpcap";

	@BeforeClass(alwaysRun = true)
	public void init() {
		home1 = new VCFHomePage(getDriver());
		login = new VCFLoginPage(getDriver());
		paIndex = new VCFPaIndexPage(getDriver());
	}
	
	@Parameters({"password"}) 
	@Test(alwaysRun = true)
	public void logintoPA(@Optional("test123") String password) {
		login.login("admin", password);
		home1.gotoPA();
	}
	
	@Parameters({"password"}) 
	@Test(alwaysRun = true)
	public void logintoIA(@Optional("test123") String password) {
		login.login("admin", password);
		home1.gotoPA();
	}
	
	@Parameters({"vcfIp"}) 
	@Test(groups={"smoke","regression"},dependsOnMethods={"logintoPA"},description="Add local Pcap")
	public void addPcapTest(String vcfIp) throws Exception{
		paIndex.addLocalPcap(pcapName,vcfIp);
		if(!paIndex.verifyPcap(pcapName)) {
			logger.error("Local pcap config failed");
			throw new Exception("Local pcap config failed");
		}
	}
	
	
	@Test(groups={"smoke","regression"},dependsOnMethods={""},description="Logout of VCFC")
	public void logout() {
		login.logout();
	}
}
