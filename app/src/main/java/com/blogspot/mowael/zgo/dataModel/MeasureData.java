package com.blogspot.mowael.zgo.dataModel;

/**
 * Created by moham on 1/12/2017.
 */

public class MeasureData {

    private String requestUrl, responseJsonStr, travelDistance, duration, durationInTraffic, origin, destination, summary;
    private double originLatitude, originLongitude, destinationLatitude, destinationLongitude;

    public MeasureData() {
    }

    public MeasureData(String requestUrl, String responseJsonStr, String travelDistance, String duration, String durationInTraffic, String origin, String destination, String summary, double originLatitude, double originLongitude, double destinationLatitude, double destinationLongitude) {
        this.requestUrl = requestUrl;
        this.responseJsonStr = responseJsonStr;
        this.travelDistance = travelDistance;
        this.duration = duration;
        this.durationInTraffic = durationInTraffic;
        this.origin = origin;
        this.destination = destination;
        this.summary = summary;
        this.originLatitude = originLatitude;
        this.originLongitude = originLongitude;
        this.destinationLatitude = destinationLatitude;
        this.destinationLongitude = destinationLongitude;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getResponseJsonStr() {
        return responseJsonStr;
    }

    public void setResponseJsonStr(String responseJsonStr) {
        this.responseJsonStr = responseJsonStr;
    }

    public String getTravelDistance() {
        return travelDistance;
    }

    public void setTravelDistance(String travelDistance) {
        this.travelDistance = travelDistance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDurationInTraffic() {
        return durationInTraffic;
    }

    public void setDurationInTraffic(String durationInTraffic) {
        this.durationInTraffic = durationInTraffic;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public double getOriginLatitude() {
        return originLatitude;
    }

    public void setOriginLatitude(double originLatitude) {
        this.originLatitude = originLatitude;
    }

    public double getOriginLongitude() {
        return originLongitude;
    }

    public void setOriginLongitude(double originLongitude) {
        this.originLongitude = originLongitude;
    }

    public double getDestinationLatitude() {
        return destinationLatitude;
    }

    public void setDestinationLatitude(double destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }

    public double getDestinationLongitude() {
        return destinationLongitude;
    }

    public void setDestinationLongitude(double destinationLongitude) {
        this.destinationLongitude = destinationLongitude;
    }
}
