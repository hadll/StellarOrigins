package haddle.stellarorigins.registry;

import haddle.stellarorigins.StellarOrigins;
import haddle.stellarorigins.power.MeteorPower;
import haddle.stellarorigins.power.StarpowerPower;
import haddle.stellarorigins.util.StarpowerHelper;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.CooldownPower;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.VariableIntPower;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;

public class SOConditions {
    public static void init(){
//        register(new ConditionFactory<>(Apoli.identifier("resource"), new SerializableData()
//                .add("cost", SerializableDataTypes.INT),
//                (data, entity) -> {
//                    int resourceValue = 0;
//                    PowerHolderComponent component = PowerHolderComponent.KEY.get(entity);
//                    Power p = component.getPower((PowerType<?>)data.get("stellarorigins:starpower"));
//                    if(p instanceof VariableIntPower) {
//                        resourceValue = ((VariableIntPower)p).getValue();
//                    } else if(p instanceof CooldownPower) {
//                        resourceValue = ((CooldownPower)p).getRemainingTicks();
//                    }
//                    int cost = data.getInt("cost");
//
//                    return resourceValue;
//                }));

        register(new ConditionFactory<>(Apoli.identifier("is_using_power"), new SerializableData().add("power", ApoliDataTypes.POWER_TYPE),
            (data, entity) -> {

            PowerType<?> powerType = data.get("power");
            PowerHolderComponent component = PowerHolderComponent.KEY.get(entity);

            if (!component.hasPower(powerType)) return false;

            Power power = component.getPower(powerType);

            if (power instanceof MeteorPower meteorPower) {
                return meteorPower.isUsingPower();
            }

            return false;
        }));
        register(new ConditionFactory<>(Apoli.identifier("attempt_consume_starpower"), new SerializableData().add("cost",  SerializableDataTypes.DOUBLE),
            (data, entity) -> {
                return StellarOrigins.STARPOWER_HELPER.attemptConsumeStarpower(entity, data.get("cost"));
            }));
    }

    private static void register(ConditionFactory<Entity> conditionFactory) {
        Registry.register(ApoliRegistries.ENTITY_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }
}
