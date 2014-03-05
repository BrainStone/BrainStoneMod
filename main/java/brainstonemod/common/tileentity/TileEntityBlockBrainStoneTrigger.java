package brainstonemod.common.tileentity;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import brainstonemod.BrainStone;
import brainstonemod.common.block.BlockBrainStoneTrigger;
import brainstonemod.common.slot.SlotBlockBrainStoneTrigger;
import brainstonemod.network.BrainStonePacketHelper;

public class TileEntityBlockBrainStoneTrigger extends
		TileEntityBlockBrainStoneHiders implements IInventory {
	private final HashMap<String, Integer> mobTriggered;
	public byte delay, max_delay, output, output_buffered;
	private ItemStack oldStack;

	public TileEntityBlockBrainStoneTrigger() {
		ItemStacks = new ItemStack[1];
		mobTriggered = new HashMap<String, Integer>();
		delay = 0;
		max_delay = 4;
		output = 0;

		final int length = BrainStone.getSidedTiggerEntities().size();
		final String[] keys = BrainStone.getSidedTiggerEntities().keySet()
				.toArray(new String[length]);

		for (int i = 0; i < length; i++) {
			mobTriggered.put(keys[i], 15);
		}
	}

	public boolean checkForSlotChange() {
		return oldStack != (oldStack = getStackInSlot(0));
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public void dropItems(World world, int i, int j, int k) {
		for (final ItemStack itemstack : ItemStacks) {
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

	// @Override
	// public Packet getDescriptionPacket() {
	// final NBTTagCompound tag = new NBTTagCompound();
	// writeToNBT(tag);
	// return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, tag);
	// }

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
		if (mobTriggered.containsKey(s))
			return mobTriggered.get(s) > 0;
		else
			return false;
	}

	public IIcon getTextureId(IBlockAccess iblockaccess, int i, int j, int k) {
		final ItemStack itemstack = ItemStacks[0];

		if (itemstack == null)
			return BlockBrainStoneTrigger.textures[0];

		final Block block = Block.getBlockFromItem(itemstack.getItem());

		if (block == null)
			return BlockBrainStoneTrigger.textures[0];
		else
			return block.getIcon(iblockaccess, i, j, k, 1);
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
		return SlotBlockBrainStoneTrigger.staticIsItemValid(itemstack);
	}

	@Override
	public void openInventory() {
	}

	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		final NBTTagList nbttaglist = nbttagcompound
				.getTagList("ItemsBrainStoneTrigger", 0);
		ItemStacks = new ItemStack[getSizeInventory()];

		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			final NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist
					.getCompoundTagAt(i);
			final byte byte0 = nbttagcompound1.getByte("SlotBrainStoneTrigger");

			if ((byte0 >= 0) && (byte0 < ItemStacks.length)) {
				ItemStacks[byte0] = ItemStack
						.loadItemStackFromNBT(nbttagcompound1);
			}
		}

		final int length = nbttagcompound.getInteger("TriggerSize");
		String trigger;

		for (int i = 0; i < length; i++) {
			trigger = "Trigger" + String.valueOf(i);

			try {
				mobTriggered.put(nbttagcompound.getString(trigger + "Key"),
						nbttagcompound.getInteger(trigger));
			} catch (final Exception e) {
				try {
					mobTriggered.put(nbttagcompound.getString(trigger + "Key"),
							nbttagcompound.getBoolean(trigger) ? 15 : 1);
				} catch (final Exception e1) {
					try {
						mobTriggered.put(
								nbttagcompound.getString(trigger + "Key"),
								(int) (nbttagcompound.getByte(trigger)));
					} catch (final Exception e2) {
						mobTriggered.put(
								nbttagcompound.getString(trigger + "Key"), 15);
					}
				}
			}
		}

		delay = nbttagcompound.getByte("BrainStoneDelay");
		max_delay = nbttagcompound.getByte("BrainStoneMaxDelay");
	}

	public void setMobTriggered(String s, int value) {
		if (mobTriggered.containsKey(s)) {
			mobTriggered.put(s, value);
		}
	}

	/**
	 * Writes a tile entity to NBT.
	 */
	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		final NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < ItemStacks.length; i++) {
			if (ItemStacks[i] != null) {
				final NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("SlotBrainStoneTrigger", (byte) i);
				ItemStacks[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		nbttagcompound.setTag("ItemsBrainStoneTrigger", nbttaglist);

		final int length = BrainStone.getSidedTiggerEntities().size();
		nbttagcompound.setInteger("TriggerSize", length);
		final String[] keys = BrainStone.getSidedTiggerEntities().keySet()
				.toArray(new String[length]);
		String trigger, key;

		for (int i = 0; i < length; i++) {
			key = keys[i];
			trigger = "Trigger" + String.valueOf(i);

			nbttagcompound.setString(trigger + "Key", key);
			nbttagcompound.setInteger(trigger, mobTriggered.get(key));
		}

		nbttagcompound.setByte("BrainStoneDelay", delay);
		nbttagcompound.setByte("BrainStoneMaxDelay", max_delay);
	}
}
