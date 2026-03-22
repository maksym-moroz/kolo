package com.focus.kolo.buildlogic.convention.internal

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

internal fun Project.libs(): VersionCatalog = extensions
    .getByType<VersionCatalogsExtension>()
    .named("libs")

internal fun Project.version(alias: String): String = libs()
    .findVersion(alias)
    .get()
    .requiredVersion

internal fun Project.versionInt(alias: String): Int = version(alias)
    .toInt()
