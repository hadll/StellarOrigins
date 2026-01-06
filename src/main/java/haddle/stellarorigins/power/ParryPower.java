package haddle.stellarorigins.power;

import haddle.stellarorigins.StellarOrigins;
import haddle.stellarorigins.registry.SODamageSources;
import haddle.stellarorigins.registry.SOParticles;
import haddle.stellarorigins.registry.SOSounds;
import haddle.stellarorigins.util.ParticleSpawnHelper;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.ModifyDamageTakenPower;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.power.factory.action.EntityActions;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.power.factory.condition.DamageConditions;
import io.github.apace100.apoli.util.MiscUtil;
import io.github.apace100.apoli.util.modifier.Modifier;
import io.github.apace100.calio.data.SerializableData;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static haddle.stellarorigins.StellarOrigins.MOD_ID;

public class ParryPower extends ModifyDamageTakenPower {
    private final Predicate<Pair<DamageSource, Float>> condition;
    private final Predicate<Pair<Entity, Entity>> biEntityCondition;

    private Consumer<Entity> attackerAction;
    private Consumer<Entity> selfAction;
    private Consumer<Pair<Entity, Entity>> biEntityAction;

    private Predicate<Entity> applyArmorCondition;
    private Predicate<Entity> damageArmorCondition;

    public int shaderActive = -1;

    private Random random = Random.create();

    public ParryPower(PowerType<?> type, LivingEntity entity, Predicate<Pair<DamageSource, Float>> dmg, Predicate<Pair<Entity, Entity>> biEntityCondition) {
        super(type, entity, dmg, biEntityCondition);
        this.condition = dmg;
        this.biEntityCondition = biEntityCondition;
        setTicking(true);
    }

    @Override
    public void tick() {
        super.tick();
        setShaderActive(shaderActive-1);
    }

    public void setApplyArmorCondition(Predicate<Entity> applyArmorCondition) {
        this.applyArmorCondition = applyArmorCondition;
    }

    public void setDamageArmorCondition(Predicate<Entity> damageArmorCondition) {
        this.damageArmorCondition = damageArmorCondition;
    }

    public boolean modifiesArmorApplicance() {
        return this.applyArmorCondition != null;
    }

    public boolean shouldApplyArmor() {
        return applyArmorCondition != null && applyArmorCondition.test(entity);
    }

    public boolean modifiesArmorDamaging() {
        return this.damageArmorCondition != null;
    }

    public boolean shouldDamageArmor() {
        return damageArmorCondition != null && damageArmorCondition.test(entity);
    }

    public boolean doesApply(DamageSource source, float damageAmount) {
        boolean noBiCon = biEntityCondition == null;
        boolean biCon = false;
        PlayerEntity player = (PlayerEntity)entity;
        if (source.getAttacker() != null){
            biCon = biEntityCondition.test(new Pair(source.getAttacker(), entity));
        }
        if (biCon && StellarOrigins.STARPOWER_HELPER.attemptConsumeStarpower(entity, 0.01)){
            //parry

            if (source.getAttacker() != null){
                source.getAttacker().damage(SODamageSources.parry(player),damageAmount);
                source.getAttacker().playSound(SOSounds.PARRY_SOUND, 1f, (float)(1-random.nextBetween(-20,20)*0.01));
            }
            setShaderActive(3);


            // shit that makes the particles on parry, fix later bcus this shit needs to be done like yesterday
//            Vec3d directionToAttacker = source.getAttacker().getPos().subtract(player.getEyePos()).normalize();
//
//            Vec3d playerMidPos = player.getPos().add(player.getEyePos()).multiply(0.5);
//
//            Vec3d particleOrigin = playerMidPos.add(directionToAttacker);
//
//            List<Vec3d> offsets = ParticleSpawnHelper.CalculateOffset(player, directionToAttacker, 16, true);
//            for (Vec3d offset:offsets){
//                offset = offset.multiply(5);
//                player.getWorld().addParticle(SOParticles.STAR_PARTICLE, particleOrigin.getX(), particleOrigin.getY(), particleOrigin.getZ(),0, 0, 0 );
//            }

            return false;
        }
        return true;
    }

    public void setAttackerAction(Consumer<Entity> attackerAction) {
        this.attackerAction = attackerAction;
    }

    public void setSelfAction(Consumer<Entity> selfAction) {
        this.selfAction = selfAction;
    }

    public void setBiEntityAction(Consumer<Pair<Entity, Entity>> biEntityAction) {
        this.biEntityAction = biEntityAction;
    }

    public void executeActions(Entity attacker) {
        if(selfAction != null) {
            selfAction.accept(entity);
        }
        if(attackerAction != null) {
            attackerAction.accept(attacker);
        }
        if(biEntityAction != null) {
            biEntityAction.accept(new Pair<>(attacker, entity));
        }
    }

    public static PowerFactory createFactory() {
        return new PowerFactory<>(Apoli.identifier("parry"),
                new SerializableData()
                        .add("damage_condition", ApoliDataTypes.DAMAGE_CONDITION, null)
                        .add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION, null)
                        .add("modifier", Modifier.DATA_TYPE, null)
                        .add("modifiers", Modifier.LIST_TYPE, null)
                        .add("self_action", ApoliDataTypes.ENTITY_ACTION, null)
                        .add("attacker_action", ApoliDataTypes.ENTITY_ACTION, null)
                        .add("bientity_action", ApoliDataTypes.BIENTITY_ACTION, null)
                        .add("apply_armor_condition", ApoliDataTypes.ENTITY_CONDITION, null)
                        .add("damage_armor_condition", ApoliDataTypes.ENTITY_CONDITION, null),
                data ->
                        (type, player) -> {
                            ParryPower power = new ParryPower(type, player,
                                    data.isPresent("damage_condition") ? data.get("damage_condition") : dmg -> true,
                                    data.get("bientity_condition"));
                            data.ifPresent("modifier", power::addModifier);
                            data.<List<Modifier>>ifPresent("modifiers",
                                    mods -> mods.forEach(power::addModifier)
                            );
                            if(data.isPresent("bientity_action")) {
                                power.setBiEntityAction(data.get("bientity_action"));
                            }
                            if(data.isPresent("self_action")) {
                                power.setSelfAction(data.get("self_action"));
                            }
                            if(data.isPresent("attacker_action")) {
                                power.setAttackerAction(data.get("attacker_action"));
                            }
                            data.ifPresent("apply_armor_condition", power::setApplyArmorCondition);
                            data.ifPresent("damage_armor_condition", power::setDamageArmorCondition);
                            return power;
                        }).allowCondition();
    }
    @Override
    public NbtElement toTag() {
        NbtCompound tag = new NbtCompound();
        tag.putInt("shaderActive", shaderActive);
        return tag;
    }

    @Override
    public void fromTag(NbtElement tag) {
        if (tag instanceof NbtCompound compound) {
            this.shaderActive = compound.getInt("shaderActive");
        }
    }
    public void setShaderActive(int active) {
        if (this.shaderActive != active && active >= -1) {
            this.shaderActive = active;
            PowerHolderComponent.syncPower(this.entity, this.getType());
        }
    }
}
