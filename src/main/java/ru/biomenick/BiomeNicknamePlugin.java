package ru.biomenick;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class BiomeNicknamePlugin extends JavaPlugin implements Listener {
    private static final String DEFAULT_SYMBOL = "●";

    private final Map<UUID, Component> cachedPrefixes = new ConcurrentHashMap<>();
    private Component defaultPrefix;
    private String symbol;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        symbol = getConfig().getString("symbol", DEFAULT_SYMBOL);
        defaultPrefix = Component.text(symbol + " ", NamedTextColor.YELLOW);

        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(this), this);

        updateAllPlayersPrefix();
        Bukkit.getScheduler().runTaskTimer(this, this::updateAllPlayersPrefix, 20L, 20L);

        getLogger().info("BiomeNicknamePlugin enabled on Paper " + Bukkit.getMinecraftVersion());
    }

    @Override
    public void onDisable() {
        cachedPrefixes.clear();
    }

    public Component getPrefix(Player player) {
        return cachedPrefixes.getOrDefault(player.getUniqueId(), defaultPrefix);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        updatePlayerPrefix(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        cachedPrefixes.remove(event.getPlayer().getUniqueId());
    }

    private void updateAllPlayersPrefix() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updatePlayerPrefix(player);
        }
    }

    private void updatePlayerPrefix(Player player) {
        Component newPrefix = buildPrefix(player);
        Component oldPrefix = cachedPrefixes.get(player.getUniqueId());

        if (newPrefix.equals(oldPrefix)) {
            return;
        }

        cachedPrefixes.put(player.getUniqueId(), newPrefix);
        Component decoratedName = Component.empty().append(newPrefix).append(Component.text(player.getName()));
        player.playerListName(decoratedName);
        player.displayName(decoratedName);
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
