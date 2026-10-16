package pl.kapiec.visuals;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class KapiecVisualsClient implements ClientModInitializer {
    private static final String CAT = "category.kapiec_visuals";
    private static KeyBinding toggleKey, menuKey;

    @Override
    public void onInitializeClient() {
        KapiecConfig.load();

        toggleKey = reg("key.kapiec_visuals.toggle", GLFW.GLFW_KEY_K);
        menuKey = reg("key.kapiec_visuals.menu", GLFW.GLFW_KEY_INSERT);

        WorldRenderEvents.AFTER_ENTITIES.register(HeartRenderer::render);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.wasPressed()) {
                KapiecConfig c = KapiecConfig.get();
                c.stretchEnabled = !c.stretchEnabled;
                KapiecConfig.save();
                if (client.player != null) {
                    client.player.sendMessage(Text.literal(
                            "[Kapiec VISUALS] Stretch: " + (c.stretchEnabled ? "ON" : "OFF")), true);
                }
            }
            while (menuKey.wasPressed()) {
                if (client.currentScreen instanceof KapiecScreen) {
                    client.setScreen(null);
                } else if (client.currentScreen == null) {
                    client.setScreen(new KapiecScreen());
                }
            }
        });
    }

    private static KeyBinding reg(String id, int key) {
        return KeyBindingHelper.registerKeyBinding(
                new KeyBinding(id, InputUtil.Type.KEYSYM, key, CAT));
    }
}
