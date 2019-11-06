package ommina.biomediversity.world.gen.feature;

import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.world.gen.structure.FluidWellStructure;

@ObjectHolder( BiomeDiversity.MODID )
@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD )
public class ModFeatures {

    @ObjectHolder( "jungle_pool" ) public static JunglePoolFeature JUNGLE_POOL;
    @ObjectHolder( "fluid_well" ) public static Structure<NoFeatureConfig> FLUID_WELL;
    @ObjectHolder( "pomegranate" ) public static PomegranateFeature POMEGRANTE;
    @ObjectHolder( "colza" ) public static ColzaFeature COLZA;

    @SubscribeEvent
    public static void register( final RegistryEvent.Register<Feature<?>> event ) {

        event.getRegistry().register( new JunglePoolFeature( "jungle_pool", NoFeatureConfig::deserialize ) );
        event.getRegistry().register( new FluidWellStructure( "fluid_well", NoFeatureConfig::deserialize ) );
        event.getRegistry().register( new PomegranateFeature( "pomegranate", NoFeatureConfig::deserialize ) );
        event.getRegistry().register( new ColzaFeature( "colza", NoFeatureConfig::deserialize ) );

        ModStructurePieceType.init();

    }

}

