package haddle.stellarorigins.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.render.GameRenderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class FreezeFrameRenderer {
    private static FreezeFrameRenderer INSTANCE;
    private Framebuffer freezeFramebuffer;
    private boolean isFrozen = false;
    private boolean frameCaptured = false;

    private FreezeFrameRenderer() {
        MinecraftClient client = MinecraftClient.getInstance();
        this.freezeFramebuffer = new SimpleFramebuffer(
                client.getWindow().getFramebufferWidth(),
                client.getWindow().getFramebufferHeight(),
                true,
                MinecraftClient.IS_SYSTEM_MAC
        );
        this.freezeFramebuffer.setClearColor(0, 0, 0, 0);
    }

    public static FreezeFrameRenderer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FreezeFrameRenderer();
        }
        return INSTANCE;
    }

    public void captureFrame() {
        if (!frameCaptured && !isFrozen) {
            MinecraftClient client = MinecraftClient.getInstance();
            Framebuffer mainFramebuffer = client.getFramebuffer();

            // Ensure framebuffer is correct size
            int width = client.getWindow().getFramebufferWidth();
            int height = client.getWindow().getFramebufferHeight();

            if (freezeFramebuffer.textureWidth != width || freezeFramebuffer.textureHeight != height) {
                freezeFramebuffer.resize(width, height, MinecraftClient.IS_SYSTEM_MAC);
            }

            // Copy current frame to our freeze framebuffer
            mainFramebuffer.beginRead();
            freezeFramebuffer.beginWrite(false);

            GlStateManager._colorMask(true, true, true, true);
            GlStateManager._disableDepthTest();
            GlStateManager._depthMask(false);
            GlStateManager._viewport(0, 0, width, height);

            // Blit the main framebuffer to freeze framebuffer
            GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, mainFramebuffer.fbo);
            GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, freezeFramebuffer.fbo);
            GL30.glBlitFramebuffer(
                    0, 0, width, height,
                    0, 0, width, height,
                    GL11.GL_COLOR_BUFFER_BIT,
                    GL11.GL_NEAREST
            );

            mainFramebuffer.endRead();
            freezeFramebuffer.endWrite();

            frameCaptured = true;
            isFrozen = true;
        }
    }

    public void renderFrozenFrame() {
        if (isFrozen && frameCaptured) {
            MinecraftClient client = MinecraftClient.getInstance();
            int width = client.getWindow().getFramebufferWidth();
            int height = client.getWindow().getFramebufferHeight();

            RenderSystem.enableBlend();
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

            // Draw the frozen frame texture
            freezeFramebuffer.draw(width, height);

            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    public void unfreeze() {
        isFrozen = false;
        frameCaptured = false;
    }

    public boolean isFrozen() {
        return isFrozen;
    }

    public void resize(int width, int height) {
        if (freezeFramebuffer != null) {
            freezeFramebuffer.resize(width, height, MinecraftClient.IS_SYSTEM_MAC);
        }
        // Reset capture when resizing
        frameCaptured = false;
    }

    public void cleanup() {
        if (freezeFramebuffer != null) {
            freezeFramebuffer.delete();
        }
    }
}
