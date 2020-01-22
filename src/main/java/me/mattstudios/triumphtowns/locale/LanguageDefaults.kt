package me.mattstudios.triumphtowns.locale

enum class LanguageDefaults(
        private var path: String,
        private var en: String,
        private var pt: String
) {

    TEST_LANG("test.path", "Hello!", "Ola!");

    /**
     * Get's the path to the message
     */
    fun getPath(): String {
        return path
    }

    /**
     * Get's the message in that language
     */
    fun get(locale: Locales): String {
       when (locale) {
           Locales.EN_US -> return en
           Locales.PT_BR -> return pt
       }
    }

}