package ru.biomenick;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

final class ChatListener implements Listener {
    private final BiomeNicknamePlugin plugin;

    ChatListener(BiomeNicknamePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAsyncChat(AsyncChatEvent event) {
        event.renderer((source, sourceDisplayName, message, viewer) -> {
            Component plainNameChatPart = Component.translatable(
                    "chat.type.text",
                    Component.text(source.getName()),
                    message
            );

            return Component.empty()
                    .append(plugin.getPrefix(source))
                    .append(plainNameChatPart);
        });
    }
}
