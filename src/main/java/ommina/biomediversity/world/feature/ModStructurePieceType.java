package ommina.biomediversity.world.feature;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.world.structure.FluidWellStructurePiece;

public class ModStructurePieceType {

    public static IStructurePieceType FLUID_WELL = register( FluidWellStructurePiece::new, "fluid_well" );

    public static void init() {
    }

    static IStructurePieceType register( IStructurePieceType pieceType, String name ) {
        return Registry.register( Registry.STRUCTURE_PIECE, BiomeDiversity.getId( name ).toString(), pieceType );
    }

}
