package brainstonemod.common.handler;

import brainstonemod.BrainStone;
import brainstonemod.common.api.BrainStoneModules;
import brainstonemod.common.helper.BSP;
import brainstonemod.common.helper.BrainStoneConfigHelper;
import brainstonemod.common.item.ItemBrainStoneLifeCapacitor;
import net.minecraft.block.Block;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemSmeltedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;

import java.util.Random;

public class BrainStoneEventHandler {

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
	public void onPlayerJoinServer(PlayerLoggedInEvent event) {
		BSP.debug("Calling onPlayerJoinServer for " + event.player.getDisplayNameString());

		BrainStone.onPlayerJoinServer(event.player, event);
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

	@SubscribeEvent
	public void onEntityLoot(LivingDropsEvent event) {
		if (event.getEntity() instanceof EntityWither) {
			final double base_chance = 0.1;
			final double chance = 1.0 - Math.pow(1.0 - base_chance, event.getLootingLevel() + 1);

			if ((new Random()).nextDouble() < chance)
				event.getDrops().add(new EntityItem(event.getEntity().worldObj, event.getEntity().posX, event.getEntity().posY,
						event.getEntity().posZ, new ItemStack(BrainStone.essenceOfLife())));
		}
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
		if (eventArgs.getModID().equals(BrainStone.MOD_ID)) {
			BrainStoneConfigHelper.loadConfig();

			if (BrainStoneModules.energy())
				ItemBrainStoneLifeCapacitor.updateRFperHalfHeart();
		}
	}
}