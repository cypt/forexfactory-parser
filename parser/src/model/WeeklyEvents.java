package model;

import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="weeklyevents")
public class WeeklyEvents {

    List<Event> event;

    public WeeklyEvents(List<Event> event) {
        this.event = event;
    }

    public WeeklyEvents() {
    }

    public List<Event> getEvent() {
        return event;
    }

    public void setEvent(List<Event> event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return "WeeklyEvents{" +
                "event=" + event +
                '}';
    }
}
