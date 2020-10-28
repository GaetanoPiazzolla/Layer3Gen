package gae.piaz.layer3gen.config;

import gae.piaz.layer3gen.ResourceReader;
import lombok.Data;
import org.yaml.snakeyaml.Yaml;

import java.io.*;

/**
 * projectPath: /home/tano/workspace_autogenerate/autogen
 * outputDirectory: /src/main/java
 *
 * options:
 *     dtoLayer : true
 *
 * inputPackages:
 *     jpaEntities : com.gae.piaz.autogen.model
 *
 * outputPackages:
 *     repositories : com.gae.piaz.autogen.repository
 *     services: com.gae.piaz.autogen.service
 *     controllers: com.gae.piaz.autogen.controller
 *
 */
@Data
public class CodeGeneratorConfig implements Serializable {

    private String projectPath;
    private String outputDirectory;
    private Options options;
    private InputPackages inputPackages;
    private OutputPackages outputPackages;

    private static final Yaml YAML = new Yaml();

    public static CodeGeneratorConfig load(String path) throws IOException {
        try (InputStream is = ResourceReader.getResourceAsStream(path)) {
            try (Reader reader = new InputStreamReader(is)) {
                return YAML.loadAs(reader, CodeGeneratorConfig.class);
            }
        }
    }


}
