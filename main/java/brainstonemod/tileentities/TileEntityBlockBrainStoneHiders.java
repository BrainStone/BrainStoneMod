package brainstonemod.tileentities;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import brainstonemod.templates.TileEntityBrainStoneSyncBase;

public abstract class TileEntityBlockBrainStoneHiders extends
		TileEntityBrainStoneSyncBase implements IInventory {
	protected ItemStack ItemStacks[];

	public TileEntityBlockBrainStoneHiders() {
	}

	@Override
	public void closeChest() {
	}

	/**
	 * Decrease the size of the stack in slot (first int arg) by the amount of
	 * the second int arg. Returns the new stack.
	 */
	@Override
	public ItemStack decrStackSize(int i, int j) {
		if (ItemStacks[i] != null) {
			if (ItemStacks[i].stackSize <= j) {
				final ItemStack itemstack = ItemStacks[i];
				ItemStacks[i] = null;
				return itemstack;
			}

			final ItemStack itemstack1 = ItemStacks[i].splitStack(j);

			if (ItemStacks[i].stackSize == 0) {
				ItemStacks[i] = null;
			}

			return itemstack1;
		} else
			return null;
	}

	public void dropItems(World world, int i, int j, int k) {
		for (int l = 0; l < ItemStacks.length; l++) {
			final ItemStack itemstack = ItemStacks[l];

			if (itemstack != null) {
				final float f = 0.7F;
				final double d = (world.rand.nextFloat() * f)
						+ ((1.0F - f) * 0.5D);
				final double d1 = (world.rand.nextFloat() * f)
						+ ((1.0F - f) * 0.5D);
				final double d2 = (world.rand.nextFloat() * f)
						+ ((1.0F - f) * 0.5D);
				final EntityItem entityitem = new EntityItem(world, i + d, j
						+ d1, k + d2, itemstack);
				entityitem.delayBeforeCanPickup = 10;
				world.spawnEntityInWorld(entityitem);
			}
		}
	}

	/**
	 * Returns the maximum stack size for a inventory slot. Seems to always be
	 * 64, possibly will be extended. *Isn't this more of a set than a get?*
	 */
	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	/**
	 * Returns the number of slots in the inventory.
	 */
	@Override
	public int getSizeInventory() {
		return ItemStacks.length;
	}

	/**
	 * Returns the stack in slot i
	 */
	@Override
	public ItemStack getStackInSlot(int i) {
		return ItemStacks[i];
	}

	/**
	 * When some containers are closed they call this on each slot, then drop
	 * whatever it returns as an EntityItem - like when you close a workbench
	 * GUI.
	 */
	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		if (ItemStacks[i] != null) {
			final ItemStack itemstack = ItemStacks[i];
			ItemStacks[i] = null;
			return itemstack;
		} else
			return null;
	}

	/**
	 * Do not make give this method the name canInteractWith because it clashes
	 * with Container
	 */
	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
			return false;
		else
			return entityplayer.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D,
					zCoord + 0.5D) <= 64D;
	}

	@Override
	public void openChest() {
	}

	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be
	 * crafting or armor sections).
	 */
	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		ItemStacks[i] = itemstack;

		if ((itemstack != null)
				&& (itemstack.stackSize > this.getInventoryStackLimit())) {
			itemstack.stackSize = this.getInventoryStackLimit();
		}
	}

	/**
	 * Writes a tile entity to NBT.
	 */
	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
	}
}
