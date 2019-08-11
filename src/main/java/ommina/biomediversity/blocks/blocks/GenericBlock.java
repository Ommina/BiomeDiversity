package ommina.biomediversity.blocks.blocks;

import net.minecraft.block.Block;
import ommina.biomediversity.BiomeDiversity;

public class GenericBlock extends Block {

    public GenericBlock( String name, Block.Properties properties ) {
        super( properties );

        setRegistryName( BiomeDiversity.getId( name ) );

    }

}
