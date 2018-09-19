package de.sopro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class EditorController {

  @RequestMapping("/editor")
  public String showHome(Model model) {
    model.addAttribute("message", "This is the Editor!");
    return "editor-jquery";
  }
}
