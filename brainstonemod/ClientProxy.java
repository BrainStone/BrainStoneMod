package brainstonemod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.world.World;
import brainstonemod.render.BrainStoneRenderBrainLogicBlockHandler;
import brainstonemod.render.TileEntityBlockBrainLogicBlockRenderer;
import brainstonemod.tileentities.TileEntityBlockBrainLogicBlock;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
	public static int BrainLogicBlockRenderType;

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
		return this.getClient().theWorld;
	}

	/**
	 * Returns the Client Player Instance.
	 * 
	 * @return the Client player Instance
	 */
	@Override
	public EntityClientPlayerMP getPlayer() {
		return this.getClient().thePlayer;
	}

	/**
	 * Registers all the render information.
	 */
	@Override
	public void registerRenderInformation() {
		ClientRegistry.bindTileEntitySpecialRenderer(
				TileEntityBlockBrainLogicBlock.class,
				new TileEntityBlockBrainLogicBlockRenderer());

		BrainLogicBlockRenderType = RenderingRegistry
				.getNextAvailableRenderId();

		RenderingRegistry
				.registerBlockHandler(new BrainStoneRenderBrainLogicBlockHandler());
	}
}