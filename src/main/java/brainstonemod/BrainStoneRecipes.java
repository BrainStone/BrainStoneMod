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
import net.minecraftforge.common.MinecraftForge;
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
		Object craftingS = new ItemStack(Items.SKULL, 1, 5);
		Object craftingX = IngredientSwitch.make()
				.add(BrainStoneModules::redstoneArsenal, RedstoneArsenalItems::getFluxInfusedObsidianRod)
				.add(BrainStoneModules::tinkersConstruct, TinkersConstructItems::getManyullyToughRod)
				.getIngredient(Blocks.END_ROD);
		Object craftingC = IngredientSwitch.make()
				.add(BrainStoneModules::thermalExpansion, ThermalExpansionItems::getResonantFluxCapacitor)
				.add(BrainStoneModules::draconicEvolution, DraconicEvolutionItems::getWyvernFluxCapacitor)
				.add(BrainStoneModules::immersiveEngineering, ImmersiveEngineeringItems::getHVCapacitor)
				.add(BrainStoneModules::redstoneArsenal, RedstoneArsenalItems::getFluxedArmorPlating)
				.add(BrainStoneModules::forestry, ForestryItems::getEmeraldElectronTube)
				.add(BrainStoneModules::tinkersConstruct, TinkersConstructItems::getMendingMoss)
				.add(BrainStoneModules::thermalFoundation, ThermalFoundationItems::getPyrotheumDustDict)
				.getIngredient("dustRedstone");
		Object craftingH = IngredientSwitch.make()
				.add(BrainStoneModules::draconicEvolution, DraconicEvolutionItems::getDragonHeart)
				.add(BrainStoneModules::mysticalAgriculture, MysticalAgricultureItems::getSupremiumApple)
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

	@Deprecated
	public static final void addShapedRecipe(ItemStack output, Object... input) {
		ResourceLocation location = getNameForRecipe(output);
		ShapedOreRecipe recipe = new ShapedOreRecipe(location, output, input);
		recipe.setRegistryName(location);
		GameData.register_impl(recipe);
	}

	@Deprecated
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
		BrainStoneLifeCapacitorUpgrade upgradeCapacity = new BrainStoneLifeCapacitorUpgrade(
				BrainStoneLifeCapacitorUpgrade.Upgrade.CAPACITY);
		BrainStoneLifeCapacitorUpgrade upgradeCharging = new BrainStoneLifeCapacitorUpgrade(
				BrainStoneLifeCapacitorUpgrade.Upgrade.CHARGING);

		// Capacitor Recipes
		registry.register(upgradeCapacity.setRegistryName("brain_stone_life_capacitor_upgrade_capacity"));
		registry.register(upgradeCharging.setRegistryName("brain_stone_life_capacitor_upgrade_charging"));

		MinecraftForge.EVENT_BUS.register(upgradeCapacity);
		MinecraftForge.EVENT_BUS.register(upgradeCharging);
	}
}
