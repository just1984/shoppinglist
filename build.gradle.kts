// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.detekt) apply false
}

// Apply ktlint and detekt to all subprojects
subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "io.gitlab.arturbosch.detekt")

    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        version.set("1.1.1")
        android.set(true)
        outputColorName.set("RED")
    }

    configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
        config.setFrom(rootProject.files("config/detekt/detekt.yml"))
        buildUponDefaultConfig = true
        allRules = false
    }

    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        jvmTarget = "17"
        reports {
            html.required.set(true)
            xml.required.set(true)
            txt.required.set(false)
            sarif.required.set(false)
        }
    }
}

// Root-level tasks for code quality
tasks.register("ktlintCheckAll") {
    description = "Run ktlint check on all modules"
    group = "verification"
    dependsOn(subprojects.map { it.tasks.named("ktlintCheck") })
}

tasks.register("ktlintFormatAll") {
    description = "Run ktlint format on all modules"
    group = "formatting"
    dependsOn(subprojects.map { it.tasks.named("ktlintFormat") })
}

tasks.register("detektAll") {
    description = "Run detekt on all modules"
    group = "verification"
    dependsOn(subprojects.map { it.tasks.matching { task -> task.name == "detekt" } })
}

tasks.register("codeQualityCheck") {
    description = "Run all code quality checks (ktlint + detekt)"
    group = "verification"
    dependsOn("ktlintCheckAll", "detektAll")
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
