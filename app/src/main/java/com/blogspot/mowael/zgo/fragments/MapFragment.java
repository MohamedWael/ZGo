package com.blogspot.mowael.zgo.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.blogspot.mowael.zgo.R;
import com.blogspot.mowael.zgo.activities.DistanceDialog;
import com.blogspot.mowael.zgo.activities.MapActivity;
import com.blogspot.mowael.zgo.dataModel.MeasureData;
import com.blogspot.mowael.zgo.utilities.Constants;
import com.blogspot.mowael.zgo.utilities.VolleySingleton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, GoogleMap.OnMapClickListener, MapActivity.MapFragmentResult {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private final String ORIGIN = "origin";
    private final String DESTINATION = "destination";

    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LatLngBounds.Builder builder;
    private LatLngBounds bounds;
    private Location mLastLocation;
    private Marker myLocation;
    private ArrayList<Marker> markers;
    private int numOfMarkers = 0;
    private SupportPlaceAutocompleteFragment autocompleteFragment;
    private DistanceDialogResult distanceDialogResult;
    private TextView etFrom, etTo;
    private Button btnMeasureDistance;
    private DistanceDialog distanceDialog;
    private final int ET_FROM_SEARCH = 1;
    private final int ET_TO_SEARCH = 100;
    private Polyline line;
    private ArrayList<Polyline> polylines;
    private MapResponseArriveListener mapResponseArriveListener;
    private MeasureData data;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        FragmentManager fragmentManager = getChildFragmentManager();
        mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.mapview);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            fragmentManager.beginTransaction().add(R.id.mapview, mapFragment).commit();
            mapFragment.getMapAsync(this);
        }

        markers = new ArrayList<>();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        distanceDialog = new DistanceDialog(getActivity(), getContext());
        distanceDialogResult = distanceDialog;
        mapResponseArriveListener = distanceDialog;
        data = new MeasureData();

        etFrom = (TextView) getActivity().findViewById(R.id.etFrom);
        etTo = (TextView) getActivity().findViewById(R.id.etTo);
        btnMeasureDistance = (Button) getActivity().findViewById(R.id.btnMeasureDistance);
        etFrom.setOnClickListener(this);
        etTo.setOnClickListener(this);
        btnMeasureDistance.setOnClickListener(this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Place place = PlaceAutocomplete.getPlace(getContext(), data);
            if (numOfMarkers == 3) {
                for (Marker mMarker : markers) mMarker.remove();
                markers.clear();
                etTo.setText("To");
                numOfMarkers = 0;
            }

            switch (requestCode) {
                case ET_FROM_SEARCH:
                    if (markers.size() == 1) markers.get(0).remove();
                    Marker origin = mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title("Origin: " + place.getName()));
                    origin.setTag(ORIGIN);
                    etFrom.setText(place.getName());
                    origin.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    markers.add(0, origin);
                    numOfMarkers = 1;
                    break;
                case ET_TO_SEARCH:
                    if (markers.size() == 2) markers.get(1).remove();
                    Marker destination = mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title("Destination: " + place.getName()));
                    destination.setTag(DESTINATION);
                    destination.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                    if (markers.size() == 0) {
                        markers.add(0, destination);
                        markers.add(1, destination);
                    } else markers.add(1, destination);
                    etTo.setText(place.getName());
                    numOfMarkers = 3;
                    break;
            }

        } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            Status status = PlaceAutocomplete.getStatus(getContext(), data);

            Log.i("ERROR", status.getStatusMessage());

        } else {
            // The user canceled the operation.
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            LatLng lastLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            myLocation = mMap.addMarker(new MarkerOptions().position(lastLocation).title("My Location").snippet("this is my current location"));
            myLocation.setTag("myLocation");
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMapClickListener(this);
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if (markers.size() == 2) {
                    float[] result = new float[1];
                    Location.distanceBetween(markers.get(0).getPosition().latitude, markers.get(0).getPosition().longitude,
                            markers.get(1).getPosition().latitude, markers.get(1).getPosition().longitude, result);
                    Toast.makeText(getContext(), result[0] + "m", Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });
        LatLng egypt = new LatLng(26.8206, 30.8025);
        Marker mainMarker = mMap.addMarker(new MarkerOptions().position(egypt).draggable(true).title("Marker in Egypt"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(egypt, 0.15f));

        builder = new LatLngBounds.Builder();
        builder.include(mainMarker.getPosition());
        bounds = builder.build();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Snackbar.make(getView(), "make sure that you are connected!", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.etFrom:
                startPlacesSearch(ET_FROM_SEARCH);
                break;
            case R.id.etTo:
                startPlacesSearch(ET_TO_SEARCH);
                break;
            case R.id.btnMeasureDistance:
                if (markers.size() >= 2) {
                    Marker origin = markers.get(0);
                    Marker destination = markers.get(1);
                    distanceDialog.show();
                    loadContent(Constants.GOOGLE_MAPS_API + Constants.ORIGIN + origin.getPosition().latitude + "," + origin.getPosition().longitude +
                            Constants.DESTINATION + destination.getPosition().latitude + "," + destination.getPosition().longitude +
                            Constants.DEPARTURE_TIME + new Date().getTime() + Constants.TRAFFIC_MODE +
                            Constants.KEY + getActivity().getString(R.string.google_maps_key));
                    data.setOriginLatitude(origin.getPosition().latitude);
                    data.setOriginLongitude(origin.getPosition().longitude);
                    data.setDestinationLatitude(destination.getPosition().latitude);
                    data.setDestinationLongitude(destination.getPosition().longitude);

                } else {
                    Snackbar.make(getView(), "Please specify location", Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {

//        if (numOfMarkers < 2) {
        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
        switch (numOfMarkers) {
            case 0:
                if (markers.size() == 1) markers.get(0).remove();
                marker.setTitle("Origin");
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                marker.setTag(ORIGIN);
                etFrom.setText("Origin");
                break;
            case 1:
                if (markers.size() == 2) markers.get(1).remove();
                marker.setTitle("Destination");
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                marker.setTag(DESTINATION);
                etTo.setText("Destination");
                break;
            default:
                for (Marker mMarker : markers) mMarker.remove();
                markers.clear();
                marker.setTitle("Origin");
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                marker.setTag(ORIGIN);
                etFrom.setText("Origin");
                etTo.setText("To");
                numOfMarkers = 0;
                break;
        }
        markers.add(marker);
        numOfMarkers++;

    }

    @Override
    public void onMapFragmentResult(int requestCode, int resultCode, Intent data) {
        distanceDialogResult.OnDialogResult(requestCode, resultCode, data);
    }

    private void startPlacesSearch(int requestCode) {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(getActivity());
            startActivityForResult(intent, requestCode);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }


    private void loadContent(final String url) {
        Log.d("URL", url);
        final VolleySingleton volley = VolleySingleton.getInstance(getContext());

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, volley.uriEncoder(url), new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("response", response.toString());
                    if (line != null) line.remove();
                    else if (polylines != null && polylines.size() > 0) {
                        for (Polyline polyline : polylines) {
                            polyline.remove();
                        }
                        polylines.clear();
                    }

                    if (response.getString("status").equals("ZERO_RESULTS")) {
                        Snackbar.make(getView(), "no road found", Snackbar.LENGTH_LONG).show();
                    } else if (response.getString("status").equals("OK")) {
                        data.setRequestUrl(volley.uriEncoder(url));
                        data.setResponseJsonStr(response.toString());
                        mapResponseArriveListener.onMapResponseArriveListener(response, data);
                        drawPath(response);
                    } else {
                        Toast.makeText(getContext(), "something went error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", error.toString());
                error.printStackTrace();
            }
        });
        RequestQueue queue = volley.getRequestQueue();
        queue.add(jsonObjectRequest);
    }

    public void drawPath(JSONObject json) {

        try {
            polylines = new ArrayList<>();
            //Tranform the string into a json object
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);

            line = mMap.addPolyline(new PolylineOptions()
                    .addAll(list)
                    .width(12)
                    .color(Color.parseColor("#05b1fb"))//Google maps blue color
                    .geodesic(true));

            for (int z = 0; z < list.size() - 1; z++) {
                LatLng src = list.get(z);
                LatLng dest = list.get(z + 1);
                Polyline line = mMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude, dest.longitude))
                        .width(5)
                        .color(Color.BLUE).geodesic(true));
                polylines.add(line);
            }

        } catch (JSONException e) {

        }
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    public interface DistanceDialogResult {
        void OnDialogResult(int requestCode, int resultCode, Intent data);
    }

    public interface MapResponseArriveListener {
        void onMapResponseArriveListener(JSONObject response, MeasureData data);
    }

}
