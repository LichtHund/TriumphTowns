package me.mattstudios.triumphtowns.scheduler

import io.github.theluca98.textapi.ActionBar
import me.mattstudios.triumphtowns.TriumphTowns
import me.mattstudios.triumphtowns.config.settings.ClaimSettings.INVALID_CLAIM_MATERIAL
import me.mattstudios.triumphtowns.config.settings.ClaimSettings.MAIN_CLAIM_MATERIAL
import me.mattstudios.triumphtowns.config.settings.ClaimSettings.MINIMUM_BLOCKS
import me.mattstudios.triumphtowns.config.settings.ClaimSettings.MINIMUM_WIDTH
import me.mattstudios.triumphtowns.config.settings.ClaimSettings.SIDES_CLAIM_MATERIAL
import me.mattstudios.triumphtowns.config.settings.ClaimSettings.WAND_MATERIAL
import me.mattstudios.triumphtowns.locale.Message
import me.mattstudios.triumphtowns.town.TownPlayer
import me.mattstudios.triumphtowns.util.Utils
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.util.BlockIterator
import kotlin.math.abs

class ClaimAnimation(private val plugin: TriumphTowns,
                     private val player: Player,
                     private val townPlayer: TownPlayer,
                     val originBlock: Block) : Runnable {

    // Materials from the config
    private val mainMaterial = Material.getMaterial(plugin.claimConfig[MAIN_CLAIM_MATERIAL])
            ?: Material.EMERALD_BLOCK
    private val invalidMaterial = Material.getMaterial(plugin.claimConfig[INVALID_CLAIM_MATERIAL])
            ?: Material.REDSTONE_BLOCK
    private val sidesMaterial = Material.getMaterial(plugin.claimConfig[SIDES_CLAIM_MATERIAL])
            ?: Material.IRON_BLOCK
    private val wand = Material.getMaterial(plugin.claimConfig[WAND_MATERIAL])
            ?: Material.GOLDEN_SHOVEL

    private val minWidth = plugin.claimConfig[MINIMUM_WIDTH]

    private val minBlocks = plugin.claimConfig[MINIMUM_BLOCKS]

    // The task ID
    var taskId = 0

    var isCancelled = false
        private set

    // Selected block
    private var blocksCount = 0

    // Handles the looking block
    private var lookingBlock = originBlock
    private var prevLooking = originBlock

    // Handles the corners
    private var upperCornerBlock = originBlock
    private var prevUpperCorner = originBlock
    private var lowerCornerBlock = originBlock
    private var prevLowerCorner = originBlock

    // Handles the sides of the origin and looking blocks
    private var side1Looking = originBlock
    private var side2Looking = originBlock
    private var side1Origin = originBlock
    private var side2Origin = originBlock
    private var prevSide1Looking = originBlock
    private var prevSide2Looking = originBlock
    private var prevSide1Origin = originBlock
    private var prevSide2Origin = originBlock

    // Whether there is or not a shovel in hand
    private var onHand = false

    override fun run() {

        // Returns if the task is cancelled
        if (isCancelled) return

        // Checks if player is holding the wand or not
        if (player.inventory.itemInMainHand.type != wand) {
            if (!onHand) resetAll()
            onHand = true
            return
        }

        // Changes the main block
        changeMainBlock(originBlock)

        // Sends the action bar text
        sendActionBar()

        // Gets the looking block
        val newLooking: Block? = getLookingBlock(player) ?: return

        // Stops checking if the blocks are the same to reduce calculations
        if (sameLocation(lookingBlock, newLooking!!) && !onHand) return

        onHand = false

        // The looking block
        this.lookingBlock = newLooking

        // Counts the blocks
        countBlocks()

        // Handles the block changes for the looking block
        lookingBlockHandler()

        // Handles the block changes for the side blocks
        originSide1Handler()
        originSide2Handler()
        lookingSide1Handler()
        lookingSide2Handler()

        // Handles the block changes for the corner blocks
        upperCornerBlockHandler()
        lowerCornerBlockHandler()
    }

    /**
     * Checks if the claim is valid
     */
    fun isValidClaim(): Boolean {
        return !(getXSelected() < minWidth || getZSelected() < minWidth || blocksCount < minBlocks || blocksCount > townPlayer.claimBlocks)
    }

    /**
     * Counts how many blocks are being selected
     */
    private fun countBlocks() {
        blocksCount = getXSelected() * getZSelected()
    }

    /**
     * Gets the blocks left (difference from claimblocks available and used)
     */
    private fun getBLocksLeft(): Int {
        return townPlayer.claimBlocks - blocksCount
    }

    /**
     * Gets the X selected
     */
    private fun getXSelected(): Int {
        return abs(lookingBlock.x - originBlock.x) + 1
    }

    /**
     * Gets the Z selected
     */
    private fun getZSelected(): Int {
        return abs(lookingBlock.z - originBlock.z) + 1
    }

    /**
     * Sends the actionbar text for the claiming
     */
    private fun sendActionBar() {
        if (getXSelected() < minWidth || getZSelected() < minWidth || blocksCount < minBlocks)
            ActionBar(plugin.locale.getMessage(Message.CLAIM_ACTIONBAR_SMALL).replace("{x}", getXSelected().toString()).replace("{z}", getZSelected().toString()).replace("{blocksleft}", getBLocksLeft().toString())).send(player)
        else if (blocksCount > townPlayer.claimBlocks)
            ActionBar(plugin.locale.getMessage(Message.CLAIM_ACTIONBAR_BIG).replace("{x}", getXSelected().toString()).replace("{z}", getZSelected().toString()).replace("{blocksleft}", getBLocksLeft().toString())).send(player)
        else
            ActionBar(plugin.locale.getMessage(Message.CLAIM_ACTIONBAR).replace("{x}", getXSelected().toString()).replace("{z}", getZSelected().toString()).replace("{blocksleft}", getBLocksLeft().toString())).send(player)
    }

    /**
     * Changes the block player is looking at
     */
    private fun changeMainBlock(block: Block) {
        if (isValidClaim()) {
            player.sendBlockChange(block.location, mainMaterial.createBlockData())
        } else {
            player.sendBlockChange(block.location, invalidMaterial.createBlockData())
        }
    }

    /**
     * Change the blocks on the sides of the pointer and the main block
     */
    private fun sideChange(block: Block) {
        player.sendBlockChange(block.location, sidesMaterial.createBlockData())
    }

    /**
     * Resets the previous blocks to their original state
     */
    private fun resetBlock(block: Block) {
        player.sendBlockChange(block.location, block.type.createBlockData())
    }

    private fun sameLocation(block: Block, block2: Block): Boolean {
        return block.location == block2.location
    }

    /**
     * Handles the changes on the looking block
     */
    private fun lookingBlockHandler() {
        changeMainBlock(lookingBlock)

        if (sameLocation(lookingBlock, prevLooking)) return

        if (!sameLocation(prevLooking, originBlock)) {
            resetBlock(prevLooking)
        }

        if (sameLocation(lookingBlock, originBlock)) return

        prevLooking = lookingBlock
    }

    /**
     * Handles the changes on the corner 1 block
     */
    private fun upperCornerBlockHandler() {
        upperCornerBlock = Utils.getHighestSolidBlockAt(player.world, lookingBlock.x, originBlock.z)

        if (sameLocation(upperCornerBlock, prevUpperCorner) && !sameLocation(lookingBlock, prevLooking)) return
        if (lookingBlock.z == originBlock.z) return
        if (!sameLocation(prevUpperCorner, originBlock) && !sameLocation(prevUpperCorner, side2Origin)) resetBlock(prevUpperCorner)
        if (sameLocation(upperCornerBlock, originBlock)) return

        changeMainBlock(upperCornerBlock)

        prevUpperCorner = upperCornerBlock
    }

    /**
     * Handles the changes on the corner 2 block
     */
    private fun lowerCornerBlockHandler() {
        lowerCornerBlock = Utils.getHighestSolidBlockAt(player.world, originBlock.x, lookingBlock.z)

        if (sameLocation(lowerCornerBlock, prevLowerCorner) && !sameLocation(lookingBlock, prevLooking)) return
        if (lookingBlock.x == originBlock.x) return
        if (!sameLocation(prevLowerCorner, originBlock) && !sameLocation(prevLowerCorner, side1Origin)) resetBlock(prevLowerCorner)
        if (sameLocation(lowerCornerBlock, originBlock)) return

        changeMainBlock(lowerCornerBlock)

        prevLowerCorner = lowerCornerBlock
    }

    /**
     * Handles the changes for the origin sides
     */
    private fun originSide1Handler() {

        if (lookingBlock.z > originBlock.z)
            side1Origin = Utils.getHighestSolidBlockAt(player.world, originBlock.x, originBlock.z + 1)
        if (lookingBlock.z < originBlock.z)
            side1Origin = Utils.getHighestSolidBlockAt(player.world, originBlock.x, originBlock.z - 1)
        if (lookingBlock.z == originBlock.z)
            side1Origin = Utils.getHighestSolidBlockAt(player.world, originBlock.x, originBlock.z)

        if (sameLocation(side1Origin, prevSide1Origin) && !sameLocation(lookingBlock, prevLooking)) return
        if (!sameLocation(prevSide1Origin, originBlock) && !sameLocation(prevSide1Origin, lookingBlock)) resetBlock(prevSide1Origin)
        if (sameLocation(side1Origin, originBlock)) return
        if (sameLocation(side1Origin, lookingBlock)) return

        sideChange(side1Origin)

        prevSide1Origin = side1Origin
    }

    /**
     * Handles the changes for the origin sides
     */
    private fun originSide2Handler() {

        if (lookingBlock.x > originBlock.x)
            side2Origin = Utils.getHighestSolidBlockAt(player.world, originBlock.x + 1, originBlock.z)
        if (lookingBlock.x < originBlock.x)
            side2Origin = Utils.getHighestSolidBlockAt(player.world, originBlock.x - 1, originBlock.z)
        if (lookingBlock.x == originBlock.x)
            side2Origin = Utils.getHighestSolidBlockAt(player.world, originBlock.x, originBlock.z)

        if (sameLocation(side2Origin, prevSide2Origin) && !sameLocation(lookingBlock, prevLooking)) return
        if (!sameLocation(prevSide2Origin, originBlock) && !sameLocation(prevSide2Origin, lookingBlock)) resetBlock(prevSide2Origin)
        if (sameLocation(side2Origin, originBlock)) return
        if (sameLocation(side2Origin, lookingBlock)) return

        sideChange(side2Origin)

        prevSide2Origin = side2Origin
    }

    /**
     * Handles the changes for the looking sides
     */
    private fun lookingSide1Handler() {
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
            resetBlock(prevSide1Looking)
        }

        if (sameLocation(side1Looking, originBlock)) return
        if (sameLocation(side1Looking, lookingBlock)) return

        sideChange(side1Looking)

        prevSide1Looking = side1Looking
    }

    /**
     * Handles the changes for the looking sides
     */
    private fun lookingSide2Handler() {

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
            resetBlock(prevSide2Looking)
        }

        if (sameLocation(side2Looking, originBlock)) return
        if (sameLocation(side2Looking, lookingBlock)) return

        sideChange(side2Looking)

        prevSide2Looking = side2Looking
    }

    /**
     * Resets all the blocks to their default stage
     */
    private fun resetAll() {
        resetBlock(originBlock)
        resetBlock(lookingBlock)
        resetBlock(upperCornerBlock)
        resetBlock(lowerCornerBlock)
        resetBlock(side1Looking)
        resetBlock(side2Looking)
        resetBlock(side1Origin)
        resetBlock(side2Origin)
    }

    /**
     * Cancels the animation
     */
    fun cancel() {

        Bukkit.getScheduler().cancelTask(taskId)

        isCancelled = true

        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            originBlock.state.update()
            lookingBlock.state.update()
            upperCornerBlock.state.update()
            lowerCornerBlock.state.update()
            side1Looking.state.update()
            side2Looking.state.update()
            side1Origin.state.update()
            side2Origin.state.update()
        }, 10L)
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