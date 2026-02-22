package ru.biomenick;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BiomeNicknamePlaceholderExpansion extends PlaceholderExpansion {
    private final BiomeNicknamePlugin plugin;

    public BiomeNicknamePlaceholderExpansion(BiomeNicknamePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "biomenick";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Codex";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getPluginMeta().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return "";
        }

        return switch (params.toLowerCase()) {
            case "prefix" -> plugin.getLegacyPrefix(player);
            case "circle" -> plugin.getLegacyCircle(player);
            case "symbol" -> plugin.getSymbol();
            default -> null;
        };
    }
}
