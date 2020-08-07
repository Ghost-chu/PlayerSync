package com.mcsunnyside.playersync.module;

import com.mcsunnyside.playersync.sync.Sync;
import com.mcsunnyside.playersync.sync.SyncDataContainer;
import com.mcsunnyside.playersync.sync.SyncModule;
import com.mcsunnyside.playersync.sync.SyncType;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collection;
import java.util.logging.Logger;

public class PlayerSyncModule implements SyncModule {
    private final Logger logger = Logger.getLogger("PlayerSync");
    @Sync(field = "hp", type = SyncType.LOAD)
    public void loadPlayerHealth(@NotNull SyncDataContainer data) {
        double hp = Double.parseDouble(data.getData());
        data.getPlayer().setHealth(hp);
    }

    @NotNull
    @Sync(field = "hp", type = SyncType.SAVE)
    public SyncDataContainer savePlayerHealth(@NotNull Player player) {
        return SyncDataContainer.builder()
                .player(player)
                .data(String.valueOf(player.getHealth()))
                .build();
    }

    @Sync(field = "exp", type = SyncType.LOAD)
    public void loadPlayerExp(@NotNull SyncDataContainer data) {
        float exp = Float.parseFloat(data.getData());
        data.getPlayer().setExp(exp);
    }

    @NotNull
    @Sync(field = "exp", type = SyncType.SAVE)
    public SyncDataContainer savePlayerExp(@NotNull Player player) {
        return SyncDataContainer.builder()
                .player(player)
                .data(String.valueOf(player.getExp()))
                .build();
    }

    @Sync(field = "air", type = SyncType.LOAD)
    public void loadPlayerAir(@NotNull SyncDataContainer data) {
        int air = Integer.parseInt(data.getData());
        data.getPlayer().setRemainingAir(air);
    }

    @NotNull
    @Sync(field = "air", type = SyncType.SAVE)
    public SyncDataContainer savePlayerAir(@NotNull Player player) {
        return SyncDataContainer.builder()
                .player(player)
                .data(String.valueOf(player.getRemainingAir()))
                .build();
    }

    @Sync(field = "maxair", type = SyncType.LOAD)
    public void loadPlayerMaxAir(@NotNull SyncDataContainer data) {
        int air = Integer.parseInt(data.getData());
        data.getPlayer().setMaximumAir(air);
    }

    @NotNull
    @Sync(field = "maxair", type = SyncType.SAVE)
    public SyncDataContainer savePlayerMaxAir(@NotNull Player player) {
        return SyncDataContainer.builder()
                .player(player)
                .data(String.valueOf(player.getMaximumAir()))
                .build();
    }

    @Sync(field = "effects", type = SyncType.LOAD)
    public void loadPlayerPotionEffects(@NotNull SyncDataContainer data) {
        YamlConfiguration configuration = new YamlConfiguration();
        try {
            configuration.load(data.getData());
        } catch (IOException | InvalidConfigurationException ioException) {
            ioException.printStackTrace();
            logger.warning("Failed to sync player " + data.getPlayer().getName() + "'s potion effects!");
            return;
        }
        //noinspection unchecked
        Collection<PotionEffect> effects = (Collection<PotionEffect>) configuration.getList("effects");
        if (effects == null) {
            return;
        }
        for (PotionEffectType value : PotionEffectType.values()) {
            data.getPlayer().removePotionEffect(value);
        }
        data.getPlayer().addPotionEffects(effects);
    }

    @NotNull
    @Sync(field = "effects", type = SyncType.SAVE)
    public SyncDataContainer savePlayerPotionEffects(@NotNull Player player) {
        YamlConfiguration configuration = new YamlConfiguration();
        configuration.set("effects", player.getActivePotionEffects());
        return SyncDataContainer.builder().player(player).data(configuration.saveToString()).build();
    }

}
