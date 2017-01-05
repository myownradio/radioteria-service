package com.radioteria.support.template;


import java.util.Collections;
import java.util.Map;

public class TemplateWithContext {

    private String templateFile;
    private Map<String, Object> templateContext;

    public TemplateWithContext(String templateFile) {
        this(templateFile, Collections.emptyMap());
    }

    public TemplateWithContext(String templateFile, Map<String, Object> templateContext) {
        this.templateFile = templateFile;
        this.templateContext = templateContext;
    }

    public String getTemplateFile() {
        return templateFile;
    }

    public Map<String, Object> getTemplateContext() {
        return templateContext;
    }

}
