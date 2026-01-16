package haddle.stellarorigins.block.custom.chargable;

import haddle.stellarorigins.block.SOBlocks;
import haddle.stellarorigins.block.StarpowerChargableBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class StellariumBlock extends StarpowerChargableBlock {

    public static final int maxCharge = 20;
    public static IntProperty CHARGE;

    public StellariumBlock(Settings settings) {
        super(settings);
    }

    public IntProperty getChargeProperty() {
        return CHARGE;
    }

    protected int getCharge(BlockState state) {
        return (Integer)state.get(this.getChargeProperty());
    }

    public BlockState withCharge(int charge) {
        return (BlockState)this.getDefaultState().with(this.getChargeProperty(), charge);
    }

    private boolean exposedToNight(ServerWorld world, BlockPos pos){
        return world.getRegistryKey() == World.OVERWORLD && ((world.getTimeOfDay() % 24000) >= 12000 && world.isSkyVisible(pos.up(1)));
    }


    @Override
    protected void chargeTick(BlockState state, ServerWorld world, BlockPos pos, Random random){
        boolean exposed = exposedToNight(world, pos);
        if (exposed){
            int i = this.getCharge(state) + 1;
            if (i > maxCharge) {
                System.out.println("Charged");
                world.setBlockState(pos, SOBlocks.STELLARIUM_CHARGED_BLOCK.getDefaultState(), 2);
            }else{
                world.setBlockState(pos, this.withCharge(i), 2);
            }
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{CHARGE});
    }

    static {
        CHARGE = IntProperty.of("charge", 0, maxCharge);
    }
}
