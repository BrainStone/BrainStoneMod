package brainstonemod;

import brainstonemod.common.item.ItemArmorBrainStone;
import brainstonemod.common.item.ItemBrainStoneLifeCapacitor;
import brainstonemod.common.item.ItemEssenceOfLife;
import brainstonemod.common.item.ItemHoeBrainStone;
import brainstonemod.common.item.ItemSwordBrainStone;
import brainstonemod.common.item.ItemToolBrainStone;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Nullable;
import lombok.NoArgsConstructor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@NoArgsConstructor(staticName = "registrar")
public class BrainStoneItems {
  /** The BrainStone Tool Material */
  public static ToolMaterial toolBRAINSTONE;
  /** The BrainStone Armor Material */
  public static ArmorMaterial armorBRAINSTONE;

  /** The Stable Pulsating BrainStone Tool Material */
  public static ToolMaterial toolSTABLEPULSATINGBS;
  /** The Stable Pulsating BrainStone Armor Material */
  public static ArmorMaterial armorSTABLEPULSATINGBS;

  private static final Map<String, Item> items = new LinkedHashMap<>();

  private static final void createEnums() {
    toolBRAINSTONE = EnumHelper.addToolMaterial("BRAINSTONE", 5, 5368, 6.0f, 5.0f, 25);
    armorBRAINSTONE =
        EnumHelper.addArmorMaterial(
            "BRAINSTONE",
            "BRAINSTONE",
            114,
            new int[] {2, 6, 5, 2},
            25,
            SoundEvents.ITEM_ARMOR_EQUIP_GENERIC,
            1.0f);

    toolSTABLEPULSATINGBS =
        EnumHelper.addToolMaterial("STABLEPULSATINGBS", 9, 21472, 18.0f, 20.0f, 50);
    armorSTABLEPULSATINGBS =
        EnumHelper.addArmorMaterial(
            "STABLEPULSATINGBS",
            "STABLEPULSATINGBS",
            456,
            new int[] {12, 32, 24, 12},
            50,
            SoundEvents.ITEM_ARMOR_EQUIP_GENERIC,
            8.0f);

    ItemStack brainStone = new ItemStack(BrainStoneBlocks.brainStone());
    ItemStack stablePulsatingBrainStone =
        new ItemStack(BrainStoneBlocks.stablePulsatingBrainStone());

    toolBRAINSTONE.setRepairItem(brainStone);
    armorBRAINSTONE.setRepairItem(brainStone);

    toolSTABLEPULSATINGBS.setRepairItem(stablePulsatingBrainStone);
    armorSTABLEPULSATINGBS.setRepairItem(stablePulsatingBrainStone);
  }

  protected static void generateItems() {
    createEnums();

    addItem(
        "brain_stone_dust",
        (new Item()).setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.MATERIALS)));
    addItem(
        "brain_processor",
        (new Item()).setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.MISC)));
    addItem("brain_stone_sword", new ItemSwordBrainStone(toolBRAINSTONE));
    addItem("brain_stone_shovel", new ItemToolBrainStone(toolBRAINSTONE, "spade"));
    addItem("brain_stone_pickaxe", new ItemToolBrainStone(toolBRAINSTONE, "pickaxe"));
    addItem("brain_stone_axe", new ItemToolBrainStone(toolBRAINSTONE, "axe"));
    addItem("brain_stone_hoe", new ItemHoeBrainStone(toolBRAINSTONE));
    addItem(
        "brain_stone_helmet", new ItemArmorBrainStone(armorBRAINSTONE, EntityEquipmentSlot.HEAD));
    addItem(
        "brain_stone_plate", new ItemArmorBrainStone(armorBRAINSTONE, EntityEquipmentSlot.CHEST));
    addItem(
        "brain_stone_leggings", new ItemArmorBrainStone(armorBRAINSTONE, EntityEquipmentSlot.LEGS));
    addItem(
        "brain_stone_boots", new ItemArmorBrainStone(armorBRAINSTONE, EntityEquipmentSlot.FEET));

    addItem("essence_of_life", new ItemEssenceOfLife());

    addItem("stable_pulsating_brain_stone_sword", new ItemSwordBrainStone(toolSTABLEPULSATINGBS));
    addItem(
        "stable_pulsating_brain_stone_shovel",
        new ItemToolBrainStone(toolSTABLEPULSATINGBS, "spade"));
    addItem(
        "stable_pulsating_brain_stone_pickaxe",
        new ItemToolBrainStone(toolSTABLEPULSATINGBS, "pickaxe"));
    addItem(
        "stable_pulsating_brain_stone_axe", new ItemToolBrainStone(toolSTABLEPULSATINGBS, "axe"));
    addItem("stable_pulsating_brain_stone_hoe", new ItemHoeBrainStone(toolSTABLEPULSATINGBS));
    addItem(
        "stable_pulsating_brain_stone_helmet",
        new ItemArmorBrainStone(armorSTABLEPULSATINGBS, EntityEquipmentSlot.HEAD));
    addItem(
        "stable_pulsating_brain_stone_plate",
        new ItemArmorBrainStone(armorSTABLEPULSATINGBS, EntityEquipmentSlot.CHEST));
    addItem(
        "stable_pulsating_brain_stone_leggings",
        new ItemArmorBrainStone(armorSTABLEPULSATINGBS, EntityEquipmentSlot.LEGS));
    addItem(
        "stable_pulsating_brain_stone_boots",
        new ItemArmorBrainStone(armorSTABLEPULSATINGBS, EntityEquipmentSlot.FEET));
    addItem("brain_stone_life_capacitor", (new ItemBrainStoneLifeCapacitor()));
  }

  public static void addItem(String name, Item item) {
    if (items.containsKey(name)) return;

    if (item.getRegistryName() == null) {
      item.setTranslationKey(name).setRegistryName(BrainStone.MOD_ID, name);
    }

    items.put(name, item);
  }

  /** @return the instance of BrainStoneDust */
  public static final Item brainStoneDust() {
    return items.get("brain_stone_dust");
  }

  /** @return the instance of BrainProcessor */
  public static final Item brainProcessor() {
    return items.get("brain_processor");
  }

  /** @return the instance of BrainStoneSword */
  public static final Item brainStoneSword() {
    return items.get("brain_stone_sword");
  }

  /** @return the instance of BrainStoneShovel */
  public static final Item brainStoneShovel() {
    return items.get("brain_stone_shovel");
  }

  /** @return the instance of BrainStonePickaxe */
  public static final Item brainStonePickaxe() {
    return items.get("brain_stone_pickaxe");
  }

  /** @return the instance of BrainStoneAxe */
  public static final Item brainStoneAxe() {
    return items.get("brain_stone_axe");
  }

  /** @return the instance of BrainStoneHoe */
  public static final Item brainStoneHoe() {
    return items.get("brain_stone_hoe");
  }

  /** @return the instance of BrainStoneHelmet */
  public static final Item brainStoneHelmet() {
    return items.get("brain_stone_helmet");
  }

  /** @return the instance of BrainStonePlate */
  public static final Item brainStonePlate() {
    return items.get("brain_stone_plate");
  }

  /** @return the instance of BrainStoneLeggings */
  public static final Item brainStoneLeggings() {
    return items.get("brain_stone_leggings");
  }

  /** @return the instance of BrainStoneBoots */
  public static final Item brainStoneBoots() {
    return items.get("brain_stone_boots");
  }

  /** @return the instance of Essence Of Live */
  public static final Item essenceOfLife() {
    return items.get("essence_of_life");
  }

  /** @return the instance of Stable Pulsating Brain Stone Sword */
  public static final Item stablePulsatingBrainStoneSword() {
    return items.get("stable_pulsating_brain_stone_sword");
  }

  /** @return the instance of Stable Pulsating Brain Stone Shovel */
  public static final Item stablePulsatingBrainStoneShovel() {
    return items.get("stable_pulsating_brain_stone_shovel");
  }

  /** @return the instance of Stable Pulsating Brain Stone Pickaxe */
  public static final Item stablePulsatingBrainStonePickaxe() {
    return items.get("stable_pulsating_brain_stone_pickaxe");
  }

  /** @return the instance of Stable Pulsating Brain Stone Axe */
  public static final Item stablePulsatingBrainStoneAxe() {
    return items.get("stable_pulsating_brain_stone_axe");
  }

  /** @return the instance of Stable Pulsating Brain Stone Hoe */
  public static final Item stablePulsatingBrainStoneHoe() {
    return items.get("stable_pulsating_brain_stone_hoe");
  }

  /** @return the instance of Stable Pulsating Brain Stone Helmet */
  public static final Item stablePulsatingBrainStoneHelmet() {
    return items.get("stable_pulsating_brain_stone_helmet");
  }

  /** @return the instance of Stable Pulsating Brain Stone Chestplate */
  public static final Item stablePulsatingBrainStonePlate() {
    return items.get("stable_pulsating_brain_stone_plate");
  }

  /** @return the instance of Stable Pulsating Brain Stone Leggings */
  public static final Item stablePulsatingBrainStoneLeggings() {
    return items.get("stable_pulsating_brain_stone_leggings");
  }

  /** @return the instance of Stable Pulsating Brain Stone Leggings */
  public static final Item stablePulsatingBrainStoneBoots() {
    return items.get("stable_pulsating_brain_stone_boots");
  }

  /** @return the instance of Brain Stone Life Capacitor */
  public static final ItemBrainStoneLifeCapacitor brainStoneLifeCapacitor() {
    return (ItemBrainStoneLifeCapacitor) items.get("brain_stone_life_capacitor");
  }

  /**
   * @return the instance of Brain Stone Dust Tiny.<br>
   *     <strong>Only available when Forestry or Ex Nihilo Adscensio is present!</strong>
   */
  @Nullable
  public static final Item brainStoneDustTiny() {
    return items.get("brain_stone_dust_tiny");
  }

  /**
   * @return the instance of Essence of Life Dust.<br>
   *     <strong>Only available when Forestry is present!</strong>
   */
  @Nullable
  public static final Item essenceOfLifeDust() {
    return items.get("essence_of_life_dust");
  }

  /**
   * @return the instance of Essence of Life Fragment.<br>
   *     <strong>Only available when Forestry or Mystical Agriculture is present!</strong>
   */
  @Nullable
  public static final Item essenceOfLifeFragment() {
    return items.get("essence_of_life_fragment");
  }

  /**
   * @return the instance of Brain Stone Comb.<br>
   *     <strong>Only available when Forestry is present!</strong>
   */
  @Nullable
  public static final Item brainStoneComb() {
    return items.get("brain_stone_comb");
  }

  /**
   * @return the instance of Brain Stone Scoop.<br>
   *     <strong>Only available when Forestry is present!</strong>
   */
  @Nullable
  public static final Item brainStoneScoop() {
    return items.get("brain_stone_scoop");
  }

  /**
   * @return the instance of Stable Pulsating Brain Stone Scoop.<br>
   *     <strong>Only available when Forestry is present!</strong>
   */
  @Nullable
  public static final Item stablePulsatingBrainStoneScoop() {
    return items.get("stable_pulsating_brain_stone_scoop");
  }

  /**
   * @return the instance of Brain Stone Essence.<br>
   *     <strong>Only available when Mystical Agriculture is present!</strong>
   */
  @Nullable
  public static final Item brainStoneEssence() {
    return items.get("brain_stone_essence");
  }

  /**
   * @return the instance of Brain Stone Seeds.<br>
   *     <strong>Only available when Mystical Agriculture is present!</strong>
   */
  @Nullable
  public static final Item brainStoneSeeds() {
    return items.get("brain_stone_seeds");
  }

  /**
   * @return the instance of Essence of Life Essence.<br>
   *     <strong>Only available when Mystical Agriculture is present!</strong>
   */
  @Nullable
  public static final Item essenceOfLifeEssence() {
    return items.get("essence_of_life_essence");
  }

  /**
   * @return the instance of Essence of Life Seeds.<br>
   *     <strong>Only available when Mystical Agriculture is present!</strong>
   */
  @Nullable
  public static final Item essenceOfLifeSeeds() {
    return items.get("essence_of_life_seeds");
  }

  @SubscribeEvent
  public void registerItems(RegistryEvent.Register<Item> event) {
    items.values().stream()
        .forEach(
            item -> {
              event.getRegistry().register(item);
              BrainStone.proxy.registerModel(item);
            });
  }
}
