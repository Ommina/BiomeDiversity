package ommina.biomediversity.world.gen;

import net.minecraft.block.BlockState;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.ModBlocks;
import ommina.biomediversity.blocks.fluidwell.TileEntityFluidWell;
import ommina.biomediversity.config.Config;

import java.util.Random;

public class FluidWellGeneration {

    public static final int BASE_HEIGHT = 38; // centre of sphere
    public static final float FREQUENCY_MULTIPLIER = 0.002f;
    public static final float BASE_FREQUENCY = 0.0039f;

    private static final int MINIMUM_DISTANCE_TO_STRONGHOLD = 100;
    private static final int MINIMUM_DISTANCE_TO_MINESHAFT = 16;
    private static final int MAXIMUM_RADIUS = 18;

    public static void createSphere( World world, BlockPos tilePos ) {

        if ( BiomeDiversity.DEBUG )
            BiomeDiversity.LOGGER.info( String.format( "Considering fluid well generation at %s,%s", tilePos.getX(), tilePos.getZ() ) );

        Tuple<Integer, Integer> hr = findHeightAndRadius( world, tilePos.getX(), tilePos.getZ() );

        if ( hr.getA() == Integer.MIN_VALUE || hr.getB() == Integer.MIN_VALUE ) {
            if ( BiomeDiversity.DEBUG )
                BiomeDiversity.LOGGER.info( String.format( "  But the height/radius check failed." ) );
            return;
        }

        int radius = hr.getB();

        BlockPos centre = new BlockPos( tilePos.getX(), hr.getA(), tilePos.getZ() );

        if ( BiomeDiversity.DEBUG )
            BiomeDiversity.LOGGER.info( String.format( "  And creating the fluid well at %s with radius %s ...", centre.toString(), radius ) );

        createSphere( world, centre, radius );

        if ( BiomeDiversity.DEBUG )
            BiomeDiversity.LOGGER.info( "   ... done." );

    }

    private static void createSphere( World world, BlockPos centre, int radius ) {

        Random random = world.getRandom();

        int lightLayers = random.nextInt( 5 );
        int heavyLayers = random.nextInt( 6 );

        BiomeDiversity.LOGGER.info( String.format( "  Light %s  Heavy %s", lightLayers, heavyLayers ) );

        BlockState blockState;

        for ( int x = -radius; x <= radius; x++ )
            for ( int y = -radius; y <= radius; y++ )
                for ( int z = -radius; z <= radius; z++ )
                    if ( Math.sqrt( (x * x) + (y * y) + (z * z) ) < radius ) {
                        BlockPos pos = centre.add( x, y, z );

                        if ( y >= radius - lightLayers )
                            blockState = ModBlocks.MINERALWATER_LIGHT.getDefaultState();
                        else if ( y <= -radius + heavyLayers )
                            blockState = ModBlocks.MINERALWATER_HEAVY.getDefaultState();
                        else
                            blockState = ModBlocks.MINERALWATER.getDefaultState();

                        if ( isReplaceable( world, pos ) )
                            setBlockState( world, blockState, pos );
                        else
                            BiomeDiversity.LOGGER.warn( "notReplaceable" );

                    }

    }

    private static boolean isReplaceable( IWorld world, BlockPos pos ) {

        if ( world.getTileEntity( pos ) != null && !(world.getTileEntity( pos ) instanceof TileEntityFluidWell) )
            return false;

        if ( world.getBlockState( pos ).getBlock().getDefaultState().getBlockHardness( world, pos ) >= 0 )
            return true;

        return false;

    }

    private static void setBlockState( IWorld world, BlockState blockstate, BlockPos blockpos ) {

        /*

        if ( !world.setBlockState( blockpos, blockstate, 2 ) )
            BiomeDiversity.LOGGER.warn( "setBlockstate Failed at " + blockpos.toString() );

        IFluidState ifluidstate = world.getFluidState( blockpos );

        if ( !ifluidstate.isEmpty() )
            world.getPendingFluidTicks().scheduleTick( blockpos, ifluidstate.getFluid(), 0 );

            */

    }

    private static Tuple<Integer, Integer> findHeightAndRadius( World world, int x, int z ) {

        Random random = world.getRandom();

        int radiusStart = Config.fluidWellGenerationRadiusBase.get(); // sphere starts with a radius of 13 (at present)

        radiusStart += random.nextFloat() >= 0.5 ? 1 : -1; // plus a bit of luck (good or bad) -> radius is now 12 or 14

        if ( world.getBiome( new BlockPos( x, BASE_HEIGHT, z ) ).isHighHumidity() )
            radiusStart++; // bonus of one radius for rainy biomes [swampland, mushroom, jungle in vanilla] -> 13 or 15

        for ( int height = BASE_HEIGHT, radius = radiusStart; radius >= 5; height--, radius-- ) {
            if ( BASE_HEIGHT == 90 || seemsValid( world, x, z, height, radius ) )
                return new Tuple<Integer, Integer>( height, radius );
        }

        return new Tuple<Integer, Integer>( Integer.MIN_VALUE, Integer.MIN_VALUE );

    }

    private static boolean seemsValid( IWorld world, int worldX, int worldZ, int height, int radius ) {

        for ( int x = -1; x <= 1; x++ )
            for ( int z = -1; z <= 1; z++ )
                if ( GeneratorHelper.getTopSolidBlock( world, new BlockPos( worldX + x * 5, height, worldZ + z * 5 ) ).getY() <= height + radius )
                    return false;

        return true;

    }

}
