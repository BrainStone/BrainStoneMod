package brainstonemod.network.packet;

import brainstonemod.BrainStone;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author The_Fireplace
 */
public abstract class AbstractMessageHandler<T extends IMessage> implements IMessageHandler<T, IMessage> {
    @SideOnly(Side.CLIENT)
    public abstract IMessage handleClientMessage(EntityPlayer player, T message, MessageContext ctx);

    public abstract IMessage handleServerMessage(EntityPlayer player, T message, MessageContext ctx);

    @Override
    public IMessage onMessage(T message, MessageContext ctx) {
        if (ctx.side.isClient()) {
            return handleClientMessage(BrainStone.proxy.getPlayerEntity(ctx), message, ctx);
        } else {
            return handleServerMessage(BrainStone.proxy.getPlayerEntity(ctx), message, ctx);
        }
    }
}