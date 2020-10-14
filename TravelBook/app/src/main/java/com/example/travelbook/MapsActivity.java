package com.example.travelbook;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap; // Google Map Instance
    LocationManager locationManager; // Lokasyon Yöneticimiz
    LocationListener locationListener; // Lokasyon Dinleyicimiz

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);  // Kullanıcı lokasyonunu almak için kullanıyoruz
        locationListener = new LocationListener() {
            // Location Listener Location Manager'a bağlı olarak kullanıcının konumunun değişimini dinler
            // Bu sayede anlık değişimi yansıtmak için kullanırız
            @Override
            public void onLocationChanged(@NonNull Location location) {
                // Lokasyon değiştikçe location nesnesi güncellenecektir
                mMap.clear(); // Haritanın üzerindeki fazlalık Marker nesnelerini temizliyoruz

                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));

                // Adres almak için Google map sağlayıcı olarak Geocoder kullanırız
                // İlk verdiğimiz değer context, o yüzden getApplicationContext kullandık
                // İkinci değer ise Locale dediğimiz bir yöreye, dile ait adres sunmak o sebeple kullanıcının telefonunda kullandığı varsayılan diline göre sunuyoruz
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    // latitude, longitude ve kaç adet sonuç almak istediğimizi belirtiyoruz
                    // Hata olması durumuna karşılık try-catch blokları içerisinde yazıyoruz
                    // Bize adres tipinde bir liste döneceği için Address tipinde bir Liste tanımlayarak dönen verileri onda saklıyoruz
                    List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (addressList != null && addressList.size() > 0) {
                        System.out.println("adres" + addressList.get(0));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        /*
            * ContextCompat eski versiyonlara olan uyum kontrolü
            * checkSelfPermission izinleri kontrol
            * Context aktivitemiz
            * Kontrol edeceğimiz izin Manifes/android altından geliyor
        */
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // İzin isteği
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            // İzin verildiyse

            /*
                * Öncelikler Provider yani konumu nereden almak istediğimizi söylüyoruz
                * İkinci olarak ne kadar zaman aralıklarıyla kontrol edileceğini milisaniye tipinde veriyoruz
                * Üçünü değer kaç metre mesafe ile kontrol sağlanacağı
                * Son değer de bizim listener yani dinleyicimiz
            */
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);

            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            // Lokasyon verememe olasılığı var

            if (lastLocation != null) {
                LatLng userLastLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(userLastLocation).title("Your Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLastLocation, 15));
            }
        }

        // Marker ekleme kodları
        //LatLng ayasofya = new LatLng(41.008583, 28.977981);
        //mMap.addMarker(new MarkerOptions().position(ayasofya).title("Marker in Ayasofya"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ayasofya, 17));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (grantResults.length > 0 && requestCode == 1) {
            // İçerik geldi mi && yukarıda verdiğimiz requestCode 1 di buradaki kod da 1 mi kontrolü

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // İzin verildi mi
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}