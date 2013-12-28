package brainstonemod.common.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import brainstonemod.BrainStone;
import brainstonemod.common.helper.BSP;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.Player;
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
		if (event.entity instanceof Player) {
			final Player player = (Player) event.entity;

			if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
				BSP.fine("Calling onPlayerJoinClient for "
						+ ((EntityPlayer) event.entity).getEntityName());

				BrainStone.onPlayerJoinClient(player, event);
			} else {
				BSP.fine("Calling onPlayerJoinServer for "
						+ ((EntityPlayer) event.entity).getEntityName());

				BrainStone.onPlayerJoinServer(player, event);
			}
		}
	}
}