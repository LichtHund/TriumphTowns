package me.mattstudios.triumphtowns.database

import me.mattstudios.triumphtowns.town.TownPlayer

interface Database {

    fun insertTownPlayer(townPlayer: TownPlayer)

}