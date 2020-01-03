package me.mattstudios.triumphtowns;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import static me.mattstudios.triumphtowns.Utils.getHighestSolidBlockAt;

public final class TestThingy implements Runnable {

    private Location blockALocation;
    private Player player;
    private Block previousB;
    private Block previousC;
    private Block previousD;

    public TestThingy(final Location blockALocation, final Player player) {
        this.blockALocation = blockALocation;
        this.player = player;
    }

    @Override
    public void run() {
        //player.sendMessage("Start - " + startLocation.getX() + " " + startLocation.getY() + " " + startLocation.getZ());
        Block currentB = getTargetBlock(player, 15);

        if (currentB == null) return;
        if (currentB.getLocation().equals(blockALocation)) return;

        if (previousB == null) previousB = currentB;

        if (!currentB.getLocation().equals(previousB.getLocation())) {
            player.sendBlockChange(previousB.getLocation(), previousB.getType().createBlockData());
            previousB = currentB;
        } else {
            player.sendBlockChange(currentB.getLocation(), Material.DIAMOND_BLOCK.createBlockData());
        }

        Block currentC = getHighestSolidBlockAt(currentB.getWorld(), currentB.getX(), blockALocation.getBlockZ());
        Block currentD = getHighestSolidBlockAt(currentB.getWorld(), blockALocation.getBlockX(), currentB.getZ());

        if (currentC.getLocation().equals(blockALocation)) return;
        if (currentD.getLocation().equals(blockALocation)) return;

        if (previousC == null) previousC = currentC;
        if (previousD == null) previousD = currentD;

        if (!currentC.getLocation().equals(previousC.getLocation())) {
            player.sendBlockChange(previousC.getLocation(), previousC.getType().createBlockData());
            previousC = currentC;
        } else {
            player.sendBlockChange(currentC.getLocation(), Material.DIAMOND_BLOCK.createBlockData());
        }

        if (!currentD.getLocation().equals(previousD.getLocation())) {
            player.sendBlockChange(previousD.getLocation(), previousD.getType().createBlockData());
            previousD = currentD;
        } else {
            player.sendBlockChange(currentD.getLocation(), Material.DIAMOND_BLOCK.createBlockData());
        }
    }

    public final Block getTargetBlock(Player player, int range) {
        BlockIterator iter = new BlockIterator(player, range);
        Block lastBlock = iter.next();
        while (iter.hasNext()) {
            lastBlock = iter.next();
            if (!lastBlock.getType().isSolid()) {
                continue;
            }
            break;
        }
        return lastBlock.getType().isSolid() ? lastBlock : null;
    }
}
