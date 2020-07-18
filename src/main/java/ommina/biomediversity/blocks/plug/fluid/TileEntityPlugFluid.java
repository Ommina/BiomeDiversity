package ommina.biomediversity.blocks.plug.fluid;

import net.minecraft.block.Block;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import ommina.biomediversity.blocks.ModTileEntities;
import ommina.biomediversity.blocks.collector.TileEntityCollector;
import ommina.biomediversity.blocks.collector.Tubes;
import ommina.biomediversity.blocks.plug.PlugRenderData;
import ommina.biomediversity.blocks.plug.TileEntityPlugBase;
import ommina.biomediversity.blocks.tile.CollectorFinder;
import ommina.biomediversity.config.Constants;
import ommina.biomediversity.fluids.BdFluidTank;
import ommina.biomediversity.network.GenericTilePacketRequest;
import ommina.biomediversity.network.Network;
import ommina.biomediversity.rendering.RenderHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

public class TileEntityPlugFluid extends TileEntityPlugBase implements ITickableTileEntity {

    int collectorTank = -1;

    private BdFluidTank tank;

    private final LazyOptional<IFluidHandler> fluidhandler = LazyOptional.of( this::createFluidHandler );

    public TileEntityPlugFluid() {
        super( ModTileEntities.PLUG_FLUID );
    }

    public TileEntityPlugFluid( Tubes tube ) {
        super( ModTileEntities.PLUG_FLUID );

        this.collectorTank = tube.tank();
        this.markDirty();

    }

    //region Overrides
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability( @Nonnull Capability<T> capability, @Nullable Direction side ) {

        if ( collectorTank != -1 && (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && (side == null || side == Direction.DOWN)) )
            return fluidhandler.cast();

        return super.getCapabilitySuper( capability, side );

    }

    @Override
    public void read( CompoundNBT tag ) {

        collectorTank = tag.getInt( "collectorTank" );

        super.read( tag );

    }

    @SuppressWarnings( "unchecked" )
    @Override
    public CompoundNBT write( CompoundNBT tag ) {

        tag.putInt( "collectorTank", collectorTank );

        return super.write( tag );

    }

    @Override
    public PlugRenderData getPlugRenderData() {

        TileEntityCollector collector = FINDER.get( world );

        if ( collector != null )
            PLUG_RENDER.value = collector.getTank( collectorTank ).getFluidAmount();
        else
            PLUG_RENDER.value = 0;

        return PLUG_RENDER;

    }

    @Override
    public void doBroadcast() {
        Network.channel.send( PacketDistributor.NEAR.with( () -> new PacketDistributor.TargetPoint( this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), 64.0f, World.field_234918_g_ ) ), new PlugFluidPacketUpdate( this ) );
    }

    @Override
    public void invalidateCollector() {

        fluidhandler.invalidate();
        tank = null;
        removeCollector();

    }

    @Override
    public void onLoad() {

        if ( world.isRemote )
            Network.channel.sendToServer( new GenericTilePacketRequest( this.pos ) );

        FluidAttributes fluidAttributes = Tubes.fluid( collectorTank ).getAttributes();

        PLUG_RENDER.spriteLocation = fluidAttributes.getStillTexture();
        PLUG_RENDER.colour = RenderHelper.getRGBA( fluidAttributes.getColor() );
        PLUG_RENDER.maximum = collectorTank > 7 ? Constants.COLLECTOR_INNER_TANK_CAPACITY : Constants.COLLECTOR_OUTER_TANK_CAPACITY;

    }

    @Override
    public void tick() {

        if ( firstTick )
            doFirstTick();

        delay--;

        if ( delay > 0 )
            return;

        delay = Constants.CLUSTER_TICK_DELAY;

        if ( world.isRemote || world.isBlockPowered( getPos() ) )
            return;

        doMainWork();

        loop++;

    }
//endregion Overrides

    private IFluidHandler createFluidHandler() {

        if ( tank != null )
            return tank;

        TileEntityCollector collector = FINDER.get( world );

        if ( collector == null )
            return BdFluidTank.EMPTY;

        tank = collector.getTank( collectorTank );

        return tank;

    }

    private void doMainWork() {

        CollectorFinder.GetCollectorResult collectorResult = FINDER.getCollector( world );

        if ( collectorResult.getCollector() == null ) {

            if ( collectorResult.isCollectorMissing() )
                removeCollector();

            if ( FINDER.getCollectorPos() == null && (loop % Constants.CLUSTER_SEARCH_ON_LOOP) == 0 ) {
                BlockPos pos = FINDER.find( world, getPos() );
                if ( pos != null ) {
                    doBroadcast();
                    markDirty();
                }
            }

            return;

        }

        if ( FINDER.shouldRegister() )
            registerSelf();

    }

    private void doFirstTick() {

        firstTick = false;

        Block block = world.getBlockState( pos ).getBlock();

        if ( block instanceof ITube )
            collectorTank = ((ITube) block).getTube().tank();


    }

    public static void handle( PlugFluidPacketUpdate packet, Supplier<NetworkEvent.Context> ctx ) {

        ctx.get().enqueueWork( () -> {

            Optional<World> world = LogicalSidedProvider.CLIENTWORLD.get( ctx.get().getDirection().getReceptionSide() );

            if ( world.get().isBlockLoaded( packet.tilePos ) ) {

                TileEntity tile = world.get().getTileEntity( packet.tilePos );

                if ( tile instanceof TileEntityPlugFluid ) {

                    TileEntityPlugFluid plug = (TileEntityPlugFluid) tile;

                    plug.FINDER.setCollectorPos( packet.collectorPos );
                    plug.collectorTank = packet.collectorTank;

                }

            }

        } );

        ctx.get().setPacketHandled( true );

    }

}
