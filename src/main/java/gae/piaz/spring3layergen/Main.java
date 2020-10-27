package gae.piaz.spring3layergen;

import freemarker.template.TemplateException;
import gae.piaz.spring3layergen.config.CodeGeneratorConfig;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class Main {

    private static CodeGeneratorConfig config;

    public static void main(String[] args) throws Exception {

        log.info("Starting generation");
        run(args);
        log.info("Finished generation");

    }

    public static void run(String[] args) throws Exception {

        if(args == null || args.length==0) {
            throw new Exception("missing argument path for configuration file");
        }

        config = CodeGeneratorConfig.load(args[0]);
        log.debug("starting configuration: {}",config);

        Reflections reflections = new Reflections(config.getInputPackages().getJpaEntities());
        Set<Class<?>> entities = reflections.getTypesAnnotatedWith(javax.persistence.Entity.class);
        log.debug("found {} entities",entities.size());

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

    private static String getPrimaryKeyClass(Class<?> entity) {
        Set<Field> ids = findFields(entity, List.of(javax.persistence.Id.class,javax.persistence.EmbeddedId.class));
        if(ids.isEmpty()){
            System.err.println("No @Id found for "+entity+" using generic object  \"?\" ");
            return "?";
        }
        return ids.stream().findFirst().get().getType().getName();
    }

    public static Set<Field> findFields(Class<?> clasz, List<Class<? extends Annotation>> anns) {
        Set<Field> set = new HashSet<>();
        Class<?> c = clasz;
        while (c != null) {
            for (Field field : c.getDeclaredFields()) {
                for (Class<? extends Annotation> ann : anns) {
                    if (field.isAnnotationPresent(ann)) {
                        set.add(field);
                    }
                }

            }
            c = c.getSuperclass();
        }
        return set;
    }

}
