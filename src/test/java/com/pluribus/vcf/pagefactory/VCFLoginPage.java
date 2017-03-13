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
	//Set password in password textbox
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
			loginBtn.click();
	}
	
	public void firstlogin(String strUserName,String newPassword){
		//Fill user name
		    login(strUserName,strUserName);
			//Fill Old Password 
			this.setOldPassword(strUserName);
			//Fill New Password 
			this.setNewPassword(newPassword);
			//Fill Confirm Password 
			this.setConfirmPassword(newPassword);
			//Click Login button
			this.clickLogin();			
	}
	
	public void login(String strUserName,String strPasword ){
		//Fill user name
		this.setUserName(strUserName);
		//Fill password
		this.setPassword(strPasword);
		//Click Login button
		this.clickLogin();	
	}
}
