package ru.biomenick;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class BiomeNicknamePlugin extends JavaPlugin {
    private static final String DEFAULT_SYMBOL = "●";

    private final Map<UUID, Component> cachedPrefixes = new HashMap<>();
    private String symbol;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        symbol = getConfig().getString("symbol", DEFAULT_SYMBOL);

        Bukkit.getPluginManager().registerEvents(new ChatListener(this), this);

        Bukkit.getScheduler().runTaskTimer(this, this::updateAllPlayersPrefix, 1L, 20L);

        getLogger().info("BiomeNicknamePlugin enabled on Paper " + Bukkit.getMinecraftVersion());
    }

    @Override
    public void onDisable() {
        cachedPrefixes.clear();
    }

    public Component getPrefix(Player player) {
        return cachedPrefixes.computeIfAbsent(player.getUniqueId(), ignored -> buildPrefix(player));
    }

    private void updateAllPlayersPrefix() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Component newPrefix = buildPrefix(player);
            Component oldPrefix = cachedPrefixes.get(player.getUniqueId());

            if (newPrefix.equals(oldPrefix)) {
                continue;
            }

            cachedPrefixes.put(player.getUniqueId(), newPrefix);
            player.playerListName(Component.empty().append(newPrefix).append(Component.text(player.getName())));
            player.displayName(Component.empty().append(newPrefix).append(Component.text(player.getName())));
        }
    }

    private Component buildPrefix(Player player) {
        Location location = player.getLocation();
        NamespacedKey biomeKey = location.getBlock().getBiome().getKey();
        NamedTextColor color = resolveColorByBiomeKey(biomeKey.getKey());

        return Component.text(symbol + " ", color);
    }

    private NamedTextColor resolveColorByBiomeKey(String biome) {
        String key = biome.toLowerCase();

        if (key.contains("end")) {
            return NamedTextColor.LIGHT_PURPLE;
        }

        if (key.contains("nether") || key.contains("crimson") || key.contains("warped") || key.contains("soul") || key.contains("basalt")) {
            return NamedTextColor.RED;
        }

        if (key.contains("ocean") || key.contains("river") || key.contains("beach")) {
            return NamedTextColor.AQUA;
        }

        if (key.contains("snow") || key.contains("ice") || key.contains("frozen") || key.contains("jagged") || key.contains("grove")) {
            return NamedTextColor.WHITE;
        }

        if (key.contains("desert") || key.contains("badlands") || key.contains("savanna")) {
            return NamedTextColor.GOLD;
        }

        if (key.contains("swamp") || key.contains("mangrove")) {
            return NamedTextColor.DARK_GREEN;
        }

        if (key.contains("jungle") || key.contains("forest") || key.contains("taiga") || key.contains("plains") || key.contains("meadow") || key.contains("cherry")) {
            return NamedTextColor.GREEN;
        }

        if (key.contains("mountain") || key.contains("peak") || key.contains("hills") || key.contains("stony") || key.contains("windswept")) {
            return NamedTextColor.GRAY;
        }

        if (key.contains("mushroom")) {
            return NamedTextColor.LIGHT_PURPLE;
        }

        return NamedTextColor.YELLOW;
    }
}
