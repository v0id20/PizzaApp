package com.github.v0id20.pizzaapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class StoreFragment extends Fragment {
    GoogleMap map;
    OnMapListItemClickListener listener;

    ArrayList<Store> storeAddressList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.store_fragment,container,false);

        listener = new OnMapListItemClickListener() {
            @Override
            public void onMapListItemClick(int position) {
                LatLng c = storeAddressList.get(position).getCoordrinates();
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(c,12));
               }
        };
        RecyclerView storeRecyclerView = v.findViewById(R.id.store_recycler);
        StoreAdapter storeAdapter = new StoreAdapter(storeAddressList, listener);
        storeRecyclerView.setAdapter(storeAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        storeRecyclerView.setLayoutManager(llm);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.maps);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                LatLng centerCoordinates = new LatLng(51.492174, -0.131981);
                for (Store s : storeAddressList){
                    LatLng coordinates = s.getCoordrinates();
                    map.addMarker(new MarkerOptions().position(coordinates));
                }

                map.moveCamera(CameraUpdateFactory.newLatLngZoom(centerCoordinates,10));

            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storeAddressList = new ArrayList<>();
        storeAddressList.add(new Store(new LatLng(51.543981, -0.224509), "79 Staverton Rd, London NW7 53A" ));
        storeAddressList.add(new Store(new LatLng(51.479316, -0.216025), "28 Kingwood Rd, Fulham, London SW6 62N" ));
        storeAddressList.add(new Store(new LatLng(51.395961, -0.104767), "112 Parchmore Court st Pauls Road, Thornthon Heath CR7 2AB" ));
        storeAddressList.add(new Store(new LatLng(51.508797, -0.130533), "17 Whitcomb St, West End, London WC2H 8HS" ));

    }


}
