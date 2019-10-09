package ommina.biomediversity.network;

public class BroadcastHelper {

    protected final int delta;
    protected final ITankBroadcast tankHolder;

    protected int[] lastBroadcast;
    protected boolean forceBroadcast = false;

    public BroadcastHelper( int tankCount, int minimumDelta, ITankBroadcast tanker ) {

        tankHolder = tanker;

        lastBroadcast = new int[tankCount];
        delta = minimumDelta;

    }

    public void forceBroadcast() {
        forceBroadcast = true;
    }

    public boolean needsBroadcast() {

        if ( forceBroadcast )
            return true;

        for ( int i = 0; i < lastBroadcast.length; i++ )
            if ( Math.abs( lastBroadcast[i] - tankHolder.getTank( i ).getFluid().getAmount() ) > delta || (lastBroadcast[i] > 0 && tankHolder.getTank( i ).getFluid().getAmount() == 0) )
                return true;

        return false;

    }

    public void reset() {

        for ( int i = 0; i < lastBroadcast.length; i++ )
            lastBroadcast[i] = tankHolder.getTank( i ).getFluid().getAmount();

        forceBroadcast = false;

    }

}
