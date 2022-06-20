package invader.timber.network;

import invader.timber.handlers.ConfigHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientSettingsMessage implements IMessage {
    private boolean client_reverseShift;
    private boolean client_disableShift;

    public ClientSettingsMessage() {}

    public ClientSettingsMessage(boolean reverseShift, boolean disableShift) {
        client_reverseShift = reverseShift;
        client_disableShift = disableShift;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        client_reverseShift = buf.readBoolean();
        client_disableShift = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(client_reverseShift);
        buf.writeBoolean(client_disableShift);
    }

    public static class MsgHandler implements IMessageHandler<ClientSettingsMessage, IMessage> {
        @Override
        public IMessage onMessage(ClientSettingsMessage message, MessageContext ctx) {
            IThreadListener mainThread = ctx.getServerHandler().player.getServerWorld();
            mainThread.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    ConfigHandler.invertCrouch = message.client_reverseShift;
                    ConfigHandler.disableShift = message.client_disableShift;
                }
            });
            return null;
        }
    }
}
