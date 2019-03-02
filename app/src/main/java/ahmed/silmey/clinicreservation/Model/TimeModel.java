package ahmed.silmey.clinicreservation.Model;

public class TimeModel
{
    String time,booked;

    public TimeModel(String time, String booked) {
        this.time = time;
        this.booked = booked;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBooked() {
        return booked;
    }

    public void setBooked(String booked) {
        this.booked = booked;
    }
}
