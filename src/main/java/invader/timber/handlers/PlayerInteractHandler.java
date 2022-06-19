package invader.timber.handlers;

import net.minecraft.util.math.BlockPos;

public class PlayerInteractHandler {
    public BlockPos blockPos;
    public float logCount;
    public int axeDurability;

    public PlayerInteractHandler(BlockPos _pos, float _logCount, int _axeDurability) {
        blockPos = _pos;
        logCount = _logCount;
        axeDurability = _axeDurability;
    }
}
