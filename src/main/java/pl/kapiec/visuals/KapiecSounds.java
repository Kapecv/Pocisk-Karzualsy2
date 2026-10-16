package pl.kapiec.visuals;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class KapiecSounds {
    public static final String[] NAMES = {
            "Orb pickup", "Arrow hit", "Level up", "Anvil", "Custom (crit.ogg)"
    };

    private static final SoundEvent[] EVENTS = {
            SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,
            SoundEvents.ENTITY_ARROW_HIT_PLAYER,
            SoundEvents.ENTITY_PLAYER_LEVELUP,
            SoundEvents.BLOCK_ANVIL_LAND,
            SoundEvent.of(Identifier.of("kapiec_visuals", "crit"))
    };

    public static void playCrit() {
        if (KapiecConfig.get().critSound) preview();
    }

    public static void preview() {
        KapiecConfig c = KapiecConfig.get();
        SoundEvent e = EVENTS[Math.floorMod(c.soundIndex, EVENTS.length)];
        MinecraftClient.getInstance().getSoundManager()
                .play(PositionedSoundInstance.master(e, c.soundPitch, c.soundVolume));
    }
}
