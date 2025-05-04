package com.github.gatoartstudios.munecraft.services.discord.helper;

import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

public class ToolsModal {

    public static Modal create() {


        return null;
    }

    public static Modal getModalVerificationMinecraft(String prefix) {

        TextInput userMinecraft = TextInput.create("userminecraft", "Usuario", TextInputStyle.SHORT)
                .setPlaceholder("Escribe tu username de Minecraft (Mayúsculas y minúsculas)")
                .setRequired(true)
                .build();

        TextInput passwordMinecraft = TextInput.create("passwordminecraft", "Contraseña", TextInputStyle.SHORT)
                .setPlaceholder("Escribe la contraseña que usarás en servidor")
                .setRequired(true)
                .build();

        TextInput confirmPasswordMinecraft = TextInput.create("confirmpasswordminecraft", "Confirmar Contraseña", TextInputStyle.SHORT)
                .setPlaceholder("Confirma la contraseña que usarás en servidor")
                .setRequired(true)
                .build();

        return Modal.create("verification-minecraft-" + prefix, "Verificación Minecraft")
                .addComponents(
                        ActionRow.of(userMinecraft),
                        ActionRow.of(passwordMinecraft),
                        ActionRow.of(confirmPasswordMinecraft)
                )
                .build();
    }
}
