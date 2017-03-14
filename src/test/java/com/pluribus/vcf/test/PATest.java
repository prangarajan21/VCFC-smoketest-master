package com.pluribus.vcf.test;
import com.pluribus.vcf.helper.TestSetup;
import com.pluribus.vcf.pagefactory.VCFLoginPage;
import com.pluribus.vcf.pagefactory.VCFHomePage;
import com.pluribus.vcf.pagefactory.VCFPaIndexPage;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;
import org.testng.annotations.Parameters;
import org.apache.log4j.Logger;

public class PATest extends TestSetup{
	private VCFHomePage home1;
	private VCFPaIndexPage paIndex;
	private VCFLoginPage login;
	final static Logger logger = Logger.getLogger(PATest.class);

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
}
