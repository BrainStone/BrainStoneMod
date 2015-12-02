package brainstonemod.common.handler;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import brainstonemod.BrainStone;
import brainstonemod.common.helper.BSP;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemSmeltedEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;

public class BrainStoneEventHandler {
	@SubscribeEvent
	public void itemCrafted(ItemCraftedEvent event) {
		final Item item = event.crafting.getItem();
		final Block block = Block.getBlockFromItem(item);
		final EntityPlayer player = event.player;

		if ((block == BrainStone.brainLightSensor())
				|| (block == BrainStone.brainStoneTrigger())) {
			player.addStat(BrainStone.intelligentBlocks(), 1);
		} else if ((item == BrainStone.brainStoneSword())
				|| (item == BrainStone.brainStoneShovel())
				|| (item == BrainStone.brainStonePickaxe())
				|| (item == BrainStone.brainStoneAxe())
				|| (item == BrainStone.brainStoneHoe())) {
			player.addStat(BrainStone.intelligentTools(), 1);
		} else if (block == BrainStone.brainLogicBlock()) {
			player.addStat(BrainStone.logicBlock(), 1);
		}
	}

	@SubscribeEvent
	public void itemPickup(ItemPickupEvent event) {
		if (event.pickedUp.getEntityItem().getItem() == BrainStone
				.brainStoneDust()) {
			event.player.addStat(BrainStone.WTHIT(), 1);
		}
	}

	@SubscribeEvent
	public void itemSmelted(ItemSmeltedEvent event) {
		if (Block.getBlockFromItem(event.smelting.getItem()) == BrainStone
				.brainStone()) {
			event.player.addStat(BrainStone.itLives(), 1);
		}
	}

	@SubscribeEvent
	public void onPlayerJoinServer(PlayerLoggedInEvent event) {
		BSP.debug("Calling onPlayerJoinServer for "
				+ event.player.getCommandSenderName());

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
						+ FMLClientHandler.instance().getClientPlayerEntity()
								.getCommandSenderName());

				BrainStone.onPlayerJoinClient(FMLClientHandler.instance()
						.getClientPlayerEntity(), event);
			}
		}).start();

	}

	/*@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event) {
		Entity entity = event.entity;

		if (entity instanceof EntityLightningBolt) {
			EntityPlayer player = (EntityPlayer) entity.worldObj.playerEntities
					.get(0);

			BSP.info("" + entity.posX + " " + entity.posY + " " + entity.posZ,
					"" + player.posX + " " + player.posY + " " + player.posZ,
					"");

			if (entity.posX != player.posX || entity.posY != player.posY
					|| entity.posZ != player.posZ) {
				entity.setDead();

				entity.worldObj
						.spawnEntityInWorld(new EntityLightningBolt(
								entity.worldObj, player.posX, player.posY,
								player.posZ));
			}
		}
	}*/
}