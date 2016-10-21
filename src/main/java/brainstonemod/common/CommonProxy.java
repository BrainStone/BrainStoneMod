package brainstonemod.common;

import brainstonemod.BrainStone;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.oredict.OreDictionary;

public class CommonProxy {
	/**
	 * Adds some armor.<br>
	 * <b><u>!!! Will do nothing !!! This is the server proxy !!!</u></b>
	 * 
	 * @param armor
	 *            The armor name
	 * @return the Armor-Renderer-Prefix
	 */
	public int addArmor(String armor) {
		return 0;
	}

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
	public EntityClientPlayerMP getPlayer() {
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
		return ctx.getServerHandler().playerEntity;
	}
}