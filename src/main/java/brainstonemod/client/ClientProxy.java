package brainstonemod.client;

import brainstonemod.client.render.BrainStoneRenderBrainLogicBlockHandler;
import brainstonemod.client.render.TileEntityBlockBrainLogicBlockRenderer;
import brainstonemod.common.CommonProxy;
import brainstonemod.common.tileentity.TileEntityBlockBrainLogicBlock;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

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
	 * Returns the Client World.
	 * 
	 * @return the Client World
	 */
	@Override
	public World getClientWorld() {
		return getClient().theWorld;
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

	/**
	 * Registers all the render information.
	 */
	@Override
	public void registerRenderInformation() {
		super.registerRenderInformation();
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBlockBrainLogicBlock.class,
				new TileEntityBlockBrainLogicBlockRenderer());

		RenderingRegistry.registerBlockHandler(new BrainStoneRenderBrainLogicBlockHandler());
	}

	@Override
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		return (ctx.side.isClient() ? Minecraft.getMinecraft().thePlayer : super.getPlayerEntity(ctx));
	}
}