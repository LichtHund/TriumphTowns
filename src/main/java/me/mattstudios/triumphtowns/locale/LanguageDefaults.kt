package me.mattstudios.triumphtowns.locale

enum class LanguageDefaults(
        private var path: String,
        private var en: String,
        private var pt: String
) {

    CLAIM_ACTIONBAR_SMALL("claims.actionbar-claiming-small", "&c&lSelecting: &3&l{x} &7x &3&l{z} &7- &c&lClaim too small!", "Ola!"),
    CLAIM_ACTIONBAR_BIG("claims.actionbar-claiming-big", "&c&lSelecting: &3&l{x} &7x &3&l{z} &7- &7&lClaimblocks: &c&l{blocksleft}", "Ola!"),
    CLAIM_ACTIONBAR("claims.actionbar-claiming", "&a&lSelecting: &3&l{x} &7x &3&l{z} &7- &7&lClaimblocks: &a&l{blocksleft}", "Ola!");

    /**
     * Get's the path to the message
     */
    fun getPath(): String {
        return path
    }

    /**
     * Get's the message in that language
     */
    fun get(locale: String): String {
       when (locale) {
           "en_US" -> return en
           "pt_BR" -> return pt
           else -> return en
       }
    }

}