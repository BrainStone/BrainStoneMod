package brainstonemod.common.handler;

import java.lang.reflect.Field;
import java.util.Random;
import java.util.UUID;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import brainstonemod.BrainStone;
import brainstonemod.common.compat.BrainStoneModules;
import brainstonemod.common.config.BrainStoneConfigWrapper;
import brainstonemod.common.helper.BSP;
import brainstonemod.common.helper.BrainStoneDamageHelper;
import brainstonemod.common.item.energy.EnergyContainerItemWrapper;
import brainstonemod.common.item.energy.IEnergyContainerItem;
import brainstonemod.common.item.energy.ItemBrainStoneLifeCapacitor;
import brainstonemod.network.PacketDispatcher;
import brainstonemod.network.packet.clientbound.PacketCapacitorData;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.FoodStats;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemSmeltedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;

public class BrainStoneEventHandler {
	private static final Field reflectionFoodTimer = getUnlockedFoodTimer();
	private static final Field reflectionLastDamage = getUnlockedLastDamage();

	private static boolean checkStack(ItemStack stack, UUID capacitorUUID) {
		return (stack != null) && (stack.getItem() != null) && (stack.getItem() == BrainStone.brainStoneLifeCapacitor())
				&& (capacitorUUID.equals(BrainStone.brainStoneLifeCapacitor().getUUID(stack)));
	}

	private static ItemStack getBrainStoneLiveCapacitor(EntityPlayer player) {
		ItemBrainStoneLifeCapacitor capacitor = BrainStone.brainStoneLifeCapacitor();
		ItemBrainStoneLifeCapacitor.PlayerCapacitorMapping mapping = capacitor.getPlayerCapacitorMapping();
		UUID capacitorUUID = mapping.getCapacitorUUID(player.getUniqueID());

		if (capacitorUUID == null)
			return null;

		if (BrainStoneModules.baubles()) {
			ItemStack bStack = BaublesApi.getBaublesHandler(player).getStackInSlot(BaubleType.BELT.getValidSlots()[0]);

			if (checkStack(bStack, capacitorUUID)) {
				return bStack;
			}
		}

		for (ItemStack stack : player.inventory.mainInventory) {
			if (checkStack(stack, capacitorUUID)) {
				return stack;
			}
		}

		return null;
	}

	private static Field getUnlockedFoodTimer() {
		try {
			Field reflectionFoodTimer = FoodStats.class.getDeclaredFields()[3];
			reflectionFoodTimer.setAccessible(true);

			return reflectionFoodTimer;
		} catch (SecurityException e) {
			BSP.warnException(e);
		}

		return null;
	}

	private static Field getUnlockedLastDamage() {
		try {
			Field reflectionLastDamage = EntityLivingBase.class.getDeclaredFields()[40];
			reflectionLastDamage.setAccessible(true);

			return reflectionLastDamage;
		} catch (SecurityException e) {
			BSP.warnException(e);
		}

		return null;
	}

	@SuppressWarnings("deprecation")
	@SubscribeEvent
	public void attachCapabilities(AttachCapabilitiesEvent.Item event) {
		if (event.getCapabilities().containsKey(BrainStone.RESOURCE_LOCATION))
			return;

		if (event.getItem() instanceof IEnergyContainerItem) {
			IEnergyContainerItem item = (IEnergyContainerItem) event.getItem();
			event.addCapability(BrainStone.RESOURCE_LOCATION,
					new EnergyContainerItemWrapper.EnergyContainerItemProvider(item, event.getItemStack()));
		}
	}

	@SubscribeEvent
	public void itemCrafted(ItemCraftedEvent event) {
		final Item item = event.crafting.getItem();
		final Block block = Block.getBlockFromItem(item);
		final EntityPlayer player = event.player;

		if ((block == BrainStone.brainLightSensor()) || (block == BrainStone.brainStoneTrigger())) {
			player.addStat(BrainStone.intelligentBlocks(), 1);
		} else if ((item == BrainStone.brainStoneSword()) || (item == BrainStone.brainStoneShovel())
				|| (item == BrainStone.brainStonePickaxe()) || (item == BrainStone.brainStoneAxe())
				|| (item == BrainStone.brainStoneHoe())) {
			player.addStat(BrainStone.intelligentTools(), 1);
		}
	}

	@SubscribeEvent
	public void itemPickup(ItemPickupEvent event) {
		if (event.pickedUp.getEntityItem().getItem() == BrainStone.brainStoneDust()) {
			event.player.addStat(BrainStone.WTHIT(), 1);
		}
	}

	@SubscribeEvent
	public void itemSmelted(ItemSmeltedEvent event) {
		if (Block.getBlockFromItem(event.smelting.getItem()) == BrainStone.brainStone()) {
			event.player.addStat(BrainStone.itLives(), 1);
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onEntityAttack(LivingAttackEvent event) {
		try {
			if (event.getEntityLiving() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) event.getEntityLiving();
				float ammount = event.getAmount();

				if (event.getSource().isFireDamage() && player.isPotionActive(MobEffects.FIRE_RESISTANCE))
					return;

				if ((float) player.hurtResistantTime > ((float) player.maxHurtResistantTime / 2.0F)) {
					if (ammount <= reflectionLastDamage.getFloat(player))
						return;

					ammount -= reflectionLastDamage.getFloat(player);
					reflectionLastDamage.set(player, event.getAmount());
				}

				ItemStack capacitor = getBrainStoneLiveCapacitor(player);

				if (capacitor != null) {
					float adjustedDamage = BrainStoneDamageHelper.getAdjustedDamage(event.getSource(), ammount, player,
							true);
					float newDamage = BrainStone.brainStoneLifeCapacitor().handleDamage(capacitor, adjustedDamage,
							true);

					if (newDamage == 0.0F) {
						event.setCanceled(true);

						BrainStoneDamageHelper.getAdjustedDamage(event.getSource(), ammount, player, false);
						BrainStone.brainStoneLifeCapacitor().handleDamage(capacitor, adjustedDamage, false);
						player.hurtResistantTime = player.maxHurtResistantTime;
						reflectionLastDamage.set(player, event.getAmount());
					}
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			BSP.warnException(e);
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onEntityDamage(LivingHurtEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			ItemStack capacitor = getBrainStoneLiveCapacitor(player);

			if (capacitor != null) {
				float newDamage = BrainStone.brainStoneLifeCapacitor().handleDamage(capacitor, event.getAmount());

				if (newDamage == 0.0F) {
					event.setCanceled(true);
				} else {
					event.setAmount(newDamage);
				}
			}
		}
	}

	@SubscribeEvent
	public void onEntityLoot(LivingDropsEvent event) {
		if (event.getEntity() instanceof EntityWither) {
			final double chance = Math.pow(1.0 - BrainStoneConfigWrapper.getEssenceOfLifeBaseChance(),
					event.getLootingLevel() + 1);

			if ((new Random()).nextDouble() >= chance)
				event.getDrops().add(new EntityItem(event.getEntity().worldObj, event.getEntity().posX,
						event.getEntity().posY, event.getEntity().posZ, new ItemStack(BrainStone.essenceOfLife())));
		}
	}

	@SubscribeEvent
	public void onPlayerJoinClient(final ClientConnectedToServerEvent event) {
		(new Thread() {
			@Override
			public void run() {
				while (FMLClientHandler.instance().getClientPlayerEntity() == null)
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						BSP.debugException(e);
					}

				BSP.debug("Calling onPlayerJoinClient for "
						+ FMLClientHandler.instance().getClientPlayerEntity().getDisplayNameString());

				BrainStone.onPlayerJoinClient(FMLClientHandler.instance().getClientPlayerEntity(), event);
			}
		}).start();

	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onPlayerJoinServer(PlayerLoggedInEvent event) {
		BSP.debug("Calling onPlayerJoinServer for " + event.player.getDisplayNameString());

		BrainStone.onPlayerJoinServer(event.player, event);
	}

	@SubscribeEvent
	public void onPlayerPreTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.START) {
			EntityPlayer player = event.player;
			FoodStats stats = player.getFoodStats();
			ItemStack capacitor = getBrainStoneLiveCapacitor(player);

			if (capacitor != null) {
				BrainStone.brainStoneLifeCapacitor().healPlayer(capacitor, player);

				// Prevent natural regeneration
				if ((reflectionFoodTimer != null) && (stats.getFoodLevel() >= 18)) {
					try {
						reflectionFoodTimer.set(stats, 0);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						BSP.warnException(e);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void playerJoinServer(PlayerEvent.PlayerLoggedInEvent event) {
		BrainStone.brainStoneLifeCapacitor().getPlayerCapacitorMapping().updateName(event.player.getUniqueID(), false);
		PacketDispatcher.sendToAll(
				new PacketCapacitorData(BrainStone.brainStoneLifeCapacitor().getPlayerCapacitorMapping().getMap()));
	}
}