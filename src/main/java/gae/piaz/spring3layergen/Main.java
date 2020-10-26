package gae.piaz.spring3layergen;

import gae.piaz.spring3layergen.config.CodeGeneratorConfig;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        CodeGeneratorConfig config = CodeGeneratorConfig.load(args[0]);
        System.out.println(config);

    }

}
