package mods.brainstone;

import mods.brainstone.tileentities.TileEntityBlockBrainLogicBlock;
import mods.brainstone.tileentities.TileEntityBlockBrainLogicBlockRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {
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
	}

	/**
	 * Registers all the textures.<br>
	 */
	@Override
	public void registerTextures() {
		MinecraftForgeClient.preloadTexture(BrainStone.armorPath
				+ "brainstone_armor_1.png");
		MinecraftForgeClient.preloadTexture(BrainStone.armorPath
				+ "brainstone_armor_2.png");

		MinecraftForgeClient.preloadTexture(BrainStone.guiPath
				+ "GuiBrainLightSensorClassic.png");
		MinecraftForgeClient.preloadTexture(BrainStone.guiPath
				+ "GuiBrainLightSensorMore.png");
		MinecraftForgeClient.preloadTexture(BrainStone.guiPath
				+ "GuiBrainLogicBlock.png");
		MinecraftForgeClient.preloadTexture(BrainStone.guiPath
				+ "GuiBrainStoneTrigger.png");
	}
}