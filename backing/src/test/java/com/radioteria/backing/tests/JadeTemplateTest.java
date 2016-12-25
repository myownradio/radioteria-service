package com.radioteria.backing.tests;

import com.radioteria.backing.template.JadeTemplateService;
import com.radioteria.backing.template.TemplateService;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class JadeTemplateTest {

    private TemplateService templateService;

    @Before
    public void setup() {
        templateService = new JadeTemplateService("src/test/resources/templates");
    }

    @Test
    public void testSimpleTemplate() {
        String renderedTemplate = templateService.render("hello-world", Collections.emptyMap());
        assertEquals("<h1>Hello, World!</h1>", renderedTemplate);
    }

    @Test
    public void testTemplateWithParameters() {
        Map<String, Object> context = new HashMap<String, Object>() {{
            this.put("username", "John");
        }};

        String renderedTemplate = templateService.render("hello-username", context);

        assertEquals("<h1>Hello, John!</h1>", renderedTemplate);
    }

    @Test
    public void testSubDirTemplate() {
        String expectedContent = "<html><head><title>Page Title</title></head><body><h1>Page Content</h1></body></html>";
        String renderedTemplate = templateService.render("sub.template", Collections.emptyMap());

        assertEquals(expectedContent, renderedTemplate);
    }

}
