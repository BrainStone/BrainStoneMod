package brainstonemod.common.handler;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import brainstonemod.BrainStone;
import brainstonemod.common.helper.BSP;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemSmeltedEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.relauncher.Side;

public class BrainStoneEventHandler {
	@SubscribeEvent
	public void onPlayerJoin(PlayerLoggedInEvent event) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			BSP.debug("Calling onPlayerJoinClient for "
					+ event.player.getCommandSenderName());

			BrainStone.onPlayerJoinClient(event.player, event);
		} else {
			BSP.debug("Calling onPlayerJoinServer for "
					+ event.player.getCommandSenderName());

			BrainStone.onPlayerJoinServer(event.player, event);
		}
	}

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
	public void itemSmelted(ItemSmeltedEvent event) {
		if (Block.getBlockFromItem(event.smelting.getItem()) == BrainStone
				.brainStone()) {
			event.player.addStat(BrainStone.itLives(), 1);
		}
	}

	@SubscribeEvent
	public void itemPickup(ItemPickupEvent event) {
		if (event.pickedUp.getEntityItem().getItem() == BrainStone
				.brainStoneDust()) {
			event.player.addStat(BrainStone.WTHIT(), 1);
		}
	}
}