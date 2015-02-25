package org.flexymouse.listener.view;

import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;

public class EarthModelView extends AbstractModelView {

    public static final float EARTH_RADIUS = 6000;

    public static final int EARTH_DETAILS = 100;

    private static final String TEXTURE_FILENAME = "/images/bluemarble.jpg";

    public static final String NAME = "Earth";

    public static final float[] INITIAL_VIEW_LOCATION = new float[] { 0.0f, 0.0f, EARTH_RADIUS * 5 };

    public EarthModelView() {
        super();
        Sphere earth = new Sphere(EARTH_RADIUS, Primitive.GENERATE_TEXTURE_COORDS, EARTH_DETAILS,
                createTexturedAppearance(TEXTURE_FILENAME));
        this.setModel(earth);
        getTransformGroup().addChild(earth);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    float[] getInitialViewLocation() {
        return INITIAL_VIEW_LOCATION;
    }

    @Override
    float getBackClipDistance() {
        return EARTH_RADIUS * 100;
    }

    @Override
    float getFrontClipDistance() {
        return EARTH_RADIUS * 0.1f;
    }

    @Override
    float getActivationRadius() {
        return EARTH_RADIUS * 100;
    }

}
