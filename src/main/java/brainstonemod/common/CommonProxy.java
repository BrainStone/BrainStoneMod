package brainstonemod.common;

import brainstonemod.BrainStoneBlocks;
import brainstonemod.BrainStoneItems;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.oredict.OreDictionary;

public class CommonProxy {

	/**
	 * Returns the Client Minecraft Instance.<br>
	 * <b><u>!!! Will do nothing !!! This is the server proxy !!!</u></b>
	 *
	 * @return the Client Minecraft Instance<br>
	 *         <b><u>!!! Returns null !!! This is the server proxy !!!</u></b>
	 */
	public Minecraft getClient() {
		return null;
	}

	/**
	 * Returns the Client Player Instance.<br>
	 * <b><u>!!! Will do nothing !!! This is the server proxy !!!</u></b>
	 *
	 * @return the Client player Instance<br>
	 *         <b><u>!!! Returns null !!! This is the server proxy !!!</u></b>
	 */
	public EntityPlayerSP getPlayer() {
		return null;
	}

	/**
	 * Registers the ore(s) in the OreDictionary
	 */
	public void registerOreDict() {
		OreDictionary.registerOre("oreBrainstone", BrainStoneBlocks.brainStoneOre());
		OreDictionary.registerOre("dustBrainstone", BrainStoneItems.brainStoneDust());
		OreDictionary.registerOre("brainstone", BrainStoneBlocks.brainStone());
		OreDictionary.registerOre("pulsatingbrainstone", BrainStoneBlocks.pulsatingBrainStone());
		OreDictionary.registerOre("stablepulsatingbrainstone", BrainStoneBlocks.stablePulsatingBrainStone());
	}

	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		return ctx.getServerHandler().player;
	}

	public String format(String key, Object... args) {
		return key;
	}

	public void registerModel(Item item) {
		// Do nothing
	}

	public void registerModel(Block block) {
		// Do nothing
	}
}
