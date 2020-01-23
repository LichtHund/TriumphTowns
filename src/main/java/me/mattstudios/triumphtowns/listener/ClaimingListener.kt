package me.mattstudios.triumphtowns.listener

import me.mattstudios.triumphtowns.TriumphTowns
import me.mattstudios.triumphtowns.config.Settings.CLAIM_ANIMATION
import me.mattstudios.triumphtowns.scheduler.ClaimAnimationScheduler
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import java.util.UUID

internal class ClaimingListener(private val plugin: TriumphTowns) : Listener {

    private val clicks: Map<UUID, Long> = HashMap()

    @EventHandler
    fun playerInteract(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK) return
        if (!event.hasItem()) return

        val player = event.player

        if (player.inventory.itemInMainHand.type != Material.GOLDEN_SHOVEL) return

        if (!plugin.townManager.isTownPlayer(player.uniqueId)) return

        event.isCancelled = true

        val townPlayer = plugin.townManager.getTownPlayer(player.uniqueId)

        if (plugin.config.get(CLAIM_ANIMATION)) {
            println("Starting")
            val claimSelectionScheduler = ClaimAnimationScheduler(plugin, player, townPlayer, event.clickedBlock!!, System.currentTimeMillis())
            Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, claimSelectionScheduler, 1L, 1L)
        }
    }

}