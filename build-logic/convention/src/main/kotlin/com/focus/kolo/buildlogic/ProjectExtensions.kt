package com.focus.kolo.buildlogic

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

internal fun Project.libs(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun Project.versionInt(alias: String): Int =
    libs()
        .findVersion(alias)
        .get()
        .requiredVersion
        .toInt()
