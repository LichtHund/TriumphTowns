package me.mattstudios.triumphtowns.config

import ch.jalu.configme.Comment
import ch.jalu.configme.SettingsHolder
import ch.jalu.configme.properties.Property
import ch.jalu.configme.properties.PropertyInitializer.newProperty


object Settings : SettingsHolder {

    @JvmField
    @Comment("Testing a comment")
    val UPDATE_CHECK: Property<Boolean> = newProperty("update-check", true)

    @JvmField
    @Comment("Testing a comment")
    val ANOTHER: Property<String> = newProperty("another", "test")

}