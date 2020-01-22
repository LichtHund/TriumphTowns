package me.mattstudios.triumphtowns.manager

import me.mattstudios.triumphtowns.town.TownPlayer
import java.util.UUID

class TownManager {

    // List with the town players
    private val townPlayers: MutableSet<TownPlayer> = HashSet()

    /**
     * Adds a town player to the set
     */
    fun addTownPlayer(townPlayer: TownPlayer) {
        townPlayers.add(townPlayer)
    }

    /**
     * Gets the town player from the set
     */
    fun getTownPlayer(uuid: UUID): TownPlayer {
        return townPlayers.stream().filter { it.player.uniqueId == uuid }.findFirst().orElse(null)
    }

    /**
     * Checks if the player is a town player
     */
    fun isTownPlayer(uuid: UUID): Boolean {
        return !townPlayers.none { it.player.uniqueId == uuid }
    }

}