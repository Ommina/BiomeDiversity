package ommina.biomediversity.items;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import ommina.biomediversity.BiomeDiversity;

public class GenericBlockItem extends BlockItem {

    public GenericBlockItem( Block block, Item.Properties properties ) {
        super( block, properties.group( BiomeDiversity.TAB ) );

        setRegistryName( block.getRegistryName() );

    }

}
