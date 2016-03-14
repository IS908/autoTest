package cn.net.cobot.autotest.common;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class getDriver {
	private String chromeLoc;
	private String driverLoc;
	private String URL;
	private String user;
	private String pwd;
	public final static int C = 1;
	public final static int CPP = 2;

	private getDriver(){
		Properties prop = new Properties();
		InputStream in = Object.class.getResourceAsStream("/env.properties");
		try {
            prop.load(in);
            chromeLoc = prop.getProperty("chromeLoc").trim();
            driverLoc = prop.getProperty("driverLoc").trim();
			URL = prop.getProperty("URL").trim();
			user = prop.getProperty("user").trim();
			pwd = prop.getProperty("pwd").trim();
        } catch (IOException e) {
            e.printStackTrace();
        }

	}
	
	private static ChromeDriver driver = null;
	
	public static ChromeDriver getInstance() {
		if(driver == null){
			getDriver getprop = new getDriver();
			System.setProperty("webdriver.chrome.driver", getprop.chromeLoc);
	    	System.out.println(System.getProperty("webdriver.chrome.driver"));
	    	ChromeDriverService service = new ChromeDriverService.Builder().
	    			usingDriverExecutable(new java.io.File(getprop.driverLoc)).
	    			usingAnyFreePort().build();
	    	driver = new ChromeDriver(service);
	    	driver.get(getprop.URL);
			driver.findElement(By.id("userName")).sendKeys(getprop.user);
	        driver.findElement(By.id("password")).sendKeys(getprop.pwd);
	        driver.findElement(By.name("submit")).click();
		}
		return driver;
	}
	
}
