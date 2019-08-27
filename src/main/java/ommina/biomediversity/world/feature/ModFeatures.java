package ommina.biomediversity.world.feature;

import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.event.RegistryEvent;

public class ModFeatures {

    public static JunglePoolFeature JUNGLE_POOL;// = register( "swamp_hut", new SwampHutStructure( NoFeatureConfig::deserialize ) );

    public static void register( final RegistryEvent.Register<Feature<?>> event ) {

        JunglePoolFeature jpf = new JunglePoolFeature( "jungle_pool", NoFeatureConfig::deserialize );

        event.getRegistry().register( jpf );

        JUNGLE_POOL = jpf;

    }

}
