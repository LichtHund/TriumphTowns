package me.mattstudios.triumphtowns.config

import ch.jalu.configme.SettingsHolder
import ch.jalu.configme.SettingsManager
import ch.jalu.configme.SettingsManagerBuilder
import ch.jalu.configme.configurationdata.ConfigurationDataBuilder
import ch.jalu.configme.properties.Property
import org.bukkit.plugin.Plugin
import java.io.File

class ClaimConfig(plugin: Plugin, claimsClass: Class<out SettingsHolder>) {

    // The settings manager of the config
    private val settingsManager: SettingsManager = SettingsManagerBuilder.withYamlFile(
            File(plugin.dataFolder, "claims.yml"))
            .useDefaultMigrationService()
            .configurationData(ConfigurationDataBuilder.createConfiguration(claimsClass))
            .create()

    /**
     * Gets a property
     *
     * @param property The property to get
     * @return The type of property from the config
     */
    operator fun <T> get(property: Property<T>): T {
        return settingsManager.getProperty(property)
    }

    /**
     * Reloads the config
     */
    fun reload() {
        settingsManager.reload()
    }

}