package com.pluribus.vcf.helper;

import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PageInfra {
    protected WebDriver driver;
    protected ResourceBundle rb;
    protected Select select;


    public PageInfra(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    public void setValue(WebElement field, String strUserName) {
        field.clear();
        field.sendKeys(strUserName);
    }

    public void setValue(WebElement field, String value, Keys key) {
        field.clear();
        field.sendKeys(value, key);
    }  
    
    public void selectElement(WebElement field, String value) {
    	Select mySelect= new Select(field);
    	List<WebElement> options = mySelect.getOptions();
    	for (WebElement option : options) {
    	    if (option.getText().equalsIgnoreCase(value)) {
    	        option.click();
    	    }
    	}
    }
    public void moveToElements(WebElement field,WebElement field2) {
        Actions builder = new Actions(driver);
        waitForElementVisibility(field, 50);
        builder.moveToElement(field).moveToElement(field2).build().perform();
    }
    
    public void clickOnElements(WebElement field,WebElement field2) {
        Actions builder = new Actions(driver);
        waitForElementVisibility(field, 50);
        builder.moveToElement(field).click(field2).build().perform();
    }
   
    public void waitForElementPresent(By locator) {
        WebElement el
                = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public Boolean waitForElementStaleUp(WebElement el, int time) {
        return (new WebDriverWait(driver, time)).until(ExpectedConditions.stalenessOf(el));
    }

    public WebElement waitForElementVisibility(WebElement el, int time) {
        return (new WebDriverWait(driver, time))
                .until(ExpectedConditions.visibilityOf(el));
    }

    public WebElement waitForElementVisibility(WebElement el) {
        return (new WebDriverWait(driver, 15))
                .until(ExpectedConditions.visibilityOf(el));
    }

    public WebElement waitForElementToClick(final By locator,int waitTime) {
        return (new WebDriverWait(driver, waitTime))
                .until(ExpectedConditions.elementToBeClickable(locator));
    }
    
    public boolean retryingFindClick(By by) {
    	 boolean result = false;
         int attempts = 0;
         while(attempts < 10) {
             try {
            	 boolean exists = (driver.findElements(by)).size() != 0;
            	 if(exists) {
            		 result = true;
            		 break;
            	 } else {
            		 //WebDriver wait = new WebDriverWait(driver,30);
            	 }
             } catch(Exception e) {
             }
             attempts++;
         }
         return result;
     }
    
    public boolean retryingFindClick (WebElement el) {
    	boolean result = false;
    	int attempts = 0;
        while(attempts < 10) {
        	try {
        		el.click();
        		result = true;
        		break;
        	} catch (Exception e) {
        	}
        	attempts++;
        }
        return result;
    }
    
    public boolean retryingFindClick(WebElement el, By by) {
        boolean result = false;
        int attempts = 0;
        while(attempts < 10) {
            try {
            		el.findElement(by).click();
                    result = true;
                    break;
            } catch(Exception e) {
            }
            attempts++;
        }
        return result;
    }
    
    public ExpectedCondition<WebElement> visibilityOfElementLocated(final By locator) {
        return new ExpectedCondition<WebElement>() {
            public WebElement apply(WebDriver driver) {
                WebElement toReturn = driver.findElement(locator);
                if (toReturn.isDisplayed()) {
                    return toReturn;
                }
                return null;
            }
        };
    }

    public ResourceBundle getBundle() {
        return rb;
    }
}
