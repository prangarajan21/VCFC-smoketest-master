package com.pluribus.vcf.pagefactory;

import com.pluribus.vcf.helper.PageInfra;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
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

	@FindBy(how = How.XPATH, using = ".//a[contains(@href,'/vcf-center/collector')]")
	WebElement collectorMgmtTab;

	@FindBy(how = How.CSS, using = "span.fa-stack-1x.pnc-text")
	WebElement pncCloudbutton;
	
	@FindBy(how = How.ID, using = "username")
	WebElement username;
	
	@FindBy(how = How.NAME, using = "name")
	WebElement name;
	
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
	
	@FindBy(how = How.CSS, using = "div [ui-view]")
	WebElement healthDetail;
	
	@FindBy(how = How.CSS, using = "div#tr_cert_0")
	WebElement certsDetail;
	
	@FindBy(how = How.CSS, using = "div#tr_apps_0")
	WebElement appsDetail;
	
	@FindBy(how = How.CSS, using = "button.btn.btn-primary.btn-xs")
	WebElement addAdmin;
	
	@FindBy(how = How.CSS, using = "ng-transclude")
	WebElement listLicense;
	
	@FindBy(how= How.CSS, using = "button.btn.btn-default.btn-sm")
	WebElement switchDropDown;
	
	@FindBy(how= How.CSS, using = "div.panel-heading.mirror-head")
	WebElement collectorList;
	
	@FindBy(how= How.CSS, using = "span.switch")
	WebElement spanSwitch;
	
	@FindBy(how= How.CSS, using = "button.btn.btn-primary")
	WebElement confirmOkButton;
	
	@FindBy(how= How.CSS, using = "table-pane")
	WebElement defaultCollectorList;
	
	/*Widget names for findElement(s) calls */
	String authSeedIcon = "span.icon-img-link.fa.fa-pencil";
	String seedList = "div[name=form]";
	String licenseList = "ng-transclude";
	String deleteIcon = "span.icon-img-link.fa fa-trash-o.ng-scope";
	String msgPopup = "button.close";
	String addButtonCss = "button.btn.btn-sm.btn-primary";
	String switchListId = "div.td";
	String instLicKey = "button.btn-sm.btn-primary";
	String keyTextBox = "key";
	String collectorListId = "div.panel-heading.mirror-head";
	String collectorAddButtons = "button.btn.btn-sm.btn-primary";
	String toggleSwitch = "span.switch";
	String switchOnState = "span.toggle-bg.on";
	String switchOffState = "span.toggle-bg.off";
	String editIcon = "span.icon-img-link.fa.fa-pencil";
	String collButtonString = "Add Netvisor Collector";
	String switchListName = "ul.dropdown-menu li";
	String licenseActivateButton = "button.btn.btn-xs.btn-primary";
	String seedSwitchAddMsgBox = "div.modal-dialog";
	
	public VcfSettingsPage(WebDriver driver) {
		super(driver);
	}
	
	public void vcfSettingsPage() {
		vcfSettingsIcon.click();
	}

	public void addSeedSwitch(String name , String usrname, String mgmtip, String pwd) throws Exception{
		vcfSettingsIcon.click();
		waitForElementVisibility(addButton,1000);
		waitForElementToClick(By.cssSelector(addButtonCss),100);
		Actions actions = new Actions(driver);
    	actions.moveToElement(addButton).perform();
    	//
    	//addButton.click();
		retryingFindClick(addButton);
		waitForElementVisibility(driver.findElement(By.cssSelector(seedSwitchAddMsgBox)),100);
		//Thread.sleep(2000);
		setValue(mgmtIp,mgmtip);
		Thread.sleep(2000);
		setValue(username,usrname);
		Thread.sleep(2000);
		setValue(password,pwd);
		Thread.sleep(2000);
		waitForElementVisibility(okButton,100);
		retryingFindClick(okButton);
		//okButton.click();
		Thread.sleep(5000);
		waitForElementVisibility(switchList,1000);
	}

	public List getSwitchList() {
		List<WebElement> rows = new ArrayList();
		rows = driver.findElements(By.cssSelector(switchListName));
		return rows;
	}

	public boolean isCollectorConfigured(String collName) {
		boolean isColl = false;
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
		boolean exists = (driver.findElements(By.cssSelector(collectorListId)).size() != 0);
		//List<WebElement> collCount = driver.findElements(By.cssSelector(collectorListId));
		driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
		if(exists) {
			    List <WebElement> collector = driver.findElements(By.cssSelector(collectorListId));
			    for (WebElement row:collector) {
			    	if(row.getText().contains(collName)) {
			    		isColl = true;
			    		com.jcabi.log.Logger.info("collectorConfigured","Collector List:"+row.getText());
			    	}
			    }	    
		}
		return isColl;
	}
	
	public boolean checkCollectorState(WebElement collector) {
	    	driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
			boolean existsOn = false;
			existsOn = (collector.findElements(By.cssSelector(switchOnState)).size() != 0);
			driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
			return existsOn;	
	}
	
	public boolean editCollector(String collName, String switchName) throws Exception{
		vcfSettingsIcon.click();
		waitForElementVisibility(collectorMgmtTab,100);
		collectorMgmtTab.click();
		waitForElementVisibility(collectorList,100);
		boolean status = false;
		status = isCollectorConfigured(collName);
		if(status) {
			List <WebElement> collList = driver.findElements(By.cssSelector(collectorListId));
			for (WebElement coll: collList) {
				if(coll.getText().contains(collName)) {
					boolean currentState = checkCollectorState(coll);
					if(currentState == false) {
						coll.findElement(By.cssSelector(editIcon)).click();
						switchDropDown.click();
						List <WebElement> rows = getSwitchList();
						for (WebElement row : rows) {
							if(row.getText().contains(switchName)) {
								row.click();
								break;
							}
						}
						retryingFindClick(okButton);
						Thread.sleep(10000);
						waitForElementVisibility(driver.findElement(By.cssSelector(toggleSwitch)),100);
						status = true;
						break;
					} else {
						return true; //No need to edit since the collector is already in running state. Edit will fail at this point.
					}
				}
			}
		} 
		return status;
	}
	
	
	public boolean toggleCollState(String collName, boolean expState) throws Exception{
    	boolean status = false;
		boolean currentState = false;
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
		boolean exists = (driver.findElements(By.cssSelector(collectorListId)).size() != 0);
		driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
		if(exists) {
			List <WebElement> collList = driver.findElements(By.cssSelector(collectorListId));
			for (WebElement coll: collList) {
				if(coll.getText().contains(collName)) {
					currentState = checkCollectorState(coll); //findCurrentState of the switch
					if(currentState != expState) {
						coll.findElement(By.cssSelector(toggleSwitch)).click();
						waitForElementVisibility(confirmOkButton,100);
						confirmOkButton.click();
						Thread.sleep(5000); //waiting for the toggle to go through
						waitForElementVisibility(collectorList,100);
						waitForElementToClick(By.cssSelector(collectorListId),100);
					}
					if(expState == checkCollectorState(coll)) status = true;
				}
				break;
			}
		} else {
			com.jcabi.log.Logger.error("toggleCollectorState","No collector configured!");
		}
		return status;
    }

	public boolean addCollector(String collName, String switchName, String user, String pwd) {
		boolean status = false;
		status = isCollectorConfigured(collName);
		if(status==false) {	
			try {
			Thread.sleep(5000);
			}catch(Exception e){
				System.out.println(e.toString());			
			}
			int i = 0;
			List<WebElement> rows = driver.findElements(By.cssSelector(collectorAddButtons));
			for (WebElement row: rows) {
				if(rows.get(i).getText().contains(collButtonString)) {
					retryingFindClick(rows.get(i));
				}
				i++;
			} 
			setValue(name,collName);
			waitForElementVisibility(switchDropDown,1000);
			switchDropDown.click();
			rows = getSwitchList();
				for (WebElement row : rows) {
					if(row.getText().contains(switchName)) {
						row.click();
						break;
					}
				}
				okButton.click();
				waitForElementVisibility(spanSwitch,100);
				waitForElementToClick(By.cssSelector(toggleSwitch),100);
				status = isCollectorConfigured(collName);
		}
		return status;
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
			Thread.sleep(5000); //sleeping for authorization to take place
			waitForElementToClick(By.cssSelector(switchListId),100);
			//retryingFindClick(By.cssSelector(switchListId));
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
	
	public boolean installLicenseKey(String licenseKey) throws Exception{
		vcfSettingsIcon.click();
		waitForElementVisibility(licenseTab,1000);
		licenseTab.click();
		driver.findElement(By.cssSelector(instLicKey)).click();
		waitForElementVisibility(driver.findElement(By.name("form")),100);
		setValue(driver.findElement(By.id("key")),licenseKey);
		okButton.click();
		Thread.sleep(10000);
		return true;
	}
	
	public void logintoPnc(String usrname, String pwd) throws Exception {
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
		Thread.sleep(2000);
		setValue(username,usrname);
		Thread.sleep(2000);
		setValue(password,pwd);
		Thread.sleep(2000);
		okButton.click();
		Thread.sleep(2000);
		waitForElementVisibility(driver.findElement(By.tagName(licenseList)),100);
		waitForElementToClick(By.tagName(licenseList),100);
	}

	public boolean activateLicense(String usrname, String pwd,LicenseTypes type) throws Exception {
	    boolean status = false;
		logintoPnc(usrname , pwd);
	    Thread.sleep(10000); 
	    //driver.navigate().refresh();
	    waitForElementVisibility(listLicense,100);
	    List<WebElement> rows = new ArrayList();
	     rows = driver.findElements(By.cssSelector("ng-transclude div.panel.panel-default"));
	     for (int i=0; i < rows.size(); i++) {
		    rows = driver.findElements(By.cssSelector("ng-transclude div.panel.panel-default"));
	            if (rows.get(i).getText().contains(type.toString())) {
	            	com.jcabi.log.Logger.info("activateLicense", "License to be selected:"+type.toString());
	            	Actions actions = new Actions(driver);
	            	actions.moveToElement(rows.get(i)).perform();
	            	retryingFindClick(rows.get(i),By.cssSelector(licenseActivateButton));
	                status = true;
	                Thread.sleep(5000); 
	                break;
	            }
	     }
	     if(status == false) {
	    	 com.jcabi.log.Logger.error("activateLicense","Couldn't find license"+type.toString()+" for activation");
	     } else {
	    	 closePopUp();
	     } 
	    return status;
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
	        waitForElementToClick(By.cssSelector(msgPopup),100);
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
		waitForElementVisibility(healthDetail,100);
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
