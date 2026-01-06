package haddle.stellarorigins.damagesources;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.EntityDamageSource;

public class MeteorDamageSource extends EntityDamageSource {
    public MeteorDamageSource(Entity attacker){
        super("meteor_damage", attacker);
    }
}
