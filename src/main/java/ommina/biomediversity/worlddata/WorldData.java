
package ommina.biomediversity.worlddata;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import ommina.biomediversity.BiomeDiversity;

import javax.annotation.Nonnull;
import java.util.UUID;

public class WorldData extends WorldSavedData {

    private static final String DATA_NAME = BiomeDiversity.MODID + "_PillarNetwork";

    public WorldData() {

        super( DATA_NAME );
    }

    public WorldData( String s ) {

        super( s );
    }

    @Override
    public void read( CompoundNBT nbt ) {

        for ( String key : nbt.keySet() ) {

            try {

                UUID playerIdentifier = UUID.fromString( key );
                CompoundNBT subPlayer = nbt.getCompound( key );

                for ( String keySub : subPlayer.keySet() ) {

                    UUID transmitterIdentifier = UUID.fromString( keySub );

                    if ( transmitterIdentifier != null ) {
                        PillarData pd = PillarNetwork.getPillar( playerIdentifier, transmitterIdentifier );
                        CompoundNBT subPillar = subPlayer.getCompound( keySub );

                        pd.setAmount( subPillar.getInt( "quantity" ) );
                        pd.rainfall = subPillar.getFloat( "humidity" );
                        pd.temperature = subPillar.getFloat( "temperature" );
                        pd.biomeId = subPillar.getInt( "biomeid" );
                        pd.receiver = subPillar.getUniqueId( "receiver" );

                        if ( subPillar.contains( "fluidname" ) )
                            System.out.println();
                        //pd.fluid = FluidRegistry.getFluid( subPillar.getString( "fluidname" ) );
                    } else {
                        BiomeDiversity.LOGGER.warn( "Transmitter identifier retrieved from WorldSavedData is null.  Ignoring." );
                    }

                }

            } catch ( IllegalArgumentException e ) {

                BiomeDiversity.LOGGER.warn( "Player identifier retrieved from WorldSavedData is not a valid UUID.  This is a sign of impending doom.  Ignoring." );

            }

        }

    }

    @Override
    public CompoundNBT write( @Nonnull CompoundNBT compound ) {

        if ( PillarNetwork.isEmpty() )
            return compound;

        for ( UUID playerIdentifier : PillarNetwork.getPlayerList() ) {

            if ( playerIdentifier == null ) {

                BiomeDiversity.LOGGER.warn( "WTH?  playerIdentifier is null" );
                BiomeDiversity.LOGGER.warn( "Transmitter network contains " + PillarNetwork.getPlayerList().size() + " entries" );

                int n = 1;

                for ( UUID p2 : PillarNetwork.getPlayerList() ) {
                    BiomeDiversity.LOGGER.warn( " entry: " + n++ );
                    BiomeDiversity.LOGGER.warn( " p2: " + (p2 == null ? "null" : p2.toString()) );
                    BiomeDiversity.LOGGER.warn( " pillarCount: " + PillarNetwork.getTransmitterList( p2 ).size() );
                }

            } else {

                CompoundNBT subTagPlayer = new CompoundNBT();

                int n = 0;

                for ( UUID transmitterIdentifier : PillarNetwork.getTransmitterList( playerIdentifier ) ) {

                    n++;

                    if ( transmitterIdentifier == null ) {

                        BiomeDiversity.LOGGER.warn( " pillarIdentifier is null.  This should not happen." );
                        BiomeDiversity.LOGGER.warn( " entry: " + n );
                        BiomeDiversity.LOGGER.warn( " owner player uuid: " + playerIdentifier.toString() );
                        BiomeDiversity.LOGGER.warn( " pillarCount: " + PillarNetwork.getTransmitterList( playerIdentifier ).size() );

                    } else {

                        PillarData pd = PillarNetwork.getPillar( playerIdentifier, transmitterIdentifier );
                        CompoundNBT subTagTransmitter = new CompoundNBT();

                        subTagTransmitter.putInt( "quantity", pd.getAmount() );
                        subTagTransmitter.putFloat( "humidity", pd.rainfall );
                        subTagTransmitter.putFloat( "temperature", pd.temperature );
                        subTagTransmitter.putInt( "biomeid", pd.biomeId );

                        if ( pd.receiver != null )
                            subTagTransmitter.putUniqueId( "receiver", pd.receiver );

                        if ( pd.fluid != null )
                            subTagTransmitter.putString( "fluidname", pd.fluid.getName() );

                        subTagPlayer.put( transmitterIdentifier.toString(), subTagTransmitter ); // NPE here, non-crashing

                    }

                }

                compound.put( playerIdentifier.toString(), subTagPlayer );

            }

        }

        return compound;

    }

    public static WorldData get( World world ) {

        /*

        MapStorage storage = world.getPerWorldStorage();
        WorldData instance = (WorldData) storage.getOrLoadData( WorldData.class, DATA_NAME );

        if ( instance == null ) {
            instance = new WorldData();
            storage.setData( DATA_NAME, instance );
        }

        return instance;

        */

        return null;

    }

}
