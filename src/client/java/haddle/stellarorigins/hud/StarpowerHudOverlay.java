package haddle.stellarorigins.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import haddle.stellarorigins.StellarOrigins;
import haddle.stellarorigins.power.ParryPower;
import haddle.stellarorigins.power.StarpowerPower;
import haddle.stellarorigins.util.StarpowerHelper;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class StarpowerHudOverlay implements HudRenderCallback {

    private static final Identifier BASE_ELEMENT = new Identifier(StellarOrigins.MOD_ID, "textures/hud/starpowerbar.png");
    List<Identifier> WAVE_TOPS = new ArrayList<Identifier>() {
        {
            add(new Identifier(StellarOrigins.MOD_ID, "textures/hud/starpowerwave1.png"));
            add(new Identifier(StellarOrigins.MOD_ID, "textures/hud/starpowerwave2.png"));
            add(new Identifier(StellarOrigins.MOD_ID, "textures/hud/starpowerwave3.png"));
            add(new Identifier(StellarOrigins.MOD_ID, "textures/hud/starpowerwave4.png"));
        }
    };

    private final double maxStarpower = 100.0;

    // this shit needs replacing with a mixin but it works for now
    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta){

        PlayerEntity player = MinecraftClient.getInstance().player;

        StarpowerPower power = PowerHolderComponent.getPowers(player, StarpowerPower.class)
                .stream()
                .findFirst()
                .orElse(null);

        int x = 0;
        int y = 0;
        double starpower = 0;
        int frame = 0;

        starpower = StellarOrigins.STARPOWER_HELPER.getStarpower(player);

        if (power == null && starpower <= 1) return;

        MinecraftClient client = MinecraftClient.getInstance();

        if (client != null){
            int height = client.getWindow().getScaledHeight();

            x = 0;
            y = height;

            long time = client.world.getTime()%20;
            frame = (int) (time/5);
        }


        double starpower_frac = starpower/maxStarpower;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.75F);
        DrawableHelper.fill(matrixStack,x, (int) (y-starpower_frac*64)+3, 16, y-1, 0xC0411951);
        RenderSystem.setShaderTexture(0, WAVE_TOPS.get(frame));
        DrawableHelper.drawTexture(matrixStack,x, (int) (y-starpower_frac*64),0,0,16,3,16,3);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BASE_ELEMENT);
        DrawableHelper.drawTexture(matrixStack, x,y-65,0,0,16,64,16,64);

    }
}
