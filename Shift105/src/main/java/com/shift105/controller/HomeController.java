package com.shift105.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shift105.model.Shift;

@Controller
public class HomeController {

	@Value("${welcome.message}")
    private String message;
	
	@GetMapping("/")
    public String main(Model model) {
        model.addAttribute("message", message);

        return "welcome"; //view
    }
	
	@GetMapping("/planned")
    public String planned(Model model) {

        return "planned"; //view
    }
	
	@GetMapping("/actual")
    public String actual(Model model) {

        return "actual"; //view
    }
	
	@GetMapping("/variance")
    public String variance(Model model) {

        return "variance"; //view
    }
	
	@GetMapping("/propose")
    public String propose(Model model) {
		Shift shift = new Shift();
		shift.setMonth_name("May");
		shift.setYear(2020);
		model.addAttribute("shift", shift);
        return "propose"; //view
    }
}
