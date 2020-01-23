package me.mattstudios.triumphtowns.database

object Queries {

    const val SQLITE_CREATE_TOWNS = "CREATE TABLE IF NOT EXISTS `towns`(\n" +
            "  `uuid` VARCHAR(36) PRIMARY KEY,\n" +
            "  `name` VARCHAR(255)\n" +
            ")"

    const val SQLITE_CREATE_TOWN_PLAYERS = "CREATE TABLE IF NOT EXISTS `town_players`(\n" +
            "  `uuid` VARCHAR(36) PRIMARY KEY,\n" +
            "  `town_uuid` VARCHAR(36),\n" +
            "  `claim_blocks` INTEGER,\n" +
            "  FOREIGN KEY (`town_uuid`)\n" +
            "       REFERENCES `towns` (`town_uuid`) " +
            ")"

    const val SQLITE_CREATE_CLAIMS = "CREATE TABLE IF NOT EXISTS `claims`(\n" +
            "  `claim_id` INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "  `uuid` VARCHAR(36),\n" +
            "  `first_corner` varchar(512),\n" +
            "  `second_corner` varchar(512),\n" +
            "  FOREIGN KEY (`uuid`)\n" +
            "       REFERENCES `town_players` (`uuid`),\n " +
            "  FOREIGN KEY (`uuid`)\n" +
            "       REFERENCES `towns` (`uuid`) " +
            ")"

    const val INSERT_PLAYER = "INSERT INTO `town_players` VALUES(?, ?, ?)"
}