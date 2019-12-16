package ommina.biomediversity.blocks.plug.energy;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.collector.TileEntityCollector;
import ommina.biomediversity.blocks.plug.PlugRenderData;
import ommina.biomediversity.blocks.plug.TileEntityPlugBase;
import ommina.biomediversity.blocks.tile.CollectorFinder;
import ommina.biomediversity.config.Config;
import ommina.biomediversity.config.Constants;
import ommina.biomediversity.energy.BdEnergyStorage;
import ommina.biomediversity.network.GenericTilePacketRequest;
import ommina.biomediversity.network.Network;

public class TileEntityPlugEnergy extends TileEntityPlugBase implements ITickableTileEntity {

    //region Overrides
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

        //TileEntityCollector collector = collectorResult.getCollector();

    }

    private void doFirstTick() {

        firstTick = false;

        //Block block = world.getBlockState( this.pos ).getBlock();

        PLUG_RENDER.sprite = BiomeDiversity.getId( "block/cluster/cluster_glow_internal" );
        PLUG_RENDER.maximum = Config.collectorEnergyCapacity.get();

        if ( world.isRemote )
            Network.channel.sendToServer( new GenericTilePacketRequest( this.pos ) );

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


}
