package ommina.biomediversity.blocks.transmitter;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.network.PacketDistributor;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.ModTileEntities;
import ommina.biomediversity.blocks.tile.TileEntityAssociation;
import ommina.biomediversity.config.Config;
import ommina.biomediversity.fluids.BdFluidTank;
import ommina.biomediversity.fluids.FluidStrengths;
import ommina.biomediversity.network.BroadcastHelper;
import ommina.biomediversity.network.GenericTankUpdatePacket;
import ommina.biomediversity.network.ITankBroadcast;
import ommina.biomediversity.network.Network;
import ommina.biomediversity.worlddata.TransmitterData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityTransmitter extends TileEntityAssociation implements ITickableTileEntity, ITankBroadcast, INamedContainerProvider {

    public static final int LINKING_SOURCE_TRANSMITTER = 1;

    private static final int TANK_COUNT = 1;
    private static final int MINIMUM_DELTA = 200;

    private final BroadcastHelper BROADCASTER = new BroadcastHelper( TANK_COUNT, MINIMUM_DELTA, this );

    private final BdFluidTank TANK = new BdFluidTank( Config.transmitterCapacity.get() ) {

        //region Overrides
        @Override
        protected void onFill( int amount ) {

            updateFluidDisplay( getWorld(), getPos() );

            super.onContentsChanged();
        }
//endregion Overrides

    };

    private LazyOptional<IFluidHandler> handler = LazyOptional.of( this::createHandler );

    public TileEntityTransmitter() {
        super( ModTileEntities.TRANSMITTER );

        TANK.setCanDrain( false );
        TANK.setCanFill( true );

        this.source = LINKING_SOURCE_TRANSMITTER;

    }

    public float getTemperature() {

        return world.getBiome( pos ).getTemperature( pos );

    }

    public float getStrength() {

        if (TANK.isEmpty() )
            return 0;

        return (float) FluidStrengths.getStrength( TANK.getFluid().getFluid() );

    }

    //region Overrides
    @Nullable
    @Override
    public Container createMenu( int i, PlayerInventory playerInventory, PlayerEntity playerEntity ) {
        return new TransmitterContainer( i, world, pos, playerInventory, playerEntity );
    }

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
    public ITextComponent getDisplayName() {
        return new StringTextComponent( getType().getRegistryName().getPath() );
    }

    @Override
    public void onChunkUnloaded() {

        handler.invalidate();

    }

    @Override
    public void onLoad() {

        BROADCASTER.reset();

        //BiomeDiversity.LOGGER.info( "Transmitter loaded: " + getPos().toString() );

    }

    @Override
    public boolean hasFastRenderer() {

        return true;
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

    @Override
    protected void doFirstTick() {
        super.doFirstTick();

        if ( world.isRemote )
            return;

        refreshTransmitterTankFromTransmitterNetwork();

    }

    @Override
    public void tick() {

        if ( firstTick )
            doFirstTick();

        if ( world.isRemote )
            return;

        doBroadcast();

    }
//endregion Overrides

    private static void updateFluidDisplay( World world, BlockPos pos ) {

        if ( world.isRemote )
            return;

        TileEntity te = world.getTileEntity( pos );

        if ( te == null )
            return;

        if ( te instanceof TileEntityTransmitter ) {

            TileEntityTransmitter tile = (TileEntityTransmitter) te;

            world.getCapability( BiomeDiversity.TRANSMITTER_NETWORK_CAPABILITY, null ).ifPresent( cap -> {

                TransmitterData pd = cap.getTransmitter( tile.getOwner(), tile.getIdentifier() );
                FluidStack fluid = tile.getTank( 0 ).getFluid();

                if ( !tile.firstTick ) {
                    pd.setAmount( fluid.getAmount() );
                    pd.fluid = fluid.getFluid();
                } else {
                    pd.setAmount( 0 );
                    pd.fluid = null;
                }

            } );


// TODO: Needs to be moved to a general Fluid Changed Event
/*

        } else if ( te instanceof TileEntityMixer ) {
            ((TileEntityMixer) te).checkNeedsMixing();
        }

        if ( te instanceof ITankBroadcast )
            ((ITankBroadcast) te).doBroadcast();

*/

        }
    }

    public void refreshTransmitterTankFromTransmitterNetwork() {

        world.getCapability( BiomeDiversity.TRANSMITTER_NETWORK_CAPABILITY, null ).ifPresent( cap -> {

            TransmitterData pd = cap.getTransmitter( this.getOwner(), this.getIdentifier() );

            if ( pd.fluid == null ) {
                this.getTank( 0 ).setFluid( FluidStack.EMPTY );
            } else {
                this.getTank( 0 ).setFluid( new FluidStack( pd.fluid, pd.getAmount() ) );
            }

        } );

    }

    private IFluidHandler createHandler() {

        return TANK;
    }

}
