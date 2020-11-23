package brainstonemod.common.compat.mysticalagriculture;

import brainstonemod.BrainStone;
import brainstonemod.BrainStoneItems;
import brainstonemod.common.compat.BrainStoneModules;
import brainstonemod.common.compat.IModIntegration;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@SuppressFBWarnings(
    value = "URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD",
    justification = "Publically readable as external API")
public class MysticalAgricultureCompat implements IModIntegration {
  public MysticalAgricultureCropType brainStone;
  public MysticalAgricultureCropType essenceOfLife;

  protected List<MysticalAgricultureCropType> crops = new LinkedList<>();

  protected MysticalAgricultureCropType newCrop(
      String name, int tier, Object material, boolean enabled) {
    MysticalAgricultureCropType crop =
        MysticalAgricultureCropType.of(name, tier, material, enabled);
    crops.add(crop);

    return crop;
  }

  protected final void forEachCrop(Consumer<? super MysticalAgricultureCropType> operation) {
    crops.stream().forEach(operation);
  }

  @Override
  public void preInit(FMLPreInitializationEvent event) {
    brainStone = newCrop("brain_stone", 3, BrainStoneItems.brainStoneDust(), true);
    essenceOfLife = newCrop("essence_of_life", 5, BrainStoneItems.essenceOfLife(), true);

    forEachCrop(MysticalAgricultureCropType::generate);

    if (!BrainStoneModules.forestry()) {
      BrainStoneItems.addItem(
          "essence_of_life_fragment",
          (new Item()).setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.MATERIALS)));
    }
  }

  @Override
  public void init(FMLInitializationEvent event) {
    forEachCrop(MysticalAgricultureCropType::addSeedRecipe);
    forEachCrop(MysticalAgricultureCropType::addReprocessorRecipe);
  }

  @Override
  public void postInit(FMLPostInitializationEvent event) {
    // Do nothing
  }
}
