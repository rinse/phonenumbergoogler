package net.esnir.png

import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider

val DirectoryProperty.reportsDirectory: Provider<Directory>
    get() = this.dir("reports")

val DirectoryProperty.generatedDirectory: Provider<Directory>
    get() = this.dir("generated")

fun Provider<Directory>.dir(path: String): Provider<Directory> {
    return this.map { it.dir(path) }
}

fun Provider<Directory>.dir(path: Provider<String>): Provider<Directory> {
    return this.flatMap { it.dir(path) }
}

fun Provider<Directory>.file(path: String): Provider<RegularFile> {
    return this.map { it.file(path) }
}

fun Provider<Directory>.file(path: Provider<String>): Provider<RegularFile> {
    return this.flatMap { it.file(path) }
}
