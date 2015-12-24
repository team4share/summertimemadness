package com.go4venue.dao;

public class SQLRaw2DB {
	public static final  String insetURL = "INSERT INTO VENUE_URL(url) VALUES (?)";
	public static final  String getVenueUrls = "SELECT * FROM venue_url WHERE data_fetched = 'N'";
	public static  final String  insertVenueData="insert  into `venue`(`venue_name`,`venue_capacity`,`venue_price`,`venue_contact`" +
	",`venue_address`,`venue_description`,`venue_url`,`venue_operating_time`,`venue_ocassions`,`venue_cusines`,`venue_facilities`) "+
	"values(?,?,?,?,?,?,?,?,?,?,?)";
	public static  final String updateUrlListFlag = "update venue_url set data_fetched = 'Y' where url  = ?";
	public static final String getAmenities = "SELECT * FROM amenities";
	public static final String getOcassionsMap = "SELECT * FROM ocassion";
	public static final String getCusinesMap = "SELECT * FROM venue_cusines";
	public static final String getVenueTypeMap = "SELECT * FROM venue_type";
	
	
}
