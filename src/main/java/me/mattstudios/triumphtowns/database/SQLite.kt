package me.mattstudios.triumphtowns.database

import me.mattstudios.mattcore.utils.MessageUtils.info
import me.mattstudios.triumphtowns.TriumphTowns
import me.mattstudios.triumphtowns.database.Queries.INSERT_PLAYER
import me.mattstudios.triumphtowns.database.Queries.SQLITE_CREATE_CLAIMS
import me.mattstudios.triumphtowns.database.Queries.SQLITE_CREATE_TOWNS
import me.mattstudios.triumphtowns.database.Queries.SQLITE_CREATE_TOWN_PLAYERS
import me.mattstudios.triumphtowns.town.Town
import me.mattstudios.triumphtowns.town.TownPlayer
import org.sqlite.SQLiteDataSource
import java.io.File
import java.io.IOException
import java.sql.Connection
import java.sql.SQLException
import java.util.UUID

class SQLite(private val plugin: TriumphTowns) : Database {

    private lateinit var dataSource: SQLiteDataSource

    /**
     * Initializes everything and caches it
     */
    init {
        createDB()
        createTables()
        cacheData()
    }

    /**
     * Connects to the database
     */
    private fun connect() {
        dataSource = SQLiteDataSource()
        dataSource.url = "jdbc:sqlite:" + plugin.dataFolder.toString() + "/townsdata.db"
    }

    /**
     * Creates the database if not exist
     */
    private fun createDB() {
        try {
            val dbFile = File(plugin.dataFolder, "townsdata.db")

            if (!dbFile.exists()) {
                if (!dbFile.createNewFile()) info("&cCouldn't create database file.")
                else info("&aDatabase created successfully!")
            }

            connect()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * Creates all the default tables
     */
    private fun createTables() {
        var connection: Connection? = null
        try {
            connection = dataSource.connection
            connection.prepareStatement(SQLITE_CREATE_TOWNS).execute()
            connection.prepareStatement(SQLITE_CREATE_TOWN_PLAYERS).execute()
            connection.prepareStatement(SQLITE_CREATE_CLAIMS).execute()
        } catch (e: SQLException) {
            info("&cAn error occurred creating database tables!")
            e.printStackTrace()
        } finally {
            if (connection != null) {
                try {
                    connection.close()
                } catch (e: SQLException) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * Caches all the data
     */
    private fun cacheData() {

        cacheTown()
        cacheTownPlayer()
        cacheClaims()

    }

    /**
     * Caches the town data
     */
    private fun cacheTown() {
        var connection: Connection? = null
        try {
            connection = dataSource.connection
            val resultSet = connection.createStatement().executeQuery("SELECT * FROM `towns`")

            while (resultSet.next()) {
                val uuid = UUID.fromString(resultSet.getString("uuid"))
                val name = resultSet.getString("name")

                plugin.townManager.addTown(Town(uuid, name))
            }

            resultSet.close()
        } catch (e: SQLException) {
            info("&cAn error occurred caching the towns data!")
            e.printStackTrace()
        } finally {
            if (connection != null) {
                try {
                    connection.close()
                } catch (e: SQLException) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * Caches the player data
     */
    private fun cacheTownPlayer() {
        var connection: Connection? = null
        try {
            connection = dataSource.connection
            val resultSet = connection.createStatement().executeQuery("SELECT * FROM `town_players`")

            while (resultSet.next()) {
                val uuid = UUID.fromString(resultSet.getString("uuid"))
                val claimBlocks: Int = resultSet.getInt("claim_blocks")

                val townPlayer = TownPlayer(uuid, claimBlocks)

                if (resultSet.getString("town_uuid") != null)
                    townPlayer.townUUID = UUID.fromString(resultSet.getString("town_uuid"))

                plugin.townManager.addTownPlayer(townPlayer)
            }

            resultSet.close()
        } catch (e: SQLException) {
            info("&cAn error occurred caching the towns data!")
            e.printStackTrace()
        } finally {
            if (connection != null) {
                try {
                    connection.close()
                } catch (e: SQLException) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * Caches the claims
     */
    private fun cacheClaims() {

    }

    /**
     * Inserts the town player into the database
     */
    override fun insertTownPlayer(townPlayer: TownPlayer) {
        Thread {
            var connection: Connection? = null
            try {
                connection = dataSource.connection
                val preparedStatement = connection.prepareStatement(INSERT_PLAYER)
                preparedStatement.setString(1, townPlayer.uuid.toString())

                if (townPlayer.townUUID != null)
                    preparedStatement.setString(2, townPlayer.townUUID.toString())
                else
                    preparedStatement.setString(2, null)

                preparedStatement.setInt(3, townPlayer.claimBlocks)

                preparedStatement.executeUpdate()
            } catch (e: SQLException) {
                info("&cAn error occurred caching the towns data!")
                e.printStackTrace()
            } finally {
                if (connection != null) {
                    try {
                        connection.close()
                    } catch (e: SQLException) {
                        e.printStackTrace()
                    }
                }
            }
        }.start()
    }

}