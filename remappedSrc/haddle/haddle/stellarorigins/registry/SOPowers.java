package haddle.stellarorigins.registry;

import haddle.stellarorigins.StellarOrigins;
import haddle.stellarorigins.power.LaunchPower;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Active;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.util.HudRender;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.util.


public class SOPowers {
    public static final PowerFactory<Power> LAUNCH = new PowerFactory<>(new Identifier(StellarOrigins.MOD_ID, "launch"), new SerializableData().add("key", ApoliDataTypes.BACKWARDS_COMPATIBLE_KEY, new Active.Key()), data -> (type, entity) -> {
        LaunchPower power = new LaunchPower(type, entity, 20, HudRender.DONT_RENDER, null);
        power.setKey((Active.Key) data.get("key"));
        return power;
    }).allowCondition();
}

