package brainstonemod.common.item;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import brainstonemod.BrainStone;
import brainstonemod.common.helper.BSP;

public class ItemToolBrainStone extends ItemTool {
	/**
	 * Determines which blocksEffectiveAgainst will be chosen.
	 * 
	 * @param type
	 *            What tool is it. Can either be "spade", "pickaxe", or "axe"
	 * @return A blocksEffectiveAgainst depending on the tool type
	 * @throws If
	 *             the type was not recognized a "IllegalArgumentException" will
	 *             be thrown. It includes the type that was given and a
	 *             possibilities. This needs to be because otherwise the game
	 *             would crash in unexpected locations (like trying to destroy a
	 *             block with this tool)
	 */
	private final static Block[] getBlocksEffectiveAgainstForToolsType(
			String type) {
		type = type.toLowerCase();

		if (type.contains("spade"))
			return ItemSpade.blocksEffectiveAgainst;
		else if (type.contains("pickaxe"))
			return ItemPickaxe.blocksEffectiveAgainst;
		else if (type.contains("axe"))
			return ItemAxe.blocksEffectiveAgainst;

		BSP.throwIllegalArgumentException("The tool type \""
				+ type
				+ "\" was not regonized!\nPlease choose from \"spade\", \"pickaxe\" or \"axe\"!");

		return null;
	}

	/**
	 * Determines which toolId the tool type is
	 * 
	 * @param type
	 *            What tool is it. Can either be "spade", "pickaxe", or "axe"
	 * @return ToolId based on type ("spade" => 0, "pickaxe" => 1, "axe" => 2)
	 * @throws If
	 *             the type was not recognized a "IllegalArgumentException" will
	 *             be thrown. It includes the type that was given and a
	 *             possibilities. This needs to be because otherwise the game
	 *             would crash in unexpected locations (like trying to destroy a
	 *             block with this tool)
	 */
	private final static int getTypeId(String type) {
		final String tmpType = type.toLowerCase();

		if (tmpType.contains("spade"))
			return 0;
		else if (tmpType.contains("pickaxe"))
			return 1;
		else if (tmpType.contains("axe"))
			return 2;

		BSP.throwIllegalArgumentException("The tool type \""
				+ type
				+ "\" was not regonized!\nPlease choose from \"spade\", \"pickaxe\" or \"axe\"!");

		return -1;
	}

	private final int typeId;

	/**
	 * Creates a tool for the BrainStoneMod (Uses several hooks special designed
	 * for this mod, so use this!)
	 * 
	 * @param i
	 *            internal Id
	 * @param enumtoolmaterial
	 *            Tool material
	 * @param type
	 *            What tool is it. Can either be "spade", "pickaxe", or "axe"
	 */
	public ItemToolBrainStone(int i, EnumToolMaterial enumtoolmaterial,
			String type) {
		super(BrainStone.getId(BrainStone.startItemId + i), 0,
				enumtoolmaterial, getBlocksEffectiveAgainstForToolsType(type));

		typeId = getTypeId(type);
	}

	@Override
	public boolean canHarvestBlock(Block par1Block) {
		switch (typeId) {
		case 0:
			return par1Block == Block.snow ? true
					: par1Block == Block.blockSnow;
		case 1:
			return par1Block == Block.obsidian ? toolMaterial.getHarvestLevel() == 3
					: ((par1Block != Block.blockDiamond)
							&& (par1Block != Block.oreDiamond) ? ((par1Block != Block.oreEmerald)
							&& (par1Block != Block.blockEmerald) ? ((par1Block != Block.blockGold)
							&& (par1Block != Block.oreGold) ? ((par1Block != Block.blockIron)
							&& (par1Block != Block.oreIron) ? ((par1Block != Block.blockLapis)
							&& (par1Block != Block.oreLapis) ? ((par1Block != Block.oreRedstone)
							&& (par1Block != Block.oreRedstoneGlowing) ? (par1Block.blockMaterial == Material.rock ? true
							: (par1Block.blockMaterial == Material.iron ? true
									: par1Block.blockMaterial == Material.anvil))
							: toolMaterial.getHarvestLevel() >= 2)
							: toolMaterial.getHarvestLevel() >= 1)
							: toolMaterial.getHarvestLevel() >= 1)
							: toolMaterial.getHarvestLevel() >= 2)
							: toolMaterial.getHarvestLevel() >= 2)
							: toolMaterial.getHarvestLevel() >= 2);
		default:
			return super.canHarvestBlock(par1Block);
		}
	}

	@Override
	public boolean getIsRepairable(ItemStack tool, ItemStack material) {
		return material.itemID == BrainStone.brainStone().blockID;
	}

	@Override
	public float getStrVsBlock(ItemStack par1ItemStack, Block par2Block) {
		switch (typeId) {
		case 1:
			return (par2Block != null)
					&& ((par2Block.blockMaterial == Material.iron)
							|| (par2Block.blockMaterial == Material.anvil) || (par2Block.blockMaterial == Material.rock)) ? efficiencyOnProperMaterial
					: super.getStrVsBlock(par1ItemStack, par2Block);
		case 2:
			return (par2Block != null)
					&& ((par2Block.blockMaterial == Material.wood)
							|| (par2Block.blockMaterial == Material.plants) || (par2Block.blockMaterial == Material.vine)) ? efficiencyOnProperMaterial
					: super.getStrVsBlock(par1ItemStack, par2Block);
		default:
			return super.getStrVsBlock(par1ItemStack, par2Block);
		}
	}

	@Override
	public void registerIcons(IconRegister par1IconRegister) {
		itemIcon = par1IconRegister.registerIcon("brainstonemod:"
				+ this.getUnlocalizedName().replaceFirst("item.", ""));
	}
}