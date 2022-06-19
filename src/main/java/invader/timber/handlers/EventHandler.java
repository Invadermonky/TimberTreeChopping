package invader.timber.handlers;

import invader.timber.Timber;
import invader.timber.network.ServerSettingsMessage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class EventHandler {
    @SubscribeEvent
    public void onServerConnect(PlayerEvent.PlayerLoggedInEvent loggedInEvent) {
        Timber.network.sendTo(new ServerSettingsMessage(ConfigHandler.reverseShift, ConfigHandler.disableShift), (EntityPlayerMP) loggedInEvent.player);
    }
}
