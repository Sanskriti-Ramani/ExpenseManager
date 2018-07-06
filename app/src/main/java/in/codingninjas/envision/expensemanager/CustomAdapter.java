package in.codingninjas.envision.expensemanager;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter {

    Context mContext;
    ArrayList<Item> itemsList;


    // Constructor
    public CustomAdapter(Context context, ArrayList<Item> itemsList){
        super(context,0);
        mContext = context;
        this.itemsList = itemsList;
    }

    // This function return the number of different type of views that will be there in the list view.
    @Override
    public int getViewTypeCount(){
    }

    // This function returns the type of item(in our case header or list item) that adapter wants to know in getView function.
    @Override
    public int getItemViewType(int position) {

    }

    // This function gives the total count of items that will be there in the list.
    @Override
    public int getCount() {
    }

    // This function returns the object of the itemList that has to inflated at that position.
    @Override
    public Object getItem(int position) {

    }


    // This function returns the unique id associated with every inflated layout, since it's is not useful in our case so
    // we return the position, which is also unique for every item.
    @Override
    public long getItemId(int position) {

    }




    // This is the function in which we have to inflate the layout as per its TYPE
    // this function gets the type of each item from getItemViewType and on the basis of it we apply if else and inflate the layout.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

    }


}
