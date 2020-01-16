package me.mattstudios.triumphtowns

import me.mattstudios.mattcore.MattPlugin
import me.mattstudios.mfgui.gui.GUI
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent


class TriumphTowns : MattPlugin(), Listener {

    private var clicked = false

    override fun onPluginEnable() {
        registerListener(this)
        var gui = GUI(this, "")

    }

    @EventHandler
    fun onTest(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK) return
        if (event.player.inventory.itemInMainHand.type != Material.GOLDEN_SHOVEL) return
        event.isCancelled = true

        if (!clicked) {
            clicked = true
            Bukkit.getScheduler().runTaskTimer(this, SelectionRunnableTest(event.clickedBlock!!, event.player), 0L, 1L)
            return
        }
    }

}