package invader.timber.proxy;

import invader.timber.handlers.PlayerInteractHandler;
import invader.timber.handlers.ConfigHandler;
import invader.timber.handlers.TreeHandler;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import java.util.UUID;

public class ServerProxy extends CommonProxy {
    @Override
    public void treeInteraction(PlayerInteractEvent event) {
        int logCount;
        boolean crouching = true;
        UUID playerID = event.getEntityPlayer().getPersistentID();

        if(!ConfigHandler.disableShift) {
            if ((event.getEntityPlayer().isSneaking() && !ConfigHandler.reverseShift) ||
                    (!event.getEntityPlayer().isSneaking() && ConfigHandler.reverseShift))
                crouching = false;
        }

        if(checkWoodBlock(event.getWorld(), event.getPos()) && checkItem(event.getEntityPlayer()) && crouching) {
            int axeDurability = event.getEntityPlayer().getHeldItemMainhand().getMaxDamage() - event.getEntityPlayer().getHeldItemMainhand().getItemDamage();

            if( playerData.containsKey(playerID) &&
            playerData.get(playerID).blockPos.equals(event.getPos()) &&
            playerData.get(playerID).axeDurability == axeDurability) {
                return;
            }

            treeHandler = new TreeHandler();
            logCount = treeHandler.analyzeTree(event.getWorld(), event.getPos(), event.getEntityPlayer());

            if (event.getEntityPlayer().getHeldItemMainhand().isItemStackDamageable() && axeDurability < logCount) {
                playerData.remove(playerID);
                return;
            }

            if (logCount > 1) {
                playerData.put(playerID, new PlayerInteractHandler(event.getPos(), logCount, axeDurability));
            }
        } else {
            playerData.remove(playerID);
        }
    }
}
