package gae.piaz.spring3layergen;

import freemarker.cache.StringTemplateLoader;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import gae.piaz.spring3layergen.config.CodeGeneratorConfig;
import lombok.Data;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Code renderer.
 */
public class CodeRenderer {

    /**
     * Renders source code by using Freemarker template engine.
     */
    public static String render(String templatePath, RenderingData data) throws IOException, TemplateException {
        Configuration config = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        StringTemplateLoader templateLoader = new StringTemplateLoader();
        String source;
        try (InputStream is = ResourceReader.getResourceAsStream(templatePath);
             BufferedReader buffer = new BufferedReader(new InputStreamReader(is))) {
            source = buffer.lines().collect(Collectors.joining("\n"));
        }
        templateLoader.putTemplate("template", source);
        config.setTemplateLoader(templateLoader);
        config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        config.setObjectWrapper(new BeansWrapper(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS));
        config.setWhitespaceStripping(true);

        try (Writer writer = new StringWriter()) {
            Template template = config.getTemplate("template");
            template.process(data, writer);
            return writer.toString();
        }
    }

    /**
     * Data used when rendering source code.
     */
    @Data
    public static class RenderingData {

        private String entityClass;
        private String entityPackage;

        private String primaryKeyClass;
        private CodeGeneratorConfig config;

        private List<Field> entityFields;

        private Date dateGen = new Date();


    }
}
