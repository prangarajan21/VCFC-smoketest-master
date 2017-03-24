package com.pluribus.vcf.pagefactory;

import com.pluribus.vcf.helper.PageInfra;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class VcfSettingsPage extends PageInfra{
	/* Settings toolbar */	
	@FindBy(how = How.XPATH, using = ".//a[contains(@href,'/vcf-center/license')]")
	WebElement licenseTab;

	@FindBy(how = How.XPATH, using = ".//a[contains(@href,'/vcf-center/datanode')]")
	WebElement dataNodeTab;

	@FindBy(how = How.CSS, using = "span.fa-stack-1x.pnc-text")
	WebElement pncCloudbutton;
	
	@FindBy(how = How.ID, using = "username")
	WebElement username;
	
	@FindBy(how = How.ID, using = "password")
	WebElement password;
	
	@FindBy(how = How.NAME, using = "ok")
	WebElement okButton;
	
	@FindBy(how = How.NAME, using = "cancel")
	WebElement cancelButton;
	
	@FindBy(how = How.CSS, using = "button.btn.btn-sm.btn-primary")
	WebElement addButton;
	
	@FindBy(how = How.ID, using = "name")
	WebElement nodeName;
	
	@FindBy(how = How.ID, using = "host")
	WebElement nodeHost;
	
	@FindBy(how = How.NAME, using = "sudo")
	WebElement sudo;
	
	@FindBy(how = How.ID, using = "heapsize")
	WebElement heapsize;
	
	@FindBy(how = How.ID, using = "mgmt-ip")
	WebElement mgmtIp;
	
	@FindBy(how = How.CSS, using = "a.list-group-item.category.config-menu")
	WebElement switchMenu;
	
	@FindBy(how = How.CSS, using = "a.list-group-item.category.health-menu")
	WebElement healthMenu;
	
	@FindBy(how = How.CSS, using = "a.list-group-item.category.server-menu")
	WebElement serverMenu;
	
	@FindBy(how = How.CSS, using = "a.list-group-item.category.apps-menu")
	WebElement appsMenu;
	
	@FindBy(how = How.CSS, using = "a.list-group-item.category.certs-menu")
	WebElement certsMenu;
	
	@FindBy(how = How.CSS, using = "a.list-group-item.category.admin-menu")
	WebElement adminMenu;
	
	@FindBy(how = How.CSS, using = "a.fa.fa-cogs")
	WebElement vcfSettingsIcon;
	
	@FindBy(how = How.CSS, using = "a.fa.fa-home")
	WebElement vcfHomeIcon;
	
	@FindBy(how = How.CSS, using = "a.fa.fa-sign-out")
	WebElement vcfLogout;
		
	@FindBy(how = How.CSS, using = "button.btn.btn-primary.btn-xs")
	WebElement AddAuthServer;
	
	@FindBy(how = How.CSS, using = "button.btn.btn-default.btn-sm")
	WebElement dropDown;
	
	@FindBy(how = How.NAME, using = "baseDn")
	WebElement baseDn;
	
	@FindBy(how = How.ID, using = "ldapManagerDn")
	WebElement ldapManagerDn;
	
	@FindBy(how = How.NAME, using = "ldapManagerPass")
	WebElement ldapManagerPass;
	
	@FindBy(how = How.NAME, using = "ldapUserDnPatterns")
	WebElement ldapUserDnPatterns;
	
	@FindBy(how = How.NAME, using = "ldapUserSearchFilter")
	WebElement ldapUserSearchFilter;
	
	@FindBy(how = How.CSS, using = "div.td")
	WebElement switchList;
	
	@FindBy(how = How.CSS, using = "div.stats.ng-scope")
	WebElement switchStats;
	
	@FindBy(how = How.CSS, using = "div#tr_cert_0")
	WebElement certsDetail;
	
	@FindBy(how = How.CSS, using = "div#tr_apps_0")
	WebElement appsDetail;
	
	@FindBy(how = How.CSS, using = "button.btn.btn-primary.btn-xs")
	WebElement addAdmin;
	
	/*Widget names for findElement(s) calls */
	String authSeedIcon = "span.icon-img-link.fa.fa-pencil";
	String seedList = "div[name=form]";
	String licenseList = "ng-transclude";
	String deleteIcon = "span.icon-img-link.fa fa-trash-o.ng-scope";
	String msgPopup = "span.fa.fa-times-circle";
	String addButtonCss = "button.btn.btn-sm.btn-primary";
	String switchListId = "div.td";

	public VcfSettingsPage(WebDriver driver) {
		super(driver);
	}
	
	public void vcfSettingsPage() {
		vcfSettingsIcon.click();
	}

	public void addSeedSwitch(String name , String usrname, String mgmtip, String pwd) {
		vcfSettingsIcon.click();
		waitForElementVisibility(addButton,1000);
		WebDriverWait myWaitVar = new WebDriverWait(driver,100);
		myWaitVar.until(ExpectedConditions.elementToBeClickable (By.cssSelector(addButtonCss)));
		addButton.click();
		setValue(mgmtIp,mgmtip);
		setValue(username,usrname);
		setValue(password,pwd);
		okButton.click();
		waitForElementVisibility(switchList,1000);
	}
	
	public void authSeedSwitches(String usrname, String pwd) throws InterruptedException {
		waitForElementVisibility(switchList,1000);
		List<WebElement> rows = new ArrayList();
		rows = driver.findElements(By.cssSelector(authSeedIcon));
		int i = 0;
		for (i = 0; i < rows.size(); i++) {
			if(i == 0) {
				rows.get(i).click();
			} else {
				rows = driver.findElements(By.cssSelector(authSeedIcon));
				rows.get(i).click();
			}
			waitForElementVisibility(username,1000);
			setValue(username,usrname);
			setValue(password,pwd);
			okButton.click();
			retryingFindClick(By.cssSelector(switchListId));
		}
		//closePopUp();
	}	

	public boolean verifySeedSwitch(String name , String usrname, String mgmtip, String pwd) {
		boolean status = false;
		waitForElementVisibility(switchList,100);
		List<WebElement> rows = new ArrayList();
		rows = driver.findElements(By.cssSelector(seedList));
		String rowTable = null;
	       for (WebElement row : rows) {
	    	   if (row.getText().contains(name)) {
		    	   com.jcabi.log.Logger.info("verifySeedSwitch", "Seed switch found:"+name);
	                rowTable = row.getText();
	                status = true;
	                break;
	            }
	        }
		return status;
	}

	public void sleepFunc (int sec) throws InterruptedException {
		Thread.sleep(sec*1000);	
	}	
	public void addDataNode(String name ,String host, String usrname, String size, String pwd) throws InterruptedException {
		vcfSettingsIcon.click();
		waitForElementVisibility(licenseTab,1000);
		dataNodeTab.click();
		sleepFunc(1);
		addButton.click();
		setValue(nodeName,name);
		setValue(nodeHost,host);	
		setValue(username,usrname);
		setValue(password,pwd);
		sudo.click();
		setValue(heapsize,size);
		okButton.click();
		sleepFunc(4);
		
		List<WebElement> rows = new ArrayList();
		rows = driver.findElements(By.cssSelector(seedList));
        	while(rows.size()==1){
			sleepFunc(4);
			rows = driver.findElements(By.cssSelector(seedList));
		}
		waitForElementVisibility(switchList,100);
		//closePopUp();
	}
	
	public boolean verifyDataNode(String host) {		
		boolean status = false;
		List<WebElement> rows = new ArrayList();
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rows = driver.findElements(By.cssSelector(seedList));
		String rowTable = null;
	        for (WebElement row : rows) {
	            if (row.getText().contains(host)) {
	                rowTable = row.getText();
	                status = true;
	                break;
	            }
	        }
		return status;
	}
	
	public void logintoPnc(String usrname, String pwd) {
		vcfSettingsIcon.click();
		waitForElementVisibility(licenseTab,1000);
		licenseTab.click();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pncCloudbutton.click();
		setValue(username,usrname);
		setValue(password,pwd);
		okButton.click();
		waitForElementVisibility(driver.findElement(By.tagName(licenseList)),1000);
	}

	public void activateLicense(String usrname, String pwd,LicenseTypes type) throws Exception {
	    logintoPnc(usrname , pwd);
	    Thread.sleep(2000); 
	    driver.navigate().refresh();
	    List<WebElement> rows = new ArrayList();
	     rows = driver.findElements(By.cssSelector("ng-transclude div.panel.panel-default"));
	     for (int i=0; i < rows.size(); i++) {
		    rows = driver.findElements(By.cssSelector("ng-transclude div.panel.panel-default"));
	            if (rows.get(i).getText().contains(type.toString())) {
	            	com.jcabi.log.Logger.info("activateLicense", "License to be selected:"+type.toString());
	            	//driver.findElements(By.cssSelector("ng-transclude div.panel.panel-default")).get(i).findElement(By.cssSelector("button.btn.btn-xs.btn-primary")).click();
	            	//rows.get(i).findElement(By.cssSelector("button.btn.btn-xs.btn-primary")).click();
	                retryingFindClick(rows.get(i),By.cssSelector("button.btn.btn-xs.btn-primary"));
	                break;
	            }
	     }
	     closePopUp();
	}
	
	public void closePopUp(){
		List <WebElement> popout = driver.findElements(By.cssSelector(msgPopup));
		if (popout.size() > 0) {
			driver.findElement(By.cssSelector(msgPopup)).click();
		}
	}
	
	public void removeLicense(LicenseTypes type) {
		vcfSettingsIcon.click();
		waitForElementVisibility(licenseTab,1000);
		licenseTab.click();
		waitForElementVisibility(driver.findElement(By.tagName(licenseList)),1000);
	    List<WebElement> rows = new ArrayList();
	     rows = driver.findElement(By.tagName(licenseList)).findElements(By.tagName("div"));
	        String rowTable = null;
	        for (WebElement row : rows) {
	            if (row.getText().contains(type.toString())) {
	                rowTable = row.getText();
	                row.findElement(By.cssSelector(deleteIcon)).click();
	                break;
	            }
	        }
	      closePopUp();
	}
	
	
	public void addAuthServer( String pwd, String basedn, String ldapmgmtdn, String ldapuserdnPatterns, String ldapuserSearchFilter, String ldapmanagerPass) {
		vcfSettingsIcon.click();
		waitForElementVisibility(switchMenu,1000);
		serverMenu.click();
		waitForElementVisibility(AddAuthServer,1000);
		AddAuthServer.click();
		setValue(baseDn,basedn);
		setValue(ldapManagerDn,ldapmgmtdn);
		setValue(ldapManagerPass,ldapmanagerPass);
		setValue(ldapUserDnPatterns,ldapuserdnPatterns);
		setValue(ldapUserSearchFilter,ldapuserSearchFilter);
	}
	
	public void navigateToSwitchMenu() {
		vcfSettingsIcon.click();
		waitForElementVisibility(switchMenu,1000);
		switchMenu.click();
		waitForElementVisibility(addButton,40);
	}
	
	public void navigateToSystemhealthMenu () {
		vcfSettingsIcon.click();
		waitForElementVisibility(switchMenu,1000);
		healthMenu.click();		
	}
	
	public void navigateToServerMenu() {
		vcfSettingsIcon.click();
		waitForElementVisibility(switchMenu,1000);
		serverMenu.click();
		waitForElementVisibility(AddAuthServer,40);
	}
	
	public String navigateToAppMenu() {
		vcfSettingsIcon.click();
		waitForElementVisibility(switchMenu,1000);
		appsMenu.click();
		waitForElementVisibility(appsDetail,40);
		return appsDetail.getText();
	}
	
	public String navigateTocertsMenu() {
		vcfSettingsIcon.click();
		waitForElementVisibility(switchMenu,1000);
		certsMenu.click();
		waitForElementVisibility(certsDetail,40);
		return certsDetail.getText();
	}
	
	public void navigateToadminMenu() {
		vcfSettingsIcon.click();
		waitForElementVisibility(switchMenu,1000);
		adminMenu.click();
		waitForElementVisibility(addAdmin,40);	
	}

}
