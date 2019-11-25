package ommina.biomediversity.blocks.plug;

import net.minecraftforge.energy.EnergyStorage;
import ommina.biomediversity.blocks.collector.TileEntityCollector;

public class PlugCollectorDetails {

    private final int uniqueBiomeCount;
    private final float temperature;
    private final EnergyStorage energyStorage;

    public PlugCollectorDetails( final TileEntityCollector collector ) {

        this.temperature = collector.getTemperature();
        this.uniqueBiomeCount = collector.getUniqueBiomeCount();
        this.energyStorage = collector.getEnergyStorage();

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

}
