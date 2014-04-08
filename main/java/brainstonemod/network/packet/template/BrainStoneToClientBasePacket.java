package brainstonemod.network.packet.template;

import net.minecraft.entity.player.EntityPlayer;
import brainstonemod.common.helper.BSP;

public abstract class BrainStoneToClientBasePacket extends BrainStoneBasePacket {
	@Override
	public final void handleServerSide(EntityPlayer player) {
		BSP.throwException(new IllegalStateException(
				"The server should never handle this packet! ("
						+ getClass().getName() + ")"));
	}
}