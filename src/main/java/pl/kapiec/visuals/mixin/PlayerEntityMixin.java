package pl.kapiec.visuals.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.kapiec.visuals.KapiecSounds;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "addCritParticles", at = @At("HEAD"))
    private void kapiec$crit(Entity target, CallbackInfo ci) {
        PlayerEntity self = (PlayerEntity) (Object) this;
        if (!self.getWorld().isClient) return;
        if (self != MinecraftClient.getInstance().player) return;
        KapiecSounds.playCrit();
    }
}
