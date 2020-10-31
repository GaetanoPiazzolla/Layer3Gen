package gae.piaz.layer3gen;

import freemarker.template.TemplateException;
import gae.piaz.layer3gen.config.CodeGeneratorConfig;
import lombok.extern.slf4j.Slf4j;
import org.gradle.api.Project;
import org.gradle.api.file.FileCollection;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSetContainer;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
public class CodeGenerator {

    private static CodeGeneratorConfig config;

    private static URLClassLoader classLoader;

    public static void runGradle(CodeGeneratorConfig arg, Project project) throws Exception {

        config = arg;
        log.debug("configuration: {}",config);

        setClassLoader(project);
        log.debug("ClassLoader: {}", classLoader);

        Set<Class<?>> entities = getEntityClasses();
        log.debug("found {} entities",entities.size());
        generateCode(entities);
    }

    public static void runMain(CodeGeneratorConfig arg) throws IOException, TemplateException {
        config = arg;
        log.debug("configuration: {}",config);

        setClassLoader();
        log.debug("ClassLoader: {}", classLoader);

        Set<Class<?>> entities = getEntityClasses();
        log.debug("found {} entities",entities.size());
        generateCode(entities);
    }

    private static void setClassLoader() throws MalformedURLException {
        final File classes = new File(Paths.get(config.getProjectPath(), config.getClassesDirectory()).toString());
        List<URL> listOfURL = List.of(classes.toURI().toURL());
        classLoader = new java.net.URLClassLoader(listOfURL.toArray(new URL[0]));
    }

    private static void setClassLoader(Project project) throws MalformedURLException {
        List<URL> listOfURL = new ArrayList<>();
        SourceSetContainer ssc = project.getConvention().getPlugin(JavaPluginConvention.class).getSourceSets();
        FileCollection classesDir = ssc.getByName("main").getOutput().getClassesDirs();
        for (File file : classesDir) {
            listOfURL.add(file.toURI().toURL());
        }
        classLoader = new java.net.URLClassLoader(listOfURL.toArray(new URL[0]));
    }

    private static void generateCode(Set<Class<?>> entities) throws IOException, TemplateException {

        createCrudInterfaces();

        for (Class<?> entity : entities) {

            createRepository(entity);
            createService(entity);
            createController(entity);

            if(config.getOptions().getDtoLayer()) {
                createDto(entity);
                createMapper(entity);
                createControllerDTO(entity);
            }
        }

    }

    private static Set<Class<?>> getEntityClasses() throws MalformedURLException {

        Reflections reflections = new Reflections(config.getInputPackages().getJpaEntities(), classLoader);
        return  reflections.getTypesAnnotatedWith(javax.persistence.Entity.class);

    }


    private static void createControllerDTO(Class<?> entity) throws IOException, TemplateException {
        CodeRenderer.RenderingData data = new CodeRenderer.RenderingData();
        data.setConfig(config);
        data.setEntityClass(entity.getSimpleName());
        data.setPrimaryKeyClass(getPrimaryKeyClass(entity));
        data.setEntityPackage(entity.getPackageName());
        String code = CodeRenderer.render("controllerdto.ftl",data);

        String filepath = config.getProjectPath() + "/" + config.getOutputDirectory() + "/" +
                config.getOutputPackages().getControllers().replaceAll("\\.", "/") + "/" + entity.getSimpleName() + "ControllerDTO.java";

        writeFile(code, filepath);
    }

    private static void createMapper(Class<?> entity) throws IOException, TemplateException {
        CodeRenderer.RenderingData data = new CodeRenderer.RenderingData();
        data.setConfig(config);
        data.setEntityClass(entity.getSimpleName());
        data.setEntityPackage(entity.getPackageName());

        String code = CodeRenderer.render("mapper.ftl",data);

        String filepath = config.getProjectPath() + "/" + config.getOutputDirectory() + "/" +
                config.getOutputPackages().getServices().replaceAll("\\.", "/") + "/mapper/" + entity.getSimpleName() + "Mapper.java";

        writeFile(code, filepath);
    }

    private static void createDto(Class<?> entity) throws IOException, TemplateException {

        CodeRenderer.RenderingData data = new CodeRenderer.RenderingData();
        data.setConfig(config);
        data.setEntityClass(entity.getSimpleName());
        data.setEntityFields(Arrays.asList(entity.getDeclaredFields()));

        String code = CodeRenderer.render("dto.ftl",data);

        String filepath = config.getProjectPath() + "/" + config.getOutputDirectory() + "/" +
                config.getOutputPackages().getControllers().replaceAll("\\.", "/") + "/dto/" + entity.getSimpleName() + "DTO.java";

        writeFile(code, filepath);

    }

    private static void createController(Class<?> entity) throws IOException, TemplateException {
        CodeRenderer.RenderingData data = new CodeRenderer.RenderingData();
        data.setConfig(config);
        data.setEntityClass(entity.getSimpleName());
        data.setPrimaryKeyClass(getPrimaryKeyClass(entity));
        data.setEntityPackage(entity.getPackageName());
        String code = CodeRenderer.render("controller.ftl",data);

        String filepath = config.getProjectPath() + "/" + config.getOutputDirectory() + "/" +
                config.getOutputPackages().getControllers().replaceAll("\\.", "/") + "/" + entity.getSimpleName() + "Controller.java";

        writeFile(code, filepath);
    }

    private static void createService(Class<?> entity) throws IOException, TemplateException {
        CodeRenderer.RenderingData data = new CodeRenderer.RenderingData();
        data.setConfig(config);
        data.setEntityClass(entity.getSimpleName());
        data.setPrimaryKeyClass(getPrimaryKeyClass(entity));
        data.setEntityPackage(entity.getPackageName());

        String code = CodeRenderer.render("service.ftl",data);

        String filepath = config.getProjectPath() + "/" + config.getOutputDirectory() + "/" +
                config.getOutputPackages().getServices().replaceAll("\\.", "/") + "/" + entity.getSimpleName() + "Service.java";

        writeFile(code, filepath);
    }

    private static void createCrudInterfaces() throws IOException, TemplateException {
        CodeRenderer.RenderingData data = new CodeRenderer.RenderingData();
        data.setConfig(config);

        String code = CodeRenderer.render("crudservice.ftl",data);
        String filepath = config.getProjectPath() + "/" + config.getOutputDirectory() + "/" +
                config.getOutputPackages().getServices().replaceAll("\\.", "/") + "/" + "CrudService.java";
        writeFile(code, filepath);

        code = CodeRenderer.render("crudcontroller.ftl",data);
        filepath = config.getProjectPath() + "/" + config.getOutputDirectory() + "/" +
                config.getOutputPackages().getControllers().replaceAll("\\.", "/") + "/" + "CrudController.java";
        writeFile(code, filepath);
    }

    private static void createRepository(Class<?> entity) throws IOException, TemplateException {

        CodeRenderer.RenderingData data = new CodeRenderer.RenderingData();
        data.setConfig(config);
        data.setEntityClass(entity.getSimpleName());
        data.setPrimaryKeyClass(getPrimaryKeyClass(entity));

        String code = CodeRenderer.render("repository.ftl",data);

        String filepath = config.getProjectPath() + "/" + config.getOutputDirectory() + "/" +
                config.getOutputPackages().getRepositories().replaceAll("\\.", "/") + "/" + entity.getSimpleName() + "Repository.java";

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

    private static String getPrimaryKeyClass(Class<?> entity){

        Reflections reflections = new Reflections(entity, classLoader, new FieldAnnotationsScanner());

        Set<Field> ids = reflections.getFieldsAnnotatedWith(javax.persistence.Id.class);
        if(ids.isEmpty()){
            ids = reflections.getFieldsAnnotatedWith(javax.persistence.EmbeddedId.class);
            if(ids.isEmpty()) {
                log.warn("No @Id found for " + entity + " using generic object  \"Object\" ");
                return "Object";
            }
        }
        return ids.stream().findFirst().get().getType().getName();
    }



}
