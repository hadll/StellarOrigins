package haddle.stellarorigins.damagesources;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.EntityDamageSource;

public class ParryDamageSource extends EntityDamageSource {
    public ParryDamageSource(Entity attacker){
        super("parry_damage", attacker);
        this.setUnblockable();
    }
}
