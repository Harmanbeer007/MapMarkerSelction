package sandhu.harman.mapmarkerselction;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    List<LatLng> storedMarkerLatlng;
    List<LatLng> toDeleteMarkerLatlng;
    private ListView list;
    private Polyline line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
         list=(ListView)findViewById(R.id.myLatLongList);
        storedMarkerLatlng=new LinkedList<>();
        toDeleteMarkerLatlng=new LinkedList<>();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);
                storedMarkerLatlng.add(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                // Clears the previously touched position
//                mMap.clear();

                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);

                  line = mMap.addPolyline(new PolylineOptions()
                        .width(5)
                        .addAll(storedMarkerLatlng)
                        .color(Color.RED));
                ArrayAdapter<LatLng> arrayAdapter=new ArrayAdapter<LatLng>(MapsActivity.this,android.R.layout.simple_list_item_1,storedMarkerLatlng);

                list.setAdapter(arrayAdapter);
                list.setSelection(arrayAdapter.getCount() - 1);
                list.setEnabled(false);
//                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                    @Override
//                    public boolean onMarkerClick(Marker marker) {
//                        Toast.makeText(MapsActivity.this, marker.getPosition().toString(), Toast.LENGTH_SHORT).show();
//                        toDeleteMarkerLatlng.add(marker.getPosition());
//                        storedMarkerLatlng.removeAll(toDeleteMarkerLatlng);
//                        marker.remove();
//                        line.remove();
//                        ((ArrayAdapter) list.getAdapter()).notifyDataSetChanged();
//                        return false;
//
//                    }
//                });
                Intent resultIntent = new Intent(MapsActivity.this, ResultActivity.class);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent piResult = PendingIntent.getActivity(MapsActivity.this,
                        (int) Calendar.getInstance().getTimeInMillis(), resultIntent, 0);
                NotificationCompat.InboxStyle inboxStyle =
                        new NotificationCompat.InboxStyle();

                inboxStyle.setBigContentTitle("Inbox Notification");
                inboxStyle.addLine("Message 1.");
                inboxStyle.addLine("Message 2.");
                inboxStyle.addLine("Message 3.");
                inboxStyle.addLine("Message 4.");
                inboxStyle.addLine("Message 5.");
                inboxStyle.setSummaryText("+2 more");

//build notification
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(MapsActivity.this)
                                .setSmallIcon(R.drawable.ic_launcher_background)
                                .setContentTitle("Inbox style notification")
                                .setContentText("This is test of inbox style notification.")
                                .setStyle(inboxStyle)
                                .addAction(R.drawable.ic_launcher_foreground, "show activity", piResult);
//                NotificationCompat.CarExtender v=new NotificationCompat.CarExtender();
//v.setColor(Color.BLACK);
                NotificationManager manager=(NotificationManager)getSystemService(MapsActivity.this.NOTIFICATION_SERVICE);
            manager.notify(0,mBuilder.build());}
        });


    }


}
