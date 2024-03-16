package com.example.mediapipebanderas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.Manifest;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mediapipebanderas.Interface.CountryApiService;
import com.example.mediapipebanderas.Modelo.CountryResponse;
import com.example.mediapipebanderas.Modelo.Results;
import com.example.mediapipebanderas.ml.Banderas;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolygonOptions;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import retrofit2.Callback;
import retrofit2.Response;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    public static int REQUEST_CAMERA = 111;
    public static int REQUEST_GALLERY = 222;
    Bitmap mSelectedImage;
    TextView txtResults;
    MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtResults = findViewById(R.id.txtresults);
        mapView = findViewById(R.id.mapView);

        mapView.onCreate(savedInstanceState);
    }

    public void abrirGaleria(View view) {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && null != data) {
            try {
                if (requestCode == REQUEST_CAMERA)
                    mSelectedImage = (Bitmap) data.getExtras().get("data");
                else
                    mSelectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void escanear(View v) {
        try {
            String[] etiquetas = {"AR", "BE", "BR", "CO", "CR", "EC", "ES", "FR", "GB", "MX", "PT", "SE", "UY"};
            Banderas model = Banderas.newInstance(getApplicationContext());
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            inputFeature0.loadBuffer(convertirImagenATensorBuffer(mSelectedImage));
            Banderas.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            String prediction = etiquetas[obtenerPosicionMayorProbabilidad(outputFeature0.getFloatArray())];
            model.close();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://www.geognos.com/api/en/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            CountryApiService service = retrofit.create(CountryApiService.class);
            Call<CountryResponse> call = service.getCountryInfo(prediction);

            call.enqueue(new Callback<CountryResponse>() {
                @Override
                public void onResponse(Call<CountryResponse> call, Response<CountryResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        CountryResponse countryResponse = response.body();
                        Results results = countryResponse.getResults();

                        String countryName = results.getName();
                        String capital = results.getCapital().getName();
                        String iso2 = results.getCountryCodes().getIso2();
                        int isoN = results.getCountryCodes().getIsoN();
                        String iso3 = results.getCountryCodes().getIso3();
                        String fips = results.getCountryCodes().getFips();
                        String telPref = results.getTelPref();
                        double[] geoPt = results.getCapital().getGeoPt();
                        String geoRectangle = "West: " + results.getGeoRectangle().getWest() +
                                ", East: " + results.getGeoRectangle().getEast() +
                                ", North: " + results.getGeoRectangle().getNorth() +
                                ", South: " + results.getGeoRectangle().getSouth();

                        String countryInfo = "Nombre: " + countryName + "\n" +
                                "Capital: " + capital + "\n" +
                                "iso2: " + iso2 + "\n" +
                                "isoN: " + isoN + "\n" +
                                "iso3: " + iso3 + "\n" +
                                "fips: " + fips + "\n" +
                                "TelPref: " + telPref + "\n" +
                                "GeoPt: " + Arrays.toString(geoPt) + "\n" +
                                "GeoRectangle: " + geoRectangle;

                        txtResults.setText(countryInfo);

                        ImageView imageViewFlag = findViewById(R.id.imagen_bandera);
                        String flagUrl = "http://www.geognos.com/api/en/countries/flag/" + prediction  + ".png";
                        Glide.with(MainActivity.this)
                                .load(flagUrl)
                                .into(imageViewFlag);

                        mapView.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                double west = results.getGeoRectangle().getWest();
                                double east = results.getGeoRectangle().getEast();
                                double north = results.getGeoRectangle().getNorth();
                                double south = results.getGeoRectangle().getSouth();

                                LatLngBounds bounds = new LatLngBounds(
                                        new LatLng(south, west),
                                        new LatLng(north, east)
                                );

                                googleMap.addPolygon(new PolygonOptions()
                                        .add(
                                                new LatLng(south, west),
                                                new LatLng(south, east),
                                                new LatLng(north, east),
                                                new LatLng(north, west),
                                                new LatLng(south, west)
                                        )
                                        .strokeColor(Color.RED)
                                        .fillColor(Color.argb(50, 150, 50, 50)));

                                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100)); // 100 px de padding
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<CountryResponse> call, Throwable t) {
                    txtResults.setText("Error en la solicitud: " + t.getMessage());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private ByteBuffer convertirImagenATensorBuffer(Bitmap bitmap) {
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3);
        byteBuffer.order(ByteOrder.nativeOrder());
        int[] intValues = new int[224 * 224];
        resizedBitmap.getPixels(intValues, 0, resizedBitmap.getWidth(), 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight());
        int pixel = 0;
        for (int i = 0; i < 224; i++) {
            for (int j = 0; j < 224; j++) {
                final int val = intValues[pixel++];
                byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
            }
        }
        return byteBuffer;
    }
    private int obtenerPosicionMayorProbabilidad(float[] probabilities) {
        int position = 0;
        float maxProb = 0;
        for (int i = 0; i < probabilities.length; i++) {
            if (probabilities[i] > maxProb) {
                maxProb = probabilities[i];
                position = i;
            }
        }
        return position;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
