package invader.timber.network;

import invader.timber.handlers.ConfigHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerSettingsMessage implements IMessage {
    private boolean server_reverseShift;
    private boolean server_disableShift;

    public ServerSettingsMessage() {}

    public ServerSettingsMessage(boolean reverseShift, boolean disableShift) {
        server_reverseShift = reverseShift;
        server_disableShift = disableShift;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        server_reverseShift = buf.readBoolean();
        server_disableShift = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(server_reverseShift);
        buf.writeBoolean(server_disableShift);
    }

    public static class MsgHandler implements IMessageHandler<ServerSettingsMessage, IMessage> {
        @Override
        public IMessage onMessage(ServerSettingsMessage message, MessageContext ctx) {
            IThreadListener mainThread = Minecraft.getMinecraft();
            mainThread.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    ConfigHandler.reverseShift = message.server_reverseShift;
                    ConfigHandler.disableShift = message.server_disableShift;
                }
            });
            return null;
        }
    }
}
