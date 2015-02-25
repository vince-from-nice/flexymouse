package org.flexymouse.listener;

import org.flexymouse.listener.event.AbstractEvent;
import org.flexymouse.listener.event.ButtonClickEvent;
import org.flexymouse.listener.event.OrientationEvent;

public class Protocol {

    static public final int DEFAULT_PACKET_SIZE = 50;

    static public final String BREAKING_STRING = "%%%";

    static public AbstractEvent extractEventFromData(String data) {
        AbstractEvent event = null;
        if (OrientationEvent.containsKeyword(data.substring(0, data.indexOf(" ")))) {
            OrientationEvent e = new OrientationEvent();
            event = e;
            String[] values = data.substring(OrientationEvent.KEYWORDS[0].length() + 1).split("°");
            e.setAzimuth(Float.parseFloat(values[0]));
            e.setPitch(Float.parseFloat(values[1]));
            e.setRoll(Float.parseFloat(values[2]));
        } else if (data.startsWith(ButtonClickEvent.KEYWORD)) {
            ButtonClickEvent e = new ButtonClickEvent();
            event = e;
            int buttonNumber = Integer.parseInt(data.substring(ButtonClickEvent.KEYWORD.length() + 1));
            e.setButtonNumber(buttonNumber);
            e.setPressed(true);
        }
        return event;
    }

}
