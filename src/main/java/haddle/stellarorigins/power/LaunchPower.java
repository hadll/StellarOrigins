package haddle.stellarorigins.power;

import haddle.stellarorigins.StellarOrigins;
import haddle.stellarorigins.events.EntityFallCallback;
import haddle.stellarorigins.registry.SOComponents;
import haddle.stellarorigins.registry.SOParticles;
import haddle.stellarorigins.registry.SOPowers;
import haddle.stellarorigins.registry.SOSounds;
import haddle.stellarorigins.util.ParticleSpawnHelper;
import haddle.stellarorigins.util.StarpowerHelper;
import io.github.apace100.apoli.power.ActiveCooldownPower;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.util.HudRender;
import java.util.function.Consumer;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import static java.lang.Math.*;

public class LaunchPower extends ActiveCooldownPower {
    private final int waveDashTicks = 5;
    private int ticksSinceLaunch = 5;
    private boolean usedWaveDash = false;

    public double cost = 3;

    public LaunchPower(PowerType<?> type, LivingEntity entity, int cooldownDuration, HudRender hudRender, Consumer<Entity> activeFunction) {
        super(type, entity, cooldownDuration, hudRender, activeFunction);
        setTicking(true);
    }

    private void launch(PlayerEntity player){

        // launch the player
        Vec3d eyeDir = player.getRotationVec(1.0F).multiply(2);
        player.addVelocity(eyeDir.getX(), eyeDir.getY(), eyeDir.getZ());
        player.velocityModified = true;

//        String side = "----Client----";
//        if (!entity.getWorld().isClient) {
//            side = "----Server----";
//        }
//        StellarOrigins.LOGGER.info(side);
//        StellarOrigins.LOGGER.info("Printing Component value:");
//        StellarOrigins.LOGGER.info(String.valueOf(SOComponents.STARPOWER.get(player).getValue()));
//        StellarOrigins.LOGGER.info("Printing StarpowerHelper value:");
//        StellarOrigins.LOGGER.info(String.valueOf(StellarOrigins.STARPOWER_HELPER.getStarpower(entity)));
//        StellarOrigins.LOGGER.info(side);


        // spawn particles
        World playerWorld = player.getWorld();
        Vec3d midPos = player.getPos().add(player.getEyePos()).multiply(0.5);
        Vec3d otherVec;
        if (abs(eyeDir.x) > 0.95) {
            otherVec = new Vec3d(0, 1, 0);
        } else {
            otherVec = new Vec3d(1, 0, 0);
        }
        Vec3d m1 = eyeDir.crossProduct(otherVec).normalize();
        Vec3d m2 = eyeDir.crossProduct(m1).normalize();
        for (int i = 0; i < 30; i++){
            double angle = toRadians(i*12);
            Vec3d offset = m1.multiply(cos(angle)).subtract(m2.multiply(sin(angle)));
            Vec3d pos = midPos.add(offset.multiply(0.2));
            Vec3d vel = eyeDir.multiply(-3).add(offset.multiply(3));
            ParticleSpawnHelper.SpawnSyncedParticle(entity, SOParticles.STAR_PARTICLE, pos.x, pos.y, pos.z, vel.x, vel.y, vel.z);
            if (i%5 == 0){
                pos = midPos.add(offset.multiply(0.5)).add(eyeDir.multiply(random()));
                vel = eyeDir.multiply(random()*0.5+0.1);
                ParticleSpawnHelper.SpawnSyncedParticle(entity, SOParticles.TRACE_PARTICLE, pos.x, pos.y, pos.z, vel.x, vel.y, vel.z);
            }
        }

        // play sound
//        if (!player.world.isClient) {
            // Play the sound as if it was coming from the entity.
            player.playSound(SOSounds.LAUNCH_SOUND, 2f, 0.7f);
            StellarOrigins.LOGGER.debug("sound played!");
//        }
//        playerWorld.addParticle(SOParticles.STAR_PARTICLE, eyeDir.x + feetPos.x, eyeDir.y + feetPos.y, eyeDir.z + feetPos.z, 0, 0, 0);
//        playerWorld.addParticle(SOParticles.STAR_PARTICLE, m1.x + feetPos.x, m1.y + feetPos.y, m1.z + feetPos.z, 0, 0, 0);
//        playerWorld.addParticle(SOParticles.STAR_PARTICLE, m2.x + feetPos.x, m2.y + feetPos.y, m2.z + feetPos.z, 0, 0, 0);
    }

    @Override
    public void onUse() {
        PlayerEntity player = (PlayerEntity) entity;

        if (!player.isOnGround() && StellarOrigins.STARPOWER_HELPER.attemptConsumeStarpower(entity, cost)){
            launch(player);
            ticksSinceLaunch = 0;
            usedWaveDash = false;
        }
    }

    @Override
    public void onAdded() {
        super.onAdded();
        EntityFallCallback.EVENT.register(() -> {
            return ActionResult.PASS;
        });
    }

    @Override
    public void onRemoved(){
        super.onRemoved();
    }

    @Override
    public void tick(){
        PlayerEntity player = (PlayerEntity) entity;
        super.tick();
        ticksSinceLaunch ++;
        if (ticksSinceLaunch < waveDashTicks && player.isOnGround() && !usedWaveDash) {
//            System.out.println(entity.getName());
            Vec3d lookDir = player.getRotationVec(0f);
            player.addVelocity(lookDir.getX(), 0.7f, lookDir.getZ()); // <-- this isnt working for some reason
            player.velocityModified = true;
            usedWaveDash = true;
            // launch the player
            Vec3d eyeDir = new Vec3d(lookDir.getX(), 0.5, lookDir.getZ()).normalize();
            // spawn particles
            World playerWorld = player.getWorld();
            Vec3d midPos = player.getPos().add(player.getEyePos()).multiply(0.5);
            Vec3d otherVec;
            if (abs(eyeDir.x) > 0.95) {
                otherVec = new Vec3d(0, 1, 0);
            } else {
                otherVec = new Vec3d(1, 0, 0);
            }
            Vec3d m1 = eyeDir.crossProduct(otherVec).normalize();
            Vec3d m2 = eyeDir.crossProduct(m1).normalize();
            for (int i = 0; i < 6; i++){
                double angle = toRadians(i*60);
                Vec3d offset = m1.multiply(cos(angle)).subtract(m2.multiply(sin(angle)));
                midPos.add(offset.multiply(0.2));
                Vec3d pos = midPos.add(offset.multiply(0.5)).add(eyeDir.multiply(random()));
                Vec3d vel = eyeDir.multiply(random()*0.5+0.1);
                ParticleSpawnHelper.SpawnSyncedParticle(entity, SOParticles.TRACE_PARTICLE, pos.x, pos.y, pos.z, vel.x, vel.y, vel.z);
            }
        }
    }
}

