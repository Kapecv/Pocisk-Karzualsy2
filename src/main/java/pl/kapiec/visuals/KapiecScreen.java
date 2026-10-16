package pl.kapiec.visuals;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class KapiecScreen extends Screen {
    private int colL, colR, colW, headerY;

    public KapiecScreen() { super(Text.literal("Kapiec VISUALS")); }

    @Override
    protected void init() {
        KapiecConfig c = KapiecConfig.get();
        colW = 150;
        int g = 6;
        colL = width / 2 - colW - g / 2;
        colR = width / 2 + g / 2;
        int y = 10, step = 19;

        addDrawableChild(toggle(colL, y, "Stretch", () -> c.stretchEnabled, v -> c.stretchEnabled = v));
        addDrawableChild(new FloatSlider(colR, y, colW, 18, "Stretch", 0.5f, 2.5f, c.stretch, v -> c.stretch = v));
        y += step;

        addDrawableChild(toggle(colL, y, "Hearts", () -> c.hearts, v -> c.hearts = v));
        addDrawableChild(new FloatSlider(colR, y, colW, 18, "Heart size", 0.5f, 2.0f, c.heartScale, v -> c.heartScale = v));
        y += step;

        addDrawableChild(toggle(colL, y, "Crit sound", () -> c.critSound, v -> c.critSound = v));
        addDrawableChild(ButtonWidget.builder(
                Text.literal("Sound: " + KapiecSounds.NAMES[c.soundIndex % KapiecSounds.NAMES.length]),
                b -> {
                    c.soundIndex = (c.soundIndex + 1) % KapiecSounds.NAMES.length;
                    b.setMessage(Text.literal("Sound: " + KapiecSounds.NAMES[c.soundIndex]));
                    KapiecSounds.preview();
                }).dimensions(colR, y, colW, 18).build());
        y += step;

        addDrawableChild(new FloatSlider(colL, y, colW, 18, "Volume", 0.1f, 2.0f, c.soundVolume, v -> c.soundVolume = v));
        addDrawableChild(new FloatSlider(colR, y, colW, 18, "Pitch", 0.5f, 2.0f, c.soundPitch, v -> c.soundPitch = v));
        y += step;

        addDrawableChild(toggle(colL, y, "Mute vanilla crit", () -> c.replaceVanillaCrit, v -> c.replaceVanillaCrit = v));
        y += step + 4;

        headerY = y;
        y += 11;
        String[] names = {"X", "Y", "Z", "Scale", "Rot X", "Rot Y", "Rot Z"};
        float[] mn = {-1f, -1f, -1.5f, 0.1f, -180f, -180f, -180f};
        float[] mx = {1f, 1f, 1f, 2f, 180f, 180f, 180f};
        for (int i = 0; i < 7; i++) {
            final int idx = i;
            addDrawableChild(new FloatSlider(colL, y, colW, 16, names[i], mn[i], mx[i],
                    c.main.v[i], val -> c.main.v[idx] = val));
            addDrawableChild(new FloatSlider(colR, y, colW, 16, names[i], mn[i], mx[i],
                    c.off.v[i], val -> c.off.v[idx] = val));
            y += 17;
        }
        y += 4;

        addDrawableChild(ButtonWidget.builder(Text.literal("Reset hands"), b -> {
            c.main = new KapiecConfig.HandCfg();
            c.off = new KapiecConfig.HandCfg();
            clearAndInit();
        }).dimensions(colL, y, colW, 18).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Done"), b -> close())
                .dimensions(colR, y, colW, 18).build());
    }

    private ButtonWidget toggle(int x, int y, String name, Supplier<Boolean> get, Consumer<Boolean> set) {
        return ButtonWidget.builder(label(name, get.get()), b -> {
            set.accept(!get.get());
            b.setMessage(label(name, get.get()));
        }).dimensions(x, y, colW, 18).build();
    }

    private static Text label(String name, boolean on) {
        return Text.literal(name + ": " + (on ? "ON" : "OFF"));
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        super.render(ctx, mx, my, delta);
        ctx.drawCenteredTextWithShadow(textRenderer, Text.literal("Main hand"), colL + colW / 2, headerY, 0xFFFFFF);
        ctx.drawCenteredTextWithShadow(textRenderer, Text.literal("Off hand"), colR + colW / 2, headerY, 0xFFFFFF);
    }

    @Override
    public void removed() {
        KapiecConfig.save();
    }

    private static class FloatSlider extends SliderWidget {
        private final String label;
        private final float min, max;
        private final Consumer<Float> setter;

        FloatSlider(int x, int y, int w, int h, String label, float min, float max,
                    float cur, Consumer<Float> setter) {
            super(x, y, w, h, Text.empty(), (cur - min) / (max - min));
            this.label = label;
            this.min = min;
            this.max = max;
            this.setter = setter;
            updateMessage();
        }

        private float val() { return (float) (min + value * (max - min)); }

        @Override
        protected void updateMessage() {
            setMessage(Text.literal(label + ": " + String.format("%.2f", val())));
        }

        @Override
        protected void applyValue() { setter.accept(val()); }
    }
}
