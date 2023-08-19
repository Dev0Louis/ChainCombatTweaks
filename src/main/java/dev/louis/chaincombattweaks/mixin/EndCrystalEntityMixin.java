package dev.louis.chaincombattweaks.mixin;

import net.minecraft.entity.decoration.EndCrystalEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(EndCrystalEntity.class)
public class EndCrystalEntityMixin {

    @ModifyConstant(method = "damage", constant = @Constant(floatValue = 6), require = 1)
    public float lessExplosionPower(float constant) {
        return 1;
    }

}
