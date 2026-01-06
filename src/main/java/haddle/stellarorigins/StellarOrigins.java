package haddle.stellarorigins;

import haddle.stellarorigins.block.SOBlocks;
import haddle.stellarorigins.item.SOItems;
import haddle.stellarorigins.util.StarpowerHelper;
import haddle.stellarorigins.world.feature.SOConfiguredFeatures;
import haddle.stellarorigins.world.gen.SOOreGeneration;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import haddle.stellarorigins.registry.*;

public class StellarOrigins implements ModInitializer {
	public static final String MOD_ID = "stellarorigins";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static StarpowerHelper STARPOWER_HELPER = new StarpowerHelper();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		SOConfiguredFeatures.init();
		SOPowers.init();
		SOConditions.init();
		SOParticles.init();
		SOSounds.init();
        SOItems.init();
        SOBlocks.init();
		SOOreGeneration.init();
		LOGGER.info("Hello Fabric world!");
	}
}