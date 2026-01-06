package haddle.stellarorigins.registry;

import haddle.stellarorigins.StellarOrigins;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SOParticles {
    public static final DefaultParticleType STAR_PARTICLE = FabricParticleTypes.simple();
    public static final DefaultParticleType TRACE_PARTICLE = FabricParticleTypes.simple();
    public static final DefaultParticleType METEOR_PARTICLE = FabricParticleTypes.simple();
    public static final DefaultParticleType TRAIL_PARTICLE = FabricParticleTypes.simple();
    public static final DefaultParticleType STARFALL_PARTICLE = FabricParticleTypes.simple();
    public static final DefaultParticleType PARRY_PARTICLE = FabricParticleTypes.simple();
    public static final DefaultParticleType PARRY_SUCCESS_PARTICLE = FabricParticleTypes.simple();
    public static void init(){
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(StellarOrigins.MOD_ID, "stars"), STAR_PARTICLE);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(StellarOrigins.MOD_ID, "trace"), TRACE_PARTICLE);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(StellarOrigins.MOD_ID, "meteor"), METEOR_PARTICLE);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(StellarOrigins.MOD_ID, "comet_gradient"), TRAIL_PARTICLE);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(StellarOrigins.MOD_ID, "starfall"), STARFALL_PARTICLE);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(StellarOrigins.MOD_ID, "parry_particle"), PARRY_PARTICLE);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(StellarOrigins.MOD_ID, "parry_success_particle"), PARRY_SUCCESS_PARTICLE);

        StellarOrigins.LOGGER.info(StellarOrigins.MOD_ID+": Particles Loaded!");
    }
}
