package brainstonemod.common.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import brainstonemod.BrainStone;
import brainstonemod.common.helper.BSP;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class BrainStoneEventHandler {
	/**
	 * This method is called whenever a entity joins the world. This means a mob
	 * spawns in the world or a player joins a server.<br>
	 * This is called for both server and client!
	 * 
	 * @param event
	 *            The event object that contains all of the information for the
	 *            join event
	 */
	@ForgeSubscribe
	public void onPlayerJoin(EntityJoinWorldEvent event) {
		if (event.entity instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) event.entity;

			if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
				BSP.debug("Calling onPlayerJoinClient for "
						+ player.getCommandSenderName());

				BrainStone.onPlayerJoinClient(player, event);
			} else {
				BSP.debug("Calling onPlayerJoinServer for "
						+ player.getCommandSenderName());

				BrainStone.onPlayerJoinServer(player, event);
			}
		}
	}
}