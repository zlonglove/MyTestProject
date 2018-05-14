package zlonglove.cn.adrecyclerview.tools;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * 描述:
 * <p>
 * 创建时间:2017年11月03日 15:14
 *
 * @version 1.0
 */
public class IOKit {
    /**
     * 从asset目录下获取指定文件里面的string
     * @param context
     * @param fileName 文件名字
     * @return
     */
    public static String getStringFromAssets(Context context, String fileName) {
        String str = "";
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(context.getAssets().open(fileName), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();
            str = stringBuilder.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return str;
    }
}
