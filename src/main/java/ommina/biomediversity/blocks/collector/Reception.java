package ommina.biomediversity.blocks.collector;

import ommina.biomediversity.config.Config;

import java.util.HashMap;
import java.util.Map;

class Reception {

    private final long lastEmitTime = 0;

    private final Map<String, ReceptionItem> RECEPTORS;

    public Reception() {

        RECEPTORS = new HashMap<String, ReceptionItem>( 30 );

    }

    public void add( String biomeRegistryName, int fluidHash, float temperature, int power ) {

        if ( RECEPTORS.containsKey( biomeRegistryName ) ) {

            ReceptionItem ri = RECEPTORS.get( biomeRegistryName );
            ri.temperature += temperature;

            if ( power > ri.currentPower )
                ri.currentPower = power;

            return;
        }

        //Biomediversity.logger.warn( "Add: " + bid + " " + fluidHash + " " + power + " " + temperature );

        RECEPTORS.put( biomeRegistryName, new ReceptionItem( biomeRegistryName, fluidHash, power, temperature ) );

    }

    public Emission emit() {

        Emission miss = new Emission();

        for ( ReceptionItem ri : RECEPTORS.values() )
            if ( Config.powerBiomes.contains( ri.biomeRegistryName ) )
                miss.add( ri.currentPower, ri.temperature, ri.fluidHash );
            else
                miss.add( ri.temperature * 2 );

        //Biomediversity.logger.warn( "emit: " + miss.toString() );

        RECEPTORS.clear();

        //lastEmitTime = Minecraft.getSystemTime();

        return miss;
    }

    private class ReceptionItem {

        public int currentRunLength = 1;
        public int currentPower;
        public int previousRunLength = 0;
        public int previousPower = 0;
        public String biomeRegistryName;
        public int fluidHash = 0;
        public float temperature;

        public ReceptionItem( String biomeRegistryName, int fluidHash, int power, float temperature ) {

            this.biomeRegistryName = biomeRegistryName;
            this.temperature = temperature;
            this.currentPower = power;

        }

    }

}
