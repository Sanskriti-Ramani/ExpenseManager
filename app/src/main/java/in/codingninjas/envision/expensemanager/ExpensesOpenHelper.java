package in.codingninjas.envision.expensemanager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class ExpensesOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "expenses_db";
    public static final int VERSION = 1;


    private static ExpensesOpenHelper instance;

    public static ExpensesOpenHelper getInstance(Context context){
        if(instance == null){
            instance = new ExpensesOpenHelper(context.getApplicationContext());
        }
        return instance;
    }


    private ExpensesOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String expensesSql = "CREATE TABLE " + Contract.Expense.TABLE_NAME  + " (  " +
                Contract.Expense.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                Contract.Expense.COLUMN_NAME  + " TEXT , " +
                Contract.Expense.COLUMN_AMOUNT + " INTEGER ," +
                Contract.Expense.DATE_TIME  + " NUMERIC  )";

        sqLiteDatabase.execSQL(expensesSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public ArrayList<Expense> getAll(){

        ArrayList<Expense> expenses = new ArrayList<>();
        String[] columns = {Contract.Expense.COLUMN_NAME, Contract.Expense.COLUMN_AMOUNT, Contract.Expense.DATE_TIME,Contract.Expense.COLUMN_ID};
        Cursor cursor = this.getReadableDatabase().query(Contract.Expense.TABLE_NAME, columns, null, null, null, null, Contract.Expense.DATE_TIME);


        if(cursor.moveToNext()){
            do{
                Expense expense = new Expense(cursor.getString(cursor.getColumnIndex(Contract.Expense.COLUMN_NAME)),
                        cursor.getInt(cursor.getColumnIndex(Contract.Expense.COLUMN_AMOUNT)));

                expense.setTimeInEpochs(cursor.getLong(cursor.getColumnIndex(Contract.Expense.DATE_TIME)));

                long id = cursor.getInt(cursor.getColumnIndex(Contract.Expense.COLUMN_ID));
                expense.setId(id);

                expenses.add(expense);
                Log.d("chekkkk",expense.getDate() + " " + expense.getName());

            }while(cursor.moveToNext());

        }

        cursor.close();
        return expenses;
    }
}
