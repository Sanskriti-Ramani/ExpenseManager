package in.codingninjas.envision.expensemanager;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static java.lang.Boolean.FALSE;

public class CustomAdapter extends ArrayAdapter {

    Context mContext;
    ArrayList<Item> itemsList;
    LayoutInflater inflater;


    // Constructor
    public CustomAdapter(Context context, ArrayList<Item> itemsList) {
        super(context, 0);
        mContext = context;
        this.itemsList = itemsList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // This function return the number of different type of views that will be there in the list view.
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    // This function returns the type of item(in our case header or list item) that adapter wants to know in getView function.
    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof HeaderItem) {
            return 0;
        } else {
            return 1;
        }
    }

    // This function gives the total count of items that will be there in the list.
    @Override
    public int getCount() {
        return itemsList.size();
    }

    // This function returns the object of the itemList that has to inflated at that position.
    @Override
    public Object getItem(int position) {

        return itemsList.get(position);
    }


    // This function returns the unique id associated with every inflated layout, since it's is not useful in our case so
    // we return the position, which is also unique for every item.
    @Override
    public long getItemId(int position) {
        return position;
    }


    // This is the function in which we have to inflate the layout as per its TYPE
    // this function gets the type of each item from getItemViewType and on the basis of it we apply if else and inflate the layout.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d("MainActivityPosition", position + "");
        View output = convertView;

        int type = getItemViewType(position);


//        if (type == 0) {
//            if (output == null) {
//                output = View.inflate(mContext, R.layout.header_item_layout, null );
//                TextView headerTextView = output.findViewById(R.id.headerTitleTextView);
//                HeaderItemViewHolder viewHolder = new HeaderItemViewHolder(headerTextView);
//                viewHolder.headerTitleTextView = headerTextView;
//                output.setTag(viewHolder);
//
//            }
//            else {
//                output = View.inflate(mContext, R.layout.header_item_layout, null );
//                TextView headerTextView = output.findViewById(R.id.headerTitleTextView);
//                HeaderItemViewHolder viewHolder = new HeaderItemViewHolder(headerTextView);
//                viewHolder.headerTitleTextView = headerTextView;
//                output.setTag(viewHolder);
//                viewHolder = (HeaderItemViewHolder) output.getTag();
//                HeaderItem headerItem = (HeaderItem) getItem(position);
//                viewHolder.headerTitleTextView.setText(headerItem.getHeaderTitle());
//            }
//
//        }
        if (output == null) {
            switch (type) {
                case 0:
                    output = inflater.inflate(R.layout.header_item_layout, parent, false);
                    break;
                case 1:
                    output = inflater.inflate(R.layout.expense_row_layout, parent, false);
                    break;
            }
        }

        switch (type) {
            case 0:
                HeaderItem headerItem = (HeaderItem)getItem(position);

                output = inflater.inflate(R.layout.header_item_layout, null);
                TextView names = (TextView) output.findViewById(R.id.headerTitleTextView);
                names.setText(headerItem.getHeaderTitle());
                break;
            case 1:
                Expense expenseItem = (Expense)getItem(position);
               output = inflater.inflate(R.layout.expense_row_layout, null);
                TextView name = (TextView) output.findViewById(R.id.expenseName);
                name.setText(expenseItem.getName());
                TextView amount = (TextView) output.findViewById(R.id.expenseAmount);
                amount.setText(expenseItem.getAmount()+ "");
                TextView date = (TextView) output.findViewById(R.id.expenseDate);
                 date.setText(expenseItem.getDate());
                TextView time = (TextView) output.findViewById(R.id.expenseTime);
              time.setText(expenseItem.getTime());


                break;
        }


        return output;

    }
}
