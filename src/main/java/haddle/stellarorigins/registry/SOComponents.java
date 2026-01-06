package haddle.stellarorigins.registry;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import haddle.stellarorigins.StellarOrigins;
import haddle.stellarorigins.components.PlayerStarpowerComponent;
import haddle.stellarorigins.components.StarpowerComponent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SOComponents implements EntityComponentInitializer {
    public static final ComponentKey<StarpowerComponent> STARPOWER = ComponentRegistry.getOrCreate(new Identifier(StellarOrigins.MOD_ID, "starpower_component"), StarpowerComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry entityComponentFactoryRegistry) {
        entityComponentFactoryRegistry.registerForPlayers(STARPOWER,player -> new PlayerStarpowerComponent(player), RespawnCopyStrategy.INVENTORY);
    }
}
