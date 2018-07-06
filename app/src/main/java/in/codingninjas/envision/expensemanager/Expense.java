package in.codingninjas.envision.expensemanager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Expense implements Item{

    private String name;
    private int amount;
    private String date;
    private String time;
    private long timeInEpochs;
    private long id;
    public final int TYPE = 1;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Expense(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int getItemType() {
        return TYPE;
    }

    public long getTimeInEpochs() {
        return timeInEpochs;
    }

    public void setTimeInEpochs(long timeInEpochs) {
        this.timeInEpochs = timeInEpochs;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInEpochs);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        ++month;

        this.date = day + "/" + month + "/" + year;

        int hour = calendar.get(Calendar.HOUR);
        int min = calendar.get(Calendar.MINUTE);

        this.time = hour + ":" + min;

    }
}
