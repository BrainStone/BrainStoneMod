package brainstonemod.network.packet.template;

import net.minecraft.entity.player.EntityPlayer;
import brainstonemod.common.helper.BSP;

public abstract class BrainStoneToServerBasePacket extends BrainStoneBasePacket {
	@Override
	public final void handleClientSide(EntityPlayer player) {
		BSP.throwException(new IllegalStateException(
				"The client should never handle this packet! ("
						+ getClass().getName() + ")"));
	}
}