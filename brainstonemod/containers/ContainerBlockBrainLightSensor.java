package brainstonemod.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerBlockBrainLightSensor extends Container {
	/**
	 * Determines if the player can use this.
	 * 
	 * @param entityplayer
	 *            The player (No effect!)
	 * @return true. Can always be used by player.
	 */
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}
}
