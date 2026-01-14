package haddle.stellarorigins.world.feature;

import haddle.stellarorigins.StellarOrigins;
import haddle.stellarorigins.block.SOBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

import java.util.List;

public class SOConfiguredFeatures {
    public static final List<OreFeatureConfig.Target> END_STELLARIUM_ORES = List.of(
            OreFeatureConfig.createTarget(new BlockMatchRuleTest(Blocks.END_STONE), SOBlocks.STELLARIUM_ORE.getDefaultState())
    );

    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> STELLARIUM_ORE =
            ConfiguredFeatures.register("stellarium_ore", Feature.ORE, new OreFeatureConfig(END_STELLARIUM_ORES, 10));

    public static void init(){
        StellarOrigins.LOGGER.info(StellarOrigins.MOD_ID+": Configured Features Loaded!");
    }
}
