// initializes the plugin and binds it to the lifecycle

import org.gradle.api.plugins.PluginInstantiationException;
import org.gradle.api.tasks.TaskContainer;

if (!project.plugins.findPlugin("java")) {
  throw new PluginInstantiationException("Forbiddenapis only works in projects using the 'java' plugin.");
}

def tasks = project.getTasks();

def forbiddenTasks = project.sourceSets.collect { sourceSet ->
  tasks.create(sourceSet.getTaskName(plugin.FORBIDDEN_APIS_TASK_NAME_VERB, null), CheckForbiddenApis.class) { task ->
    task.setClassesDir(sourceSet.output.classesDir);
    task.setClasspath(sourceSet.compileClasspath);
    task.dependsOn(sourceSet.output);
  }
}

// Add our tasks as dependencies to chain
tasks.getByName("check").dependsOn(forbiddenTasks);
