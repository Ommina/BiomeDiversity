package ommina.biomediversity.integration.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.fluids.FluidStrengths;
import ommina.biomediversity.fluids.Recipe;
import ommina.biomediversity.gui.Control;
import ommina.biomediversity.items.ModItems;
import ommina.biomediversity.util.Translator;

import java.awt.*;

public class TransmitterCategory implements IRecipeCategory<Recipe> {

    public static final ResourceLocation ID = BiomeDiversity.getId( "category/transmitter" );

    private static final Point TANK_LOCATION = new Point( 3, 3 );
    private static final JeiTank TANK_FLUID = new JeiTank();

    private static final int GUI_WIDTH = 160;
    private static final int GUI_HEIGHT = 60;

    private final IDrawable background;
    private final IDrawable icon;

    public TransmitterCategory( IGuiHelper guiHelper ) {

        background = guiHelper.createBlankDrawable( GUI_WIDTH, GUI_HEIGHT );
        icon = guiHelper.createDrawableIngredient( new ItemStack( ModItems.TRANSMITTER ) );

        TANK_FLUID.setPostion( TANK_LOCATION );

    }

    //region Overrides
    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    @Override
    public Class<? extends Recipe> getRecipeClass() {
        return Recipe.class;
    }

    @Override
    public String getTitle() {
        return Translator.translateToLocal( "jei.biomediversity.transmitter" );
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients( Recipe recipe, IIngredients iIngredients ) {

        iIngredients.setInputs( VanillaTypes.FLUID, FluidStrengths.getAllFluids() );

    }

    @Override
    public void setRecipe( IRecipeLayout iRecipeLayout, Recipe recipe, IIngredients iIngredients ) {

        IGuiFluidStackGroup fluidStacks = iRecipeLayout.getFluidStacks();

        fluidStacks.init( 0, true, TANK_LOCATION.x, TANK_LOCATION.y, Control.Sizes.TANK.width, Control.Sizes.TANK.height, 1, true, null );

        fluidStacks.set( 0, FluidStrengths.getAllFluids() );

    }

    @Override
    public void draw( Recipe recipe, double mouseX, double mouseY ) {

        TANK_FLUID.drawBackgroundLayer( 0, 0 );

    }

//endregion Overrides

}