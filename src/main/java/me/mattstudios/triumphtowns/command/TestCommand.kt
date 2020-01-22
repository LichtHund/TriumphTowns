package me.mattstudios.triumphtowns.command

import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Default
import me.mattstudios.mf.base.CommandBase
import me.mattstudios.triumphtowns.TriumphTowns
import me.mattstudios.triumphtowns.town.TownPlayer
import org.bukkit.entity.Player

@Command("cmd")
class TestCommand(private val plugin: TriumphTowns) : CommandBase() {

    @Default
    fun testCommand(player: Player) {
        player.sendMessage("Set test")
        plugin.townManager.addTownPlayer(TownPlayer(player.uniqueId))
    }

}