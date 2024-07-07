package com.softuni.crossfitapp.domain.dto.events;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventDetailsDto {
    private String description;
    private Date date;
    private String address;
    private String eventName;
    private String videoUrl;

    public EventDetailsDto setVideoUrl(String videoUrl) {
        this.videoUrl = extractLastPartFromUrl(videoUrl);
        return this;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    private String extractLastPartFromUrl(String videoUrl) {
        String regex = "http(?:s?):\\/\\/(?:www\\.)?youtu(?:be\\.com\\/watch\\?v=|\\.be\\/)([\\w\\-\\_]*)(&(amp;)?\u200C\u200B[\\w\\?\u200C\u200B=]*)?";
        Pattern compile = Pattern.compile(regex);

        Matcher matcher = compile.matcher(videoUrl);
        if (matcher.find()) {
            return  matcher.group(1);
        }

        return null;
    }

    public String getDescription() {
        return description;
    }

    public EventDetailsDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public EventDetailsDto setDate(Date date) {
        this.date = date;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public EventDetailsDto setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getEventName() {
        return eventName;
    }

    public EventDetailsDto setEventName(String eventName) {
        this.eventName = eventName;
        return this;
    }
}
