/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amazon.notification.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 *
 * @author Vbalu
 */
@Configuration
@PropertySource({"classpath:project.properties"})
public class ProjectConfig {
    
    public static final String CONFIG_ADMIN_EMAIL_ADDRESSES_PROPERTY = "config.adminEmailAddresses";
    
}
