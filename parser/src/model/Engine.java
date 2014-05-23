package model;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;
import sun.misc.IOUtils;
import sun.net.www.protocol.http.HttpURLConnection;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Engine {

    public static WeeklyEvents parse(final String url) throws IOException {
        HttpURLConnection connection = new HttpURLConnection(new URL(url), null);
        InputStream inputStream = connection.getInputStream();
        String page = org.apache.commons.io.IOUtils.toString(inputStream);
        inputStream.close();

        System.out.println("page loaded: " + page.length() + " chars");
        Source source = new Source(page);
        source.fullSequentialParse();
        Element table = source.getAllElements("thead").get(0).getParentElement();

        List<Element> tableContent = new ArrayList<Element>(table.getChildElements());
        Element remove = tableContent.remove(0);
//        System.out.println("remove " + remove.getStartTag().toString());
        List<Element> rows = tableContent;

        String date = null;
        String time = null;

        ArrayList<Event> events = new ArrayList<Event>();
        WeeklyEvents weeklyEvents = new WeeklyEvents();
        weeklyEvents.setEvent(events);

        for (final Element row : rows) {
//            System.out.println("row " + row.toString());

            Attributes attributes = row.getAttributes();
            Attribute aClass = attributes.get("class");
            if (aClass.getValue().contains("borderfix")) continue;

            boolean newDay = aClass.getValue().contains("newday");
            boolean newTime = !aClass.getValue().contains("nogrid");

            List<Element> cells = row.getChildElements();
            Element dateElement = cells.get(0);
            Element timeElement = cells.get(1);

            if (newDay) {
                date = dateElement.getFirstElement().getChildElements().get(0).getTextExtractor().toString();
            }
            if (newTime) {
                time = timeElement.getTextExtractor().toString();
            }

            Event newEvent = new Event();

            Element currencyElement = cells.get(2);
            String currency = currencyElement.getTextExtractor().toString();
            newEvent.setCountry(currency);
            Element impactElement = cells.get(3);
            String impact = impactElement.getChildElements().get(0).getAttributeValue("class");
            newEvent.setImpact(impact);
            Element eventElement = cells.get(4);
            String event = eventElement.getTextExtractor().toString();
            newEvent.setTitle(event);
            Element actualElement = cells.get(6);
            String actual = actualElement.getTextExtractor().toString();
            newEvent.setActual(actual);
            Element forecastElement = cells.get(7);
            String forecast = forecastElement.getTextExtractor().toString();
            newEvent.setForecast(forecast);
            Element previousElement = cells.get(8);
            String previous = previousElement.getTextExtractor().toString();
            newEvent.setPrevious(previous);

            String substring2 = date.substring(3, date.length());

            newEvent.setDate(substring2 + " 2014");
            newEvent.setTime(time);

            events.add(newEvent);

//            System.out.println(newEvent.toString());
        }

        return weeklyEvents;
    }

    public static void main(String[] args) {
        try {
            parse("http://www.forexfactory.com/calendar.php");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
