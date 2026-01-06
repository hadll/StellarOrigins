package haddle.stellarorigins.renderer;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.util.math.Vec3d;

public class LineRenderUtil {
    public static void drawLine(
            VertexConsumer consumer,
            Camera camera,
            Vec3d start,
            Vec3d end,
            float width,
            float r, float g, float b, float a,
            int light
    ) {
        Vec3d cam = camera.getPos();

        float x1 = (float)(start.x - cam.x);
        float y1 = (float)(start.y - cam.y);
        float z1 = (float)(start.z - cam.z);

        float x2 = (float)(end.x - cam.x);
        float y2 = (float)(end.y - cam.y);
        float z2 = (float)(end.z - cam.z);

        // Simple UVs (full sprite)
        float u1 = 0f, v1 = 0f;
        float u2 = 1f, v2 = 1f;

        float nx = 0f;
        float ny = 1f;
        float nz = 0f;

        consumer.vertex(x1, y1, z1)
                .color(r, g, b, a)
                .texture(u1, v1)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(nx, ny, nz)
                .next();

        consumer.vertex(x2, y2, z2)
                .color(r, g, b, a)
                .texture(u2, v2)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(nx, ny, nz)
                .next();
    }

}
