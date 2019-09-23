package ommina.biomediversity.fluids;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BdFluidTank extends FluidTank implements IFluidTank, IFluidHandler {

    private final ObjectArrayList<Integer> fluidWhitelist = new ObjectArrayList<Integer>();

    private int index = 0;
    private boolean canFill = false;
    private boolean canDrain = false;

    public BdFluidTank( int capacity ) {
        super( capacity );
    }

    public BdFluidTank( @Nullable FluidStack fluidStack, int capacity ) {
        super( capacity );

        this.setFluid( fluidStack );

    }

    public BdFluidTank( Fluid fluid, int amount, int capacity ) {
        super( capacity );

        this.setFluid( new FluidStack( fluid, amount ) );

    }

    public int getIndex() {
        return index;
    }

    public void read( CompoundNBT nbt ) {

        if ( !nbt.contains( "index" + index ) )
            return;

        FluidStack fs = FluidStack.loadFluidStackFromNBT( nbt.getCompound( "index" + index ) );

        if ( !fs.isEmpty() )
            this.setFluid( fs );

    }

    public CompoundNBT write( CompoundNBT nbt ) {

        if ( this.getFluid().isEmpty() ) {
            nbt.remove( "index" + index );
            return nbt;
        }

        CompoundNBT indexNbt = new CompoundNBT();

        this.getFluid().writeToNBT( indexNbt );

        nbt.put( "index" + index, indexNbt );

        return nbt;

    }

    public void addFluidToWhitelist( List<? extends Fluid> fluid ) {

        fluid.forEach( this::addFluid );

    }

    public void addFluid( Fluid fluid ) {

        fluidWhitelist.add( fluid.hashCode() );

    }

    public boolean canFillFluidType( FluidStack fluid ) {

        if ( fluid.getFluid().getAttributes().isLighterThanAir() || !fluidWhitelist.contains( fluid.hashCode() ) )
            return false;

        return this.canFillFluidType( fluid );

    }

    public boolean getCanFill() {
        return this.canFill;
    }

    public BdFluidTank setCanFill( boolean canFill ) {
        this.canFill = canFill;
        return this;
    }

    public boolean getCanDrain() {
        return this.canDrain;
    }

    public BdFluidTank setCanDrain( boolean canDrain ) {
        this.canDrain = canDrain;
        return this;
    }

    @Override
    public int fill( FluidStack resource, FluidAction action ) {

        if ( this.canFill )
            return super.fill( resource, action );

        return 0;

    }

    @Nonnull
    @Override
    public FluidStack drain( FluidStack resource, FluidAction action ) {

        if ( this.canDrain )
            return super.drain( resource, action );

        return FluidStack.EMPTY;

    }

    @Nonnull
    @Override
    public FluidStack drain( int maxDrain, FluidAction action ) {

        if ( this.canDrain )
            return super.drain( maxDrain, action );

        return FluidStack.EMPTY;

    }

}
