package com.github.gatoartstudios.munecraft.listener;

import com.github.gatoartstudios.munecraft.helpers.LoggerCustom;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class OnReadyDiscord extends ListenerAdapter {
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        LoggerCustom.info("Discord bot is ready " + event.getJDA().getSelfUser().getName());
    }
}
