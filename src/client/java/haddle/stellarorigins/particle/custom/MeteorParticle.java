package haddle.stellarorigins.particle.custom;

import haddle.stellarorigins.StellarOrigins;
import haddle.stellarorigins.particle.SpriteRotationParticle;
import haddle.stellarorigins.power.MeteorPower;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.random.Random;

public class MeteorParticle extends SpriteBillboardParticle {


    private final Vec3d startPos;
    private final Vec3d endPos;
    private final Vec3d velocity;

    protected MeteorParticle(ClientWorld level, double xCoord, double yCoord, double zCoord,
                             SpriteProvider spriteSet, double xd, double yd, double zd) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);

        this.velocityMultiplier = 1f;
        this.x = xCoord;
        this.y = yCoord;
        this.z = zCoord;
        this.scale = 4F;
        this.maxAge = 40;
        this.setSpriteForAge(spriteSet);

        this.red = 1f;
        this.green = 0.592f;
        this.blue = 0.953f;

        this.startPos = new Vec3d(xCoord,yCoord,zCoord);
        this.endPos = new Vec3d(xd,yd,zd);
        this.velocity = endPos.subtract(startPos).normalize().multiply(MeteorPower.speed);

        this.velocityX = velocity.x;
        this.velocityY = velocity.y;
        this.velocityZ = velocity.z;

        this.velocityMultiplier = 1f;
    }


    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.age++ >= this.maxAge || this.endPos.distanceTo(new Vec3d(this.x,this.y,this.z)) < 5) {
            this.markDead();
        } else {
            this.move(this.velocityX, this.velocityY, this.velocityZ);
            if (this.onGround) {
                StellarOrigins.LOGGER.debug("GROUND");
            }
        }
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider sprites;

        public Factory(SpriteProvider spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(DefaultParticleType particleType, ClientWorld level, double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new MeteorParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }
    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        Vec3d vec3d = camera.getPos();
//        double tempX = this.x;
//        double tempY = this.y;
//        double tempZ = this.z;
//        Vec3d shiftVector = vec3d.subtract(new Vec3d(this.x, this.y, this.z)).normalize().multiply(2);
//        this.x += shiftVector.x;
//        this.y += shiftVector.y;
//        this.z += shiftVector.z;

        float f = (float)(MathHelper.lerp((double)tickDelta, this.prevPosX, this.x) - vec3d.getX());
        float g = (float)(MathHelper.lerp((double)tickDelta, this.prevPosY, this.y) - vec3d.getY());
        float h = (float)(MathHelper.lerp((double)tickDelta, this.prevPosZ, this.z) - vec3d.getZ());
        Quaternion quaternion;
        Vec3d A = new Vec3d(f, g, h).normalize();
//        Vec3d B = new Vec3d(0, 0, -1);
//        Vec3d crossProduct = A.crossProduct(B);
//        Vec3f crossProductF = new Vec3f(crossProduct);
//        quaternion = new Quaternion(crossProductF, (float)Math.acos(A.dotProduct(B)), false);
////        quaternion.normalize();
        quaternion = LookAt(A);

        Vec3f vec3f = new Vec3f(-1.0F, -1.0F, 0.0F);
        vec3f.rotate(quaternion);
        Vec3f[] vec3fs = new Vec3f[]{new Vec3f(-1.0F, -1.0F, -2.0F), new Vec3f(-1.0F, 1.0F, -2.0F), new Vec3f(1.0F, 1.0F, -2.0F), new Vec3f(1.0F, -1.0F, -2.0F)};
        float j = this.getSize(tickDelta);

        for(int k = 0; k < 4; ++k) {
            Vec3f vec3f2 = vec3fs[k];
            vec3f2.rotate(quaternion);
            vec3f2.scale(j);
            vec3f2.add(f, g, h);
        }

        float l = this.getMinU();
        float m = this.getMaxU();
        float n = this.getMinV();
        float o = this.getMaxV();
        int p = this.getBrightness(tickDelta);
        vertexConsumer.vertex((double)vec3fs[0].getX(), (double)vec3fs[0].getY(), (double)vec3fs[0].getZ()).texture(m, o).color(this.red, this.green, this.blue, this.alpha).light(p).next();
        vertexConsumer.vertex((double)vec3fs[1].getX(), (double)vec3fs[1].getY(), (double)vec3fs[1].getZ()).texture(m, n).color(this.red, this.green, this.blue, this.alpha).light(p).next();
        vertexConsumer.vertex((double)vec3fs[2].getX(), (double)vec3fs[2].getY(), (double)vec3fs[2].getZ()).texture(l, n).color(this.red, this.green, this.blue, this.alpha).light(p).next();
        vertexConsumer.vertex((double)vec3fs[3].getX(), (double)vec3fs[3].getY(), (double)vec3fs[3].getZ()).texture(l, o).color(this.red, this.green, this.blue, this.alpha).light(p).next();
//        this.x = tempX;
//        this.y = tempY;
//        this.z = tempZ;
    }

    // Source - https://stackoverflow.com/a
    // Posted by nasskov, modified by community. See post 'Timeline' for change history
    // Retrieved 2025-11-23, License - CC BY-SA 4.0

    public static Quaternion LookAt(Vec3d forwardVector)
    {

        double dot = new Vec3d(0,0,1).dotProduct(forwardVector);

        if (Math.abs(dot - (-1.0f)) < 0.000001f)
        {
            return new Quaternion(0, 0, 0, -1);
        }
        if (Math.abs(dot - (1.0f)) < 0.000001f)
        {
            return Quaternion.IDENTITY;
        }

        float rotAngle = (float)Math.acos(dot);
        Vec3d rotAxis = new Vec3d(0,0,1).crossProduct(forwardVector).normalize();
        return CreateFromAxisAngle(rotAxis, rotAngle);
    }

    // just in case you need that function also
    public static Quaternion CreateFromAxisAngle(Vec3d axis, float angle)
    {
        float halfAngle = angle * .5f;
        float s = (float)Math.sin(halfAngle);
        Quaternion q = new Quaternion((float) (axis.x * s), (float) (axis.y * s), (float) (axis.z * s), (float)Math.cos(halfAngle));
        return q;
    }

    @Override
    protected int getBrightness(float tint) {
        return 255;
    }
}
