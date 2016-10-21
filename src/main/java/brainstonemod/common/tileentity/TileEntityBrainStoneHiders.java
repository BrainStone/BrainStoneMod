package brainstonemod.common.tileentity;

import brainstonemod.common.tileentity.template.TileEntityBrainStoneSyncBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class TileEntityBrainStoneHiders extends TileEntityBrainStoneSyncBase implements IInventory {
	protected ItemStack ItemStacks[];

	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack is = getStackInSlot(index);
		if (is != null) {
			if (is.stackSize <= count) {
				setInventorySlotContents(index, null);
			} else {
				is = is.splitStack(count);
				markDirty();
			}
		}
		return is;
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
	public ItemStack getStackInSlotOnClosing(int index) {
		ItemStack is = getStackInSlot(index);
		setInventorySlotContents(index, null);
		return is;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return player.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemstack) {
		ItemStacks[slot] = itemstack;

		if ((itemstack != null) && (itemstack.stackSize > getInventoryStackLimit())) {
			itemstack.stackSize = getInventoryStackLimit();
		}

		markDirty();
	}
}
