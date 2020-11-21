package brainstonemod.common.compat.enderio;

import brainstonemod.BrainStoneBlocks;
import brainstonemod.BrainStoneItems;
import brainstonemod.common.compat.BrainStoneModules;
import brainstonemod.common.compat.enderio.dark_steel_upgrade.BrainStoneUpgrade;
import brainstonemod.common.compat.enderio.dark_steel_upgrade.DarkSteelBaseUpgrade;
import brainstonemod.common.helper.BSP;
import crazypants.enderio.api.upgrades.IDarkSteelUpgrade;
import crazypants.enderio.base.recipe.BasicManyToOneRecipe;
import crazypants.enderio.base.recipe.Recipe;
import crazypants.enderio.base.recipe.RecipeBonusType;
import crazypants.enderio.base.recipe.RecipeInput;
import crazypants.enderio.base.recipe.RecipeOutput;
import crazypants.enderio.base.recipe.alloysmelter.AlloyRecipeManager;
import crazypants.enderio.base.recipe.sagmill.SagMillRecipeManager;
import javax.annotation.Nonnull;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class EnderIORecipes {
  @Optional.Method(modid = BrainStoneModules.ENDER_IO_MODID)
  private static void addSAGMillRecipe(Object input, int energyRequired, Object... outputs) {
    addSAGMillRecipe(input, energyRequired, RecipeBonusType.MULTIPLY_OUTPUT, outputs);
  }

  @Optional.Method(modid = BrainStoneModules.ENDER_IO_MODID)
  private static void addSAGMillRecipe(
      Object input, int energyRequired, RecipeBonusType bonusType, Object... outputs) {
    RecipeInput rInput = objectToInput(input);
    RecipeOutput[] rOutputs = new RecipeOutput[outputs.length];

    for (int i = 0; i < outputs.length; i++) {
      rOutputs[i] = objectToOutput(outputs[i]);
    }

    SagMillRecipeManager.getInstance()
        .addRecipe(new Recipe(rInput, energyRequired, bonusType, rOutputs));
  }

  @Optional.Method(modid = BrainStoneModules.ENDER_IO_MODID)
  private static void addAlloySmelterRecipe(Object output, int energyRequired, Object... inputs) {
    addAlloySmelterRecipe(output, energyRequired, RecipeBonusType.NONE, inputs);
  }

  @Optional.Method(modid = BrainStoneModules.ENDER_IO_MODID)
  private static void addAlloySmelterRecipe(
      Object output, int energyRequired, RecipeBonusType bonusType, Object... inputs) {
    RecipeOutput rOutput = objectToOutput(output);
    RecipeInput[] rInputs = new RecipeInput[inputs.length];

    for (int i = 0; i < inputs.length; i++) {
      rInputs[i] = objectToInput(inputs[i]);
    }

    AlloyRecipeManager.getInstance()
        .addRecipe(
            new BasicManyToOneRecipe(new Recipe(rOutput, energyRequired, bonusType, rInputs)));
  }

  @Optional.Method(modid = BrainStoneModules.ENDER_IO_MODID)
  private static void addDarkSteelUpgradeRecipe(DarkSteelBaseUpgrade upgrade) {
    final ItemStack upgradeItem = upgrade.getUpgradeItem();
    final NBTTagCompound upgradeItemNBT = upgradeItem.getTagCompound();

    final String upgradeId = upgrade.getId();
    final String itemXMLAttributes =
        "name=\""
            + upgradeItem.getItem().getRegistryName().toString()
            + ':'
            + upgradeItem.getItemDamage()
            + '"'
            + ((upgradeItemNBT == null) ? "" : " nbt='" + upgradeItemNBT.toString() + '\'');
    final String resultItemXMLAttributes =
        "name=\"enderio:item_dark_steel_upgrade:1\" nbt='{\"enderio:dsu\":\"" + upgradeId + "\"}'";

    final String recipes =
        "<recipes>\n"
            + "<recipe required=\"true\" name=\"Dark Steel Upgrade: Pulsating Brain Stone Protection\">\n"
            + "<crafting>\n"
            + "<shapeless>\n"
            + "<item name=\"enderio:item_dark_steel_upgrade:0\"/>\n"
            + "<item "
            + itemXMLAttributes
            + " />\n"
            + "</shapeless>\n"
            + "<output "
            + resultItemXMLAttributes
            + " />\n"
            + "</crafting>\n"
            + "</recipe>\n"
            + "<recipe required=\"true\" name=\"Dark Steel Upgrade Empowering: Pulsating Brain Stone Protection\">\n"
            + "<tanking type=\"FILL\" logic=\"DSU\">\n"
            + "<input "
            + resultItemXMLAttributes
            + "/>\n"
            + "<fluid name=\"xpjuice\" amount=\"1\"/>\n"
            + "<output "
            + resultItemXMLAttributes
            + "/>\n"
            + "</tanking>\n"
            + "</recipe>\n"
            + "</recipes>";

    // Note BrainStoneModules.ENDER_IO_MODID is "enderiobase" and not
    // "enderio"!
    FMLInterModComms.sendMessage("enderio", "recipe:xml", recipes);
    BSP.debug(recipes);
  }

  @Optional.Method(modid = BrainStoneModules.ENDER_IO_MODID)
  private static RecipeOutput objectToOutput(Object object) {
    if (object instanceof Block) return new RecipeOutput(new ItemStack((Block) object));
    else if (object instanceof Item) return new RecipeOutput(new ItemStack((Item) object));
    else if (object instanceof ItemStack) return new RecipeOutput((ItemStack) object);
    else if (object instanceof RecipeOutput) return (RecipeOutput) object;

    return null;
  }

  @Optional.Method(modid = BrainStoneModules.ENDER_IO_MODID)
  private static RecipeInput objectToInput(Object object) {
    if (object instanceof Block) return new RecipeInput(new ItemStack((Block) object));
    else if (object instanceof Item) return new RecipeInput(new ItemStack((Item) object));
    else if (object instanceof ItemStack) return new RecipeInput((ItemStack) object);
    else if (object instanceof RecipeInput) return (RecipeInput) object;

    return null;
  }

  @Optional.Method(modid = BrainStoneModules.ENDER_IO_MODID)
  public void registerEnderIORecipies() {
    ItemStack brainStoneDust4 = new ItemStack(BrainStoneItems.brainStoneDust(), 4);

    // SAG Mill

    // BrainStoneOre => BrainStoneDust, BrainStoneDust 50%
    addSAGMillRecipe(
        BrainStoneBlocks.brainStoneOre(),
        4000,
        BrainStoneItems.brainStoneDust(),
        new RecipeOutput(new ItemStack(BrainStoneItems.brainStoneDust()), 0.5f),
        new RecipeOutput(new ItemStack(Blocks.COBBLESTONE), 0.15f));
    // BrainStone => 4xBrainStoneDust
    addSAGMillRecipe(BrainStoneBlocks.brainStone(), 5000, RecipeBonusType.NONE, brainStoneDust4);
    // DirtyBrainStone => 4xBrainStoneDust
    addSAGMillRecipe(
        BrainStoneBlocks.dirtyBrainStone(), 4000, RecipeBonusType.NONE, brainStoneDust4);

    // Alloy Smelter

    // 4xBrainStoneDust => BrainStone
    addAlloySmelterRecipe(BrainStoneBlocks.brainStone(), 3000, brainStoneDust4);
    // 4xBrainStoneDust, 4xBrainStoneDust => 2xBrainStone
    addAlloySmelterRecipe(
        new ItemStack(BrainStoneBlocks.brainStone(), 2), 6000, brainStoneDust4, brainStoneDust4);
    // 4xBrainStoneDust, 4xBrainStoneDust, 4xBrainStoneDust => 3xBrainStone
    addAlloySmelterRecipe(
        new ItemStack(BrainStoneBlocks.brainStone(), 3),
        9000,
        brainStoneDust4,
        brainStoneDust4,
        brainStoneDust4);

    // Dark Steel Upgrades
    addDarkSteelUpgradeRecipe(BrainStoneUpgrade.UPGRADE);
  }

  @SubscribeEvent
  @Optional.Method(modid = BrainStoneModules.ENDER_IO_MODID)
  public void registerDarkSteelUpgrades(@Nonnull RegistryEvent.Register<IDarkSteelUpgrade> event) {
    final IForgeRegistry<IDarkSteelUpgrade> registry = event.getRegistry();

    registry.registerAll(BrainStoneUpgrade.UPGRADE);
  }
}
