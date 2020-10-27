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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class Main {

    private static CodeGeneratorConfig config;

    public static void main(String[] args) throws Exception {

        if(args == null || args.length==0) {
            throw new Exception("missing argument path for configuration file");
        }

        config = CodeGeneratorConfig.load(args[0]);
        log.debug("starting configuration: {}",config);

        Reflections reflections = new Reflections(config.getInputPackages().getJpaEntities());
        Set<Class<?>> entities = reflections.getTypesAnnotatedWith(javax.persistence.Entity.class);
        log.debug("found {} entities",entities.size());

        createCrudInterface();
        for (Class<?> entity : entities) {
            createRepository(entity);
            createService(entity);
            //            createController(entity);
            //
            //            if(config.getOptions().getDtoLayer()) {
            //                createMapper(entity);
            //                createControllerDTO(entity);
            //            }
        }


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

        Path path = Paths.get(filepath);
        if (!Files.exists(path)) {
            Files.createDirectories(path.getParent());
        }

        Files.write(path, code.getBytes());

        log.debug("path: {}, code: {}", path, code);
    }

    private static void createCrudInterface() throws IOException, TemplateException {
        CodeRenderer.RenderingData data = new CodeRenderer.RenderingData();
        data.setConfig(config);

        String code = CodeRenderer.render("crudservice.ftl",data);

        String filepath = config.getProjectPath() + "/" + config.getOutputDirectory() + "/" +
                config.getOutputPackages().getServices().replaceAll("\\.", "/") + "/" + "CrudService.java";

        Path path = Paths.get(filepath);
        if (!Files.exists(path)) {
            Files.createDirectories(path.getParent());
        }

        Files.write(path, code.getBytes());

        log.debug("path: {}, code: {}", path, code);
    }

    private static void createRepository(Class<?> entity) throws IOException, TemplateException {

        CodeRenderer.RenderingData data = new CodeRenderer.RenderingData();
        data.setConfig(config);
        data.setEntityClass(entity.getSimpleName());
        data.setPrimaryKeyClass(getPrimaryKeyClass(entity));

        String code = CodeRenderer.render("repository.ftl",data);

        String filepath = config.getProjectPath() + "/" + config.getOutputDirectory() + "/" +
                config.getOutputPackages().getRepositories().replaceAll("\\.", "/") + "/" + entity.getSimpleName() + "Repository.java";

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
