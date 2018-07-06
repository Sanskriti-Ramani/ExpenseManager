package in.codingninjas.envision.expensemanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import in.codingninjas.envision.expensemanager.AlarmReceiver;
import in.codingninjas.envision.expensemanager.Contract;
import in.codingninjas.envision.expensemanager.ExpensesOpenHelper;

import static android.content.Context.ALARM_SERVICE;

public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.


        ExpensesOpenHelper openHelper = ExpensesOpenHelper.getInstance(context);
        SQLiteDatabase database = openHelper.getReadableDatabase();
        String[] columns = {Contract.Expense.COLUMN_ID,Contract.Expense.DATE_TIME};
        Cursor cursor = database.query(Contract.Expense.TABLE_NAME,columns,  null,null,null,null,null);
        while(cursor.moveToNext()){

            long id = cursor.getLong(cursor.getColumnIndex(Contract.Expense.COLUMN_ID));
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

            Intent intent1 = new Intent(context,AlarmReceiver.class);
            intent1.putExtra("id",id);
            PendingIntent pendingIntent =  PendingIntent.getBroadcast(context,1,intent1,0);

            alarmManager.set(AlarmManager.RTC_WAKEUP,cursor.getLong(cursor.getColumnIndex(Contract.Expense.DATE_TIME)),pendingIntent);

        }
        cursor.close();



    }
}
