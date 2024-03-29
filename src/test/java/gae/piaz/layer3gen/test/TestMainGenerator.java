package gae.piaz.layer3gen.test;

import freemarker.template.TemplateException;
import gae.piaz.layer3gen.CodeGenerator;
import gae.piaz.layer3gen.config.CodeGeneratorConfig;
import gae.piaz.layer3gen.main.ClassLoaderBuilderMain;
import java.io.IOException;
import org.junit.Test;

public class TestMainGenerator {

  @Test
  public void testGeneration() throws IOException, TemplateException {
    CodeGeneratorConfig config = CodeGeneratorConfig.load("3layer-settings.yml", true);
    CodeGenerator.run(config, ClassLoaderBuilderMain.getClassLoader(config));
  }

  @Test
  public void testJakartaGeneration() throws IOException, TemplateException {
    CodeGeneratorConfig config = CodeGeneratorConfig.load("3layer-settings-jakarta.yml", true);
    CodeGenerator.run(config, ClassLoaderBuilderMain.getClassLoader(config));
  }
}
