package com.go4venue.bl;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class VenueURLPuller {

	public static void main(String[] args) {
		VenueURLPuller vp = new VenueURLPuller();
		WebDriver driver = new FirefoxDriver();
		String topLevelUrl = "http://www.venuelook.com/Wedding-venues-in-Delhi-NCR/page/";
		for(int k=1;k<4;k++){
			vp.getDataFromUrl(topLevelUrl+k+"/",driver);
		}
		driver.close();
	}
	
	
	public void getDataFromUrl(String url,WebDriver driver){
		
		driver.get(url);
		String topLevelXpath = "/html/body/div[5]/div[2]/div[5]/div[5]/ul/li[";
		
		for(int i=1;i<=10;i++){
			

			WebElement elem = driver.findElement(By.xpath(topLevelXpath+i+"]/div[2]/div[1]/div[1]/h2/a"));
			System.out.println(elem.getAttribute("href"));
		}
		
		
		
	}

}
