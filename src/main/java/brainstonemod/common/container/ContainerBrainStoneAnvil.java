package brainstonemod.common.container;

import java.util.Map;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import brainstonemod.common.block.BlockBrainStoneAnvil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerBrainStoneAnvil extends ContainerRepair {
	@SideOnly(Side.CLIENT)
	public ContainerBrainStoneAnvil(InventoryPlayer inventoryIn, World worldIn, EntityPlayer thePlayer) {
		this(inventoryIn, worldIn, BlockPos.ORIGIN, thePlayer);
	}

	public ContainerBrainStoneAnvil(InventoryPlayer playerInventory, final World world, final BlockPos blockPosIn,
			EntityPlayer player) {
		super(playerInventory, world, blockPosIn, player);

		inventorySlots.set(2, new Slot(outputSlot, 2, 134, 47) {
			@Override
			public boolean isItemValid(@Nullable ItemStack stack) {
				return false;
			}

			@Override
			public boolean canTakeStack(EntityPlayer stack) {
				return (stack.capabilities.isCreativeMode || (stack.experienceLevel >= maximumCost))
						&& (maximumCost > 0) && getHasStack();
			}

			@Override
			public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
				if (!playerIn.capabilities.isCreativeMode) {
					playerIn.addExperienceLevel(-maximumCost);
				}

				float breakChance = net.minecraftforge.common.ForgeHooks.onAnvilRepair(playerIn, stack,
						inputSlots.getStackInSlot(0), inputSlots.getStackInSlot(1));

				inputSlots.setInventorySlotContents(0, (ItemStack) null);

				if (materialCost > 0) {
					ItemStack itemstack = inputSlots.getStackInSlot(1);

					if ((itemstack != null) && (itemstack.stackSize > materialCost)) {
						itemstack.stackSize -= materialCost;
						inputSlots.setInventorySlotContents(1, itemstack);
					} else {
						inputSlots.setInventorySlotContents(1, (ItemStack) null);
					}
				} else {
					inputSlots.setInventorySlotContents(1, (ItemStack) null);
				}

				maximumCost = 0;
				IBlockState iblockstate = world.getBlockState(blockPosIn);

				if (!playerIn.capabilities.isCreativeMode && !world.isRemote && isValidBlock(iblockstate)
						&& (playerIn.getRNG().nextFloat() < (breakChance / 20.0f))) {
					int l = iblockstate.getValue(BlockAnvil.DAMAGE).intValue();
					++l;

					if (l > 2) {
						world.setBlockToAir(blockPosIn);
						world.playEvent(1029, blockPosIn, 0);
					} else {
						world.setBlockState(blockPosIn, iblockstate.withProperty(BlockAnvil.DAMAGE, Integer.valueOf(l)),
								2);
						world.playEvent(1030, blockPosIn, 0);
					}
				} else if (!world.isRemote) {
					world.playEvent(1030, blockPosIn, 0);
				}
			}
		});

	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return isValidBlock(theWorld.getBlockState(selfPosition)) ? (playerIn.getDistanceSq(selfPosition.getX() + 0.5D,
				selfPosition.getY() + 0.5D, selfPosition.getZ() + 0.5D) <= 64.0D) : false;
	}

	@Override
	public void updateRepairOutput() {
		ItemStack itemstack = inputSlots.getStackInSlot(0);
		maximumCost = 1;
		int i = 0;
		int j = 0;
		int k = 0;

		if (itemstack == null) {
			outputSlot.setInventorySlotContents(0, (ItemStack) null);
			maximumCost = 0;
		} else {
			ItemStack itemstack1 = itemstack.copy();
			ItemStack itemstack2 = inputSlots.getStackInSlot(1);
			Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemstack1);
			j = j + itemstack.getRepairCost() + (itemstack2 == null ? 0 : itemstack2.getRepairCost());
			materialCost = 0;
			boolean flag = false;

			if (itemstack2 != null) {
				if (!net.minecraftforge.common.ForgeHooks.onAnvilChange(this, itemstack, itemstack2, outputSlot,
						repairedItemName, j))
					return;
				flag = (itemstack2.getItem() == Items.ENCHANTED_BOOK)
						&& !Items.ENCHANTED_BOOK.getEnchantments(itemstack2).hasNoTags();

				if (itemstack1.isItemStackDamageable() && itemstack1.getItem().getIsRepairable(itemstack, itemstack2)) {
					int j2 = Math.min(itemstack1.getItemDamage(), itemstack1.getMaxDamage() / 4);

					if (j2 <= 0) {
						outputSlot.setInventorySlotContents(0, (ItemStack) null);
						maximumCost = 0;
						return;
					}

					int k2;

					for (k2 = 0; (j2 > 0) && (k2 < itemstack2.stackSize); ++k2) {
						int l2 = itemstack1.getItemDamage() - j2;
						itemstack1.setItemDamage(l2);
						++i;
						j2 = Math.min(itemstack1.getItemDamage(), itemstack1.getMaxDamage() / 4);
					}

					materialCost = k2;
				} else {
					if (!flag && ((itemstack1.getItem() != itemstack2.getItem())
							|| !itemstack1.isItemStackDamageable())) {
						outputSlot.setInventorySlotContents(0, (ItemStack) null);
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

					for (Enchantment enchantment1 : map1.keySet()) {
						if (enchantment1 != null) {
							int i3 = map.containsKey(enchantment1) ? map.get(enchantment1).intValue() : 0;
							int j3 = map1.get(enchantment1).intValue();
							j3 = i3 == j3 ? j3 + 1 : Math.max(j3, i3);
							boolean flag1 = enchantment1.canApply(itemstack);

							if (thePlayer.capabilities.isCreativeMode
									|| (itemstack.getItem() == Items.ENCHANTED_BOOK)) {
								flag1 = true;
							}

							for (Enchantment enchantment : map.keySet()) {
								// Forge BugFix: Let Both enchantments veto
								// being together
								if ((enchantment != enchantment1) && !(enchantment1.canApplyTogether(enchantment)
										&& enchantment.canApplyTogether(enchantment1))) {
									flag1 = false;
									++i;
								}
							}

							if (flag1) {
								if (j3 > enchantment1.getMaxLevel()) {
									j3 = enchantment1.getMaxLevel();
								}

								map.put(enchantment1, Integer.valueOf(j3));
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

								i += k3 * j3;
							}
						}
					}
				}
			}

			if (flag && !itemstack1.getItem().isBookEnchantable(itemstack1, itemstack2)) {
				itemstack1 = null;
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

			maximumCost = j + i;

			if (i <= 0) {
				itemstack1 = null;
			}
			
			// No max checking!

			if (itemstack1 != null) {
				int i2 = itemstack1.getRepairCost();

				if ((itemstack2 != null) && (i2 < itemstack2.getRepairCost())) {
					i2 = itemstack2.getRepairCost();
				}

				if ((k != i) || (k == 0)) {
					i2 = (i2 * 2) + 1;
				}

				itemstack1.setRepairCost(i2);
				EnchantmentHelper.setEnchantments(map, itemstack1);
			}

			outputSlot.setInventorySlotContents(0, itemstack1);
			detectAndSendChanges();
		}
	}

	private static boolean isValidBlock(IBlockState iblockstate) {
		return isValidBlock(iblockstate.getBlock());
	}

	private static boolean isValidBlock(Block block) {
		return block instanceof BlockBrainStoneAnvil;
	}
}
