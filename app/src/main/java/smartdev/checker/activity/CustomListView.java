package smartdev.checker.activity;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SimpleCursorAdapter;

/**
 * Created by Administrator on 15/10/12.
 */
public class CustomListView extends SimpleCursorAdapter {

    Context context;

    public CustomListView(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);

    }

    @Override
    public View getView(int position,
                        View convertView,
                        ViewGroup parent){

        return new CheckBox(context);
    }
}
