package brainstonemod.common.tileentity;

import java.util.ArrayList;
import java.util.HashMap;

import brainstonemod.BrainStone;
import brainstonemod.BrainStoneBlocks;
import brainstonemod.common.helper.BSP;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class TileEntityBrainStoneTrigger extends TileEntity implements IInventory {
	private static final ArrayList<TileEntityBrainStoneTrigger> failedTileEntities = new ArrayList<>();

	private NonNullList<ItemStack> inventory;

	private final HashMap<String, Integer> mobTriggered;
	private byte delay, maxDelay, output, outputBuffered;

	private ItemStack oldStack;
	
	public static void retryFailedTileEntities() {
		for (TileEntityBrainStoneTrigger tileEntity : failedTileEntities) {
			try {
				tileEntity.fillMobTriggered();
			} catch (Exception e) {
				BSP.warnException(e);
			}
		}
	}

	public TileEntityBrainStoneTrigger() {
		inventory = NonNullList.withSize(1, ItemStack.EMPTY);
		mobTriggered = new HashMap<>();
		delay = 0;
		maxDelay = 4;
		output = 0;
		oldStack = ItemStack.EMPTY;

		try {
			fillMobTriggered();
		} catch (NullPointerException e) {
			// Only catch the exception if we are on the client to put it in the
			// list of the failed ones.

			if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
				failedTileEntities.add(this);
			} else
				throw e;
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	private void fillMobTriggered() {
		final int length = BrainStone.getSidedTriggerEntities().size();
		final String[] keys = BrainStone.getSidedTriggerEntities().keySet().toArray(new String[length]);

		for (int i = 0; i < length; i++) {
			mobTriggered.put(keys[i], 15);
		}
	}

	public boolean checkForSlotChange() {
		final ItemStack oldOldStack = oldStack;
		oldStack = getStackInSlot(0);

		return !ItemStack.areItemStacksEqual(oldOldStack, oldStack);
	}

	@Override
	public String getName() {
		return "container.brainstonetrigger";
	}

	public byte getMobPower(String mob) {
		byte tmp;

		if (mobTriggered.containsKey(mob) && ((tmp = (byte) ((int) mobTriggered.get(mob))) > 0))
			return tmp;
		else
			return 0;
	}

	public boolean getMobTriggered(String s) {
		return mobTriggered.containsKey(s) && (mobTriggered.get(s) > 0);
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	public void invertMobTriggered(String s) {
		if (mobTriggered.containsKey(s)) {
			mobTriggered.put(s, -1 * mobTriggered.get(s));
		}
	}

	public void disableAllMobs() {
		mobTriggered.keySet().stream().filter(mob -> mobTriggered.get(mob) > 0)
				.forEach(mob -> mobTriggered.put(mob, -1 * mobTriggered.get(mob)));
	}

	public void enableAllMobs() {
		mobTriggered.keySet().stream().filter(mob -> mobTriggered.get(mob) < 0)
				.forEach(mob -> mobTriggered.put(mob, -1 * mobTriggered.get(mob)));
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		if (!(itemstack.getItem() instanceof ItemBlock))
			return false;

		Block block = Block.getBlockFromItem(itemstack.getItem());
		return (block != null) && !((block == BrainStoneBlocks.brainStoneTrigger()) || (block == Blocks.LEAVES))
				&& block.getDefaultState().isOpaqueCube() && block.getDefaultState().isFullCube();

	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		// Do nothing
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		// Do nothing
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		NBTTagList list = (NBTTagList) compound.getTag("Items");
		if (list != null) {
			for (int i = 0; i < list.tagCount(); i++) {
				NBTTagCompound item = list.getCompoundTagAt(i);// list.get(i)
				int slot = item.getByte("Slot");

				if ((slot >= 0) && (slot < getSizeInventory())) {
					setInventorySlotContents(slot, new ItemStack(item));
				}
			}
		} else {
			BSP.error("List was null when reading TileEntityBrainStoneTrigger NBTTagCompound");
		}

		final int length = compound.getInteger("TriggerSize");
		String trigger;

		for (int i = 0; i < length; i++) {
			trigger = "Trigger" + String.valueOf(i);

			try {
				mobTriggered.put(compound.getString(trigger + "Key"), compound.getInteger(trigger));
			} catch (final Exception e) {
				try {
					mobTriggered.put(compound.getString(trigger + "Key"), compound.getBoolean(trigger) ? 15 : 1);
				} catch (final Exception e1) {
					try {
						mobTriggered.put(compound.getString(trigger + "Key"), (int) (compound.getByte(trigger)));
					} catch (final Exception e2) {
						mobTriggered.put(compound.getString(trigger + "Key"), 15);
					}
				}
			}
		}

		delay = compound.getByte("BrainStoneDelay");
		maxDelay = compound.getByte("BrainStoneMaxDelay");
	}

	// DOCME
	public void setMobTriggered(String s, int value) {
		if (mobTriggered.containsKey(s)) {
			mobTriggered.put(s, value);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		NBTTagList list = new NBTTagList();
		for (int i = 0; i < getSizeInventory(); i++) {
			ItemStack is = getStackInSlot(i);
			if (!is.isEmpty()) {
				NBTTagCompound item = new NBTTagCompound();

				item.setByte("Slot", (byte) i);
				is.writeToNBT(item);

				list.appendTag(item);
			}
		}
		compound.setTag("Items", list);

		final int length = BrainStone.getSidedTriggerEntities().size();
		compound.setInteger("TriggerSize", length);
		final String[] keys = BrainStone.getSidedTriggerEntities().keySet().toArray(new String[length]);
		String trigger, key;

		for (int i = 0; i < length; i++) {
			key = keys[i];
			trigger = "Trigger" + String.valueOf(i);

			compound.setString(trigger + "Key", key);
			if (mobTriggered.get(key) != null) {
				compound.setInteger(trigger, mobTriggered.get(key));
			}
		}

		compound.setByte("BrainStoneDelay", delay);
		compound.setByte("BrainStoneMaxDelay", maxDelay);

		return compound;
	}

	public byte getDelay() {
		return delay;
	}

	public void setDelay(byte delay) {
		this.delay = delay;
	}

	public byte getMaxDelay() {
		return maxDelay;
	}

	public void setMaxDelay(byte maxDelay) {
		this.maxDelay = maxDelay;
	}

	public byte getOutput() {
		return output;
	}

	public void setOutput(byte output) {
		this.output = output;
	}

	public byte getOutputBuffered() {
		return outputBuffered;
	}

	public void setOutputBuffered(byte outputBuffered) {
		this.outputBuffered = outputBuffered;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(pos, getBlockMetadata(), getUpdateTag());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack is = getStackInSlot(index);

		if (!is.isEmpty()) {
			if (is.getCount() <= count) {
				setInventorySlotContents(index, ItemStack.EMPTY);
			} else {
				is = is.splitStack(count);
				markDirty();
			}
		}

		return is;
	}

	// DOCME
	public void dropItems(World world, int x, int y, int z) {
		for (final ItemStack itemstack : inventory) {
			if (!itemstack.isEmpty()) {
				final float f = 0.7F;
				final double dx = (world.rand.nextFloat() * f) + ((1.0F - f) * 0.5D);
				final double dy = (world.rand.nextFloat() * f) + ((1.0F - f) * 0.5D);
				final double dz = (world.rand.nextFloat() * f) + ((1.0F - f) * 0.5D);
				final EntityItem entityitem = new EntityItem(world, x + dx, y + dy, z + dz, itemstack);
				entityitem.setPickupDelay(10);
				world.spawnEntity(entityitem);
			}
		}
	}

	@Override
	public int getSizeInventory() {
		return inventory.size();
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inventory.get(slot);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack is = getStackInSlot(index);
		setInventorySlotContents(index, null);

		markDirty();

		return is;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64;
	}

	@Override
	public void openInventory(EntityPlayer player) {
		// Do nothing
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		// Do nothing
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemstack) {
		inventory.set(slot, itemstack);

		if (!itemstack.isEmpty() && (itemstack.getCount() > getInventoryStackLimit())) {
			itemstack.setCount(getInventoryStackLimit());
		}

		markDirty();
	}

	@Override
	public boolean isEmpty() {
		return inventory.stream().allMatch(ItemStack::isEmpty);
	}
}
