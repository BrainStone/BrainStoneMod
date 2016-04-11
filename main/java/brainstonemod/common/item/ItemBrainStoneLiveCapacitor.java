package brainstonemod.common.item;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.lwjgl.input.Keyboard;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import brainstonemod.BrainStone;
import brainstonemod.common.helper.BSP;
import brainstonemod.common.item.template.ItemBrainStoneBase;
import brainstonemod.network.packet.BrainStoneLiveCapacitorMap;
import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.Optional.Method;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import crazypants.enderio.machine.power.PowerDisplayUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.common.util.Constants.NBT;

@Optional.InterfaceList({
		// IEnergyContainerItem
		@Optional.Interface(iface = "cofh.api.energy.IEnergyContainerItem", modid = "CoFHAPI|energy"),
		// IBauble
		@Optional.Interface(iface = "baubles.api.IBauble", modid = "Baubles|API") })
public class ItemBrainStoneLiveCapacitor extends ItemBrainStoneBase implements IEnergyContainerItem, IBauble {
	public static final int MaxDamage = 32;
	public static final int RFperHalfHeart = 1000000;
	private PlayerCapacitorMapping PCmapping;

	public ItemBrainStoneLiveCapacitor() {
		super();

		setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.tabTools));
		setMaxDamage(MaxDamage);
		setMaxStackSize(1);

		PCmapping = new PlayerCapacitorMapping();
	}

	public void newPlayerCapacitorMapping(File path) {
		PCmapping = new PlayerCapacitorMapping(path);
	}

	public PlayerCapacitorMapping getPlayerCapacitorMapping() {
		return PCmapping;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack container, int pass) {
		return (getEnergyStored(container) >= RFperHalfHeart) && (PCmapping.getPlayerUUID(getUUID(container)) != null);
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
		setEnergyStored(is, 0);
		list.add(is);

		int levels[] = { 1, 2, 3, 5, 9, 19, 99 };

		for (int i : levels) {
			is = new ItemStack(this);
			setCapacityLevel(is, i);
			setChargingLevel(is, i);
			setEnergyStored(is, getMaxEnergyStored(is));
			list.add(is);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack container, EntityPlayer player, List list, boolean advancedToolTipInfo) {
		boolean sneak = Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode());
		boolean correctOwner = isCurrentOwner(container, player.getUniqueID());
		boolean canReown = !correctOwner && !isFormerOwner(container, player.getUniqueID());

		if (sneak) {
			list.add("Owner: " + PCmapping.getPlayerName(getUUID(container), true)
					+ (correctOwner ? (EnumChatFormatting.GRAY + " (You)")
							: (canReown ? (EnumChatFormatting.DARK_GRAY + " (Shift-R-Click to claim)") : "")));
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
			list.add("Owner: " + PCmapping.getPlayerName(getUUID(container), true)
					+ (correctOwner ? (EnumChatFormatting.GRAY + " (You)") : ""));
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
		createUUID(container);

		updateDamage(container);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (!world.isRemote && player.isSneaking()) {
			UUID playerUUID = player.getUniqueID();

			if (!isCurrentOwner(stack, playerUUID)) {
				if (!isFormerOwner(stack, playerUUID)) {
					UUID capacitorUUID = getUUID(stack);

					PCmapping.updateMapping(playerUUID, capacitorUUID);

					addOwnerToList(stack, playerUUID);
				} else {
					BrainStone.sendToPlayer(player,
							EnumChatFormatting.DARK_RED + "You can only claim a Brain Stone Live Capacitor once!");
				}
			}
		}

		return stack;
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

		if (capacity <= (charging * 2)) {
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

		if (charging <= (capacity * 2)) {
			setChargingLevel(container, charging + 1);

			return container;
		} else {
			return null;
		}
	}

	@Override
	public int getEnergyStored(ItemStack container) {
		if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("Energy")) {
			setEnergyStored(container, 0);
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

	public UUID getUUID(ItemStack container) {
		if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("UUID")) {
			createUUID(container);
		}

		return UUID.fromString(container.stackTagCompound.getString("UUID"));
	}

	public boolean isCurrentOwner(ItemStack container, UUID playerUUID) {
		return playerUUID.equals(PCmapping.getPlayerUUID(getUUID(container)));
	}

	public boolean isFormerOwner(ItemStack container, UUID playerUUID) {
		return getOwnerList(container).contains(playerUUID);
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

	private void setEnergyStored(ItemStack container, int energy) {
		if (container.stackTagCompound == null) {
			container.stackTagCompound = new NBTTagCompound();
		}

		container.stackTagCompound.setInteger("Energy", energy);

		updateDamage(container);
	}

	private void createUUID(ItemStack container) {
		if (container.stackTagCompound == null) {
			container.stackTagCompound = new NBTTagCompound();
		}

		if (!container.stackTagCompound.hasKey("UUID")) {
			UUID uuid = UUID.randomUUID();

			container.stackTagCompound.setString("UUID", uuid.toString());
		}
	}

	private void addOwnerToList(ItemStack container, UUID uuid) {
		if ((container.stackTagCompound == null) || !container.stackTagCompound.hasKey("OwnerList")) {
			getOwnerList(container);
		}

		NBTTagList tagList = container.stackTagCompound.getTagList("OwnerList", NBT.TAG_STRING);

		tagList.appendTag(new NBTTagString(uuid.toString()));

		container.stackTagCompound.setTag("OwnerList", tagList);
	}

	private List<UUID> getOwnerList(ItemStack container) {
		if (container.stackTagCompound == null) {
			container.stackTagCompound = new NBTTagCompound();
		}
		if (!container.stackTagCompound.hasKey("OwnerList")) {
			container.stackTagCompound.setTag("OwnerList", new NBTTagList());
		}

		NBTTagList tagList = container.stackTagCompound.getTagList("OwnerList", NBT.TAG_STRING);
		final int size = tagList.tagCount();

		List<UUID> out = new ArrayList<UUID>(size);

		for (int i = 0; i < size; i++)
			out.add(UUID.fromString(tagList.getStringTagAt(i)));

		return out;
	}

	@Override
	@Method(modid = "Baubles|API")
	public boolean canEquip(ItemStack stack, EntityLivingBase entity) {
		return isCurrentOwner(stack, entity.getUniqueID());
	}

	@Override
	@Method(modid = "Baubles|API")
	public boolean canUnequip(ItemStack stack, EntityLivingBase entity) {
		return true;
	}

	@Override
	@Method(modid = "Baubles|API")
	public BaubleType getBaubleType(ItemStack stack) {
		return BaubleType.BELT;
	}

	@Override
	@Method(modid = "Baubles|API")
	public void onEquipped(ItemStack stack, EntityLivingBase entity) {
	}

	@Override
	@Method(modid = "Baubles|API")
	public void onUnequipped(ItemStack stack, EntityLivingBase entity) {
	}

	@Override
	@Method(modid = "Baubles|API")
	public void onWornTick(ItemStack stack, EntityLivingBase entity) {
	}

	public class PlayerCapacitorMapping {
		private static final String relativePath = "/brainstonemod/brainStoneLiveCapacitor.dat";

		private final File currentFile;
		private NBTTagCompound map;

		public PlayerCapacitorMapping() {
			currentFile = null;
			map = new NBTTagCompound();
		}

		public PlayerCapacitorMapping(File path) {
			currentFile = new File(path, relativePath);

			readFromFile();
		}

		public void readFromStream(InputStream in) {
			try {
				map = CompressedStreamTools.readCompressed(in);
			} catch (EOFException e) {
				map = new NBTTagCompound();
			} catch (IOException e) {
				BSP.errorException(e);
			}
		}

		public void writeToStream(OutputStream out) {
			try {
				CompressedStreamTools.writeCompressed(map, out);
			} catch (IOException e) {
				BSP.errorException(e);
			}
		}

		public void readFromFile() {
			try {
				currentFile.getParentFile().mkdirs();
				currentFile.createNewFile();

				FileInputStream fileInpuStream = new FileInputStream(currentFile);

				readFromStream(fileInpuStream);

				fileInpuStream.close();
			} catch (FileNotFoundException e) {
				BSP.errorException(e);
			} catch (IOException e) {
				BSP.errorException(e);
			}

			writeToFile();
		}

		public void writeToFile() {
			try {
				FileOutputStream fileOutputStream = new FileOutputStream(currentFile);

				writeToStream(fileOutputStream);

				fileOutputStream.close();
			} catch (FileNotFoundException e) {
				BSP.errorException(e);
			} catch (IOException e) {
				BSP.errorException(e);
			}
		}

		public void updateMapping(UUID playerUUID, UUID capacitorUUID) {
			if (!map.hasKey("capacitorToPlayer")) {
				map.setTag("capacitorToPlayer", new NBTTagCompound());
			}
			if (!map.hasKey("playerNameCache")) {
				map.setTag("playerNameCache", new NBTTagCompound());
			}
			if (!map.hasKey("playerToCapacitor")) {
				map.setTag("playerToCapacitor", new NBTTagCompound());
			}

			NBTTagCompound capacitorToPlayer = map.getCompoundTag("capacitorToPlayer");
			NBTTagCompound playerNameCache = map.getCompoundTag("playerNameCache");
			NBTTagCompound playerToCapacitor = map.getCompoundTag("playerToCapacitor");

			final String player = playerUUID.toString();
			final String capacitor = capacitorUUID.toString();
			final String oldPlayer = capacitorToPlayer.getString(capacitor);
			final String oldCapacitor = playerToCapacitor.getString(player);

			if (playerToCapacitor.hasKey(oldPlayer) || capacitorToPlayer.hasKey(oldCapacitor)) {
				capacitorToPlayer.removeTag(oldCapacitor);
				playerToCapacitor.removeTag(oldPlayer);
			}

			capacitorToPlayer.setString(capacitor, player);
			playerToCapacitor.setString(player, capacitor);

			updateName(playerUUID, true);

			BrainStone.packetPipeline.sendToAll(new BrainStoneLiveCapacitorMap());

			writeToFile();
		}

		public void updateName(UUID playerUUID, boolean force) {
			if (!map.hasKey("playerNameCache")) {
				map.setTag("playerNameCache", new NBTTagCompound());
			}

			String player = playerUUID.toString();
			NBTTagCompound playerNameCache = map.getCompoundTag("playerNameCache");

			if (force || playerNameCache.hasKey(player))
				playerNameCache.setString(player, UsernameCache.getLastKnownUsername(playerUUID));
		}

		public UUID getPlayerUUID(UUID capacitorUUID) {
			if (!map.hasKey("capacitorToPlayer")) {
				map.setTag("capacitorToPlayer", new NBTTagCompound());
			}

			NBTTagCompound capacitorToPlayer = map.getCompoundTag("capacitorToPlayer");

			if (!capacitorToPlayer.hasKey(capacitorUUID.toString())) {
				return null;
			}

			return UUID.fromString(capacitorToPlayer.getString(capacitorUUID.toString()));
		}

		public String getPlayerName(UUID capacitorUUID) {
			return getPlayerName(capacitorUUID, false);
		}

		public String getPlayerName(UUID capacitorUUID, boolean colorCode) {
			if (!map.hasKey("playerNameCache")) {
				map.setTag("playerNameCache", new NBTTagCompound());
			}

			UUID playerUUID = getPlayerUUID(capacitorUUID);
			String player = (playerUUID == null) ? "" : playerUUID.toString();
			NBTTagCompound playerNameCache = map.getCompoundTag("playerNameCache");

			if (!playerNameCache.hasKey(player)) {
				return (colorCode ? (EnumChatFormatting.DARK_AQUA + "" + EnumChatFormatting.ITALIC) : "") + "<Nobody>";
			}

			return (colorCode ? EnumChatFormatting.AQUA : "") + playerNameCache.getString(player);
		}

		public UUID getCapacitorUUID(UUID playerUUID) {
			if (!map.hasKey("playerToCapacitor")) {
				map.setTag("playerToCapacitor", new NBTTagCompound());
			}

			NBTTagCompound playerToCapacitor = map.getCompoundTag("playerToCapacitor");

			if (!playerToCapacitor.hasKey(playerUUID.toString())) {
				return null;
			}

			return UUID.fromString(playerToCapacitor.getString(playerUUID.toString()));
		}

		public NBTTagCompound getMap() {
			return map;
		}

		public void setMap(NBTTagCompound map) {
			this.map = map;
		}
	}
}
