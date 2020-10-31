package gae.piaz.layer3gen.main;

import gae.piaz.layer3gen.CodeGenerator;
import gae.piaz.layer3gen.config.CodeGeneratorConfig;

public class Layer3GenMain {

    public static void main(String[] args) throws Exception {
        CodeGeneratorConfig config = CodeGeneratorConfig.load(args[0]);
        CodeGenerator.runMain(config);
    }

}