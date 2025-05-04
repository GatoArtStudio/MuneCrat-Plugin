package com.github.gatoartstudios.munecraft.services.discord.listener;

import com.github.gatoartstudios.munecraft.services.discord.embed.VerificationMinecraftEmbed;
import com.github.gatoartstudios.munecraft.services.discord.modal.VerificationMinecraftModal;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SelectMenuVerificationMinecraftHandler extends ListenerAdapter  {

    @Override
    public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {
        if (!event.getComponentId().equals("select-menu-verification-minecraft")) return;

        List<String> selectedValues = event.getValues();

        VerificationMinecraftEmbed verificationMinecraftEmbed = new VerificationMinecraftEmbed();

        MessageEditBuilder messageEditBuilder = new MessageEditBuilder()
                .setEmbeds(verificationMinecraftEmbed.getEmbedBuilder().build())
                .setComponents(ActionRow.of(verificationMinecraftEmbed.getSelectMenu()));

        event.getMessage().editMessage(messageEditBuilder.build()).queue();

        String selected = selectedValues.getFirst();

        switch (selected) {
            case "select-menu-verification-minecraft-bedrock":
                Modal verificationMinecraftModal = new VerificationMinecraftModal("bedrock").build();
                event.replyModal(verificationMinecraftModal).queue();
                break;
            case "select-menu-verification-minecraft-java":
                Modal verificationMinecraftModalJava = new VerificationMinecraftModal("java").build();
                event.replyModal(verificationMinecraftModalJava).queue();
                break;
        }
    }
}
