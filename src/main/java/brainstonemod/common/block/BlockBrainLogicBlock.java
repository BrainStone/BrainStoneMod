package brainstonemod.common.block;

import brainstonemod.BrainStone;
import brainstonemod.client.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockBrainLogicBlock extends Block {
	public static IIcon[] textures;

	public BlockBrainLogicBlock() {
		super(Material.rock);
		setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.tabBlock));//TODO: Remove from Creative before release
		blockParticleGravity = 0.0F;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		//TODO: Drop items
	}

	@Override
	public IIcon getIcon(IBlockAccess iblockaccess, int x, int y, int z, int side) {
		if (side < 2)
			return textures[2];

		return textures[1];
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		if (side >= 2)
			return textures[1 + side];

		return textures[2];
	}

	@Override
	public int getRenderType() {
		return ClientProxy.BrainLogicBlockRenderType;
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess iblockaccess, int x, int y, int z, int side) {
		return isProvidingStrongPower(iblockaccess, x, y, z, side);
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
		return true;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int unknown, float px, float py, float pz) {
		//TODO: Drop items and destroy block
		return false;
	}

	@Override
	public void registerBlockIcons(IIconRegister IconReg) {
		textures = new IIcon[] { IconReg.registerIcon("brainstonemod:brainLogicBlockPin"),
				IconReg.registerIcon("furnace_side"), IconReg.registerIcon("furnace_top"),
				IconReg.registerIcon("brainstonemod:brainLogicBlockNotConnectedA"),
				IconReg.registerIcon("brainstonemod:brainLogicBlockOffC"),
				IconReg.registerIcon("brainstonemod:brainLogicBlockOnQ"),
				IconReg.registerIcon("brainstonemod:brainLogicBlockOnB") };
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
}
