package me.mattstudios.triumphtowns;

import me.mattstudios.mattscore.MattPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public final class TriumphTowns extends MattPlugin implements Listener {

    @Override
    protected void onPluginEnable() {
        registerListener(this);
    }

    @EventHandler
    public void onTest(PlayerInteractEvent event) {
        System.out.println(event.getAction().name());
    }

}
