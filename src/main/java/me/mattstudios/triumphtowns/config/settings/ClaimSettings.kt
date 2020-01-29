package me.mattstudios.triumphtowns.config.settings

import ch.jalu.configme.Comment
import ch.jalu.configme.SettingsHolder
import ch.jalu.configme.configurationdata.CommentsConfiguration
import ch.jalu.configme.properties.Property
import ch.jalu.configme.properties.PropertyInitializer.newListProperty
import ch.jalu.configme.properties.PropertyInitializer.newProperty
import org.bukkit.Bukkit
import org.bukkit.Material

object ClaimSettings : SettingsHolder {

    /**
     * Claim configurations
     */

    /**
     * Worlds
     */
    @JvmField
    @Comment("Select in which worlds claiming is available [default: All]")
    val WORLDS: Property<List<String>> = newListProperty("allowed-worlds", *Bukkit.getWorlds().map { it.name }.toTypedArray())

    /**
     * Claim defaults
     */
    @JvmField
    @Comment("Minimum width a claim can be [default: 5]")
    val MINIMUM_WIDTH: Property<Int> = newProperty("defaults.minimum-width", 5)

    @JvmField
    @Comment("Minimum blocks that can be spent on claims [default: 100]")
    val MINIMUM_BLOCKS: Property<Int> = newProperty("defaults.minimum-blocks", 100)

    /**
     * Materials
     */
    @JvmField
    @Comment("Material use as the wand [default: GOLDEN_SHOVEL]")
    val WAND_MATERIAL: Property<String> = newProperty("materials.wand", Material.GOLDEN_SHOVEL.name)

    @JvmField
    @Comment("Material use as the main block of display [default: EMERALD_BLOCK]")
    val MAIN_CLAIM_MATERIAL: Property<String> = newProperty("materials.main-material", Material.EMERALD_BLOCK.name)

    @JvmField
    @Comment("Material use when the claim is invalid [default: REDSTONE_BLOCK]")
    val INVALID_CLAIM_MATERIAL: Property<String> = newProperty("materials.invalid-material", Material.REDSTONE_BLOCK.name)

    @JvmField
    @Comment("Material use in the side of the main materials [default: IRON_BLOCK]")
    val SIDES_CLAIM_MATERIAL: Property<String> = newProperty("materials.sides-material", Material.IRON_BLOCK.name)

    override fun registerComments(conf: CommentsConfiguration) {
        conf.setComment("defaults", "", "", "Default values for the claims")
        conf.setComment("materials", "", "", "Materials used in the claim borders display")
    }
}