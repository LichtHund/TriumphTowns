package me.mattstudios.triumphtowns

import me.mattstudios.mattcore.MattPlugin
import me.mattstudios.triumphtowns.command.TestCommand
import me.mattstudios.triumphtowns.config.Settings
import me.mattstudios.triumphtowns.database.DBType
import me.mattstudios.triumphtowns.listener.ClaimingListener
import me.mattstudios.triumphtowns.listener.PlayerListener
import me.mattstudios.triumphtowns.locale.Locales
import me.mattstudios.triumphtowns.locale.Message
import me.mattstudios.triumphtowns.manager.DatabaseManager
import me.mattstudios.triumphtowns.manager.TownManager
import org.bukkit.event.Listener


class TriumphTowns : MattPlugin(), Listener {

    lateinit var townManager: TownManager
    lateinit var databaseManager: DatabaseManager

    companion object {
        var LOCALE = Locales.EN_US
    }

    override fun onPluginEnable() {
        config.load(Settings::class.java)
        locale.load(Message::class.java, "en_US.yml")

        registerListeners(
                ClaimingListener(this),
                PlayerListener(this)
        )

        registerCommands(TestCommand(this))

        townManager = TownManager(this)
        databaseManager = DatabaseManager(this, DBType.SQLITE)
    }


}