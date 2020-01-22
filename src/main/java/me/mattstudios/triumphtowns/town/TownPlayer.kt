package me.mattstudios.triumphtowns.town

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import java.util.UUID

class TownPlayer (uuid: UUID) {

    val player: OfflinePlayer = Bukkit.getOfflinePlayer(uuid)

}