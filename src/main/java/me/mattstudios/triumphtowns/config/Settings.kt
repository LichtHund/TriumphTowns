package me.mattstudios.triumphtowns.config

import ch.jalu.configme.Comment
import ch.jalu.configme.SettingsHolder
import ch.jalu.configme.configurationdata.CommentsConfiguration
import ch.jalu.configme.properties.Property
import ch.jalu.configme.properties.PropertyInitializer.newProperty
import org.bukkit.Material


object Settings : SettingsHolder {

    @JvmField
    @Comment("Make this false to disable update checking [default: true]")
    val UPDATE_CHECK: Property<Boolean> = newProperty("update-check", true)

    /**
     * Claim configurations
     */

    @JvmField
    @Comment("Turn this false to disable claiming animation [default: true]")
    val CLAIM_ANIMATION: Property<Boolean> = newProperty("claim.animation", true)

    @JvmField
    @Comment("Material to use as the main block of claims [default: EMERALD_BLOCK]")
    val MAIN_CLAIM_MATERIAL: Property<String> = newProperty("claim.main-material", Material.EMERALD_BLOCK.name)

    @JvmField
    @Comment("Material to use as the main block if the claim is invalid [default: REDSTONE_BLOCK]")
    val INVALID_CLAIM_MATERIAL: Property<String> = newProperty("claim.invalid-material", Material.REDSTONE_BLOCK.name)

    @JvmField
    @Comment("Material to use as the directional sides of the claim [default: IRON_BLOCK]")
    val SIDES_CLAIM_MATERIAL: Property<String> = newProperty("claim.sides-material", Material.IRON_BLOCK.name)


    override fun registerComments(conf: CommentsConfiguration) {
        val claimComment = arrayOf(
                "",
                "Configuration for the claiming section"
        )
        conf.setComment("claim", *claimComment)
    }

}