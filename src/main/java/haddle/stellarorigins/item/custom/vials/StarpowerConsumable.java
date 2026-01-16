package haddle.stellarorigins.item.custom.vials;

import haddle.stellarorigins.StellarOrigins;
import haddle.stellarorigins.registry.SODamageSources;
import haddle.stellarorigins.registry.SOSounds;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StarpowerConsumable extends Item {

    public StarpowerConsumable(Settings settings) {
        super(settings.maxDamage(3));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(!world.isClient() && hand == Hand.MAIN_HAND) {
            ItemStack stack = user.getEquippedStack(EquipmentSlot.MAINHAND);
            stack.damage(1, user, (e) -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
            StellarOrigins.STARPOWER_HELPER.consumeStarpower(user, 30);
            StellarOrigins.STARPOWER_HELPER.updateStarpower(user);
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SOSounds.RECHARGE_SOUND, SoundCategory.PLAYERS, 1f, (float)StellarOrigins.STARPOWER_HELPER.getStarpower(user)/70F);
            user.damage(SODamageSources.vial(),  4);
            user.getItemCooldownManager().set(this, 15);

        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal("Restores 30 Starpower").setStyle(Style.EMPTY.withColor(MathHelper.packRgb(230, 118, 252))));
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return MathHelper.packRgb(230, 118, 252);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return true;
    }
}
