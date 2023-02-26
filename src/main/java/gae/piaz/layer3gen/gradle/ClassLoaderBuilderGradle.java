package gae.piaz.layer3gen.gradle;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import org.gradle.api.Project;
import org.gradle.api.file.FileCollection;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSetContainer;

public final class ClassLoaderBuilderGradle {

  private ClassLoaderBuilderGradle() {}

  public static URLClassLoader getClassLoader(Project project) throws MalformedURLException {
    List<URL> listOfURL = new ArrayList<>();
    SourceSetContainer ssc =
        project.getConvention().getPlugin(JavaPluginConvention.class).getSourceSets();
    FileCollection classesDir = ssc.getByName("main").getOutput().getClassesDirs();
    for (File file : classesDir) {
      listOfURL.add(file.toURI().toURL());
    }
    return new java.net.URLClassLoader(listOfURL.toArray(new URL[0]));
  }
}
