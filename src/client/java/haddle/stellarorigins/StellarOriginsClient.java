package haddle.stellarorigins;

import haddle.stellarorigins.hud.StarpowerHudOverlay;
import haddle.stellarorigins.particle.custom.*;
import haddle.stellarorigins.registry.SOParticles;
import haddle.stellarorigins.renderer.FreezeFrameRenderer;
import haddle.stellarorigins.renderer.ParryShaderRenderer;
import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import ladysnake.satin.api.managed.uniform.Uniform4f;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class StellarOriginsClient implements ClientModInitializer {
	public static final String MOD_ID = "stellarorigins";

//	private boolean renderingBlit = false;
//	// literally the same as minecraft's blit, we are just checking that custom paths work
//	private final Uniform4f color = testShader.findUniform4f("ColorModulate");

	private static KeyBinding freezeKey;
	private static KeyBinding unfreezeKey;

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		ParticleFactoryRegistry.getInstance().register(SOParticles.STAR_PARTICLE, StarParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SOParticles.TRACE_PARTICLE, TraceParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SOParticles.METEOR_PARTICLE, MeteorParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SOParticles.TRAIL_PARTICLE, TrailParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SOParticles.STARFALL_PARTICLE, StarfallParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SOParticles.PARRY_PARTICLE, ParryIndicatorParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(SOParticles.PARRY_SUCCESS_PARTICLE, ParrySuccessParticle.Factory::new);

		ParryShaderRenderer.init();
		HudRenderCallback.EVENT.register(new StarpowerHudOverlay());



//		// this is the shader code for if i ever add them
//		ShaderEffectRenderCallback.EVENT.register(tickDelta -> {
//			testShader.render(tickDelta);
//		});
	}
}