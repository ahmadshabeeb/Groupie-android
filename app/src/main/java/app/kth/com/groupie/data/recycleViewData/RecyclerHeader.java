package app.kth.com.groupie.data.recycleViewData;

public class RecyclerHeader implements RecyclerListItem {
    private String day;

    public String getDay() { return day; }
    public void setDay(String day) { this.day = day; }

    @Override
    public boolean isHeader() {
        return true;
    }
}
