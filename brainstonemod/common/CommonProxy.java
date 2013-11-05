package brainstonemod.common;

import brainstonemod.BrainStone;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.world.World;
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
	 * Returns the Client World.<br>
	 * <b><u>!!! Will do nothing!!! This is the server proxy !!!</u></b>
	 * 
	 * @return the Client World<br>
	 *         <b><u>!!! Returns null !!! This is the server proxy !!!</u></b>
	 */
	public World getClientWorld() {
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
		OreDictionary.registerOre("brainstoneore", BrainStone.brainStoneOre());
	}

	/**
	 * Registers all the render information.<br>
	 * <b><u>!!! Will do nothing !!! This is the server proxy !!!</u></b>
	 */
	public void registerRenderInformation() {
	}
}