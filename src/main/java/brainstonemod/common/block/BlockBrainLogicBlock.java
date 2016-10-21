package brainstonemod.common.block;

import brainstonemod.BrainStone;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BlockBrainLogicBlock extends Block {

	private boolean dropped;

	public BlockBrainLogicBlock() {
		super(Material.rock);
		blockParticleGravity = 0.0F;
		dropped = false;
	}

	public void dropItems(World world, int x, int y, int z){
		if(!world.isRemote && !dropped){
			//4 stone
			EntityItem stone = new EntityItem(world);
			stone.setEntityItemStack(new ItemStack(Blocks.stone, 4));
			//4 redstone
			EntityItem redstone = new EntityItem(world);
			redstone.setEntityItemStack(new ItemStack(Items.redstone, 4));
			//1 brainprocessor
			EntityItem processor = new EntityItem(world);
			processor.setEntityItemStack(new ItemStack(BrainStone.brainProcessor()));

			stone.setLocationAndAngles(x, y, z, 0, 0);
			redstone.setLocationAndAngles(x, y, z, 0, 0);
			processor.setLocationAndAngles(x, y, z, 0, 0);

			world.spawnEntityInWorld(stone);
			world.spawnEntityInWorld(redstone);
			world.spawnEntityInWorld(processor);
			world.setBlockToAir(x, y, z);
			dropped = true;
		}
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		dropItems(world, x, y, z);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int unknown, float px, float py, float pz) {
		dropItems(world, x, y, z);
		return false;
	}
}
