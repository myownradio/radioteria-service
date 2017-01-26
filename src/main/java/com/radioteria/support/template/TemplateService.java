package com.radioteria.support.template;

import java.util.Map;

public interface TemplateService {
    String render(String template, Map<String, Object> context) throws TemplateServiceException;
}
