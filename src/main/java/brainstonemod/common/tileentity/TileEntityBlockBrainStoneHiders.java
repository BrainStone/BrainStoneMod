package brainstonemod.common.tileentity;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import brainstonemod.common.tileentity.template.TileEntityBrainStoneSyncBase;

public abstract class TileEntityBlockBrainStoneHiders extends
		TileEntityBrainStoneSyncBase implements IInventory {
	protected ItemStack ItemStacks[];

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if (ItemStacks[slot] != null) {
			if (ItemStacks[slot].stackSize <= amount) {
				final ItemStack itemstack = ItemStacks[slot];
				ItemStacks[slot] = null;
				return itemstack;
			}

			final ItemStack itemstack1 = ItemStacks[slot].splitStack(amount);

			if (ItemStacks[slot].stackSize == 0) {
				ItemStacks[slot] = null;
			}

			return itemstack1;
		} else
			return null;
	}

	// DOCME
	public void dropItems(World world, int x, int y, int z) {
		for (final ItemStack itemstack : ItemStacks) {
			if (itemstack != null) {
				final float f = 0.7F;
				final double dx = (world.rand.nextFloat() * f)
						+ ((1.0F - f) * 0.5D);
				final double dy = (world.rand.nextFloat() * f)
						+ ((1.0F - f) * 0.5D);
				final double dz = (world.rand.nextFloat() * f)
						+ ((1.0F - f) * 0.5D);
				final EntityItem entityitem = new EntityItem(world, x + dx, y
						+ dy, z + dz, itemstack);
				entityitem.delayBeforeCanPickup = 10;
				world.spawnEntityInWorld(entityitem);
			}
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public int getSizeInventory() {
		return ItemStacks.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return ItemStacks[slot];
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		// if (ItemStacks[slot] != null) {
		// final ItemStack itemstack = ItemStacks[slot];
		// ItemStacks[slot] = null;
		// return itemstack;
		// } else
		// return null;

		return null;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		if (worldObj.getTileEntity(xCoord, yCoord, zCoord) != this)
			return false;
		else
			return entityplayer.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D,
					zCoord + 0.5D) <= 64D;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemstack) {
		ItemStacks[slot] = itemstack;

		if ((itemstack != null)
				&& (itemstack.stackSize > getInventoryStackLimit())) {
			itemstack.stackSize = getInventoryStackLimit();
		}
	}
}
