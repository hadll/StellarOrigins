package haddle.stellarorigins.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.OxidizableBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class StarsteelOxidisationBlock extends OxidizableBlock {
    public StarsteelOxidisationBlock(OxidationLevel oxidationLevel, Settings settings) {
        super(oxidationLevel, settings);
    }

    @Override
    public void tickDegradation(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        float f = 0.05688889F;
        if (random.nextFloat() < 0.05688889F) {
            this.tryDegrade(state, world, pos, random);
        }

    }
}
