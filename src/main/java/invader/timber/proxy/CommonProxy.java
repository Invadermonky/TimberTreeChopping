package invader.timber.proxy;

import invader.timber.handlers.PlayerInteractHandler;
import invader.timber.handlers.ConfigHandler;
import invader.timber.handlers.TreeHandler;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
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
        ItemStack stack = event.getEntityPlayer().getHeldItemMainhand();

        if(!ConfigHandler.disableShift) {
            if ((event.getEntityPlayer().isSneaking() && !ConfigHandler.invertCrouch) ||
            (!event.getEntityPlayer().isSneaking() && ConfigHandler.invertCrouch)) {
                crouching = false;
            }
        }

        if (checkWoodBlock(event.getWorld(), event.getPos()) && checkItem(event.getEntityPlayer()) && crouching) {

            int axeDurability = stack.getMaxDamage() - stack.getItemDamage();

            if (playerData.containsKey(event.getEntityPlayer().getPersistentID()) &&
            playerData.get(playerID).blockPos.equals(event.getPos()) &&
            playerData.get(playerID).axeDurability == axeDurability) {
                return;
            }

            treeHandler = new TreeHandler();
            logCount = treeHandler.analyzeTree(event.getWorld(), event.getPos(), event.getEntityPlayer());

            if ((stack.isItemStackDamageable() && axeDurability < (int) (logCount * ConfigHandler.toolDamage * getUnbreakingModifier(stack))) ||
            logCount > ConfigHandler.maxTreeSize) {
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
        if (!ConfigHandler.dynamicBreakSpeed)
            return;

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

    @SubscribeEvent(priority = EventPriority.LOW)
    public void destroyWoodBlock(BlockEvent.BreakEvent breakEvent) {
        UUID playerID = breakEvent.getPlayer().getPersistentID();
        ItemStack stack = breakEvent.getPlayer().getHeldItemMainhand();

        if (stack.isEmpty() ||
        breakEvent.getState().getBlock().isAir(breakEvent.getState(), breakEvent.getWorld(), breakEvent.getPos()))
            return;
        if (playerData.containsKey(playerID)) {
            BlockPos pos = playerData.get(playerID).blockPos;

            if(pos.equals(breakEvent.getPos())) {
                treeHandler.chopTree(breakEvent.getWorld(), breakEvent.getPlayer());
                setToolDamage(breakEvent.getPlayer(), stack);
            }
        }
    }

    public void setToolDamage(EntityPlayer player, ItemStack stack) {
        if(!player.isCreative() && stack.isItemStackDamageable()) {
            int toolDamage = (int) (playerData.get(player.getPersistentID()).logCount * ConfigHandler.toolDamage);
            stack.damageItem(toolDamage, player);
        }
    }

    public float getUnbreakingModifier(ItemStack stack) {
        if (stack.isItemEnchanted() && EnchantmentHelper.getEnchantments(stack).containsKey(Enchantments.UNBREAKING)) {
            return (float) (1 / (1 + EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack)));
        }
        return 1;
    }

    public boolean checkWoodBlock(World world, BlockPos pos) {
        Block block = world.getBlockState(pos).getBlock();

        if(block.getRegistryName().toString().isEmpty())
            return false;

        if(ConfigHandler.block_whitelist.contains(block.getRegistryName().toString()) ||
                ConfigHandler.block_whitelist.contains(block.getRegistryName().toString() + ":" + block.getMetaFromState(block.getDefaultState())))
            return true;

        if(ConfigHandler.block_blacklist.contains(block.getRegistryName().toString()) ||
                ConfigHandler.block_blacklist.contains(block.getRegistryName().toString() + ":" + block.getMetaFromState(block.getDefaultState())))
            return false;

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

        return stack.getItem() instanceof ItemAxe || stack.getItem().getToolClasses(stack).contains("axe");
    }
}
