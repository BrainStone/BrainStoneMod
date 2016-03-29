package brainstonemod.common.handler;

import java.lang.reflect.Field;
import java.util.Random;

import brainstonemod.BrainStone;
import brainstonemod.common.helper.BSP;
import brainstonemod.common.helper.BrainStoneDamageHelper;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemSmeltedEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.FoodStats;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class BrainStoneEventHandler {
	private static final Field reflectionFoodTimer = getUnlockedFoodTimer();
	private static final Field reflectionLastDamage = getUnlockedLastDamage();

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
		} else if (block == BrainStone.brainLogicBlock()) {
			player.addStat(BrainStone.logicBlock(), 1);
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

	@SubscribeEvent
	public void onPlayerJoinServer(PlayerLoggedInEvent event) {
		BSP.debug("Calling onPlayerJoinServer for " + event.player.getCommandSenderName());

		BrainStone.onPlayerJoinServer(event.player, event);
	}

	@SubscribeEvent
	public void onPlayerJoinClient(final ClientConnectedToServerEvent event) {
		(new Thread() {
			public void run() {
				while (FMLClientHandler.instance().getClientPlayerEntity() == null)
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						BSP.debugException(e);
					}

				BSP.debug("Calling onPlayerJoinClient for "
						+ FMLClientHandler.instance().getClientPlayerEntity().getCommandSenderName());

				BrainStone.onPlayerJoinClient(FMLClientHandler.instance().getClientPlayerEntity(), event);
			}
		}).start();

	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onEntityDamage(LivingHurtEvent event) {
		if (event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			ItemStack capacitor = getBrainStoneLiveCapacitor(player);

			if (capacitor != null) {
				float newDamage = BrainStone.brainStoneLiveCapacitor().handleDamage(capacitor, event.ammount);

				if (newDamage == 0.0F) {
					event.setCanceled(true);
				} else {
					event.ammount = newDamage;
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onEntityAttack(LivingAttackEvent event) {
		try {
			if (event.entityLiving instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) event.entityLiving;
				float ammount = event.ammount;

				if ((float) player.hurtResistantTime > ((float) player.maxHurtResistantTime / 2.0F)) {
					if (ammount <= reflectionLastDamage.getFloat(player)) {
						return;
					}

					ammount -= reflectionLastDamage.getFloat(player);
					reflectionLastDamage.set(player, event.ammount);
				}

				ItemStack capacitor = getBrainStoneLiveCapacitor(player);

				if (capacitor != null) {
					float adjustedDamage = BrainStoneDamageHelper.getAdjustedDamage(event.source, ammount, player,
							true);
					float newDamage = BrainStone.brainStoneLiveCapacitor().handleDamage(capacitor, adjustedDamage,
							true);

					if (newDamage == 0.0F) {
						event.setCanceled(true);

						BrainStoneDamageHelper.getAdjustedDamage(event.source, ammount, player, false);
						BrainStone.brainStoneLiveCapacitor().handleDamage(capacitor, adjustedDamage, false);
						player.hurtResistantTime = player.maxHurtResistantTime;
						reflectionLastDamage.set(player, event.ammount);
					}
				}
			}
		} catch (IllegalArgumentException e) {
			BSP.warnException(e);
		} catch (IllegalAccessException e) {
			BSP.warnException(e);
		}
	}

	@SubscribeEvent
	public void onPlayerPreTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == Phase.START) {
			EntityPlayer player = event.player;
			FoodStats stats = player.getFoodStats();
			ItemStack capacitor = getBrainStoneLiveCapacitor(player);

			if (capacitor != null) {
				BrainStone.brainStoneLiveCapacitor().healPlayer(capacitor, player);

				// Prevent natural regeneration
				if ((reflectionFoodTimer != null) && (stats.getFoodLevel() >= 18)) {
					try {
						reflectionFoodTimer.set(stats, 0);
					} catch (IllegalArgumentException e) {
						BSP.warnException(e);
					} catch (IllegalAccessException e) {
						BSP.warnException(e);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onEntityLoot(LivingDropsEvent event) {
		if (event.entity instanceof EntityWither) {
			final double base_chance = 0.1;
			final double chance = 1.0 - Math.pow(1.0 - base_chance, event.lootingLevel + 1);

			if ((new Random()).nextDouble() < chance)
				event.drops.add(new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY,
						event.entity.posZ, new ItemStack(BrainStone.essenceOfLive())));
		}
	}

	private static ItemStack getBrainStoneLiveCapacitor(EntityPlayer player) {
		for (ItemStack stack : player.inventory.mainInventory) {
			if ((stack != null) && (stack.getItem() != null)
					&& (stack.getItem() == BrainStone.brainStoneLiveCapacitor()))
				return stack;
		}

		return null;
	}
}