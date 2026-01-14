package haddle.stellarorigins.block;

import haddle.stellarorigins.StellarOrigins;
import haddle.stellarorigins.item.SOItemGroups;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.OreBlock;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.Registry;


public class SOBlocks {

    public static final Block STELLARIUM_ORE = registerBlock("stellarium_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(4f).requiresTool(), UniformIntProvider.create(3,8)), SOItemGroups.STARSTEEL);
    public static final Item STELLARIUM_ORE_ITEM = registerBlockItem("stellarium_ore", STELLARIUM_ORE, SOItemGroups.STARSTEEL);

    public static Block registerBlock(String name, Block block, ItemGroup tab){
        return Registry.register(Registry.BLOCK, new Identifier(StellarOrigins.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, ItemGroup tab){
        return Registry.register(Registry.ITEM, new Identifier(StellarOrigins.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings().group(tab)));
    }

    public static void init(){
        StellarOrigins.LOGGER.debug("Registering Blocks");
    }
}
