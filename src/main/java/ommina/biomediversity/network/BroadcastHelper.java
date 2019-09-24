package ommina.biomediversity.network;

public class BroadcastHelper {

    protected final int delta;
    protected final ITankBroadcast tankHolder;
    protected int[] lastBroadcast;

    public BroadcastHelper( int tankCount, int minimumDelta, ITankBroadcast tanker ) {

        tankHolder = tanker;

        lastBroadcast = new int[tankCount];
        delta = minimumDelta;

    }

    public void reset() {

        for ( int i = 0; i < lastBroadcast.length; i++ )
            lastBroadcast[i] = tankHolder.getTank( i ).getFluid().getAmount();

    }

    public boolean needsBroadcast() {

        for ( int i = 0; i < lastBroadcast.length; i++ )
            if ( Math.abs( lastBroadcast[i] - tankHolder.getTank( i ).getFluid().getAmount() ) > delta || (lastBroadcast[i] > 0 && tankHolder.getTank( i ).getFluid().getAmount() == 0) )
                return true;

        return false;

    }

}
