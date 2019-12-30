package ommina.biomediversity.sounds;

import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import ommina.biomediversity.BiomeDiversity;

@ObjectHolder( BiomeDiversity.MODID )
@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD )
public class ModSounds {

    @ObjectHolder( "collector_running" ) public static SoundEvent COLLECTOR_RUNNING;

    @SubscribeEvent
    public static void register( RegistryEvent.Register<SoundEvent> event ) {

        event.getRegistry().register( new SoundEvent( BiomeDiversity.getId( "collector_running" ) ).setRegistryName( "collector_running" ) );

    }

}
