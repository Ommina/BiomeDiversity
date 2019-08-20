
package ommina.biomediversity.blocks.tile;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fluids.FluidStack;

public abstract class TileEntityWithTank extends TileEntity { // implements ITankBroadcast {

    public TileEntityWithTank( TileEntityType<?> tile, int capacity ) {
        super( tile );

        //TANK = new BdFluidTank( capacity );

    }

    @Override
    public CompoundNBT serializeNBT() {
        return null;
    }

    @Override
    public void read( CompoundNBT compound ) {
        super.read( compound );
    }

    @Override
    public CompoundNBT write( CompoundNBT compound ) {

        //writeToNBT( compound, "tank", TANK.getFluid() );

        return super.write( compound );

    }


    /*

    protected final BdFluidTank TANK;

    public TileEntityWithTank( int capacity ) {

        TANK = new BdFluidTank( capacity );

    }

    // Overrides

    @Override
    public void readFromNBT( NBTTagCompound compound ) {

        super.readFromNBT( compound );

        TANK.fillInternal( readFromNBT( compound, "tank" ), true );

    }


    @Override
    public NBTTagCompound writeToNBT( NBTTagCompound compound ) {

    }


    @Override
    public int getBroadcastTankAmount( int tank ) {

        return TANK.getFluidAmount();
    }

    // End Overrides

    @Nonnull
    public BdFluidTank getTank() {

        return TANK;
    }

*/

    private void writeToNBT( CompoundNBT compound, String prefix, FluidStack fs ) {

        if ( fs == null ) {
            compound.putInt( prefix + "_amount", 0 );
            return;
        }

        compound.putInt( prefix + "_amount", fs.amount );
        compound.putString( prefix + "_name", fs.getFluid().getName() );

    }

    private FluidStack readFromNBT( CompoundNBT compound, String prefix ) {

        int amount = compound.getInt( prefix + "_amount" );

        if ( amount == 0 )
            return null;

        //return new FluidStack( ForgeRegistries.FLUIDS.getValue( BiomeDiversity.getId( compound.getString( prefix + "_name" ) ) ), amount );

        //Fluid f = ForgeRegistries.FLUIDS.getValue( BiomeDiversity.getId( compound.getString( prefix + "_name" ) ) );

        return null;

        //return new FluidStack( f, 1000 );


        // FluidRegistry.  getFluidStack( compound.getString( prefix + "_name" ), amount );

    }


}
