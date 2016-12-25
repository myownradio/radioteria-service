package com.radioteria.backing.template;

import de.neuland.jade4j.Jade4J;
import de.neuland.jade4j.exceptions.JadeCompilerException;
import de.neuland.jade4j.template.JadeTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class JadeTemplateService implements TemplateService {

    final private static String JADE_FILE_EXTENSION = ".jade";

    final private ConcurrentMap<String, JadeTemplate> cachedTemplates = new ConcurrentHashMap<>();

    final private String templateRoot;

    public JadeTemplateService(String templatesRoot) {
        this.templateRoot = templatesRoot;
    }

    @Override
    public String render(String template, Map<String, Object> context) {
        String templateFile = convertTemplateFile(template);
        CharSequence[] pathParts = new String[] { templateRoot, templateFile };
        String pathToTemplate = String.join(File.separator, pathParts);
        try {
            JadeTemplate jadeTemplate = getTemplate(pathToTemplate);
            return Jade4J.render(jadeTemplate, context);
        } catch (JadeCompilerException e) {
            throw new TemplateServiceException(e);
        }
    }

    private String convertTemplateFile(String templateFile) {
        return templateFile.replace('.', File.separatorChar) + JADE_FILE_EXTENSION;
    }

    private JadeTemplate getTemplate(String pathToTemplate) {
        return cachedTemplates.computeIfAbsent(pathToTemplate, path -> {
            try {
                return Jade4J.getTemplate(path);
            } catch (IOException e) {
                throw new TemplateServiceException(e);
            }
        });
    }

}
