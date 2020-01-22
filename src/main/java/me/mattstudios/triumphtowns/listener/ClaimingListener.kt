package me.mattstudios.triumphtowns.listener

import me.mattstudios.triumphtowns.TriumphTowns
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

internal class ClaimingListener(private val plugin: TriumphTowns) : Listener {

    @EventHandler
    fun playerInteract(event: PlayerInteractEvent) {
        println("testing")
        if (!plugin.townManager.isTownPlayer(event.player.uniqueId)) return

        val townPlayer = plugin.townManager.getTownPlayer(event.player.uniqueId)
        println(townPlayer)
    }

}