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

public class VCFIaIndexPage extends PageInfra {

	private static final Object source = null;

	@FindBy(how = How.CSS, using = "a.list-group-item.category.ia-dashboard-menu")
	WebElement dashboardIcon;
	
	@FindBy(how = How.CSS, using = "a.list-group-item.category.ia-config-menu")
	WebElement configIcon;
	
	@FindBy(how= How.CSS, using = "button.btn.btn-primary.btn-xs")
	WebElement addButton;
	
	@FindBy(how= How.CSS, using = "button.btn.btn-default.btn-sm")
	WebElement switchDropDown;
    
	@FindBy(how = How.NAME, using = "username")
	WebElement userName;
	
	@FindBy(how = How.NAME, using = "password")
	WebElement password;

	@FindBy(how = How.NAME, using = "ok")
	WebElement okButton;
	
	@FindBy(how = How.ID, using = "tr_switch_0")
	WebElement collectorList;

	@FindBy(how = How.CSS, using = "div.metric-value.ng-binding")
	WebElement countIcons;
	
	@FindBy(how = How.CSS, using = "input[type = 'text']")
	WebElement searchBox;
	
	
	/* Field names used for webdriver findElement*/
	String iframeTag = "iframe";
	String switchListName = "ul.dropdown-menu";
	String insightCountWidget =  "div.metric-value.ng-binding";
	String inputTagName = "input";
	String srchString = "a[title=";
	
	public VCFIaIndexPage(WebDriver driver) {
		super(driver);
	}
	
	public List getSwitchList() {
		List<WebElement> rows = new ArrayList();
		rows = driver.findElements(By.cssSelector(switchListName));
		return rows;
	}
	
	public void applySearchFilter(String searchString) {
		waitForElementVisibility(searchBox,1000);
		setValue(searchBox,searchString);
		WebElement searchItem = driver.findElement(By.cssSelector(srchString+"'"+searchString+"'"));
		if(searchItem.isDisplayed()) {
			searchItem.click();
		}
	}
	
	public List<WebElement> getInsightCount() {
		List<WebElement> rows = new ArrayList();
		driver.navigate().refresh();
		driver.switchTo().frame(driver.findElement(By.tagName(iframeTag)));			
		waitForElementVisibility(countIcons,100);
		rows = driver.findElements(By.cssSelector(insightCountWidget));
		return rows;
	}
	
	public int getConnectionCount() {
		int connCount = 0;
		List <WebElement> rows = getInsightCount();
			if(!rows.isEmpty()) {
				String connOutput = rows.get(0).getText();
				if(StringUtils.contains(connOutput, ',')) {
					StringUtils.remove(connOutput, ',');
				}
				connCount = Integer.parseInt(connOutput);
			}
			driver.switchTo().defaultContent();
		return connCount;
	}
	
	public int getAppCount() {
		int connCount = 0;
		List <WebElement> rows = getInsightCount();
			if(!rows.isEmpty()) {
				String connOutput = rows.get(1).getText();
				StringUtils.remove(connOutput, ',');
				connCount = Integer.parseInt(connOutput);
			}
			driver.switchTo().defaultContent();	
		return connCount;
	}
	
	public boolean isCollectorConfigured() {
		boolean isColl = false;
		if(collectorList.isDisplayed()) {
			isColl = true;
			} 
		else {
			isColl = false;
		}
		return isColl;
	}
	
	public void addCollector(String switchName, String user, String pwd) {
		configIcon.click();
		boolean status = isCollectorConfigured();
		if(status==false) {
			waitForElementVisibility(addButton,1000);
			addButton.click();
			waitForElementVisibility(switchDropDown,1000);
			switchDropDown.click();
			List <WebElement> rows = getSwitchList();
				for (WebElement row : rows) {
					if(row.getAttribute("outerText").contains(switchName)) {
						row.click();
						break;
					}
				}	
				setValue(userName,user);
				setValue(password,pwd);
				okButton.click();
				waitForElementVisibility(collectorList,1000);
		}
	}
	
	public void gotoIADashboard() {
		dashboardIcon.click();
	}
}
