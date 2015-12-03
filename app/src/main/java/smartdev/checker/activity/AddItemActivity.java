package smartdev.checker.activity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import smartdev.checker.MyHelper;
import smartdev.checker.R;

public class AddItemActivity extends AppCompatActivity {

	static final int ITEM_MODE = 0;
	static final int CATEGORY_MODE = 1;

	private final String TAG = "AddItemActivity";
	private Button button;
	private EditText editText;
	private SQLiteDatabase db;
	private int mode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_item);

		MyHelper myHelper = new MyHelper(this);
		db = myHelper.getWritableDatabase();

		mode = getIntent().getIntExtra("mode", -1);
		editText = (EditText) findViewById(R.id.itemNameEditText);
		button = (Button) findViewById(R.id.addItemButton);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String sql;
				if (mode == ITEM_MODE) {

					sql = "INSERT INTO item(name, category) VALUES('" +
							editText.getText().toString() + "'," + getIntent().getIntExtra("categoryID", -1) +
							")";
				} else {
					sql = "INSERT INTO category(name) VALUES('" +
							editText.getText().toString() + "')";
				}

				db.execSQL(sql);
				Toast.makeText(AddItemActivity.this, "項目を追加しました。", Toast.LENGTH_LONG).show();
			}
		});
	}

}
