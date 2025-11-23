pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "GiftTrack"

include(":app")

// Core modules
include(":core:common")
include(":core:domain")
include(":core:data")
include(":core:database")
include(":core:network")
include(":core:ui")

// Feature modules
include(":feature:orders")
include(":feature:shops")
include(":feature:recipients")
include(":feature:tracking")
include(":feature:settings")
