package ommina.biomediversity.blocks.receiver;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.network.PacketDistributor;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.ModTileEntities;
import ommina.biomediversity.blocks.tile.TileEntityAssociation;
import ommina.biomediversity.config.Config;
import ommina.biomediversity.fluids.BdFluidTank;
import ommina.biomediversity.network.BroadcastHelper;
import ommina.biomediversity.network.GenericTankPacket;
import ommina.biomediversity.network.ITankBroadcast;
import ommina.biomediversity.network.Network;
import ommina.biomediversity.worlddata.TransmitterData;
import ommina.biomediversity.worlddata.capabilities.ITransmitterNetwork;
import ommina.biomediversity.worlddata.capabilities.TransmitterNetworkProvider;

public class TileEntityReceiver extends TileEntityAssociation implements ITickableTileEntity, ITankBroadcast {

    private static final int TANK_COUNT = 1;
    private static final int MINIMUM_DELTA = 200;
    private final BroadcastHelper BROADCASTER = new BroadcastHelper( TANK_COUNT, MINIMUM_DELTA, this );
    private final BdFluidTank TANK = new BdFluidTank( Config.transmitterCapacity.get() );

    private int lastAmount = 0;
    private int power;
    private String biomeId;
    private float temperature;
    private float rainfall;

    public TileEntityReceiver() {
        super( ModTileEntities.RECEIVER );
    }

    @Override
    public void tick() {

        if ( firstTick )
            doFirstTick();

    }

    @Override
    protected void doFirstTick() {
        super.doFirstTick();

        if ( !world.isRemote ) {
            if ( this.getOwner() == null )
                BiomeDiversity.LOGGER.warn( "Receiver has null owner at: " + this.getPos() );
            else
                refreshReceiverTankFromPillarNetwork();
        }

        BROADCASTER.reset();

    }

    public void refreshReceiverTankFromPillarNetwork() {

        LazyOptional<ITransmitterNetwork> t = world.getCapability( BiomeDiversity.TRANSMITTER_NETWORK_CAPABILITY, null );

        TransmitterData pd = ((ITransmitterNetwork) t).getTransmitter( this.getOwner(), this.getIdentifier() );

        if ( pd.fluid != null ) {

            this.getTank( 0 ).setFluid( new FluidStack( pd.fluid, pd.getAmount() ) );

            lastAmount = pd.getAmount();

            if ( !FluidStrengths.contains( pd.fluid.hashCode() ) ) {
                BiomeDiversity.LOGGER.warn( "Fluid network contains a fluid with hash " + pd.fluid.hashCode() + ", but it is not in the config.  Perhaps it was removed?  Setting power to 1.00.  BiomeId: " + pd.biomeId );
                power = 1;
            } else {
                power = FluidStrengths.getStrength( pd.fluid.hashCode() );
            }

            biomeId = pd.biomeId.toString();
            temperature = pd.temperature;
            rainfall = pd.rainfall;

        }

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

}