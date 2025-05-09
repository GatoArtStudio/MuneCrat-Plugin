-- MySQL database export

START TRANSACTION;

CREATE TABLE IF NOT EXISTS `player` (
    `id_player` BIGINT NOT NULL AUTO_INCREMENT,
    `uuid` VARCHAR(36) DEFAULT NULL,
    `minecraft_name` VARCHAR(255) NOT NULL,
    `ip` VARCHAR(40) DEFAULT NULL,
    `login_at` DATETIME DEFAULT NULL,
    `logout_at` DATETIME DEFAULT NULL,
    `is_active` BOOLEAN DEFAULT FALSE,
    `inventory` MEDIUMTEXT DEFAULT NULL,
    `inventory_staff` MEDIUMTEXT DEFAULT NULL,
    `location` MEDIUMTEXT DEFAULT NULL,
    `is_premium` BOOLEAN DEFAULT FALSE,
    `mode_staff` BOOLEAN DEFAULT FALSE,
    `mode_staffchat` BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (`id_player`)
);


CREATE TABLE IF NOT EXISTS `user_discord` (
    `id_discord` BIGINT NOT NULL,
    `discord_name` TEXT NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `minecraft_name` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id_discord`)
);


CREATE TABLE IF NOT EXISTS `vip` (
    `id_vip` BIGINT NOT NULL,
    `name` VARCHAR(255) NOT NULL UNIQUE,
    `kit` TEXT NOT NULL,
    `group_luckperms` TEXT,
    PRIMARY KEY (`id_vip`)
);


CREATE TABLE IF NOT EXISTS `vip_player` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `id_vip` BIGINT NOT NULL,
    `player_uuid` VARCHAR(36) NOT NULL,
    PRIMARY KEY (`id`)
);


CREATE TABLE IF NOT EXISTS `guild_discord` (
    `guild_id` BIGINT NOT NULL,
    `log_channel_id` BIGINT,
    `warning_channel_id` BIGINT,
    `announcement_channel_id` BIGINT,
    `sanction_channel_id` BIGINT,
    `report_channel_id` BIGINT,
    `message_channel_id` BIGINT,
    `command_channel_id` BIGINT,
    `alert_channel_id` BIGINT,
    `player_activity_channel_id` BIGINT,
    `log_user_verified` BIGINT,
    PRIMARY KEY (`guild_id`)
);


CREATE TABLE IF NOT EXISTS `rol_discord` (
    `id_rol` BIGINT NOT NULL,
    `name` TEXT NOT NULL,
    `guild_id` BIGINT NOT NULL UNIQUE,
    `isAdmin` TINYINT(1) NOT NULL,
    `isBuilder` TINYINT(1) NOT NULL,
    `isMod` TINYINT(1) NOT NULL,
    `isDevelopment` TINYINT(1) NOT NULL,
    `isHelper` TINYINT(1) NOT NULL,
    `isVip` TINYINT(1) NOT NULL,
    `isBooter` TINYINT(1) NOT NULL,
    `isVerified` TINYINT(1) NOT NULL,
    PRIMARY KEY (`id_rol`)
);


CREATE TABLE IF NOT EXISTS `user_guild_discord` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `id_discord` BIGINT NOT NULL,
    `guild_id` BIGINT NOT NULL,
    `id_rol` BIGINT NOT NULL,
    PRIMARY KEY (`id`)
);


CREATE TABLE IF NOT EXISTS `grave` (
    `id_grave` INT NOT NULL AUTO_INCREMENT,
    `uuid` VARCHAR(36) NOT NULL,
    `respawn_here` TINYINT(1) NOT NULL,
    `grave_public` TINYINT(1) NOT NULL,
    `grave_duration` INT NOT NULL,
    `grave_location` MEDIUMTEXT NOT NULL,
    `grave_inventory` MEDIUMTEXT NOT NULL,
    PRIMARY KEY (`id_grave`)
);

CREATE TABLE IF NOT EXISTS `subscription` (
    `id_subscription` BIGINT NOT NULL AUTO_INCREMENT,
    `subscription_type` ENUM('monthly', 'semiannual', 'annual', 'permanent') NOT NULL,
    `producer_type` ENUM('rank', 'product') NOT NULL,
    `acquisition_date` DATETIME NOT NULL,
    `expiration_date` DATETIME,
    `acquired_product` TEXT NOT NULL,
    PRIMARY KEY (`id_subscription`)
);


-- Foreign key constraints

ALTER TABLE rol_discord
    ADD FOREIGN KEY (guild_id) REFERENCES guild_discord(guild_id);

ALTER TABLE user_guild_discord
    ADD FOREIGN KEY (id_rol) REFERENCES rol_discord(id_rol),
    ADD FOREIGN KEY (guild_id) REFERENCES guild_discord(guild_id),
    ADD FOREIGN KEY (id_discord) REFERENCES user_discord(id_discord),
    ADD UNIQUE (id_discord, guild_id, id_rol);

ALTER TABLE player
    ADD UNIQUE (minecraft_name, uuid);

ALTER TABLE user_discord
    ADD FOREIGN KEY (minecraft_name) REFERENCES player(minecraft_name);

ALTER TABLE vip_player
    ADD FOREIGN KEY (player_uuid) REFERENCES player(uuid),
    ADD FOREIGN KEY (id_vip) REFERENCES vip(id_vip);

ALTER TABLE grave
    ADD FOREIGN KEY (uuid) REFERENCES player(uuid);

COMMIT;