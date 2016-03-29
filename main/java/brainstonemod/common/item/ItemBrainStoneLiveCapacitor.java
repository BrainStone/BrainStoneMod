package brainstonemod.common.item;

import java.util.List;

import org.lwjgl.input.Keyboard;

import brainstonemod.BrainStone;
import brainstonemod.common.item.template.ItemBrainStoneBase;
import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import crazypants.enderio.machine.power.PowerDisplayUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class ItemBrainStoneLiveCapacitor extends ItemBrainStoneBase implements IEnergyContainerItem {
	public static final int MaxDamage = 32;
	public static final int RFperHalfHeart = 1000000;

	public ItemBrainStoneLiveCapacitor() {
		super();

		setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.tabTools));
		setMaxDamage(MaxDamage);
		setMaxStackSize(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack container, int pass) {
		return getEnergyStored(container) >= RFperHalfHeart;
	}

	@Override
	public EnumRarity getRarity(ItemStack container) {
		return EnumRarity.epic;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs creativeTab, List list) {
		ItemStack is = new ItemStack(this);
		setCapacityLevel(is, 1);
		setChargingLevel(is, 1);
		setEnergy(is, 0);
		list.add(is);

		int levels[] = { 1, 2, 3, 5, 9, 19, 99 };

		for (int i : levels) {
			is = new ItemStack(this);
			setCapacityLevel(is, i);
			setChargingLevel(is, i);
			setEnergy(is, getMaxEnergyStored(is));
			list.add(is);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack container, EntityPlayer player, List list, boolean advancedToolTipInfo) {
		boolean sneak = Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode());

		if (sneak) {
			list.add(EnumChatFormatting.GREEN + "Absorbs all damage while powered.");
			list.add(EnumChatFormatting.YELLOW + "Costs " + PowerDisplayUtil.formatPower(RFperHalfHeart * 2) + " "
					+ PowerDisplayUtil.abrevation() + "/" + EnumChatFormatting.DARK_RED + "\u2764");
			list.add("Capacity Level: " + EnumChatFormatting.GOLD + EnumChatFormatting.BOLD
					+ String.valueOf(getCapacityLevel(container)) + EnumChatFormatting.RESET + EnumChatFormatting.GRAY
					+ " (" + PowerDisplayUtil.formatPower(getMaxEnergyStored(container)) + " "
					+ PowerDisplayUtil.abrevation() + " " + EnumChatFormatting.DARK_RED
					+ ((getCapacityLevel(container) + 1) * 5) + "\u2764" + EnumChatFormatting.GRAY + ")");
			list.add("Charging Level: " + EnumChatFormatting.GOLD + EnumChatFormatting.BOLD
					+ String.valueOf(getChargingLevel(container)) + EnumChatFormatting.RESET + EnumChatFormatting.GRAY
					+ " (" + PowerDisplayUtil.formatPower(getMaxRecieve(container)) + " "
					+ PowerDisplayUtil.abrevation() + "/t)");
		} else {
			list.add("Hold " + EnumChatFormatting.YELLOW + EnumChatFormatting.ITALIC
					+ Keyboard.getKeyName(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode())
					+ EnumChatFormatting.RESET + EnumChatFormatting.GRAY + " for details");
		}

		list.add(PowerDisplayUtil.formatPower(getEnergyStored(container)) + "/"
				+ PowerDisplayUtil.formatPower(getMaxEnergyStored(container)) + " " + PowerDisplayUtil.abrevation());
	}

	@Override
	public void onCreated(ItemStack container, World world, EntityPlayer entityPlayer) {
		// Init NBT if new and keep old NBT
		getEnergyStored(container);
		getCapacityLevel(container);
		getChargingLevel(container);

		updateDamage(container);
	}

	public float handleDamage(ItemStack container, float damage) {
		return handleDamage(container, damage, false);
	}

	public float handleDamage(ItemStack container, float damage, boolean simulate) {
		int energyCost = (int) (damage * RFperHalfHeart);

		return (float) (energyCost - extractEnergyIntern(container, energyCost, simulate)) / (float) RFperHalfHeart;
	}

	public void healPlayer(ItemStack container, EntityPlayer player) {
		if (getEnergyStored(container) < RFperHalfHeart)
			return;

		double heal = Math.floor(player.getMaxHealth() - player.getHealth());
		int energy = (int) (heal * RFperHalfHeart);

		int energySpent = extractEnergyIntern(container, energy, false);

		player.heal((float) energySpent / (float) RFperHalfHeart);
	}

	public int getCapacityLevel(ItemStack container) {
		if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("LevelCapacity")) {
			setCapacityLevel(container, 1);
		}

		return container.stackTagCompound.getInteger("LevelCapacity");
	}

	public ItemStack upgradeCapacity(ItemStack container) {
		final int capacity = getCapacityLevel(container);
		final int charging = getChargingLevel(container);

		if (capacity < (charging * 2)) {
			setCapacityLevel(container, capacity + 1);

			return container;
		} else {
			return null;
		}
	}

	public int getChargingLevel(ItemStack container) {
		if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("LevelCharging")) {
			setChargingLevel(container, 1);
		}

		return container.stackTagCompound.getInteger("LevelCharging");
	}

	public ItemStack upgradeCharging(ItemStack container) {
		final int capacity = getCapacityLevel(container);
		final int charging = getChargingLevel(container);

		if (charging < (capacity * 2)) {
			setChargingLevel(container, charging + 1);

			return container;
		} else {
			return null;
		}
	}

	@Override
	public int getEnergyStored(ItemStack container) {
		if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("Energy")) {
			setEnergy(container, 0);
		}

		return container.stackTagCompound.getInteger("Energy");
	}

	@Override
	public int getMaxEnergyStored(ItemStack container) {
		return (getCapacityLevel(container) + 1) * 10 * RFperHalfHeart;
	}

	@Override
	public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
		if (container.stackTagCompound == null) {
			container.stackTagCompound = new NBTTagCompound();
		}

		int energy = container.stackTagCompound.getInteger("Energy");
		int energyReceived = Math.min(getMaxEnergyStored(container) - energy,
				Math.min(getMaxRecieve(container), maxReceive));

		if (!simulate) {
			energy += energyReceived;
			container.stackTagCompound.setInteger("Energy", energy);
		}

		updateDamage(container);

		return energyReceived;
	}

	@Override
	public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
		return 0;
	}

	private int extractEnergyIntern(ItemStack container, int maxExtract, boolean simulate) {
		if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("Energy")) {
			return 0;
		}

		int energy = container.stackTagCompound.getInteger("Energy");
		int energyExtracted = Math.min(energy, maxExtract);

		if (!simulate) {
			energy -= energyExtracted;
			container.stackTagCompound.setInteger("Energy", energy);
		}

		updateDamage(container);

		return energyExtracted;
	}

	private void updateDamage(ItemStack container) {
		float r = (float) getEnergyStored(container) / getMaxEnergyStored(container);
		int res = MaxDamage - (int) (r * MaxDamage);

		container.setItemDamage(res);
	}

	private void setCapacityLevel(ItemStack container, int level) {
		if (container.stackTagCompound == null) {
			container.stackTagCompound = new NBTTagCompound();
		}

		container.stackTagCompound.setInteger("LevelCapacity", level);

		updateDamage(container);
	}

	private void setChargingLevel(ItemStack container, int level) {
		if (container.stackTagCompound == null) {
			container.stackTagCompound = new NBTTagCompound();
		}

		container.stackTagCompound.setInteger("LevelCharging", level);
	}

	private int getMaxRecieve(ItemStack container) {
		return (getChargingLevel(container) + 1) * RFperHalfHeart / 200;
	}

	private void setEnergy(ItemStack container, int energy) {
		if (container.stackTagCompound == null) {
			container.stackTagCompound = new NBTTagCompound();
		}

		container.stackTagCompound.setInteger("Energy", energy);

		updateDamage(container);
	}
}
