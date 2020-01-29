package me.mattstudios.triumphtowns.town

import org.bukkit.World
import java.util.UUID
import javax.xml.stream.Location

class Claim {

    var town = false

    lateinit var owner: UUID

    lateinit var world: World

    lateinit var lowerCorner: Location
    lateinit var upperCorner: Location

}