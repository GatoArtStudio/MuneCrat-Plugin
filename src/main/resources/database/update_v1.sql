ALTER TABLE player
    ADD COLUMN IF NOT EXISTS StaffChatMode TINYINT(1) NOT NULL DEFAULT 0;

ALTER TABLE player
    ADD COLUMN IF NOT EXISTS ultimate_location MEDIUMTEXT;

ALTER TABLE guild_discord
    ADD COLUMN IF NOT EXISTS log_user_verified BIGINT;