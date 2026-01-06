package haddle.stellarorigins.particle.custom;

import haddle.stellarorigins.particle.SpriteRotationParticle;
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

public class ParryIndicatorParticle extends SpriteBillboardParticle {

    private double scaleMult = 0.5;
    private Quaternion rotation;

    protected ParryIndicatorParticle(ClientWorld level, double xCoord, double yCoord, double zCoord,
                               SpriteProvider spriteSet, double xd, double yd, double zd) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);
        this.rotation = Quaternion.fromEulerYxz((float) Math.toRadians(yd), (float) Math.toRadians(xd), (float) Math.toRadians(zd));
        this.velocityMultiplier = 0f;
        this.x = xCoord;
        this.y = yCoord;
        this.z = zCoord;
        this.scale *= 2F;
        this.maxAge = 10;
        this.setSpriteForAge(spriteSet);

        this.red = 1f;
        this.green = 0.592f;
        this.blue = 0.953f;

        this.velocityX = 0;
        this.velocityY = 0;
        this.velocityZ = 0;
    }

    private void fadeOut() {
        this.alpha = (-(1/(float)maxAge) * age + 1);
        this.scale = (float) (Math.log(age+3) / Math.log(3) * scaleMult) ;
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
            return new ParryIndicatorParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }

    @Override
    protected int getBrightness(float tint) {
        return 255;
    }

    @Override
    public void tick(){
        fadeOut();
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.age++ >= this.maxAge) {
            this.markDead();
        } else {
            this.move(this.velocityX, this.velocityY, this.velocityZ);
            this.velocityX *= (double)this.velocityMultiplier;
            this.velocityY *= (double)this.velocityMultiplier;
            this.velocityZ *= (double)this.velocityMultiplier;
        }
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta){
        Vec3d vec3d = camera.getPos();
        float f = (float)(MathHelper.lerp((double)tickDelta, this.prevPosX, this.x) - vec3d.getX());
        float g = (float)(MathHelper.lerp((double)tickDelta, this.prevPosY, this.y) - vec3d.getY());
        float h = (float)(MathHelper.lerp((double)tickDelta, this.prevPosZ, this.z) - vec3d.getZ());

        Vec3f vec3f = new Vec3f(-1.0F, -1.0F, 0.0F);
        vec3f.rotate(rotation);
        Vec3f[] vec3fs = new Vec3f[]{new Vec3f(-1.0F, -1.0F, 0.0F), new Vec3f(-1.0F, 1.0F, 0.0F), new Vec3f(1.0F, 1.0F, 0.0F), new Vec3f(1.0F, -1.0F, 0.0F)};
        float j = this.getSize(tickDelta);

        for(int k = 0; k < 4; ++k) {
            Vec3f vec3f2 = vec3fs[k];
            vec3f2.rotate(rotation);
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
    }


}
