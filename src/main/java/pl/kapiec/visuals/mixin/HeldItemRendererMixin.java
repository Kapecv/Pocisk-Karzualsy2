package pl.kapiec.visuals.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.kapiec.visuals.KapiecConfig;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {

    @Inject(method = "renderFirstPersonItem", at = @At("HEAD"))
    private void kapiec$pre(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand,
                            float swingProgress, ItemStack item, float equipProgress,
                            MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
                            CallbackInfo ci) {
        KapiecConfig c = KapiecConfig.get();
        float[] v = (hand == Hand.MAIN_HAND ? c.main : c.off).v;

        matrices.push();
        matrices.translate(v[0], v[1], v[2]);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(v[4]));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(v[5]));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(v[6]));
        matrices.scale(v[3], v[3], v[3]);
    }

    @Inject(method = "renderFirstPersonItem", at = @At("RETURN"))
    private void kapiec$post(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand,
                             float swingProgress, ItemStack item, float equipProgress,
                             MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
                             CallbackInfo ci) {
        matrices.pop();
    }
}
