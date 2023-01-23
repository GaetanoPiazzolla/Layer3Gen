package gae.piaz.layer3gen.main;

import gae.piaz.layer3gen.config.CodeGeneratorConfig;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.List;

public final class ClassLoaderBuilderMain {

    private ClassLoaderBuilderMain() {}

    public static URLClassLoader getClassLoader(CodeGeneratorConfig config) throws MalformedURLException {
        final File classes = new File(Paths.get(config.getProjectPath(), config.getClassesDirectory()).toString());
        List<URL> listOfURL = List.of(classes.toURI().toURL());
        return new URLClassLoader(listOfURL.toArray(new URL[0]));
    }

}
