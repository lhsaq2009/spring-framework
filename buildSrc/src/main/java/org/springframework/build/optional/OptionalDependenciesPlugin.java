package org.springframework.build.optional;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.plugins.ide.eclipse.EclipsePlugin;
import org.gradle.plugins.ide.eclipse.model.EclipseModel;

/**
 * A {@code Plugin} that adds support for Maven-style optional dependencies. Creates a new
 * {@code optional} configuration. The {@code optional} configuration is part of the
 * project's compile and runtime classpath's but does not affect the classpath of
 * dependent projects.
 *
 * @author Andy Wilkinson
 */
public class OptionalDependenciesPlugin implements Plugin<Project> {

	/**
	 * Name of the {@code optional} configuration.
	 */
	public static final String OPTIONAL_CONFIGURATION_NAME = "optional";

	@Override
	public void apply(Project project) {
		Configuration optional = project.getConfigurations().create("optional");
		project.getPlugins().withType(JavaPlugin.class, (javaPlugin) -> {
			SourceSetContainer sourceSets = project.getConvention()
					.getPlugin(JavaPluginConvention.class).getSourceSets();
			sourceSets.all((sourceSet) -> {
				sourceSet.setCompileClasspath(
						sourceSet.getCompileClasspath().plus(optional));
				sourceSet.setRuntimeClasspath(
						sourceSet.getRuntimeClasspath().plus(optional));
			});
		});
		project.getPlugins().withType(EclipsePlugin.class, (eclipePlugin) -> {
			project.getExtensions().getByType(EclipseModel.class)
					.classpath((classpath) -> {
						classpath.getPlusConfigurations().add(optional);
					});
		});
	}

}