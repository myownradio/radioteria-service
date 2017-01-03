package com.radioteria.controllers;

import com.radioteria.player.broadcast.ChannelOutputStream;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.AsyncContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Controller
public class BaseController {
    private ChannelOutputStream channelOutputStream = new ChannelOutputStream();

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
        channelOutputStream.addListener(response.getOutputStream());
    }
}
