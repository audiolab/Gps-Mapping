package org.audiolab.gps.mapping;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Gps_mapping extends Activity {

	private LocationManager lManager;
	private Handler mHandler;
	private TextView tUbica;
	private TextView tRegistros;
	private FileWriter fWriter;
	
	private boolean writeToFile = false;
	private Integer numRegistros = 0;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_mapping);
        
        lManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        tUbica = (TextView) findViewById(R.id.ubicacion);
        tRegistros = (TextView) findViewById(R.id.textRegistros);
        
        if (isExternalStorageWritable()){
            File or = Environment.getExternalStorageDirectory();
            File fold = new File(or.getAbsolutePath() + "/gps_map");
            fold.mkdir();
            File fn = new File(fold.getAbsolutePath(),"gps_map.info");
            try {
                fWriter = new FileWriter(fn);				
                fWriter.write("GPS_MAPPING@@");                
			} catch (IOException e) {
				// TODO: handle exception
				tUbica.setText("Fallo al crear el archivo");
			}
        }

        
        mHandler = new Handler(){
        	
        	public void handleMessage( Message msg){
        		 tUbica.setText((String) msg.obj);        		 
        		 tRegistros.setText(numRegistros.toString());
        	}
        	
        };
        
        
    }

    /* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.start_recording:
	            writeToFile = true;
	            return true;
	        case R.id.stop_recording:
	            //Guardar el archivo.
	        	try {
					fWriter.write("@@END_OF_FILE");
					fWriter.close();
				} catch (IOException e) {
					// TODO: handle exception
					Log.e("GPSMAP", "Problema al intentar cerrar el archivo");
				}
	        	writeToFile = false;
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		lManager.removeUpdates(lListener);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		lManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,500,0.5f,lListener);		
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_gps_mapping, menu);
        return true;
    }
	
	private final LocationListener lListener = new LocationListener() {
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			Double latitude = location.getLatitude();
			Double longitude = location.getLongitude();
			Float accuracy = location.getAccuracy();
			if(writeToFile){
				try {
					numRegistros = numRegistros + 1;
					fWriter.write("@@" + latitude.toString() + ";" + longitude.toString() + ";" + accuracy.toString() + "@@");
				} catch (IOException e) {
					// TODO: handle exception
					Log.e("GPSMAP","Error al guardar un registro");
				}
			}
	        Message.obtain(mHandler,1, latitude.toString() + ", " + longitude.toString() + ", " + accuracy.toString()).sendToTarget();
			
		}
	};
	
	
	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
}
