package brainstonemod.common.helper;

import brainstonemod.BrainStoneBlocks;
import brainstonemod.BrainStoneItems;
import brainstonemod.common.advancement.CriterionRegistry;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

@RequiredArgsConstructor
public class BrainStoneLifeCapacitorUpgrade extends IForgeRegistryEntry.Impl<IRecipe>
    implements IRecipe {
  protected final Upgrade upgrade;

  @Override
  public ItemStack getCraftingResult(InventoryCrafting items) {
    ItemStack capacitor = ItemStack.EMPTY;
    ItemStack slot;

    for (int i = 0; i < items.getSizeInventory(); i++) {
      slot = items.getStackInSlot(i);

      if (!slot.isEmpty() && (slot.getItem() == BrainStoneItems.brainStoneLifeCapacitor())) {
        capacitor = slot;
        break;
      }
    }

    return getRecipeOutput(capacitor);
  }

  public ItemStack getRecipeOutput(ItemStack catalyst) {
    if (catalyst.isEmpty()) return getRecipeOutput();

    return BrainStoneItems.brainStoneLifeCapacitor().upgradeType(catalyst.copy(), upgrade);
  }

  @Override
  public ItemStack getRecipeOutput() {
    final ItemStack baseStack = new ItemStack(BrainStoneItems.brainStoneLifeCapacitor());

    return BrainStoneItems.brainStoneLifeCapacitor().upgradeType(baseStack, upgrade);
  }

  @Override
  public boolean matches(InventoryCrafting items, World world) {
    return matches((IInventory) items, world);
  }

  public boolean matches(IInventory items, World world) {
    boolean foundCapacitor = false;
    boolean foundUpgrade = false;

    ItemStack slot;
    Item item;

    final Item capacitor = BrainStoneItems.brainStoneLifeCapacitor();
    final Item upgradeItem = upgrade.getUpgrade().getItem();

    for (int i = 0; i < items.getSizeInventory(); ++i) {
      slot = items.getStackInSlot(i);

      if (slot.isEmpty()) {
        continue;
      }

      item = slot.getItem();

      if (item == capacitor) {
        if (foundCapacitor) return false;

        foundCapacitor = true;
      } else if (item == upgradeItem) {
        if (foundUpgrade) return false;

        foundUpgrade = true;
      } else return false;
    }

    return foundCapacitor && foundUpgrade;
  }

  @Override
  public boolean canFit(int width, int height) {
    return (width * height) > 2;
  }

  public ItemStack[] getStacks() {
    return new ItemStack[] {
      new ItemStack(BrainStoneItems.brainStoneLifeCapacitor()), upgrade.getUpgrade()
    };
  }

  @SubscribeEvent
  public void onItemCrafted(ItemCraftedEvent event) {
    if (!matches(event.craftMatrix, event.player.getEntityWorld())) return;

    EntityPlayerMP player;

    if (event.player instanceof EntityPlayerMP) {
      player = (EntityPlayerMP) event.player;
    } else {
      player =
          FMLCommonHandler.instance()
              .getMinecraftServerInstance()
              .getPlayerList()
              .getPlayerByUUID(event.player.getUniqueID());
    }

    int level = BrainStoneItems.brainStoneLifeCapacitor().getTypeLevel(event.crafting, upgrade);

    CriterionRegistry.UPGRADE_BRAIN_STONE_LIFE_CAPACITOR.trigger(player, upgrade, level);
  }

  public static enum Upgrade {
    CAPACITY(BrainStoneItems.essenceOfLife()),
    CHARGING(BrainStoneBlocks.pulsatingBrainStone());

    @Getter private final ItemStack upgrade;

    private Upgrade(Object upgrade) {
      if (upgrade instanceof Item) {
        this.upgrade = new ItemStack((Item) upgrade);
      } else if (upgrade instanceof Block) {
        this.upgrade = new ItemStack((Block) upgrade);
      } else if (upgrade instanceof ItemStack) {
        this.upgrade = (ItemStack) upgrade;
      } else {
        this.upgrade = ItemStack.EMPTY;
      }
    }
  }
}
