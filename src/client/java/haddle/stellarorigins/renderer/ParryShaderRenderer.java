package haddle.stellarorigins.renderer;

import haddle.stellarorigins.power.ParryPower;
import io.github.apace100.apoli.component.PowerHolderComponent;
import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import static haddle.stellarorigins.StellarOrigins.MOD_ID;

public class ParryShaderRenderer {
    private static final ManagedShaderEffect parryShader = ShaderEffectManager.getInstance().manage(new Identifier(MOD_ID, "shaders/post/parry.json"));

    public static void init() {
        ShaderEffectRenderCallback.EVENT.register(tickDelta -> {
            var player = MinecraftClient.getInstance().player;
            if (player == null) return;

            ParryPower power = PowerHolderComponent.getPowers(player, ParryPower.class)
                    .stream()
                    .findFirst()
                    .orElse(null);

            if (power == null) return;

            if (power.shaderActive > 0) {
                parryShader.render(tickDelta);
            }
        });
    }
}
