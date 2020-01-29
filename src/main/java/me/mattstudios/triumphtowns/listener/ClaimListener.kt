package me.mattstudios.triumphtowns.listener

import me.mattstudios.triumphtowns.TriumphTowns
import me.mattstudios.triumphtowns.scheduler.ClaimAnimation
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.UUID

internal class ClaimListener(private val plugin: TriumphTowns) : Listener {

    // Saves people who have clicked
    private val claimingList: MutableMap<UUID, ClaimAnimation> = HashMap()
    private val cooldowns: MutableSet<UUID> = HashSet()

    @EventHandler(ignoreCancelled = true)
    fun playerClaimAnimation(event: PlayerInteractEvent) {
        // Checks for claiming stuff
        if (event.action != Action.RIGHT_CLICK_BLOCK) return
        if (event.clickedBlock == null) return
        if (!event.hasItem()) return

        // The player
        val player = event.player

        // Checks if is it's the claiming wand
        if (player.inventory.itemInMainHand.type != Material.GOLDEN_SHOVEL) return

        // Checks if it's a town player or not
        if (!plugin.townManager.isTownPlayer(player)) return

        // Cancels the event
        event.isCancelled = true

        // Gets the town player
        val townPlayer = plugin.townManager.getTownPlayer(player)

        // Starts the claim animation
        if (claimingList[player.uniqueId] == null && !cooldowns.contains(player.uniqueId)) {

            // Starts the claiming animation
            val claimAnimation = ClaimAnimation(plugin, player, townPlayer, event.clickedBlock!!)
            val taskId = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, claimAnimation, 1L, 1L).taskId

            // Adds in the task ID
            claimAnimation.taskId = taskId

            // Adds the claim animation to the list
            claimingList[player.uniqueId] = claimAnimation

            // Runs task later to cancel the claiming if it's not claimed in time
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, Runnable {
                if (claimAnimation.isCancelled) return@Runnable
                cancelClaiming(claimAnimation, player)
            }, 400L)

            return
        }

        // Checks if the claim is on cooldown
        if (cooldowns.contains(player.uniqueId)) return

        // Gets the claim animation object
        val claimAnimation: ClaimAnimation = claimingList[player.uniqueId]!!

        if (claimAnimation.isCancelled) return

        if (!claimAnimation.isValidClaim()) return

        cancelClaiming(claimAnimation, player)

        cooldowns.add(player.uniqueId)

        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, Runnable {
            cooldowns.remove(player.uniqueId)
        }, 30L)

    }

    @EventHandler
    fun playerLeave(event: PlayerQuitEvent) {
        if (claimingList[event.player.uniqueId] == null) return

        val claimAnimation: ClaimAnimation = claimingList[event.player.uniqueId]!!

        cancelClaiming(claimAnimation, event.player)
    }

    /**
     * Cancels the claiming
     */
    private fun cancelClaiming(claimAnimation: ClaimAnimation, player: Player) {
        claimAnimation.cancel()
        claimingList.remove(player.uniqueId)
    }

}