package me.mattstudios.triumphtowns.manager

import me.mattstudios.triumphtowns.TriumphTowns
import me.mattstudios.triumphtowns.town.Town
import me.mattstudios.triumphtowns.town.TownPlayer
import java.util.UUID

class TownManager(private val plugin: TriumphTowns) {

    // List with the town players
    private val townPlayers: MutableSet<TownPlayer> = HashSet()

    private val towns: MutableSet<Town> = HashSet()

    fun test() {
        println(towns)
    }

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