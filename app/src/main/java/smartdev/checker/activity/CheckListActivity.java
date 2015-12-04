package smartdev.checker.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import smartdev.checker.MyHelper;
import smartdev.checker.R;
import smartdev.checker.throwable.IncorrectValueException;

public class CheckListActivity extends AppCompatActivity {

    ViewGroup mScreen; //アイテムを追加していくレイアウト
    Button createItemButton;
    int categoryId = -1;
	final static int COMMON = -1;

	private ArrayList<CheckBox> checkBoxes = new ArrayList<>();
	private ArrayList<Integer> itemIdList = new ArrayList<>();

    private static final String ACTIVITY_NAME = "CheckListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);

	    categoryId = getIntent().getIntExtra("category", -1);

	    //region Log
	    if(categoryId != -1) Log.v(ACTIVITY_NAME, "カテゴリIDは" + categoryId + "です。");
	    else                 Log.e(ACTIVITY_NAME, "カテゴリIDは" + categoryId + "です。");
	    //endregion
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

	    Button deleteButton = (Button) findViewById(R.id.deleteButton);
	    deleteButton.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
			    for (int i = 0; i < mScreen.getChildCount(); i++) {

				    Button child = (Button) ((ViewGroup) mScreen.getChildAt(i)).getChildAt(0);

				    if (child.getVisibility() == View.GONE) {
					    child.setVisibility(View.VISIBLE);
					    if (child.getTag().equals(COMMON)) {
						    child.setVisibility(View.INVISIBLE);
					    }

				    } else {
					    child.setVisibility(View.GONE);
				    }
			    }

		    }
	    });

		checkValue(categoryId);
    }

	@Override
	protected void onResume() {
		super.onResume();
		//前回のリストが出ていると邪魔なので、全削除
		mScreen.removeAllViews();

		MyHelper helper = new MyHelper(this);
		SQLiteDatabase db = helper.getReadableDatabase();

		String str =
				" SELECT DISTINCT item.name, item.category, item._id FROM item, category " +
						" WHERE (category._id = item.category " +
						"       AND category._id = " + categoryId + ") " +
						"       OR item.category = 1 " +
						" ORDER BY item.category DESC, item._id ASC";

		Cursor cursor = db.rawQuery(str, null);

		int i = 0;
		while (cursor.moveToNext()) {
			String item = "";
			if (cursor.getInt(1) == 1) {
				item = " 共通";
			}
			itemIdList.add(cursor.getInt(2));
			addList(cursor.getString(0) + item, mScreen, i++);

			Log.d("リスト アイテム確認",
					"名前:" + cursor.getString(0) +
							", item.category:" + cursor.getInt(1) +
							", item._id:" + cursor.getInt(2));
		}
		cursor.close();
		db.close();
	}

    private void addList(String text, ViewGroup layout, int index){

        CheckBox checkBox = new CheckBox(this);
	    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		    @Override
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			    int count = 0;
			    //全部チェックされたら、-月-日 完了と表示
			    for (int i = 0; i < checkBoxes.size(); i++) {
				    if (checkBoxes.get(i).isChecked()) {
					    count++;
				    }
			    }

			    if (count == checkBoxes.size()) {
				    for (int i = 0; i < checkBoxes.size(); i++) {
					    checkBoxes.get(i).setChecked(false);
				    }
				    Calendar cal = Calendar.getInstance();
				    int month = cal.get(Calendar.MONTH) + 1;
				    int date = cal.get(Calendar.DATE);

				    TextView textView = new TextView(CheckListActivity.this);
				    textView.setText(month + "月" + date + "日　完了");
				    mScreen.addView(textView);
			    }
		    }
	    });
	    LinearLayout ll = new LinearLayout(this);
	    ll.setOrientation(LinearLayout.HORIZONTAL);

	    checkBox.setTag(index);
	    checkBoxes.add(checkBox);

        checkBox.setText(text);
        checkBox.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
		        ViewGroup.LayoutParams.WRAP_CONTENT));
        checkBox.setTextSize(32);

	    Button button = new Button(this);
	    button.setTag(index);
	    button.setBackgroundColor(Color.RED);
	    button.setTextColor(Color.WHITE);
	    button.setText("-");
	    button.setVisibility(View.GONE);
	    if(checkBox.getText().toString().contains("共通")){
		    button.setTag(COMMON);
	    }else {
		    button.setOnClickListener(new View.OnClickListener() {
			    @Override
			    public void onClick(View v) {
				    //TODO 削除ボタン
				    String sql = "DELETE FROM item " +
						    " WHERE item.category = " + categoryId +
						    " AND item._id = " + v.getTag();

				    Log.v("sql確認", sql);

				    MyHelper helper = new MyHelper(CheckListActivity.this);
				    SQLiteDatabase db = helper.getWritableDatabase();
				    db.execSQL(sql);
				    Toast.makeText(CheckListActivity.this, "削除", Toast.LENGTH_LONG).show();
			    }
		    });
	    }

	    //子レイアウトに追加
	    ll.addView(button);
	    ll.addView(checkBox);

	    //親レイアウトにllを追加
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
		    IncorrectValueException.showMessage(ACTIVITY_NAME);
	    }catch (Exception e){
		    e.printStackTrace();
	    }
    }
}
