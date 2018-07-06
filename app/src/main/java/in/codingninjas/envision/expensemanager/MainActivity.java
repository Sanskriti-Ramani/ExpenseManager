package in.codingninjas.envision.expensemanager;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import java.util.LinkedHashMap;
import java.util.Set;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    // Since we have to pass two kinds of Items, the header item and list item, so we make the array list
    // of the type Item as both of them can be added to it.
    ArrayList<Item> items = new ArrayList<>();
//    ExpenseAdapter adapter;
    CustomAdapter customAdapter;

    public static final String TITLE_KEY = "title";
    public static final String AMOUNT_KEY = "amount";
    public static final String DATE_KEY = "date";
    public static final String TIME_KEY = "time";
    public static final String ID_KEY = "id";

    int day,month,year,hour,min;

    public static final int DETAILS_REQUEST_CODE = 1011;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.listview);

        customAdapter = new CustomAdapter(this, items);
        fetchData();

        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);



    }

//  This function helps in retreiving the data from the database and then create a map
//  of in which the key is the date and it's value is the arraylist of the expense objects
//  of that data.
//  LinkedHashMap is used to maintain the order of elements as they are retreived in ascending order.
    void fetchData(){
        ExpensesOpenHelper dbHelper = ExpensesOpenHelper.getInstance(this);

        ArrayList<Expense> expenseItems = dbHelper.getAll();

        LinkedHashMap<String, ArrayList<Expense>> map = new LinkedHashMap<>();

        if(expenseItems != null){
            for (int i = 0; i < expenseItems.size(); ++i) {

                Expense item = expenseItems.get(i);

                if (!map.containsKey(item.getDate())) {

                    ArrayList<Expense> expenseItems1 = new ArrayList<>();
                    expenseItems1.add(item);
                    map.put(item.getDate(), expenseItems1);
                } else {

                    ArrayList<Expense> expenseItems1 = map.get(item.getDate());
                    expenseItems1.add(item);
                    map.put(item.getDate(), expenseItems1);

                }
            }

        }else

        {

            Toast.makeText(this, "Nothing to show", Toast.LENGTH_SHORT).show();

        }

        setArrayList(map);
    }


//    This is the function in which we iterate over the map and for each date we create a
//    header item and add it to the itemsList(which will be passed to the customAdapter) then we add it's
//    corresponding arraylist items to this list so that one list is generated of items.
    private void setArrayList(LinkedHashMap<String, ArrayList<Expense>> map) {

        ArrayList<Item> itemsList = new ArrayList<>();
        Set<String> keySet = map.keySet();

        ArrayList<String> sortedKeys = new ArrayList<>(keySet);

        for(String s: sortedKeys){

            HeaderItem headerItem = new HeaderItem(s);
            itemsList.add(headerItem);
            ArrayList<Expense> expenseItems = map.get(s);
            itemsList.addAll(expenseItems);
        }

        items.addAll(itemsList);
        customAdapter.notifyDataSetChanged();
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        if(items.get(i) instanceof Expense) {
            Expense expense = (Expense) items.get(i);
            Intent intent = new Intent(this, ShowDetailsActivity.class);
            intent.putExtra(ID_KEY, expense.getId());
            startActivityForResult(intent, DETAILS_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle bundle;

        if(requestCode == DETAILS_REQUEST_CODE && resultCode == ShowDetailsActivity.DETAILS_RESULT_CODE){

            items.clear();
            fetchData();

        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {


        Item item = items.get(i);
        if (item instanceof Expense) {

            final Expense expense = (Expense) items.get(i);
            final int position = i;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm Delete");
            builder.setCancelable(false);
            builder.setMessage("Do you really want to delete " + expense.getName() + "?");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    //Toast.makeText(MainActivity.this,"Ok Presses",Toast.LENGTH_LONG).show();
                    ExpensesOpenHelper openHelper = ExpensesOpenHelper.getInstance(getApplicationContext());
                    SQLiteDatabase database = openHelper.getWritableDatabase();

                    long id = expense.getId();
                    String[] selectionArgs = {id + ""};

                    database.delete(Contract.Expense.TABLE_NAME,Contract.Expense.COLUMN_ID + " = ?",selectionArgs);

                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    Intent intent1 = new Intent(MainActivity.this,AlarmReceiver.class);
                    PendingIntent pendingIntent =  PendingIntent.getBroadcast(getApplicationContext(),1,intent1,0);
                    alarmManager.cancel(pendingIntent);

                    items.remove(position);
                    customAdapter.notifyDataSetChanged();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //TODO
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }



        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.addExpense){

            addExpense();
        }

        if(item.getItemId() == R.id.settings){

            startActivity(new Intent(this,SettingsActivity.class));
        }

        return true;
    }

    private void addExpense() {


        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.add_expense_dialog_layout,null);
        builder.setView(dialogView);

        final EditText expenseTitleEditText = dialogView.findViewById(R.id.expenseTitleEditText);
        final EditText expenseAmountEditText = dialogView.findViewById(R.id.expenseAmountTitleEditText);
        final EditText dateEditText = dialogView.findViewById(R.id.addExpense_DateEditText);
        final EditText timeEditText = dialogView.findViewById(R.id.addExpense_TimeEditText);

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        int tempMonth = month+1;
                        // dateExpense = dayOfMonth + "/" + tempMonth + "/" + year;
                        dateEditText.setText(dayOfMonth + "/" + tempMonth + "/" + year);

                        MainActivity.this.day = dayOfMonth;
                        MainActivity.this.month  = month;
                        MainActivity.this.year  = year;

                    }
                },year,month,day);

                datePickerDialog.show();
            }
        });

        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                hour = calendar.get(Calendar.HOUR_OF_DAY);
                min = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        timeEditText.setText(hourOfDay + ":" + minute);

                        MainActivity.this.hour = hourOfDay;
                        MainActivity.this.min = minute;

                    }
                },hour,min,false);

                timePickerDialog.show();
            }
        });

        builder.setTitle("Add Expense");
        builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String expenseTitle,expenseAmount;

                expenseTitle = expenseTitleEditText.getText().toString();
                expenseAmount = expenseAmountEditText.getText().toString();

                Calendar calendar = Calendar.getInstance();
                calendar.set(year,month,day,hour,min);

                Expense expense = new Expense(expenseTitle,Integer.parseInt(expenseAmount));
                expense.setTimeInEpochs(calendar.getTimeInMillis());
                items.add(expense);
                saveExpenseInDatabase(expense);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();

    }


    private void saveExpenseInDatabase(Expense expense) {

        ExpensesOpenHelper expensesOpenHelper = ExpensesOpenHelper.getInstance(this);
        SQLiteDatabase database = expensesOpenHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.Expense.COLUMN_NAME,expense.getName());
        contentValues.put(Contract.Expense.COLUMN_AMOUNT,expense.getAmount());
        contentValues.put(Contract.Expense.DATE_TIME,expense.getTimeInEpochs());

        long id = database.insert(Contract.Expense.TABLE_NAME,null,contentValues);
        if (id > -1L){
            expense.setId(id);
            items.clear();
            fetchData();
            customAdapter.notifyDataSetChanged();
        }

    }
}
