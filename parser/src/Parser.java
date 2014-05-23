import model.Engine;
import model.Event;
import model.WeeklyEvents;
import sun.net.www.protocol.http.HttpURLConnection;

import javax.xml.bind.JAXB;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Parser {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd yyyy hh:mmaaa", Locale.ENGLISH);

    public static final int FIVE_MINUTES = 5 * 60 * 1000;
    private static Event nearestEvent;

    public static void main(String[] args) throws IOException, ParseException {
        run();
    }

    private static void run() throws IOException, ParseException {
        WeeklyEvents result = getWeeklyEvents();
        System.out.println("loaded events " + result.toString());

        writeImportantEvents(result);
        System.out.println("important events were writen");
        long currentTime = System.currentTimeMillis();
        for (Event event : result.getEvent()) {
            if (validateEvent(event)) {
                Date parse = getDate(event);
                if (parse.getTime() > currentTime) {
                    nearestEvent = event;
                    break;
                }
            }
        }

        if (nearestEvent == null) {
            System.out.println("No nearest event");
            return;
        }

        System.out.println("nearest event " + nearestEvent);

        long remaining = getDate(nearestEvent).getTime() - currentTime;
        if (remaining > FIVE_MINUTES) {
            System.out.println(String.format("Remaining time %s seconds", String.valueOf(remaining / 1000)));
            try {
                Thread.sleep(remaining - FIVE_MINUTES);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Start writing");
        startWriting(nearestEvent);

        run();
    }

    private static boolean validateEvent(Event event) {
        return "high".equals(event.getImpact())
                && (event.getTime().contains("am") || event.getTime().contains("pm")
                && event.getForecast() != null && !event.getForecast().isEmpty());
    }

    private static WeeklyEvents getWeeklyEvents() throws IOException {
        return Engine.parse("http://www.forexfactory.com/calendar.php");
    }

    private static void startWriting(final Event event) throws ParseException, IOException {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Event updatedEvent = findEvent(event.getTitle());

            if (updatedEvent != null) {
                System.out.println("updated event : " + updatedEvent.toString());

                writeEvent(updatedEvent);
                if (updatedEvent.getActual() != null && !updatedEvent.getActual().isEmpty()) {
                    System.out.println("Actual news ready finish circle");
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }
        }
    }

    private static void writeImportantEvents(final WeeklyEvents weeklyEvents) throws IOException {
        File file = new File("important_events.out");
        file.delete();
        FileWriter fileWriter = new FileWriter(file);
        for (Event event : weeklyEvents.getEvent()) {
            if (validateEvent(event)) {
                fileWriter.write(String.format("%s;%s;%s;%s;%s;%s;%s\n",
                        event.getCountry(),
                        event.getDate(), event.getTime(),
                        event.getTitle(), event.getActual(),
                        event.getForecast(), event.getPrevious()));
            }
        }
        fileWriter.close();
    }

    private static void writeEvent(Event event) throws IOException {
        File file = new File("out_minute.csv");
        file.delete();
        FileWriter fw = new FileWriter(file);
        fw.write(String.format("%s;%s;%s;%s\n",
                event.getCountry(), event.getActual(), event.getForecast(), event.getPrevious()));
        fw.close();
        System.out.println("Event writen");
    }

    private static Event findEvent(final String title) {
        try {
            WeeklyEvents weeklyEvents = getWeeklyEvents();
            for (final Event event : weeklyEvents.getEvent()) {
                if (title.equals(event.getTitle())) {
                    return event;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Date getDate(Event event) throws ParseException {
//        System.out.println("parse " + event.getTitle());
        return DATE_FORMAT.parse(event.getDate() + " " + event.getTime());
    }
}
