package brainstonemod.common.container;

import brainstonemod.BrainStoneBlocks;
import brainstonemod.common.block.BlockBrainStoneAnvil;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemNameTag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringUtils;

public class ContainerBrainStoneAnvil extends ContainerRepair {
  public static final float BRAIN_STONE_FACTOR = 1.0f / 20.0f;
  public static final float PULSATING_BRAIN_STONE_FACTOR = 1.0f / 100000.0f;

  @SideOnly(Side.CLIENT)
  public ContainerBrainStoneAnvil(
      InventoryPlayer inventoryIn, World worldIn, EntityPlayer thePlayer) {
    this(inventoryIn, worldIn, BlockPos.ORIGIN, thePlayer);
  }

  public ContainerBrainStoneAnvil(
      InventoryPlayer playerInventory,
      final World world,
      final BlockPos blockPosIn,
      EntityPlayer player) {
    super(playerInventory, world, blockPosIn, player);

    overrideSlot(
        2,
        new Slot(outputSlot, 2, 134, 47) {
          @Override
          public boolean isItemValid(@Nullable ItemStack stack) {
            return false;
          }

          @Override
          public boolean canTakeStack(EntityPlayer playerIn) {
            return (playerIn.capabilities.isCreativeMode
                    || (playerIn.experienceLevel >= maximumCost))
                && (maximumCost > 0)
                && getHasStack();
          }

          @Override
          public ItemStack onTake(EntityPlayer playerIn, ItemStack stack) {
            if (!player.capabilities.isCreativeMode) {
              player.addExperienceLevel(-maximumCost);
            }

            float breakChance =
                net.minecraftforge.common.ForgeHooks.onAnvilRepair(
                    player, stack, inputSlots.getStackInSlot(0), inputSlots.getStackInSlot(1));

            ItemStack itemstack = inputSlots.getStackInSlot(0);

            if ((itemstack.getCount() != 1)
                && !player.capabilities.isCreativeMode
                && !(itemstack.getItem() instanceof ItemNameTag)) {
              itemstack.setCount(itemstack.getCount() - 1);
            } else {
              inputSlots.setInventorySlotContents(0, ItemStack.EMPTY);
            }

            if (materialCost > 0) {
              ItemStack itemstack1 = inputSlots.getStackInSlot(1);

              if (!itemstack1.isEmpty() && (itemstack1.getCount() > materialCost)) {
                itemstack1.shrink(materialCost);
                inputSlots.setInventorySlotContents(1, itemstack1);
              } else {
                inputSlots.setInventorySlotContents(1, ItemStack.EMPTY);
              }
            } else {
              inputSlots.setInventorySlotContents(1, ItemStack.EMPTY);
            }

            maximumCost = 0;
            IBlockState iblockstate = world.getBlockState(blockPosIn);
            Block block = iblockstate.getBlock();

            if (block == BrainStoneBlocks.brainStoneAnvil()) {
              breakChance *= BRAIN_STONE_FACTOR;
            } else if (block == BrainStoneBlocks.pulsatingBrainStoneAnvil()) {
              breakChance *= PULSATING_BRAIN_STONE_FACTOR;
            }

            if (!player.capabilities.isCreativeMode
                && !world.isRemote
                && (iblockstate.getBlock() == Blocks.ANVIL)
                && (player.getRNG().nextFloat() < breakChance)) {
              int l = iblockstate.getValue(BlockAnvil.DAMAGE).intValue();
              ++l;

              if (l > 2) {
                world.setBlockToAir(blockPosIn);
                world.playEvent(1029, blockPosIn, 0);
              } else {
                world.setBlockState(
                    blockPosIn, iblockstate.withProperty(BlockAnvil.DAMAGE, Integer.valueOf(l)), 2);
                world.playEvent(1030, blockPosIn, 0);
              }
            } else if (!world.isRemote) {
              world.playEvent(1030, blockPosIn, 0);
            }

            return stack;
          }
        });
  }

  @Override
  public boolean canInteractWith(EntityPlayer playerIn) {
    return isValidBlock(world.getBlockState(pos))
        ? (playerIn.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D)
        : false;
  }

  @Override
  public void updateRepairOutput() {
    ItemStack itemstack = inputSlots.getStackInSlot(0);
    maximumCost = 1;
    int i = 0;
    int j = 0;
    int k = 0;

    if (itemstack.isEmpty()) {
      outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
      maximumCost = 0;
    } else {
      ItemStack itemstack1 = itemstack.copy();
      ItemStack itemstack2 = inputSlots.getStackInSlot(1);
      Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemstack1);
      j = j + itemstack.getRepairCost() + (itemstack2.isEmpty() ? 0 : itemstack2.getRepairCost());
      materialCost = 0;
      boolean flag = false;

      if (!itemstack2.isEmpty()) {
        if (!net.minecraftforge.common.ForgeHooks.onAnvilChange(
            this, itemstack, itemstack2, outputSlot, repairedItemName, j)) return;
        flag =
            (itemstack2.getItem() == Items.ENCHANTED_BOOK)
                && !ItemEnchantedBook.getEnchantments(itemstack2).isEmpty();

        if (itemstack1.isItemStackDamageable()
            && itemstack1.getItem().getIsRepairable(itemstack, itemstack2)) {
          int l2 = Math.min(itemstack1.getItemDamage(), itemstack1.getMaxDamage() / 4);

          if (l2 <= 0) {
            outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
            maximumCost = 0;
            return;
          }

          int i3;

          for (i3 = 0; (l2 > 0) && (i3 < itemstack2.getCount()); ++i3) {
            int j3 = itemstack1.getItemDamage() - l2;
            itemstack1.setItemDamage(j3);
            ++i;
            l2 = Math.min(itemstack1.getItemDamage(), itemstack1.getMaxDamage() / 4);
          }

          materialCost = i3;
        } else {
          if (!flag
              && ((itemstack1.getItem() != itemstack2.getItem())
                  || !itemstack1.isItemStackDamageable())) {
            outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
            maximumCost = 0;
            return;
          }

          if (itemstack1.isItemStackDamageable() && !flag) {
            int l = itemstack.getMaxDamage() - itemstack.getItemDamage();
            int i1 = itemstack2.getMaxDamage() - itemstack2.getItemDamage();
            int j1 = i1 + ((itemstack1.getMaxDamage() * 12) / 100);
            int k1 = l + j1;
            int l1 = itemstack1.getMaxDamage() - k1;

            if (l1 < 0) {
              l1 = 0;
            }

            if (l1 < itemstack1.getMetadata()) {
              itemstack1.setItemDamage(l1);
              i += 2;
            }
          }

          Map<Enchantment, Integer> map1 = EnchantmentHelper.getEnchantments(itemstack2);
          boolean flag2 = false;
          boolean flag3 = false;

          for (Enchantment enchantment1 : map1.keySet()) {
            if (enchantment1 != null) {
              int i2 = map.containsKey(enchantment1) ? map.get(enchantment1).intValue() : 0;
              int j2 = map1.get(enchantment1).intValue();
              j2 = i2 == j2 ? j2 + 1 : Math.max(j2, i2);
              boolean flag1 = enchantment1.canApply(itemstack);

              if (player.capabilities.isCreativeMode
                  || (itemstack.getItem() == Items.ENCHANTED_BOOK)) {
                flag1 = true;
              }

              for (Enchantment enchantment : map.keySet()) {
                if ((enchantment != enchantment1) && !enchantment1.isCompatibleWith(enchantment)) {
                  flag1 = false;
                  ++i;
                }
              }

              if (!flag1) {
                flag3 = true;
              } else {
                flag2 = true;

                if (j2 > enchantment1.getMaxLevel()) {
                  j2 = enchantment1.getMaxLevel();
                }

                map.put(enchantment1, Integer.valueOf(j2));
                int k3 = 0;

                switch (enchantment1.getRarity()) {
                  case COMMON:
                    k3 = 1;
                    break;
                  case UNCOMMON:
                    k3 = 2;
                    break;
                  case RARE:
                    k3 = 4;
                    break;
                  case VERY_RARE:
                    k3 = 8;
                }

                if (flag) {
                  k3 = Math.max(1, k3 / 2);
                }

                i += k3 * j2;

                if (itemstack.getCount() > 1) {
                  i = 40;
                }
              }
            }
          }

          if (flag3 && !flag2) {
            outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
            maximumCost = 0;
            return;
          }
        }
      }

      if (StringUtils.isBlank(repairedItemName)) {
        if (itemstack.hasDisplayName()) {
          k = 1;
          i += k;
          itemstack1.clearCustomName();
        }
      } else if (!repairedItemName.equals(itemstack.getDisplayName())) {
        k = 1;
        i += k;
        itemstack1.setStackDisplayName(repairedItemName);
      }
      if (flag && !itemstack1.getItem().isBookEnchantable(itemstack1, itemstack2)) {
        itemstack1 = ItemStack.EMPTY;
      }

      maximumCost = j + i;

      if (i <= 0) {
        itemstack1 = ItemStack.EMPTY;
      }

      // No max checking!

      if (!itemstack1.isEmpty()) {
        int k2 = itemstack1.getRepairCost();

        if (!itemstack2.isEmpty() && (k2 < itemstack2.getRepairCost())) {
          k2 = itemstack2.getRepairCost();
        }

        if ((k != i) || (k == 0)) {
          k2 = (k2 * 2) + 1;
        }

        itemstack1.setRepairCost(k2);
        EnchantmentHelper.setEnchantments(map, itemstack1);
      }

      outputSlot.setInventorySlotContents(0, itemstack1);
      detectAndSendChanges();
    }
  }

  private Slot overrideSlot(int index, Slot slotIn) {
    inventorySlots.set(index, slotIn);
    slotIn.slotNumber = index;

    return slotIn;
  }

  private static boolean isValidBlock(IBlockState iblockstate) {
    return isValidBlock(iblockstate.getBlock());
  }

  private static boolean isValidBlock(Block block) {
    return block instanceof BlockBrainStoneAnvil;
  }
}
