package dev.louis.chaincombattweaks;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

import java.util.List;

public class ChainCombatTweaks implements ModInitializer {
    public static final List<Item> COMBAT_DISABLED_ITEMS = List.of(Items.TRIDENT, Items.ENDER_PEARL);
    public static final int COMBAT_TICKS = 30*20;
    @Override
    public void onInitialize() {

    }


    public static ServerBossBar createCombatBossBar(ServerPlayerEntity player, int combatTicks) {
        var bossBar = new ServerBossBar(Text.of(""), BossBar.Color.RED, BossBar.Style.PROGRESS);
        bossBar.addPlayer(player);
        return bossBar;
    }
}
