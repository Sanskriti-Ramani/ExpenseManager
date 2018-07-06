package in.codingninjas.envision.expensemanager;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import in.codingninjas.envision.expensemanager.Contract;
import in.codingninjas.envision.expensemanager.Expense;
import in.codingninjas.envision.expensemanager.ExpensesOpenHelper;

public class SMSReceiver extends BroadcastReceiver {

    String name;
    long timeStamp;
    int amount;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving

        Bundle data = intent.getExtras();


        if(data != null) {
            Object[] pdus = (Object[]) data.get("pdus");

            for (int i = 0; i < pdus.length; ++i) {

                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i], "3gpp");
                name = smsMessage.getDisplayMessageBody();
                amount = 0;
                timeStamp = smsMessage.getTimestampMillis();

            }

            Expense expense = new Expense(name,amount);
            expense.setTimeInEpochs(timeStamp);

            ExpensesOpenHelper expensesOpenHelper = ExpensesOpenHelper.getInstance(context);
            SQLiteDatabase database = expensesOpenHelper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(Contract.Expense.COLUMN_NAME, name);
            contentValues.put(Contract.Expense.COLUMN_AMOUNT, amount);
            contentValues.put(Contract.Expense.DATE_TIME, expense.getTimeInEpochs());

            long id = database.insert(Contract.Expense.TABLE_NAME, null, contentValues);
            if (id > -1L) {
                Toast.makeText(context, "Todo Added!", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
