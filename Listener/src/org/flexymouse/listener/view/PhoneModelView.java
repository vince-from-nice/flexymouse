package org.flexymouse.listener.view;

import com.sun.j3d.utils.geometry.Box;

public class PhoneModelView extends AbstractModelView {

    public static final String NAME = "Phone";
    
    public static final float WIDTH = 58f;

    public static final float HEIGHT = 9;

    public static final float DEEPTH = 115f;

    private static final String TEXTURE_FILENAME = "/images/iphone4s-front.png";
    
    public static final float[] INITIAL_VIEW_LOCATION = new float[] {0.0f, 0.0f, DEEPTH * 5};

    public PhoneModelView() {
        super();
        Box phone = new Box(WIDTH, HEIGHT, DEEPTH, Box.GENERATE_TEXTURE_COORDS, createTexturedAppearance(TEXTURE_FILENAME));
        setModel(phone);
        getTransformGroup().addChild(phone);
    }
        
    @Override
    public String getName() {
       return NAME;
    }

    @Override
    float[] getInitialViewLocation() {
        return  INITIAL_VIEW_LOCATION;
    }

    @Override
    float getBackClipDistance() {
        return DEEPTH * 100;
    }

    @Override
    float getFrontClipDistance() {
        return DEEPTH * 0.1f;
    }

    @Override
    float getActivationRadius() {
        return DEEPTH * 100;
    }
}
