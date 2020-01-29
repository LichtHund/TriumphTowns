package me.mattstudios.triumphtowns.config.settings

import ch.jalu.configme.Comment
import ch.jalu.configme.SettingsHolder
import ch.jalu.configme.properties.Property
import ch.jalu.configme.properties.PropertyInitializer.newProperty


object Settings : SettingsHolder {

    @JvmField
    @Comment("Make this false to disable update checking [default: true]")
    val UPDATE_CHECK: Property<Boolean> = newProperty("update-check", true)

    @JvmField
    @Comment("Language of the plugin [default: true]")
    val LANGUAGE: Property<String> = newProperty("language", "en_US")

}