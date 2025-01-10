import dev.iurysouza.modulegraph.Theme

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    id("dev.iurysouza.modulegraph") version "0.10.1"
}
moduleGraphConfig {
    readmePath.set("./README.md") // Where the graph will be appended
    heading = "### Module Graph"  // Heading in the README
    showFullPath.set(false)       // Optional: Show full module paths
    theme.set(
        Theme.BASE(
            themeVariables = mapOf(
                "primaryTextColor" to "#FFFFFF",
                "primaryColor" to "#5A4F7C",
                "lineColor" to "#F5A623"
            ),
            focusColor = "#FA8140"
        )
    )
}