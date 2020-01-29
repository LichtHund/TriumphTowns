package me.mattstudios.triumphtowns.manager

import me.mattstudios.triumphtowns.TriumphTowns
import me.mattstudios.triumphtowns.town.Claim
import me.mattstudios.triumphtowns.town.Town
import me.mattstudios.triumphtowns.town.TownPlayer
import org.bukkit.entity.Player

class TownManager(private val plugin: TriumphTowns) {

    // List with the town players
    private val townPlayers: MutableSet<TownPlayer> = HashSet()

    // List with all the towns
    private val towns: MutableSet<Town> = HashSet()

    // List with all the claims
    private val claims: MutableSet<Claim> = HashSet()

    /**
     * Adds a town to the set
     */
    fun addTown(town: Town) {
        towns.add(town)
    }

    /**
     * Adds a town player to the set
     */
    fun addTownPlayer(townPlayer: TownPlayer) {
        townPlayers.add(townPlayer)
    }

    /**
     * Adds a town player to the set
     */
    fun addNewTownPlayer(townPlayer: TownPlayer) {
        plugin.databaseManager.insertTownPlayer(townPlayer)
        townPlayers.add(townPlayer)
    }

    /**
     * Gets the town player from the set
     */
    fun getTownPlayer(player: Player): TownPlayer {
        return townPlayers.stream().filter { it.player.uniqueId == player.uniqueId }.findFirst().orElse(null)
    }

    /**
     * Checks if the player is a town player
     */
    fun isTownPlayer(player: Player): Boolean {
        return !townPlayers.none { it.player.uniqueId == player.uniqueId }
    }

}