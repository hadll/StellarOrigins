package haddle.stellarorigins.mixin.client;


import haddle.stellarorigins.renderer.FreezeFrameRenderer;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class FreezeFrameMixin {
    @Inject(method = "render", at = @At("RETURN"))
    private void onRenderEnd(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        FreezeFrameRenderer renderer = FreezeFrameRenderer.getInstance();

        if (renderer.isFrozen()) {
            // Render the frozen frame over everything
            renderer.renderFrozenFrame();
        }
    }
}
