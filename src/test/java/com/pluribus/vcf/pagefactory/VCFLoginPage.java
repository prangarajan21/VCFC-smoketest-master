package com.pluribus.vcf.pagefactory;

import com.pluribus.vcf.helper.PageInfra;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class VCFLoginPage extends PageInfra{

	/**
	 * All WebElements are identified by @FindBy annotation
	 */

	@FindBy(name="username")
	WebElement userNameVCF;
	
	@FindBy(name="password")
	WebElement passwordVCF;
	
	@FindBy(name="newPassword")
	WebElement newPassword;
	
	@FindBy(id="oldPassword")
	WebElement oldPassword;
	
	@FindBy(id="confirmNewPassword")
	WebElement confirmNewPassword;
	
	@FindBy(css="button.btn.btn-primary")
	WebElement loginBtn;
	
	@FindBy(css = "a.fa.fa-sign-out")
	WebElement vcfLogout;

	@FindBy(how = How.CSS, using = "a.fa.fa-cogs")
	WebElement vcfSettingsIcon;
	
	@FindBy(how = How.CSS, using = "a.fa.fa-home")
	WebElement vcfHomeIcon;
	
	public VCFLoginPage(WebDriver driver){
		 super(driver);
	}
	
	//Set user name in textbox
	public void setUserName(String strUserName){
		waitForElementVisibility(userNameVCF,100);
		setValue(userNameVCF,strUserName);
	}
	public void logout(){
		waitForElementVisibility(vcfLogout,100);
		if(vcfLogout.isEnabled()) {
			vcfLogout.click();
		} 	
	}
	public void gotoHome() {
		vcfHomeIcon.click();
	}
	public void waitForLogoutButton() {
		waitForElementVisibility(vcfLogout,100);
	}
	
	public void setPassword(String strPassword){
		setValue(passwordVCF,strPassword);
	}
	
	public void setOldPassword(String strPassword){	
		setValue(oldPassword,strPassword);
	}
	
	public void setNewPassword(String strPassword){
		setValue(newPassword,strPassword);
	}
	
	public void setConfirmPassword(String strPassword){
		setValue(confirmNewPassword,strPassword);
	}
	
	//Click on login button
	public void clickLogin(){
		    waitForElementVisibility(loginBtn);
			retryingFindClick(loginBtn);		
	}
	
	public void firstlogin(String strUserName,String newPassword){
		    login(strUserName,strUserName);
			this.setOldPassword(strUserName);
			this.setNewPassword(newPassword);
			this.setConfirmPassword(newPassword);
			this.clickLogin();
	}
	
	public void login(String strUserName,String strPasword ){
		this.setUserName(strUserName);
		this.setPassword(strPasword);
		this.clickLogin();	
	}
}
