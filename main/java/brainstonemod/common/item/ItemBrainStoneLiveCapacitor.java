package brainstonemod.common.item;

import java.util.List;

import brainstonemod.BrainStone;
import brainstonemod.common.helper.BSP;
import cofh.api.energy.ItemEnergyContainer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import crazypants.enderio.machine.power.PowerDisplayUtil;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class ItemBrainStoneLiveCapacitor extends ItemEnergyContainer {
	public static final int MaxDamage = 200;
	public static final int RFperHalfHeart = 1000000;

	public ItemBrainStoneLiveCapacitor() {
		super(MaxDamage * RFperHalfHeart, RFperHalfHeart / 10, 0);

		setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.tabTools));
		setMaxDamage(MaxDamage);
		setMaxStackSize(1);
	}

	@Override
	public void registerIcons(IIconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon("brainstonemod:" + this.getUnlocalizedName().replaceFirst("item.", ""));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack item, int pass) {
		return item.getItemDamage() < item.getMaxDamage();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs creativeTab, List list) {
		ItemStack is = new ItemStack(this);
		setEnergy(is, 0);
		list.add(is);

		is = new ItemStack(this);
		setEnergy(is, getMaxEnergyStored(is));
		list.add(is);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean advancedToolTipInfo) {
		boolean sneak = Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode());

		if (sneak) {
			list.add(EnumChatFormatting.GREEN + "Absorbs all damage while powered.");
			list.add(EnumChatFormatting.YELLOW + "Costs " + PowerDisplayUtil.formatPower(RFperHalfHeart * 2) + " "
					+ PowerDisplayUtil.abrevation() + "/" + EnumChatFormatting.DARK_RED + "\u2764");
		} else {
			list.add("Hold " + EnumChatFormatting.YELLOW + EnumChatFormatting.ITALIC
					+ Keyboard.getKeyName(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode())
					+ EnumChatFormatting.RESET + EnumChatFormatting.GRAY + " for details");
		}

		list.add(PowerDisplayUtil.formatPower(getEnergyStored(itemStack)) + "/"
				+ PowerDisplayUtil.formatPower(getMaxEnergyStored(itemStack)) + " " + PowerDisplayUtil.abrevation());
	}

	@Override
	public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
		int res = super.receiveEnergy(container, maxReceive, simulate);
		if (res != 0 && !simulate) {
			updateDamage(container);
		}
		return res;
	}

	@Override
	public void onCreated(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
		setEnergy(itemStack, 0);
	}

	public float handleDamage(ItemStack stack, float damage) {
		int energyCost = (int) (damage * RFperHalfHeart);

		return (float) (energyCost - extractEnergyIntern(stack, energyCost, false)) / (float) RFperHalfHeart;
	}

	public void healPlayer(ItemStack stack, EntityPlayer player) {
		if (getEnergyStored(stack) < RFperHalfHeart)
			return;

		double heal = Math.floor(player.getMaxHealth() - player.getHealth());
		int energy = (int) (heal * RFperHalfHeart);

		int energySpent = extractEnergyIntern(stack, energy, false);

		player.heal((float) energySpent / (float) RFperHalfHeart);
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

	private void updateDamage(ItemStack stack) {
		float r = (float) getEnergyStored(stack) / getMaxEnergyStored(stack);
		int res = MaxDamage - (int) (r * MaxDamage);
		stack.setItemDamage(res);
	}

	private void setEnergy(ItemStack stack, int energy) {
		if (stack.stackTagCompound == null) {
			stack.stackTagCompound = new NBTTagCompound();
		}

		stack.stackTagCompound.setInteger("Energy", energy);

		updateDamage(stack);
	}
}
