package brainstonemod.common.block;

import brainstonemod.BrainStone;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockStablePulsatingBrainStone extends Block {
  public BlockStablePulsatingBrainStone() {
    super(Material.ROCK);

    setHardness(4.0F);
    setResistance(1.5F);
    setLightLevel(1.0F);
    setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.BUILDING_BLOCKS));
    setHarvestLevel("pickaxe", 4);

    blockParticleGravity = -0.2F;
  }

  @Override
  public boolean canEntityDestroy(
      IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
    return !((entity instanceof EntityDragon) || (entity instanceof EntityWither))
        && super.canEntityDestroy(state, world, pos, entity);
  }
}
