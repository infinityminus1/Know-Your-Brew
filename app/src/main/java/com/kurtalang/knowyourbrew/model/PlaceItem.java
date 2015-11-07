package com.kurtalang.knowyourbrew.model;

/**
 * Model class for place data
 *
 * Created by kurtalang on 11/5/15.
 */
public class PlaceItem {

    private String name;
    private boolean open_now;
    private int price_level;
    private float rating;
    private String id;
    private String icon;
    private String vicinity;
    private double latitude;
    private double longitude;

    public PlaceItem() {

    }

    /* NAME */
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    /* OPEN_NOW */
    public boolean getOpen() {return open_now;}
    public void setOpen(boolean open_now) {this.open_now = open_now;}

    /*PRICE LEVEL*/
    public int getPrice_level() {return price_level;}
    public void setPrice_level(int price_level) {this.price_level = price_level;}

    /*RATING*/
    public float getRating() {return rating;}
    public void setRating(float rating) {this.rating = rating;}

    /* ID */
    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    /* ICON */
    public String getIcon() {return icon;}
    public void setIcon(String icon) {this.icon = icon;}

    /* VICINITY */
    public String getVicinity() {return vicinity;}
    public void setVicinity(String vicinity) {this.vicinity = vicinity;}

    /*LATITUDE*/
    public double getLatitude() {return latitude;}
    public void setLatitude(double latitude) {this.latitude = latitude;}

    /*LONGITUDE*/
    public double getLongitude() {return longitude;}
    public void setLongitude(double longitude) {this.longitude = longitude;}


}
