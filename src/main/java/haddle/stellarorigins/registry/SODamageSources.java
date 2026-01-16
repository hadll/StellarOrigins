package haddle.stellarorigins.registry;

import haddle.stellarorigins.StellarOrigins;
import haddle.stellarorigins.damagesources.MeteorDamageSource;
import haddle.stellarorigins.damagesources.ParryDamageSource;
import haddle.stellarorigins.damagesources.VialDamageSource;
import io.github.apace100.calio.mixin.DamageSourceAccessor;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class SODamageSources {
   public static DamageSource parry(PlayerEntity attacker) {
      return new ParryDamageSource(attacker);
   }
   public static DamageSource meteor(PlayerEntity attacker) {
      return new MeteorDamageSource(attacker);
   }
   public static DamageSource vial() {return new VialDamageSource();}

}
