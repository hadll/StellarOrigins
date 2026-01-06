package haddle.stellarorigins.particle;

import haddle.stellarorigins.renderer.LineRenderUtil;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public abstract class BillboardTrailParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;
    private final List<Vec3d> previousPositions = new ArrayList<>();
    private static final int MAX_TRAIL_LENGTH = 10;

    public BillboardTrailParticle(ClientWorld world, double x, double y, double z,
                         double vx, double vy, double vz, SpriteProvider spriteProvider) {
        super(world, x, y, z, vx, vy, vz);
        this.spriteProvider = spriteProvider;
        this.maxAge = 40;
        this.setSpriteForAge(spriteProvider);
    }

    @Override
    public void tick() {
        super.tick();

        // Store positions for trail
        previousPositions.add(new Vec3d(this.x, this.y, this.z));
        if (previousPositions.size() > MAX_TRAIL_LENGTH) {
            previousPositions.remove(0);
        }

        // Optional: spawn small particle trail behind
        if (this.world.isClient) {
            world.addParticle(ParticleTypes.SMOKE, this.x, this.y, this.z, 0, 0, 0);
        }
    }

    @Override
    public void buildGeometry(VertexConsumer consumer, Camera camera, float tickDelta) {
        int light = this.getBrightness(tickDelta);

        for (int i = 1; i < previousPositions.size(); i++) {
            Vec3d a = previousPositions.get(i - 1);
            Vec3d b = previousPositions.get(i);

            float alpha = i / (float) previousPositions.size();

            LineRenderUtil.drawLine(
                    consumer,
                    camera,
                    a, b,
                    0.05f,
                    1f, 0.6f, 1f, alpha,
                    light
            );
        }
    }

}
