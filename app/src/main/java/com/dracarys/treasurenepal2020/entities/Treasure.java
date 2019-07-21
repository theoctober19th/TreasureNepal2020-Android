package com.dracarys.treasurenepal2020.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Treasure implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("points")
    @Expose
    private Integer points;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("desp_link")
    @Expose
    private String despLink;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("place_desp")
    @Expose
    private String placeDesp;
    @SerializedName("challenge_desp")
    @Expose
    private String challengeDesp;
    @SerializedName("challenge_type")
    @Expose
    private String challengeType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDespLink() {
        return despLink;
    }

    public void setDespLink(String despLink) {
        this.despLink = despLink;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPlaceDesp() {
        return placeDesp;
    }

    public void setPlaceDesp(String placeDesp) {
        this.placeDesp = placeDesp;
    }

    public String getChallengeDesp() {
        return challengeDesp;
    }

    public void setChallengeDesp(String challengeDesp) {
        this.challengeDesp = challengeDesp;
    }

    public String getChallengeType() {
        return challengeType;
    }

    public void setChallengeType(String challengeType) {
        this.challengeType = challengeType;
    }

    public Treasure(String name, Double latitude, Double longitude, Integer points) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.points = points;
    }
}