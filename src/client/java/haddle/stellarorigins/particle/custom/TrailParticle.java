package haddle.stellarorigins.particle.custom;

import haddle.stellarorigins.StellarOrigins;
import haddle.stellarorigins.power.MeteorPower;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

public class TrailParticle extends SpriteBillboardParticle {

    private final Vec3d startPos;
    private final Vec3d endPos;
    private final Vec3d velocity;

    protected TrailParticle(ClientWorld level, double xCoord, double yCoord, double zCoord,
                           SpriteProvider spriteSet, double xd, double yd, double zd) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);
        this.startPos = new Vec3d(xCoord,yCoord,zCoord);
        this.endPos = new Vec3d(xd,yd,zd);
        this.velocity = endPos.subtract(startPos).normalize().multiply(MeteorPower.speed);

        this.setSpriteForAge(spriteSet);

        this.velocityX = velocity.x;
        this.velocityY = velocity.y;
        this.velocityZ = velocity.z;

        this.velocityMultiplier = 1f;

    }
    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        Vec3d cameraPos = camera.getPos();
        Vec3d pos = new Vec3d(
                MathHelper.lerp((double)tickDelta, this.prevPosX, this.x),
                MathHelper.lerp((double)tickDelta, this.prevPosY, this.y),
                MathHelper.lerp((double)tickDelta, this.prevPosZ, this.z)
        );

        Vec3d end;
        double len;
        if (pos.subtract(startPos).lengthSquared() > MeteorPower.trailLength * MeteorPower.trailLength) {
            end = pos.subtract(velocity.normalize().multiply(MeteorPower.trailLength)).subtract(cameraPos);
            len = MeteorPower.trailLength;
        } else {
            end = startPos.subtract(cameraPos);
            len = pos.subtract(startPos).length();
        }

        double heightScale = len / MeteorPower.trailLength * MeteorPower.trailheight;
        Vec3d top = pos.add(new Vec3d(0, heightScale, 0)).subtract(cameraPos);
        Vec3d bottom = pos.subtract(new Vec3d(0, heightScale, 0)).subtract(cameraPos);

        float minU = this.getMinU();
        float maxU = this.getMaxU();
        float minV = this.getMinV();
        float maxV = this.getMaxV();
        int brightness = this.getBrightness(tickDelta);
        vertexConsumer.vertex(top.getX(), top.getY(), top.getZ()).texture(maxU, maxV).color(this.red, this.green, this.blue, this.alpha).light(brightness).next();
        vertexConsumer.vertex(end.getX(), end.getY(), end.getZ()).texture(maxU, minV).color(this.red, this.green, this.blue, this.alpha).light(brightness).next();
        vertexConsumer.vertex(end.getX(), end.getY(), end.getZ()).texture(minU, minV).color(this.red, this.green, this.blue, this.alpha).light(brightness).next();
        vertexConsumer.vertex(bottom.getX(), bottom.getY(), bottom.getZ()).texture(minU, maxV).color(this.red, this.green, this.blue, this.alpha).light(brightness).next();
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider sprites;

        public Factory(SpriteProvider spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(DefaultParticleType particleType, ClientWorld level, double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new TrailParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }

    @Override
    protected int getBrightness(float tint) {
        return 255;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.age++ >= this.maxAge) {
            this.markDead();
        } else {
            this.move(this.velocityX, this.velocityY, this.velocityZ);
            if (this.onGround) {
                StellarOrigins.LOGGER.debug("GROUND");
            }
        }
    }

}
