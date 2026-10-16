package pl.kapiec.visuals.mixin;

import net.minecraft.client.render.GameRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pl.kapiec.visuals.KapiecConfig;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "getBasicProjectionMatrix", at = @At("RETURN"))
    private void kapiec$stretch(float fov, CallbackInfoReturnable<Matrix4f> cir) {
        KapiecConfig c = KapiecConfig.get();
        if (c.stretchEnabled) cir.getReturnValue().scaleLocal(c.stretch, 1.0f, 1.0f);
    }
}
