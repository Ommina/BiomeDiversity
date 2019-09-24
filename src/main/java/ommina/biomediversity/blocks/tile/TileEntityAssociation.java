package ommina.biomediversity.blocks.tile;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.receiver.TileEntityReceiver;
import ommina.biomediversity.blocks.transmitter.TileEntityTransmitter;
import ommina.biomediversity.network.GenericTankPacketRequest;
import ommina.biomediversity.network.Network;
import ommina.biomediversity.util.NbtUtils;
import ommina.biomediversity.world.chunkloader.ChunkLoader;
import ommina.biomediversity.worlddata.TransmitterData;
import ommina.biomediversity.worlddata.capabilities.ITransmitterNetwork;
import ommina.biomediversity.worlddata.capabilities.TransmitterNetworkProvider;

import java.util.UUID;

public abstract class TileEntityAssociation extends TileEntity {

    private static final BooleanProperty IS_CONNECTED = BooleanProperty.create( "connected" );

    protected UUID owner;
    protected UUID associatedIdentifier = null;
    protected BlockPos associatedPos = null;
    protected int hash = 0;
    protected int source = 0;
    protected boolean firstTick = true;
    private UUID identifier;

    public TileEntityAssociation( TileEntityType<?> tile ) {
        super( tile );

        this.identifier = UUID.randomUUID();

    }

    // Overrides

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

        UUID identifierPillar;
        UUID identifierReceiver;
        UUID owner;

        if ( tile instanceof TileEntityTransmitter ) {
            identifierPillar = tile.getIdentifier();
            identifierReceiver = tea.getIdentifier();
            owner = tile.getOwner();
        } else {
            identifierReceiver = tile.getIdentifier();
            identifierPillar = tea.getIdentifier();
            owner = tea.getOwner();
        }

        createLinkComplete( world, owner, identifierPillar, identifierReceiver );

        //if ( tile instanceof TileEntityReceiver )
        //    ((TileEntityReceiver) tile).refreshReceiverTankFromPillarNetwork(); //TODO: 14.4 port -- requires Receiver Tile update
        //else if ( remoteTile instanceof TileEntityReceiver )
        //    ((TileEntityReceiver) remoteTile).refreshReceiverTankFromPillarNetwork();

        if ( shouldUnload )
            ChunkLoader.releaseSingle( world, remotePos );

        tile.markDirty();

    }

    private static void createLinkComplete( World world, UUID owner, UUID identifierPillar, UUID identifierReceiver ) {

        TransmitterData pd = ((ITransmitterNetwork) world.getCapability( BiomeDiversity.TRANSMITTER_NETWORK_CAPABILITY, null )).getTransmitter( owner, identifierPillar );

        pd.receiver = identifierReceiver;

    }

    private static TileEntityAssociation createLink( World world, TileEntity remoteTE, TileEntityAssociation tile ) {

        TileEntityAssociation remoteTile = null;

        if ( remoteTE instanceof TileEntityAssociation ) {
            remoteTile = (TileEntityAssociation) remoteTE;

            createLink( remoteTile, tile.getIdentifier(), tile.getPos() );
        }

        return remoteTile;

    }

    // End Overrides

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

        if ( world.isBlockLoaded( remotePos ) ) {
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

        UUID identifierPillar;
        UUID identifierReceiver;
        UUID owner;

        if ( tile instanceof TileEntityTransmitter ) {
            identifierPillar = tile.getIdentifier();
            identifierReceiver = tea.getIdentifier();
            owner = tile.getOwner();
        } else {
            identifierReceiver = tile.getIdentifier();
            identifierPillar = tea.getIdentifier();
            owner = tea.getOwner();
        }

        removeLinkComplete( world, owner, identifierPillar, identifierReceiver );

        if ( !isDestroying )
            removeLink( tile );

    }

    private static void removeLinkComplete( World world, UUID owner, UUID identifierPillar, UUID identifierReceiver ) {

        LazyOptional<ITransmitterNetwork> t = world.getCapability( BiomeDiversity.TRANSMITTER_NETWORK_CAPABILITY, null );

        TransmitterData pd = ((ITransmitterNetwork) t).getTransmitter( owner, identifierPillar );

        if ( pd.receiver == null ) {
            BiomeDiversity.LOGGER.error( "Receiver is null when attempting to unlink a PillarNetwork transmitter/receiver pair" );
        }

        pd.receiver = null;

        //TransmitterNetwork.markDirty( world );

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
            //receiver.getTank().setFluid( null ); //TODO: 14.4 --> requires Receiver update

        } else if ( tile instanceof TileEntityTransmitter ) { // It really can't be anything else, but ok

            TileEntityTransmitter transmitter = (TileEntityTransmitter) tile;
            LazyOptional<ITransmitterNetwork> t = transmitter.world.getCapability( BiomeDiversity.TRANSMITTER_NETWORK_CAPABILITY, null );

            TransmitterData pd = ((ITransmitterNetwork) t).getTransmitter( transmitter.getOwner(), transmitter.getIdentifier() );

            //Biomediversity.logger.warn( "** Pillarizing " + pd.getAmount() );
            // pillar.getTank().setFluid( pd.fluid, pd.getAmount() ); //TODO: 14.4 --> requires Transmitter update

        }

        clearAssociation( tile );

    }

    private static void clearAssociation( TileEntityAssociation tile ) {

        tile.setAssociatedIdentifier( null );
        tile.setAssociatedPos( null );

        updateBlockStateForAntenna( tile, false );

        tile.markDirty();

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
            Network.channel.sendToServer( new GenericTankPacketRequest( this.pos ) );
        }

    }

    private static void updateBlockStateForAntenna( TileEntityAssociation tile, boolean connected ) {

        tile.getWorld().setBlockState( tile.getPos(), tile.getWorld().getBlockState( tile.getPos() ).with( IS_CONNECTED, connected ), 2 );

    }

    public boolean hasLink() {

        return (this.associatedIdentifier != null);

    }

    @Override
    public void read( final CompoundNBT compound ) {

        identifier = compound.getUniqueId( "identifier" );

        if ( compound.hasUniqueId( "owner" ) )
            owner = compound.getUniqueId( "owner" );

        if ( compound.hasUniqueId( "associatedidentifier" ) ) {
            associatedIdentifier = compound.getUniqueId( "associatedidentifier" );
            associatedPos = NbtUtils.getBlockPosFromNbt( compound );
        }

        super.read( compound );

    }

    @Override
    public CompoundNBT write( final CompoundNBT compound ) {

        compound.putUniqueId( "identifier", identifier );

        if ( owner != null )
            compound.putUniqueId( "owner", owner );

        if ( associatedIdentifier != null )
            compound.putUniqueId( "associatedidentifier", associatedIdentifier );

        if ( associatedPos != null )
            NbtUtils.addBlockPosToNbt( compound, associatedPos );

        return super.write( compound );

    }

    // Create Links

    public UUID getIdentifier() {

        return this.identifier;
    }

    public void setIdentifier( final UUID identifier ) {

        this.identifier = identifier;
    }

    public UUID getOwner() {

        return this.owner;
    }

    public void setOwner( final UUID owner ) {

        this.owner = owner;
    }

    public BlockPos getAssociatedPos() {

        return this.associatedPos;
    }

    // End Create Links

    // PreLinking

    public void setAssociatedPos( final BlockPos pos ) {

        this.associatedPos = pos;
    }

    public UUID getAssociatedIdentifier() {

        return this.associatedIdentifier;
    }

    // End PreLinking

    // Remove Links

    public void setAssociatedIdentifier( final UUID associatedIdentifier ) {

        this.associatedIdentifier = associatedIdentifier;
    }

    public boolean hasAssociation() {

        return this.associatedIdentifier != null;
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

    // End Remove Links

}
