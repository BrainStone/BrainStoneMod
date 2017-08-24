package brainstonemod.common;

import brainstonemod.BrainStone;
import brainstonemod.common.block.BlockBrainStoneAnvil;
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
	public void registerOre() {
		OreDictionary.registerOre("oreBrainstone", BrainStone.brainStoneOre());
		OreDictionary.registerOre("dustBrainstone", BrainStone.brainStoneDust());
		OreDictionary.registerOre("brainstone", BrainStone.brainStone());
	}

	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		return ctx.getServerHandler().player;
	}

	public String format(String key, Object... args) {
		return key;
	}

	public void rmm(Item item) {
		// Do nothing
	}

	public void rmm(Block block) {
		// Do nothing
	}

	public void rmm(BlockBrainStoneAnvil block) {
		// Do nothing
	}
}