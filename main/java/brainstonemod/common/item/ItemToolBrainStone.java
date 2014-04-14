package brainstonemod.common.item;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import scala.reflect.internal.Trees.If;
import brainstonemod.BrainStone;
import brainstonemod.common.helper.BSP;

import com.google.common.collect.Sets;

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
	private final static Set getBlocksEffectiveAgainstForToolsType(String type) {
		type = type.toLowerCase();

		if (type.contains("spade"))
			// Copied from ItemSpade
			return Sets.newHashSet(new Block[] { Blocks.grass, Blocks.dirt,
					Blocks.sand, Blocks.gravel, Blocks.snow_layer, Blocks.snow,
					Blocks.clay, Blocks.farmland, Blocks.soul_sand,
					Blocks.mycelium });
		else if (type.contains("pickaxe"))
			// Copied from ItemPickaxe
			return Sets.newHashSet(new Block[] { Blocks.cobblestone,
					Blocks.double_stone_slab, Blocks.stone_slab, Blocks.stone,
					Blocks.sandstone, Blocks.mossy_cobblestone,
					Blocks.iron_ore, Blocks.iron_block, Blocks.coal_ore,
					Blocks.gold_block, Blocks.gold_ore, Blocks.diamond_ore,
					Blocks.diamond_block, Blocks.ice, Blocks.netherrack,
					Blocks.lapis_ore, Blocks.lapis_block, Blocks.redstone_ore,
					Blocks.lit_redstone_ore, Blocks.rail, Blocks.detector_rail,
					Blocks.golden_rail, Blocks.activator_rail });
		else if (type.contains("axe"))
			// Copied from ItemAxe
			return Sets.newHashSet(new Block[] { Blocks.planks,
					Blocks.bookshelf, Blocks.log, Blocks.log2, Blocks.chest,
					Blocks.pumpkin, Blocks.lit_pumpkin });

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
	public ItemToolBrainStone(ToolMaterial enumtoolmaterial, String type) {
		super(getTypeId(type) + 1, enumtoolmaterial,
				getBlocksEffectiveAgainstForToolsType(type));

		typeId = getTypeId(type);
	}

	// @Override
	// public boolean func_150897_b(Block par1Block) {
	// switch (typeId) {
	// case 0:
	// return par1Block == Blocks.snow_layer ? true
	// : par1Block == Blocks.snow;
	// case 1:
	// return par1Block == Blocks.obsidian ? toolMaterial
	// .getHarvestLevel() == 3
	// : ((par1Block != Blocks.diamond_block)
	// && (par1Block != Blocks.diamond_ore) ? ((par1Block != Blocks.emerald_ore)
	// && (par1Block != Blocks.emerald_block) ? ((par1Block !=
	// Blocks.gold_block)
	// && (par1Block != Blocks.gold_ore) ? ((par1Block != Blocks.iron_block)
	// && (par1Block != Blocks.iron_ore) ? ((par1Block != Blocks.lapis_block)
	// && (par1Block != Blocks.lapis_ore) ? ((par1Block != Blocks.redstone_ore)
	// && (par1Block != Blocks.lit_redstone_ore) ? (par1Block
	// .getMaterial() == Material.rock ? true : (par1Block
	// .getMaterial() == Material.iron ? true : par1Block
	// .getMaterial() == Material.anvil)) : toolMaterial
	// .getHarvestLevel() >= 2)
	// : toolMaterial.getHarvestLevel() >= 1)
	// : toolMaterial.getHarvestLevel() >= 1)
	// : toolMaterial.getHarvestLevel() >= 2)
	// : toolMaterial.getHarvestLevel() >= 2)
	// : toolMaterial.getHarvestLevel() >= 2);
	// default:
	// return super.func_150897_b(par1Block);
	// }
	// }

	@Override
	public boolean getIsRepairable(ItemStack tool, ItemStack material) {
		return Block.getBlockFromItem(material.getItem()) == BrainStone
				.brainStone();
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		itemIcon = par1IconRegister.registerIcon("brainstonemod:"
				+ this.getUnlocalizedName().replaceFirst("item.", ""));
	}
}