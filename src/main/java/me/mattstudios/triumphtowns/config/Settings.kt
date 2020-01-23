package me.mattstudios.triumphtowns.config

import ch.jalu.configme.Comment
import ch.jalu.configme.SettingsHolder
import ch.jalu.configme.configurationdata.CommentsConfiguration
import ch.jalu.configme.properties.Property
import ch.jalu.configme.properties.PropertyInitializer.newProperty
import org.bukkit.Material


object Settings : SettingsHolder {

    @JvmField
    @Comment("Make this false to disable update checking")
    val UPDATE_CHECK: Property<Boolean> = newProperty("update-check", true)

    /**
     * Claim configurations
     */

    @JvmField
    @Comment("Turn this false to disable claiming animation")
    val CLAIM_ANIMATION: Property<Boolean> = newProperty("claim.animation", true)

    @JvmField
    @Comment("Material to use as the main block of claims")
    val MAIN_CLAIM_MATERIAL: Property<String> = newProperty("claim.claim-material", Material.EMERALD_BLOCK.name)


    override fun registerComments(conf: CommentsConfiguration) {
        val claimComment = arrayOf(
                "",
                "Configuration for the claiming section"
        )
        conf.setComment("claim", *claimComment)
    }

}