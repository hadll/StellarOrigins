package haddle.stellarorigins.item;

import haddle.stellarorigins.StellarOrigins;
import haddle.stellarorigins.item.custom.vials.StarpowerConsumable;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SOItems {

    public static final Item STELLARIUM = registerItem("stellarium",
            new Item(new FabricItemSettings().group(SOItemGroups.STARSTEEL)));
    public static final Item STELLARIUM_CHARGED = registerItem("stellarium_charged",
            new Item(new FabricItemSettings().group(SOItemGroups.STARSTEEL)));
    public static final Item STARPOWER_TASER = registerItem("starpower_taser",
            new StarpowerConsumable(new FabricItemSettings().group(SOItemGroups.STARSTEEL)));


    private static Item registerItem(String name, Item item){
        return Registry.register(Registry.ITEM, new Identifier(StellarOrigins.MOD_ID, name), item);
    }

    public static void init(){
        StellarOrigins.LOGGER.debug("Registering Items");
    }
}
