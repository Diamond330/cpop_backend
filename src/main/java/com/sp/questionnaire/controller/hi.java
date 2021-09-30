package com.sp.questionnaire.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hi")
public class hi
{
    @RequestMapping("/say")
    public String say(){
        return "Hello World";
    }
}