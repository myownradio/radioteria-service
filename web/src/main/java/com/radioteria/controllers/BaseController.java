package com.radioteria.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class BaseController {

    final private static Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    @RequestMapping("/")
    public void index(HttpServletResponse response) throws IOException {
        LOGGER.info("Printing 'Hello, World!'");
        response.getWriter().print("Hello, World!");
    }

}
