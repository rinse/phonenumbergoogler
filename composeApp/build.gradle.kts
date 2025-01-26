import net.esnir.png.GenerateLicenceResource
import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.licensee)
}

kotlin {
    jvmToolchain(21)
    androidTarget {
        //https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-test.html
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.materialIconsExtended)
            implementation(libs.napier)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.androidx.navigation.composee)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.coil)
            implementation(libs.coil.network.ktor)
            implementation(libs.multiplatformSettings)
            implementation(libs.kodein)
            implementation(libs.kodein.compose)
            implementation(libs.kotlinx.datetime)
            implementation(libs.material.icons)
            implementation(libs.material.icons.extended)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
            implementation(libs.kotlinx.coroutines.test)
        }

        androidMain.dependencies {
            implementation(compose.uiTooling)
            implementation(libs.androidx.activityCompose)
            implementation(libs.kotlinx.coroutines.android)
        }
    }
}

android {
    namespace = "net.esnir.png"
    compileSdk = 35

    defaultConfig {
        minSdk = 21
        targetSdk = 35

        applicationId = "net.esnir.png.androidApp"
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

//https://developer.android.com/develop/ui/compose/testing#setup
dependencies {
    androidTestImplementation(libs.androidx.uitest.junit4)
    debugImplementation(libs.androidx.uitest.testManifest)
}

licensee {
    // allow("Apache-1.1")
    allow("Apache-2.0")
    // allow("BSD-2-Clause")
    // allow("BSD-3-Clause")
    // allow("ISC")
    allow("MIT")
}

enum class LicenceResource(val sourceSetName: String, val licenseeName: String) {
    AndroidMain(sourceSetName = "androidMain", licenseeName = "androidRelease"),
    IOSX64(sourceSetName = "iosX64Main", licenseeName = "iosX64"),
    IOSArm64(sourceSetName = "iosArm64Main", licenseeName = "iosArm64"),
    IosSimulatorArm64(sourceSetName = "iosSimulatorArm64Main", licenseeName = "iosSimulatorArm64"),
}

compose.resources {
    for (item in LicenceResource.values()) {
        val taskName = "generateLicenceResourceFor${item.sourceSetName.uppercaseFirstChar()}"
        val task = tasks.register(taskName, GenerateLicenceResource::class.java) {
            dependsOn("licensee")
            group = "licence"
            sourceSetName = item.sourceSetName
            licenseeName = item.licenseeName
        }
        customDirectory(
            sourceSetName = item.sourceSetName,
            directoryProvider = task.map { it.outputDir },
        )
    }
}
