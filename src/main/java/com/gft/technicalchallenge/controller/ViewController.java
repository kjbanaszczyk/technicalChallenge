package com.gft.technicalchallenge.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {

    @RequestMapping("/start")
    public String mainPage(Model model){
        return "index";
    }

}
