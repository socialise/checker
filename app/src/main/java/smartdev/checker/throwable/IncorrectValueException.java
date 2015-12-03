package smartdev.checker.throwable;

import android.util.Log;

/**
 * Created by Administrator on 15/10/14.
 */
public class IncorrectValueException extends Throwable{

    public static void showMessage(String location){
        Log.e(location, "エラーが発生しました。\n カテゴリIDの取得失敗");
    }
}
