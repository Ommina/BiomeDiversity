package ommina.biomediversity.util;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public class NbtUtils {

    private static final String POSX = "posx";
    private static final String POSY = "posy";
    private static final String POSZ = "posz";

    @Nullable
    public static BlockPos getBlockPos( CompoundNBT nbt ) {

        if ( nbt.contains( POSX ) && nbt.contains( POSY ) && nbt.contains( POSZ ) )
            return new BlockPos( nbt.getInt( POSX ), nbt.getInt( POSY ), nbt.getInt( POSZ ) );

        return null;

    }

    public static void putBlockPos( CompoundNBT nbt, BlockPos pos ) {

        nbt.putInt( POSX, pos.getX() );
        nbt.putInt( POSY, pos.getY() );
        nbt.putInt( POSZ, pos.getZ() );

    }

    public static void removeBlockPos( CompoundNBT nbt ) {

        nbt.remove( POSX );
        nbt.remove( POSY );
        nbt.remove( POSZ );

    }

}
