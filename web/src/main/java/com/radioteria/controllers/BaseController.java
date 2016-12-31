package com.radioteria.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class BaseController {

    @RequestMapping("/")
    public void index(HttpServletResponse response) throws IOException {
        response.getWriter().print("Hello, World!");
    }

    @RequestMapping("views")
    public String view() {
        return "index";
    }

}
