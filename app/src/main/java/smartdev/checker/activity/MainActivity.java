package smartdev.checker.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import smartdev.checker.MyHelper;
import smartdev.checker.R;

//最初に起動されるアクティビティ
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

	Button createButton;
    HashMap<Integer, Integer> hashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        createButton = (Button)findViewById(R.id.createItemButton);
		createButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, AddItemActivity.class)
						.putExtra("mode", AddItemActivity.CATEGORY_MODE));
			}
		});
    }

	@Override
	protected void onResume() {
		super.onResume();
		makeList();
	}

	private void makeList(){
		ListView listView = (ListView) findViewById(R.id.categoryList);
		List<String> stringList = new ArrayList<>();

		MyHelper myHelper = new MyHelper(this);
		SQLiteDatabase db = myHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM category WHERE NOT (name = 'common')", null);

		int position = 0;
		while(cursor.moveToNext()){
			stringList.add(cursor.getString(1));        //リストにカテゴリの名前を追加
			hashMap.put(position++, cursor.getInt(0));  //ハッシュマップにカテゴリIDを追加
			Log.v("MainActivity", "" + cursor.getInt(0));
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
				android.R.layout.simple_expandable_list_item_1, stringList);

		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		cursor.close();
	}


	@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Log.v("MainActivity カテゴリＩＤ", "" + hashMap.get(position ));

        Intent intent = new Intent(this, CheckListActivity.class);
        intent.putExtra("category", hashMap.get(position));
        startActivity(intent);
    }
}
