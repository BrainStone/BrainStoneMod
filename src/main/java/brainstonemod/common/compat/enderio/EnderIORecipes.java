package brainstonemod.common.compat.enderio;

import brainstonemod.BrainStoneBlocks;
import brainstonemod.BrainStoneItems;
import brainstonemod.common.compat.BrainStoneModules;
import crazypants.enderio.api.upgrades.IDarkSteelUpgrade;
import crazypants.enderio.base.recipe.BasicManyToOneRecipe;
import crazypants.enderio.base.recipe.Recipe;
import crazypants.enderio.base.recipe.RecipeBonusType;
import crazypants.enderio.base.recipe.RecipeInput;
import crazypants.enderio.base.recipe.RecipeOutput;
import crazypants.enderio.base.recipe.alloysmelter.AlloyRecipeManager;
import crazypants.enderio.base.recipe.sagmill.SagMillRecipeManager;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

public class EnderIORecipes {
	@Optional.Method(modid = BrainStoneModules.ENDER_IO_MODID)
	private static void addSAGMillRecipe(Object input, int energyRequired, Object... outputs) {
		addSAGMillRecipe(input, energyRequired, RecipeBonusType.MULTIPLY_OUTPUT, outputs);
	}

	@Optional.Method(modid = BrainStoneModules.ENDER_IO_MODID)
	private static void addSAGMillRecipe(Object input, int energyRequired, RecipeBonusType bonusType,
			Object... outputs) {
		RecipeInput rInput = objectToInput(input);
		RecipeOutput[] rOutputs = new RecipeOutput[outputs.length];

		for (int i = 0; i < outputs.length; i++) {
			rOutputs[i] = objectToOutput(outputs[i]);
		}

		SagMillRecipeManager.getInstance().addRecipe(new Recipe(rInput, energyRequired, bonusType, rOutputs));
	}

	@Optional.Method(modid = BrainStoneModules.ENDER_IO_MODID)
	private static void addAlloySmelterRecipe(Object output, int energyRequired, Object... inputs) {
		addAlloySmelterRecipe(output, energyRequired, RecipeBonusType.NONE, inputs);
	}

	@Optional.Method(modid = BrainStoneModules.ENDER_IO_MODID)
	private static void addAlloySmelterRecipe(Object output, int energyRequired, RecipeBonusType bonusType,
			Object... inputs) {
		RecipeOutput rOutput = objectToOutput(output);
		RecipeInput[] rInputs = new RecipeInput[inputs.length];

		for (int i = 0; i < inputs.length; i++) {
			rInputs[i] = objectToInput(inputs[i]);
		}

		AlloyRecipeManager.getInstance()
				.addRecipe(new BasicManyToOneRecipe(new Recipe(rOutput, energyRequired, bonusType, rInputs)));
	}

	@Optional.Method(modid = BrainStoneModules.ENDER_IO_MODID)
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

	@Optional.Method(modid = BrainStoneModules.ENDER_IO_MODID)
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

	@Optional.Method(modid = BrainStoneModules.ENDER_IO_MODID)
	public void registerEnderIORecipies() {
		ItemStack brainStoneDust4 = new ItemStack(BrainStoneItems.brainStoneDust(), 4);
	
		// SAG Mill
	
		// BrainStoneOre => BrainStoneDust, BrainStoneDust 50%
		addSAGMillRecipe(BrainStoneBlocks.brainStoneOre(), 4000, BrainStoneItems.brainStoneDust(),
				new RecipeOutput(new ItemStack(BrainStoneItems.brainStoneDust()), 0.5f),
				new RecipeOutput(new ItemStack(Blocks.COBBLESTONE), 0.15f));
		// BrainStone => 4xBrainStoneDust
		addSAGMillRecipe(BrainStoneBlocks.brainStone(), 5000, RecipeBonusType.NONE, brainStoneDust4);
		// DirtyBrainStone => 4xBrainStoneDust
		addSAGMillRecipe(BrainStoneBlocks.dirtyBrainStone(), 4000, RecipeBonusType.NONE, brainStoneDust4);
	
		// Alloy Smelter
	
		// 4xBrainStoneDust => BrainStone
		addAlloySmelterRecipe(BrainStoneBlocks.brainStone(), 3000, brainStoneDust4);
		// 4xBrainStoneDust, 4xBrainStoneDust => 2xBrainStone
		addAlloySmelterRecipe(new ItemStack(BrainStoneBlocks.brainStone(), 2), 6000, brainStoneDust4, brainStoneDust4);
		// 4xBrainStoneDust, 4xBrainStoneDust, 4xBrainStoneDust => 3xBrainStone
		addAlloySmelterRecipe(new ItemStack(BrainStoneBlocks.brainStone(), 3), 9000, brainStoneDust4, brainStoneDust4,
				brainStoneDust4);
	}

	@SubscribeEvent
	@Optional.Method(modid = BrainStoneModules.ENDER_IO_MODID)
	public void registerDarkSteelUpgrades(@Nonnull RegistryEvent.Register<IDarkSteelUpgrade> event) {
		final IForgeRegistry<IDarkSteelUpgrade> registry = event.getRegistry();

		registry.register(BrainStoneUpgrade.HELMET);
		registry.register(BrainStoneUpgrade.CHEST);
		registry.register(BrainStoneUpgrade.LEGS);
		registry.register(BrainStoneUpgrade.BOOTS);
	}
}
