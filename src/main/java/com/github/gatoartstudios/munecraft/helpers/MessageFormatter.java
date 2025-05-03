package com.github.gatoartstudios.munecraft.helpers;

import com.github.gatoartstudios.munecraft.core.enums.PlaceHoldersType;
import net.kyori.adventure.text.TextComponent;
import java.util.Map;

public class MessageFormatter {
    /**
     * Formats a message by replacing color codes with the corresponding Minecraft color codes.
     *
     * @param message the message to format
     * @return the formatted message
     */
    public static String format(String message) {
        return message.replace("&", "§");
    }

    /**
     * Formats a message by replacing color codes with the corresponding Minecraft color codes.
     *
     * @param message the message to format
     * @return the formatted message
     */
    public static String format(TextComponent message) {
        return format(message.content());
    }

    /**
     * Formats a message by replacing placeholders with their corresponding values dynamically.
     *
     * @param message the message to format
     * @param placeholders a map of placeholders and their corresponding values
     * @return the formatted message
     */
    public static String formatWithPlaceholders(String message, Map<PlaceHoldersType, String> placeholders) {
        for (Map.Entry<PlaceHoldersType, String> entry : placeholders.entrySet()) {
            message = message.replace(entry.getValue(), entry.getValue());
        }
        return message;
    }
}
