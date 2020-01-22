package me.mattstudios.triumphtowns

import me.mattstudios.triumphtowns.util.Utils.getHighestSolidBlockAt
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.util.BlockIterator
import kotlin.math.abs

class SelectionRunnableTest(private val mainBlock: Block, private val player: Player) : Runnable {

    private var world: World = mainBlock.world

    // Testing variables only
    private var blocksCount = 0
    private var claimBlocks = 100

    // Variables for the pointer block that follows the cursor
    private var pointerBlock: Block? = null
    private var pointerPrevious: Block? = null

    // Variables for the corners of the rectangular selection
    private var cornerAPrevious: Block? = null
    private var cornerBPrevious: Block? = null

    // Variables for the sides of the pointer
    private var pointerSideAPrevious: Block? = null
    private var pointerSideBPrevious: Block? = null

    // Variables for the sides of the main block
    private var mainSideAPrevious: Block? = null
    private var mainSideBPrevious: Block? = null

    override fun run() {
        pointerChange(mainBlock)

        pointerBlockHandler()
        cornerABlockHandler()
        cornerBBlockHandler()

        pointerSideAHandler()
        pointerSideBHandler()
        mainSideAHandler()
        mainSideBHandler()

        countBlocks()
    }

    /**
     * Counts how many blocks are being selected
     */
    private fun countBlocks() {
        if (pointerBlock == null) return
        blocksCount = (abs(pointerBlock!!.x - mainBlock.x) + 1) * (abs(pointerBlock!!.z - mainBlock.z) + 1)
    }

    /**
     * Changes the block player is looking at
     */
    private fun pointerChange(block: Block) {
        // 25 is just a test number for smallest amount
        if (blocksCount > claimBlocks || blocksCount < 25)
            player.sendBlockChange(block.location, Material.GLOWSTONE.createBlockData())
        else
            player.sendBlockChange(block.location, Material.SEA_LANTERN.createBlockData())
    }

    /**
     * Change the blocks on the sides of the pointer and the main block
     */
    private fun sideChange(block: Block) {
        // 25 is just a test number for smallest amount
        if (blocksCount > claimBlocks || blocksCount < 25)
            player.sendBlockChange(block.location, Material.REDSTONE_BLOCK.createBlockData())
        else
            player.sendBlockChange(block.location, Material.DIAMOND_BLOCK.createBlockData())
    }

    /**
     * Resets the previous blocks to their original state
     */
    private fun resetBlock(block: Block) {
        player.sendBlockChange(block.location, block.type.createBlockData())
    }

    /**
     * Handles changes for the pointer block
     */
    private fun pointerBlockHandler() {
        pointerBlock = getLookingBlock(player)

        if (pointerBlock == null) return

        if (pointerBlock?.location == mainBlock.location && pointerPrevious != null) pointerChange(pointerPrevious!!)

        if (pointerPrevious == null) pointerPrevious = pointerBlock

        if (pointerBlock?.location != pointerPrevious?.location) {
            resetBlock(pointerPrevious!!)
            pointerPrevious = pointerBlock
        }

        pointerChange(pointerBlock!!)
    }

    /**
     * Handles changes to the corner block A
     */
    private fun cornerABlockHandler() {
        if (pointerBlock == null) {
            if (cornerAPrevious != null) resetBlock(cornerAPrevious!!)
            return
        }

        if (pointerBlock?.location == mainBlock.location && cornerAPrevious != null) resetBlock(cornerAPrevious!!)

        val cornerABlock = getHighestSolidBlockAt(world, pointerBlock!!.x, mainBlock.z)

        if (cornerAPrevious == null) cornerAPrevious = cornerABlock

        if (cornerABlock.location != cornerAPrevious?.location) {
            resetBlock(cornerAPrevious!!)
            cornerAPrevious = cornerABlock
        }

        pointerChange(cornerABlock)
    }

    /**
     * Handles changes to the corner B
     */
    private fun cornerBBlockHandler() {
        if (pointerBlock == null) {
            if (cornerBPrevious != null) resetBlock(cornerBPrevious!!)
            return
        }

        if (pointerBlock?.location == mainBlock.location && cornerBPrevious != null) resetBlock(cornerBPrevious!!)

        val currentD = getHighestSolidBlockAt(world, mainBlock.x, pointerBlock!!.z)

        if (cornerBPrevious == null) cornerBPrevious = currentD

        if (currentD.location != cornerBPrevious?.location) {
            resetBlock(cornerBPrevious!!)
            cornerBPrevious = currentD
        }

        pointerChange(currentD)
    }

    /**
     * Handles changes to the side block A for the main block
     */
    private fun mainSideAHandler() {
        if (pointerBlock == null) {
            if (mainSideAPrevious != null) resetBlock(mainSideAPrevious!!)
            return
        }

        if (pointerBlock?.location == mainBlock.location && mainSideAPrevious != null) resetBlock(mainSideAPrevious!!)

        var mainSideA: Block? = null

        if (pointerBlock!!.x > mainBlock.x)
            mainSideA = getHighestSolidBlockAt(world, mainBlock.x + 1, mainBlock.z)
        if (pointerBlock!!.x < mainBlock.x)
            mainSideA = getHighestSolidBlockAt(world, mainBlock.x - 1, mainBlock.z)

        if (mainSideA == null) return

        if (mainSideAPrevious == null) mainSideAPrevious = mainSideA

        if (mainSideA.location != mainSideAPrevious?.location) {
            resetBlock(mainSideAPrevious!!)
            mainSideAPrevious = mainSideA
        }

        sideChange(mainSideA)
    }

    /**
     * Handles changes to the side block B for the main block
     */
    private fun mainSideBHandler() {
        if (pointerBlock == null) {
            if (mainSideBPrevious != null) resetBlock(mainSideBPrevious!!)
            return
        }

        if (pointerBlock?.location == mainBlock.location && mainSideBPrevious != null) resetBlock(mainSideBPrevious!!)

        var currentAB: Block? = null

        if (pointerBlock!!.z > mainBlock.z)
            currentAB = getHighestSolidBlockAt(world, mainBlock.x, mainBlock.z + 1)
        if (pointerBlock!!.z < mainBlock.z)
            currentAB = getHighestSolidBlockAt(world, mainBlock.x, mainBlock.z - 1)

        if (currentAB == null) return

        if (mainSideBPrevious == null) mainSideBPrevious = currentAB

        if (currentAB.location != mainSideBPrevious?.location) {
            resetBlock(mainSideBPrevious!!)
            mainSideBPrevious = currentAB
        }

        sideChange(currentAB)
    }

    /**
     * Handles changes to the side block A for the pointer block
     */
    private fun pointerSideAHandler() {
        if (pointerBlock == null) {
            if (pointerSideAPrevious != null) resetBlock(pointerSideAPrevious!!)
            return
        }

        if (pointerBlock!!.location == mainBlock.location && pointerSideAPrevious != null) resetBlock(pointerSideAPrevious!!)

        var pointerSideA: Block? = null

        if (pointerBlock!!.x > mainBlock.x)
            pointerSideA = getHighestSolidBlockAt(world, pointerBlock!!.x - 1, pointerBlock!!.z)
        if (pointerBlock!!.x < mainBlock.x)
            pointerSideA = getHighestSolidBlockAt(world, pointerBlock!!.x + 1, pointerBlock!!.z)

        if (pointerSideA == null) return

        if (pointerSideAPrevious == null) pointerSideAPrevious = pointerSideA

        if (pointerSideA.location != pointerSideAPrevious?.location) {
            resetBlock(pointerSideAPrevious!!)
            pointerSideAPrevious = pointerSideA
        }

        sideChange(pointerSideA)
    }

    /**
     * Handles changes to the side block B for the pointer block
     */
    private fun pointerSideBHandler() {
        if (pointerBlock == null) {
            if (pointerSideBPrevious != null) resetBlock(pointerSideBPrevious!!)
            return
        }

        if (pointerBlock?.location == mainBlock.location && pointerSideBPrevious != null) resetBlock(pointerSideBPrevious!!)

        var pointerSideB: Block? = null

        if (pointerBlock!!.z > mainBlock.z)
            pointerSideB = getHighestSolidBlockAt(world, pointerBlock!!.x, pointerBlock!!.z - 1)
        if (pointerBlock!!.z < mainBlock.z)
            pointerSideB = getHighestSolidBlockAt(world, pointerBlock!!.x, pointerBlock!!.z + 1)

        if (pointerSideB == null) return

        if (pointerSideBPrevious == null) pointerSideBPrevious = pointerSideB

        if (pointerSideB.location != pointerSideBPrevious?.location) {
            resetBlock(pointerSideBPrevious!!)
            pointerSideBPrevious = pointerSideB
        }

        sideChange(pointerSideB)
    }

    /**
     * Gets the block the player is looking at
     */
    private fun getLookingBlock(player: Player): Block? {
        val iterator = BlockIterator(player, 10)
        var lastBlock = iterator.next()

        while (iterator.hasNext()) {
            lastBlock = iterator.next()
            if (!lastBlock.type.isSolid) continue
            break
        }

        return if (lastBlock.type.isSolid) lastBlock else null
    }

}