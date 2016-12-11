package brainstonemod.common.item;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import brainstonemod.BrainStone;
import brainstonemod.common.helper.BSP;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import scala.reflect.internal.Trees.If;

public class ItemToolBrainStone extends ItemTool {
	private final int typeId;
	private final String toolClass;

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
	private final static Set<Block> getBlocksEffectiveAgainstForToolsType(String type) {
		type = type.toLowerCase();

		if (type.contains("spade")) {
			try {
				return ItemSpade.EFFECTIVE_ON;
			} catch (IllegalAccessError e) {
				return ReflectionHelper.getPrivateValue(ItemSpade.class, null, "EFFECTIVE_ON", "field_150916_c",
						"field_150915_c", "field_150917_c");
			}
		} else if (type.contains("pickaxe")) {
			try {
				return ItemPickaxe.EFFECTIVE_ON;
			} catch (IllegalAccessError e) {
				return ReflectionHelper.getPrivateValue(ItemPickaxe.class, null, "EFFECTIVE_ON", "field_150916_c",
						"field_150915_c", "field_150917_c");
			}
		} else if (type.contains("axe")) {
			try {
				return ItemAxe.EFFECTIVE_ON;
			} catch (IllegalAccessError e) {
				return ReflectionHelper.getPrivateValue(ItemAxe.class, null, "EFFECTIVE_ON", "field_150916_c",
						"field_150915_c", "field_150917_c");
			}
		}

		BSP.throwIllegalArgumentException("The tool type \"" + type
				+ "\" was not regonized!\nPlease choose from \"spade\", \"pickaxe\" or \"axe\"!");

		return null;
	}

	/**
	 * Determines which toolId the tool type is
	 *
	 * @param type
	 *            What tool is it. Can either be "spade", "pickaxe", or "axe"
	 * @return ToolId based on type ("spade" => 0, "pickaxe" => 1, "axe" => 2)
	 * @throws IllegalArgumentException
	 *             If the type was not recognized a "IllegalArgumentException"
	 *             will be thrown. It includes the type that was given and a
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

		BSP.throwIllegalArgumentException("The tool type \"" + type
				+ "\" was not regonized!\nPlease choose from \"spade\", \"pickaxe\" or \"axe\"!");

		return -1;
	}

	/**
	 * Creates a tool for the BrainStoneMod (Uses several hooks special designed
	 * for this mod, so use this!)
	 *
	 * @param enumtoolmaterial
	 *            Tool material
	 * @param type
	 *            What tool is it. Can either be "spade", "pickaxe", or "axe"
	 */
	public ItemToolBrainStone(ToolMaterial enumtoolmaterial, String type) {
		super(enumtoolmaterial, getBlocksEffectiveAgainstForToolsType(type));

		typeId = getTypeId(type);
		toolClass = type;

		setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.TOOLS));
	}

	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state) {
		Material material = state.getMaterial();
		switch (typeId) {
		case 1:
			return (material != Material.IRON) && (material != Material.ANVIL) && (material != Material.ROCK)
					? super.getStrVsBlock(stack, state)
					: efficiencyOnProperMaterial;
		case 2:
			return (material != Material.WOOD) && (material != Material.PLANTS) && (material != Material.VINE)
					? super.getStrVsBlock(stack, state)
					: efficiencyOnProperMaterial;
		default:
			return super.getStrVsBlock(stack, state);
		}
	}

	@Override
	public boolean canHarvestBlock(IBlockState blockIn) {
		Block block = blockIn.getBlock();
		switch (typeId) {
		case 0:
			return block == Blocks.SNOW_LAYER ? true : block == Blocks.SNOW;
		case 1:
			if (block == Blocks.OBSIDIAN)
				return toolMaterial.getHarvestLevel() == 3;
			else if ((block != Blocks.DIAMOND_BLOCK) && (block != Blocks.DIAMOND_ORE)) {
				if ((block != Blocks.EMERALD_ORE) && (block != Blocks.EMERALD_BLOCK)) {
					if ((block != Blocks.GOLD_BLOCK) && (block != Blocks.GOLD_ORE)) {
						if ((block != Blocks.IRON_BLOCK) && (block != Blocks.IRON_ORE)) {
							if ((block != Blocks.LAPIS_BLOCK) && (block != Blocks.LAPIS_ORE)) {
								if ((block != Blocks.REDSTONE_ORE) && (block != Blocks.LIT_REDSTONE_ORE)) {
									Material material = blockIn.getMaterial();
									return material == Material.ROCK ? true
											: (material == Material.IRON ? true : material == Material.ANVIL);
								} else
									return toolMaterial.getHarvestLevel() >= 2;
							} else
								return toolMaterial.getHarvestLevel() >= 1;
						} else
							return toolMaterial.getHarvestLevel() >= 1;
					} else
						return toolMaterial.getHarvestLevel() >= 2;
				} else
					return toolMaterial.getHarvestLevel() >= 2;
			} else
				return toolMaterial.getHarvestLevel() >= 2;
		default:
			return super.canHarvestBlock(blockIn);
		}
	}

	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass) {
		final int level = super.getHarvestLevel(stack, toolClass);
		if ((level == -1) && (toolClass != null) && toolClass.equals(this.toolClass))
			return toolMaterial.getHarvestLevel();
		else
			return level;
	}

	@Override
	public Set<String> getToolClasses(ItemStack stack) {
		return toolClass != null ? ImmutableSet.of(toolClass) : super.getToolClasses(stack);
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (typeId == 0) {
			if (!playerIn.canPlayerEdit(pos.offset(facing), facing, stack))
				return EnumActionResult.FAIL;
			else {
				IBlockState iblockstate = worldIn.getBlockState(pos);
				Block block = iblockstate.getBlock();

				if ((facing != EnumFacing.DOWN) && (worldIn.getBlockState(pos.up()).getMaterial() == Material.AIR)
						&& (block == Blocks.GRASS)) {
					IBlockState iblockstate1 = Blocks.GRASS_PATH.getDefaultState();
					worldIn.playSound(playerIn, pos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);

					if (!worldIn.isRemote) {
						worldIn.setBlockState(pos, iblockstate1, 11);
						stack.damageItem(1, playerIn);
					}

					return EnumActionResult.SUCCESS;
				} else
					return EnumActionResult.PASS;
			}
		}
		return EnumActionResult.PASS;
	}
}
