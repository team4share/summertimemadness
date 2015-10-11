package com.go4venue.bl;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.go4venue.dao.JDBCConnection;
import com.go4venue.dao.SQLRaw2DB;


public class DataPullerVenueLook {

	public static void main(String[] args) {
		DataPullerVenueLook vp = new DataPullerVenueLook();
		WebDriver driver = new FirefoxDriver();
		
		String topLevelUrl = "http://www.venuelook.com/Wedding-venues-in-Delhi-NCR/page/";
		for(int k=21;k<=35;k++){
			System.out.println(k);
			vp.getDataFromUrl(topLevelUrl+k+"/",driver);
		}
		driver.close();
	}
	
	
public void getDataFromUrl(String url,WebDriver driver){
		JDBCConnection con = JDBCConnection.getInstance();
		driver.get(url);
	
		String topLevelXpath = "/html/body/div[5]/div[2]/div[3]/div[5]/ul/li[";
		
		for(int i=1;i<=10;i++){
			WebElement elem = driver.findElement(By.xpath(topLevelXpath+i+"]/div[2]/div[1]/div[1]/h2/a"));
			String query = SQLRaw2DB.insetURL;
			List<String> params = Arrays.asList(elem.getAttribute("href"));
			try {
				con.insertData(query, params, false);
			} catch (SQLException e) {
				//e.printStackTrace();
				e.getMessage();
			}
			
		}
}


}
