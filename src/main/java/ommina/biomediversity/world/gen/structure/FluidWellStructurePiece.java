package ommina.biomediversity.world.gen.structure;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.IFluidState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.ModBlocks;
import ommina.biomediversity.config.Config;
import ommina.biomediversity.world.gen.GeneratorHelper;
import ommina.biomediversity.world.gen.feature.ModStructurePieceType;

import java.util.List;
import java.util.Random;

public class FluidWellStructurePiece extends StructurePiece {

    public FluidWellStructurePiece( BlockPos blockPos, int radius ) {
        super( ModStructurePieceType.FLUID_WELL, 0 );

        // radius++;

        this.boundingBox = new MutableBoundingBox( blockPos.getX() - radius, blockPos.getY() - radius, blockPos.getZ() - radius, blockPos.getX() + radius, blockPos.getY() + radius, blockPos.getZ() + radius );
        //this.boundingBox = new MutableBoundingBox( blockPos.getX() - radius, blockPos.getY() - radius, blockPos.getZ() - radius, blockPos.getX(), blockPos.getY(), blockPos.getZ() );

    }

    public FluidWellStructurePiece( TemplateManager templateManager, CompoundNBT nbt ) {
        super( ModStructurePieceType.FLUID_WELL, nbt );
    }

//region Overrides


    @Override
    protected void readAdditional( CompoundNBT tagCompound ) {
    }


    @Override
    public void buildComponent( StructurePiece structurePiece, List<StructurePiece> structurePieces, Random random ) {

        BiomeDiversity.LOGGER.info( "buildComponent" );

        super.buildComponent( structurePiece, structurePieces, random );
    }

    /**
     * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes Mineshafts at
     * the end, it adds Fences...
     */
    @Override
    public boolean func_225577_a_( IWorld world, ChunkGenerator<?> p_225577_2_, Random random, MutableBoundingBox boundingBox, ChunkPos chunkPos ) {

        int x = (this.boundingBox.minX + this.boundingBox.maxX) / 2;
        int z = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2;

        Tuple<Integer, Integer> hr = findHeightAndRadius( world, random, x, z );

        if ( hr.getA() == Integer.MIN_VALUE )
            return false;

        int y = FluidWellStructure.BASE_HEIGHT;

        BlockPos centrePos = new BlockPos( x, y, z );

        createSphere( world, centrePos, hr.getB(), FluidWellStructure.BASE_HEIGHT == 90 ? Blocks.GOLD_BLOCK : ModBlocks.MINERALWATER, this.boundingBox );

        return false;

    }
//endregion Overrides

    private void createSphere( IWorld world, BlockPos centre, int radius, Block block, MutableBoundingBox boundingBox ) {

        int attempts = 0;
        int failed = 0;

        if ( BiomeDiversity.DEBUG )
            BiomeDiversity.LOGGER.info( String.format( "Starting fluid well generation at %s with radius %s", centre.toString(), radius ) );

        for ( int x = -radius; x <= radius; x++ )
            for ( int y = -radius; y <= radius; y++ )
                for ( int z = -radius; z <= radius; z++ )
                    if ( Math.sqrt( (x * x) + (y * y) + (z * z) ) < radius ) {


                        attempts++;
                        BlockPos pos = centre.add( x, y, z );

                        if ( isReplaceable( world, pos ) )
                            failed += setBlockState( world, block.getDefaultState(), pos, boundingBox ) ? 0 : 1;
                        else
                            failed++;

                        if ( pos.getX() == -181 && pos.getY() == 35 && pos.getZ() == 175 )
                            BiomeDiversity.LOGGER.info( "  Placing This One" );

                    }

        if ( BiomeDiversity.DEBUG ) {
            BiomeDiversity.LOGGER.info( "  and done.  Total: " + attempts + " blocks placed." );
            if ( failed > 0 )
                BiomeDiversity.LOGGER.info( "    " + failed + " blocks failed to place!" );
        }

    }

    private boolean isReplaceable( IWorld world, BlockPos pos ) {

        if ( world.getTileEntity( pos ) != null )
            return false;

        if ( world.getBlockState( pos ).getBlock().getDefaultState().getBlockHardness( world, pos ) >= 0 )
            return true;

        return false;

    }

    protected boolean setBlockState( IWorld world, BlockState blockstate, BlockPos pos, MutableBoundingBox boundingbox ) {

        if ( boundingbox.isVecInside( pos ) ) {

            if ( pos.getX() == -181 && pos.getY() == 35 && pos.getZ() == 175 ) {
                BiomeDiversity.LOGGER.warn( " TestBlock is " + world.getBlockState( pos ).toString() );
            }

                if ( !world.setBlockState( pos, blockstate, 2 ) ) {
                BiomeDiversity.LOGGER.warn( "setBlockstate Failed at " + pos.toString() );
                return false;
            }

            IFluidState ifluidstate = world.getFluidState( pos );
            if ( !ifluidstate.isEmpty() ) {
                world.getPendingFluidTicks().scheduleTick( pos, ifluidstate.getFluid(), 0 );
            }

        } else {

            BiomeDiversity.LOGGER.warn( "isOutside " + pos.toString() );
            return false;

        }

        return true;
    }

    private static Tuple<Integer, Integer> findHeightAndRadius( IWorld world, Random random, int x, int z ) {

        int radiusStart = Config.fluidWellGenerationRadiusBase.get(); // sphere starts with a radius of 13 (at present)

        //radiusStart += random.nextFloat() >= 0.5 ? 1 : -1; // plus a bit of luck (good or bad) -> radius is now 12 or 14

        //if ( world.getBiome( new BlockPos( x, FluidWellStructure.BASE_HEIGHT, z ) ).isHighHumidity() )
        //    radiusStart++; // bonus of one radius for rainy biomes [swampland, mushroom, jungle in vanilla] -> 13 or 15

        for ( int height = FluidWellStructure.BASE_HEIGHT, radius = radiusStart; radius >= 5; height--, radius-- ) {
            if ( FluidWellStructure.BASE_HEIGHT == 90 || seemsValid( world, x, z, height, radius ) )
                return new Tuple<Integer, Integer>( height, radius );
        }

        return new Tuple<Integer, Integer>( Integer.MIN_VALUE, Integer.MIN_VALUE );

    }

    private static boolean seemsValid( IWorld world, int worldX, int worldZ, int height, int radius ) {

        // Work through nine points (3x3), multiplying by five to spread them out.
        // If ANY TopSolidBlock for a point is too low / deep into the ground, shrink the height AND the radius by one and try again.

        for ( int x = -1; x <= 1; x++ )
            for ( int z = -1; z <= 1; z++ )
                if ( GeneratorHelper.getTopSolidBlock( world, new BlockPos( worldX + x * 5, height, worldZ + z * 5 ) ).getY() <= height + radius )
                    return false;

        return true;

    }

}