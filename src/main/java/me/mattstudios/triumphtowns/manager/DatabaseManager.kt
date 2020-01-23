package me.mattstudios.triumphtowns.manager

import me.mattstudios.triumphtowns.TriumphTowns
import me.mattstudios.triumphtowns.database.DBType
import me.mattstudios.triumphtowns.database.Database
import me.mattstudios.triumphtowns.database.SQLite
import me.mattstudios.triumphtowns.town.TownPlayer

class DatabaseManager(private val plugin: TriumphTowns, private val dbType: DBType) {

    private lateinit var database: Database

    /**
     * Initializes the database type
     */
    init {
        when (dbType) {
            DBType.SQLITE -> database = SQLite(plugin)
        }
    }

    /**
     * Inserts the town player in the DB
     */
    fun insertTownPlayer(townPlayer: TownPlayer) {
        database.insertTownPlayer(townPlayer)
    }

}