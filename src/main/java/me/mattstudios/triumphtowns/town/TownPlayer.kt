package me.mattstudios.triumphtowns.town

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import java.util.UUID

class TownPlayer(val uuid: UUID, var townUUID: UUID?, var claimBlocks: Int) {

    val player: OfflinePlayer = Bukkit.getOfflinePlayer(uuid)

    constructor(uuid: UUID) : this(uuid, null, 100)

    constructor(uuid: UUID, claimBlocks: Int) : this(uuid, null, claimBlocks)
}