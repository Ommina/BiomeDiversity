package ommina.biomediversity.blocks.cluster;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ClusterBlock extends Block {

    public ClusterBlock( Properties properties ) {
        super( properties );
    }

    //region Overrides
    @Override
    public void onBlockPlacedBy( World world, BlockPos blockPos, BlockState state, @Nullable LivingEntity entity, ItemStack itemStack ) {

        if ( !world.isRemote )
            for ( int y = -1; y <= 1; y++ )
                for ( int x = -1; x <= 1; x++ )
                    for ( int z = -1; z <= 1; z++ )
                        if ( world.getBlockState( blockPos.add( x, y, z ) ).getBlock() instanceof IClusterController )
                            System.out.println( "" + x + y + z + " yay!" );
                        else
                            System.out.println( "" + x + y + z + " no" );

        super.onBlockPlacedBy( world, blockPos, state, entity, itemStack );

    }
//endregion Overrides

}
