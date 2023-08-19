package dev.louis.chaincombattweaks.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SnowballEntity.class)
public abstract class FreezingSnowballEntityMixin extends ThrownItemEntity {
    public FreezingSnowballEntityMixin(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "onEntityHit", at = @At("RETURN"))
    public void freezeOnEntityHit(EntityHitResult entityHitResult, CallbackInfo ci) {
        Entity entity = entityHitResult.getEntity();
        Random random = this.getWorld().getRandom();
        if(entity instanceof LivingEntity livingEntity && random.nextFloat() > 0.7f) {
            livingEntity.setFrozenTicks(Math.max(200, livingEntity.getFrozenTicks()+50));
            livingEntity.setStatusEffect(
                    new StatusEffectInstance(StatusEffects.SLOWNESS,3*20,1,false,false),
                    this.getOwner()
            );
        }
    }
}
