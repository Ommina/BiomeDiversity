
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

                    UUID pillarIdentifier = UUID.fromString( keySub );

                    if ( pillarIdentifier != null ) {
                        PillarData pd = PillarNetwork.getPillar( playerIdentifier, pillarIdentifier );
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
                        BiomeDiversity.LOGGER.warn( "Pillar identifier retrieved from WorldSavedData is null.  Ignoring." );
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
                BiomeDiversity.LOGGER.warn( "TRANSMITTER network contains " + PillarNetwork.getPlayerList().size() + " entries" );

                int n = 1;

                for ( UUID p2 : PillarNetwork.getPlayerList() ) {
                    BiomeDiversity.LOGGER.warn( " entry: " + n++ );
                    BiomeDiversity.LOGGER.warn( " p2: " + (p2 == null ? "null" : p2.toString()) );
                    BiomeDiversity.LOGGER.warn( " pillarCount: " + PillarNetwork.getPillarList( p2 ).size() );
                }

            } else {

                CompoundNBT subTagPlayer = new CompoundNBT();

                int n = 0;

                for ( UUID pillarIdentifier : PillarNetwork.getPillarList( playerIdentifier ) ) {

                    n++;

                    if ( pillarIdentifier == null ) {

                        BiomeDiversity.LOGGER.warn( " pillarIdentifier is null.  This should not happen." );
                        BiomeDiversity.LOGGER.warn( " entry: " + n );
                        BiomeDiversity.LOGGER.warn( " owner player uuid: " + playerIdentifier.toString() );
                        BiomeDiversity.LOGGER.warn( " pillarCount: " + PillarNetwork.getPillarList( playerIdentifier ).size() );

                    } else {

                        PillarData pd = PillarNetwork.getPillar( playerIdentifier, pillarIdentifier );
                        CompoundNBT subTagPillar = new CompoundNBT();

                        subTagPillar.putInt( "quantity", pd.getAmount() );
                        subTagPillar.putFloat( "humidity", pd.rainfall );
                        subTagPillar.putFloat( "temperature", pd.temperature );
                        subTagPillar.putInt( "biomeid", pd.biomeId );

                        if ( pd.receiver != null )
                            subTagPillar.putUniqueId( "receiver", pd.receiver );

                        if ( pd.fluid != null )
                            subTagPillar.putString( "fluidname", pd.fluid.getName() );

                        subTagPlayer.put( pillarIdentifier.toString(), subTagPillar ); // NPE here, non-crashing

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
