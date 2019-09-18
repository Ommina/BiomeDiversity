
package ommina.biomediversity.blocks.rainbarrel;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import ommina.biomediversity.blocks.ModTileEntities;
import ommina.biomediversity.config.Config;
import ommina.biomediversity.fluids.BdFluidTank;
import ommina.biomediversity.fluids.IHasFluidTank;
import ommina.biomediversity.fluids.ModFluids;
import ommina.biomediversity.network.BroadcastHelper;
import ommina.biomediversity.network.GenericTankPacketRequest;
import ommina.biomediversity.network.ITankBroadcast;
import ommina.biomediversity.network.Network;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.minecraftforge.fluids.capability.IFluidHandler.FluidAction.EXECUTE;

public class TileEntityRainBarrel extends TileEntity implements ITickableTileEntity, IHasFluidTank, ITankBroadcast { // implements ITickable, ITankBroadcast {

    private static final int DELAY_RAIN = 4; // 0.20s
    private static final int DELAY_NO_RAIN = 100; // 5.00s
    private static final int FLUID_PER_CYCLE = 200; // Effectively one bucket per second
    private static final int TANK_COUNT = 1;
    private static final int MINIMUM_DELTA = 200;

    private final BroadcastHelper BROADCASTER = new BroadcastHelper( TANK_COUNT, MINIMUM_DELTA, this );
    private final BdFluidTank TANK = new BdFluidTank( Config.rainbarrelCapacity.get() );

    private LazyOptional<IFluidHandler> handler = LazyOptional.of( this::createHandler );
    private int delay = DELAY_NO_RAIN;

    public TileEntityRainBarrel() {
        super( ModTileEntities.RAIN_BARREL );

        TANK.setCanDrain( true );
        TANK.setCanFill( true );

    }

    @Override
    public int getBroadcastTankAmount( int tank ) {

        return TANK.getFluidAmount();

    }

    @Override
    public void doBroadcast() {

        if ( BROADCASTER.needsBroadcast() ) {
            Network.channel.sendToServer( new GenericTankPacketRequest( this.pos ) );
            BROADCASTER.reset();
        }

    }

    @Override
    public void onLoad() {

        if ( getWorld().isRemote ) {
            Network.channel.sendToServer( new GenericTankPacketRequest( this.pos ) );
            BROADCASTER.reset();
        }

        super.onLoad();
    }

    @Override
    public boolean hasFastRenderer() {

        return true;
    }

    @SuppressWarnings( "unchecked" )
    @Override
    public void read( CompoundNBT tag ) {

        TANK.read( tag );

        super.read( tag );

        return;

    }

    @SuppressWarnings( "unchecked" )
    @Override
    public CompoundNBT write( CompoundNBT tag ) {

        tag = TANK.write( tag );

        return super.write( tag );

    }


    public FluidStack getFluid() {
        return TANK.getFluid();
    }

    private IFluidHandler createHandler() {
        return new FluidTank( Config.rainbarrelCapacity.get() );
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

        if ( TANK.fill( new FluidStack( ModFluids.RAINWATER, FLUID_PER_CYCLE ), EXECUTE ) == FLUID_PER_CYCLE )
            delay = DELAY_RAIN;
        else
            delay = DELAY_NO_RAIN;

        doBroadcast();

        this.markDirty();

    }

    @Override
    public BdFluidTank getTank() {
        return TANK;
    }

}
