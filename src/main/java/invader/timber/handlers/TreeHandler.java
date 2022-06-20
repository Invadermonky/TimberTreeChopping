package invader.timber.handlers;

import invader.timber.TreeStructure;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class TreeHandler {
    private static Map<UUID, TreeStructure> trees = new HashMap<>();

    private TreeStructure tree;

    public int analyzeTree(World world, BlockPos pos, EntityPlayer player) {
        Queue<BlockPos> queuedBlocks = new LinkedList<>();
        Set<BlockPos> tempBlocks = new HashSet<>();
        Set<BlockPos> checkedBlocks = new HashSet<>();
        Block logBlock = world.getBlockState(pos).getBlock();

        tree = new TreeStructure();
        queuedBlocks.add(pos);
        tree.insertWood(pos);

        while(!queuedBlocks.isEmpty()) {
            pos = queuedBlocks.remove();
            checkedBlocks.add(pos);

            tempBlocks.addAll(borderingBlocks(logBlock, pos, world, checkedBlocks));
            queuedBlocks.addAll(tempBlocks);
            checkedBlocks.addAll(tempBlocks);
            tempBlocks.clear();
        }

        Set<BlockPos> tempLeaves = new HashSet<>(tree.getLeaves());

        for (BlockPos pos1 : tempLeaves) {
            checkedBlocks.add(pos1);
            borderingBlocks(null, pos1, world, checkedBlocks);
        }

        tree.setPosition(pos);
        trees.put(player.getPersistentID(), tree);

        return tree.getWoodCount();
    }

    private Queue<BlockPos> borderingBlocks(Block logBlock, BlockPos logPos, World world, Set<BlockPos> checkedBlocks) {
        Queue<BlockPos> queuedBlocks = new LinkedList<>();
        BlockPos temp_pos;

        for (int i = -1; i <= 1; i++) {
            temp_pos = new BlockPos(logPos.getX() + 1, logPos.getY() + i, logPos.getZ());
            if (checkBlock(world, temp_pos, checkedBlocks, logBlock)) {
                queuedBlocks.add(temp_pos);
            }

            temp_pos = new BlockPos(logPos.getX(), logPos.getY() + i, logPos.getZ() + 1);
            if (checkBlock(world, temp_pos, checkedBlocks, logBlock)) {
                queuedBlocks.add(temp_pos);
            }

            temp_pos = new BlockPos(logPos.getX() - 1, logPos.getY() + i, logPos.getZ());
            if (checkBlock(world, temp_pos, checkedBlocks, logBlock)) {
                queuedBlocks.add(temp_pos);
            }

            temp_pos = new BlockPos(logPos.getX(), logPos.getY() + i, logPos.getZ() - 1);
            if (checkBlock(world, temp_pos, checkedBlocks, logBlock)) {
                queuedBlocks.add(temp_pos);
            }

            temp_pos = new BlockPos(logPos.getX() + 1, logPos.getY() + i, logPos.getZ() + 1);
            if (checkBlock(world, temp_pos, checkedBlocks, logBlock)) {
                queuedBlocks.add(temp_pos);
            }

            temp_pos = new BlockPos(logPos.getX() - 1, logPos.getY() + i, logPos.getZ() - 1);
            if (checkBlock(world, temp_pos, checkedBlocks, logBlock)) {
                queuedBlocks.add(temp_pos);
            }

            temp_pos = new BlockPos(logPos.getX() - 1, logPos.getY() + i, logPos.getZ() + 1);
            if (checkBlock(world, temp_pos, checkedBlocks, logBlock)) {
                queuedBlocks.add(temp_pos);
            }

            temp_pos = new BlockPos(logPos.getX() + 1, logPos.getY() + i, logPos.getZ() - 1);
            if (checkBlock(world, temp_pos, checkedBlocks, logBlock)) {
                queuedBlocks.add(temp_pos);
            }

            temp_pos = new BlockPos(logPos.getX(), logPos.getY() + i, logPos.getZ());
            if (checkBlock(world, temp_pos, checkedBlocks, logBlock)) {
                queuedBlocks.add(temp_pos);
            }
        }
        return queuedBlocks;
    }

    private boolean checkBlock(World world, BlockPos pos, Set<BlockPos> checkedBlocks, Block origin) {
        if (checkedBlocks.contains(pos))
            return false;

        Block block = world.getBlockState(pos).getBlock();

        if(block.getRegistryName().toString().isEmpty())
            return false;

        if(world.getBlockState(pos).getBlock() != origin && ConfigHandler.destroyLeaves) {
            if (ConfigHandler.leaf_whitelist.contains(block.getRegistryName().toString()) ||
                    ConfigHandler.leaf_whitelist.contains(block.getRegistryName().toString() + ":" + block.getMetaFromState(block.getDefaultState()))) {
                tree.insertLeaves(pos);
                return false;
            }

            if (!ConfigHandler.leaf_blacklist.contains(block.getRegistryName().toString()) ||
                    ConfigHandler.leaf_blacklist.contains((block.getRegistryName().toString() + ":" + block.getMetaFromState(block.getDefaultState())))) {
                return false;
            }

            if (world.getBlockState(pos).getBlock().isLeaves(world.getBlockState(pos), world, pos)) {
                tree.insertLeaves(pos);
            }
            return false;
        }
        tree.insertWood(pos);
        return true;
    }

    public void chopTree(World world, EntityPlayer player) {
        int soundReduced = 0;

        if (trees.containsKey(player.getPersistentID())) {
            TreeStructure temp_tree = trees.get(player.getPersistentID());

            for (BlockPos pos : temp_tree.getWood()) {
                if (soundReduced <= 1) {
                    world.destroyBlock((pos), true);
                } else {
                    world.getBlockState(pos).getBlock().dropBlockAsItem(world, pos, world.getBlockState(pos), 1);
                }
                world.setBlockToAir(pos);
                soundReduced++;
            }

            soundReduced = 0;

            if (ConfigHandler.destroyLeaves) {
                for (BlockPos pos : temp_tree.getLeaves()) {
                    if(soundReduced <= 1) {
                        world.destroyBlock(pos, true);
                    } else {
                        world.getBlockState(pos).getBlock().dropBlockAsItem(world, pos, world.getBlockState(pos), 1);
                    }
                    world.setBlockToAir(pos);
                    soundReduced++;
                }
            }
        }
    }
}
