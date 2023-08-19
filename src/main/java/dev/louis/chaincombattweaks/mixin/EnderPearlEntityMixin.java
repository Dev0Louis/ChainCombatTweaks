package dev.louis.chaincombattweaks.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Debug(export = true)
@Mixin(EnderPearlEntity.class)
public abstract class EnderPearlEntityMixin
extends ThrownItemEntity {
    public EnderPearlEntityMixin(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "onCollision", at = @At(value = "HEAD"), cancellable = true)
    public void cancelEnderPearl(HitResult hitResult, CallbackInfo ci) {
        if(this.getOwner() instanceof ServerPlayerEntity player && player.chainCombatSystem$isInCombat()) {
            ci.cancel();
            this.discard();
        }
    }

}
