package ommina.biomediversity.blocks.mixer_advanced;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;
import ommina.biomediversity.blocks.ModTileEntities;
import ommina.biomediversity.config.Constants;
import ommina.biomediversity.fluids.BdFluidTank;
import ommina.biomediversity.network.BroadcastHelper;
import ommina.biomediversity.network.GenericTankUpdatePacket;
import ommina.biomediversity.network.ITankBroadcast;
import ommina.biomediversity.network.Network;

public class TileEntityMixerAdvanced extends TileEntity implements ITickableTileEntity, ITankBroadcast {

    private static final int FLUID_PER_CYCLE = 200; // Effectively one bucket per second
    private static final int TANK_COUNT = 1;
    private static final int MINIMUM_DELTA = 200;

    private final BdFluidTank TANK = new BdFluidTank( 0, Constants.ADVANCED_MIXER_TANK_CAPACITY );

    private final BroadcastHelper BROADCASTER = new BroadcastHelper( TANK_COUNT, MINIMUM_DELTA, this );

    public TileEntityMixerAdvanced() {
        super( ModTileEntities.MIXER_ADVANCCED );

        TANK.setCanDrain( true );
        TANK.setCanFill( true );

    }

    //region Overrides
    @Override
    public void doBroadcast() {

        if ( BROADCASTER.needsBroadcast() ) {
            Network.channel.send( PacketDistributor.NEAR.with( () -> new PacketDistributor.TargetPoint( this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), 64.0f, World.field_234918_g_ ) ), new GenericTankUpdatePacket( this ) );
            BROADCASTER.reset();
        }

    }

    @Override
    public BdFluidTank getTank( int index ) {
        return TANK;
    }

    @Override
    public void tick() {

        if ( world.isRemote )
            return;

        // DoStuff

    }
//endregion Overrides

}
