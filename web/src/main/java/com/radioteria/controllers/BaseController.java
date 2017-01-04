package com.radioteria.controllers;

import com.radioteria.util.io.MultiListenerOutputStream;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class BaseController {
    private MultiListenerOutputStream multiListenerOutputStream = new MultiListenerOutputStream();

    @RequestMapping("/")
    public void index(HttpServletResponse response) throws IOException {
        response.getWriter().print("Hello, World!");
    }

    @RequestMapping("views")
    public String view() {
        return "index";
    }

    @ResponseBody
    @RequestMapping("time")
    public void stream(HttpServletRequest request) throws IOException {
        AsyncContext asyncContext = request.startAsync();
        asyncContext.setTimeout(0);
        HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();
        response.addHeader("Transfer-Encoding", "chunked");
        multiListenerOutputStream.addListener(response.getOutputStream());
    }
}
