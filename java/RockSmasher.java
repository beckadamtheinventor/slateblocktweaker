package com.beckati.slateblocktweaker.registry;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RockSmasher extends Item {

    public RockSmasher(Settings settings) {
        super(settings);
    }

    private final BlockState[] recipesources = {
            Blocks.COBBLESTONE.getDefaultState(),
            Blocks.STONE.getDefaultState(),
            Blocks.COBBLED_DEEPSLATE.getDefaultState(),
            Blocks.GRAVEL.getDefaultState(),
    };
    private final ItemStack[] reciperesults = {
            Items.GRAVEL.getDefaultStack(),
            Items.GRAVEL.getDefaultStack(),
            Items.GRAVEL.getDefaultStack(),
            Items.SAND.getDefaultStack(),
    };

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (!world.isClient()) {
            BlockState smashing = world.getBlockState(context.getBlockPos());
            for (int i = 0; i < recipesources.length; i++) {
                if (smashing.equals(recipesources[i])) {
                    this.smash(context, reciperesults[i]);
                    break;
                }
            }
        }

        return super.useOnBlock(context);
    }

    private void smash(ItemUsageContext context, ItemStack result) {
        ItemStack tool = context.getStack();
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();

        tool.damage(1, context.getPlayer(),
                (player) -> player.sendToolBreakStatus(player.getActiveHand()));
        world.breakBlock(pos, false);
        world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), result));
    }

}
