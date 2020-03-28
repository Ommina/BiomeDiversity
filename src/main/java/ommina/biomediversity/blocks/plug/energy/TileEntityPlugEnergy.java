package ommina.biomediversity.blocks.plug.energy;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.ModTileEntities;
import ommina.biomediversity.blocks.collector.TileEntityCollector;
import ommina.biomediversity.blocks.plug.PlugRenderData;
import ommina.biomediversity.blocks.plug.TileEntityPlugBase;
import ommina.biomediversity.blocks.tile.CollectorFinder;
import ommina.biomediversity.blocks.tile.RenderHelper;
import ommina.biomediversity.config.Config;
import ommina.biomediversity.config.Constants;
import ommina.biomediversity.energy.BdEnergyStorage;
import ommina.biomediversity.network.GenericTilePacketRequest;
import ommina.biomediversity.network.Network;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.Optional;
import java.util.function.Supplier;

public class TileEntityPlugEnergy extends TileEntityPlugBase implements ITickableTileEntity {

    private BdEnergyStorage battery;

    private final LazyOptional<IEnergyStorage> handlerEnergy = LazyOptional.of( this::createEnergyHandler );

    public TileEntityPlugEnergy() {
        super( ModTileEntities.PLUG_ENERGY );
    }

    //region Overrides
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability( @Nonnull Capability<T> capability, @Nullable Direction side ) {

        if ( capability == CapabilityEnergy.ENERGY && (side == null || side == Direction.DOWN) )
            return handlerEnergy.cast();

        return super.getCapabilitySuper( capability, side );

    }

    @Override
    public void onLoad() {

        if ( world.isRemote )
            Network.channel.sendToServer( new GenericTilePacketRequest( this.pos ) );

        PLUG_RENDER.colour = RenderHelper.getRGBA( new Color( 127, 255, 142, 192 ).getRGB() );
        PLUG_RENDER.spriteLocation = BiomeDiversity.getId( "block/cluster/cluster_glow_internal" );
        PLUG_RENDER.maximum = Config.collectorEnergyCapacity.get();

    }

    @Override
    public void onChunkUnloaded() {

        handlerEnergy.invalidate();
        super.onChunkUnloaded();

    }

    @Override
    public PlugRenderData getPlugRenderData() {

        TileEntityCollector collector = FINDER.get( world );

        if ( collector != null )
            PLUG_RENDER.value = collector.getEnergyStorage().getEnergyStored();
        else
            PLUG_RENDER.value = 0;

        return PLUG_RENDER;

    }

    @Override
    public void doBroadcast() {
        Network.channel.send( PacketDistributor.NEAR.with( () -> new PacketDistributor.TargetPoint( this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), 64.0f, DimensionType.OVERWORLD ) ), new PlugEnergyPacketUpdate( this ) );
    }

    @Override
    public void invalidateCollector() {

        handlerEnergy.invalidate();
        battery = null;
        removeCollector();

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

    private IEnergyStorage createEnergyHandler() {

        if ( battery != null )
            return battery;

        TileEntityCollector collector = FINDER.get( world );

        if ( collector == null )
            return BdEnergyStorage.EMPTY;

        battery = collector.getEnergyStorage();

        return battery;

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

        //TileEntityCollector collector = collectorResult.getCollector();

    }

    private void doFirstTick() {

        firstTick = false;


    }

    private boolean isBatteryOk() {

        if ( battery != null )
            return true;

        TileEntityCollector collector = FINDER.get( world );

        if ( collector != null ) {
            battery = collector.getEnergyStorage();
            return true;
        }

        return false;

    }

    private void distributeRf() {

        // We're sending energy OUT here, so we are looking at the block below us, and checking if said block has an energy connection upward.  If so, stuff it full.

        TileEntity tileEntity = world.getTileEntity( getPos().down() );

        if ( tileEntity != null && tileEntity.getCapability( CapabilityEnergy.ENERGY, Direction.UP ).isPresent() ) {

            tileEntity.getCapability( CapabilityEnergy.ENERGY, Direction.UP ).ifPresent( e -> {

                TileEntityCollector collector = FINDER.get( world );

                if ( collector != null ) {

                    BdEnergyStorage battery = collector.getEnergyStorage();
                    int maxReceive = e.receiveEnergy( Integer.MAX_VALUE, true );
                    int withdraw = battery.extractEnergy( maxReceive, false );
                    e.receiveEnergy( withdraw, false );

                }

            } );

        }

    }

    public static void handle( PlugEnergyPacketUpdate packet, Supplier<NetworkEvent.Context> ctx ) {

        ctx.get().enqueueWork( () -> {

            Optional<World> world = LogicalSidedProvider.CLIENTWORLD.get( ctx.get().getDirection().getReceptionSide() );

            if ( world.get().isBlockLoaded( packet.tilePos ) ) {

                TileEntity tile = world.get().getTileEntity( packet.tilePos );

                if ( tile instanceof TileEntityPlugEnergy ) {

                    TileEntityPlugEnergy plug = (TileEntityPlugEnergy) tile;

                    plug.FINDER.setCollectorPos( packet.collectorPos );

                }

            }

        } );

        ctx.get().setPacketHandled( true );

    }

}
