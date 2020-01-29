package me.mattstudios.triumphtowns.command

import me.mattstudios.mattcore.utils.MessageUtils.color
import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.SubCommand
import me.mattstudios.mf.base.CommandBase
import me.mattstudios.triumphtowns.TriumphTowns
import me.mattstudios.triumphtowns.config.settings.Settings
import org.bukkit.command.CommandSender

@Command("tt")
class ReloadCommand(private val plugin: TriumphTowns) : CommandBase() {

    @SubCommand("reload")
    fun reload(sender: CommandSender) {

        plugin.config.reload()

        if (plugin.config[Settings.LANGUAGE].equals(TriumphTowns.LOCALE, true)) plugin.locale.reload()
        else {
            sender.sendMessage(color("The language have been changed, please restart the server!"))
        }

    }

}