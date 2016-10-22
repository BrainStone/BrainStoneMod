package brainstonemod.common.tileentity;

import brainstonemod.BrainStone;
import brainstonemod.common.block.BlockBrainStoneTrigger;
import brainstonemod.common.helper.BSP;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
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
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;

public class TileEntityBrainStoneTrigger extends TileEntity implements IInventory {
	private static final ArrayList<TileEntityBrainStoneTrigger> failedTileEntities = new ArrayList<>();
	private ItemStack inventory[];

	public static void retryFailedTileEntities() {
		for (TileEntityBrainStoneTrigger tileEntity : failedTileEntities) {
			try {
				tileEntity.fillMobTriggered();
			} catch (Exception e) {
				BSP.warnException(e);
			}
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	private final HashMap<String, Integer> mobTriggered;
	private byte delay, maxDelay, output, outputBuffered;

	private ItemStack oldStack;

	public TileEntityBrainStoneTrigger() {
		inventory = new ItemStack[1];
		mobTriggered = new HashMap<>();
		delay = 0;
		maxDelay = 4;
		output = 0;

		try {
			fillMobTriggered();
		} catch (NullPointerException e) {
			// Only catch the exception if we are on the client to put it in the
			// list of the failed ones.

			if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
				failedTileEntities.add(this);
			} else {
				throw e;
			}
		}
	}

	private void fillMobTriggered() {
		final int length = BrainStone.getSidedTiggerEntities().size();
		final String[] keys = BrainStone.getSidedTiggerEntities().keySet()
				.toArray(new String[length]);

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
	public void closeInventory() {
	}

	@Override
	public String getInventoryName() {
		return "container.brainstonetrigger";
	}

	public byte getMobPower(String mob) {
		byte tmp;

		if (mobTriggered.containsKey(mob)
				&& ((tmp = (byte) ((int) mobTriggered.get(mob))) > 0))
			return tmp;
		else
			return 0;
	}

	public boolean getMobTriggered(String s) {
        return mobTriggered.containsKey(s) && mobTriggered.get(s) > 0;
    }

	public IIcon getTextureId() {
		final ItemStack itemstack = inventory[0];

		if (itemstack == null)
			return BlockBrainStoneTrigger.textures[0];

		final Block block = Block.getBlockFromItem(itemstack.getItem());

		if (block == null)
			return BlockBrainStoneTrigger.textures[0];
		else
			return block.getIcon(1, itemstack.getItemDamage());
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	public void invertMobTriggered(String s) {
		if (mobTriggered.containsKey(s)) {
			mobTriggered.put(s, -1 * mobTriggered.get(s));
		}
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        if (!(itemstack.getItem() instanceof ItemBlock))
            return false;

        Block block = Block.getBlockFromItem(itemstack.getItem());
        return block != null && !(block == BrainStone.brainStoneTrigger() || block == Blocks.leaves) && block.isOpaqueCube();

    }

	@Override
	public void openInventory() {
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		NBTTagList list = (NBTTagList) compound.getTag("Items");
		if (list != null) {
			for (int i = 0; i < list.tagCount(); i++) {
				NBTTagCompound item = list.getCompoundTagAt(i);//list.get(i)
				int slot = item.getByte("Slot");
				if (slot >= 0 && slot < getSizeInventory()) {
					setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(item));
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
				mobTriggered.put(compound.getString(trigger + "Key"),
						compound.getInteger(trigger));
			} catch (final Exception e) {
				try {
					mobTriggered.put(compound.getString(trigger + "Key"),
							compound.getBoolean(trigger) ? 15 : 1);
				} catch (final Exception e1) {
					try {
						mobTriggered.put(
								compound.getString(trigger + "Key"),
								(int) (compound.getByte(trigger)));
					} catch (final Exception e2) {
						mobTriggered.put(
								compound.getString(trigger + "Key"), 15);
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
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		NBTTagList list = new NBTTagList();
		for (int i = 0; i < getSizeInventory(); i++) {
			ItemStack is = getStackInSlot(i);
			if (is != null) {
				NBTTagCompound item = new NBTTagCompound();

				item.setByte("Slot", (byte) i);
				is.writeToNBT(item);

				list.appendTag(item);
			}
		}
		compound.setTag("Items", list);

		final int length = BrainStone.getSidedTiggerEntities().size();
		compound.setInteger("TriggerSize", length);
		final String[] keys = BrainStone.getSidedTiggerEntities().keySet()
				.toArray(new String[length]);
		String trigger, key;

		for (int i = 0; i < length; i++) {
			key = keys[i];
			trigger = "Trigger" + String.valueOf(i);

			compound.setString(trigger + "Key", key);
			compound.setInteger(trigger, mobTriggered.get(key));
		}

		compound.setByte("BrainStoneDelay", delay);
		compound.setByte("BrainStoneMaxDelay", maxDelay);
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
	public Packet getDescriptionPacket() {
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, getBlockMetadata(), getUpdateTag());
	}

	public NBTTagCompound getUpdateTag(){
		NBTTagCompound compound = new NBTTagCompound();
		writeToNBT(compound);
		return compound;
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		readFromNBT(pkt.func_148857_g());//func_148857_g=getNBTCompound()
	}

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
		for (final ItemStack itemstack : inventory) {
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
	public int getSizeInventory() {
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inventory[slot];
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
		inventory[slot] = itemstack;

		if ((itemstack != null) && (itemstack.stackSize > getInventoryStackLimit())) {
			itemstack.stackSize = getInventoryStackLimit();
		}

		markDirty();
	}
}
