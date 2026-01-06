package haddle.stellarorigins.particle.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

import static java.lang.Math.sqrt;

public class TraceParticle extends SpriteBillboardParticle {


    protected TraceParticle(ClientWorld level, double xCoord, double yCoord, double zCoord,
                            SpriteProvider spriteSet, double xd, double yd, double zd) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);

        this.velocityMultiplier = 0.99f;
        this.x = xd;
        this.y = yd;
        this.z = zd;
        this.scale *= 2F;
        this.maxAge = 40;
        this.setSpriteForAge(spriteSet);

        this.red = 1f;
        this.green = 0.592f;
        this.blue = 0.953f;

        this.velocityX = xd;
        this.velocityY = yd;
        this.velocityZ = zd;
    }


    @Override
    public void tick() {
        fadeOut();
        super.tick();
    }

    private void fadeOut() {
        this.alpha = (-(1/(float)maxAge) * age + 1);
//        this.scale = (float)sqrt(this.velocityX*this.velocityX+this.velocityY*this.velocityY+this.velocityZ*this.velocityZ);
        this.scale = ((1/(float)maxAge) * age);
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
            return new TraceParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }

    @Override
    protected int getBrightness(float tint) {
        return 255;
    }
}
