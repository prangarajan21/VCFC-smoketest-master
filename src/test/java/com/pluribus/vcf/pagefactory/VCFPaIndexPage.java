package com.pluribus.vcf.pagefactory;

import com.jcabi.ssh.SSHByPassword;
import com.jcabi.ssh.Shell;
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
	
	@FindBy(how= How.CSS, using = "button.btn.btn-primary.btn-sm")
	WebElement fetchButton;
	
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
	
	@FindBy(how = How.CSS, using = "div.col-sm-9")
	WebElement interfaceList;
	
	@FindBy(how = How.CSS, using = "div.table.my-table.case-list")
	WebElement pcapList;
	
	/* Names for findElement(s) methods */
	String switchListName = "ul.dropdown-menu";
	String dropdownName = "button.btn.btn-default.btn-sm";
	String lblCheckBox = "label.checkbox span.add- span";
	String checkBox = "label.checkbox input";
	
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
	
	public String getEth1Ip(String hostIp) {
		String eth1Ip = null;
		try {
			Shell sh1 = new Shell.Verbose(
					new SSHByPassword(
							hostIp,
							22,
							"vcf",
							"changeme"
					)
	        );
			String out1 = new Shell.Plain(sh1).exec("ifconfig eth1 | grep 'inet addr:' | cut -d: -f2 | awk '{ print $1}'");
			eth1Ip = out1.trim();
		}
		catch(Exception e) {
		}
		return eth1Ip;
	}
	public void addLocalPcap(String pcapName, String hostIp) {
		String eth0Ip = hostIp;
		String eth1Ip = getEth1Ip(hostIp); 
		configIcon.click();
		waitForElementVisibility(addButton,1000);
		addButton.click();
		waitForElementVisibility(pcapAddMenu,100);
		setValue(name,pcapName);
		setValue(ip,eth1Ip);
		setValue(port,"8080");
		fetchButton.click();
		waitForElementVisibility(interfaceList,100);
		List <WebElement> ifNames = driver.findElements(By.cssSelector(lblCheckBox));
		List <WebElement> checkBoxes = driver.findElements(By.cssSelector(checkBox));
		int index = 0;
		int hitIdx = 0;
		for (WebElement row: ifNames) {
			if(row.getText().contains("eth0")) {
				checkBoxes.get(index).click();
				hitIdx += 1;
			}
			if(row.getText().contains("eth1")) {
				checkBoxes.get(index).click();
				hitIdx += 1;
			}
			if(hitIdx == 2) break;
			index++;
		}
		okButton.click();
		waitForElementVisibility(pcapList,100);
	}	
	
	public boolean verifyPcap(String pcapName) {
		boolean status = false; 
		return status;
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
	
	public void gotoPADashboard() {
		dashboardIcon.click();
	}

}
