package ommina.biomediversity.fluids;

import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import ommina.biomediversity.blocks.receiver.FluidStrengths;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BdFluidTank extends FluidTank implements IFluidTank, IFluidHandler {

    //private final ObjectArrayList<Integer> fluidWhitelist = new ObjectArrayList<Integer>();

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

    //region Overrides
    @Override
    public boolean isFluidValid( FluidStack fluidStack ) {
        return canFillFluidType( fluidStack );
    }

    @Override
    public int fill( FluidStack resource, FluidAction action ) {

        if ( this.canFill ) {

            int amount = super.fill( resource, action );

            if ( amount > 0 )
                onFill( amount );

            return amount;

        }

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
            return drain_internal( maxDrain, action );

        return FluidStack.EMPTY;

    }
//endregion Overrides

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

    public boolean canFillFluidType( FluidStack fluidStack ) {

        if ( fluidStack.getFluid().getAttributes().isLighterThanAir() || !FluidStrengths.contains( fluidStack.getFluid().hashCode() ) )
            return false;

        return true;

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

    public FluidStack drain_internal( int maxDrain, FluidAction action ) {

        return super.drain( maxDrain, action );

    }

    protected void onFill( int amount ) {

    }

}
