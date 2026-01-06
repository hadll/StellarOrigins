package haddle.stellarorigins.world.gen;

import haddle.stellarorigins.world.feature.SOPlacedFeatures;
import net.fabricmc.fabric.api.biome.v1.BiomeModification;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.gen.GenerationStep;

public class SOOreGeneration {
    public static void init() {
        BiomeModifications.addFeature(BiomeSelectors.foundInTheEnd(),
                GenerationStep.Feature.UNDERGROUND_ORES, SOPlacedFeatures.STARSTEEL_ORE_PLACED.getKey().get());
    }
}
