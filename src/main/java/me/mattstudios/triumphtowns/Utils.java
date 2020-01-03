package me.mattstudios.triumphtowns;

import org.bukkit.World;
import org.bukkit.block.Block;

public final class Utils {

    public static Block getHighestSolidBlockAt(World world, int x, int z) {
        Block highest = world.getHighestBlockAt(x, z);

        while (true) {
            if (highest.getType().isSolid() || highest.getY() == 0) return highest;

            highest = world.getBlockAt(highest.getX(), highest.getY() - 1, highest.getZ());
        }
    }

}
