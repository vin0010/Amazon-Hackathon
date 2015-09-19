package com.amazon.notification.service;



import java.util.Map;

public interface NotificationManager {

    void sendEnquiryEmail(String name, String email, String enquiry);


    // to allow for testing, this method is in the interface
    String renderTemplate(Map<String, Object> map, String template);

    // to allow for testing, this method is in the interface
    void sendEmail(String from, String[] to, String[] bcc, String subject, String body);
}
