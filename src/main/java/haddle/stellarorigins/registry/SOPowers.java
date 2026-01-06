package haddle.stellarorigins.registry;

import haddle.stellarorigins.StellarOrigins;
import haddle.stellarorigins.power.LaunchPower;
import haddle.stellarorigins.power.MeteorPower;
import haddle.stellarorigins.power.ParryPower;
import haddle.stellarorigins.power.StarpowerPower;
import haddle.stellarorigins.util.ParticleSpawnHelper;
import haddle.stellarorigins.util.StarpowerHelper;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Active;
import io.github.apace100.apoli.power.ModifyDamageTakenPower;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.power.factory.PowerFactorySupplier;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.apoli.util.HudRender;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;


public class SOPowers{
    public static final PowerFactory<Power> LAUNCH = new PowerFactory<>(new Identifier(StellarOrigins.MOD_ID, "launch"), new SerializableData().add("key", ApoliDataTypes.BACKWARDS_COMPATIBLE_KEY, new Active.Key()), data -> (type, entity) -> {
        LaunchPower power = new LaunchPower(type, entity, 20, HudRender.DONT_RENDER, null);
        power.setKey((Active.Key) data.get("key"));
        return power;
    }).allowCondition();
    public static final PowerFactory<Power> METEOR = new PowerFactory<>(new Identifier(StellarOrigins.MOD_ID, "meteor"), new SerializableData().add("key", ApoliDataTypes.BACKWARDS_COMPATIBLE_KEY, new Active.Key()), data -> (type, entity) -> {
        MeteorPower power = new MeteorPower(type, entity, 20, HudRender.DONT_RENDER, null);
        power.setKey((Active.Key) data.get("key"));
        return power;
    }).allowCondition();

    public static final PowerFactory<Power> STARPOWER = new PowerFactory<>(new Identifier(StellarOrigins.MOD_ID, "starpower"), new SerializableData(), data -> (type, entity) -> new StarpowerPower(type, entity, 20)).allowCondition();

    private static void register(PowerFactory<?> powerFactory) {
        Registry.register(ApoliRegistries.POWER_FACTORY, powerFactory.getSerializerId(), powerFactory);
    }

    private static void register(PowerFactorySupplier<?> factorySupplier) {
        register(factorySupplier.createFactory());
    }

    private static void register(ActionFactory<Entity> actionFactory) {
        Registry.register(ApoliRegistries.ENTITY_ACTION, actionFactory.getSerializerId(), actionFactory);
    }

    public static final StarpowerHelper STARPOWER_HELPER = new StarpowerHelper();

    public static void init() {
        Registry.register(ApoliRegistries.POWER_FACTORY, LAUNCH.getSerializerId(), LAUNCH);
        Registry.register(ApoliRegistries.POWER_FACTORY, METEOR.getSerializerId(), METEOR);
        Registry.register(ApoliRegistries.POWER_FACTORY, STARPOWER.getSerializerId(), STARPOWER);
        register(new ActionFactory<>(Apoli.identifier("parry_particle"), new SerializableData(),
                (data, entity) -> {
                    PlayerEntity player = (PlayerEntity) entity;
                    World playerWorld = player.world;
                    Vec3d spawnPos = player.getRotationVec(1).multiply(1).add(player.getEyePos());
                    ParticleSpawnHelper.SpawnSyncedParticle(entity, SOParticles.PARRY_PARTICLE, spawnPos.x, spawnPos.y, spawnPos.z, player.getPitch(), -player.getHeadYaw(), 0);
                    ParticleSpawnHelper.SpawnSyncedParticle(entity, SOParticles.PARRY_PARTICLE, spawnPos.x, spawnPos.y, spawnPos.z, -player.getPitch(), player.getHeadYaw(), 0);
//                    playerWorld.addParticle(SOParticles.PARRY_PARTICLE, spawnPos.x, spawnPos.y, spawnPos.z, -player.getPitch(), -player.getHeadYaw(), 0);
                }));
        register(ParryPower::createFactory);

        StellarOrigins.LOGGER.info(StellarOrigins.MOD_ID+": Powers Loaded!");
    }
}

