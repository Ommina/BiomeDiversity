package ommina.biomediversity.world.gen.feature;

import net.minecraft.world.gen.carver.WorldCarver;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.world.gen.carver.FluidWellWorldCarver;

@ObjectHolder( BiomeDiversity.MODID )
@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD )
public class ModCarvers {

    @ObjectHolder( "fluid_well_carver" ) public static WorldCarver<ProbabilityConfig> FLUID_WELL_CARVER;

    @SubscribeEvent
    public static void register( final RegistryEvent.Register<WorldCarver<?>> event ) {

        event.getRegistry().register( new FluidWellWorldCarver( ProbabilityConfig::deserialize ).setRegistryName( "fluid_well_carver" ) );

        ModStructurePieceType.init();

    }

}
