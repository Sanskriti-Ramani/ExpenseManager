package in.codingninjas.envision.expensemanager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import static in.codingninjas.envision.expensemanager.MainActivity.ID_KEY;

public class ShowDetailsActivity extends AppCompatActivity {

    TextView expenseTitleTextView;
    TextView expenseAmountTextView;
    TextView expenseDateTextView;
    TextView expenseTimeTextView;
    Bundle bundle;
    long id;

    public static final int DETAILS_RESULT_CODE = 1012;
    public static final int DETAILS_REQUEST_CODE = 2012;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

        expenseTitleTextView = findViewById(R.id.expenseTitleTextView);
        expenseAmountTextView = findViewById(R.id.expenseAmountTextView);
        expenseDateTextView = findViewById(R.id.expenseDateTextView);
        expenseTimeTextView = findViewById(R.id.expenseTimeTextView);

        Intent intent = getIntent();

        id = intent.getLongExtra("id",-1);


        if(id != -1){

            getAndSetData();
        }
    }



    private void getAndSetData() {

        ExpensesOpenHelper openHelper = ExpensesOpenHelper.getInstance(getApplicationContext());
        SQLiteDatabase database = openHelper.getReadableDatabase();
        String[] selectionArgs = {id + ""};

        String[] columns = {Contract.Expense.COLUMN_NAME,Contract.Expense.COLUMN_AMOUNT,Contract.Expense.COLUMN_ID,
                Contract.Expense.DATE_TIME};
        Cursor cursor = database.query(Contract.Expense.TABLE_NAME,columns,  Contract.Expense.COLUMN_ID + " = ?",selectionArgs,null,null,null);

        while(cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(Contract.Expense.COLUMN_NAME));
            int amount = cursor.getInt(cursor.getColumnIndex(Contract.Expense.COLUMN_AMOUNT));

            Expense expense = new Expense(name,amount);
            expense.setTimeInEpochs(cursor.getLong(cursor.getColumnIndex(Contract.Expense.DATE_TIME)));

            expenseTitleTextView.setText(name);
            expenseAmountTextView.setText(amount + " ");
            expenseDateTextView.setText(expense.getDate());
            expenseTimeTextView.setText(expense.getTime());
        }
        cursor.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == DETAILS_REQUEST_CODE && resultCode == EditExpenseActivity.EDIT_EXPENSE_RESULT_CODE) {

            if (data != null) {
                id = data.getLongExtra(ID_KEY,-1);

                if (id != -1) {

                    getAndSetData();
                    setResult(DETAILS_RESULT_CODE,data);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.editExpense){

            Intent  intent = new Intent(this,EditExpenseActivity.class);
            intent.putExtra(MainActivity.ID_KEY,id);
            startActivityForResult(intent,DETAILS_REQUEST_CODE);
        }

        return true;

    }
}
