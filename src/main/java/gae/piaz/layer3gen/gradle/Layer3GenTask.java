package gae.piaz.layer3gen.gradle;

import gae.piaz.layer3gen.CodeGenerator;
import gae.piaz.layer3gen.config.CodeGeneratorConfig;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

/**
 * entityGen Gradle task.
 */
public class Layer3GenTask extends DefaultTask {

    @TaskAction
    public void generateAll() throws Exception {

        Layer3GenExtension ext = getProject().getExtensions().getByType(Layer3GenExtension.class);
        if (ext == null) {
            ext = new Layer3GenExtension();
        }
        CodeGeneratorConfig config = CodeGeneratorConfig.load(ext.getConfigPath());
        CodeGenerator.run(config,getProject());
    }
}
