package ommina.biomediversity.fluids;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import ommina.biomediversity.BiomeDiversity;

public class DeferredRegistration {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create( ForgeRegistries.BLOCKS, BiomeDiversity.MODID );
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create( ForgeRegistries.ITEMS, BiomeDiversity.MODID );
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create( ForgeRegistries.FLUIDS, BiomeDiversity.MODID );
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create( ForgeRegistries.FEATURES, BiomeDiversity.MODID );

    public static void setup() {

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        BLOCKS.register( modEventBus );
        ITEMS.register( modEventBus );
        FLUIDS.register( modEventBus );
        FEATURES.register( modEventBus );

    }

}
/*
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

    public static final RegistryObject<Block> ROCK_BLOCK = BLOCKS.register("rock", () -> new Block(Block.Properties.create( Material.ROCK)));

    public ExampleMod() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
*/