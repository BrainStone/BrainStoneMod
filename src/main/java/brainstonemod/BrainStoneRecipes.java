package brainstonemod;

import brainstonemod.common.compat.BrainStoneModules;
import brainstonemod.common.compat.cofh.thermalexpansion.ThermalExpansionItems;
import brainstonemod.common.compat.cofh.thermalfoundation.ThermalFoundationItems;
import brainstonemod.common.compat.draconicevolution.DraconicEvolutionItems;
import brainstonemod.common.compat.forestry.ForestryItems;
import brainstonemod.common.compat.immersiveengineering.ImmersiveEngineeringItems;
import brainstonemod.common.compat.mysticalagriculture.MysticalAgricultureItems;
import brainstonemod.common.compat.redstonearsenal.RedstoneArsenalItems;
import brainstonemod.common.compat.tinkersconstruct.TinkersConstructItems;
import brainstonemod.common.helper.BrainStoneLifeCapacitorUpgrade;
import brainstonemod.common.helper.IngredientSwitch;
import lombok.NoArgsConstructor;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.IForgeRegistry;

@NoArgsConstructor(staticName = "registrar")
public class BrainStoneRecipes {
	/**
	 * Adds the recipes.
	 */
	protected static final void addRecipes() {
		addShapedRecipe(new ItemStack(BrainStoneBlocks.dirtyBrainStone(), 1), "XX", "XX", 'X', "dustBrainstone");
		addShapedRecipe(new ItemStack(BrainStoneBlocks.brainLightSensor(), 1), "XGX", "XBX", "XPX", 'X', "stone", 'G',
				"blockGlass", 'B', "brainstone", 'P', BrainStoneItems.brainProcessor());
		addShapedRecipe(new ItemStack(BrainStoneBlocks.brainStoneTrigger(), 1), "XXX", "RPR", "XBX", 'X', "stone", 'B',
				"brainstone", 'R', "dustRedstone", 'P', BrainStoneItems.brainProcessor());
		addShapedRecipe(new ItemStack(BrainStoneBlocks.pulsatingBrainStone(), 1), "dBd", "BDB", "dBd", 'd',
				"dustBrainstone", 'B', "brainstone", 'D', "gemDiamond");
		addShapedRecipe(new ItemStack(BrainStoneBlocks.pulsatingBrainStone(), 1), "BdB", "dDd", "BdB", 'd',
				"dustBrainstone", 'B', "brainstone", 'D', "gemDiamond");
		addShapedRecipe(new ItemStack(BrainStoneBlocks.stablePulsatingBrainStone(), 1), "EPE", "PSP", "EPE", 'E',
				BrainStoneItems.essenceOfLife(), 'P', "pulsatingbrainstone", 'S', Items.NETHER_STAR);
		addShapedRecipe(new ItemStack(BrainStoneBlocks.stablePulsatingBrainStone(), 1), "PEP", "ESE", "PEP", 'E',
				BrainStoneItems.essenceOfLife(), 'P', "pulsatingbrainstone", 'S', Items.NETHER_STAR);
		addShapedRecipe(new ItemStack(BrainStoneBlocks.brainStoneAnvil(), 1), "PPP", " B ", "BBB", 'P',
				BrainStoneBlocks.pulsatingBrainStone(), 'B', "brainstone");
		addShapedRecipe(new ItemStack(BrainStoneBlocks.pulsatingBrainStoneAnvil(), 1), "PEP", " P ", "BBB", 'P',
				BrainStoneBlocks.pulsatingBrainStone(), 'E', BrainStoneItems.essenceOfLife(), 'B', "brainstone");
		addShapedRecipe(new ItemStack(BrainStoneItems.brainStoneSword(), 1), "B", "B", "S", 'S', "stickWood", 'B',
				"brainstone");
		addShapedRecipe(new ItemStack(BrainStoneItems.brainStoneShovel(), 1), "B", "S", "S", 'S', "stickWood", 'B',
				"brainstone");
		addShapedRecipe(new ItemStack(BrainStoneItems.brainStonePickaxe(), 1), "BBB", " S ", " S ", 'S', "stickWood",
				'B', "brainstone");
		addShapedRecipe(new ItemStack(BrainStoneItems.brainStoneAxe(), 1), "BB", "BS", " S", 'S', "stickWood", 'B',
				"brainstone");
		addShapedRecipe(new ItemStack(BrainStoneItems.brainStoneHoe(), 1), "BB", " S", " S", 'S', "stickWood", 'B',
				"brainstone");
		addShapedRecipe(new ItemStack(BrainStoneItems.brainStoneHelmet(), 1), "BBB", "B B", 'B', "brainstone");
		addShapedRecipe(new ItemStack(BrainStoneItems.brainStonePlate(), 1), "B B", "BBB", "BBB", 'B', "brainstone");
		addShapedRecipe(new ItemStack(BrainStoneItems.brainStoneLeggings(), 1), "BBB", "B B", "B B", 'B', "brainstone");
		addShapedRecipe(new ItemStack(BrainStoneItems.brainStoneBoots(), 1), "B B", "B B", 'B', "brainstone");
		addShapedRecipe(new ItemStack(BrainStoneItems.brainProcessor(), 4), "TRT", "SBS", "TRT", 'B', "brainstone", 'S',
				"dustRedstone", 'T', Blocks.REDSTONE_TORCH, 'R', Items.REPEATER);
		addShapedRecipe(new ItemStack(BrainStoneItems.stablePulsatingBrainStoneSword(), 1), "B", "B", "S", 'S',
				"blockRedstone", 'B', "stablepulsatingbrainstone");
		addShapedRecipe(new ItemStack(BrainStoneItems.stablePulsatingBrainStoneShovel(), 1), "B", "S", "S", 'S',
				"blockRedstone", 'B', "stablepulsatingbrainstone");
		addShapedRecipe(new ItemStack(BrainStoneItems.stablePulsatingBrainStonePickaxe(), 1), "BBB", " S ", " S ", 'S',
				"blockRedstone", 'B', "stablepulsatingbrainstone");
		addShapedRecipe(new ItemStack(BrainStoneItems.stablePulsatingBrainStoneAxe(), 1), "BB", "BS", " S", 'S',
				"blockRedstone", 'B', "stablepulsatingbrainstone");
		addShapedRecipe(new ItemStack(BrainStoneItems.stablePulsatingBrainStoneHoe(), 1), "BB", " S", " S", 'S',
				"blockRedstone", 'B', "stablepulsatingbrainstone");
		addShapedRecipe(new ItemStack(BrainStoneItems.stablePulsatingBrainStoneHelmet(), 1), "BBB", "B B", 'B',
				"stablepulsatingbrainstone");
		addShapedRecipe(new ItemStack(BrainStoneItems.stablePulsatingBrainStonePlate(), 1), "B B", "BBB", "BBB", 'B',
				"stablepulsatingbrainstone");
		addShapedRecipe(new ItemStack(BrainStoneItems.stablePulsatingBrainStoneLeggings(), 1), "BBB", "B B", "B B", 'B',
				"stablepulsatingbrainstone");
		addShapedRecipe(new ItemStack(BrainStoneItems.stablePulsatingBrainStoneBoots(), 1), "B B", "B B", 'B',
				"stablepulsatingbrainstone");

		Object craftingS = new ItemStack(Items.SKULL, 1, 5);
		Object craftingX = IngredientSwitch.make()
				.add(BrainStoneModules::redstoneArsenal, () -> RedstoneArsenalItems.getFluxInfusedObsidianRod())
				.add(BrainStoneModules::tinkersConstruct, () -> TinkersConstructItems.getManyullyToughRod())
				.getIngredient(Blocks.END_ROD);
		Object craftingC = IngredientSwitch.make()
				.add(BrainStoneModules::thermalExpansion, () -> ThermalExpansionItems.getResonantFluxCapacitor())
				.add(BrainStoneModules::draconicEvolution, () -> DraconicEvolutionItems.getWyvernFluxCapacitor())
				.add(BrainStoneModules::immersiveEngineering, () -> ImmersiveEngineeringItems.getHVCapacitor())
				.add(BrainStoneModules::redstoneArsenal, () -> RedstoneArsenalItems.getFluxedArmorPlating())
				.add(BrainStoneModules::forestry, () -> ForestryItems.getEmeraldElectronTube())
				.add(BrainStoneModules::tinkersConstruct, () -> TinkersConstructItems.getMendingMoss())
				.add(BrainStoneModules::tinkersConstruct, () -> ThermalFoundationItems.getPyrotheumDustDict())
				.getIngredient("dustRedstone");
		Object craftingH = IngredientSwitch.make()
				.add(BrainStoneModules::draconicEvolution, () -> DraconicEvolutionItems.getDragonHeart())
				.add(BrainStoneModules::mysticalAgriculture, () -> MysticalAgricultureItems.getSupremiumApple())
				.getIngredient(() -> new ItemStack(Items.GOLDEN_APPLE, 1, 1));

		addShapedRecipe(new ItemStack(BrainStoneItems.brainStoneLifeCapacitor(), 1), "SBX", "CHC", " P ", 'S',
				craftingS, 'B', BrainStoneItems.brainProcessor(), 'X', craftingX, 'C', craftingC, 'H', craftingH, 'P',
				BrainStoneItems.stablePulsatingBrainStonePlate());
	}

	/**
	 * Adds the smeltings.
	 */
	protected static final void addSmeltings() {
		GameRegistry.addSmelting(BrainStoneBlocks.dirtyBrainStone(), new ItemStack(BrainStoneBlocks.brainStone(), 1, 0),
				3.0F);
	}

	public static final void addShapedRecipe(ItemStack output, Object... input) {
		ResourceLocation location = getNameForRecipe(output);
		ShapedOreRecipe recipe = new ShapedOreRecipe(location, output, input);
		recipe.setRegistryName(location);
		GameData.register_impl(recipe);
	}

	public static final void addShapelessRecipe(ItemStack output, Object... input) {
		ResourceLocation location = getNameForRecipe(output);
		ShapelessRecipes recipe = new ShapelessRecipes(location.getResourceDomain(), output, buildInput(input));
		recipe.setRegistryName(location);
		GameData.register_impl(recipe);
	}

	private static ResourceLocation getNameForRecipe(ItemStack output) {
		ModContainer activeContainer = Loader.instance().activeModContainer();
		ResourceLocation baseLoc = new ResourceLocation(activeContainer.getModId(),
				output.getItem().getRegistryName().getResourcePath());
		ResourceLocation recipeLoc = baseLoc;
		int index = 0;

		while (CraftingManager.REGISTRY.containsKey(recipeLoc)) {
			index++;
			recipeLoc = new ResourceLocation(activeContainer.getModId(), baseLoc.getResourcePath() + "_" + index);
		}

		return recipeLoc;
	}

	private static NonNullList<Ingredient> buildInput(Object[] input) {
		NonNullList<Ingredient> list = NonNullList.create();

		for (Object obj : input) {
			if (obj instanceof Ingredient) {
				list.add((Ingredient) obj);
			} else {
				Ingredient ingredient = CraftingHelper.getIngredient(obj);

				if (ingredient == null) {
					ingredient = Ingredient.EMPTY;
				}
				list.add(ingredient);
			}
		}

		return list;
	}

	@SubscribeEvent
	public void registerRecipes(Register<IRecipe> event) {
		IForgeRegistry<IRecipe> registry = event.getRegistry();

		// Capacitor Recipes
		registry.register(new BrainStoneLifeCapacitorUpgrade(BrainStoneLifeCapacitorUpgrade.Upgrade.CAPACITY)
				.setRegistryName("brain_stone_life_capacitor_upgrade_capacity"));
		registry.register(new BrainStoneLifeCapacitorUpgrade(BrainStoneLifeCapacitorUpgrade.Upgrade.CHARGING)
				.setRegistryName("brain_stone_life_capacitor_upgrade_charging"));
	}
}
