package org.flexymouse.listener.view;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Group;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.Node;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.media.j3d.ViewPlatform;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import org.flexymouse.listener.event.AbstractEvent;
import org.flexymouse.listener.event.ButtonClickEvent;
import org.flexymouse.listener.event.OrientationEvent;

import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;

public abstract class AbstractModelView {

    private Node model;

    private float azimuth, pitch, roll;

//    private Float initialAzimuth, initialPitch, initialRoll;

    private Float rotationFactor = 1.0f;

    private BranchGroup branchGroup;

    private TransformGroup transformGroup;

    /*********************************************************************************************/
    /** Abstract methods **/
    /*********************************************************************************************/

    abstract public String getName();

    abstract float[] getInitialViewLocation();

    abstract float getBackClipDistance();

    abstract float getFrontClipDistance();

    abstract float getActivationRadius();

    // abstract public void setupBehaviors();

    // abstract public void refreshView(AbstractEvent event);

    /*********************************************************************************************/
    /** Public methods **/
    /*********************************************************************************************/

    public AbstractModelView() {
        init();
    }
    
    public void init() {
        // Create transform group
        this.transformGroup = new TransformGroup();
        this.transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        this.transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        // Add a background
        Background background = new Background(0.0f, 0.0f, 0.0f);
        background.setApplicationBounds(new BoundingSphere());
        this.transformGroup.addChild(background);

        // Animate the model ?
//        Alpha rotationAlpha = new Alpha(-1, 16000);
//        RotationInterpolator rotator = new RotationInterpolator(rotationAlpha, getTransformGroup());
//        rotator.setSchedulingBounds(new BoundingSphere());
//        // objSpin.addChild(rotator);
        
        // Create branch group
        this.branchGroup = new BranchGroup();
        this.branchGroup.setCapability(Group.ALLOW_CHILDREN_READ);
        this.branchGroup.setCapability(Group.ALLOW_CHILDREN_WRITE);
        this.branchGroup.setCapability(Group.ALLOW_CHILDREN_EXTEND);
        this.branchGroup.setCapability(BranchGroup.ALLOW_DETACH);
        this.branchGroup.addChild(transformGroup);

        // TODO Let Java 3D perform optimizations on this scene graph.
        //this.branchGroup.compile();
    }

    public void initviewPlatform(SimpleUniverse universe) {
        // Customize view misc attributes
        ViewPlatform vp = universe.getViewingPlatform().getViewPlatform();
        vp.setActivationRadius(getActivationRadius());
        // vp.setCapability(Node.ALLOW_BOUNDS_WRITE);
        // vp.setBounds(new BoundingSphere(new Point3d(), EARTH_RADIUS * 100));
        // simpleUniverse.getViewingPlatform().getViewPlatformBehavior();

        // Customize view clipping
        View view = universe.getViewer().getView();
        view.setBackClipDistance(this.getBackClipDistance());
        view.setFrontClipDistance(this.getFrontClipDistance());

        // Customize view initial position
        Vector3f translate = new Vector3f();
        translate.set(getInitialViewLocation());
        Transform3D T3D = new Transform3D();
        T3D.setTranslation(translate);
        TransformGroup vpTrans = universe.getViewingPlatform().getViewPlatformTransform();
        vpTrans.setTransform(T3D);
        // simpleUniverse.getViewingPlatform().setNominalViewingTransform();
    }

    public Appearance createTexturedAppearance(String textureFilename) {
        Appearance appearance = new Appearance();

        // Load texture image
        System.out.println("Attempting to load texture from file: " + textureFilename);
        TextureLoader loader = null;
        try {
            loader = new TextureLoader(ImageIO.read(this.getClass().getResourceAsStream(textureFilename)));
        } catch (IOException e) {
            System.out.println("Load has failed for image: " + textureFilename);
            e.printStackTrace();
        }
        ImageComponent2D image = loader.getImage();
        if (image == null) {
            System.out.println("Load has failed for texture: " + textureFilename);
        } else {
            System.out.println("Texture has been loaded:  width = " + image.getWidth() + " height = "
                    + image.getHeight() + " format = " + image.getFormat());
        }

        // Set texture
        Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, image.getWidth(), image.getHeight());
        texture.setImage(0, image);
        texture.setEnable(true);
        texture.setMagFilter(Texture.BASE_LEVEL_LINEAR);
        texture.setMinFilter(Texture.BASE_LEVEL_LINEAR);
        appearance.setTexture(texture);

        // Set other appearance attributes
        appearance
                .setColoringAttributes(new ColoringAttributes(new Color3f(0, 0, 1), ColoringAttributes.SHADE_GOURAUD));
        appearance.setLineAttributes(new LineAttributes(2, LineAttributes.PATTERN_SOLID, true));
        appearance.setPolygonAttributes(new PolygonAttributes(PolygonAttributes.POLYGON_FILL,
                PolygonAttributes.CULL_BACK, 0));

        return appearance;
    }

    public void onFlexyMouseEvent(AbstractEvent event) {
        if (event instanceof OrientationEvent) {
            OrientationEvent e = (OrientationEvent) event;
            this.setAzimuth(e.getAzimuth() * this.getRotationFactor());
            this.setRoll(e.getRoll() * this.getRotationFactor());
            this.setPitch(e.getPitch() * this.getRotationFactor());
            this.rotate();
        } else if (event instanceof ButtonClickEvent) {
            // Nothing for now...
        }
    }

    public void rotate() {
        Transform3D rotation1 = new Transform3D();
        Transform3D rotation2 = new Transform3D();
        Transform3D rotation3 = new Transform3D();
        rotation1.rotX(Math.toRadians(this.getPitch()));
        rotation2.rotY(Math.toRadians(this.getAzimuth()));
        rotation3.rotZ(Math.toRadians(this.getRoll()));
        rotation2.mul(rotation3);
        rotation1.mul(rotation2);
        this.getTransformGroup().setTransform(rotation1);
    }

    /*********************************************************************************************/
    /** Getters & Setters **/
    /*********************************************************************************************/

    public float getAzimuth() {
        return azimuth;
    }

    public void setAzimuth(float azimuth) {
        this.azimuth = azimuth;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getRoll() {
        return roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    public Float getRotationFactor() {
        return rotationFactor;
    }

    public void setRotationFactor(Float rotationFactor) {
        this.rotationFactor = rotationFactor;
    }

    public BranchGroup getBranchGroup() {
        return branchGroup;
    }

    public void setBranchGroup(BranchGroup contentBranchGroup) {
        this.branchGroup = contentBranchGroup;
    }

    public TransformGroup getTransformGroup() {
        return transformGroup;
    }

    public void setTransformGroup(TransformGroup transformGroup) {
        this.transformGroup = transformGroup;
    }

    public Node getModel() {
        return model;
    }

    public void setModel(Node model) {
        this.model = model;
    }
}
