package com.go4venue.bl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.go4venue.dao.JDBCConnection;
import com.go4venue.dao.SQLRaw2DB;

public class VenueDataBuilder {
	JDBCConnection conn;
	private WebDriver driver;
	public VenueDataBuilder() {
		conn = JDBCConnection.getInstance();
		 driver = new FirefoxDriver();
	}

	public static void main(String[] args) {
		VenueDataBuilder vdb = new VenueDataBuilder();
		//vdb.getData("http://www.venuelook.com/Gurgaon/Regency-Ballroom-of-Hyatt-Regency-Gurgaon-in-Sector-83");
		vdb.ListUrl();
	}

	public void ListUrl() {
		String query = SQLRaw2DB.getVenueUrls;
		String inertVenueDataQuery = SQLRaw2DB.insertVenueData;
		List<String> params = new ArrayList<String>();
		
		ResultSet rs = conn.executeQuery(query, null);
		try {
			while (rs.next()){
				try{
				this.getData(rs.getString("url"),params);
				conn.insertData(inertVenueDataQuery
						, params, false);
				conn.upsertData(SQLRaw2DB.updateUrlListFlag, Arrays.asList(rs.getString("url")));
				params.clear();
				}catch(Exception e){
					e.getMessage();
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			driver.close();
			conn.closeConnection();
		}
	}

	public void getData(String url,List<String> params)throws Exception {
		
		// HtmlUnitDriver driver = new HtmlUnitDriver(BrowserVersion.CHROME);
		driver.get(url);
		

			// driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);

			//System.out.println(url);
			params.add(url);
			// System.out.println(driver.findElement(By.xpath("/html/body/div[1]/section[1]/div/div[2]/div[1]/div[2]/div[2]")).getText());

			String venueCapacity = driver
					.findElement(
							By.xpath("/html/body/div[1]/section[1]/div/div[2]/div[1]/div[2]/div[2]"))
					.getText();
			params.add(venueCapacity);
			//System.out.println(venueCapacity);

			String venuePrice = driver
					.findElement(
							By.xpath("/html/body/div[1]/section[1]/div/div[2]/div[1]/div[1]/div[2]"))
					.getText();
			params.add(venuePrice);
			//System.out.println(venuePrice);

			String contactInfo = driver
					.findElement(
							By.xpath("/html/body/header/div/div/nav/div/ul[2]/li[1]/a/span"))
					.getText();
			//System.out.println(contactInfo);
			params.add(contactInfo);
			
			String address = driver.findElement(
					By.xpath("/html/body/div[1]/section[1]/div/div[1]/h4[1]"))
					.getText();
			//System.out.println(address);
			params.add(address);
			
			String about = driver
					.findElement(
							By.xpath("/html/body/div[1]/section[4]/div/div/div[2]/div/div[2]/div[1]/div[2]"))
					.getText();
			//System.out.println(about);
			params.add(about);
			params.add(url);
			String operatingTime = driver
					.findElement(
							By.xpath("/html/body/div[1]/section[4]/div/div/div[2]/div/div[2]/div[2]/div[1]/ul/li"))
					.getText();
			//System.out.println(operatingTime);
			params.add(operatingTime);

			List<WebElement> rows = driver
					.findElements(By
							.cssSelector("html body div#space-detail-page section.gray-bg div.container div.gray-bg div.col-xs-12.col-sm-12.col-md-6 div#contenctscroll.vspace div#about.space-content div.panel.panel-default.panel-white.btn-flat div.panel-body ul.list-inline.space-gdfroccasion"));

			StringBuilder ocassions = new StringBuilder();
			for (int i = 1; i <= rows.size(); i++) {
				ocassions
						.append(driver
								.findElement(
										By.xpath("/html/body/div[1]/section[4]/div/div/div[2]/div/div[2]/div[3]/div[2]/ul/li["
												+ i + "]")).getText()).append(
								",");
			}
			//System.out.println(ocassions.toString());
			rows.clear();
			params.add(ocassions.toString());

			String cusines = driver
					.findElement(
							By.xpath("/html/body/div[1]/section[4]/div/div/div[2]/div/div[2]/div[4]/div[2]"))
					.getText();

			//System.out.println(cusines.replaceAll(" ", ","));
			params.add(cusines.replaceAll(" ", ","));
			
			String facilities = driver
					.findElement(
							By.xpath("/html/body/div[1]/section[4]/div/div/div[2]/div/div[2]/div[5]/div[2]"))
					.getText();
			// System.out.println(facilities.split(" "));

			String facilitiessArr[] = facilities.split(" ");
			StringBuilder finalfacilities = new StringBuilder();
			for (int i = 0; i < facilitiessArr.length; i++) {
				if ((i % 2 == 0 && i != 0) || i == facilitiessArr.length)
					finalfacilities.append(",");
				if (i != 0)
					finalfacilities.append(" ");
				finalfacilities.append(facilitiessArr[i]);

			}
			//System.out.println(finalfacilities);
			params.add(finalfacilities.toString());
			
	
	}

}
