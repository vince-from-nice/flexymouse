package org.flexymouse.listener.behavior;

import java.awt.AWTEvent;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.media.j3d.WakeupOr;

import org.flexymouse.listener.Application;

public class MouseBehaviour extends AbstractBehavior {

    // private TransformGroup targetTG;

    // private boolean button1Pressed = false;
    //
    // private boolean button2Pressed = false;

    private float rotationFactor = 0.3f;

    private int lastX = 0;

    private int lastY = 0;

    // private static final WakeupCriterion[] WAKEUP_CRITERION = {
    // new WakeupOnAWTEvent(MouseEvent.MOUSE_PRESSED),
    // new WakeupOnAWTEvent(MouseEvent.MOUSE_RELEASED),
    // new WakeupOnAWTEvent(MouseEvent.MOUSE_MOVED)
    // };

    private static final WakeupCriterion[] WAKEUP_CRITERION = { new WakeupOnAWTEvent(MouseEvent.MOUSE_DRAGGED),
            new WakeupOnAWTEvent(MouseEvent.MOUSE_PRESSED), new WakeupOnAWTEvent(MouseEvent.MOUSE_WHEEL) };

    public MouseBehaviour(Application a) {
        super(a);
    }

    @Override
    public void initialize() {
        // set initial wakeup condition
        this.wakeupOn(new WakeupOr(WAKEUP_CRITERION));
    }

    @Override
    public void processStimulus(Enumeration criteria) {

        while (criteria.hasMoreElements()) {
            WakeupOnAWTEvent wakeup = (WakeupOnAWTEvent) criteria.nextElement();
            AWTEvent[] events = wakeup.getAWTEvent();
            for (int i = 0; i < events.length; i++) {
                MouseEvent event = (MouseEvent) events[i];
                // Mouse click
                if (event.getID() == MouseEvent.MOUSE_PRESSED) {
                    //System.out.println("Event : type=MOUSE_PRESSED  x=" + event.getX() + " y=" + event.getY());
                    lastX = event.getX();
                    lastY = event.getY();
                }
                // Mouse drag
                else if (event.getID() == MouseEvent.MOUSE_DRAGGED) {
                    //System.out.println("Event : type=MOUSE_DRAGGED  x=" + event.getX() + " y=" + event.getY());
                    float deltaX = 0, deltaY = 0;
                    deltaX = (event.getX() - lastX) * rotationFactor;
                    deltaY = (event.getY() - lastY) * rotationFactor;
                    //System.out.println("Delta : x=" + deltaX + " y=" + deltaY);
                    getCurrentView().setAzimuth((getCurrentView().getAzimuth() + deltaX) % 360);
                    getCurrentView().setPitch((getCurrentView().getPitch() + deltaY) % 360);
                    lastX = event.getX();
                    lastY = event.getY();
                }
            }
        }
        //System.out.println("angleX=" + view.getPitch() + " angleY=" + view.getRoll() + " angleZ=" + view.getAzimuth());
        this.getCurrentView().rotate();
        this.getApplication().getStatusBar().updateOrientationLabel("Yaw: " + (int) this.getCurrentView().getAzimuth() + "° Pitch: " + (int) this.getCurrentView().getPitch() + "° Roll: "
                + (int) this.getCurrentView().getRoll() + "°");
        this.wakeupOn(new WakeupOr(WAKEUP_CRITERION));
    }
}
