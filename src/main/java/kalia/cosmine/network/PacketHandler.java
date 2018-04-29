package kalia.cosmine.network;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public abstract class PacketHandler<T extends IMessage> implements IMessageHandler<T, IMessage> {
    @Override
    public IMessage onMessage(T message, MessageContext context) {
        FMLCommonHandler.instance().getWorldThread(context.netHandler).addScheduledTask(() -> handleMessage(message, context));
        return null;
    }

    protected abstract void handleMessage(T message, MessageContext context);
}
