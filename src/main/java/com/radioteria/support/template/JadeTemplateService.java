package com.radioteria.support.template;

import de.neuland.jade4j.Jade4J;
import de.neuland.jade4j.exceptions.JadeCompilerException;
import de.neuland.jade4j.template.JadeTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class JadeTemplateService implements TemplateService {

    final private static String JADE_FILE_EXTENSION = ".jade";

    final private ConcurrentMap<String, JadeTemplate> cachedTemplates = new ConcurrentHashMap<>();

    final private String templateRoot;

    public JadeTemplateService(String templatesRoot) {
        this.templateRoot = templatesRoot;
    }

    @Override
    public String render(String template, Map<String, Object> context) throws TemplateServiceException {
        try {
            JadeTemplate jadeTemplate = getTemplate(template);
            return Jade4J.render(jadeTemplate, context);
        } catch (JadeCompilerException e) {
            throw new TemplateServiceException("Template " + template + " could not be rendered.", e);
        }
    }

    private String templateToFilename(String templateFile) {
        return templateFile.replace('.', File.separatorChar) + JADE_FILE_EXTENSION;
    }

    private JadeTemplate getTemplate(String template) throws TemplateServiceException {
        try {
            return cachedTemplates.computeIfAbsent(template, t -> {
                String templateFile = templateToFilename(t);
                CharSequence[] pathParts = { templateRoot, templateFile };
                String pathToTemplate = String.join(File.separator, pathParts);
                try {
                    return Jade4J.getTemplate(pathToTemplate);
                } catch (IOException e) {
                    throw new RuntimeException(
                            new TemplateServiceException("Template " + template + " could not be read.", e));
                }
            });
        } catch (RuntimeException e) {
            if (e.getCause() instanceof TemplateServiceException) {
                throw (TemplateServiceException) e.getCause();
            }
            throw e;
        }
    }

}
