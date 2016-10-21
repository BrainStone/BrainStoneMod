package brainstonemod.common.tileentity;

import brainstonemod.BrainStone;
import brainstonemod.common.block.BlockBrainStoneTrigger;
import brainstonemod.common.helper.BSP;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IIcon;

import java.util.ArrayList;
import java.util.HashMap;

public class TileEntityBrainStoneTrigger extends
		TileEntityBrainStoneHiders {
	private static final ArrayList<TileEntityBrainStoneTrigger> failedTileEntities = new ArrayList<TileEntityBrainStoneTrigger>();

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
		ItemStacks = new ItemStack[1];
		mobTriggered = new HashMap<String, Integer>();
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
		if (mobTriggered.containsKey(s))
			return mobTriggered.get(s) > 0;
		else
			return false;
	}

	public IIcon getTextureId() {
		final ItemStack itemstack = ItemStacks[0];

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

		final Block block = Block.getBlockFromItem(itemstack.getItem());
		if (block == null)
			return false;

		if (block == BrainStone.brainStoneTrigger() || block == Blocks.leaves)
			return false;
		else
			return block.isOpaqueCube();
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);

			final NBTTagList nbttaglist = nbttagcompound
					.getTagList("Items", 10);

			if ((nbttaglist != null) && (nbttaglist.tagCount() > 0)) {
				ItemStacks = new ItemStack[getSizeInventory()];

				for (int i = 0; i < nbttaglist.tagCount(); i++) {
					final NBTTagCompound nbttagcompound1 = nbttaglist
							.getCompoundTagAt(i);
					final byte byte0 = nbttagcompound1.getByte("Slot");

					if ((byte0 >= 0) && (byte0 < ItemStacks.length)) {
						ItemStacks[byte0] = ItemStack
								.loadItemStackFromNBT(nbttagcompound1);
					}
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
		maxDelay = nbttagcompound.getByte("BrainStoneMaxDelay");
	}

	// DOCME
	public void setMobTriggered(String s, int value) {
		if (mobTriggered.containsKey(s)) {
			mobTriggered.put(s, value);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);

			final NBTTagList nbttaglist = new NBTTagList();

			for (int i = 0; i < ItemStacks.length; i++) {
				if (ItemStacks[i] != null) {
					final NBTTagCompound nbttagcompound1 = new NBTTagCompound();
					nbttagcompound1.setByte("Slot", (byte) i);
					ItemStacks[i].writeToNBT(nbttagcompound1);
					nbttaglist.appendTag(nbttagcompound1);
				}
			}

			nbttagcompound.setTag("Items", nbttaglist);

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
		nbttagcompound.setByte("BrainStoneMaxDelay", maxDelay);
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
}
