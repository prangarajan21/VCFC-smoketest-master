package com.pluribus.vcf.pagefactory;

import com.pluribus.vcf.helper.PageInfra;

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

public class VCFPaIndexPage extends PageInfra {
	@FindBy(how = How.CSS, using = "a.list-group-item.category.pa-dashboard-menu")
	WebElement dashboardIcon;
	
	@FindBy(how = How.CSS, using = "a.list-group-item.category.pcap-engine-menu")
	WebElement configIcon;
	
	@FindBy(how = How.CSS, using = "a.list-group-item.category.vflow-menu")
	WebElement vFlowConfig;
	
	@FindBy(how = How.CSS, using = "div.modal-body")
	WebElement pcapAddMenu;
	
	@FindBy(how= How.CSS, using = "button.btn.btn-sm.btn-primary")
	WebElement addButton;
	
	@FindBy(how = How.NAME, using = "name")
	WebElement name;
		
	@FindBy(how = How.NAME, using = "ip")
	WebElement ip;
	
	@FindBy(how = How.NAME, using = "port")
	WebElement port;

	@FindBy(how = How.NAME, using = "ok")
	WebElement okButton;
	
	@FindBy(how = How.ID, using = "selectedInport")
	WebElement inPortText;
	
	@FindBy(how = How.ID, using = "selectedOutport")
	WebElement outPortText;
	
	/* Names for findElement(s) methods */
	String switchListName = "ul.dropdown-menu";
	String dropdownName = "button.btn.btn-default.btn-sm";
	
	public VCFPaIndexPage(WebDriver driver) {
		super(driver);
	}
	
	public List getSwitchList() {
		List<WebElement> rows = new ArrayList();
		rows = driver.findElements(By.cssSelector(switchListName));
		return rows;
	}
	
	public List getDropDownButtons() {
		List<WebElement> rows = new ArrayList();
		rows = driver.findElements(By.cssSelector(dropdownName));
		return rows;
	}
	
	public void addLocalPcap(String pcapName, String hostIp) {
		waitForElementVisibility(addButton,1000);
		addButton.click();
		waitForElementVisibility(pcapAddMenu,100);
		setValue(name,pcapName);
		setValue(ip,hostIp);
		setValue(port,"8080");
		okButton.click();
	}	
	
	public void addVFlow(String flowName,String switchName, String inPort, String outPort, String pcapName) {
		waitForElementVisibility(addButton,1000);
		addButton.click();
		setValue(name,flowName);
		List <WebElement> dds = getDropDownButtons();
		dds.get(0).click();
		List <WebElement> rows = getSwitchList();
		for (WebElement row : rows) {
			if(row.getAttribute("outerText").contains(switchName)) {
				row.click();
				break;
			}
		}
		setValue(inPortText,inPort);
		setValue(outPortText,outPort);
		dds.get(1).click();
		rows = getSwitchList();
		for (WebElement row : rows) {
			if(row.getAttribute("outerText").contains("60")) {
				row.click();
				break;
			}
		}
		dds.get(2).click();
		rows = getSwitchList();
		for (WebElement row : rows) {
			if(row.getAttribute("outerText").contains(pcapName)) {
				row.click();
				break;
			}
		}
		okButton.click();
	}
	
	public boolean verifyPcapAdd(String pcapName) {
		boolean status = false;
		
		return status;
	}
	
	public void gotoPADashboard() {
		dashboardIcon.click();
	}

}
