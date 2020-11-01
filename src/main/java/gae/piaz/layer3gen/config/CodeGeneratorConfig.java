package gae.piaz.layer3gen.config;

import gae.piaz.layer3gen.ResourceReader;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Data
@Slf4j
public class CodeGeneratorConfig implements Serializable {

    private String projectPath;
    private String classesDirectory;
    private String outputDirectory;
    private Options options;
    private InputPackages inputPackages;
    private OutputPackages outputPackages;

    private static final Yaml YAML = new Yaml();

    public static CodeGeneratorConfig load(String path, boolean fromClassPath) throws IOException {

        if(StringUtils.isBlank(path)){
            path = "3layer-settings.yml";
        }

        InputStream is;

        if(!fromClassPath) {
            Path a = Paths.get(path);
            log.info("Configuration path: {}",a.toString());
            is = Files.newInputStream(a);
        }

        else{
             is = ResourceReader.getResourceAsStream(path);
        }

        try (Reader reader = new InputStreamReader(is)) {
            return YAML.loadAs(reader, CodeGeneratorConfig.class);
        }

    }


}
