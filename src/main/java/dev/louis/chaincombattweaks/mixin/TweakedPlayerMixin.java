package dev.louis.chaincombattweaks.mixin;

import com.mojang.authlib.GameProfile;
import dev.louis.chaincombattweaks.ChainCombatTweaks;
import dev.louis.chaincombattweaks.api.TweakedPlayer;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class TweakedPlayerMixin extends PlayerEntity implements TweakedPlayer {
    @Unique
    private int combatTicks;
    @Unique
    private ServerBossBar combatBossBar;
    @Unique
    private boolean combatLogImmunity;

    public TweakedPlayerMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "onDisconnect", at = @At("HEAD"))
    public void killPlayer(CallbackInfo ci) {
        //Don't kill the player if the Server is stopping
        if(this.chainCombatSystem$isImmuneToCombatLog())return;
        if(this.chainCombatSystem$isInCombat())this.kill();
    }

    @Inject(method = "tick", at = @At("RETURN"))
    public void decreaseCombatTicks(CallbackInfo ci) {
        if(combatTicks > 0)  {
            if(combatBossBar == null)this.combatBossBar = ChainCombatTweaks.createCombatBossBar(((ServerPlayerEntity) (Object) this), combatTicks);
            combatBossBar.setPercent((float) combatTicks / ChainCombatTweaks.COMBAT_TICKS);
            combatBossBar.setName(Text.literal(String.valueOf(combatTicks / 20)).setStyle(Style.EMPTY.withColor(Formatting.RED)));
            --this.combatTicks;
        } else if(combatBossBar != null) {
            this.combatBossBar.removePlayer((ServerPlayerEntity) (Object)this);
            this.combatBossBar = null;
        }
    }

    @Inject(method = "onDeath", at = @At("HEAD"))
    public void clearBossBarOnDeath(DamageSource damageSource, CallbackInfo ci) {
        if(this.combatBossBar == null)return;
        this.combatBossBar.removePlayer((ServerPlayerEntity) (Object) this);
    }

    @Inject(method = "copyFrom", at = @At("RETURN"))
    public void copyCombatTicksAndBoosBar(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        if(alive) {
            this.chainCombatSystem$setCombatTicks(oldPlayer.chainCombatSystem$getCombatTicks());
            this.combatBossBar = oldPlayer.chainCombatSystem$getCombatBossBar();
        }
    }

    @Inject(method = "damage", at = @At("RETURN"))
    public void setPlayerInCombatOnDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        var attacker = source.getAttacker();
        if(source.getAttacker() instanceof ServerPlayerEntity playerTarget &&
                        attacker instanceof ServerPlayerEntity serverPlayerAttacker) {
            playerTarget.chainCombatSystem$setCombatTicks(ChainCombatTweaks.COMBAT_TICKS);
            serverPlayerAttacker.chainCombatSystem$setCombatTicks(ChainCombatTweaks.COMBAT_TICKS);

            BlockPos.stream(this.getBoundingBox().stretch(1,1,1)).forEach(blockPos -> {
                if(attacker.getWorld().getBlockState(blockPos).getBlock() == Blocks.COBWEB) {
                    attacker.getWorld().breakBlock(blockPos, false, attacker);
                }
            });
        }
    }

    public int chainCombatSystem$getCombatTicks() {
        return combatTicks;
    }

    public boolean chainCombatSystem$isInCombat() {
        return this.chainCombatSystem$getCombatTicks() > 0;
     }

     public void chainCombatSystem$setCombatTicks(int combatTicks) {
        ChainCombatTweaks.COMBAT_DISABLED_ITEMS.forEach(combatDisabledItem -> this.getItemCooldownManager().set(combatDisabledItem, combatTicks));
        this.combatTicks = combatTicks;
    }


    public ServerBossBar chainCombatSystem$getCombatBossBar() {
        return this.combatBossBar;
    }


    @Override
    public void takeShieldHit(LivingEntity attacker) {
        if (attacker instanceof ServerPlayerEntity attackerPlayer && attackerPlayer.chainCombatSystem$shouldStealShield()){
            attacker.getMainHandStack().damage(78, attacker, p -> p.sendToolBreakStatus(attacker.getActiveHand()));
            this.chainCombatSystem$stealShield(attacker);

        }
    }

    public void chainCombatSystem$setCombatLogImmunity(boolean combatLogImmunity) {
        this.combatLogImmunity = combatLogImmunity;
    }
    public boolean chainCombatSystem$isImmuneToCombatLog() {
        return combatLogImmunity;
    }


    @Override
    public boolean chainCombatSystem$shouldStealShield() {
        return this.getMainHandStack().getItem() instanceof HoeItem;
    }

    @Override
    public void chainCombatSystem$stealShield(LivingEntity livingEntity) {
        if(livingEntity.getActiveItem().isOf(Items.SHIELD)) {
            livingEntity.getActiveItem().setCount(0);
            this.clearActiveItem();
            if(livingEntity instanceof PlayerEntity player) {
                player.getItemCooldownManager().set(Items.SHIELD, 15);
            }
            this.getWorld().sendEntityStatus(this, EntityStatuses.BREAK_SHIELD);
        }
    }
}
