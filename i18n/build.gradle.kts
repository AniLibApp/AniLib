import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.TaskContainerScope

plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "anilib.i18n"

    lint {
        disable.addAll(listOf("MissingTranslation", "ExtraTranslation"))
    }
}

tasks {
    val localesConfigTask = registerLocalesConfigTask(project)

    // Duplicating Hebrew string assets due to some locale code issues on different devices
    val copyHebrewStrings by registering(Copy::class) {
        from("./src/main/res/values-he")
        into("./src/main/res/values-iw")
        include("**/*")
    }

    preBuild {
        dependsOn(copyHebrewStrings, localesConfigTask)
    }
}


private val emptyResourcesElement = "<resources>\\s*</resources>|<resources/>".toRegex()
private val valuesPrefix = "values(-(b\\+)?)?".toRegex()

fun TaskContainerScope.registerLocalesConfigTask(project: Project): TaskProvider<Task> {
    return with(project) {
        register("generateLocalesConfig") {
            val languages = fileTree("$projectDir/src/main/res/")
                .matching { include("**/strings.xml") }
                .filterNot { it.readText().contains(emptyResourcesElement) }
                .map { it.parentFile.name }
                .sorted()
                .joinToString(separator = "\n") {
                    val language = it
                        .replace(valuesPrefix, "")
                        .replace("-r", "-")
                        .replace("+", "-")
                        .takeIf(String::isNotBlank) ?: "en"
                    "   <locale android:name=\"$language\"/>"
                }

            val content = """
                <?xml version="1.0" encoding="utf-8"?>
                <locale-config xmlns:android="http://schemas.android.com/apk/res/android">
                $languages
                </locale-config>
                    """.trimIndent()

            val localeFile = file("$projectDir/src/main/res/xml/locales_config.xml")
            localeFile.parentFile.mkdirs()
            localeFile.writeText(content)
        }
    }
}