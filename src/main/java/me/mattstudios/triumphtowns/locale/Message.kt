package me.mattstudios.triumphtowns.locale

import ch.jalu.configme.Comment
import ch.jalu.configme.SettingsHolder
import ch.jalu.configme.properties.Property
import ch.jalu.configme.properties.PropertyInitializer
import me.mattstudios.triumphtowns.TriumphTowns

object Message : SettingsHolder {

    @JvmField
    @Comment("Actionbar message displayed when claim is too small")
    val CLAIM_ACTIONBAR_SMALL: Property<String> =
            PropertyInitializer.newProperty(LanguageDefaults.CLAIM_ACTIONBAR_SMALL.getPath(), LanguageDefaults.CLAIM_ACTIONBAR_SMALL.get(TriumphTowns.LOCALE))

    @JvmField
    @Comment("Actionbar message displayed when there isn't enough claimblocks to claim (claim is too big)")
    val CLAIM_ACTIONBAR_BIG: Property<String> =
            PropertyInitializer.newProperty(LanguageDefaults.CLAIM_ACTIONBAR_BIG.getPath(), LanguageDefaults.CLAIM_ACTIONBAR_BIG.get(TriumphTowns.LOCALE))

    @JvmField
    @Comment("Actionbar message displayed when claim is valid")
    val CLAIM_ACTIONBAR: Property<String> =
            PropertyInitializer.newProperty(LanguageDefaults.CLAIM_ACTIONBAR.getPath(), LanguageDefaults.CLAIM_ACTIONBAR.get(TriumphTowns.LOCALE))

}