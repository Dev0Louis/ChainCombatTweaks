package dev.louis.chaincombattweaks.mixin;

import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TntMinecartEntity.class)
public class TntMinecartEntityMixin {
    @Redirect(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;isOnFire()Z"))
    public boolean noExplosion(PersistentProjectileEntity instance) {
        return false;
    }
}
