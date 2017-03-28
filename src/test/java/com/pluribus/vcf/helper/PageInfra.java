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

        // This waits up to 10 seconds before throwing a TimeoutException or 
        // if it finds the element will return it in 0 - 10 seconds. 
        // WebDriverWait by default calls the ExpectedCondition every 500 milliseconds 
        // until it returns successfully. 
        // A successful return is for ExpectedCondition type is Boolean 
        // return true or not null return value for all other ExpectedCondition types.

        /* for (int i = 0; i < 60; i++) {
         if (isElementPresent(locator)) {
         break;
         } else {
         try {
         synchronized (driver) {
         driver.wait(1000);
         }
         } catch (InterruptedException e) {
         e.printStackTrace();
         }
         }
         } */
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

    public WebElement waitForElementToClick(final By locator) {
        return (new WebDriverWait(driver, 25))
                .until(ExpectedConditions.elementToBeClickable(locator));
    }
    
    public boolean retryingFindClick(By by) {
    	 boolean result = false;
         int attempts = 0;
         while(attempts < 10) {
             try {
         		driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
            	 boolean exists = (driver.findElements(by)).size() != 0;
            	 if(exists) {
            		 result = true;
            		 break;
            	 } else {
            		 new WebDriverWait(driver,30);
            	 }
             } catch(Exception e) {
             }
             attempts++;
         }
 		 driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
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

    /*
     WebElement element = driver.findElement(By.id("foo"));
     String name = (String) ((JavascriptExecutor) driver).executeScript(
     "return arguments[0].tagName", element);
     */
    public ResourceBundle getBundle() {
        return rb;
    }

}
