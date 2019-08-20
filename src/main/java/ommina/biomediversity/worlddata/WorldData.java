
package ommina.biomediversity.worlddata;

import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fluids.FluidRegistry;
import ommina.biomediversity.Biomediversity;

public class WorldData extends WorldSavedData {

    private static final String DATA_NAME = Biomediversity.MODID + "_PillarNetwork";

    public WorldData() {

        super( DATA_NAME );
    }

    public WorldData( String s ) {

        super( s );
    }

    @Override
    public void readFromNBT( NBTTagCompound nbt ) {

        for ( String key : nbt.getKeySet() ) {

            try {

                UUID playerIdentifier = UUID.fromString( key );
                NBTTagCompound subPlayer = nbt.getCompoundTag( key );

                for ( String keySub : subPlayer.getKeySet() ) {

                    UUID pillarIdentifier = UUID.fromString( keySub );

                    if ( pillarIdentifier != null ) {
                        PillarData pd = PillarNetwork.getPillar( playerIdentifier, pillarIdentifier );
                        NBTTagCompound subPillar = subPlayer.getCompoundTag( keySub );

                        pd.setAmount( subPillar.getInteger( "quantity" ) );
                        pd.rainfall = subPillar.getFloat( "humidity" );
                        pd.temperature = subPillar.getFloat( "temperature" );
                        pd.biomeId = subPillar.getInteger( "biomeid" );
                        pd.receiver = subPillar.getUniqueId( "receiver" );

                        if ( subPillar.hasKey( "fluidname" ) )
                            pd.fluid = FluidRegistry.getFluid( subPillar.getString( "fluidname" ) );
                    } else {
                        Biomediversity.logger.warn( "Pillar identifier retrieved from WorldSavedData is null.  Ignoring." );
                    }

                }

            } catch ( IllegalArgumentException e ) {

                Biomediversity.logger.warn( "Player identifier retrieved from WorldSavedData is not a valid UUID.  This is a sign of impending doom.  Ignoring." );

            }

        }

    }

    @Override
    public NBTTagCompound writeToNBT( @Nonnull NBTTagCompound compound ) {

        if ( PillarNetwork.isEmpty() )
            return compound;

        for ( UUID playerIdentifier : PillarNetwork.getPlayerList() ) {

            if ( playerIdentifier == null ) {

                Biomediversity.logger.warn( "WTH?  playerIdentifier is null" );
                Biomediversity.logger.warn( "pillar network contains " + PillarNetwork.getPlayerList().size() + " entries" );

                int n = 1;

                for ( UUID p2 : PillarNetwork.getPlayerList() ) {
                    Biomediversity.logger.warn( " entry: " + n++ );
                    Biomediversity.logger.warn( " p2: " + (p2 == null ? "null" : p2.toString()) );
                    Biomediversity.logger.warn( " pillarCount: " + PillarNetwork.getPillarList( p2 ).size() );
                }

            } else {

                NBTTagCompound subTagPlayer = new NBTTagCompound();

                int n = 0;

                for ( UUID pillarIdentifier : PillarNetwork.getPillarList( playerIdentifier ) ) {

                    n++ ;

                    if ( pillarIdentifier == null ) {

                        Biomediversity.logger.warn( " pillarIdentifier is null.  This should not happen." );
                        Biomediversity.logger.warn( " entry: " + n );
                        Biomediversity.logger.warn( " owner player uuid: " + playerIdentifier.toString() );
                        Biomediversity.logger.warn( " pillarCount: " + PillarNetwork.getPillarList( playerIdentifier ).size() );

                    } else {

                        PillarData pd = PillarNetwork.getPillar( playerIdentifier, pillarIdentifier );
                        NBTTagCompound subTagPillar = new NBTTagCompound();

                        subTagPillar.setInteger( "quantity", pd.getAmount() );
                        subTagPillar.setFloat( "humidity", pd.rainfall );
                        subTagPillar.setFloat( "temperature", pd.temperature );
                        subTagPillar.setInteger( "biomeid", pd.biomeId );

                        if ( pd.receiver != null )
                            subTagPillar.setUniqueId( "receiver", pd.receiver );

                        if ( pd.fluid != null )
                            subTagPillar.setString( "fluidname", pd.fluid.getName() );

                        subTagPlayer.setTag( pillarIdentifier.toString(), subTagPillar ); // NPE here, non-crashing

                    }

                }

                compound.setTag( playerIdentifier.toString(), subTagPlayer );

            }

        }

        return compound;

    }

    public static WorldData get( World world ) {

        MapStorage storage = world.getPerWorldStorage();
        WorldData instance = (WorldData) storage.getOrLoadData( WorldData.class, DATA_NAME );

        if ( instance == null ) {
            instance = new WorldData();
            storage.setData( DATA_NAME, instance );
        }

        return instance;

    }

}
