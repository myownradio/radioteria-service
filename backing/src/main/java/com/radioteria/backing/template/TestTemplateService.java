package com.radioteria.backing.template;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TestTemplateService implements TemplateService {
    @Override
    public String render(String template, Map<String, Object> context) {
        return null;
    }
}
