import java.util.Properties

fun loadEnv(): Properties {
    val envFile = File(rootDir, ".env")
    val properties = Properties()
    if (envFile.exists()) {
        envFile.inputStream().use { properties.load(it) }
    }
    return properties
}

val env = loadEnv()
fun getEnv(key: String): String? {
    return env.getProperty(key) ?: System.getenv(key)
}

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/trustwallet/wallet-core")
            credentials {
                username = getEnv("GITHUB_USERNAME")
                password = getEnv("GITHUB_TOKEN")
            }
        }
        google()
        mavenCentral()
    }
}

// Store repositories in Gradle properties
gradle.beforeProject {
    val repoList = dependencyResolutionManagement.repositories
        .filterIsInstance<MavenArtifactRepository>()
        .map { it.url.toString() }

    // Store as a Gradle property
    project.extensions.extraProperties.set("repoList", repoList)

    // Debug output
    println("📌 settings.gradle.kts - Found Repositories:")
    repoList.forEach { println(" - $it") }
}

rootProject.name = "FearthAndroidGDK"
include(":fearthgdk")
include(":sample")
