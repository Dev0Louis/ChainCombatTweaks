package dev.louis.chaincombattweaks.api;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.ServerBossBar;

public interface TweakedPlayer {
    default boolean chainCombatSystem$shouldStealShield() {
        throw new UnsupportedOperationException("BEEP BOPP MIXIN NONO CALL");
    }


    default void chainCombatSystem$stealShield(LivingEntity livingEntity) {
        throw new UnsupportedOperationException("BEEP BOPP MIXIN NONO CALL");
    }

    default boolean chainCombatSystem$isInCombat() {
        throw new UnsupportedOperationException("BEEP BOPP MIXIN NONO CALL");
    }

    default int chainCombatSystem$getCombatTicks() {
        throw new UnsupportedOperationException("BEEP BOPP MIXIN NONO CALL");
    }

    default void chainCombatSystem$setCombatTicks(int combatTicks) {
        throw new UnsupportedOperationException("BEEP BOPP MIXIN NONO CALL");
    }
    default ServerBossBar chainCombatSystem$getCombatBossBar() {
        throw new UnsupportedOperationException("BEEP BOPP MIXIN NONO CALL");
    }
    default void chainCombatSystem$setCombatLogImmunity(boolean immunity) {
        throw new UnsupportedOperationException("BEEP BOPP MIXIN NONO CALL");
    }
    default boolean chainCombatSystem$isImmuneToCombatLog() {
        throw new UnsupportedOperationException("BEEP BOPP MIXIN NONO CALL");
    }
}
