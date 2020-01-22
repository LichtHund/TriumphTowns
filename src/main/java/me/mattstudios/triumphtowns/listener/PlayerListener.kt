package me.mattstudios.triumphtowns.listener

import me.mattstudios.triumphtowns.TriumphTowns
import me.mattstudios.triumphtowns.town.TownPlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerListener(private val plugin: TriumphTowns) : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        if (plugin.townManager.isTownPlayer(player.uniqueId)) return

        plugin.townManager.addTownPlayer(TownPlayer(player.uniqueId))
    }

}