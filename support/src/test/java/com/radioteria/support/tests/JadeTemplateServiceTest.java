package com.radioteria.support.tests;

import com.radioteria.support.template.JadeTemplateService;
import com.radioteria.support.template.TemplateService;
import com.radioteria.support.template.TemplateServiceException;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class JadeTemplateServiceTest {

    private TemplateService templateService;

    @Before
    public void setup() {
        templateService = new JadeTemplateService("src/test/resources/templates");
    }

    @Test
    public void testSimpleTemplate() throws TemplateServiceException {
        String renderedTemplate = (String) templateService.render("hello-world", Collections.emptyMap());
        assertEquals("<h1>Hello, World!</h1>", renderedTemplate);
    }

    @Test
    public void testTemplateWithParameters() throws TemplateServiceException {
        Map<String, Object> context = new HashMap<String, Object>() {{
            this.put("username", "John");
        }};

        String renderedTemplate = (String) templateService.render("hello-username", context);

        assertEquals("<h1>Hello, John!</h1>", renderedTemplate);
    }

    @Test
    public void testSubDirTemplate() throws TemplateServiceException {
        String expectedContent = "<html><head><title>Page Title</title></head><body><h1>Page Content</h1></body></html>";
        String renderedTemplate = (String) templateService.render("sub.template", Collections.emptyMap());

        assertEquals(expectedContent, renderedTemplate);
    }

}
