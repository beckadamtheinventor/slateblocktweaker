package ${mod.package}.registry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class CrucibleBlock extends Block {
    public static final IntProperty LEVEL = IntProperty.of("level", 0, 8);

    public CrucibleBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(LEVEL, 0));
    }

    private boolean canAddToCrucible(ItemStack item) {
        return item.isIn(ItemTags.LEAVES) || item.isIn(ItemTags.SAPLINGS) || item.isIn(ItemTags.FLOWERS);
    }

    private boolean canPullFromCrucible(ItemStack item) {
        return item.isOf(Items.BUCKET);
    }

    private boolean addToCrucibleChanceCheck(Random random) {
        return random.nextInt(4) > 0;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            ItemStack held = player.getStackInHand(hand);
            if (canAddToCrucible(held) && state.get(LEVEL) < 8) {
                if (addToCrucibleChanceCheck(world.getRandom())) {
                    world.setBlockState(pos, state.with(LEVEL, state.get(LEVEL) + 1));
                    held.decrement(1);
                    return ActionResult.CONSUME;
                }
            } else if (canPullFromCrucible(held)) {
                world.setBlockState(pos, state.with(LEVEL, 0));
                held.decrement(1);
                return ActionResult.CONSUME;
            }

        }
        return super.onUse(state, world, pos, player, hand, hit);
    }
}
