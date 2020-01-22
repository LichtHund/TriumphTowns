package me.mattstudios.triumphtowns

import me.mattstudios.mattcore.MattPlugin
import me.mattstudios.triumphtowns.command.TestCommand
import me.mattstudios.triumphtowns.config.Settings
import me.mattstudios.triumphtowns.listener.ClaimingListener
import me.mattstudios.triumphtowns.locale.Locales
import me.mattstudios.triumphtowns.locale.Message
import me.mattstudios.triumphtowns.manager.TownManager
import org.bukkit.event.Listener


class TriumphTowns : MattPlugin(), Listener {

    val townManager: TownManager = TownManager()

    companion object {
        var LOCALE = Locales.EN_US
    }

    override fun onPluginEnable() {
        config.load(Settings::class.java)
        locale.load(Message::class.java, "en_US.yml")

        registerListeners(ClaimingListener(this))
        registerCommands(TestCommand(this))
    }


}