package me.mattstudios.triumphtowns.scheduler

import me.mattstudios.mattcore.utils.MessageUtils.color
import me.mattstudios.triumphtowns.TriumphTowns
import me.mattstudios.triumphtowns.config.Settings.INVALID_CLAIM_MATERIAL
import me.mattstudios.triumphtowns.config.Settings.MAIN_CLAIM_MATERIAL
import me.mattstudios.triumphtowns.config.Settings.SIDES_CLAIM_MATERIAL
import me.mattstudios.triumphtowns.town.TownPlayer
import me.mattstudios.triumphtowns.util.Utils
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.util.BlockIterator
import kotlin.math.abs

class ClaimAnimationScheduler(plugin: TriumphTowns,
                              private val player: Player,
                              private val townPlayer: TownPlayer,
                              private val originBlock: Block,
                              private val clickTime: Long) : Runnable {

    private var mainMaterial = Material.getMaterial(plugin.config.get(MAIN_CLAIM_MATERIAL))
            ?: Material.EMERALD_BLOCK
    private var invalidMaterial = Material.getMaterial(plugin.config.get(INVALID_CLAIM_MATERIAL))
            ?: Material.REDSTONE_BLOCK
    private var sidesMaterial = Material.getMaterial(plugin.config.get(SIDES_CLAIM_MATERIAL))
            ?: Material.IRON_BLOCK

    private var blocksCount = 0

    private var lookingBlock = originBlock
    private var prevLooking = originBlock

    private var corner1Block = originBlock
    private var prevCorner1 = originBlock
    private var corner2Block = originBlock
    private var prevCorner2 = originBlock

    private var side1Looking = originBlock
    private var side2Looking = originBlock
    private var side1Origin = originBlock
    private var side2Origin = originBlock
    private var prevSide1Looking = originBlock
    private var prevSide2Looking = originBlock
    private var prevSide1Origin = originBlock
    private var prevSide2Origin = originBlock

    override fun run() {

        if (player.inventory.itemInMainHand.type != Material.GOLDEN_SHOVEL) {
            resetAll()
            return
        }

        changeMainBlock(player, originBlock)

        var color = "&a&l"

        if (blocksCount > townPlayer.claimBlocks) color = "&c&l"

        player.sendTitle("", color("${color}Selecting: &3${getXSelected()} ${color}x &3${getZSelected()}"), 0, 10, 10)

        val newLooking: Block? = getLookingBlock(player) ?: return

        if (sameLocation(lookingBlock, newLooking!!)) return

        this.lookingBlock = newLooking

        countBlocks()

        lookingBlockHandler(player)

        originSide1Handler(player)
        originSide2Handler(player)
        lookingSide1Handler(player)
        lookingSide2Handler(player)

        corner1BlockHandler(player)
        corner2BlockHandler(player)
    }

    /**
     * Counts how many blocks are being selected
     */
    private fun countBlocks() {
        blocksCount = getXSelected() * getZSelected()
    }

    private fun getXSelected(): Int {
        return abs(lookingBlock.x - originBlock.x) + 1
    }

    private fun getZSelected(): Int {
        return abs(lookingBlock.z - originBlock.z) + 1
    }

    /**
     * Changes the block player is looking at
     */
    private fun changeMainBlock(player: Player, block: Block) {
        if (blocksCount > townPlayer.claimBlocks) {
            player.sendBlockChange(block.location, invalidMaterial.createBlockData())
        } else {
            player.sendBlockChange(block.location, mainMaterial.createBlockData())
        }
    }

    /**
     * Change the blocks on the sides of the pointer and the main block
     */
    private fun sideChange(player: Player, block: Block) {
        player.sendBlockChange(block.location, sidesMaterial.createBlockData())
    }

    /**
     * Resets the previous blocks to their original state
     */
    private fun resetBlock(player: Player, block: Block) {
        player.sendBlockChange(block.location, block.type.createBlockData())
    }

    private fun sameLocation(block: Block, block2: Block): Boolean {
        return block.location == block2.location
    }

    /**
     * Handles the changes on the looking block
     */
    private fun lookingBlockHandler(player: Player) {
        changeMainBlock(player, lookingBlock)

        if (sameLocation(lookingBlock, prevLooking)) return

        if (!sameLocation(prevLooking, originBlock)) {
            resetBlock(player, prevLooking)
        }

        if (sameLocation(lookingBlock, originBlock)) return

        prevLooking = lookingBlock
    }

    /**
     * Handles the changes on the corner 1 block
     */
    private fun corner1BlockHandler(player: Player) {
        corner1Block = Utils.getHighestSolidBlockAt(player.world, lookingBlock.x, originBlock.z)

        if (sameLocation(corner1Block, prevCorner1) && !sameLocation(lookingBlock, prevLooking)) return
        if (lookingBlock.z == originBlock.z) return
        if (!sameLocation(prevCorner1, originBlock) && !sameLocation(prevCorner1, side2Origin)) resetBlock(player, prevCorner1)
        if (sameLocation(corner1Block, originBlock)) return

        changeMainBlock(player, corner1Block)

        prevCorner1 = corner1Block
    }

    /**
     * Handles the changes on the corner 2 block
     */
    private fun corner2BlockHandler(player: Player) {
        corner2Block = Utils.getHighestSolidBlockAt(player.world, originBlock.x, lookingBlock.z)

        if (sameLocation(corner2Block, prevCorner2) && !sameLocation(lookingBlock, prevLooking)) return
        if (lookingBlock.x == originBlock.x) return
        if (!sameLocation(prevCorner2, originBlock) && !sameLocation(prevCorner2, side1Origin)) resetBlock(player, prevCorner2)
        if (sameLocation(corner2Block, originBlock)) return

        changeMainBlock(player, corner2Block)

        prevCorner2 = corner2Block
    }

    /**
     * Handles the changes for the origin sides
     */
    private fun originSide1Handler(player: Player) {

        if (lookingBlock.z > originBlock.z)
            side1Origin = Utils.getHighestSolidBlockAt(player.world, originBlock.x, originBlock.z + 1)
        if (lookingBlock.z < originBlock.z)
            side1Origin = Utils.getHighestSolidBlockAt(player.world, originBlock.x, originBlock.z - 1)
        if (lookingBlock.z == originBlock.z)
            side1Origin = Utils.getHighestSolidBlockAt(player.world, originBlock.x, originBlock.z)

        if (sameLocation(side1Origin, prevSide1Origin) && !sameLocation(lookingBlock, prevLooking)) return
        if (!sameLocation(prevSide1Origin, originBlock) && !sameLocation(prevSide1Origin, lookingBlock)) resetBlock(player, prevSide1Origin)
        if (sameLocation(side1Origin, originBlock)) return
        if (sameLocation(side1Origin, lookingBlock)) return

        sideChange(player, side1Origin)

        prevSide1Origin = side1Origin
    }

    /**
     * Handles the changes for the origin sides
     */
    private fun originSide2Handler(player: Player) {

        if (lookingBlock.x > originBlock.x)
            side2Origin = Utils.getHighestSolidBlockAt(player.world, originBlock.x + 1, originBlock.z)
        if (lookingBlock.x < originBlock.x)
            side2Origin = Utils.getHighestSolidBlockAt(player.world, originBlock.x - 1, originBlock.z)
        if (lookingBlock.x == originBlock.x)
            side2Origin = Utils.getHighestSolidBlockAt(player.world, originBlock.x, originBlock.z)

        if (sameLocation(side2Origin, prevSide2Origin) && !sameLocation(lookingBlock, prevLooking)) return
        if (!sameLocation(prevSide2Origin, originBlock) && !sameLocation(prevSide2Origin, lookingBlock)) resetBlock(player, prevSide2Origin)
        if (sameLocation(side2Origin, originBlock)) return
        if (sameLocation(side2Origin, lookingBlock)) return

        sideChange(player, side2Origin)

        prevSide2Origin = side2Origin
    }

    /**
     * Handles the changes for the looking sides
     */
    private fun lookingSide1Handler(player: Player) {
        if (lookingBlock.x > originBlock.x)
            side1Looking = Utils.getHighestSolidBlockAt(player.world, lookingBlock.x - 1, lookingBlock.z)
        if (lookingBlock.x < originBlock.x)
            side1Looking = Utils.getHighestSolidBlockAt(player.world, lookingBlock.x + 1, lookingBlock.z)
        if (lookingBlock.x == originBlock.x)
            side1Looking = Utils.getHighestSolidBlockAt(player.world, lookingBlock.x, lookingBlock.z)

        if (sameLocation(side1Looking, prevSide1Looking) && !sameLocation(lookingBlock, prevLooking)) return

        if (!sameLocation(prevSide1Looking, originBlock) &&
                !sameLocation(prevSide1Looking, lookingBlock) &&
                !sameLocation(prevSide1Looking, side1Origin) &&
                !sameLocation(prevSide1Looking, side2Origin)) {
            resetBlock(player, prevSide1Looking)
        }

        if (sameLocation(side1Looking, originBlock)) return
        if (sameLocation(side1Looking, lookingBlock)) return

        sideChange(player, side1Looking)

        prevSide1Looking = side1Looking
    }

    /**
     * Handles the changes for the looking sides
     */
    private fun lookingSide2Handler(player: Player) {

        if (lookingBlock.z > originBlock.z)
            side2Looking = Utils.getHighestSolidBlockAt(player.world, lookingBlock.x, lookingBlock.z - 1)
        if (lookingBlock.z < originBlock.z)
            side2Looking = Utils.getHighestSolidBlockAt(player.world, lookingBlock.x, lookingBlock.z + 1)
        if (lookingBlock.z == originBlock.z)
            side2Looking = Utils.getHighestSolidBlockAt(player.world, lookingBlock.x, lookingBlock.z)


        if (sameLocation(side2Looking, prevSide2Looking) && !sameLocation(lookingBlock, prevLooking)) return

        if (!sameLocation(prevSide2Looking, originBlock) &&
                !sameLocation(prevSide2Looking, lookingBlock) &&
                !sameLocation(prevSide2Looking, side1Origin) &&
                !sameLocation(prevSide2Looking, side2Origin)) {
            resetBlock(player, prevSide2Looking)
        }

        if (sameLocation(side2Looking, originBlock)) return
        if (sameLocation(side2Looking, lookingBlock)) return

        sideChange(player, side2Looking)

        prevSide2Looking = side2Looking
    }

    /**
     * Resets all the blocks to their default stage
     */
    private fun resetAll() {
        originBlock.state.update()
        lookingBlock.state.update()
        corner1Block.state.update()
        corner2Block.state.update()
        side1Looking.state.update()
        side2Looking.state.update()
        side1Origin.state.update()
        side2Origin.state.update()

        lookingBlock = originBlock
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