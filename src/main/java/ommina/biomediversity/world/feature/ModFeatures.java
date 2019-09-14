package ommina.biomediversity.world.feature;

import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.world.structure.FluidWellStructure;

@ObjectHolder( BiomeDiversity.MODID )
@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD )
public class ModFeatures {

    @ObjectHolder( "jungle_pool" ) public static JunglePoolFeature JUNGLE_POOL = new JunglePoolFeature( "jungle_pool", NoFeatureConfig::deserialize );
    @ObjectHolder( "fluid_well" ) public static Structure<NoFeatureConfig> FLUID_WELL = new FluidWellStructure( "fluid_well", NoFeatureConfig::deserialize );

    //public static final Structure<MineshaftConfig> MINESHAFT = register("mineshaft", new MineshaftStructure(MineshaftConfig::deserialize));
    //public static final Structure<?> MINESHAFT = register("Mineshaft", Feature.MINESHAFT);


    @SubscribeEvent
    public static void register( final RegistryEvent.Register<Feature<?>> event ) {

        event.getRegistry().register( JUNGLE_POOL );
        event.getRegistry().register( FLUID_WELL );

        ModStructures.init();
        ModStructurePieceType.init();

        Feature.STRUCTURES.put( BiomeDiversity.getId( "fluid_well" ).toString(), FLUID_WELL );

        //Registry.register( Registry.STRUCTURE_FEATURE, BiomeDiversity.getId( "fluid_well" ), FLUID_WELL );

    }

}

//public static final Structure<NoFeatureConfig> END_CITY = register("end_city", new EndCityStructure(NoFeatureConfig::deserialize));
//public static final Structure<BuriedTreasureConfig> BURIED_TREASURE = register("buried_treasure", new BuriedTreasureStructure(BuriedTreasureConfig::deserialize));
