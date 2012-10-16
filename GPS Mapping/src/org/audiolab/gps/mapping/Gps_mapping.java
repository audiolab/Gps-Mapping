package org.audiolab.gps.mapping;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Gps_mapping extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_mapping);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_gps_mapping, menu);
        return true;
    }
}
