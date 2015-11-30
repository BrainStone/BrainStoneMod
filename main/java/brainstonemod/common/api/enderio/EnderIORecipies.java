package brainstonemod.common.api.enderio;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import brainstonemod.BrainStone;
import brainstonemod.common.helper.BSP;
import cpw.mods.fml.common.Optional;
import crazypants.enderio.item.darksteel.DarkSteelRecipeManager;
import crazypants.enderio.machine.alloy.AlloyRecipeManager;
import crazypants.enderio.machine.crusher.CrusherRecipeManager;
import crazypants.enderio.machine.recipe.BasicManyToOneRecipe;
import crazypants.enderio.machine.recipe.Recipe;
import crazypants.enderio.machine.recipe.RecipeBonusType;
import crazypants.enderio.machine.recipe.RecipeInput;
import crazypants.enderio.machine.recipe.RecipeOutput;

public class EnderIORecipies {
	@Optional.Method(modid = "EnderIO")
	public static void registerEnderIORecipies() {
		BSP.info("EnderIO detected! Enabling recipes and armor upgrade!");
		
		// SAG Mill

		// BrainStoneOre => BrainStoneDust, BrainStoneDust 50%
		addSAGMillRecipe(BrainStone.brainStoneOre(), 4000,
				BrainStone.brainStoneDust(), new RecipeOutput(new ItemStack(
						BrainStone.brainStoneDust()), 0.5f), new RecipeOutput(
						new ItemStack(Blocks.cobblestone), 0.15f));
		// BrainStone => 4xBrainStoneDust
		addSAGMillRecipe(BrainStone.brainStone(), 5000, RecipeBonusType.NONE,
				new ItemStack(BrainStone.brainStoneDust(), 4));
		// DirtyBrainStone => 4xBrainStoneDust
		addSAGMillRecipe(BrainStone.dirtyBrainStone(), 4000,
				RecipeBonusType.NONE, new ItemStack(
						BrainStone.brainStoneDust(), 4));

		// Alloy Smelter

		// 4xBrainStoneDust => BrainStone
		addAlloySmelterRecipe(BrainStone.brainStone(), 3000, new ItemStack(
				BrainStone.brainStoneDust(), 4));
		// 4xBrainStoneDust, 4xBrainStoneDust => 2xBrainStone
		addAlloySmelterRecipe(new ItemStack(BrainStone.brainStone(), 2), 6000,
				new ItemStack(BrainStone.brainStoneDust(), 4), new ItemStack(
						BrainStone.brainStoneDust(), 4));
		// 4xBrainStoneDust, 4xBrainStoneDust, 4xBrainStoneDust => 3xBrainStone
		addAlloySmelterRecipe(new ItemStack(BrainStone.brainStone(), 3), 9000,
				new ItemStack(BrainStone.brainStoneDust(), 4), new ItemStack(
						BrainStone.brainStoneDust(), 4), new ItemStack(
						BrainStone.brainStoneDust(), 4));

		DarkSteelRecipeManager.instance.getUpgrades().add(
				BrainStoneUpgrade.UPGRADE);
	}

	@Optional.Method(modid = "EnderIO")
	private static void addSAGMillRecipe(Object input, int energyRequired,
			Object... outputs) {
		addSAGMillRecipe(input, energyRequired,
				RecipeBonusType.MULTIPLY_OUTPUT, outputs);
	}

	@Optional.Method(modid = "EnderIO")
	private static void addSAGMillRecipe(Object input, int energyRequired,
			RecipeBonusType bonusType, Object... outputs) {
		RecipeInput rInput = objectToInput(input);
		RecipeOutput[] rOutputs = new RecipeOutput[outputs.length];

		for (int i = 0; i < outputs.length; i++) {
			rOutputs[i] = objectToOutput(outputs[i]);
		}

		CrusherRecipeManager.getInstance().addRecipe(
				new Recipe(rInput, energyRequired, bonusType, rOutputs));
	}

	@Optional.Method(modid = "EnderIO")
	private static void addAlloySmelterRecipe(Object output,
			int energyRequired, Object... inputs) {
		addAlloySmelterRecipe(output, energyRequired, RecipeBonusType.NONE,
				inputs);
	}

	@Optional.Method(modid = "EnderIO")
	private static void addAlloySmelterRecipe(Object output,
			int energyRequired, RecipeBonusType bonusType, Object... inputs) {
		RecipeOutput rOutput = objectToOutput(output);
		RecipeInput[] rInputs = new RecipeInput[inputs.length];

		for (int i = 0; i < inputs.length; i++) {
			rInputs[i] = objectToInput(inputs[i]);
		}

		AlloyRecipeManager.getInstance().addRecipe(
				new BasicManyToOneRecipe(new Recipe(rOutput, energyRequired,
						bonusType, rInputs)));
	}

	@Optional.Method(modid = "EnderIO")
	private static RecipeOutput objectToOutput(Object object) {
		if (object instanceof Block)
			return new RecipeOutput(new ItemStack((Block) object));
		else if (object instanceof Item)
			return new RecipeOutput(new ItemStack((Item) object));
		else if (object instanceof ItemStack)
			return new RecipeOutput((ItemStack) object);
		else if (object instanceof RecipeOutput)
			return (RecipeOutput) object;

		return null;
	}

	@Optional.Method(modid = "EnderIO")
	private static RecipeInput objectToInput(Object object) {
		if (object instanceof Block)
			return new RecipeInput(new ItemStack((Block) object));
		else if (object instanceof Item)
			return new RecipeInput(new ItemStack((Item) object));
		else if (object instanceof ItemStack)
			return new RecipeInput((ItemStack) object);
		else if (object instanceof RecipeInput)
			return (RecipeInput) object;

		return null;
	}
}
