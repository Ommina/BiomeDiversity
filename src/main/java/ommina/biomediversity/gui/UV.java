package ommina.biomediversity.gui;

public class UV {

    public int minU;
    public int minV;
    public int maxU;
    public int maxV;

    public int sizeU;
    public int sizeV;

    public UV( int minU, int minV, int maxU, int maxV ) {

        this.minU = minU;
        this.minV = minV;
        this.maxU = maxU;
        this.maxV = maxV;

        this.sizeU = maxU - minU;
        this.sizeV = maxV - minV;

    }

}
