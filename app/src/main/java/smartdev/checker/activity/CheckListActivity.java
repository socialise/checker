package smartdev.checker.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import smartdev.checker.MyHelper;
import smartdev.checker.R;
import smartdev.checker.throwable.IncorrectValueException;

public class CheckListActivity extends AppCompatActivity {

    ViewGroup mScreen; //アイテムを追加していくレイアウト
    Button createItemButton;
    int categoryId = -1;

	private ArrayList<CheckBox> checkBoxes = new ArrayList<>();

    private static final String TAG = "CheckListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);

	    mScreen = (ViewGroup) findViewById(R.id.listScreen);
		createItemButton = (Button)findViewById(R.id.createItemButton);
	    createItemButton.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
			    Intent intent = new Intent(CheckListActivity.this, AddItemActivity.class);
			    intent.putExtra("categoryID", categoryId);
			    intent.putExtra("mode", AddItemActivity.ITEM_MODE);
			    startActivity(intent);
		    }
	    });

	    categoryId = getIntent().getIntExtra("category", -1);
        Log.v(TAG, "categoryID:" + categoryId);
		checkValue(categoryId);
    }

	@Override
	protected void onResume() {
		super.onResume();
		makeList();
	}

	private void makeList(){
		//共通カテゴリを含めたチェックリストの作成
		MyHelper helper = new MyHelper(this);
		SQLiteDatabase db = helper.getReadableDatabase();

		Log.v("CheckListActivity ID", "" + categoryId);

		//region Description
		//String sql = "SELECT item.name, category._id FROM item.item"
		//endregion


		String str =
				" SELECT DISTINCT item.name, item.category, item._id FROM item, category " +
				" WHERE (category._id = item.category " +
				"       AND category._id = " + categoryId + ") " +
				"       OR item.category = 1 " +
				" ORDER BY item._id";

		Log.v("CheckListActivity クエリ", str);

		Cursor cursor = db.rawQuery(str, null);

		mScreen.removeAllViews();
		int i = 0;
		while (cursor.moveToNext()) {
			String item = "";
			if (cursor.getInt(1) == 1){
				item = " 共通";
			}
			addList(cursor.getString(0) + item, mScreen, i++);

			Log.v(cursor.getString(0), "" + cursor.getInt(1));
		}
		cursor.close();
		db.close();
	}

    private void addList(String text, ViewGroup layout, int index){


        //ll.setOrientation(LinearLayout.HORIZONTAL);

        final CheckBox checkBox = new CheckBox(this);
	    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		    @Override
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			    int count = 0;
			    for (int i = 0; i < checkBoxes.size(); i++) {
				    if (checkBoxes.get(i).isChecked()) {
					    count++;
				    }
			    }

			    if (count == checkBoxes.size()) {
				    //Toast.makeText(CheckListActivity.this, "完了", Toast.LENGTH_LONG).show();

				    for (int i = 0; i < checkBoxes.size(); i++) {
					    checkBoxes.get(i).setChecked(false);
				    }
				    //mScreen.removeViewAt();
				    TextView textView = new TextView(CheckListActivity.this);
				    Calendar cal = Calendar.getInstance();
				    textView.setText((cal.get(Calendar.MONTH) + 1) + "月" + cal.get(Calendar.DATE) + "日　完了");
				    mScreen.addView(textView);
			    }


		    }
	    });
	    LinearLayout ll = new LinearLayout(this);
	    ll.setOrientation(LinearLayout.HORIZONTAL);
	    Button button = new Button(this);
	    button.setTag(index);
	    button.setVisibility(View.GONE);

	    button.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
			    String sql = "DELETE FROM item WHERE " + v.getTag();


		    }
	    });

	    checkBox.setTag(index);
	    checkBoxes.add(checkBox);
        checkBox.setText(text);
        checkBox.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
		        ViewGroup.LayoutParams.WRAP_CONTENT));
        checkBox.setTextSize(32);
        ll.addView(checkBox);

        layout.addView(ll);
    }


    private void checkValue(int val) {
	    try {
		    //値が正常なときだけ処理を実行する。
		    if(val == -1){
			    throw new IncorrectValueException();
		    }
	    } catch (IncorrectValueException e) {
		    e.printStackTrace();
		    IncorrectValueException.showMessage(TAG);

	    }catch (Exception e){
		    e.printStackTrace();
	    }

    }

}
