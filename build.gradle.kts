plugins {
    id("java-library")
    id("eclipse")
    id("idea")
    id("maven-publish")
    id("net.neoforged.gradle.userdev") version "7.0.152"
    id("org.cadixdev.licenser") version "0.6.1"
}

// Variables
internal val minecraftVersion: String = extra.get("minecraft_version").toString()
internal val minecraftVersionRange: String = extra.get("minecraft_version_range").toString()
internal val neoVersion: String = extra.get("neo_version").toString()
internal val neoVersionRange: String = extra.get("neo_version_range").toString()
internal val loaderVersionRange: String = extra.get("loader_version_range").toString()
internal val modId: String = extra.get("mod_id").toString()
internal val modName: String = extra.get("mod_name").toString()
internal val modLicense: String = extra.get("mod_license").toString()
internal val modVersion: String = extra.get("mod_version").toString()
internal val modGroupId: String = extra.get("mod_group_id").toString()
internal val modAuthors: String = extra.get("mod_authors").toString()
internal val modDescription: String = extra.get("mod_description").toString()

// Integrations
internal val jeiVersion: String = extra.get("jei_version").toString()

group = modGroupId
base.archivesName.set(modId.replace('_', '-'))
version = modVersion

java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))

// Handle source sets
internal val generatedSourceSet: SourceSet = sourceSets.create("generated")
sourceSets["main"].resources {
    source(generatedSourceSet.resources)
    exclude("./cache")
}

// TODO: Change to use main source set directly
accessTransformers {
    file("src/main/resources/META-INF/accesstransformer.cfg")
}

runs {
    // Apply to all run configurations
    configureEach {
        // Recommended logging data for an userdev environment
        // The markers can be added/remove as needed separated by commas.
        // "SCAN": For mods scan.
        // "REGISTRIES": For firing of registry events.
        // "REGISTRYDUMP": For getting the contents of all registries.
        systemProperty("forge.logging.markers", "REGISTRIES")

        // Recommended logging level for the console
        // You can set various levels here.
        // Please read: https://stackoverflow.com/questions/2031163/when-to-use-the-different-log-levels
        systemProperty("forge.logging.console.level", "debug")

        modSource(sourceSets["main"])
    }

    create("client") {
        systemProperty("neoforge.enabledGameTestNamespaces", modId)
    }

    create("server") {
        systemProperty("neoforge.enabledGameTestNamespaces", modId)
        programArguments("--nogui")
    }

    create("gameTestServer") {
        systemProperty("neoforge.enabledGameTestNamespaces", modId)
    }

    create("data") {
        programArguments("--mod", modId, "--all", "--output", generatedSourceSet.resources.srcDirs.first().absolutePath)
        sourceSets["main"].resources.srcDirs.forEach { programArguments("--existing", it.absolutePath) }
    }
}

repositories {
    mavenLocal()
    maven {
        name = "Jared's Maven"
        url = uri("https://maven.blamejared.com/")
    }
    maven {
        name = "ModMaven"
        url = uri("https://modmaven.dev")
    }
}

configurations {
    /*
     * Sets up a dependency configuration called 'localRuntime'.
     * This configuration should be used instead of 'runtimeOnly' to declare
     * a dependency that will be present for runtime testing but that is
     * "optional", meaning it will not be pulled by dependents of this mod.
     */
    runtimeClasspath {
        extendsFrom(configurations.localRuntime.get())
    }
}

dependencies {
    // NeoForge dependency
    implementation(group = "net.neoforged", name = "neoforge", version = neoVersion)

    // JEI Integration
    compileOnly(group = "mezz.jei", name = "jei-${minecraftVersion}-common-api", version = jeiVersion)
    compileOnly(group = "mezz.jei", name = "jei-${minecraftVersion}-neoforge-api", version = jeiVersion)
    localRuntime(group = "mezz.jei", name = "jei-${minecraftVersion}-neoforge", version = jeiVersion)
}

tasks.withType<ProcessResources> {
    val replaceProperties: Map<String, String> = mapOf(
        "minecraft_version" to minecraftVersion,
        "minecraft_version_range" to minecraftVersionRange,
        "neo_version" to neoVersion,
        "neo_version_range" to neoVersionRange,
        "loader_version_range" to loaderVersionRange,
        "mod_id" to modId,
        "mod_name" to modName,
        "mod_license" to modLicense,
        "mod_version" to modVersion,
        "mod_authors" to modAuthors,
        "mod_description" to modDescription
    )

    inputs.properties(replaceProperties)

    filesMatching("META-INF/neoforge.mods.toml") {
        expand(replaceProperties)
    }
}

license {
    header.set(resources.text.fromFile("HEADER"))

    properties {
        set("mod_authors", modAuthors)
        set("mod_license", modLicense)
    }

    include("**/*.java")
}

tasks.withType<JavaCompile> {
    // Use the UTF-8 charset for Java compilation
    options.encoding = "UTF-8"
}

// IDEA no longer automatically downloads sources/javadoc jars for dependencies, so we need to explicitly enable the behavior.
idea.module {
    isDownloadSources = true
    isDownloadJavadoc = true
}
