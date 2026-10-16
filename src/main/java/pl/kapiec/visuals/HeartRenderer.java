package pl.kapiec.visuals;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public class HeartRenderer {
    private static final double MAX_DIST_SQ = 32.0 * 32.0;

    public static void render(WorldRenderContext ctx) {
        KapiecConfig cfg = KapiecConfig.get();
        if (!cfg.hearts) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.world == null || mc.player == null) return;

        MatrixStack matrices = ctx.matrixStack();
        VertexConsumerProvider consumers = ctx.consumers();
        if (matrices == null || consumers == null) return;

        Camera camera = ctx.camera();
        Vec3d cam = camera.getPos();
        float td = ctx.tickCounter().getTickDelta(false);
        TextRenderer tr = mc.textRenderer;

        for (Entity e : mc.world.getEntities()) {
            if (!(e instanceof LivingEntity le) || le == mc.player || le instanceof ArmorStandEntity) continue;
            if (!(le instanceof HostileEntity || le instanceof PlayerEntity)) continue;
            if (!le.isAlive() || le.squaredDistanceTo(mc.player) > MAX_DIST_SQ) continue;

            float max = Math.max(le.getMaxHealth(), 1f);
            float filled = MathHelper.clamp(le.getHealth() / max, 0f, 1f) * 10f;
            int full = (int) Math.floor(filled);
            boolean half = (filled - full) >= 0.5f;

            MutableText line = Text.empty();
            for (int i = 0; i < 10; i++) {
                Formatting f = i < full ? Formatting.RED
                        : (i == full && half ? Formatting.DARK_RED : Formatting.DARK_GRAY);
                line.append(Text.literal("\u2764").formatted(f));
            }
            line.append(Text.literal(String.format(" %.1f", le.getHealth())).formatted(Formatting.WHITE));

            Vec3d p = le.getLerpedPos(td);
            matrices.push();
            matrices.translate(p.x - cam.x, p.y + le.getHeight() + 0.75 - cam.y, p.z - cam.z);
            matrices.multiply(camera.getRotation());
            float s = 0.025f * cfg.heartScale;
            matrices.scale(-s, -s, s);

            Matrix4f m = matrices.peek().getPositionMatrix();
            float w = tr.getWidth(line);
            tr.draw(line, -w / 2f, 0f, 0xFFFFFFFF, false, m, consumers,
                    TextRenderer.TextLayerType.NORMAL, 0, LightmapTextureManager.MAX_LIGHT_COORDINATE);
            matrices.pop();
        }

        if (consumers instanceof VertexConsumerProvider.Immediate imm) imm.draw();
    }
}
