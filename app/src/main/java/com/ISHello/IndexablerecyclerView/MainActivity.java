package com.ISHello.IndexablerecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ISHello.IndexablerecyclerView.city.PickCityActivity;
import com.ISHello.IndexablerecyclerView.contact.PickContactActivity;
import com.ISHello.base.base.BaseActivity;
import com.example.ishelloword.R;


/**
 * @author
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("IndexablerecyclerView");
        setContentView(R.layout.activity_indexable_recycler);

        findViewById(R.id.btn_pick_city).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PickCityActivity.class));
            }
        });
        findViewById(R.id.btn_pick_contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PickContactActivity.class));
            }
        });
    }
}
