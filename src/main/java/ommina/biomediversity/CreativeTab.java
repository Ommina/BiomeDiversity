package ommina.biomediversity;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import ommina.biomediversity.items.ModItems;

public class CreativeTab extends ItemGroup {

    public CreativeTab() {
        super( "biomediversity" );
    }

    @Override
    public ItemStack createIcon() {

        return new ItemStack( ModItems.linkStaff, 1 );
    }


}
