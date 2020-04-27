package ommina.biomediversity.blocks.plug;

import net.minecraftforge.energy.EnergyStorage;
import ommina.biomediversity.blocks.collector.TileEntityCollector;
import ommina.biomediversity.fluids.BdFluidTank;

import java.util.List;

public class PlugCollectorDetails {

    private final int uniqueBiomeCount;
    private final int releasePerTick;
    private final float temperature;
    private final EnergyStorage energyStorage;
    private final List<BdFluidTank> tank;

    public PlugCollectorDetails( final TileEntityCollector collector ) {

        this.temperature = collector.getTemperature();
        this.uniqueBiomeCount = collector.getUniqueBiomeCount();
        this.energyStorage = collector.getEnergyStorage();
        this.releasePerTick = collector.getRfReleasedPerTick();
        this.tank = collector.getTanks();

    }

    public int getUniqueBiomeCount() {
        return uniqueBiomeCount;
    }

    public float getTemperature() {
        return temperature;
    }

    public EnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public BdFluidTank getTank( int tankIndex ) {
        return tank.get( tankIndex );
    }

    public int getRfReleasedPerTick() {
        return releasePerTick;
    }

}
