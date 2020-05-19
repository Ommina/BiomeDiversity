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
import net.minecraftforge.fluids.FluidStack;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.rendering.RenderHelper;
import ommina.biomediversity.fluids.FluidStrengths;
import ommina.biomediversity.fluids.TransmitterFluidRecipe;
import ommina.biomediversity.gui.Control;
import ommina.biomediversity.integration.jei.controls.JeiLowHigh;
import ommina.biomediversity.integration.jei.controls.JeiTank;
import ommina.biomediversity.items.ModItems;
import ommina.biomediversity.util.Translator;

import java.awt.*;

import static ommina.biomediversity.config.Constants.DEFAULT_TEXT_COLOUR;

public class TransmitterCategory implements IRecipeCategory<TransmitterFluidRecipe> {

    public static final ResourceLocation ID = BiomeDiversity.getId( "category/transmitter" );

    private static final int GUI_WIDTH = 160;
    private static final int GUI_HEIGHT = 60;

    private static final Point TANK_LOCATION = new Point( 3, 3 );
    private static final Point TEXT_LOCATION = new Point( 20, 3 );
    private static final Point LOWHIGH_POSITION = new Point( GUI_WIDTH - Control.Sizes.LOW_HIGH.width - 3, 3 );

    private static final JeiTank TANK_FLUID = new JeiTank();
    private static final JeiLowHigh LOW_HIGH = new JeiLowHigh();

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable tankOverlay;

    public TransmitterCategory( IGuiHelper guiHelper ) {

        background = guiHelper.createBlankDrawable( GUI_WIDTH, GUI_HEIGHT );
        icon = guiHelper.createDrawableIngredient( new ItemStack( ModItems.TRANSMITTER ) );
        tankOverlay = guiHelper.createDrawable( Control.OVERLAY_RESOURCE, 0, 0, Control.Sizes.TANK.width, Control.Sizes.TANK.height );

        TANK_FLUID.setPostion( TANK_LOCATION );
        LOW_HIGH.setPostion( LOWHIGH_POSITION );

    }

    //region Overrides
    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    @Override
    public Class<? extends TransmitterFluidRecipe> getRecipeClass() {
        return TransmitterFluidRecipe.class;
    }

    @Override
    public String getTitle() {
        return Translator.translateToLocal( "jei.biomediversity.transmitter.title" );
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
    public void setIngredients( TransmitterFluidRecipe recipe, IIngredients iIngredients ) {
        iIngredients.setInputs( VanillaTypes.FLUID, FluidStrengths.getAllFluids() );
    }

    @Override
    public void setRecipe( IRecipeLayout iRecipeLayout, TransmitterFluidRecipe recipe, IIngredients iIngredients ) {

        IGuiFluidStackGroup fluidStacks = iRecipeLayout.getFluidStacks();

        fluidStacks.init( 0, true, TANK_LOCATION.x, TANK_LOCATION.y, Control.Sizes.TANK.width, Control.Sizes.TANK.height, 1, true, tankOverlay );

        fluidStacks.set( 0, new FluidStack( recipe.getFluid(), 1 ) );

    }

    @Override
    public void draw( TransmitterFluidRecipe recipe, double mouseX, double mouseY ) {

        TANK_FLUID.drawBackgroundLayer( 0, 0 );

        LOW_HIGH.setValue( recipe.getStrength() );
        LOW_HIGH.drawBackgroundLayer( 0, 0 );
        LOW_HIGH.drawForegroundLayer();

        RenderHelper.drawText( Translator.translateToLocalFormatted( "jei.biomediversity.transmitter.text.energy", recipe.getStrength() ), TEXT_LOCATION.x, TEXT_LOCATION.y, GUI_WIDTH - Control.Sizes.TANK.width - Control.Sizes.LOW_HIGH.width - 9, RenderHelper.Justification.RIGHT, DEFAULT_TEXT_COLOUR );

    }

//endregion Overrides

}