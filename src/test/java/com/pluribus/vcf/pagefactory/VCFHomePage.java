package com.pluribus.vcf.pagefactory;

import com.pluribus.vcf.helper.PageInfra;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class VCFHomePage extends PageInfra{

	@FindBy(how = How.CSS, using = "a.fa.fa-cogs")
    WebElement vcfSettingsIcon;

    @FindBy(how = How.CSS, using = "a.fa.fa-home")
    WebElement vcfHomeIcon;

	@FindBy(how = How.CSS, using = "div#pie-vcf-ia span")
	WebElement vcfIAIcon;
	
	@FindBy(how = How.CSS, using = "div#pie-vcf-pa span")
	WebElement vcfPAIcon;
	
	@FindBy(how = How.CSS, using = "div#pie-vcf-mgr span")
	WebElement vcfMgrIcon;
	
	@FindBy(how = How.CSS, using = "a.inner-center div") 
	WebElement vcfCenterIcon;

	@FindBy(css = "a.fa.fa-sign-out")
	WebElement vcfLogout;
	
	public VCFHomePage(WebDriver driver) {
         	super(driver);
	}
	
	public void gotoIA() {
	    System.out.println(driver.toString());
		driver.navigate().refresh();
		waitForElementVisibility(vcfIAIcon,1000);
		vcfIAIcon.click();
	}	
		
	public void gotoPA() {
		driver.navigate().refresh();
		vcfPAIcon.click();
	}
	
	public void gotoVCFMgr() {	
		driver.navigate().refresh();
		vcfMgrIcon.click();
	}
	
}
