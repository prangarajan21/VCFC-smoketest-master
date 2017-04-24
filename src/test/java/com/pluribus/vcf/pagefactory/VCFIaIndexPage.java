package com.pluribus.vcf.pagefactory;
import com.jcabi.log.Logger;
import com.pluribus.vcf.helper.PageInfra;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.pluribus.vcf.helper.PageInfra;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.concurrent.TimeUnit;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;


public class VCFIaIndexPage extends PageInfra {

	@FindBy(how = How.CSS, using = "a.list-group-item.category.ia-dashboard-menu")
	WebElement dashboardIcon;
	
	@FindBy(how = How.CSS, using = "a.list-group-item.category.ia-config-menu")
	WebElement configIcon;
	
	@FindBy(how = How.CSS, using = "a.list-group-item.category.ia-tag-menu")
	WebElement tagIcon;
	
	@FindBy(how= How.CSS, using = "button.btn.btn-sm.btn-primary")
	WebElement addButton;
	
	@FindBy(how= How.CSS, using = "button.btn.btn-default.btn-sm")
	WebElement switchDropDown;
    
	@FindBy(how= How.CSS, using = "a#taggingOptions.btn.btn-default.dropdown-toggle")
	WebElement tagOptions;
	
	@FindBy(how = How.NAME, using = "username")
	WebElement userName;
	
	@FindBy(how = How.NAME, using = "name")
	WebElement name;
	
	@FindBy(how = How.NAME, using = "password")
	WebElement password;

	@FindBy(how = How.NAME, using = "ok")
	WebElement okButton;

	@FindBy(how = How.CSS, using = "div.metric-value.ng-binding")
	WebElement countIcons;
	
	@FindBy(how = How.CSS, using = "input[type = 'text']")
	WebElement searchBox;
	
	@FindBy(how= How.CSS, using = "button.btn.btn-primary")
	WebElement confirmOkButton;
	
	@FindBy(how= How.CSS, using = "div.panel-heading.mirror-head")
	WebElement collectorList;
	
	@FindBy(how= How.CSS, using = "span.switch")
	WebElement spanSwitch;
	
	/* Field names used for webdriver findElement*/
	String iframeTag = "iframe";
	String switchListName = "ul.dropdown-menu li";
	String insightCountWidget =  "div.metric-value.ng-binding";
	String inputTagName = "input";
	String srchString = "a[title=";
	String collectorListId = "div.panel-heading.mirror-head";
	String collectorAddButtons = "button.btn.btn-sm.btn-primary";
	String uploadTagStr = "Upload Tags";
	String clearTagStr = "Clear Tags";
	String fileUpload = "div.holder"; 
	String countIconsId = "div.metric-value.ng-binding";
	String toggleSwitch = "span.switch";
	String switchOnState = "span.toggle-bg.on";
	String switchOffState = "span.toggle-bg.off";
	String editIcon = "span.icon-img-link.fa.fa-pencil";
	String collButtonString = "Add Netvisor Collector";
	
	public VCFIaIndexPage(WebDriver driver) {
		super(driver);
	}
	
	public List getSwitchList() {
		List<WebElement> rows = new ArrayList();
		rows = driver.findElements(By.cssSelector(switchListName));
		return rows;
	}
	
	public void applySearchFilter(String searchString) {
		waitForElementVisibility(searchBox,100);
		setValue(searchBox,searchString);
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
		boolean existsOn = false;
		existsOn = (driver.findElements(By.cssSelector(srchString+"'"+searchString+"'")).size() != 0);
		driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
		if(existsOn) {
			driver.findElement(By.cssSelector(srchString+"'"+searchString+"'")).click();
		}
	}
	
	public List<WebElement> getInsightCount() {
		List<WebElement> rows = new ArrayList();
		dashboardIcon.click();
		waitForElementVisibility(driver.findElement(By.tagName(iframeTag)),1000);
		driver.switchTo().frame(driver.findElement(By.tagName(iframeTag)));	
		//retryingFindClick(By.cssSelector(countIconsId));
		waitForElementVisibility(countIcons,120);
		rows = driver.findElements(By.cssSelector(insightCountWidget));
		return rows;
	}
	
	public int getConnectionCount() {
		int connCount = 0;
		List <WebElement> rows = getInsightCount();
			if(!rows.isEmpty()) {
				String connOutput = rows.get(0).getText();
				if(StringUtils.contains(connOutput, ',')) {
					connOutput = StringUtils.remove(connOutput, ',');
				}
				if(connOutput.equals("")) {
					connCount = 0;
				} else {
					connCount = Integer.parseInt(connOutput);
				}
			}
			driver.switchTo().defaultContent();
		return connCount;
	}
	
	public int getAppCount() {
		int connCount = 0;
		List <WebElement> rows = getInsightCount();
			if(!rows.isEmpty()) {
				String connOutput = rows.get(1).getText();	
				if(StringUtils.contains(connOutput, ',')) {
					connOutput = StringUtils.remove(connOutput, ',');
				} 
				if(connOutput.equals("")) {
					connCount = 0;
				} else {
					connCount = Integer.parseInt(connOutput);
				}
			}
			driver.switchTo().defaultContent();	
		return connCount;
	}
	
    public boolean checkCollectorState(WebElement collector) {
    	driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
		boolean existsOn = false;
		existsOn = (collector.findElements(By.cssSelector(switchOnState)).size() != 0);
		driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
		return existsOn;	
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
	public boolean editCollector(String collName, String switchName) throws Exception{
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
						okButton.click();
						Thread.sleep(5000);
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
	
	/*Might need this when we add multiple collectors feature
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
	*/
	
	public void gotoIADashboard() {
		dashboardIcon.click();
		waitForElementVisibility(driver.findElement(By.tagName(iframeTag)),1000);
	}
	public void gotoIAConfig() {
		configIcon.click();
		waitForElementVisibility(collectorList,100);
	}
	
	public static void setClipboardData(String string) {
		//StringSelection is a class that can be used for copy and paste operations.
		StringSelection stringSelection = new StringSelection(string);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
	}
	
	public void uploadTag(String fileLocation) throws Exception{
		tagIcon.click();
		waitForElementVisibility(tagOptions,100);
		tagOptions.click();
		WebElement uploadTags = findAnchorTags(uploadTagStr);
		uploadTags.click();
		waitForElementVisibility(driver.findElement(By.cssSelector(fileUpload)),100);
		WebElement element = driver.findElement(By.cssSelector(fileUpload));
		element.click(); //Click on fileUpload
		setClipboardData(fileLocation);
		 Robot robot = new Robot();
		 robot.keyPress(KeyEvent.VK_CONTROL);
         robot.keyPress(KeyEvent.VK_V);
         robot.keyRelease(KeyEvent.VK_V);
         robot.keyRelease(KeyEvent.VK_CONTROL);
         robot.keyPress(KeyEvent.VK_ENTER);
         robot.keyRelease(KeyEvent.VK_ENTER);
         
         WebDriverWait myWaitVar = new WebDriverWait(driver,1000);
		 myWaitVar.until(ExpectedConditions.elementToBeClickable(okButton));
		 waitForElementVisibility(okButton,100);
		// To click on the submit button (Not the browse button)
		 okButton.click();
		//String checkText = driver.findElement(By.id("message")).getText();
		//Assert.assertEquals("File uploaded successfully", checkText);	
	}
	
	
	public WebElement findAnchorTags(String anchorText) {
		List <WebElement> anchorTags = driver.findElements(By.cssSelector("a"));
		WebElement returnRow = null;
		for (WebElement row:anchorTags) {
			if(row.getText().equalsIgnoreCase(anchorText)) {
				returnRow = row;
				break;
			}
		}
		return returnRow;
	}
	
}
