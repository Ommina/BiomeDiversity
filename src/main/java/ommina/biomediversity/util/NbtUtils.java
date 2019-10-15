package ommina.biomediversity.util;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public class NbtUtils {

    private static final String POSX = "posx";
    private static final String POSY = "posy";
    private static final String POSZ = "posz";

    @Nullable
    public static BlockPos getBlockPos( String keyName, CompoundNBT compound ) {

        CompoundNBT nbt = compound.getCompound( keyName );

        if ( nbt.isEmpty() )
            return null;

        if ( nbt.contains( POSX ) && nbt.contains( POSY ) && nbt.contains( POSZ ) )
            return new BlockPos( nbt.getInt( POSX ), nbt.getInt( POSY ), nbt.getInt( POSZ ) );

        return null;

    }

    public static void putBlockPos( String keyName, CompoundNBT compound, BlockPos pos ) {

        CompoundNBT nbt = compound.getCompound( keyName );

        nbt.putInt( POSX, pos.getX() );
        nbt.putInt( POSY, pos.getY() );
        nbt.putInt( POSZ, pos.getZ() );

        compound.put( keyName, nbt );

    }

    public static void removeBlockPos( String keyName, CompoundNBT compound ) {

        compound.remove( keyName );

    }

}
