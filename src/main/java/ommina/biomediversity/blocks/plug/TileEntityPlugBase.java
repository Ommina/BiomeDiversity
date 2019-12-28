package ommina.biomediversity.blocks.plug;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.cluster.IClusterComponent;
import ommina.biomediversity.blocks.collector.TileEntityCollector;
import ommina.biomediversity.blocks.plug.energy.PlugEnergyContainer;
import ommina.biomediversity.blocks.tile.CollectorFinder;
import ommina.biomediversity.config.Constants;
import ommina.biomediversity.util.NbtUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class TileEntityPlugBase extends TileEntity implements IClusterComponent, INamedContainerProvider {

    final protected CollectorFinder FINDER = new CollectorFinder();
    final protected PlugRenderData PLUG_RENDER = new PlugRenderData();

    protected int delay = Constants.CLUSTER_TICK_DELAY;
    protected int loop = 1;

    protected boolean firstTick = true;

    public TileEntityPlugBase( TileEntityType<?> tileEntityTypeIn ) {
        super( tileEntityTypeIn );

    }

    //region Overrides
    @Nullable
    @Override
    public Container createMenu( int i, PlayerInventory playerInventory, PlayerEntity playerEntity ) {
        return new PlugEnergyContainer( i, world, pos, playerInventory, playerEntity );
    }

    @Override
    public abstract <T> LazyOptional<T> getCapability( @Nonnull Capability<T> capability, @Nullable Direction side );

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
    public void registerSelf() {

        CollectorFinder.GetCollectorResult result = FINDER.getCollector( world );

        if ( result.getCollector() != null ) {
            result.getCollector().registerComponent( this );
            FINDER.markAsRegistered();
        } else
            BiomeDiversity.LOGGER.error( "Component is marked as ShouldRegister, but Collector TileEntity is null" );

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

    public <T> LazyOptional<T> getCapabilitySuper( @Nonnull Capability<T> capability, @Nullable Direction side ) {
        return super.getCapability( capability, side );
    }

    public abstract PlugRenderData getPlugRenderData();

    public abstract void doBroadcast();

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
