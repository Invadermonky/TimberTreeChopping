package invader.timber;

import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.Set;

public class TreeStructure {
    private Set<BlockPos> wood = new HashSet<>();
    private Set<BlockPos> leaves = new HashSet<>();
    private BlockPos position;

    public void insertWood(BlockPos pos) {
        this.wood.add(pos);
    }
    public void insertLeaves(BlockPos pos) {
        this.leaves.add(pos);
    }
    public int getWoodCount() {
        return this.wood.size();
    }
    public int getLeavesCount() {
        return this.leaves.size();
    }
    public Set<BlockPos> getWood() {
        return this.wood;
    }
    public Set<BlockPos> getLeaves() {
        return this.leaves;
    }
    public BlockPos getPosition() {
        return this.position;
    }
    public void setPosition(BlockPos pos) {
        this.position = pos;
    }
}
