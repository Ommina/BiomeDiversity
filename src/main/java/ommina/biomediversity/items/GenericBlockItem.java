package ommina.biomediversity.items;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.blocks.blocks.GenericBlock;

public class GenericBlockItem extends BlockItem {

    public GenericBlockItem( GenericBlock block, Item.Properties properties ) {
        super( block, properties.group( BiomeDiversity.TAB ) );

        setRegistryName( block.getRegistryName() );

    }

}
