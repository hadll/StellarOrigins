package haddle.stellarorigins.power;


import haddle.stellarorigins.StellarOrigins;
import haddle.stellarorigins.registry.SODamageSources;
import haddle.stellarorigins.registry.SOParticles;
import haddle.stellarorigins.registry.SOSounds;
import haddle.stellarorigins.util.ParticleSpawnHelper;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.ActiveCooldownPower;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.util.HudRender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import java.util.List;
import java.util.function.Consumer;

public class MeteorPower extends ActiveCooldownPower {

    public static double trailLength = 7;
    public static double trailheight = 2.5;

    public static double speed = 5;
    private final double explosionSize = 5;

    private Vec3d direction;
    private Vec3d endPos;
    private boolean willLand = false;
    private boolean usingPower = false;
    private double flightTime = 0;
    public final double maxFlightTime = 50;

    public MeteorPower(PowerType<?> type, LivingEntity entity, int cooldownDuration, HudRender hudRender, Consumer<Entity> activeFunction) {
        super(type, entity, cooldownDuration, hudRender, activeFunction);
        this.setTicking(true);
    }

    @Override
    public void tick() {
        super.tick();
        flightTime++;

        if (!usingPower) return;

        if (endPos == null) {
            this.setUsingPower(false);
            return;
        }

        if (flightTime <= maxFlightTime && usingPower){
            PlayerEntity player = (PlayerEntity) entity;


            if (player.getPos().distanceTo(endPos) < 5) {
                this.setUsingPower(false);
                direction = endPos.subtract(player.getPos()).normalize();
                explosion();
            }else{
                direction = endPos.subtract(player.getPos()).normalize().multiply(speed);
            }

            player.setVelocity(direction);
            player.velocityModified = true;
        }else{
            this.setUsingPower(false);
        }



    }

    private Box generateExplosionHitbox(Vec3d origin, double size){
        return new Box(origin, origin).expand(size);
    }

    private float calculateDamage(Entity target){
        double distance_mult = 5-target.getPos().distanceTo(endPos);
        double charge_power = Math.max(Math.min((flightTime-5)/10.0,1.0),0.0);
        double power_mult = 4;
        return (float) (charge_power*distance_mult*power_mult);
    }

    public void explosion(){
        if (!willLand){
            return;
        }
        for (Vec3d offset: ParticleSpawnHelper.CalculateOffset((PlayerEntity) entity, direction, 100, true)){
            ParticleSpawnHelper.SpawnSyncedParticle(entity, SOParticles.TRACE_PARTICLE, entity.getX(), entity.getY(), entity.getZ(), offset.x,  offset.y, offset.z);
        }

        entity.getWorld().playSoundFromEntity(null, entity, SOSounds.METEOR_SOUND, SoundCategory.PLAYERS, 3f, 1f);
        List<Entity> hits = entity.getWorld().getOtherEntities(entity, generateExplosionHitbox(entity.getPos(), explosionSize));
        for (Entity hit:hits){
            float damage = calculateDamage(hit);
            hit.damage(SODamageSources.meteor((PlayerEntity) entity), damage);
            Vec3d launch = hit.getPos().subtract(entity.getPos()).normalize().multiply(damage*0.5);
            hit.addVelocity(launch.x, launch.y*0.01, launch.z);
        }
    }

    public void setUsingPower(boolean active) {
        if (this.usingPower != active) {
            this.usingPower = active;

            PowerHolderComponent.syncPower(this.entity, this.getType());
        }
    }

    @Override
    public void onUse() {
        if (!(StellarOrigins.STARPOWER_HELPER.getStarpower(entity) > 40)){
            return;
        }
        if (usingPower) {
            return;
        }
        if (!entity.getWorld().isClient()) {
            StellarOrigins.STARPOWER_HELPER.consumeStarpower(entity, -40);
        }
        this.setUsingPower(true);
        flightTime = 0;
        PlayerEntity player = (PlayerEntity) entity;
//        if (result != null && result.getType() != HitResult.Type.MISS) {
//            player.world.addParticle(SOParticles.METEOR_PARTICLE, result.getPos().x, result.getPos().y, result.getPos().z, 0,0, 0);
//        }
        Vec3d lookDir = player.getRotationVec(1.0F);
        direction = lookDir.multiply(speed);
        endPos = player.getEyePos().add(lookDir.multiply(maxFlightTime*speed*20));
        HitResult hit = player.world.raycast(new RaycastContext(player.getEyePos(), endPos, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, entity));
        if (hit.getType() == HitResult.Type.BLOCK) {
            endPos = hit.getPos();
            willLand = true;
        }else {
            willLand = false;
        }
        ParticleSpawnHelper.SpawnSyncedParticle(entity, SOParticles.METEOR_PARTICLE, player.getX(),  player.getY(), player.getZ(), endPos.x, endPos.y, endPos.z, maxFlightTime*speed*20);
//        player.world.addParticle(SOParticles.TRAIL_PARTICLE, player.getX(),  player.getY(), player.getZ(), endPos.x, endPos.y, endPos.z);
    }

    public boolean isUsingPower(){
        return usingPower;
    }

    @Override
    public NbtElement toTag() {
        NbtCompound tag = new NbtCompound();
        tag.putBoolean("usingPower", usingPower);
        return tag;
    }

    @Override
    public void fromTag(NbtElement tag) {
        if (tag instanceof NbtCompound compound) {
            this.usingPower = compound.getBoolean("usingPower");
        }
    }
}
