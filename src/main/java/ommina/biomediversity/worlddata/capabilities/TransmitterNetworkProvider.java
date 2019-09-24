package ommina.biomediversity.worlddata.capabilities;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import ommina.biomediversity.BiomeDiversity;

import javax.annotation.Nullable;

public class TransmitterNetworkProvider implements ICapabilitySerializable<INBT> {


    private LazyOptional<ITransmitterNetwork> instance = LazyOptional.of( BiomeDiversity.TRANSMITTER_NETWORK_CAPABILITY::getDefaultInstance );

    @Override
    public <T> LazyOptional<T> getCapability( Capability<T> cap, @Nullable Direction direction ) {
        return cap == BiomeDiversity.TRANSMITTER_NETWORK_CAPABILITY ? instance.cast() : LazyOptional.empty();
    }

    @Nullable
    @Override
    public INBT serializeNBT() {
        return BiomeDiversity.TRANSMITTER_NETWORK_CAPABILITY.writeNBT( instance.orElseThrow( () -> new IllegalArgumentException( "LazyOptional must not be empty!" ) ), null );


        //return CAPABILITY.writeNBT( inst.orElse( null ), null );
    }

    @Override
    public void deserializeNBT( INBT nbt ) {
        BiomeDiversity.TRANSMITTER_NETWORK_CAPABILITY.getStorage().readNBT( BiomeDiversity.TRANSMITTER_NETWORK_CAPABILITY, this.instance.orElseThrow( () -> new IllegalArgumentException( "LazyOptional must not be empty!" ) ), null, nbt );
    }

}

