package invader.timber.proxy;

import invader.timber.handlers.PlayerInteractHandler;
import invader.timber.handlers.ConfigHandler;
import invader.timber.handlers.TreeHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommonProxy {

    public static Map<UUID, Boolean> playerNames = new HashMap<>();
    protected static Map<UUID, PlayerInteractHandler> playerData = new HashMap<>();
    protected TreeHandler treeHandler;

    @SubscribeEvent
    public void treeInteraction(PlayerInteractEvent event) {
        int logCount;
        boolean crouching = true;
        UUID playerID = event.getEntityPlayer().getPersistentID();
        ItemStack heldStack = event.getEntityPlayer().getHeldItemMainhand();

        if(!ConfigHandler.disableShift) {
            if ((event.getEntityPlayer().isSneaking() && !ConfigHandler.reverseShift) ||
                    (!event.getEntityPlayer().isSneaking() && ConfigHandler.reverseShift)) {
                crouching = false;
            }
        }

        if (checkWoodBlock(event.getWorld(), event.getPos()) && checkItem(event.getEntityPlayer()) && crouching) {

            int axeDurability = heldStack.getMaxDamage() - heldStack.getItemDamage();

            if (playerData.containsKey(event.getEntityPlayer().getPersistentID()) &&
            playerData.get(playerID).blockPos.equals(event.getPos()) &&
            playerData.get(playerID).axeDurability == axeDurability) {
                return;
            }

            treeHandler = new TreeHandler();
            logCount = treeHandler.analyzeTree(event.getWorld(), event.getPos(), event.getEntityPlayer());

            if (heldStack.isItemStackDamageable() && axeDurability < logCount) {
                playerData.remove(event.getEntityPlayer().getPersistentID());
                return;
            }

            if (logCount > 1) {
                playerData.put(playerID, new PlayerInteractHandler(event.getPos(), logCount, axeDurability));
            }
        } else {
            playerData.remove(playerID);
        }
    }

    @SubscribeEvent
    public void breakingBlock(PlayerEvent.BreakSpeed breakSpeed) {
        if (playerData.containsKey(breakSpeed.getEntityPlayer().getPersistentID())) {
            UUID playerID = breakSpeed.getEntityPlayer().getPersistentID();
            BlockPos pos = playerData.get(playerID).blockPos;

            if(pos.equals(breakSpeed.getPos())) {
                breakSpeed.setNewSpeed(breakSpeed.getOriginalSpeed() / (playerData.get(playerID).logCount / 2.0f));
            } else {
                breakSpeed.setNewSpeed(breakSpeed.getOriginalSpeed());
            }
        }
    }

    @SubscribeEvent
    public void destroyWoodBlock(BlockEvent.BreakEvent breakEvent) {
        UUID playerID = breakEvent.getPlayer().getPersistentID();

        if (playerData.containsKey(playerID)) {
            BlockPos pos = playerData.get(playerID).blockPos;

            if(pos.equals(breakEvent.getPos())) {
                treeHandler.chopTree(breakEvent.getWorld(), breakEvent.getPlayer());

                if(!breakEvent.getPlayer().isCreative() && breakEvent.getPlayer().getHeldItemMainhand().isItemStackDamageable()) {
                    int axeDurability = breakEvent.getPlayer().getHeldItemMainhand().getItemDamage() + (int) (playerData.get(playerID).logCount * 1.5);
                    breakEvent.getPlayer().getHeldItemMainhand().setItemDamage(axeDurability);
                }
            }
        }
    }

    public boolean checkWoodBlock(World world, BlockPos pos) {
        Block block = world.getBlockState(pos).getBlock();

        if(ConfigHandler.block_whitelist.contains(block.getUnlocalizedName())) {
            return true;
        }
        return (block.isWood(world, pos));
    }

    public boolean checkItem(EntityPlayer player) {
        ItemStack stack = player.getHeldItemMainhand();

        if (stack.isEmpty())
            return false;

        String stackName = stack.getItem().getRegistryName().toString().toLowerCase();
        if (ConfigHandler.axe_blacklist.contains(stackName))
            return false;

        if (ConfigHandler.axe_whitelist.contains(stackName))
            return true;

        try {
            ItemAxe temp = (ItemAxe) player.getHeldItemMainhand().getItem();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
