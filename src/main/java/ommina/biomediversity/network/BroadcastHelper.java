package ommina.biomediversity.network;

public class BroadcastHelper {

    protected int[] lastBroadcast;

    protected final int delta;
    protected final ITankBroadcast tankHolder;

    public BroadcastHelper( int tankCount, int minimumDelta, ITankBroadcast tanker ) {

        tankHolder = tanker;

        lastBroadcast = new int[tankCount];
        delta = minimumDelta;

    }

    public void reset() {

        for ( int i = 0; i < lastBroadcast.length; i++ )
            lastBroadcast[i] = tankHolder.getBroadcastTankAmount( i );

    }

    public boolean needsBroadcast() {

        for ( int i = 0; i < lastBroadcast.length; i++ )
            if ( Math.abs( lastBroadcast[i] - tankHolder.getBroadcastTankAmount( i ) ) > delta || (lastBroadcast[i] > 0 && tankHolder.getBroadcastTankAmount( i ) == 0) )
                return true;

        return false;

    }

}
