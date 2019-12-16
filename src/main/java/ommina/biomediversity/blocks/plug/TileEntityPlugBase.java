package ommina.biomediversity.blocks.plug;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import ommina.biomediversity.blocks.ModTileEntities;
import ommina.biomediversity.blocks.cluster.IClusterComponent;
import ommina.biomediversity.blocks.collector.TileEntityCollector;
import ommina.biomediversity.blocks.plug.energy.PlugEnergyContainer;
import ommina.biomediversity.blocks.tile.CollectorFinder;
import ommina.biomediversity.config.Constants;
import ommina.biomediversity.network.Network;
import ommina.biomediversity.util.NbtUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class TileEntityPlugBase extends TileEntity implements IClusterComponent, INamedContainerProvider {

    final protected CollectorFinder FINDER = new CollectorFinder();
    final protected PlugRenderData PLUG_RENDER = new PlugRenderData();

    protected int delay = Constants.CLUSTER_TICK_DELAY;
    protected int loop = 1;

    protected boolean firstTick = true;

    public TileEntityPlugBase() {
        super( ModTileEntities.PLUG );

    }

    //region Overrides
    @Nullable
    @Override
    public Container createMenu( int i, PlayerInventory playerInventory, PlayerEntity playerEntity ) {
        return new PlugEnergyContainer( i, world, pos, playerInventory, playerEntity );
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability( @Nonnull Capability<T> capability, @Nullable Direction side ) {

        if ( capability == CapabilityEnergy.ENERGY && (side == null || side == Direction.DOWN) ) {
            TileEntityCollector te = FINDER.getCollector( world ).getCollector();
            if ( te != null )
                return te.getEnergyHandler().cast();
        }

        return super.getCapability( capability, side );

    }

    @Override
    @Nullable
    public BlockPos getCollectorPos() {
        return FINDER.getCollectorPos();
    }

    @Override
    public boolean isClusterComponentConnected() {
        return (FINDER.getCollectorPos() != null);
    }

    @Override
    public void invalidateCollector() {
        removeCollector();
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent( getType().getRegistryName().getPath() );
    }

    @Override
    public void onChunkUnloaded() {

        TileEntityCollector collector = FINDER.get( world );

        if ( collector != null )
            collector.deregisterComponent( this );

    }

    @Override
    public boolean hasFastRenderer() {

        return true;
    }

    @Override
    public void read( CompoundNBT nbt ) {

        FINDER.setCollectorPos( NbtUtils.getBlockPos( "collectorpos", nbt ) );

        super.read( nbt );

    }

    @Override
    public CompoundNBT write( CompoundNBT nbt ) {

        if ( FINDER.getCollectorPos() != null )
            NbtUtils.putBlockPos( "collectorpos", nbt, FINDER.getCollectorPos() );

        return super.write( nbt );

    }


    public abstract PlugRenderData getPlugRenderData();

    public void doBroadcast() {

        Network.channel.send( PacketDistributor.NEAR.with( () -> new PacketDistributor.TargetPoint( this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), 64.0f, DimensionType.OVERWORLD ) ), new PacketUpdatePlug( this ) );

    }

    public static void handle( PacketUpdatePlug packet, Supplier<NetworkEvent.Context> ctx ) {

        ctx.get().enqueueWork( () -> {

            Optional<World> world = LogicalSidedProvider.CLIENTWORLD.get( ctx.get().getDirection().getReceptionSide() );

            if ( world.get().isBlockLoaded( packet.tilePos ) ) {

                TileEntity tile = world.get().getTileEntity( packet.tilePos );

                if ( tile instanceof TileEntityPlugBase ) {

                    TileEntityPlugBase plug = (TileEntityPlugBase) tile;

                    plug.FINDER.setCollectorPos( packet.collectorPos );

                }

            }

        } );

        ctx.get().setPacketHandled( true );

    }

    protected void removeCollector() {

        FINDER.setCollectorPos( null );
        PLUG_RENDER.value = 0;
        doBroadcast();
        markDirty();

    }

    public PlugCollectorDetails getCollectorDetails() {

        return new PlugCollectorDetails( FINDER.get( world ) );

    }

    public float getTemperature() {

        TileEntityCollector collector = FINDER.get( world );

        if ( collector == null )
            return 0f;

        return collector.getTemperature();

    }

    /*

    public int getUniqueBiomeCount() {

        TileEntityCollector collector = FINDER.get( world );

        if ( collector == null )
            return 0;

        return collector.getUniqueBiomeCount();

    }

    @Nullable
    public EnergyStorage getEnergyStorage() {

        TileEntityCollector collector = FINDER.get( world );

        if ( collector == null )
            return null;

        return collector.getEnergyStorage();

    }

*/

}
