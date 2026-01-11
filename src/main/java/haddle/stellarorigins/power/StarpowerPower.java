package haddle.stellarorigins.power;

import haddle.stellarorigins.StellarOrigins;
import haddle.stellarorigins.registry.SOComponents;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.VariableIntPower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;

public class StarpowerPower extends VariableIntPower {
    public StarpowerPower(PowerType<?> type, LivingEntity entity, int startValue) {
        super(type, entity, startValue);
        setTicking(true);
    }

    private final int update_rate = 10;
    private int ticks_since_update = 0;

    private boolean exposedToNight(){
        BlockPos blockPos = entity.getVehicle() instanceof BoatEntity ? (new BlockPos(entity.getX(), (double) Math.round(entity.getY()), entity.getZ())).up() : new BlockPos(entity.getX(), (double) Math.round(entity.getY()), entity.getZ());
        long time;
        if (entity.getWorld().getRegistryKey() == World.OVERWORLD){
            time = entity.getWorld().getTimeOfDay() % 24000;
        } else {
            time = 0;
        }
        return entity.world.isSkyVisible(blockPos) && time >= 12000 && time < 24000;
    }

    @Override
    public void tick() {
        super.tick();
        ticks_since_update ++;
        if (exposedToNight()) {
            StellarOrigins.STARPOWER_HELPER.consumeStarpower(entity, 0.1);
        }
        if (ticks_since_update >= 10) {
            ticks_since_update = 0;
            StellarOrigins.STARPOWER_HELPER.updateStarpower(entity);
        }
    }
}
