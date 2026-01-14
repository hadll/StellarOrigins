package haddle.stellarorigins.item;

import haddle.stellarorigins.StellarOrigins;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class SOItemGroups {
    public static final ItemGroup STARSTEEL = FabricItemGroupBuilder.build(
            new Identifier(StellarOrigins.MOD_ID, "starsteel"), () -> new ItemStack(SOItems.STELLARIUM)
    );
}
