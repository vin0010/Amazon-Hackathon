/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.amazon.notification.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author vbalu
 */
@Controller
public class TestController {
    
    @RequestMapping(value = "/sample/test", produces = "application/json", method = RequestMethod.GET)
    @ResponseBody
    public String getTest(){
        return "hello";
    }
    
}
