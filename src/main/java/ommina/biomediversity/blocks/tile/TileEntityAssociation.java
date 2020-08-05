package ommina.biomediversity.blocks.tile;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.receiver.TileEntityReceiver;
import ommina.biomediversity.blocks.transmitter.TileEntityTransmitter;
import ommina.biomediversity.network.GenericTilePacketRequest;
import ommina.biomediversity.network.Network;
import ommina.biomediversity.util.NbtUtils;
import ommina.biomediversity.world.chunkloader.ChunkLoader;
import ommina.biomediversity.worlddata.TransmitterData;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class TileEntityAssociation extends TileEntity {

    private static final BooleanProperty IS_CONNECTED = BooleanProperty.create( "connected" );

    protected UUID owner;
    protected UUID associatedIdentifier = null;
    protected BlockPos associatedPos = null;
    protected int source = 0;
    protected boolean firstTick = true;
    protected String biomeRegistryName = "null:null";

    private UUID identifier;

    public TileEntityAssociation( TileEntityType<?> tile ) {
        super( tile );

        this.identifier = UUID.randomUUID();

    }

    //region Overrides
    @Override
    public void read( BlockState blockState, CompoundNBT nbt ) {
        super.read( blockState, nbt );

        identifier = nbt.getUniqueId( "identifier" );

        if ( nbt.hasUniqueId( "owner" ) )
            owner = nbt.getUniqueId( "owner" );

        if ( nbt.hasUniqueId( "associatedidentifier" ) ) {
            associatedIdentifier = nbt.getUniqueId( "associatedidentifier" );
            associatedPos = NbtUtils.getBlockPos( "associatedpos", nbt );
        }

    }

    @Override
    public CompoundNBT write( final CompoundNBT nbt ) {

        nbt.putUniqueId( "identifier", identifier );

        if ( owner != null )
            nbt.putUniqueId( "owner", owner );

        if ( associatedIdentifier != null )
            nbt.putUniqueId( "associatedidentifier", associatedIdentifier );

        if ( associatedPos != null )
            NbtUtils.putBlockPos( "associatedpos", nbt, associatedPos );

        return super.write( nbt );

    }

    // End Overrides
//endregion Overrides

    public String getBiomeRegistryName() {
        return this.biomeRegistryName;
    }

    public static void createLink( World world, TileEntityAssociation tile, UUID remoteIdentifier, BlockPos remotePos ) {

        boolean shouldUnload = false;

        preLink( world, tile, remoteIdentifier, remotePos );
        createLink( tile, remoteIdentifier, remotePos );

        if ( !world.isBlockLoaded( remotePos ) ) {
            shouldUnload = true;
            ChunkLoader.forceSingle( world, remotePos );
        }

        TileEntity remoteTile = world.getTileEntity( remotePos );
        TileEntityAssociation tea = createLink( world, remoteTile, tile );

        UUID identifierTransmitter;
        UUID identifierReceiver;
        UUID owner;

        if ( tile instanceof TileEntityTransmitter ) {
            identifierTransmitter = tile.getIdentifier();
            identifierReceiver = tea.getIdentifier();
            owner = tile.getOwner();
        } else {
            identifierReceiver = tile.getIdentifier();
            identifierTransmitter = tea.getIdentifier();
            owner = tea.getOwner();
        }

        createLinkComplete( world, owner, identifierTransmitter, identifierReceiver );

        if ( tile instanceof TileEntityReceiver )
            ((TileEntityReceiver) tile).refreshReceiverTankFromTransmitterNetwork();
        else if ( remoteTile instanceof TileEntityReceiver )
            ((TileEntityReceiver) remoteTile).refreshReceiverTankFromTransmitterNetwork();

        if ( shouldUnload )
            ChunkLoader.releaseSingle( world, remotePos );

        tile.markDirty();

    }

    public static void removeLink( World world, TileEntityAssociation tile, boolean isDestroying ) {

        // Removes a connection from a tile. If that tile has a connection to another tile, remove the other's connection too
        // (That is, remove BOTH sides of the connection)

        BlockPos remotePos = tile.getAssociatedPos();
        TileEntityAssociation tea = null;

        if ( world.isBlockLoaded( remotePos ) ) {
            tea = removeLink( world, world.getTileEntity( remotePos ), isDestroying );
        } else {
            ChunkLoader.forceSingle( world, remotePos );
            tea = removeLink( world, world.getTileEntity( remotePos ), isDestroying );
            ChunkLoader.releaseSingle( world, remotePos );
        }

        if ( tea == null ) { // A bugged/broken link.  One side thinks it is linked, the other doesn't.  Just clear it the local side so it's back into a good state.
            clearAssociation( tile );
            return;
        }

        UUID identifierTransmitter;
        UUID identifierReceiver;
        UUID owner;

        if ( tile instanceof TileEntityTransmitter ) {
            identifierTransmitter = tile.getIdentifier();
            identifierReceiver = tea.getIdentifier();
            owner = tile.getOwner();
        } else {
            identifierReceiver = tile.getIdentifier();
            identifierTransmitter = tea.getIdentifier();
            owner = tea.getOwner();
        }

        removeLinkComplete( world, owner, identifierTransmitter, identifierReceiver );

        if ( !isDestroying )
            removeLink( tile );

    }

    private static void createLinkComplete( World world, UUID owner, UUID identifierTransmitter, UUID identifierReceiver ) {

        world.getCapability( BiomeDiversity.TRANSMITTER_NETWORK_CAPABILITY, null ).ifPresent( cap -> cap.getTransmitter( owner, identifierTransmitter ).receiver = identifierReceiver );

    }

    private static TileEntityAssociation createLink( World world, TileEntity remoteTE, TileEntityAssociation tile ) {

        TileEntityAssociation remoteTile = null;

        if ( remoteTE instanceof TileEntityAssociation ) {
            remoteTile = (TileEntityAssociation) remoteTE;

            createLink( remoteTile, tile.getIdentifier(), tile.getPos() );
        }

        return remoteTile;

    }

    private static void createLink( TileEntityAssociation tile, UUID identifier, BlockPos pos ) {

        tile.setAssociatedIdentifier( identifier );
        tile.setAssociatedPos( pos );

        updateBlockStateForAntenna( tile, true );

        tile.markDirty();

    }

    private static void preLink( World world, TileEntityAssociation tile, UUID remoteIdentifier, BlockPos remotePos ) {

        // Before we start, we need to remove any existing connections between the source and the destination tiles.

        if ( tile.hasLink() )
            removeLink( world, tile, false );

        if ( remotePos != null && world.isBlockLoaded( remotePos ) ) {
            preLink( world, world.getTileEntity( remotePos ) );
        } else {
            ChunkLoader.forceSingle( world, remotePos );
            preLink( world, world.getTileEntity( remotePos ) );
            ChunkLoader.releaseSingle( world, remotePos );
        }

    }

    private static TileEntityAssociation preLink( World world, TileEntity te ) {

        TileEntityAssociation tile = null;

        if ( te instanceof TileEntityAssociation ) {
            tile = (TileEntityAssociation) te;
            if ( tile.hasLink() )
                removeLink( world, (TileEntityAssociation) te, false );
        }

        return tile;

    }

    private static void removeLinkComplete( World world, UUID owner, UUID identifierTransmitter, UUID identifierReceiver ) {

        world.getCapability( BiomeDiversity.TRANSMITTER_NETWORK_CAPABILITY, null ).ifPresent( cap -> {

            TransmitterData pd = cap.getTransmitter( owner, identifierTransmitter );

            if ( pd.receiver == null ) {
                BiomeDiversity.LOGGER.error( "Receiver is null when attempting to unlink a TransmitterNetwork transmitter/receiver pair" );
            }

            pd.receiver = null;

        } );

    }

    private static TileEntityAssociation removeLink( World world, TileEntity remoteTE, boolean isDestroying ) {

        TileEntityAssociation tea = null; // "I can't stand tea"

        if ( remoteTE instanceof TileEntityAssociation ) {
            tea = (TileEntityAssociation) remoteTE;
            removeLink( tea );
        }

        return tea;

    }

    private static void removeLink( TileEntityAssociation tile ) {

        if ( tile instanceof TileEntityReceiver ) {

            TileEntityReceiver receiver = (TileEntityReceiver) tile;
            receiver.getTank( 0 ).setFluid( FluidStack.EMPTY );

        } else if ( tile instanceof TileEntityTransmitter ) { // It really can't be anything else, but ok

            TileEntityTransmitter transmitter = (TileEntityTransmitter) tile;

            tile.world.getCapability( BiomeDiversity.TRANSMITTER_NETWORK_CAPABILITY, null ).ifPresent( cap -> {

                TransmitterData pd = cap.getTransmitter( transmitter.getOwner(), transmitter.getIdentifier() );
                BiomeDiversity.LOGGER.warn( "** Transmitterizing " + pd.getAmount() );
                transmitter.getTank( 0 ).setFluid( pd.fluid == null ? FluidStack.EMPTY : new FluidStack( pd.fluid, pd.getAmount() ) );

            } );

        }

        clearAssociation( tile );

    }

    private static void clearAssociation( TileEntityAssociation tile ) {

        tile.setAssociatedIdentifier( null );
        tile.setAssociatedPos( null );

        updateBlockStateForAntenna( tile, false );

        tile.markDirty();

    }

    private static void updateBlockStateForAntenna( TileEntityAssociation tile, boolean connected ) {

        tile.getWorld().setBlockState( tile.getPos(), tile.getWorld().getBlockState( tile.getPos() ).with( IS_CONNECTED, connected ), 2 );

    }

    // Create Links

    @Nullable
    public UUID getAssociatedIdentifier() {

        return this.associatedIdentifier;
    }

    public void setAssociatedIdentifier( @Nullable UUID associatedIdentifier ) {

        this.associatedIdentifier = associatedIdentifier;
    }

    @Nullable
    public BlockPos getAssociatedPos() {

        return this.associatedPos;
    }

    public void setAssociatedPos( @Nullable final BlockPos pos ) {

        this.associatedPos = pos;
    }

    public UUID getIdentifier() {

        return this.identifier;
    }

    // End Create Links

    // PreLinking

    public void setIdentifier( final UUID identifier ) {

        this.identifier = identifier;
    }

    public UUID getOwner() {

        return this.owner;
    }

    // End PreLinking

    // Remove Links

    //public boolean hasAssociation() {

    //    return this.associatedIdentifier != null;
    //}

    public void setOwner( final UUID owner ) {

        this.owner = owner;
    }

    public int getSource() {

        return this.source;
    }

    public String getSourceName() {

        return this.source == TileEntityTransmitter.LINKING_SOURCE_TRANSMITTER ? "transmitter" : "receiver";

    }

    public String getTargetName() {

        return this.source == TileEntityTransmitter.LINKING_SOURCE_TRANSMITTER ? "receiver" : "transmitter";

    }

    public boolean hasLink() {

        return (this.associatedIdentifier != null && this.associatedPos != null);

    }

    /**
     * This controls whether the tile entity gets replaced whenever the block state is changed. Normally only want this when block actually is replaced.
     */

    /*

    @Override
    public boolean shouldRefresh( World world, BlockPos pos, BlockState oldState, BlockState newState ) {

        return (oldState.getBlock() != newState.getBlock());

    }

    */
    protected void doFirstTick() {

        firstTick = false;

        if ( !this.getWorld().isRemote ) {
            updateBlockStateForAntenna( this, this.hasLink() );
        } else {
            Network.channel.sendToServer( new GenericTilePacketRequest( this.pos ) );
        }

    }

    // End Remove Links

}
