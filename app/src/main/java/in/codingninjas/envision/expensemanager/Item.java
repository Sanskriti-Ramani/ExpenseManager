package in.codingninjas.envision.expensemanager;

// This is the interface we have used to give the behaviour to the Items so that
// they both have getItemType function through which the adapter can diffrentiate among the different kind of
// list items.
public interface Item {

    int getItemType();
}
