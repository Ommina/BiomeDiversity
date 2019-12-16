package ommina.biomediversity.blocks.rainbarrel;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.network.PacketDistributor;
import ommina.biomediversity.blocks.ModTileEntities;
import ommina.biomediversity.config.Config;
import ommina.biomediversity.fluids.BdFluidTank;
import ommina.biomediversity.fluids.ModFluids;
import ommina.biomediversity.network.BroadcastHelper;
import ommina.biomediversity.network.GenericTankUpdatePacket;
import ommina.biomediversity.network.ITankBroadcast;
import ommina.biomediversity.network.Network;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.minecraftforge.fluids.capability.IFluidHandler.FluidAction.EXECUTE;

public class TileEntityRainBarrel extends TileEntity implements ITickableTileEntity, ITankBroadcast {

    private static final int DELAY_RAIN = 4; // 0.20s
    private static final int DELAY_NO_RAIN = 100; // 5.00s
    private static final int FLUID_PER_CYCLE = 200; // Effectively one bucket per second
    private static final int TANK_COUNT = 1;
    private static final int MINIMUM_DELTA = 200;

    private final BroadcastHelper BROADCASTER = new BroadcastHelper( TANK_COUNT, MINIMUM_DELTA, this );
    private final BdFluidTank TANK = new BdFluidTank( 0, Config.rainbarrelCapacity.get() );

    private final LazyOptional<IFluidHandler> handler = LazyOptional.of( this::createHandler );
    private int delay = DELAY_NO_RAIN;

    public TileEntityRainBarrel() {
        super( ModTileEntities.RAIN_BARREL );

        TANK.setCanDrain( true );
        TANK.setCanFill( true );

    }

//region Overrides
    @Override
    public void doBroadcast() {

        if ( BROADCASTER.needsBroadcast() ) {
            Network.channel.send( PacketDistributor.NEAR.with( () -> new PacketDistributor.TargetPoint( this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), 64.0f, DimensionType.OVERWORLD ) ), new GenericTankUpdatePacket( this ) );
            BROADCASTER.reset();
        }

    }

    @Override
    public BdFluidTank getTank( int index ) {
        return TANK;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability( @Nonnull Capability<T> cap, @Nullable Direction side ) {

        if ( cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY )
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.orEmpty( cap, handler );

        return super.getCapability( cap, side );

    }

    @Override
    public void onLoad() {

        doBroadcast();
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

    }

    @SuppressWarnings( "unchecked" )
    @Override
    public CompoundNBT write( CompoundNBT tag ) {

        tag = TANK.write( tag );
        return super.write( tag );

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
//endregion Overrides

    public FluidStack getFluid() {
        return TANK.getFluid();
    }

    private IFluidHandler createHandler() {
        return TANK;
    }

}
