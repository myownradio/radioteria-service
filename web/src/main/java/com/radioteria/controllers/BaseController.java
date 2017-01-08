package com.radioteria.controllers;

import com.radioteria.business.services.user.api.UserService;
import com.radioteria.fs.FileSystem;
import com.radioteria.web.dto.RegistrationForm;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class BaseController {

    @Resource
    private FileSystem fileSystem;

    @Resource
    private UserService userService;

    @RequestMapping("/")
    public void index(HttpServletResponse response) throws IOException {
        response.setHeader("Content-Type", "text/plain");
        response.getWriter().print("Hello, World!");
    }

    @RequestMapping("views")
    @Secured(value = "ROLE_REGULAR_USER")
    public String view(ModelMap modelMap) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        modelMap.put("user", authentication.getName());
        return "index";
    }

    @RequestMapping(value = "registration", method = RequestMethod.GET)
    public void registration(ModelMap modelMap) {
        RegistrationForm form = new RegistrationForm();
        modelMap.put("form", form);
    }

    @RequestMapping(value = "registration", method = RequestMethod.POST)
    @Transactional
    public String registration(@Validated RegistrationForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "registration";
        }

        userService.register(form.getEmail(), form.getPassword(), form.getName());

        return "redirect:/";
    }

//    @ResponseBody
//    @RequestMapping("time")
//    public void stream(HttpServletRequest request) throws IOException {
//        AsyncContext asyncContext = request.startAsync();
//        asyncContext.setTimeout(0);
//        HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();
//        response.addHeader("Transfer-Encoding", "chunked");
//        multiListenerOutputStream.addListener(response.getOutputStream());
//    }
}
