package haddle.stellarorigins.util;

import haddle.stellarorigins.registry.SOComponents;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtDouble;

public class StarpowerHelper {

    private double starpower = 0.0;
    private double starpower_change = 0.0;

    public double getStarpower(Entity entity){
        return starpower;
    }

    public void updateStarpower(Entity entity){
        if (!entity.getWorld().isClient()){
            SOComponents.STARPOWER.get(entity).change(starpower_change);
        }
        this.starpower_change = 0.0;
        this.starpower = SOComponents.STARPOWER.get(entity).getValue();
    }
    public void consumeStarpower(Entity entity, double cost){
        if (entity.getWorld().isClient()){
            return;
        }
        this.starpower_change += cost;
        this.starpower += cost;
    }
    public boolean attemptConsumeStarpower(Entity entity, double cost){
        boolean success = (starpower >= cost);
        if (success) {
            consumeStarpower(entity, -cost);
        }
        return success;
    }

    public StarpowerHelper(){

    }

}
