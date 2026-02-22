package ru.biomenick;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

final class ChatListener implements Listener {
    private final BiomeNicknamePlugin plugin;

    ChatListener(BiomeNicknamePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onAsyncChat(AsyncChatEvent event) {
        ChatRenderer existingRenderer = event.renderer();

        event.renderer((source, sourceDisplayName, message, viewer) -> Component.empty()
                .append(plugin.getPrefix(source))
                .append(existingRenderer.render(source, sourceDisplayName, message, viewer)));
    }
}
