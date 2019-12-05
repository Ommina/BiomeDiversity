package ommina.biomediversity.gui.controls;

import net.minecraft.tileentity.TileEntity;
import ommina.biomediversity.BiomeDiversity;
import ommina.biomediversity.gui.Control;
import ommina.biomediversity.gui.Size;
import ommina.biomediversity.util.MathUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class DynamicRange extends Control {

    protected final TileEntity tile;
    protected Method method = null;
    protected float min = 0f;
    protected float max = 0f;
    protected float value = 0f;
    protected float range = 0f;

    public DynamicRange( Size size, TileEntity te, String methodName, float min, float max ) {

        super( size );

        tile = te;

        try {
            method = te.getClass().getMethod( methodName );
        } catch ( NoSuchMethodException e ) {
            BiomeDiversity.LOGGER.error( "Reflection failed.  NoSuchMethod: " + methodName );
        } catch ( SecurityException e ) {
            BiomeDiversity.LOGGER.error( "Reflection failed.  Security Exception: " + methodName );
        }

        setMinMax( min, max );

    }

    public DynamicRange( Size size, float value, float min, float max ) {

        super( size );

        setMinMax( min, max );
        this.value = value;
        tile = null;

    }

    public void setMinMax( float min, float max ) {

        if ( min > max )
            min = max;

        if ( max < min )
            max = min;

        this.min = min;
        this.max = max;

        range = max - min;

    }

    public void setValue() {

        if ( method == null )
            return;

        Float f;

        try {
            f = ((Float) method.invoke( tile ));
        } catch ( IllegalAccessException | IllegalArgumentException | InvocationTargetException e ) {
            BiomeDiversity.LOGGER.error( "Reflection failed." );
            return;
        }

        value = MathUtil.clamp( MathUtil.round( f, 1 ), min, max );

    }

}
