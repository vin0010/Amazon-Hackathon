/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amazon.notification.utils;

import com.amazon.notification.service.NotificationManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Vbalu
 */
public class NotificationUtils {
    
    @Autowired
    public NotificationConfig notificationConfig;
    
    @Autowired
    private NotificationManager notificationManager;
    
    public void scanDirectory(){
       String inboundFilePath = notificationConfig.getInboundFilePath();
        System.out.println("Inbound File Path ="+inboundFilePath);
        String[] toArr = {"vikash.b88@gmail.com"};
        String[] bccArr = {};
        notificationManager.sendEmail("amazehackathon2015@gmail.com", toArr, 
                bccArr, "Test Mail", "hi");
    }
}
