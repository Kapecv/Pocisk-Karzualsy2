package pl.kapiec.visuals.mixin;

import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.kapiec.visuals.KapiecConfig;

@Mixin(SoundManager.class)
public class SoundManagerMixin {
    private static final Identifier KAPIEC_CRIT = Identifier.ofVanilla("entity.player.attack.crit");

    @Inject(method = "play(Lnet/minecraft/client/sound/SoundInstance;)V",
            at = @At("HEAD"), cancellable = true)
    private void kapiec$muteVanillaCrit(SoundInstance sound, CallbackInfo ci) {
        KapiecConfig c = KapiecConfig.get();
        if (c.critSound && c.replaceVanillaCrit && KAPIEC_CRIT.equals(sound.getId())) ci.cancel();
    }
}
