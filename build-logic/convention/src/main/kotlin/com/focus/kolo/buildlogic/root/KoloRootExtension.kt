package com.focus.kolo.buildlogic.root

import javax.inject.Inject
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty

public abstract class KoloRootExtension
@Inject
constructor(
    objects: ObjectFactory
) {
    public val qualityModules: ListProperty<String> =
        objects
            .listProperty(String::class.java)
            .convention(emptyList())

    public val dependencyHealthModules: ListProperty<String> =
        objects
            .listProperty(String::class.java)
            .convention(emptyList())
}
