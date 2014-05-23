package model;

public class Event {

    String title;
    String country;
    String date;
    String impact;
    String forecast;
    String previous;
    String time;
    String actual;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImpact() {
        return impact;
    }

    public void setImpact(String impact) {
        this.impact = impact;
    }

    public String getForecast() {
        return forecast;
    }

    public void setForecast(String forecast) {
        this.forecast = forecast;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getActual() {
        return actual;
    }

    public void setActual(String actual) {
        this.actual = actual;
    }

    @Override
    public String toString() {
        return "Event{" +
                "title='" + title + '\'' +
                ", country='" + country + '\'' +
                ", date='" + date + '\'' +
                ", impact='" + impact + '\'' +
                ", forecast='" + forecast + '\'' +
                ", previous='" + previous + '\'' +
                ", time='" + time + '\'' +
                ", actual='" + actual + '\'' +
                '}';
    }
}
