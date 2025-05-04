package com.github.gatoartstudios.munecraft.services.discord.modal;

// ...existing imports...

import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VerificationMinecraftModal implements Modal {
    private final String prefix;

    public VerificationMinecraftModal(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public @NotNull String getId() {
        return "verification-minecraft-" + prefix;
    }

    @Override
    public @NotNull String getTitle() {
        return "Verificación Minecraft";
    }

    @Override
    public @NotNull List<LayoutComponent> getComponents() {
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

        return List.of(
                ActionRow.of(userMinecraft),
                ActionRow.of(passwordMinecraft),
                ActionRow.of(confirmPasswordMinecraft)
        );
    }

    /**
     * Builds the modal using the provided components.
     *
     * @return The constructed Modal object.
     */
    public Modal build() {
        return Modal.create(getId(), getTitle())
                .addComponents(getComponents())
                .build();
    }

    @Override
    public @NotNull DataObject toData() {
        return null;
    }
}
