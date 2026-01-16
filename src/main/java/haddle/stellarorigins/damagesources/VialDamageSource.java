package haddle.stellarorigins.damagesources;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;

public class VialDamageSource extends DamageSource {
    public VialDamageSource(){
        super("vial_damage");
        this.setUnblockable();
        this.setBypassesArmor();
    }
}
