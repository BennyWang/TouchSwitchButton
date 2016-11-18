package com.benny.app.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.benny.library.tsbutton.TouchSwitchButton;

public class MainActivity extends AppCompatActivity {


    private TouchSwitchButton.OnActionSelectedListener onToLeft = new TouchSwitchButton.OnActionSelectedListener() {
        @Override
        public int onSelected() {
            Toast.makeText(MainActivity.this, "To Left has selected", Toast.LENGTH_LONG).show();
            return 500;
        }
    };

    private TouchSwitchButton.OnActionSelectedListener onToRight = new TouchSwitchButton.OnActionSelectedListener() {
        @Override
        public int onSelected() {
            Toast.makeText(MainActivity.this, "To Right has selected", Toast.LENGTH_LONG).show();
            return 500;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TouchSwitchButton toRight = (TouchSwitchButton) findViewById(R.id.to_right);
        toRight.setOnToRightSelectedListener(onToRight);

        TouchSwitchButton toLeft = (TouchSwitchButton) findViewById(R.id.to_left);
        toLeft.setOnToLeftSelectedListener(onToLeft);

        TouchSwitchButton twoWay = (TouchSwitchButton) findViewById(R.id.two_way);
        twoWay.setOnToLeftSelectedListener(onToLeft);
        twoWay.setOnToRightSelectedListener(onToRight);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
