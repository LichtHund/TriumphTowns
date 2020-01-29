package me.mattstudios.triumphtowns

import me.mattstudios.mattcore.MattPlugin
import me.mattstudios.mattcore.utils.MessageUtils
import me.mattstudios.triumphtowns.command.ReloadCommand
import me.mattstudios.triumphtowns.config.ClaimConfig
import me.mattstudios.triumphtowns.config.settings.ClaimSettings
import me.mattstudios.triumphtowns.config.settings.Settings
import me.mattstudios.triumphtowns.database.DBType
import me.mattstudios.triumphtowns.listener.ClaimListener
import me.mattstudios.triumphtowns.listener.PlayerListener
import me.mattstudios.triumphtowns.locale.Message
import me.mattstudios.triumphtowns.manager.DatabaseManager
import me.mattstudios.triumphtowns.manager.TownManager
import org.apache.commons.lang.StringUtils
import org.bukkit.event.Listener
import java.util.stream.Stream


class TriumphTowns : MattPlugin(), Listener {

    lateinit var townManager: TownManager
    lateinit var databaseManager: DatabaseManager
    lateinit var claimConfig: ClaimConfig

    companion object {
        var LOCALE = "en_US"
    }

    override fun onPluginEnable() {

        config.load(Settings::class.java)
        claimConfig = ClaimConfig(this, ClaimSettings::class.java)

        setupLocale()

        startUpMessage()

        registerListeners(
                ClaimListener(this),
                PlayerListener(this)
        )

        registerCommands(ReloadCommand(this))

        townManager = TownManager(this)
        databaseManager = DatabaseManager(this, DBType.SQLITE)
    }

    private fun setupLocale() {
        LOCALE = config[Settings.LANGUAGE]
        locale.load(Message::class.java, LOCALE)
    }

    private fun startUpMessage() {
        Stream.of(
                "",
                "&c&l▀█▀ █▀█ █░█░█ █▄░█ █▀",
                "&c&l░█░ █▄█ ▀▄▀▄▀ █░▀█ ▄█",
                "       &8Version: &c1.0 &8(&a$LOCALE&8)",
                ""
        ).forEach { MessageUtils.info(StringUtils.center(MessageUtils.color(it), 53)) }
    }


}