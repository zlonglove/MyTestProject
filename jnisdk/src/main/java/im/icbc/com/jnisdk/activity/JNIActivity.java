package im.icbc.com.jnisdk.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import im.icbc.com.jnisdk.Native.JNI;
import im.icbc.com.jnisdk.R;
import im.icbc.com.jnisdk.Student;


public class JNIActivity extends AppCompatActivity {
    private final String TAG = JNIActivity.class.getSimpleName();
    private Button onStartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jni);
        Log.i(TAG, "--->CPU_ABI==" + Build.CPU_ABI);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportABIList();
        }
        onStartButton = (Button) findViewById(R.id.onStartClick);
        onStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStartClick();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getSupportABIList() {
        String[] supportAbiList=Build.SUPPORTED_ABIS;
        for (String supportAbi:supportAbiList) {
            Log.i(TAG, "--->Support abis==" + supportAbi);
        }
    }

    @Override
    protected void onDestroy() {
        JNI.getInstance().Release();
        super.onDestroy();
    }

    private void onStartClick() {
        Log.i(TAG, "--->" + JNI.getInstance().stringFromJNI());
        JNI.getInstance().stringPutJNI("hello word");

        Log.i(TAG, JNI.getInstance().getStaticField());

        Log.i(TAG, JNI.getInstance().getInstanceField());

        Log.i(TAG, JNI.getInstance().getIntField() + "");
        Log.i(TAG, "--->" + JNI.getInstance().getInt());
        Log.i(TAG, "--->" + JNI.getInstance().getInstanceMethod());
        Log.i(TAG, "--->" + JNI.getInstance().getStaticMethod());
        Log.i(TAG, "--->" + JNI.getInstance().GetIntInstanceMethod());
        JNI.getInstance().accessMethod();
        try {
            JNI.getInstance().throwException();
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }
        JNI.getInstance().syncThread();
        JNI.getInstance().startThread();
        Log.i(TAG, "--->" + JNI.getInstance().dynamicRegister());
        Log.i(TAG, "--->" + JNI.getInstance().dynamicRegister2(23));
        int[] a = {1, 2, 3, 4, 5};
        Log.i(TAG, "--->" + JNI.getInstance().sumArray(a));

        String[] stringArray = {"hello", "word", "zhang", "long"};
        JNI.getInstance().setStringArray(stringArray);

        String[] array = JNI.getInstance().getStringArray();
        for (int i = 0; i < array.length; i++) {
            Log.i(TAG, "--->Element is==" + array[i]);
        }
        Student student = JNI.getInstance().getStudentInfo();
        Log.i(TAG, "--->" + student.toString());

        Student student2 = new Student(25, "zhanglong");
        JNI.getInstance().setStudentInfo(student2);

        ArrayList<Student> list = JNI.getInstance().getListStudents();
        for (int i = 0; i < list.size(); i++) {
            Log.i(TAG, "--->" + list.get(i).toString());
        }

        JNI.getInstance().writeFile("jni write file!");
        Log.i(TAG, "--->read file==" + JNI.getInstance().readFile());
    }
}
