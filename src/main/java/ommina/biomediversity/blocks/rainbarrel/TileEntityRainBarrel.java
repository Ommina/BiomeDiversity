
package ommina.biomediversity.blocks.rainbarrel;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import ommina.biomediversity.blocks.ModTileEntities;
import ommina.biomediversity.config.Config;
import ommina.biomediversity.fluids.BdFluidTank;
import ommina.biomediversity.fluids.ModFluids;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.minecraftforge.fluids.capability.IFluidHandler.FluidAction.EXECUTE;

public class TileEntityRainBarrel extends TileEntity implements ITickableTileEntity { // implements ITickable, ITankBroadcast {

    private static final int DELAY_RAIN = 4; // 0.20s
    private static final int DELAY_NO_RAIN = 100; // 5.00s
    private static final int FLUID_PER_CYCLE = 200; // Effectively one bucket per second

    private static final int TANK_COUNT = 1;
    private static final int MINIMUM_DELTA = 200;

    //private final BroadcastHelper BROADCASTER = new BroadcastHelper( TANK_COUNT, MINIMUM_DELTA, this );
    private final BdFluidTank TANK = new BdFluidTank( Config.Rainbarrel_Capacity.get() );

    private LazyOptional<IFluidHandler> handler = LazyOptional.of( this::createHandler );

    private int delay = DELAY_NO_RAIN;

    public TileEntityRainBarrel() {
        super( ModTileEntities.RAIN_BARREL );

        TANK.setCanDrain( true );

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

    @SuppressWarnings( "unchecked" )
    @Override
    public void read( CompoundNBT tag ) {

        CompoundNBT tankTag = tag.getCompound( "tank" );

        handler.ifPresent( h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT( tankTag ) );
        super.read( tag );

    }

    @SuppressWarnings( "unchecked" )
    @Override
    public CompoundNBT write( CompoundNBT compound ) {

        handler.ifPresent( h -> {
            CompoundNBT tag = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            tag.put( "tank", compound );
        } );

        return super.write( compound );
    }

    public FluidStack getFluid() {
        return TANK.getFluid();
    }

    private IFluidHandler createHandler() {
        return new FluidTank( Config.Rainbarrel_Capacity.get() );
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability( @Nonnull Capability<T> cap, @Nullable Direction side ) {
        if ( cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ) {
            return handler.cast();
        }
        return super.getCapability( cap, side );
    }

    @Override
    public void tick() {

        if ( world.isRemote )
            return;

        if ( delay > 0 ) {
            delay--;
            return;
        }

        if ( !world.isRainingAt( getPos().offset( Direction.UP ) ) ) {
            delay = DELAY_NO_RAIN;
            return;
        }

        if ( TANK.fill( new FluidStack( ModFluids.RAINWATER_FLOWING, FLUID_PER_CYCLE ), EXECUTE ) == FLUID_PER_CYCLE )
            delay = DELAY_RAIN;
        else
            delay = DELAY_NO_RAIN;

        //doBroadcast();

        this.markDirty();

    }


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
