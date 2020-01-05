package me.mattstudios.triumphtowns;

import me.mattstudios.mattscore.MattPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public final class TriumphTowns extends MattPlugin implements Listener {

    private boolean clicked;

    @Override
    protected void onPluginEnable() {
        registerListener(this);
    }

    @EventHandler
    public void onTest(PlayerInteractEvent event) {

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if (event.getPlayer().getInventory().getItemInMainHand().getType() != Material.GOLDEN_SHOVEL) return;

        event.setCancelled(true);

        if (!clicked) {
            clicked = true;

            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new TestThingy(event.getClickedBlock(), event.getPlayer()), 0L, 1L);

            return;
        }




    }

}
