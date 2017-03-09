package com.pluribus.vcf.pagefactory;

import com.pluribus.vcf.helper.PageInfra;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class VCFPaIndexPage extends PageInfra {
	@FindBy(how = How.CSS, using = "a.list-group-item.category.pa-dashboard-menu.active")
	WebElement dashboardIcon;
	
	@FindBy(how = How.CSS, using = "a.list-group-item.category.pcap-engine-menu.active")
	WebElement configIcon;
	
	@FindBy(how= How.CSS, using = "button.btn.btn-sm.btn-primary")
	WebElement AddButton;
		
	@FindBy(how = How.NAME, using = "name")
	WebElement Name;
		
	@FindBy(how = How.NAME, using = "ip")
	WebElement Ip;
	
	@FindBy(how = How.NAME, using = "port")
	WebElement Port;

	public VCFPaIndexPage(WebDriver driver) {
		super(driver);
	}
/*	
	public int addpCapEngine(String collName, String ip, String username, String password, int sudo) {
		
	}	
*/

}
