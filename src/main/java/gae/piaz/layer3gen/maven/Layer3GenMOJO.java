package gae.piaz.layer3gen.maven;

import gae.piaz.layer3gen.CodeGenerator;
import gae.piaz.layer3gen.config.CodeGeneratorConfig;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "dependency-counter")
public class Layer3GenMOJO extends AbstractMojo {
    private String configPath = "src/main/resources/3layer-settings.yml";

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    public void execute() throws MojoExecutionException {
        try {
            CodeGeneratorConfig config = CodeGeneratorConfig.load(configPath);
            CodeGenerator.runMaven(config,project);
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }
}