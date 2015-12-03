package smartdev.checker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MyHelper extends SQLiteOpenHelper {


	//データベースの名前
	private static String dbName = "test";

    //ふでばこなどのアイテム
	private String createTableSQL = "CREATE TABLE item(_id integer primary key autoincrement, " +
            "name text NOT NULL, " +
            "category integer NOT NULL, " +
            "FOREIGN KEY (category) REFERENCES category(_id));";

    //リストに表示するカテゴリ
    private String createTableCate = "CREATE TABLE category(_id integer primary key autoincrement, " +
            "name text);";

	public MyHelper(Context context) {
		super(context,dbName, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO 自動生成されたメソッド・スタブ
        db.execSQL(createTableSQL);
        db.execSQL(createTableCate);

        // 共通カテゴリを作る
        String insertKyotu = "INSERT INTO category(name) VALUES('common')";
        db.execSQL(insertKyotu);

        String rei = "INSERT INTO category(name) VALUES('学校')";
        db.execSQL(rei);

		String common1 = "INSERT INTO item(name, category) VALUES('ハンカチ',1)";
		String common2 = "INSERT INTO item(name, category) VALUES('ティッシュ',1)";
        String rei1 = "INSERT INTO item(name, category) VALUES('ふでばこ', 2)";
        String rei2 = "INSERT INTO item(name, category) VALUES('学生証', 2)";
		db.execSQL(common1);
		db.execSQL(common2);
        db.execSQL(rei1);
        db.execSQL(rei2);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO 自動生成されたメソッド・スタブ

	}


}
