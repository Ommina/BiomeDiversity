package ommina.biomediversity.worlddata.capabilities;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

public class TransmitterNetworkProvider implements ICapabilitySerializable<INBT> {

    @CapabilityInject( ITransmitterNetwork.class )
    public static final Capability<ITransmitterNetwork> TRANSMITTER_NETWORK_CAPABILITY = null;

    private LazyOptional<ITransmitterNetwork> instance = LazyOptional.of( TRANSMITTER_NETWORK_CAPABILITY::getDefaultInstance );

    @Override
    public <T> LazyOptional<T> getCapability( Capability<T> cap, @Nullable Direction direction ) {
        return cap == TRANSMITTER_NETWORK_CAPABILITY ? instance.cast() : LazyOptional.empty();
    }

    @Nullable
    @Override
    public INBT serializeNBT() {
        return TRANSMITTER_NETWORK_CAPABILITY.getStorage().writeNBT( TRANSMITTER_NETWORK_CAPABILITY, this.instance.orElseThrow( () -> new IllegalArgumentException( "LazyOptional must not be empty!" ) ), null );
    }

    @Override
    public void deserializeNBT( INBT nbt ) {
        TRANSMITTER_NETWORK_CAPABILITY.getStorage().readNBT( TRANSMITTER_NETWORK_CAPABILITY, this.instance.orElseThrow( () -> new IllegalArgumentException( "LazyOptional must not be empty!" ) ), null, nbt );
    }

}

