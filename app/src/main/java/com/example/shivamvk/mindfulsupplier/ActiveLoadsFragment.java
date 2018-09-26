package com.example.shivamvk.mindfulsupplier;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActiveLoadsFragment extends Fragment {

    private TextView tvActiveLoadsNoOrder;
    private ProgressBar pbActiveLoads;
    private RecyclerView rvActiveLoads;

    List<Order> APPLIED_ORDERS;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_active_loads,null,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Active Loads");

        tvActiveLoadsNoOrder = view.findViewById(R.id.tv_active_loads_no_order);
        pbActiveLoads = view.findViewById(R.id.pb_active_loads);
        rvActiveLoads = view.findViewById(R.id.rv_active_loads);

        rvActiveLoads.setLayoutManager(new LinearLayoutManager(getContext()));
        rvActiveLoads.setHasFixedSize(true);

        APPLIED_ORDERS = new ArrayList<>();

        loadActiveLoads();

    }

    private void loadActiveLoads() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("suppliers")
                .child(generateHash(SharedPrefManager.getInstance(getContext()).getEmail()))
                .child("appliedfor");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                APPLIED_ORDERS.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Order order = snapshot.getValue(Order.class);
                    APPLIED_ORDERS.add(order);
                }
                ActiveLoadsAdapter adapter = new ActiveLoadsAdapter(APPLIED_ORDERS, getContext());
                rvActiveLoads.setAdapter(adapter);
                pbActiveLoads.setVisibility(View.GONE);
                if (APPLIED_ORDERS.isEmpty()){
                    tvActiveLoadsNoOrder.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private String generateHash(String s) {
        int hash = 21;
        for (int i = 0; i < s.length(); i++) {
            hash = hash*31 + s.charAt(i);
        }
        if (hash < 0){
            hash = hash * -1;
        }
        return hash + "";
    }
}
