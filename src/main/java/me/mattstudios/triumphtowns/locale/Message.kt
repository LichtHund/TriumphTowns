package me.mattstudios.triumphtowns.locale

import ch.jalu.configme.Comment
import ch.jalu.configme.SettingsHolder
import ch.jalu.configme.properties.Property
import ch.jalu.configme.properties.PropertyInitializer
import me.mattstudios.triumphtowns.TriumphTowns

object Message : SettingsHolder {

    @JvmField
    @Comment("Testing a comment")
    val UPDATE_CHECK: Property<String> =
            PropertyInitializer.newProperty(LanguageDefaults.TEST_LANG.getPath(), LanguageDefaults.TEST_LANG.get(TriumphTowns.LOCALE))

}