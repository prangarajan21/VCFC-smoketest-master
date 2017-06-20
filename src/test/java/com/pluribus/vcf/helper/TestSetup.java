package com.pluribus.vcf.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.HashMap;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Optional;
import static org.testng.Assert.assertTrue;
import com.browserstack.local.Local;
import com.jcabi.ssh.Shell;
import com.pluribus.vcf.test.IATest;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.jcabi.ssh.SSHByPassword;


/**
 *
 * @author Haritha
 */
public class TestSetup {
   private RemoteWebDriver driver;
   private ResourceBundle bundle;
   Local bsLocal = new Local();
   private String localId;
   
   @Parameters({"vcfIp","upgrade","git_revision","vcfc_version","buildNum"})
   @BeforeSuite(alwaysRun = true)
   public void upgradeVCFC(String vcfIp,@Optional("0")String upgrade, @Optional("")String git_revision, @Optional("")String vcfc_version,@Optional("")String build_number) throws IOException,InterruptedException {
	   if(Integer.parseInt(upgrade) == 1) {
		Shell sh1 = new Shell.Verbose(
	            new SSHByPassword(
	                vcfIp,
	                22,
	                "vcf",
	                "changeme"
	            )
	        );
		String out1;
		String imageName = "vcf-"+vcfc_version+"-"+git_revision+".tgz";
		String imageUrl = "http://sandy:8081/artifactory/Maestro/VCFC_Upgrade/"+build_number+"/"+imageName;
		int out2 = new Shell.Empty(sh1).exec("cd /srv/docker/offline_images;wget "+imageUrl);
		//out1 = new Shell.Plain(sh1).exec("cd /srv/docker/offline_images;wget "+imageUrl);
		out1 = new Shell.Plain(sh1).exec("/home/vcf/VCFC_setup.sh upgrade "+imageName);
		Thread.sleep(30000); //Sleeping after upgrade
	   }
   }
   
   @Parameters({"vcfIp","clean"})
   @BeforeTest(alwaysRun = true)
   public void cleanLogs(String vcfIp,@Optional("1")String clean) throws IOException,InterruptedException {
	   if(Integer.parseInt(clean) == 1) {
		Shell sh1 = getVcfShell(vcfIp);
		String out1;
		out1 = new Shell.Plain(sh1).exec("rm  /home/vcf/srv/vcf/config/flowfilters.json");	
		out1 = new Shell.Plain(sh1).exec("rm  /home/vcf/srv/vcf/config/pcap-agent.properties");	
		out1 = new Shell.Plain(sh1).exec("rm  /home/vcf/srv/vcf/config/pcap-engine.json");
		out1 = new Shell.Plain(sh1).exec("rm  /home/vcf/srv/vcf/config/vcf-center.properties");
		out1 = new Shell.Plain(sh1).exec("rm  /home/vcf/srv/vcf/config/pcap_agents.properties");
		out1 = new Shell.Plain(sh1).exec("rm  /home/vcf/srv/vcf/config/pcap-file.json");
		out1 = new Shell.Plain(sh1).exec("rm  /home/vcf/srv/vcf/config/switch-details.json");
		out1 = new Shell.Plain(sh1).exec("rm  /home/vcf/srv/vcf/config/vcf-license.json");
		out1 = new Shell.Plain(sh1).exec("rm  /home/vcf/srv/vcf/config/vcf-user.properties");
		out1 = new Shell.Plain(sh1).exec("rm  /home/vcf/srv/vcf/config/vcf-maestro.properties");	
		out1 = new Shell.Plain(sh1).exec("rm  /home/vcf/srv/vcf/config/es_nodes.properties");	
		out1 = new Shell.Plain(sh1).exec("rm  /home/vcf/srv/vcf/config/es_node.json");	
		out1 = new Shell.Plain(sh1).exec("rm  /home/vcf/srv/vcf/config/vcf-collector.json");
		out1 = new Shell.Plain(sh1).exec("rm -r /home/vcf/var/vcf/data/vcf-es-cluster1");
		out1 = new Shell.Plain(sh1).exec("/home/vcf/srv/vcf/bin/stop-vcfc.sh");
		Thread.sleep(10000);
		out1 = new Shell.Plain(sh1).exec("/home/vcf/srv/vcf/bin/start-vcfc.sh");
		Thread.sleep(30000);
	}
   }
   
   public Shell getVcfShell(String vcfIp) {
	   Shell sh1 = null;
	   try{
		   sh1 = new Shell.Verbose(
				   new SSHByPassword(
	               vcfIp,
	               22,
	               "vcf",
	               "changeme"
	            )
	        );
	   } catch(Exception e) {
		   printLogs("error","getVcfShell",e.toString());
	   }
	   return sh1;
   }
   
   /*Routine for logging. So any changes to this routine will be a single point of edit for all log messages */
   public void printLogs (String level, String msg1, String msg2) {
	   if(level.equalsIgnoreCase("error")) {
		   com.jcabi.log.Logger.error(msg1,msg2);
	   } 
	   if(level.equalsIgnoreCase("info")) {
		   com.jcabi.log.Logger.info(msg1, msg2);
	   }
	   System.out.println(level+": "+ msg1 +" "+msg2);
   }
   
   @Parameters({"vcfIp","browser","local","bsUserId","bsKey","jenkins"}) 
   @BeforeClass(alwaysRun = true)
   public void initDriver(String vcfIp, String browser, @Optional("0")String local,@Optional("pratikdam1")String bsUserId, @Optional("uZCXEzKXwgzgzMr3G7R6") String bsKey,@Optional("0")String jenkins) throws Exception{
	   if(Integer.parseInt(local)==1) {
		   startDriver(vcfIp,browser);
	   }
	   else {
		   startDriver(vcfIp,browser,bsUserId,bsKey,Integer.parseInt(jenkins)); //Call browserstack test session
	   }
   }
   
   public void startDriver(String vcfIp,String browserName) {
	   if(browserName.equalsIgnoreCase("chrome")) {
	   		System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver");
	   		DesiredCapabilities handlSSLErr = DesiredCapabilities.chrome();     
	   		handlSSLErr.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
	   		driver=new ChromeDriver(handlSSLErr);
	   		driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
			driver.manage().window().maximize();
			driver.get("https:"+vcfIp);
	   } else if (browserName.equalsIgnoreCase("firefox")) {
		    System.setProperty("webdriver.gecko.driver","src/test/resources/geckodriver");
	   		DesiredCapabilities caps = DesiredCapabilities.firefox();
	   	    caps.setCapability("marionette", true);
	   	    caps.setCapability("acceptInsecureCerts",true);
	   	    //var capabilities = new FirefoxOptions().addTo(caps);
	   	    System.out.println("Capabilities:"+caps.toString());
		    driver = new FirefoxDriver(caps);
	   		driver.get("https:"+vcfIp);
	   		System.out.println("title"+driver.getTitle());
	   }
	   //Firefox local doesn't work due to a bug. TODO: ADD IE AND SAFARI TO THE LIST
   }
   
    public void startDriver(String vcfIp,String browser,String bsUserId, String bsKey,int jenkins) throws Exception {
		String sessionId = null;
		String command = null;
		String logFileName = "/tmp/browserstack/BSlogs.txt";		
		HashMap<String,String> bsLocalArgs = new HashMap<String,String>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMddhhmmss");
		String dateAsString = simpleDateFormat.format(new Date());
		String localId = "convergenceTest"+dateAsString;
		if(jenkins == 0) {
			bsLocalArgs.put("-log-file", logFileName);
			bsLocalArgs.put("localIdentifier",localId); //environment variable
			bsLocalArgs.put("key",bsKey); //BrowserStack Key
			bsLocalArgs.put("v", "true"); 
			bsLocal.start(bsLocalArgs); 
		}
		 
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("browser",browser);
		caps.setCapability("build", "VCFC SmokeTest Cases");
		caps.setCapability("acceptSslCerts","true");
		caps.setCapability("browserstack.debug","true");
		caps.setCapability("browserstack.idleTimeout","150");
		caps.setCapability("platform","ANY");
		if(jenkins ==0) {
			caps.setCapability("browserstack.local", "true");	
			caps.setCapability("browserstack.localIdentifier",localId);
		} else {
			String browserstackLocal = System.getenv("BROWSERSTACK_LOCAL");
			String browserstackLocalIdentifier = System.getenv("BROWSERSTACK_LOCAL_IDENTIFIER");
			caps.setCapability("browserstack.local", browserstackLocal);
			caps.setCapability("browserstack.localIdentifier", browserstackLocalIdentifier);
		}
		driver = new RemoteWebDriver(
			      new URL("https://"+bsUserId+":"+bsKey+"@hub-cloud.browserstack.com/wd/hub"),
			      caps
			    );
		driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
        // Get a handle to the driver. This will throw an exception if a matching driver cannot be located
	    driver.get("https://"+ vcfIp);
	    com.jcabi.log.Logger.info("Logfile",getBSLogs(bsUserId,bsKey));
   }
   
   public String getBSLogs(String bsUserId,String bsKey) {
	    String sessId = driver.getSessionId().toString();
	    //System.out.println("sessionId:"+sessId);
	    String url = "https://browserstack.com/automate/sessions/"+sessId+".json";
	    //System.out.println("url:"+url.toString());
	    String authUser = bsUserId+":"+bsKey;
	    String encoding = Base64.encodeBase64String(authUser.getBytes());
	    Client restClient = Client.create();
        WebResource webResource = restClient.resource(url);
        ClientResponse resp = webResource.accept("application/json")
                                        .header("Authorization", "Basic " + encoding)
                                        .get(ClientResponse.class);
       if(resp.getStatus() != 200){
    	   printLogs("error","getBSLogs","Unable to connect to the server");
       }
       String output = resp.getEntity(String.class);
       JSONObject obj = new JSONObject(output);
       JSONObject bsLogs = (JSONObject) obj.get("automation_session");
       String publicUrl = bsLogs.get("public_url").toString();
       return publicUrl;
  }
   
   @Parameters({"jenkins"})
   @AfterClass(alwaysRun = true)
    public void setupAfterSuite(@Optional("0")String jenkins) {
	    try {
	    	driver.quit();
	    	if(Integer.parseInt(jenkins) == 0) {
	    		bsLocal.stop();
	    	}
	    } catch (Exception e) {
	    	printLogs("info","setupAfterSuite","driver already closed");
	    }
    }
	
    public boolean isContainsText(String text) {
        return driver.getPageSource().contains(text);
    }

    public ResourceBundle getBundle() {
        return bundle;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public String getPageSource() {
        return driver.getPageSource();
    }

    public void pageRefresh() {
        driver.navigate().refresh();
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String isRequired(String field) {
        String isrequired = getBundle().getString("errors.required").replace("{0}", field);
        return isrequired;
    }

    public String matchPattern(String field) {
        String isrequired = getBundle().getString("errors.required").replace("{0}", field);
        return isrequired;
    }
}

