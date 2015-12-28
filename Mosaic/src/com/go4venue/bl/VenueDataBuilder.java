package com.go4venue.bl;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	Map<String, BigInteger> amenitiesMap;
	Map<String, BigInteger> ocassionsMap;
	Map<String, BigInteger> cusinesMap;
	Map<String, BigInteger> venueTypeMap;

	private WebDriver driver;

	public VenueDataBuilder() {
		conn = JDBCConnection.getInstance();
		driver = new FirefoxDriver();
	}

	public static void main(String[] args) {
		VenueDataBuilder vdb = new VenueDataBuilder();

		//
		vdb.loadMaps();
		vdb.ListUrl();
		// vdb.getData("http://www.venuelook.com/Gurgaon/Regency-Ballroom-of-Hyatt-Regency-Gurgaon-in-Sector-83");
	}

	public void ListUrl() {
		String query = SQLRaw2DB.getVenueUrls;
		String inertVenueDataQuery = SQLRaw2DB.insertVenueData;
		List<String> params = new ArrayList<String>();

		ResultSet rs = conn.executeQuery(query, null);
		try {
			while (rs.next()) {
				try {
					this.getData(rs.getString("url"), params);
					conn.insertData(inertVenueDataQuery, params, false);

					params.clear();
				} catch (Exception e) {

					e.printStackTrace();

				} finally {
					params.clear();
					conn.upsertData(SQLRaw2DB.updateUrlListFlag,
							Arrays.asList(rs.getString("url")));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			driver.close();
			conn.closeConnection();
		}
	}

	public void getData(String url, List<String> params) throws Exception {

		// HtmlUnitDriver driver = new HtmlUnitDriver(BrowserVersion.CHROME);
		driver.get(url);
		System.out.println(url);
		// name
		params.add(url);
		// System.out.println(driver.findElement(By.xpath("/html/body/div[1]/section[1]/div/div[2]/div[1]/div[2]/div[2]")).getText());

		if (driver
				.findElements(
						By.xpath("/html/body/div[2]/section[1]/div/div[2]/div[1]/div[2]/div[2]"))
				.size() > 0) {
			String venueCapacity = driver
					.findElement(
							By.xpath("/html/body/div[2]/section[1]/div/div[2]/div[1]/div[2]/div[2]"))
					.getText();
			params.add(venueCapacity);
		} else
			params.add("");
		// System.out.println(venueCapacity);

		if (driver
				.findElements(
						By.xpath("/html/body/div[2]/section[1]/div/div[2]/div[1]/div[1]/div[2]"))
				.size() > 0) {
			String venuePrice = driver
					.findElement(
							By.xpath("/html/body/div[2]/section[1]/div/div[2]/div[1]/div[1]/div[2]"))
					.getText();
			params.add(venuePrice);
		} else
			params.add("");
		// System.out.println(venuePrice);

		// removed the contact info from the page do give our contact number or
		// get it from google
		params.add("");

		if (driver.findElements(
				By.xpath("/html/body/div[2]/section[1]/div/div[1]/h4[1]"))
				.size() > 0) {
			String address = driver.findElement(
					By.xpath("/html/body/div[2]/section[1]/div/div[1]/h4[1]"))
					.getText();
			// System.out.println(address);
			params.add(address);
		} else
			params.add("");

		// click more button before getting description
		if (driver
				.findElements(
						By.xpath("/html/body/div[2]/section[4]/div/div/div[2]/div/div[2]/div[1]/div[2]/p/span[2]/a"))
				.size() > 0)
			driver.findElement(
					By.xpath("/html/body/div[2]/section[4]/div/div/div[2]/div/div[2]/div[1]/div[2]/p/span[2]/a"))
					.click();

		if (driver
				.findElements(
						By.xpath("/html/body/div[2]/section[4]/div/div/div[2]/div/div[2]/div[1]/div[2]"))
				.size() > 0) {
			String about = driver
					.findElement(
							By.xpath("/html/body/div[2]/section[4]/div/div/div[2]/div/div[2]/div[1]/div[2]"))
					.getText();
			// System.out.println(about);
			params.add(about);
		} else
			params.add("");
		params.add(url);
		if(driver.findElements(By.xpath("/html/body/div[2]/section[4]/div/div/div[2]/div/div[2]/div[2]/div[1]/ul/li")).size()>0){
			  String operatingTime = driver.findElement(By.xpath("/html/body/div[2]/section[4]/div/div/div[2]/div/div[2]/div[2]/div[1]/ul/li"))
				.getText();
		// System.out.println(operatingTime);
		params.add(operatingTime);
		}else
			params.add("");
		List<WebElement> rows = driver
				.findElements(By
						.cssSelector("html body div#space-detail-page section.gray-bg div.container div.gray-bg div.col-xs-12.col-sm-12.col-md-6 div#contenctscroll.vspace div#about.space-content div.panel.panel-default.panel-white.btn-flat div.panel-body ul.list-inline.space-gdfroccasion"));

		StringBuilder ocassions = new StringBuilder();
		for (int i = 1; i <= rows.size(); i++) {
			ocassions
					.append(driver
							.findElement(
									By.xpath("/html/body/div[2]/section[4]/div/div/div[2]/div/div[2]/div[3]/div[2]/ul/li["
											+ i + "]")).getText()).append(",");
		}

		// System.out.println(encodedValue(ocassions, ocassionsMap).toString());
		rows.clear();
		params.add(encodedValue(ocassions, ocassionsMap).toString());

		if (driver
				.findElements(
						By.xpath("/html/body/div[2]/section[4]/div/div/div[2]/div/div[2]/div[4]/div[2]"))
				.size() > 0) {
			String cusines = driver
					.findElement(
							By.xpath("/html/body/div[2]/section[4]/div/div/div[2]/div/div[2]/div[4]/div[2]"))
					.getText();

			// System.out.println(cusines.replaceAll(" ", ","));
			params.add(encodedValue(
					new StringBuilder(cusines.replaceAll(" ", ",")), cusinesMap)
					.toString());
		} else
			params.add("");

		if (driver
				.findElements(
						By.xpath("/html/body/div[2]/section[4]/div/div/div[2]/div/div[2]/div[5]/div[2]"))
				.size() > 0) {
			String facilities = driver
					.findElement(
							By.xpath("/html/body/div[2]/section[4]/div/div/div[2]/div/div[2]/div[5]/div[2]"))
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
			// System.out.println(finalfacilities);
			params.add(encodedValue(finalfacilities, amenitiesMap).toString());
		} else
			params.add("");
		// space Type

	}

	public BigInteger encodedValue(StringBuilder strBuff,
			Map<String, BigInteger> map) {
		BigInteger sum = new BigInteger("0");
		String strArr[] = strBuff.toString().split(",");
		for (String str : strArr) {
			sum = sum.add(map.get(str) != null ? map.get(str) : new BigInteger(
					"0"));
		}
		return sum;
	}

	public void loadMaps() {
		amenitiesMap = new HashMap<>();
		ocassionsMap = new HashMap<>();
		cusinesMap = new HashMap<>();
		venueTypeMap = new HashMap<>();
		// take the map from database
		populateMap(amenitiesMap, SQLRaw2DB.getAmenities);
		populateMap(ocassionsMap, SQLRaw2DB.getOcassionsMap);
		populateMap(cusinesMap, SQLRaw2DB.getCusinesMap);
		populateMap(venueTypeMap, SQLRaw2DB.getVenueTypeMap);
		// System.out.println(ocassionsMap);System.out.println(cusinesMap);System.out.println(venueTypeMap);

	}

	public void populateMap(Map<String, BigInteger> map, String query) {
		ResultSet rs = conn.executeQuery(query, null);
		try {
			while (rs.next()) {
				map.put(rs.getString(2), new BigInteger(rs.getString(3)));
			}
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		}

	}

}
