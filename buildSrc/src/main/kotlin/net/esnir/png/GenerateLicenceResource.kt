package net.esnir.png

import org.gradle.api.file.Directory
import org.gradle.api.DefaultTask
import org.gradle.api.file.ProjectLayout
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

@CacheableTask
abstract class GenerateLicenceResource : DefaultTask() {
    companion object {
        const val NAMESPACE = "esnir/licence"
    }

    @get:Inject
    abstract val layout: ProjectLayout

    @get:Input
    abstract val sourceSetName: Property<String>

    @get:Input
    abstract val licenseeName: Property<String>

    @get:OutputDirectory
    val outputDir: Directory
        get() = layout
            .buildDirectory.generatedDirectory.dir(NAMESPACE)
            .dir(sourceSetName).dir("composeResources")
            .get()

    @TaskAction
    fun action() {
        val sourceFile = layout.buildDirectory.reportsDirectory.dir("licensee").dir(licenseeName)
            .file("artifacts.json").get()
        val destination = outputDir.dir("files").also { it.asFile.mkdirs() }
            .file("licenseeArtifacts.json")
        sourceFile.asFile.renameTo(destination.asFile)
    }
}
