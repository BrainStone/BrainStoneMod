package brainstonemod.common.handler;

import java.util.Random;
import java.util.UUID;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import brainstonemod.BrainStone;
import brainstonemod.BrainStoneItems;
import brainstonemod.common.capability.EnergyContainerItemProvider;
import brainstonemod.common.capability.IEnergyContainerItem;
import brainstonemod.common.compat.BrainStoneModules;
import brainstonemod.common.config.BrainStoneConfigWrapper;
import brainstonemod.common.helper.BSP;
import brainstonemod.common.helper.BrainStoneDamageHelper;
import brainstonemod.common.item.ItemBrainStoneLifeCapacitor;
import brainstonemod.network.PacketDispatcher;
import brainstonemod.network.packet.clientbound.PacketCapacitorData;
import lombok.NoArgsConstructor;
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
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;

@NoArgsConstructor(staticName = "registrar")
public class BrainStoneEventHandler {
	private static boolean checkStack(ItemStack stack, UUID capacitorUUID) {
		return (stack != null) && (stack.getItem() != null)
				&& (stack.getItem() == BrainStoneItems.brainStoneLifeCapacitor())
				&& (capacitorUUID.equals(BrainStoneItems.brainStoneLifeCapacitor().getUUID(stack)));
	}

	public static ItemStack getBrainStoneLiveCapacitor(EntityPlayer player) {
		ItemBrainStoneLifeCapacitor capacitor = BrainStoneItems.brainStoneLifeCapacitor();
		ItemBrainStoneLifeCapacitor.PlayerCapacitorMapping mapping = capacitor.getPlayerCapacitorMapping();
		UUID capacitorUUID = mapping.getCapacitorUUID(player.getUniqueID());

		if (capacitorUUID == null)
			return null;

		if (BrainStoneModules.baubles()) {
			ItemStack bStack = BaublesApi.getBaublesHandler(player).getStackInSlot(BaubleType.BELT.getValidSlots()[0]);

			if (checkStack(bStack, capacitorUUID))
				return bStack;
		}

		for (ItemStack stack : player.inventory.mainInventory) {
			if (checkStack(stack, capacitorUUID))
				return stack;
		}

		return null;
	}

	@SubscribeEvent
	public void attachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
		if (event.getCapabilities().containsKey(BrainStone.RESOURCE_LOCATION))
			return;

		ItemStack stack = event.getObject();
		Item item = stack.getItem();

		if (item instanceof IEnergyContainerItem) {
			IEnergyContainerItem energyItem = (IEnergyContainerItem) item;
			event.addCapability(BrainStone.RESOURCE_LOCATION, new EnergyContainerItemProvider(energyItem, stack));
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onEntityAttack(LivingAttackEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			float amount = event.getAmount();

			if (event.getSource().isFireDamage() && player.isPotionActive(MobEffects.FIRE_RESISTANCE))
				return;

			if (player.hurtResistantTime > (player.maxHurtResistantTime / 2.0f)) {
				if (amount <= player.lastDamage)
					return;

				amount -= player.lastDamage;
				player.lastDamage = event.getAmount();
			}

			ItemStack capacitor = getBrainStoneLiveCapacitor(player);

			if (capacitor != null) {
				float adjustedDamage = BrainStoneDamageHelper.getAdjustedDamage(event.getSource(), amount, player,
						true);
				float newDamage = BrainStoneItems.brainStoneLifeCapacitor().handleDamage(capacitor, adjustedDamage,
						true);

				if (newDamage == 0.0f) {
					event.setCanceled(true);

					BrainStoneDamageHelper.getAdjustedDamage(event.getSource(), amount, player, false);
					BrainStoneItems.brainStoneLifeCapacitor().handleDamage(capacitor, adjustedDamage, false);
					player.hurtResistantTime = player.maxHurtResistantTime;
					player.lastDamage = event.getAmount();
					player.maxHurtTime = 10;
					player.hurtTime = player.maxHurtTime;
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onEntityDamage(LivingHurtEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			ItemStack capacitor = getBrainStoneLiveCapacitor(player);

			if (capacitor != null) {
				float newDamage = BrainStoneItems.brainStoneLifeCapacitor().handleDamage(capacitor, event.getAmount());

				if (newDamage == 0.0f) {
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

			if ((new Random()).nextDouble() >= chance) {
				event.getDrops()
						.add(new EntityItem(event.getEntity().world, event.getEntity().posX, event.getEntity().posY,
								event.getEntity().posZ, new ItemStack(BrainStoneItems.essenceOfLife())));
			}
		}
	}

	@SubscribeEvent
	public void onPlayerJoinClient(final ClientConnectedToServerEvent event) {
		(new Thread() {
			@Override
			public void run() {
				while (FMLClientHandler.instance().getClientPlayerEntity() == null) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						BSP.debugException(e);
					}
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
				BrainStoneItems.brainStoneLifeCapacitor().healPlayer(capacitor, player);

				// Prevent natural regeneration
				if (stats.getFoodLevel() >= 18) {
					stats.foodTimer = 0;
				}
			}
		}
	}

	@SubscribeEvent
	public void playerJoinServer(PlayerEvent.PlayerLoggedInEvent event) {
		BrainStoneItems.brainStoneLifeCapacitor().getPlayerCapacitorMapping().updateName(event.player.getUniqueID(),
				false);
		PacketDispatcher.sendToAll(new PacketCapacitorData(
				BrainStoneItems.brainStoneLifeCapacitor().getPlayerCapacitorMapping().getMap()));
	}
}