package ommina.biomediversity.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.fluids.FluidStrengths;
import ommina.biomediversity.items.ModItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@JeiPlugin
public class JustEnoughItems implements IModPlugin {

    static final ResourceLocation GUI_TEXTURE = BiomeDiversity.getId( "textures/gui/gui_blank.png" );

    private static final ResourceLocation PLUGIN_UID = BiomeDiversity.getId( "plugin/main" );

    //region Overrides
    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_UID;
    }

    @Override
    public void registerCategories( IRecipeCategoryRegistration reg ) {

        IGuiHelper guiHelper = reg.getJeiHelpers().getGuiHelper();

        reg.addRecipeCategories(
             new TransmitterCategory( guiHelper )
        );

    }

    @Override
    public void registerRecipes( IRecipeRegistration registration ) {

        registration.addRecipes( FluidStrengths.getRecipes(), TransmitterCategory.ID );

        //addInfoPage( registration, PressCraftingCategory.BASE_ITEM.getItem() );

    }

    @Override
    public void registerRecipeCatalysts( IRecipeCatalystRegistration reg ) {
        reg.addRecipeCatalyst( new ItemStack( ModItems.TRANSMITTER ), TransmitterCategory.ID );
    }

    @Override
    public void onRuntimeAvailable( IJeiRuntime jeiRuntime ) {

        List<ItemStack> removals = new ArrayList<>();

        //removals.add( new ItemStack( ForgeRegistries.ITEMS.getValue( Wallpapercraft.getId( "pressstamp" ) ) ) );
        //removals.add( new ItemStack( ForgeRegistries.ITEMS.getValue( Wallpapercraft.getId( "pressjewel" ) ) ) );

        //jeiRuntime.getIngredientManager().removeIngredientsAtRuntime( VanillaTypes.ITEM, removals );

    }
//endregion Overrides

    private static void addInfoPage( IRecipeRegistration reg, IItemProvider item ) {

        String key = getDescKey( Objects.requireNonNull( item.asItem().getRegistryName() ) );
        ItemStack stack = new ItemStack( item );

        reg.addIngredientInfo( stack, VanillaTypes.ITEM, key );

    }

    private static List<IRecipe> getRecipesOfType( IRecipeType<?> recipeType ) {
        return Minecraft.getInstance().world.getRecipeManager().getRecipes().stream().filter( r -> r.getType() == recipeType ).collect( Collectors.toList() );
    }

    private static String getDescKey( ResourceLocation name ) {
        return "jei." + name.getNamespace() + "." + name.getPath() + ".desc";
    }

}