package ommina.biomediversity.items;

import net.minecraft.item.Item;
import ommina.biomediversity.BiomeDiversity;

public class GenericItem extends Item {

    public GenericItem( String name, Item.Properties properties ) {
        super( properties.group( BiomeDiversity.TAB ) );

        setRegistryName( BiomeDiversity.getId( name ) );

    }

}
