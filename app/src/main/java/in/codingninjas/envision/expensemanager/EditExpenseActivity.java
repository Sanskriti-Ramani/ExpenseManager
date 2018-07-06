package in.codingninjas.envision.expensemanager;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import static in.codingninjas.envision.expensemanager.MainActivity.ID_KEY;

public class EditExpenseActivity extends AppCompatActivity {
    EditText titleEditText,amountEditText,dateEditText,timeEditText;
    String titleExpense,amountExpense,dateExpense,timeExpense;
    Bundle bundle;
    long id;
    int year,month,day,hour,min;


    public static final int EDIT_EXPENSE_RESULT_CODE = 1013;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);

        titleEditText = findViewById(R.id.addExpense_TitleEditText);
        amountEditText = findViewById(R.id.addExpense_AmountEditText);
        dateEditText = findViewById(R.id.addExpense_DateEditText);
        timeEditText = findViewById(R.id.addExpense_TimeEditText);


        Intent intent = getIntent();

        id = intent.getLongExtra(ID_KEY,-1);

        if(id != -1) {

            ExpensesOpenHelper openHelper = ExpensesOpenHelper.getInstance(getApplicationContext());
            SQLiteDatabase database = openHelper.getReadableDatabase();
            String[] selectionArgs = {id + ""};

            String[] columns = {Contract.Expense.COLUMN_NAME, Contract.Expense.COLUMN_AMOUNT,Contract.Expense.DATE_TIME ,Contract.Expense.COLUMN_ID};
            Cursor cursor = database.query(Contract.Expense.TABLE_NAME, columns, Contract.Expense.COLUMN_ID + " = ?", selectionArgs, null, null, null);

            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(Contract.Expense.COLUMN_NAME));
                int amount = cursor.getInt(cursor.getColumnIndex(Contract.Expense.COLUMN_AMOUNT));

                Expense expense = new Expense(name,amount);
                expense.setTimeInEpochs(cursor.getLong(cursor.getColumnIndex(Contract.Expense.DATE_TIME)));

                titleEditText.setText(name);
                amountEditText.setText(amount + " ");
                dateEditText.setText(expense.getDate());
                timeEditText.setText(expense.getTime());

                String[] dateComp = expense.getDate().split("/");
                String[] timeComp = expense.getTime().split(":");

                day = Integer.parseInt(dateComp[0]);
                month = Integer.parseInt(dateComp[1]);
                year = Integer.parseInt(dateComp[2]);

                hour = Integer.parseInt(timeComp[0]);
                min = Integer.parseInt(timeComp[1]);
            }
            cursor.close();

        }else{

            String action = intent.getAction();

            if(action == Intent.ACTION_SEND){

                String text = intent.getStringExtra(Intent.EXTRA_TEXT);
                titleEditText.setText(text);

            }
        }




        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setDate();

            }
        });

        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setTime();
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_expense_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.saveExpense){

            if(validateFields()){

                Intent intent = new Intent();

                ExpensesOpenHelper openHelper = ExpensesOpenHelper.getInstance(getApplicationContext());
                SQLiteDatabase database = openHelper.getWritableDatabase();

                String[] selectionArgs = {id + ""};

                ContentValues contentValues = new ContentValues();
                contentValues.put(Contract.Expense.COLUMN_NAME,titleExpense);
                contentValues.put(Contract.Expense.COLUMN_AMOUNT,amountExpense);
                Calendar calendar = Calendar.getInstance();
                calendar.set(year,month,day,hour,min);

                contentValues.put(Contract.Expense.DATE_TIME,calendar.getTimeInMillis());

                database.update(Contract.Expense.TABLE_NAME,contentValues,Contract.Expense.COLUMN_ID + " = ?",selectionArgs);

                intent.putExtra(ID_KEY,id);
                setResult(EDIT_EXPENSE_RESULT_CODE,intent);


                AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);

                Intent intent1 = new Intent(getApplicationContext(),AlarmReceiver.class);
                intent1.putExtra("id",id);
                PendingIntent pendingIntent =  PendingIntent.getBroadcast(getApplicationContext(),1,intent1,PendingIntent.FLAG_UPDATE_CURRENT);

                long currentTime = calendar.getTimeInMillis();
                alarmManager.set(AlarmManager.RTC_WAKEUP,currentTime,pendingIntent);

                finish();



            }else{

                Toast.makeText(EditExpenseActivity.this, "Incomplete details", Toast.LENGTH_SHORT).show();
            }
        }

        return true;

    }

    private boolean validateFields() {

        titleExpense = titleEditText.getText().toString().trim();
        amountExpense = amountEditText.getText().toString().trim();
        dateExpense = dateEditText.getText().toString();
        timeExpense = timeEditText.getText().toString();

        if(titleExpense.isEmpty()){

            titleEditText.setError("Enter title");
            return false;
        }

        if(amountExpense.isEmpty()){

            amountEditText.setError("Enter amount");
            return false;
        }

        if(dateExpense.isEmpty()){

            dateEditText.setError("Select date");
            return false;
        }

        if(timeExpense.isEmpty()){

            timeEditText.setError("Select time");
            return false;
        }

        return true;
    }

    private void setTime() {

        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        min = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(EditExpenseActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                timeExpense = hourOfDay + ":" + minute;
                timeEditText.setText(timeExpense);

                EditExpenseActivity.this.hour = hourOfDay;
                EditExpenseActivity.this.min = minute;

            }
        },hour,min,false);

        timePickerDialog.show();
    }

    private void setDate() {


        Calendar calendar = Calendar.getInstance();
         year = calendar.get(Calendar.YEAR);
         month = calendar.get(Calendar.MONTH);
         day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(EditExpenseActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                int tempMonth = month + 1;
                dateExpense = dayOfMonth + "/" + tempMonth + "/" + year;
                dateEditText.setText(dateExpense);

                EditExpenseActivity.this.day = dayOfMonth;
                EditExpenseActivity.this.month  = month;
                EditExpenseActivity.this.year  = year;

            }
        },year,month,day);

        datePickerDialog.show();
    }


}
