package haddle.stellarorigins.registry;

import haddle.stellarorigins.StellarOrigins;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SOSounds {
    public static final SoundEvent LAUNCH_SOUND = registerSoundEvent("launch_sound");
    public static final SoundEvent STARFALL_SOUND = registerSoundEvent("starfall_sound");
    public static final SoundEvent PARRY_SOUND = registerSoundEvent("parry_sound");
    public static final SoundEvent METEOR_SOUND = registerSoundEvent("meteor_sound");
    public static final SoundEvent RECHARGE_SOUND = registerSoundEvent("recharge_sound");


    private static SoundEvent registerSoundEvent(String name){
        Identifier id = new Identifier(StellarOrigins.MOD_ID, name);
        return Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(id));
    }
    public static void init(){
        StellarOrigins.LOGGER.info(StellarOrigins.MOD_ID+": Sounds Loaded!");
    }
}
