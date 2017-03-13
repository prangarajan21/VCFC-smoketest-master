package com.pluribus.vcf.test;
import com.pluribus.vcf.helper.TestSetup;
import com.pluribus.vcf.pagefactory.VCFLoginPage;
import com.pluribus.vcf.pagefactory.VCFHomePage;
import com.pluribus.vcf.pagefactory.VCFPaIndexPage;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.annotations.Parameters;
import org.apache.log4j.Logger;

public class PATest extends TestSetup{
	private VCFHomePage home1;
	private VCFPaIndexPage paIndex;
	final static Logger logger = Logger.getLogger(PATest.class);

	@BeforeClass(alwaysRun = true)
	public void init() {
		home1 = new VCFHomePage(getDriver());
		paIndex = new VCFPaIndexPage(getDriver());
	}
	
}
