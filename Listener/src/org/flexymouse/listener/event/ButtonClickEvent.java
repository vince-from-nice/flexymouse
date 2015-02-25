package org.flexymouse.listener.event;

public class ButtonClickEvent extends AbstractEvent {
    
    private int buttonNumber;
    
    static public final String KEYWORD = "BUTTON";

    public int getButtonNumber() {
        return buttonNumber;
    }

    public void setButtonNumber(int buttonNumber) {
        this.buttonNumber = buttonNumber;
    }

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    private boolean pressed;

}
