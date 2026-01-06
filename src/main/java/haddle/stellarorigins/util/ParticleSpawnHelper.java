package haddle.stellarorigins.util;

import haddle.stellarorigins.registry.SOParticles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;

public class ParticleSpawnHelper {


    public static List<Vec3d> CalculateOffset(PlayerEntity player, Vec3d forwardVec, int amount, boolean random){
        World playerWorld = player.getWorld();
        Vec3d midPos = player.getPos().add(player.getEyePos()).multiply(0.5);
        Vec3d otherVec;
        if (abs(forwardVec.x) > 0.95) {
            otherVec = new Vec3d(0, 1, 0);
        } else {
            otherVec = new Vec3d(1, 0, 0);
        }
        Vec3d m1 = forwardVec.crossProduct(otherVec).normalize();
        Vec3d m2 = forwardVec.crossProduct(m1).normalize();
        List<Vec3d> offsets = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            double angle;
            if (random){
                angle = Math.random() * Math.PI * 2.0;
            }else{
                angle = toRadians((double) (i * 360) / amount);
            }
            offsets.add(m1.multiply(cos(angle)).subtract(m2.multiply(sin(angle))));
        }
        return offsets;
    }

    public static void SpawnSyncedParticle(Entity entity, ParticleEffect particle, double x, double y, double z, int count, double dx, double dy, double dz, double speed) {
        if (!entity.getWorld().isClient()) {
            ((ServerWorld) entity.getWorld()).spawnParticles(
                    particle,
                    x,
                    y,
                    z,
                    count,      // count
                    dx, dy, dz, // offset
                    speed     // speed
            );
        }
    }
    public static void SpawnSyncedParticle(Entity entity, ParticleEffect particle, double x, double y, double z, double dx, double dy, double dz){
        SpawnSyncedParticle(entity, particle, x,y,z, 0,dx,dy,dz,1);
    }
}
