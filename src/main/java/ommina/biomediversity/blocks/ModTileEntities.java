package ommina.biomediversity.blocks;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ObjectHolder;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.pillar.TileEntityPillar;

public class ModTileEntities {

    @ObjectHolder( BiomeDiversity.MODID + ":pillar" ) public static TileEntityType<TileEntityPillar> PILLAR;

    public static void register( final RegistryEvent.Register<TileEntityType<?>> event ) {

        event.getRegistry().register( TileEntityType.Builder.create( TileEntityPillar::new, ModBlocks.pillar ).build( null ).setRegistryName( "pillar" ) );

    }

}
