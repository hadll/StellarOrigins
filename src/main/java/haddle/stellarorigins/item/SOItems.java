package haddle.stellarorigins.item;

import haddle.stellarorigins.StellarOrigins;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SOItems {

    public static final Item RAW_STARSTEEL = registerItem("raw_starsteel",
            new Item(new FabricItemSettings().group(SOItemGroups.STARSTEEL)));

    private static Item registerItem(String name, Item item){
        return Registry.register(Registry.ITEM, new Identifier(StellarOrigins.MOD_ID, name), item);
    }

    public static void init(){
        StellarOrigins.LOGGER.debug("Registering Items");
    }
}
