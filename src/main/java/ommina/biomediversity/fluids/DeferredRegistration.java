package ommina.biomediversity.fluids;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import ommina.biomediversity.BiomeDiversity;

public class DeferredRegistration {

    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>( ForgeRegistries.BLOCKS, BiomeDiversity.MODID );
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>( ForgeRegistries.ITEMS, BiomeDiversity.MODID );
    public static final DeferredRegister<Fluid> FLUIDS = new DeferredRegister<>( ForgeRegistries.FLUIDS, BiomeDiversity.MODID );

    public static void setup() {

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        DeferredRegistration.BLOCKS.register( modEventBus );
        DeferredRegistration.ITEMS.register( modEventBus );
        DeferredRegistration.FLUIDS.register( modEventBus );

    }

}
