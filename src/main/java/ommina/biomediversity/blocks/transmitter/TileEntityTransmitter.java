package ommina.biomediversity.blocks.transmitter;

import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.network.PacketDistributor;
import ommina.biomediversity.blocks.ModTileEntities;
import ommina.biomediversity.blocks.tile.TileEntityAssociation;
import ommina.biomediversity.config.Config;
import ommina.biomediversity.fluids.BdFluidTank;
import ommina.biomediversity.network.BroadcastHelper;
import ommina.biomediversity.network.GenericTankPacket;
import ommina.biomediversity.network.ITankBroadcast;
import ommina.biomediversity.network.Network;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileEntityTransmitter extends TileEntityAssociation implements ITickableTileEntity, ITankBroadcast {

    public static final int LINKING_SOURCE_TRANSMITTER = 1;
    private static final int TANK_COUNT = 1;
    private static final int MINIMUM_DELTA = 200;
    private static List<Fluid> fluidWhitelist = new ArrayList<Fluid>();
    private final BroadcastHelper BROADCASTER = new BroadcastHelper( TANK_COUNT, MINIMUM_DELTA, this );
    private final BdFluidTank TANK = new BdFluidTank( Config.transmitterCapacity.get() );
    private boolean isLoaded = false;
    private LazyOptional<IFluidHandler> handler = LazyOptional.of( this::createHandler );

    public TileEntityTransmitter() {
        super( ModTileEntities.TRANSMITTER );

        TANK.setCanDrain( false );
        TANK.setCanFill( true );
        TANK.addFluidToWhitelist( fluidWhitelist );

        this.source = LINKING_SOURCE_TRANSMITTER;

    }

    public static void addFluidToWhitelist( Fluid fluid ) {

        fluidWhitelist.add( fluid );

    }

    @Override
    public void onLoad() {

        BROADCASTER.reset();

    }

    @Override
    public boolean hasFastRenderer() {

        return true;
    }

    @Override
    public void tick() {

        if ( firstTick )
            doFirstTick();

        if ( world.isRemote )
            return;

        doBroadcast();

    }

    @Override
    public void doBroadcast() {

        if ( BROADCASTER.needsBroadcast() ) {
            Network.channel.send( PacketDistributor.NEAR.with( () -> new PacketDistributor.TargetPoint( this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), 64.0f, DimensionType.OVERWORLD ) ), new GenericTankPacket( this ) );
            BROADCASTER.reset();
        }

    }

    @Override
    public BdFluidTank getTank( int index ) {

        return TANK;
    }

    @Override
    public void read( CompoundNBT tag ) {

        TANK.read( tag );
        super.read( tag );
    }

    @Override
    public CompoundNBT write( CompoundNBT tag ) {

        tag = TANK.write( tag );
        return super.write( tag );

    }

    private IFluidHandler createHandler() {

        return TANK;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability( @Nonnull Capability<T> cap, @Nullable Direction side ) {

        if ( cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY )
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.orEmpty( cap, handler );

        return super.getCapability( cap, side );

    }

}
