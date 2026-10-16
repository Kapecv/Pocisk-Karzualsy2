package pl.kapiec.visuals;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class KapiecConfig {
    public boolean stretchEnabled = true;
    public float stretch = 1.33f;

    public boolean hearts = true;
    public float heartScale = 1.0f;

    public boolean critSound = true;
    public int soundIndex = 0;
    public float soundVolume = 1.0f;
    public float soundPitch = 1.0f;
    public boolean replaceVanillaCrit = false;

    public HandCfg main = new HandCfg();
    public HandCfg off = new HandCfg();

    public static class HandCfg {
        /** x, y, z, scale, rotX, rotY, rotZ */
        public float[] v = {0f, 0f, 0f, 1f, 0f, 0f, 0f};
    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE =
            FabricLoader.getInstance().getConfigDir().resolve("kapiec_visuals.json");
    private static KapiecConfig inst = new KapiecConfig();

    public static KapiecConfig get() { return inst; }

    public static void load() {
        try {
            if (Files.exists(FILE)) {
                KapiecConfig c = GSON.fromJson(Files.readString(FILE), KapiecConfig.class);
                if (c != null) inst = c;
            }
        } catch (Exception e) {
            inst = new KapiecConfig();
        }
    }

    public static void save() {
        try {
            Files.writeString(FILE, GSON.toJson(inst));
        } catch (IOException ignored) {}
    }
}
