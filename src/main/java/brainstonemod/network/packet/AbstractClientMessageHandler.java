package brainstonemod.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author The_Fireplace
 */
public abstract class AbstractClientMessageHandler<T extends IMessage> extends AbstractMessageHandler<T> {
    @Override
    public final IMessage handleServerMessage(EntityPlayer player, T message, MessageContext ctx) {
        return null;
    }
}