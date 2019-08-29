
package ommina.biomediversity.blocks.rainbarrel;

import ommina.biomediversity.blocks.ModTileEntities;
import ommina.biomediversity.blocks.tile.TileEntityWithTank;

public class TileEntityRainBarrel extends TileEntityWithTank { // implements ITickable, ITankBroadcast {

    private static final int DELAY_RAIN = 4; // 0.20s
    private static final int DELAY_NO_RAIN = 100; // 5.00s
    private static final int FLUID_PER_CYCLE = 200; // Effectively one bucket per second
    private static final int TANK_COUNT = 1;
    private static final int MINIMUM_DELTA = 200;

    //private final BroadcastHelper BROADCASTER = new BroadcastHelper( TANK_COUNT, MINIMUM_DELTA, this );

    private int delay = DELAY_NO_RAIN;

    public TileEntityRainBarrel() {

        super( ModTileEntities.RAIN_BARREL, 1000 ); //Config.rainBarrelCapacity );

        //TANK.setTileEntity( this );
        //TANK.setCanDrain( true );
        //TANK.setCanFill( false );

    }

    // Overrides

    @Override
    public void onLoad() {

        //if ( getWorld().isRemote ) {
        //    Network.network.sendToServer( new PacketRequestUpdateRainBarrel( this ) );
        //    BROADCASTER.reset();
        //}

        super.onLoad();
    }

    @Override
    public boolean hasFastRenderer() {

        return true;
    }

    /*

    @Override
    public boolean hasCapability( @Nonnull Capability<?> capability, @Nullable FacingEnum facing ) {

        if ( facing == EnumFacing.UP )
            return false;

        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability( capability, facing );
    }



    @SuppressWarnings( "unchecked" )
    @Override
    @Nullable
    public <T> T getCapability( @Nonnull Capability<T> capability, @Nullable EnumFacing facing ) {

        if ( capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ) {
            return (T) TANK;
        }

        return super.getCapability( capability, facing );

    }
*/
    /*
    @Override
    public void update() {

        if ( world.isRemote )
            return;

        if ( delay > 0 ) {
            delay--;
            return;
        }

        if ( !world.isRainingAt( getPos().offset( EnumFacing.UP ) ) ) {
            delay = DELAY_NO_RAIN;
            return;
        }

        if ( TANK.fillInternal( new FluidStack( ModFluids.rainWater, FLUID_PER_CYCLE ), true ) == FLUID_PER_CYCLE )
            delay = DELAY_RAIN;
        else
            delay = DELAY_NO_RAIN;

        doBroadcast();

        this.markDirty();

    }

    */

//    @Override
//    public void doBroadcast() {
//
//        if ( BROADCASTER.needsBroadcast() ) {
//            Network.network.sendToAllAround( new PacketUpdateRainBarrel( this ), new NetworkRegistry.TargetPoint( world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64 ) );
//            BROADCASTER.reset();
//        }

//    }

    // End Overrides

}
