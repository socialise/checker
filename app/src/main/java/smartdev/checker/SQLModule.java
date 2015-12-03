package smartdev.checker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 15/05/19.
 */
public class SQLModule{
    Context mContext;

    public SQLModule(Context context){
        mContext = context;

    }

    /*
     カーソル
     */
     public Cursor getSelectCursor(String sql) throws NullPointerException{
        SQLiteOpenHelper helper = new MyHelper(mContext);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = null;
        try{
            cursor = db.rawQuery(sql, null);

        }catch (Exception e){
            Toast.makeText(mContext, "select:" + e.getMessage(), Toast.LENGTH_SHORT);
            Log.v("SQLModule select", e.toString());
        }finally {
//            db.close(); //エラー
        }
        return cursor;
    }

    public void insert(String sql){
        SQLiteOpenHelper helper = new MyHelper(mContext);
        SQLiteDatabase db = helper.getWritableDatabase();

        try{
            db.execSQL(sql);
        }catch (Exception e){
            Toast.makeText(mContext, "insert:" + e.getMessage(), Toast.LENGTH_SHORT);
            Log.v("SQLModule insert", e.toString());
        }finally {
            db.close();
        }
    }

    public  void showTable(String tableName){
        Cursor cursor = getSelectCursor("Select * FROM " + tableName);

        int columnCount = cursor.getColumnCount();
        Log.i("showTable", "------------------");

        String columnNames[] = new String[columnCount];

        for(int i = 0; i < columnCount; i++){
            columnNames[i] = cursor.getString(i);
        }

        while(cursor.moveToNext()){
            for(int i = 0; i < columnCount; i++){

            }
            Log.i("showTable","");
        }
    }

}

