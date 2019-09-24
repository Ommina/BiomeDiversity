package ommina.biomediversity.worlddata.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.registries.ForgeRegistries;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.worlddata.TransmitterData;

import javax.annotation.Nullable;
import java.util.UUID;

public class TransmitterNetworkStorage implements Capability.IStorage<ITransmitterNetwork> {

    /**
     * Serialize the capability instance to a NBTTag.
     * This allows for a central implementation of saving the data.
     * <p>
     * It is important to note that it is up to the API defining
     * the capability what requirements the 'instance' value must have.
     * <p>
     * Due to the possibility of manipulating internal data, some
     * implementations MAY require that the 'instance' be an instance
     * of the 'default' implementation.
     * <p>
     * Review the API docs for more info.
     *
     * @param capability The Capability being stored.
     * @param instance   An instance of that capabilities interface.
     * @param side       The side of the object the instance is associated with.
     * @return a NBT holding the data. Null if no data needs to be stored.
     */
    @Nullable
    @Override
    public INBT writeNBT( Capability<ITransmitterNetwork> capability, ITransmitterNetwork instance, Direction side ) {

        if ( instance.isEmpty() )
            return new CompoundNBT();

        CompoundNBT nbt = new CompoundNBT();

        for ( UUID playerIdentifier : instance.getPlayerList() ) {

            if ( playerIdentifier == null ) {

                BiomeDiversity.LOGGER.warn( "WTH?  playerIdentifier is null" );
                BiomeDiversity.LOGGER.warn( "Transmitter network contains " + instance.getPlayerList().size() + " entries" );

                int n = 1;

                for ( UUID p2 : instance.getPlayerList() ) {
                    BiomeDiversity.LOGGER.warn( " entry: " + n++ );
                    BiomeDiversity.LOGGER.warn( " p2: " + (p2 == null ? "null" : p2.toString()) );
                    BiomeDiversity.LOGGER.warn( " TransmitterCount: " + instance.getTransmitterList( p2 ).size() );
                }

            } else {

                CompoundNBT subTagPlayer = new CompoundNBT();

                int n = 0;

                for ( UUID transmitterIdentifier : instance.getTransmitterList( playerIdentifier ) ) {

                    n++;

                    if ( transmitterIdentifier == null ) {

                        BiomeDiversity.LOGGER.warn( " transmitterIdentifier is null.  This should not happen." );
                        BiomeDiversity.LOGGER.warn( " entry: " + n );
                        BiomeDiversity.LOGGER.warn( " owner player uuid: " + playerIdentifier.toString() );
                        BiomeDiversity.LOGGER.warn( " transmitterCount: " + instance.getTransmitterList( playerIdentifier ).size() );

                    } else {

                        TransmitterData pd = instance.getTransmitter( playerIdentifier, transmitterIdentifier );
                        CompoundNBT subTagTransmitter = new CompoundNBT();

                        subTagTransmitter.putInt( "quantity", pd.getAmount() );
                        subTagTransmitter.putFloat( "humidity", pd.rainfall );
                        subTagTransmitter.putFloat( "temperature", pd.temperature );
                        subTagTransmitter.putString( "biomeid", pd.biomeId.toString() );

                        if ( pd.receiver != null )
                            subTagTransmitter.putUniqueId( "receiver", pd.receiver );

                        if ( pd.fluid != null )
                            subTagTransmitter.putString( "fluidname", pd.fluid.getRegistryName().toString() );

                        subTagPlayer.put( transmitterIdentifier.toString(), subTagTransmitter ); // NPE here, non-crashing

                    }

                }

                nbt.put( playerIdentifier.toString(), subTagPlayer );

            }

        }

        return nbt;

    }

    /**
     * Read the capability instance from a NBT tag.
     * <p>
     * This allows for a central implementation of saving the data.
     * <p>
     * It is important to note that it is up to the API defining
     * the capability what requirements the 'instance' value must have.
     * <p>
     * Due to the possibility of manipulating internal data, some
     * implementations MAY require that the 'instance' be an instance
     * of the 'default' implementation.
     * <p>
     * Review the API docs for more info.         *
     *
     * @param capability The Capability being stored.
     * @param instance   An instance of that capabilities interface.
     * @param side       The side of the object the instance is associated with.
     * @param inbt       A NBT holding the data. Must not be null, as doesn't make sense to call this function with nothing to read...
     */
    @Override
    public void readNBT( Capability<ITransmitterNetwork> capability, ITransmitterNetwork instance, Direction side, INBT inbt ) {

        CompoundNBT nbt = (CompoundNBT) inbt;

        for ( String key : nbt.keySet() )

            try {

                UUID playerIdentifier = UUID.fromString( key );
                CompoundNBT subPlayer = nbt.getCompound( key );

                for ( String keySub : subPlayer.keySet() ) {

                    UUID transmitterIdentifier = UUID.fromString( keySub );

                    if ( transmitterIdentifier != null ) {
                        TransmitterData pd = instance.getTransmitter( playerIdentifier, transmitterIdentifier );
                        CompoundNBT subPillar = subPlayer.getCompound( keySub );

                        pd.setAmount( subPillar.getInt( "quantity" ) );
                        pd.rainfall = subPillar.getFloat( "humidity" );
                        pd.temperature = subPillar.getFloat( "temperature" );
                        pd.biomeId = new ResourceLocation( subPillar.getString( "biomeid" ) );
                        pd.receiver = subPillar.getUniqueId( "receiver" );
                        pd.fluid = ForgeRegistries.FLUIDS.getValue( new ResourceLocation( subPillar.getString( "fluidname" ) ) );

                    } else {
                        BiomeDiversity.LOGGER.warn( "Transmitter identifier retrieved from WorldSavedData is null.  Ignoring." );
                    }

                }

            } catch ( IllegalArgumentException e ) {

                BiomeDiversity.LOGGER.warn( "Player identifier retrieved from WorldSavedData is not a valid UUID.  This is a sign of impending doom.  Ignoring." );

            }

    }

}