package com.github.gatoartstudios.munecraft.services.discord.listener;

import com.github.gatoartstudios.munecraft.databases.mysql.DAO.MySQLPlayerDAO;
import com.github.gatoartstudios.munecraft.databases.mysql.DAO.MySQLUserDiscordDAO;
import com.github.gatoartstudios.munecraft.helpers.LoggerCustom;
import com.github.gatoartstudios.munecraft.helpers.PasswordUtil;
import com.github.gatoartstudios.munecraft.helpers.PlayerHelper;
import com.github.gatoartstudios.munecraft.models.PlayerModel;
import com.github.gatoartstudios.munecraft.models.UserDiscordModel;
import com.github.gatoartstudios.munecraft.services.discord.helper.ToolsDiscord;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class RegistersModalHandler extends ListenerAdapter {
    private final MySQLUserDiscordDAO userDiscordDAO = new MySQLUserDiscordDAO();
    private final MySQLPlayerDAO playerDAO = new MySQLPlayerDAO();
    private final long VERIFIED_USER_ROLE_ID = 1363244684436181237L;


    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        if (!event.getModalId().contains("verification-minecraft-")) return;

        String idModal = event.getModalId();
        String userName = Objects.requireNonNull(event.getValue("userminecraft")).getAsString();
        String password = Objects.requireNonNull(event.getValue("passwordminecraft")).getAsString();
        String confirmPassword = Objects.requireNonNull(event.getValue("confirmpasswordminecraft")).getAsString();

        // We check if the passwords match
        if (!password.equals(confirmPassword)) {
            event.reply("Las contraseñas no coinciden!, intentalo de nuevo.").setEphemeral(true).queue();
            return;
        }

        UserDiscordModel userDiscordModel = userDiscordDAO.read(event.getUser().getIdLong());

        if (userDiscordModel != null) {
            event.reply("Ya estas verificado!").setEphemeral(true).queue();
            return;
        }

        UserDiscordModel userAlreadyRegistered = userDiscordDAO.readByMinecraftName(idModal.equals("verification-minecraft-bedrock") ? "." + userName : userName);

        if (userAlreadyRegistered != null) {
            event.reply("El usuario " + userName + " ya esta registrado!").setEphemeral(true).queue();
            return;
        }

        password = PasswordUtil.hash(password);

        if (idModal.equals("verification-minecraft-bedrock")) {
            if (registerUserMinecraft(
                    "." + userName,
                    password,
                    event.getUser().getIdLong(),
                    event.getUser().getName()
            )) {
                try {
                    ToolsDiscord.addRoleUser(event.getUser().getIdLong(), VERIFIED_USER_ROLE_ID, Objects.requireNonNull(event.getGuild()).getIdLong(), "." + userName);
                } catch (Exception e) {
                    LoggerCustom.error(String.valueOf(e));
                }

                editNameUser(event);

                event.reply("Verificación exitosa en la plataforma Bedrock!").setEphemeral(true).queue();
            } else {
                event.reply("Verificación fallida!").setEphemeral(true).queue();
            }
        } else if (idModal.equals("verification-minecraft-java")) {
            if (registerUserMinecraft(
                    userName,
                    password,
                    event.getUser().getIdLong(),
                    event.getUser().getName()
            )) {
                try {
                    ToolsDiscord.addRoleUser(event.getUser().getIdLong(), VERIFIED_USER_ROLE_ID, Objects.requireNonNull(event.getGuild()).getIdLong(), userName);
                } catch (Exception e) {
                    LoggerCustom.error(String.valueOf(e));
                }

                editNameUser(event);

                event.reply("Verificación exitosa en la plataforma Java!").setEphemeral(true).queue();
            } else {
                event.reply("Verificación fallida!").setEphemeral(true).queue();
            }
        }
    }

    private void editNameUser(ModalInteractionEvent event) {
        Member member = event.getMember();

        if (member != null) {
            String memberName = member.getNickname();
            if (memberName != null) {
                Objects.requireNonNull(event.getGuild()).modifyNickname(member, memberName + "*").queue();
            }
        }
    }

    // This method registers users in the database
    private boolean registerUserMinecraft(String userName, String password, long idDiscord, String discordName) {
        try {

            PlayerModel playerData = playerDAO.readByMinecraftName(userName);
            Optional<UUID> uuidGenerated = PlayerHelper.getOfflinePlayerUUID(userName);


            LoggerCustom.debug("UUID Offline Player: " + uuidGenerated + " Name: " + userName);

            if (playerData == null) {
                UUID uuid = uuidGenerated.orElse(null);
                if (uuid != null) {
                    playerDAO.create(
                            new PlayerModel(
                                    uuidGenerated.get(),
                                    userName
                            )
                    );
                } else {
                    playerDAO.create(
                            new PlayerModel(
                                    userName
                            )
                    );
                }
            }

            userDiscordDAO.create(
                    new UserDiscordModel(
                            idDiscord,
                            discordName,
                            password,
                            userName
                    )
            );
            return true;
        } catch (Exception e) {
            LoggerCustom.error("Error while creating user: " + e.getMessage());
        }
        return false;
    }
}
