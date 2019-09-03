package ommina.biomediversity;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class BucketTinter implements IItemColor {

    @Override
    public int getColor( ItemStack itemStack, int i ) {
        return (new Color( 200, 0, 0 ).getRGB());
    }

}
