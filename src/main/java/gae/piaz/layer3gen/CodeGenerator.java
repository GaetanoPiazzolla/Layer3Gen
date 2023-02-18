package gae.piaz.layer3gen;

import freemarker.template.TemplateException;
import gae.piaz.layer3gen.config.CodeGeneratorConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

@Slf4j
public class CodeGenerator {

    // Utility class such CodeGenerator that have all method as static should have private constructor
    // Code smell : java:S1118
    private CodeGenerator() {
        throw new IllegalStateException("Utility class");
    }

    private static CodeGeneratorConfig config;

    private static URLClassLoader classLoader;

    public static void run(CodeGeneratorConfig arg, URLClassLoader classLoader) throws IOException, TemplateException {

        CodeGenerator.config = arg;
        CodeGenerator.classLoader = classLoader;
        log.debug("configuration: {}", config);
        log.debug("ClassLoader: {}", classLoader);

        Set<Class<?>> entities = getEntityClasses();
        log.debug("found {} entities", entities.size());
        generateCode(entities);

    }

    private static void generateCode(Set<Class<?>> entities) throws IOException, TemplateException {

        createCrudInterfaces();

        for (Class<?> entity : entities) {

            createRepository(entity);

            if (Boolean.TRUE.equals(config.getOptions().getServiceInterface())) {
                createServiceBean(entity);
                createServiceInterface(entity);
            } else {
                createService(entity);
            }

            if (Boolean.TRUE.equals(config.getOptions().getEntityControllers())){
                createController(entity);
            }

            if (Boolean.TRUE.equals(config.getOptions().getDtoLayer())) {
                createDto(entity);
                createMapper(entity);
                createControllerDTO(entity);
            }
        }

    }

    private static Set<Class<?>> getEntityClasses() {
        Reflections reflections = new Reflections(config.getInputPackages().getJpaEntities(), classLoader);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(javax.persistence.Entity.class);
        classes.addAll(reflections.getTypesAnnotatedWith(jakarta.persistence.Entity.class));
        return classes;
    }


    private static void createControllerDTO(Class<?> entity) throws IOException, TemplateException {

        CodeRenderer.RenderingData data = new CodeRenderer.RenderingData();
        data.setConfig(config);
        data.setEntityClass(entity.getSimpleName());
        data.setPrimaryKeyClass(getPrimaryKeyClass(entity));
        data.setEntityPackage(entity.getPackageName());
        String code = CodeRenderer.render("controllerdto.ftl", data);

        String filepath = Paths.get(config.getProjectPath(), config.getOutputDirectory(),
                config.getOutputPackages().getControllers().replaceAll("\\.", "/"),
                entity.getSimpleName() + "ControllerDTO.java").toString();

        writeFile(code, filepath);

    }

    private static void createMapper(Class<?> entity) throws IOException, TemplateException {

        if(StringUtils.isBlank(config.getOutputPackages().getMappers())){
            config.getOutputPackages().setMappers(config.getOutputPackages().getServices() + ".mapper");
        }

        CodeRenderer.RenderingData data = new CodeRenderer.RenderingData();
        data.setConfig(config);
        data.setEntityClass(entity.getSimpleName());
        data.setEntityPackage(entity.getPackageName());

        String code = CodeRenderer.render("mapper.ftl", data);
        String filepath = Paths.get(config.getProjectPath(), config.getOutputDirectory(),
                config.getOutputPackages().getMappers().replaceAll("\\.", "/"),
                entity.getSimpleName() + "Mapper.java").toString();

        writeFile(code, filepath);

    }

    private static void createDto(Class<?> entity) throws IOException, TemplateException {

        if(StringUtils.isBlank(config.getOutputPackages().getDtos())){
            config.getOutputPackages().setDtos(config.getOutputPackages().getControllers() + ".dto");
        }

        CodeRenderer.RenderingData data = new CodeRenderer.RenderingData();
        data.setConfig(config);
        data.setEntityClass(entity.getSimpleName());
        data.setEntityFields(Arrays.asList(entity.getDeclaredFields()));

        String code = CodeRenderer.render("dto.ftl", data);
        String filepath = Paths.get(config.getProjectPath(), config.getOutputDirectory(),
                config.getOutputPackages().getDtos().replaceAll("\\.", "/"),
                entity.getSimpleName() + "DTO.java").toString();

        writeFile(code, filepath);

    }

    private static void createController(Class<?> entity) throws IOException, TemplateException {

        CodeRenderer.RenderingData data = new CodeRenderer.RenderingData();
        data.setConfig(config);
        data.setEntityClass(entity.getSimpleName());
        data.setPrimaryKeyClass(getPrimaryKeyClass(entity));
        data.setEntityPackage(entity.getPackageName());
        String code = CodeRenderer.render("controller.ftl", data);

        String filepath = Paths.get(config.getProjectPath(), config.getOutputDirectory(),
                config.getOutputPackages().getControllers().replaceAll("\\.", "/"),
                entity.getSimpleName() + "Controller.java").toString();

        writeFile(code, filepath);

    }

    private static void createService(Class<?> entity) throws IOException, TemplateException {

        CodeRenderer.RenderingData data = new CodeRenderer.RenderingData();
        data.setConfig(config);
        data.setEntityClass(entity.getSimpleName());
        data.setPrimaryKeyClass(getPrimaryKeyClass(entity));
        data.setEntityPackage(entity.getPackageName());

        String code = CodeRenderer.render("service.ftl", data);

        String filepath = Paths.get(config.getProjectPath(), config.getOutputDirectory(),
                config.getOutputPackages().getServices().replaceAll("\\.", "/"),
                entity.getSimpleName() + "Service.java").toString();

        writeFile(code, filepath);

    }

    private static void createServiceInterface(Class<?> entity) throws IOException, TemplateException {

        CodeRenderer.RenderingData data = new CodeRenderer.RenderingData();
        data.setConfig(config);
        data.setEntityClass(entity.getSimpleName());
        data.setPrimaryKeyClass(getPrimaryKeyClass(entity));
        data.setEntityPackage(entity.getPackageName());

        String code = CodeRenderer.render("serviceInterface.ftl", data);

        String filepath = Paths.get(config.getProjectPath(), config.getOutputDirectory(),
                config.getOutputPackages().getServices().replaceAll("\\.", "/"),
                entity.getSimpleName() + "Service.java").toString();

        writeFile(code, filepath);

    }

    private static void createServiceBean(Class<?> entity) throws IOException, TemplateException {

        CodeRenderer.RenderingData data = new CodeRenderer.RenderingData();
        data.setConfig(config);
        data.setEntityClass(entity.getSimpleName());
        data.setPrimaryKeyClass(getPrimaryKeyClass(entity));
        data.setEntityPackage(entity.getPackageName());

        String code = CodeRenderer.render("serviceBean.ftl", data);

        String filepath = Paths.get(config.getProjectPath(), config.getOutputDirectory(),
                config.getOutputPackages().getServices().replaceAll("\\.", "/"),
                "impl", entity.getSimpleName() + "ServiceBean.java").toString();

        writeFile(code, filepath);

    }

    private static void createCrudInterfaces() throws IOException, TemplateException {

        CodeRenderer.RenderingData data = new CodeRenderer.RenderingData();
        data.setConfig(config);

        String code = CodeRenderer.render("crudservice.ftl", data);
        String filepath = Paths.get(config.getProjectPath() , config.getOutputDirectory(),
                config.getOutputPackages().getServices().replaceAll("\\.", "/"), "CrudService.java").toString();
        writeFile(code, filepath);

        code = CodeRenderer.render("crudcontroller.ftl", data);
        filepath = Paths.get(config.getProjectPath() , config.getOutputDirectory() ,
                config.getOutputPackages().getControllers().replaceAll("\\.", "/"), "CrudController.java").toString();
        writeFile(code, filepath);

    }

    private static void createRepository(Class<?> entity) throws IOException, TemplateException {

        CodeRenderer.RenderingData data = new CodeRenderer.RenderingData();
        data.setConfig(config);
        data.setEntityClass(entity.getSimpleName());
        data.setPrimaryKeyClass(getPrimaryKeyClass(entity));

        String code = CodeRenderer.render("repository.ftl", data);

        String filepath = Paths.get(config.getProjectPath() , config.getOutputDirectory(),
                config.getOutputPackages().getRepositories().replaceAll("\\.", "/"),
                entity.getSimpleName() + "Repository.java").toString();

        writeFile(code, filepath);

    }

    private static void writeFile(String code, String filepath) throws IOException {

        Path path = Paths.get(filepath);
        if (!Files.exists(path)) {
            Files.createDirectories(path.getParent());
        }
        Files.write(path, code.getBytes());
        log.debug("path: {}, code: {}", path, code);

    }

    private static String getPrimaryKeyClass(Class<?> entity) {

        Reflections reflections = new Reflections(entity, classLoader, new FieldAnnotationsScanner());

        Set<Field> ids = reflections.getFieldsAnnotatedWith(javax.persistence.Id.class);
        ids.addAll(reflections.getFieldsAnnotatedWith(jakarta.persistence.Id.class));
        if (ids.isEmpty()) {
            ids = reflections.getFieldsAnnotatedWith(javax.persistence.EmbeddedId.class);
            ids.addAll(reflections.getFieldsAnnotatedWith(javax.persistence.EmbeddedId.class));

            if (ids.isEmpty()) {
                log.warn("No @Id found for " + entity + " using generic object  \"Object\" ");
                return "Object";
            }
        }

        return ids.stream().findFirst().get().getType().getName();

    }


}
