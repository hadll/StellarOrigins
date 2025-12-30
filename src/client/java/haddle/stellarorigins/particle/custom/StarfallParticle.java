package haddle.stellarorigins.particle.custom;

import haddle.stellarorigins.particle.SpriteRotationParticle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.Quaternion;

public class ShockwaveParticle extends SpriteRotationParticle {
    private double scaleMult = 3;

    protected ShockwaveParticle(ClientWorld level, double xCoord, double yCoord, double zCoord,
                           SpriteProvider spriteSet, double xd, double yd, double zd) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd, Quaternion.IDENTITY);

        this.velocityMultiplier = 0f;
        this.x = xd;
        this.y = yd;
        this.z = zd;
        this.scale *= 2F;
        this.maxAge = 80;
        this.setSpriteForAge(spriteSet);

        this.red = 1f;
        this.green = 0.592f;
        this.blue = 0.953f;

        this.velocityX = xd + (Math.random() * (double)2.0F - (double)1.0F) * (double)0.1F;
        this.velocityY = yd + (Math.random() * (double)2.0F - (double)1.0F) * (double)0.1F;
        this.velocityZ = zd + (Math.random() * (double)2.0F - (double)1.0F) * (double)0.1F;
        double d = (Math.random() + Math.random() + (double)1.0F) * (double)0.05F;
        double e = Math.sqrt(this.velocityX * this.velocityX + this.velocityY * this.velocityY + this.velocityZ * this.velocityZ);
        this.velocityX = this.velocityX * d;
        this.velocityY = this.velocityY * d;
        this.velocityZ = this.velocityZ * d;
    }


    @Override
    public void tick() {
        fadeOut();
        super.tick();
    }

    private void fadeOut() {
        this.alpha = (-(1/(float)maxAge) * age + 1);
        this.scale = (float) (Math.log(age) / Math.log(3) * scaleMult) ;
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
            return new StarParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }

    @Override
    protected int getBrightness(float tint) {
        return 255;
    }
}
