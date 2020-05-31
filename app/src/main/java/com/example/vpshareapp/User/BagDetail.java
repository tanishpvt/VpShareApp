package com.example.vpshareapp.User;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vpshareapp.Admin.AllCommanderDetail;
import com.example.vpshareapp.MainActivity;
import com.example.vpshareapp.MainScreen;
import com.example.vpshareapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

import static android.app.Notification.DEFAULT_SOUND;
import static android.content.ContentValues.TAG;
import static androidx.core.app.NotificationCompat.DEFAULT_VIBRATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class BagDetail extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMarkerClickListener,
        View.OnClickListener {


    String on="0";
    //map component
    private static final String TAG = "BagDetail";
    MapView mMapView;
    private GoogleMap googleMap;
    private double longitude;
    private double latitude;
    private GoogleApiClient googleApiClient;


    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;

    String uid;
    Button checkLocation;


    //varible to tell whick  location acity to display
    String location1 = "0", status1 = "0";

    //all acitvity component
    TextView bagId, bagName, bagDonated, bagAllocated, trackingId, bag_Status;


    public BagDetail() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bag_detail, container, false);


        createNotificationChannel();

        mMapView = (MapView) view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        on="1";
        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        checkPermission();



            mMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap mMap) {
                    googleMap = mMap;

                    // For showing a move to my location button
                    googleMap.setMyLocationEnabled(true);

                    // For dropping a marker at a point on the Map
                    LatLng sydney = new LatLng(-34, 151);
                    googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                    // For zooming automatically to the location of the marker
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            });
            //Initializing googleApiClient
            googleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();


        //init firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        //init componets
        bagId = view.findViewById(R.id.bagDetail_bag_id);
        bagName = view.findViewById(R.id.bagDetail_bagName);
        bagDonated = view.findViewById(R.id.bagDetail_donated);
        bagAllocated = view.findViewById(R.id.bagDetail_allocatedTo);
        trackingId = view.findViewById(R.id.bagDetail_trackingId);
        bag_Status = view.findViewById(R.id.bag_Status);
        checkLocation = view.findViewById(R.id.checkLocation);


        checkforuserlogin();

        Query query = databaseReference.orderByChild("Email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String bagid = "" + ds.child("bagid").getValue();
                    String bagname = "" + ds.child("Barcode").getValue();
                    String bagdonated = "" + ds.child("Donated").getValue();
                    String bagallocated = "" + ds.child("Name").getValue();
                    String trakingid = "" + ds.child("bagid").getValue();


                    //init components
                    bagId.setText(bagid);
                    bagName.setText(bagname);
                    bagDonated.setText(bagdonated);
                    bagAllocated.setText(bagallocated);
                    trackingId.setText(trakingid);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
        Query query1 = FirebaseDatabase.getInstance().getReference("Bags").orderByChild("bagid").equalTo(user.getUid());
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String status = ds.child("status").getValue().toString();
                    String location = ds.child("location").getValue().toString();
                    //it is a varible that tell whether location is null or already set
                    if (status.equals("0")) {
                        bag_Status.setText(" User Allocated To Bag");

                        if (location.equals("0")) {
                            location1 = "0";
                            Toast.makeText(getActivity(), "get location from user and store to firebase", Toast.LENGTH_SHORT).show();
                        } else {
                            location1 = "1";
                            Toast.makeText(getActivity(), "get location from firebase", Toast.LENGTH_SHORT).show();
                        }
                    } else if (status.equals("1")) {
                        status1 = "1";
                        location1 = "1";
                        bag_Status.setText(" Commander Collected And Sending Bags To Admin");
                    } else if (status.equals("2")) {
                        location1 = "2";
                        status1 = "2";
                        //send Notification  to  User
                        bag_Status.setText("Commander Sending Bags To Ngo");
                    }
                    else if(status.equals("3")){
                        location1 = "3";
                        status1 = "3";
                        //send Notification  to  User
                        triggerNotification();
                        bag_Status.setText("NGO Collected Bags");
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        checkLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start map and show address of user
                if (location1.equals("0")) {
                    //start map acivity that will create location and will store in  database
                    Intent intent = new Intent(getActivity(), GetLocationUser.class);
                    intent.putExtra("barcode", bagName.getText().toString());

                    startActivity(intent);
                } else {
                    //start acivty that will take location from firebase and show to user
                    Intent intent = new Intent(getActivity(), GetBagLocationFirebase.class);
                    intent.putExtra("barcode", bagName.getText().toString());
                    intent.putExtra("status", status1);
                    startActivity(intent);
                }
            }
        });


        return view;
    }





    //its for notification channel creation
    private void createNotificationChannel(){


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(getString(R.string.DonatedNotify_ID)
                    ,getString(R.string.CHANNEL_DONATED), NotificationManager.IMPORTANCE_HIGH );
            notificationChannel.setDescription(getString(R.string.CHANNEL_donatedDes));
            notificationChannel.setShowBadge(true);
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);


            if (defaultSoundUri != null) {
                AudioAttributes att = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build();
                notificationChannel.setSound(defaultSoundUri, att);
            }

            notificationManager.createNotificationChannel(notificationChannel);


        }
    }


    //its to show actual notification
    private void triggerNotification(){
        Intent intent = new Intent(getContext(), MainScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(),0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), getString(R.string.DonatedNotify_ID))
                .setSmallIcon(R.drawable.onlylogonottext)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.onlylogonottext))
                .setContentTitle("Alert Message")
                .setContentText("your Donated Stuff"+bagDonated.getText().toString()+"is received By NGO  \n" +
                        " For More Details Visit Your Mail")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(("your Donated Stuff"+bagDonated.getText().toString()+"is received By NGO  \n" +
                        " For More Details Visit Your Mail")))
              //  .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(),R.drawable.ngoimage)))
                .setContentIntent(pendingIntent)
                .setChannelId(getString(R.string.DonatedNotify_ID))
                .setAutoCancel(true)
                .setDefaults(DEFAULT_SOUND | DEFAULT_VIBRATE)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setVibrate(new long[]{Notification.DEFAULT_VIBRATE})
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getActivity());
        notificationManagerCompat.notify(1, builder.build());


    }


    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        //permission is not granted
            new AlertDialog.Builder(getActivity(),4)
                    .setTitle("Required Location Permission")
                    .setMessage("You have to give this permission to acess this feature")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create()
                    .show();
            return;

        }
        else {


        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void checkforuserlogin() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {

            uid = user.getUid();


        } else {
            startActivity(new Intent(getActivity(), MainActivity.class));
            try {
                Objects.requireNonNull(getActivity()).finish();
            } catch (NullPointerException e) {

            }
        }

        checkPermission();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            SharedPreferences.Editor editor;
            editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
            editor.remove("username");
            editor.remove("password");
            editor.apply();
            firebaseAuth.signOut();
            checkforuserlogin();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }


    //below function is use for taking location from user to show map
    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        // googleMapOptions.mapType(googleMap.MAP_TYPE_HYBRID)
        //    .compassEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng india = new LatLng(-34, 151);
        googleMap.addMarker(new MarkerOptions().position(india).title("Marker in India"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(india));
        googleMap.setOnMarkerDragListener(this);
        googleMap.setOnMapLongClickListener(this);



        if (!location1.equals("0")) {
            Toast.makeText(getActivity(), "getting", Toast.LENGTH_SHORT).show();
            getCurrentLocation();
        }
    }

    //Getting current location
    private void getCurrentLocation() {
        googleMap.clear();

        if (location1.equals("0")&&status1.equals("0"))
         {
           //  Toast.makeText(getActivity(), "Setting location ", Toast.LENGTH_SHORT).show();
            //if location is not set already and status is 0 then take location from user and store it
            googleMap.clear();
            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                new AlertDialog.Builder(getActivity(),4)
                        .setTitle("Required Location Permission")
                        .setMessage("You have to give this permission to acess this feature")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create()
                        .show();
                return;
            }
            else {



            }
            Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (location != null) {
                //Getting longitude and latitude
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                DatabaseReference database = FirebaseDatabase.getInstance().getReference("Bags");
                final Query query = database.orderByChild("Barcode").equalTo(bagName.getText().toString());
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                          //  Toast.makeText(getContext(), "Status Updated..", Toast.LENGTH_SHORT).show();
                            String child = ds.getKey();
                            dataSnapshot.getRef().child(child).child("latitude").setValue(latitude);
                            dataSnapshot.getRef().child(child).child("longitude").setValue(longitude);
                            dataSnapshot.getRef().child(child).child("location").setValue("1");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //moving the map to location
                moveMap();
            }

        } else {
            Toast.makeText(getActivity(), "getting ", Toast.LENGTH_SHORT).show();
            //if location is  set already and status is 0 or more 0 then take location from Firebase and store it
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Bags");
            Query query = databaseReference.orderByChild("Barcode").equalTo(bagName.getText().toString());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        // Add a marker in Sydney and move the camera
                        latitude = ds.child("latitude").getValue(Double.class);
                        longitude = ds.child("longitude").getValue(Double.class);

                        LatLng sydney = new LatLng(latitude, longitude);
                        if (status1.equals("1")) {
                            googleMap.addMarker(new MarkerOptions().position(sydney).title("Bag Received By Commander"));
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12F));
                        } else if (status1.equals("2")){

                            googleMap.addMarker(new MarkerOptions().position(sydney).title("Bag Received By Admin"));
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12F));

                        }
                        else if (status1.equals("0")){
                            googleMap.addMarker(new MarkerOptions().position(sydney).title("Bag Allocated To User"));
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10F));
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }





    private void moveMap() {
        /**
         * Creating the latlng object to store lat, long coordinates
         * adding marker to map
         * move the camera with animation
         */
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true)
                .title("Marker in Bag"));

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        googleMap.getUiSettings().setZoomControlsEnabled(true);


    }

    @Override
    public void onClick(View view) {
        Log.v(TAG,"view click event");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        // mMap.clear();
        googleMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        Toast.makeText(getContext(), "onMarkerDragStart", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        Toast.makeText(getContext(), "onMarkerDrag", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        // getting the Co-ordinates
        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;

        //move to current position
        moveMap();
    }

    @Override
    public void onStart() {
        if (on.equals("1"))
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {

        if (on.equals("1"))
        googleApiClient.disconnect();
        super.onStop();
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(getContext(), "onMarkerClick", Toast.LENGTH_SHORT).show();
        return true;
    }

}