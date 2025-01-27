fun properties(key: String) = providers.gradleProperty(key)

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.intellij") version "1.15.0"
    id("org.jetbrains.changelog") version "2.1.0"
}

version = properties("pluginVersion").get()

dependencies {
    implementation(compose.desktop.linux_x64)
    implementation(compose.desktop.windows_x64)
    implementation(compose.desktop.macos_x64)
    implementation(compose.desktop.macos_arm64)
    implementation(project(":shared"))
}

intellij {
    pluginName = properties("pluginName").get()
    version = properties("platformVersion").get()
    type = properties("platformType").get()
}

tasks {
    buildSearchableOptions {
        enabled = false
    }
    patchPluginXml {
        version = properties("pluginVersion").get()
        sinceBuild = properties("pluginSinceBuild").get()
        untilBuild = properties("pluginUntilBuild").get()

        changeNotes.set("""
            Bumped IntelliJ plugin versions (min: 223, max: 233.*), Compose Desktop dependencies to 1.4.0
        """)
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}