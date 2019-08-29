
package ommina.biomediversity.fluids;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nullable;
import java.util.List;

public class BdFluidTank extends FluidTank implements IFluidTank, IFluidHandler {

    private final ObjectArrayList<Integer> fluidWhitelist = new ObjectArrayList<Integer>();

    public BdFluidTank( int capacity ) {
        super( capacity );
    }

    public BdFluidTank( @Nullable FluidStack fluidStack, int capacity ) {
        super( capacity );

        this.setFluid( fluidStack );

    }

    public BdFluidTank( Fluid fluid, int amount, int capacity ) {
        super( capacity );

        this.setFluid( fluid, amount );

    }

    public void addFluid( Fluid fluid ) {

        fluidWhitelist.add( fluid.getAttributes().getName().hashCode() );

    }

    public void addFluid( List<? extends Fluid> fluid ) {

        for ( Fluid f : fluid )
            addFluid( f );

    }

    public boolean canFillFluidType( FluidStack fluid ) {

        if ( fluid.getFluid().getAttributes().isLighterThanAir() || !fluidWhitelist.contains( fluid.getFluid().getAttributes().getName().hashCode() ) )
            return false;

        return this.canFillFluidType( fluid );

    }

    public int fillInternal( FluidStack fs, boolean doFill ) {

        if ( fs.isEmpty() ) {
            return 0;
        }

        if ( !doFill ) {

            if ( fluid.isEmpty() ) {
                return Math.min( capacity, fs.getAmount() );
            }
            if ( !fluid.isFluidEqual( fs ) ) {
                return 0;
            }

            return Math.min( capacity - fluid.getAmount(), fs.getAmount() );
        }

        if ( fluid.isEmpty() ) {

            fluid = new FluidStack( fs, Math.min( capacity, fs.getAmount() ) );

            onContentsChanged();

            //if ( tile != null ) {
            //    FluidEvent.fireEvent( new FluidEvent.FluidFillingEvent( fluid, tile.getWorld(), tile.getPos(), this, fluid.amount ) );
            //}

            return fluid.getAmount();

        }

        if ( !fluid.isFluidEqual( fs ) ) {
            return 0;
        }

        int filled = capacity - fluid.getAmount();

        if ( filled == 0 )
            return 0;

        if ( fs.getAmount() < filled ) {
            fluid.setAmount( fluid.getAmount() + fs.getAmount() );
            filled = fs.getAmount();
        } else {
            fluid.setAmount( capacity );

        }

        onContentsChanged();

        //if ( tile != null ) {
        //    FluidEvent.fireEvent( new FluidEvent.FluidFillingEvent( fluid, tile.getWorld(), tile.getPos(), this, filled ) );
        //}

        return filled;

    }

    public void setFluid( Fluid fluid, int amount ) {

        if ( fluid == null )
            setFluid( null );
        else
            setFluid( new FluidStack( fluid, amount ) );

    }

}
