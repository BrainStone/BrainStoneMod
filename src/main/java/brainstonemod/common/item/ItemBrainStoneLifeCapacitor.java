package brainstonemod.common.item;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
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
import brainstonemod.client.config.BrainStoneClientConfigWrapper;
import brainstonemod.common.capabilities.IEnergyContainerItem;
import brainstonemod.common.compat.BrainStoneModules;
import brainstonemod.common.config.BrainStoneConfigWrapper;
import brainstonemod.common.helper.BSP;
import brainstonemod.common.helper.BrainStonePowerDisplayUtil;
import brainstonemod.common.item.template.ItemBrainStoneBase;
import brainstonemod.network.PacketDispatcher;
import brainstonemod.network.packet.clientbound.PacketCapacitorData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Optional.Interface(iface = "baubles.api.IBauble", modid = BrainStoneModules.BAUBLES_MODID)
public class ItemBrainStoneLifeCapacitor extends ItemBrainStoneBase implements IEnergyContainerItem, IBauble {
	public static final int MaxDamage = 32;
	public static long RFperHalfHeart;
	/**
	 * The maximum level for any type. The limit is calculated by dividing the
	 * energy limit of 1 000 000 000 000 RF through the RFperHalfHeart * 10
	 */
	public static int MaxLevel;
	private static final UUID emptyUUID = new UUID(0, 0);
	private static final String KEY_ENERGY = "Energy";
	private static final String KEY_LEVEL_CAPACITY = "LevelCapacity";
	private static final String KEY_LEVEL_CHARGING = "LevelCharging";
	private static final String KEY_OWNER_LIST = "OwnerList";
	private static final String KEY_UUID = "UUID";

	private PlayerCapacitorMapping PCmapping;
	private final ItemStack base;

	static {
		updateRFperHalfHeart(BrainStoneConfigWrapper.getBSLCRFperHalfHeart());
	}

	private static void updateRFperHalfHeart(long newRFperHalfHeart) {
		RFperHalfHeart = newRFperHalfHeart;
		MaxLevel = (int) (1000000000000L / (RFperHalfHeart * 10));
	}

	@SideOnly(Side.CLIENT)
	public static void updateRFperHalfHeart() {
		updateRFperHalfHeart(BrainStoneClientConfigWrapper.getBSLCRFperHalfHeart());
	}

	public ItemBrainStoneLifeCapacitor() {
		super(null);

		setCreativeTab(BrainStone.getCreativeTab(CreativeTabs.TOOLS));
		setHasSubtypes(true);
		setMaxDamage(MaxDamage);
		setMaxStackSize(1);

		PCmapping = new PlayerCapacitorMapping();
		base = new ItemStack(this);
		setEmptyUUID(base);
	}

	public void newPlayerCapacitorMapping(File path) {
		PCmapping = new PlayerCapacitorMapping(path);
	}

	public PlayerCapacitorMapping getPlayerCapacitorMapping() {
		return PCmapping;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack container) {
		return (getEnergyStoredLong(container) >= RFperHalfHeart) && hasOwner(container);
	}

	@Override
	public EnumRarity getRarity(ItemStack container) {
		return EnumRarity.EPIC;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (!isInCreativeTab(tab))
			return;

		items.add(getLeveledCapacitor(1, false, false));

		int levels[] = { 1, 2, 3, 5, 9, 19, 99 };

		for (int level : levels) {
			items.add(getLeveledCapacitor(level, true, false));
		}
	}

	public ItemStack getLeveledCapacitor(int level, boolean full, boolean fakeOwner) {
		ItemStack stack = base.copy();
		setCapacityLevel(stack, level);
		setChargingLevel(stack, level);

		if (full) {
			setEnergyStored(stack, getMaxEnergyStoredLong(stack));
		} else {
			setEnergyStored(stack, 0);
		}

		setFakeOwner(stack, fakeOwner);

		return stack;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack container, World worldIn, List<String> list, ITooltipFlag flagIn) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		boolean sneak = Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode());
		boolean correctOwner = isCurrentOwner(container, player);
		boolean canClaim = !correctOwner && isNotFormerOwner(container, player)
				&& (BrainStoneClientConfigWrapper.getBSLCallowStealing() || !hasOwner(container));

		if (sneak) {
			list.add(
					BrainStone.proxy.format("capacitor.owner") + " " + PCmapping.getPlayerName(getUUID(container), true)
							+ (correctOwner ? (TextFormatting.GRAY + BrainStone.proxy.format("capacitor.you"))
									: (canClaim
											? (TextFormatting.DARK_GRAY + BrainStone.proxy.format("capacitor.claim"))
											: "")));
			list.add(TextFormatting.GREEN + BrainStone.proxy.format("capacitor.summary"));
			list.add(TextFormatting.YELLOW + BrainStone.proxy.format("capacitor.costs") + " "
					+ BrainStonePowerDisplayUtil.formatPower(RFperHalfHeart * 2) + " "
					+ BrainStonePowerDisplayUtil.abrevation() + "/" + TextFormatting.DARK_RED + "\u2764");
			list.add(BrainStone.proxy.format("capacitor.capacity") + " " + TextFormatting.GOLD + TextFormatting.BOLD
					+ String.valueOf(getCapacityLevel(container)) + TextFormatting.RESET + TextFormatting.GRAY + " ("
					+ BrainStonePowerDisplayUtil.formatPower(getMaxEnergyStoredLong(container)) + " "
					+ BrainStonePowerDisplayUtil.abrevation() + " " + TextFormatting.DARK_RED
					+ ((getCapacityLevel(container) + 1) * 5) + "\u2764" + TextFormatting.GRAY + ")");
			list.add(BrainStone.proxy.format("capacitor.charging") + " " + TextFormatting.GOLD + TextFormatting.BOLD
					+ String.valueOf(getChargingLevel(container)) + TextFormatting.RESET + TextFormatting.GRAY + " ("
					+ BrainStonePowerDisplayUtil.formatPowerPerTick(getMaxInput(container)) + ")");
		} else {
			list.add(
					BrainStone.proxy.format("capacitor.owner") + " " + PCmapping.getPlayerName(getUUID(container), true)
							+ (correctOwner ? (TextFormatting.GRAY + BrainStone.proxy.format("capacitor.you")) : ""));
			list.add(BrainStone.proxy.format("capacitor.details",
					"" + TextFormatting.YELLOW + TextFormatting.ITALIC
							+ Keyboard.getKeyName(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode())
							+ TextFormatting.RESET + TextFormatting.GRAY));
		}

		list.add(BrainStonePowerDisplayUtil.formatStoredPower(getEnergyStoredLong(container),
				getMaxEnergyStoredLong(container)));
	}

	@Override
	public void onCreated(ItemStack container, World world, EntityPlayer entityPlayer) {
		// Init NBT if new and keep old NBT
		getEnergyStoredLong(container);
		getCapacityLevel(container);
		getChargingLevel(container);
		createUUID(container);

		updateDamage(container);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);

		if (!world.isRemote && player.isSneaking()) {
			UUID playerUUID = player.getUniqueID();

			if (!isCurrentOwner(stack, playerUUID)) {
				if (isNotFormerOwner(stack, playerUUID)) {
					if (BrainStoneConfigWrapper.getBSLCallowStealing() || !hasOwner(stack)) {
						// Get proper UUID if it is empty
						createUUID(stack);
						UUID capacitorUUID = getUUID(stack);

						PCmapping.updateMapping(playerUUID, capacitorUUID);

						addOwnerToList(stack, playerUUID);

						// TODO: Give advancement
						// player.addStat(BrainStone.lifeCapacitor(), 1);

						return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
					} else {
						BrainStone.sendToPlayer(player,
								TextFormatting.DARK_RED + "You cannot steal a Brain Stone Live Capacitor!");
						return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
					}
				} else {
					BrainStone.sendToPlayer(player,
							TextFormatting.DARK_RED + "You can only claim a Brain Stone Live Capacitor once!");
					return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
				}
			}
		}

		return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}

	public float handleDamage(ItemStack container, float damage) {
		return handleDamage(container, damage, false);
	}

	public float handleDamage(ItemStack container, float damage, boolean simulate) {
		int energyCost = (int) (damage * RFperHalfHeart);

		return (float) (energyCost - extractEnergyIntern(container, energyCost, simulate)) / (float) RFperHalfHeart;
	}

	public void healPlayer(ItemStack container, EntityPlayer player) {
		if (getEnergyStoredLong(container) < RFperHalfHeart)
			return;

		double heal = Math.floor(player.getMaxHealth() - player.getHealth());
		int energy = (int) (heal * RFperHalfHeart);

		int energySpent = extractEnergyIntern(container, energy, false);

		player.heal((float) energySpent / (float) RFperHalfHeart);
	}

	public int getCapacityLevel(ItemStack container) {
		if ((container.getTagCompound() == null) || !container.getTagCompound().hasKey(KEY_LEVEL_CAPACITY)) {
			setCapacityLevel(container, 1);
		}

		int capacityLevel = container.getTagCompound().getInteger(KEY_LEVEL_CAPACITY);

		if (capacityLevel >= MaxLevel) {
			setCapacityLevel(container, capacityLevel = (MaxLevel - 1));
		} else if (capacityLevel < 1) {
			setCapacityLevel(container, capacityLevel = 1);
		}

		return capacityLevel;
	}

	public ItemStack upgradeCapacity(ItemStack container) {
		final int capacity = getCapacityLevel(container);
		final int charging = getChargingLevel(container);

		if ((capacity <= (charging * 2)) && (capacity < MaxLevel)) {
			setCapacityLevel(container, capacity + 1);

			return container;
		} else
			return ItemStack.EMPTY;
	}

	public int getChargingLevel(ItemStack container) {
		if ((container.getTagCompound() == null) || !container.getTagCompound().hasKey(KEY_LEVEL_CHARGING)) {
			setChargingLevel(container, 1);
		}

		int chargingLevel = container.getTagCompound().getInteger(KEY_LEVEL_CHARGING);

		if (chargingLevel >= MaxLevel) {
			setChargingLevel(container, chargingLevel = (MaxLevel - 1));
		} else if (chargingLevel < 1) {
			setChargingLevel(container, chargingLevel = 1);
		}

		return chargingLevel;
	}

	public ItemStack upgradeCharging(ItemStack container) {
		final int capacity = getCapacityLevel(container);
		final int charging = getChargingLevel(container);

		if ((charging <= (capacity * 2)) && (charging < MaxLevel)) {
			setChargingLevel(container, charging + 1);

			return container;
		} else
			return ItemStack.EMPTY;
	}

	@Override
	public long getEnergyStoredLong(ItemStack container) {
		if ((container.getTagCompound() == null) || !container.getTagCompound().hasKey(KEY_ENERGY)) {
			setEnergyStored(container, 0L);
		}

		long energyStored = container.getTagCompound().getLong(KEY_ENERGY);
		long maxEnergyStored = getMaxEnergyStoredLong(container);

		if (energyStored > maxEnergyStored) {
			setEnergyStored(container, energyStored = maxEnergyStored);
		} else if (energyStored < 0) {
			setEnergyStored(container, energyStored = 0);
		}

		return energyStored;
	}

	@Override
	public long getMaxEnergyStoredLong(ItemStack container) {
		return (getCapacityLevel(container) + 1) * 10 * RFperHalfHeart;
	}

	@Override
	public void setEnergyStored(ItemStack container, long energy) {
		if (container.getTagCompound() == null) {
			container.setTagCompound(new NBTTagCompound());
		}

		container.getTagCompound().setLong(KEY_ENERGY, energy);

		updateDamage(container);
	}

	@Override
	public long getMaxInput(ItemStack container) {
		return ((getChargingLevel(container) + 1) * RFperHalfHeart) / 200L;
	}

	@Override
	public long getMaxOutput(ItemStack stack) {
		return 0;
	}

	@Override
	public boolean canCharge(ItemStack container, EntityPlayer player) {
		return canReceive(container) && isCurrentOwner(container, player.getUniqueID());
	}

	public UUID getUUID(ItemStack container) {
		if ((container.getTagCompound() == null) || !container.getTagCompound().hasKey(KEY_UUID)) {
			createUUID(container);
		}

		return UUID.fromString(container.getTagCompound().getString(KEY_UUID));
	}

	public void setFakeOwner(ItemStack container, boolean fakeOwner) {
		if (container.getTagCompound() == null) {
			container.setTagCompound(new NBTTagCompound());
		}

		if (fakeOwner) {
			container.getTagCompound().setBoolean("fakeOwner", true);
		} else {
			container.getTagCompound().removeTag("fakeOwner");
		}
	}

	public boolean hasOwner(ItemStack container) {
		if (container.getTagCompound() == null) {
			container.setTagCompound(new NBTTagCompound());
		}

		return container.getTagCompound().hasKey("fakeOwner") || (PCmapping.getPlayerUUID(getUUID(container)) != null);
	}

	public boolean isCurrentOwner(ItemStack container, EntityPlayer player) {
		UUID uuid;

		if (player == null) {
			uuid = emptyUUID;
		} else {
			uuid = player.getUniqueID();
		}

		return isCurrentOwner(container, uuid);
	}

	public boolean isCurrentOwner(ItemStack container, UUID playerUUID) {
		return playerUUID.equals(PCmapping.getPlayerUUID(getUUID(container)));
	}

	public boolean isNotFormerOwner(ItemStack container, EntityPlayer player) {
		UUID uuid;

		if (player == null) {
			uuid = emptyUUID;
		} else {
			uuid = player.getUniqueID();
		}

		return isNotFormerOwner(container, uuid);
	}

	public boolean isNotFormerOwner(ItemStack container, UUID playerUUID) {
		return !getOwnerList(container).contains(playerUUID);
	}

	private int extractEnergyIntern(ItemStack container, int maxExtract, boolean simulate) {
		if ((container.getTagCompound() == null) || !container.getTagCompound().hasKey(KEY_ENERGY))
			return 0;

		long energy = container.getTagCompound().getLong(KEY_ENERGY);
		int energyExtracted = (int) Math.min(energy, maxExtract);

		if (!simulate) {
			energy -= energyExtracted;
			container.getTagCompound().setLong(KEY_ENERGY, energy);
		}

		updateDamage(container);

		return energyExtracted;
	}

	private void updateDamage(ItemStack container) {
		float r = (float) getEnergyStoredLong(container) / getMaxEnergyStoredLong(container);
		int res = MaxDamage - (int) (r * MaxDamage);

		container.setItemDamage(res);
	}

	private void setCapacityLevel(ItemStack container, int level) {
		if (container.getTagCompound() == null) {
			container.setTagCompound(new NBTTagCompound());
		}

		container.getTagCompound().setInteger(KEY_LEVEL_CAPACITY, level);

		updateDamage(container);
	}

	private void setChargingLevel(ItemStack container, int level) {
		if (container.getTagCompound() == null) {
			container.setTagCompound(new NBTTagCompound());
		}

		container.getTagCompound().setInteger(KEY_LEVEL_CHARGING, level);
	}

	private void createUUID(ItemStack container) {
		UUID uuid;

		if (container.getTagCompound() == null) {
			container.setTagCompound(new NBTTagCompound());
		}

		if (container.getTagCompound().hasKey(KEY_UUID)) {
			uuid = getUUID(container);
		} else {
			uuid = emptyUUID;
		}

		if (emptyUUID.equals(uuid)) {
			uuid = UUID.randomUUID();

			container.getTagCompound().setString(KEY_UUID, uuid.toString());
		}
	}

	private void setEmptyUUID(ItemStack container) {
		if (container.getTagCompound() == null) {
			container.setTagCompound(new NBTTagCompound());
		}

		container.getTagCompound().setString(KEY_UUID, emptyUUID.toString());
	}

	private void addOwnerToList(ItemStack container, UUID uuid) {
		if ((container.getTagCompound() == null) || !container.getTagCompound().hasKey(KEY_OWNER_LIST)) {
			getOwnerList(container);
		}

		NBTTagList tagList = container.getTagCompound().getTagList(KEY_OWNER_LIST, NBT.TAG_STRING);

		tagList.appendTag(new NBTTagString(uuid.toString()));

		container.getTagCompound().setTag(KEY_OWNER_LIST, tagList);
	}

	private List<UUID> getOwnerList(ItemStack container) {
		if (container.getTagCompound() == null) {
			container.setTagCompound(new NBTTagCompound());
		}
		if (!container.getTagCompound().hasKey(KEY_OWNER_LIST)) {
			container.getTagCompound().setTag(KEY_OWNER_LIST, new NBTTagList());
		}

		NBTTagList tagList = container.getTagCompound().getTagList(KEY_OWNER_LIST, NBT.TAG_STRING);
		final int size = tagList.tagCount();

		List<UUID> out = new ArrayList<>(size);

		for (int i = 0; i < size; i++) {
			out.add(UUID.fromString(tagList.getStringTagAt(i)));
		}

		return out;
	}

	@Override
	@Optional.Method(modid = BrainStoneModules.BAUBLES_MODID)
	public boolean canEquip(ItemStack stack, EntityLivingBase entity) {
		if (stack.isEmpty() || (entity == null))
			return false;

		return isCurrentOwner(stack, entity.getUniqueID());
	}

	@Override
	@Optional.Method(modid = BrainStoneModules.BAUBLES_MODID)
	public BaubleType getBaubleType(ItemStack stack) {
		return BaubleType.BELT;
	}

	@Override
	@Optional.Method(modid = BrainStoneModules.BAUBLES_MODID)
	public boolean willAutoSync(ItemStack itemstack, EntityLivingBase player) {
		return true;
	}

	public class PlayerCapacitorMapping {
		private static final String relativePath = "/data/" + BrainStone.MOD_ID + "/brainStoneLifeCapacitor.dat";
		private static final String KEY_CAPACITOR_TO_PLAYER = "capacitorToPlayer";
		private static final String KEY_PLAYER_NAME_CACHE = "playerNameCache";
		private static final String KEY_PLAYER_TO_CAPACITOR = "playerToCapacitor";

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
			} catch (IOException e) {
				BSP.errorException(e);
			}
		}

		public void updateMapping(UUID playerUUID, UUID capacitorUUID) {
			if (!map.hasKey(KEY_CAPACITOR_TO_PLAYER)) {
				map.setTag(KEY_CAPACITOR_TO_PLAYER, new NBTTagCompound());
			}
			if (!map.hasKey(KEY_PLAYER_NAME_CACHE)) {
				map.setTag(KEY_PLAYER_NAME_CACHE, new NBTTagCompound());
			}
			if (!map.hasKey(KEY_PLAYER_TO_CAPACITOR)) {
				map.setTag(KEY_PLAYER_TO_CAPACITOR, new NBTTagCompound());
			}

			NBTTagCompound capacitorToPlayer = map.getCompoundTag(KEY_CAPACITOR_TO_PLAYER);
			NBTTagCompound playerToCapacitor = map.getCompoundTag(KEY_PLAYER_TO_CAPACITOR);

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

			PacketDispatcher.sendToAll(new PacketCapacitorData(getPlayerCapacitorMapping().getMap()));

			writeToFile();
		}

		public void updateName(UUID playerUUID, boolean force) {
			if (!map.hasKey(KEY_PLAYER_NAME_CACHE)) {
				map.setTag(KEY_PLAYER_NAME_CACHE, new NBTTagCompound());
			}

			String player = playerUUID.toString();
			NBTTagCompound playerNameCache = map.getCompoundTag(KEY_PLAYER_NAME_CACHE);

			if (force || playerNameCache.hasKey(player)) {
				playerNameCache.setString(player, UsernameCache.getLastKnownUsername(playerUUID));
			}
		}

		public UUID getPlayerUUID(UUID capacitorUUID) {
			if (!map.hasKey(KEY_CAPACITOR_TO_PLAYER)) {
				map.setTag(KEY_CAPACITOR_TO_PLAYER, new NBTTagCompound());
			}

			NBTTagCompound capacitorToPlayer = map.getCompoundTag(KEY_CAPACITOR_TO_PLAYER);

			if (!capacitorToPlayer.hasKey(capacitorUUID.toString()))
				return null;

			return UUID.fromString(capacitorToPlayer.getString(capacitorUUID.toString()));
		}

		public String getPlayerName(UUID capacitorUUID) {
			return getPlayerName(capacitorUUID, false);
		}

		public String getPlayerName(UUID capacitorUUID, boolean colorCode) {
			if (!map.hasKey(KEY_PLAYER_NAME_CACHE)) {
				map.setTag(KEY_PLAYER_NAME_CACHE, new NBTTagCompound());
			}

			UUID playerUUID = getPlayerUUID(capacitorUUID);
			String player = (playerUUID == null) ? "" : playerUUID.toString();
			NBTTagCompound playerNameCache = map.getCompoundTag(KEY_PLAYER_NAME_CACHE);

			if (!playerNameCache.hasKey(player))
				return (colorCode ? (TextFormatting.DARK_AQUA + "" + TextFormatting.ITALIC) : "") + '<'
						+ BrainStone.proxy.format("capacitor.nobody") + '>';

			return (colorCode ? TextFormatting.AQUA : "") + playerNameCache.getString(player);
		}

		public UUID getCapacitorUUID(UUID playerUUID) {
			if (!map.hasKey(KEY_PLAYER_TO_CAPACITOR)) {
				map.setTag(KEY_PLAYER_TO_CAPACITOR, new NBTTagCompound());
			}

			NBTTagCompound playerToCapacitor = map.getCompoundTag(KEY_PLAYER_TO_CAPACITOR);

			if (!playerToCapacitor.hasKey(playerUUID.toString()))
				return null;

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
