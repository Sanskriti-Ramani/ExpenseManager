package in.codingninjas.envision.expensemanager;

public class HeaderItem implements Item{

    String headerTitle;
    public final int TYPE = 0;

    public HeaderItem(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public int getTYPE() {
        return TYPE;
    }

    @Override
    public int getItemType() {
        return TYPE;
    }
}
