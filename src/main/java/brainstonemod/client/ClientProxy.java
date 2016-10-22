package brainstonemod.client;

import brainstonemod.common.CommonProxy;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;

public class ClientProxy extends CommonProxy {
	/**
	 * Adds some armor.
	 * 
	 * @param armor
	 *            The armor name
	 * @return the Armor-Renderer-Prefix
	 */
	@Override
	public int addArmor(String armor) {
		return RenderingRegistry.addNewArmourRendererPrefix(armor);
	}

	/**
	 * Returns the Client Minecraft Instance.
	 * 
	 * @return the Client Minecraft Instance
	 */
	@Override
	public Minecraft getClient() {
		return FMLClientHandler.instance().getClient();
	}

	/**
	 * Returns the Client Player Instance.
	 * 
	 * @return the Client player Instance
	 */
	@Override
	public EntityClientPlayerMP getPlayer() {
		return getClient().thePlayer;
	}

	@Override
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		return (ctx.side.isClient() ? Minecraft.getMinecraft().thePlayer : super.getPlayerEntity(ctx));
	}

	@Override
	public String format(String key, Object... args){
		return I18n.format(key, args);
	}
}