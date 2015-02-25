package org.flexymouse.listener.behavior;

import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

import javax.media.j3d.WakeupOnAWTEvent;

import org.flexymouse.listener.Application;

public class KeyboardBehaviour extends AbstractBehavior {

    private float rotationDelta = 2.0f;

    public KeyboardBehaviour(Application a) {
        super(a);
    }

    @Override
    public void initialize() {
        // set initial wakeup condition
        this.wakeupOn(new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED));
    }

    @Override
    public void processStimulus(Enumeration criteria) {
        while (criteria.hasMoreElements()) {
            WakeupOnAWTEvent wakeup = (WakeupOnAWTEvent) criteria.nextElement();
            AWTEvent[] events = wakeup.getAWTEvent();
            for (int i = 0; i < events.length; i++) {
                KeyEvent event = (KeyEvent) events[i];
                // Mouse click
                if (event.getKeyCode() == KeyEvent.VK_LEFT) {
                    this.getCurrentView().setAzimuth((this.getCurrentView().getAzimuth() - rotationDelta) % 360);
                } else if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
                    this.getCurrentView().setAzimuth((this.getCurrentView().getAzimuth() + rotationDelta) % 360);
                } else if (event.getKeyCode() == KeyEvent.VK_UP) {
                    this.getCurrentView().setPitch((this.getCurrentView().getPitch() + rotationDelta) % 360);
                } else if (event.getKeyCode() == KeyEvent.VK_DOWN) {
                    this.getCurrentView().setPitch((this.getCurrentView().getPitch() - rotationDelta) % 360);
                }
            }
        }
        // System.out.println("angleX=" + this.view.getPitch() + " angleY=" + this.view.getRoll() + " angleZ="
        // + this.view.getAzimuth());
        this.getCurrentView().rotate();
        this.getApplication().getStatusBar().updateOrientationLabel("Yaw: " + (int) this.getCurrentView().getAzimuth() + "° Pitch: " + (int) this.getCurrentView().getPitch() + "° Roll: "
                + (int) this.getCurrentView().getRoll() + "°");
        // this.wakeupOn(new WakeupOr(WAKEUP_CRITERION));
        this.wakeupOn(new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED));
    }
}
