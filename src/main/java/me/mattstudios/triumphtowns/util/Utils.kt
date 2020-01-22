package me.mattstudios.triumphtowns.util

import org.bukkit.World
import org.bukkit.block.Block

object Utils {

    fun getHighestSolidBlockAt(world: World, x: Int, z: Int): Block {
        var highest = world.getHighestBlockAt(x, z)

        while (true) {
            if (highest.type.isSolid || highest.y == 0) return highest
            highest = world.getBlockAt(highest.x, highest.y - 1, highest.z)
        }
    }
}