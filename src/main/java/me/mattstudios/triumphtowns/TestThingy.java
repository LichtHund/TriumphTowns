package me.mattstudios.triumphtowns;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import static me.mattstudios.triumphtowns.Utils.getHighestSolidBlockAt;

public final class TestThingy implements Runnable {

    private final Player player;
    private final World world;

    private int blocksCount;
    private int claimBlocks = 100;

    private Block blockA;
    private Block currentB;

    private Block previousB;
    private Block previousC;
    private Block previousD;

    private Block previousAA;
    private Block previousAB;
    private Block previousBA;
    private Block previousBB;

    public TestThingy(final Block blockA, final Player player) {
        this.blockA = blockA;
        this.player = player;

        world = blockA.getWorld();
    }

    @Override
    public void run() {

        pointerChange(blockA);

        blockAAHandler();
        blockABHandler();
        blockBAHandler();
        blockBBHandler();

        blockBHandler();
        blockCHandler();
        blockDHandler();

        countBlocks();
    }

    private void countBlocks() {
        blocksCount = (Math.abs(currentB.getX() - blockA.getX()) + 1) * (Math.abs(currentB.getZ() - blockA.getZ()) + 1);
    }

    private void pointerChange(Block block) {
        if (blocksCount > claimBlocks || blocksCount < 25)
            player.sendBlockChange(block.getLocation(), Material.GLOWSTONE.createBlockData());
        else
            player.sendBlockChange(block.getLocation(), Material.SEA_LANTERN.createBlockData());
    }

    private void sidesChange(Block block) {
        if (blocksCount > claimBlocks || blocksCount < 25)
            player.sendBlockChange(block.getLocation(), Material.REDSTONE_BLOCK.createBlockData());
        else
            player.sendBlockChange(block.getLocation(), Material.DIAMOND_BLOCK.createBlockData());
    }

    private void previousChange(Block block) {
        player.sendBlockChange(block.getLocation(), block.getType().createBlockData());
    }

    private void blockBHandler() {
        currentB = getTargetBlock(player, 15);

        if (currentB == null) return;

        if (currentB.getLocation().equals(blockA.getLocation()) && previousB != null) pointerChange(previousB);

        if (previousB == null) previousB = currentB;

        if (!currentB.getLocation().equals(previousB.getLocation())) {
            previousChange(previousB);
            previousB = currentB;
        }

        pointerChange(currentB);
    }

    private void blockCHandler() {
        if (currentB == null) {
            if (previousC != null) previousChange(previousC);
            return;
        }

        if (currentB.getLocation().equals(blockA.getLocation()) && previousC != null) previousChange(previousC);

        final Block currentC = getHighestSolidBlockAt(world, currentB.getX(), blockA.getZ());

        if (previousC == null) previousC = currentC;

        if (!currentC.getLocation().equals(previousC.getLocation())) {
            previousChange(previousC);
            previousC = currentC;
        }

        pointerChange(currentC);
    }

    private void blockDHandler() {
        if (currentB == null) {
            if (previousD != null) previousChange(previousD);
            return;
        }

        if (currentB.getLocation().equals(blockA.getLocation()) && previousD != null) previousChange(previousD);

        final Block currentD = getHighestSolidBlockAt(world, blockA.getX(), currentB.getZ());

        if (previousD == null) previousD = currentD;

        if (!currentD.getLocation().equals(previousD.getLocation())) {
            previousChange(previousD);
            previousD = currentD;
        }

        pointerChange(currentD);
    }

    private void blockAAHandler() {
        if (currentB == null) {
            if (previousAA != null) previousChange(previousAA);
            return;
        }

        if (currentB.getLocation().equals(blockA.getLocation()) && previousAA != null) previousChange(previousAA);

        Block currentAA = null;

        if (currentB.getX() > blockA.getX())
            currentAA = getHighestSolidBlockAt(world, blockA.getX() + 1, blockA.getZ());

        if (currentB.getX() < blockA.getX())
            currentAA = getHighestSolidBlockAt(world, blockA.getX() - 1, blockA.getZ());

        if (currentAA == null) return;

        if (previousAA == null) previousAA = currentAA;

        if (!currentAA.getLocation().equals(previousAA.getLocation())) {
            previousChange(previousAA);
            previousAA = currentAA;
        }

        sidesChange(currentAA);
    }

    private void blockBAHandler() {
        if (currentB == null) {
            if (previousBA != null) previousChange(previousBA);
            return;
        }

        if (currentB.getLocation().equals(blockA.getLocation()) && previousBA != null) previousChange(previousBA);

        Block currentBA = null;

        if (currentB.getX() > blockA.getX())
            currentBA = getHighestSolidBlockAt(world, currentB.getX() - 1, currentB.getZ());

        if (currentB.getX() < blockA.getX())
            currentBA = getHighestSolidBlockAt(world, currentB.getX() + 1, currentB.getZ());

        if (currentBA == null) return;

        if (previousBA == null) previousBA = currentBA;

        if (!currentBA.getLocation().equals(previousBA.getLocation())) {
            previousChange(previousBA);
            previousBA = currentBA;
        }

        sidesChange(currentBA);
    }

    private void blockABHandler() {
        if (currentB == null) {
            if (previousAB != null) previousChange(previousAB);
            return;
        }

        if (currentB.getLocation().equals(blockA.getLocation()) && previousAB != null) previousChange(previousAB);

        Block currentAB = null;

        if (currentB.getZ() > blockA.getZ())
            currentAB = getHighestSolidBlockAt(world, blockA.getX(), blockA.getZ() + 1);

        if (currentB.getZ() < blockA.getZ())
            currentAB = getHighestSolidBlockAt(world, blockA.getX(), blockA.getZ() - 1);

        if (currentAB == null) return;

        if (previousAB == null) previousAB = currentAB;

        if (!currentAB.getLocation().equals(previousAB.getLocation())) {
            previousChange(previousAB);
            previousAB = currentAB;
        }

        sidesChange(currentAB);
    }

    private void blockBBHandler() {
        if (currentB == null) {
            if (previousBB != null) previousChange(previousBB);
            return;
        }

        if (currentB.getLocation().equals(blockA.getLocation()) && previousBB != null) previousChange(previousBB);

        Block currentBB = null;

        if (currentB.getZ() > blockA.getZ())
            currentBB = getHighestSolidBlockAt(world, currentB.getX(), currentB.getZ() - 1);

        if (currentB.getZ() < blockA.getZ())
            currentBB = getHighestSolidBlockAt(world, currentB.getX(), currentB.getZ() + 1);

        if (currentBB == null) return;

        if (previousBB == null) previousBB = currentBB;

        if (!currentBB.getLocation().equals(previousBB.getLocation())) {
            previousChange(previousBB);
            previousBB = currentBB;
        }

        sidesChange(currentBB);
    }

    private Block getTargetBlock(final Player player, final int range) {
        BlockIterator iterator = new BlockIterator(player, range);
        Block lastBlock = iterator.next();

        while (iterator.hasNext()) {
            lastBlock = iterator.next();
            if (!lastBlock.getType().isSolid()) continue;
            break;
        }

        return lastBlock.getType().isSolid() ? lastBlock : null;
    }
}
