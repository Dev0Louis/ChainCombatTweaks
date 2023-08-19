package dev.louis.chaincombattweaks.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(ArrowEntity.class)
public abstract class NoInstantDamageArrowMixin extends PersistentProjectileEntity {
    @Shadow private Potion potion;

    @Shadow @Final private Set<StatusEffectInstance> effects;

    @Shadow @Final private static TrackedData<Integer> COLOR;

    protected NoInstantDamageArrowMixin(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initFromStack", at = @At("RETURN"))
    public void noInstantDamageEffectArrows(ItemStack stack, CallbackInfo ci) {
        AtomicBoolean atomicBoolean = new AtomicBoolean();
        this.potion.getEffects().forEach((statusEffectInstance -> {
            if(statusEffectInstance.getEffectType().equals(StatusEffects.INSTANT_DAMAGE)) {
                atomicBoolean.set(true);
            }
        }));
        this.effects.forEach((statusEffectInstance -> {
            if(statusEffectInstance.getEffectType().equals(StatusEffects.INSTANT_DAMAGE)) {
                atomicBoolean.set(true);
            }
        }));
        if(atomicBoolean.get()) {
            this.potion = Potions.EMPTY;
            this.effects.clear();
            this.dataTracker.set(COLOR, -1);

        }
    }
}
