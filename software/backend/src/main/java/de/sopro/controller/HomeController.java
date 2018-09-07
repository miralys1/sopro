package de.sopro.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

  // Note that this value is overridden via injection from
  // file application.properties
  @Value("${home.welcome}")
  String message = "dummy";

  @RequestMapping("/home")
  public String showHome(Model model) {
    model.addAttribute("message", message);
    return "home";
  }
}
