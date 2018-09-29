package com.example.shivamvk.mindfulsupplier;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class RecommendedFragment extends Fragment {

    private TextView tvRecommendedNoOrders;
    private ProgressBar pbRecommended;
    private RecyclerView rvRecommended;
    private Button btFilterRecomended;
    List<Order> LIST_OF_ORDERS;
    List<String> APPLIEDORDERS;

    private EditText etFilterFrom,etFilterTo;
    private CardView cvFilter;

    private int LOADING_POINT_REQUEST_CODE = 1;
    private int TRIP_DESTINATION_REQUEST_CODE = 2;

    private HashMap<String, String> filterMap;

    private EditText origin,destination,truck,material,loadingtime;

    private int NoOfFilters = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recommended, null, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Mindful Supplier");

        cvFilter = view.findViewById(R.id.cv_filter_fragment_recommended);
        etFilterFrom = view.findViewById(R.id.et_filter_from_fragment_recommended);
        etFilterTo = view.findViewById(R.id.et_filter_to_fragment_recommended);

        tvRecommendedNoOrders = view.findViewById(R.id.tv_recommended_no_order);
        pbRecommended = view.findViewById(R.id.pb_recommended);
        rvRecommended = view.findViewById(R.id.rv_recommended);
        btFilterRecomended = view.findViewById(R.id.bt_filter_recommended);

        rvRecommended.setHasFixedSize(true);
        rvRecommended.setLayoutManager(new LinearLayoutManager(getContext()));

        LIST_OF_ORDERS = new ArrayList<>();
        APPLIEDORDERS = new ArrayList<>();

      /*  filterMap = new HashMap<>();

        if (SharedPrefManager.getInstance(getContext()).getFilterOrigin() != null){
            filterMap.put("filterorigin", SharedPrefManager.getInstance(getContext()).getFilterOrigin());
            btFilterRecomended.setText("Filter(" + (NoOfFilters + 1) + ")");
            NoOfFilters++;
        }

        if (SharedPrefManager.getInstance(getContext()).getFilterDestination() != null){
            filterMap.put("filterdestination", SharedPrefManager.getInstance(getContext()).getFilterDestination());
            btFilterRecomended.setText("Filter(" + (NoOfFilters + 1) + ")");
            NoOfFilters++;
        }

        if (SharedPrefManager.getInstance(getContext()).getFilterTruck() != null){
            filterMap.put("filtertruck", SharedPrefManager.getInstance(getContext()).getFilterTruck());
            btFilterRecomended.setText("Filter(" + (NoOfFilters + 1) + ")");
            NoOfFilters++;
        }

        if (SharedPrefManager.getInstance(getContext()).getFilterMaterial() != null){
            filterMap.put("filtermaterial", SharedPrefManager.getInstance(getContext()).getFilterMaterial());
            btFilterRecomended.setText("Filter(" + (NoOfFilters + 1) + ")");
            NoOfFilters++;
        }

        if (SharedPrefManager.getInstance(getContext()).getFilterDate() != null){
            filterMap.put("filterdate", SharedPrefManager.getInstance(getContext()).getFilterDate());
            btFilterRecomended.setText("Filter(" + (NoOfFilters + 1) + ")");
            NoOfFilters++;
        }*/

        listAppliedOrders();
        Log.i("TAG", "onViewCreated: 11");

       /* if (filterMap.isEmpty()){
            loadOrders();
        } else {
            loadOrders(filterMap);
        }*/

        loadOrders();
        Log.i("TAG", "onViewCreated: 22");

        /*btFilterRecomended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View view1 = LayoutInflater.from(getContext()).inflate(R.layout.dialog_filter_recommended, null, false);
                builder.setView(view1);
                origin = view1.findViewById(R.id.et_place_order_loading_point);
                destination = view1.findViewById(R.id.et_place_order_trip_destination);
                truck = view1.findViewById(R.id.et_place_order_truck_type);
                material = view1.findViewById(R.id.et_place_order_material_type);
                loadingtime = view1.findViewById(R.id.et_place_order_loading_time);
                builder.setMessage("Select the filters that you want!");

                if (filterMap.get("filterorigin") != null){
                    origin.setText(filterMap.get("filterorigin"));
                }

                if (filterMap.get("filterdestination") != null){
                    destination.setText(filterMap.get("filterdestination"));
                }

                if (filterMap.get("filtertruck") != null){
                    truck.setText(filterMap.get("filtertruck"));
                }

                if (filterMap.get("filtermaterial") != null){
                    material.setText(filterMap.get("filtermaterial"));
                }

                if (filterMap.get("filterdate") != null){
                    material.setText(filterMap.get("filterdate"));
                }

                origin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                                    .setCountry("IN")
                                    .build();
                            Intent intent =
                                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                            .setFilter(typeFilter)
                                            .build(getActivity());
                            startActivityForResult(intent, LOADING_POINT_REQUEST_CODE);
                        } catch (GooglePlayServicesRepairableException e) {
                            // TODO: Handle the error.
                        } catch (GooglePlayServicesNotAvailableException e) {
                            // TODO: Handle the error.
                        }
                    }
                });

                destination.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                                    .setCountry("IN")
                                    .build();
                            Intent intent =
                                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                            .setFilter(typeFilter)
                                            .build(getActivity());
                            startActivityForResult(intent, TRIP_DESTINATION_REQUEST_CODE);
                        } catch (GooglePlayServicesRepairableException e) {
                            // TODO: Handle the error.
                        } catch (GooglePlayServicesNotAvailableException e) {
                            // TODO: Handle the error.
                        }
                    }
                });

                truck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                        View view2 = LayoutInflater.from(getContext()).inflate(R.layout.dialog_truck_type,null,false);

                        LinearLayout llOpen = view2.findViewById(R.id.ll_truck_type_open);
                        LinearLayout llContainer = view2.findViewById(R.id.ll_truck_type_container);
                        LinearLayout llTrailer = view2.findViewById(R.id.ll_truck_type_trailer);

                        builder1.setView(view2);

                        final AlertDialog dialog=builder1.show();
                        llOpen.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                truck.setText("Open");
                                dialog.dismiss();
                            }
                        });

                        llContainer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                truck.setText("Container");
                                dialog.dismiss();
                            }
                        });

                        llTrailer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                truck.setText("Trailer");
                                dialog.dismiss();
                            }
                        });
                    }
                });

                material.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                        View view2 = LayoutInflater.from(getContext()).inflate(R.layout.dialog_material_type, null, false);
                        LinearLayout ll1 = view2.findViewById(R.id.ll_material_type_1);
                        LinearLayout ll2 = view2.findViewById(R.id.ll_material_type_2);
                        LinearLayout ll3 = view2.findViewById(R.id.ll_material_type_3);
                        LinearLayout ll4 = view2.findViewById(R.id.ll_material_type_4);
                        LinearLayout ll5 = view2.findViewById(R.id.ll_material_type_5);
                        LinearLayout ll6 = view2.findViewById(R.id.ll_material_type_6);
                        LinearLayout ll7 = view2.findViewById(R.id.ll_material_type_7);
                        LinearLayout ll8 = view2.findViewById(R.id.ll_material_type_8);
                        LinearLayout ll9 = view2.findViewById(R.id.ll_material_type_9);
                        LinearLayout ll10 = view2.findViewById(R.id.ll_material_type_10);
                        LinearLayout ll11 = view2.findViewById(R.id.ll_material_type_11);
                        LinearLayout ll12 = view2.findViewById(R.id.ll_material_type_12);

                        builder1.setView(view2);
                        final AlertDialog dialog = builder1.show();

                        ll1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                material.setText(getString(R.string.material_type_1));
                                dialog.dismiss();
                            }
                        });

                        ll2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                material.setText(getString(R.string.material_type_2));
                                dialog.dismiss();
                            }
                        });

                        ll3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                material.setText(getString(R.string.material_type_3));
                                dialog.dismiss();
                            }
                        });

                        ll4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                material.setText(getString(R.string.material_type_4));
                                dialog.dismiss();
                            }
                        });

                        ll5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                material.setText(getString(R.string.material_type_5));
                                dialog.dismiss();
                            }
                        });

                        ll6.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                material.setText(getString(R.string.material_type_6));
                                dialog.dismiss();
                            }
                        });

                        ll7.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                material.setText(getString(R.string.material_type_7));
                                dialog.dismiss();
                            }
                        });

                        ll8.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                material.setText(getString(R.string.material_type_8));
                                dialog.dismiss();
                            }
                        });

                        ll9.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                material.setText(getString(R.string.material_type_9));
                                dialog.dismiss();
                            }
                        });

                        ll10.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                material.setText(getString(R.string.material_type_10));
                                dialog.dismiss();
                            }
                        });

                        ll11.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                material.setText(getString(R.string.material_type_11));
                                dialog.dismiss();
                            }
                        });

                        ll12.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                material.setText(getString(R.string.material_type_12));
                                dialog.dismiss();
                            }
                        });
                    }
                });

                final Calendar calendar = Calendar.getInstance();

                final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel(calendar);
                    }

                };

                loadingtime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new DatePickerDialog(getActivity(), date, calendar
                                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });

                builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!origin.getText().toString().equals("Filter by origin")){
                            filterMap.put("filterorigin", origin.getText().toString());
                            SharedPrefManager.getInstance(getContext()).putFilterOrigin(origin.getText().toString());
                        }
                        if (!destination.getText().toString().equals("Filter by destination")){
                            filterMap.put("filterdestination", destination.getText().toString());
                            SharedPrefManager.getInstance(getContext()).putFilterDestination(destination.getText().toString());
                        }
                        if(!truck.getText().toString().equals("Filter by truck")){
                            filterMap.put("filtertruck", truck.getText().toString());
                            SharedPrefManager.getInstance(getContext()).putFilterTruck(truck.getText().toString());
                        }
                        if (!material.getText().toString().equals("Filter by material")){
                            filterMap.put("filtermaterial", material.getText().toString());
                            SharedPrefManager.getInstance(getContext()).putFilterMaterial(material.getText().toString());
                        }
                        if (!loadingtime.getText().toString().equals("Filter by loading date")){
                            filterMap.put("filterdate", loadingtime.getText().toString());
                            SharedPrefManager.getInstance(getContext()).putFilterDate(loadingtime.getText().toString());
                        }

                        if (!filterMap.isEmpty()){
                            loadOrders(filterMap);
                        }
                        Fragment fragment=new RecommendedFragment();
                        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fl_main_activity,fragment);
                        fragmentTransaction.commit();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //do nothing
                    }
                });
                builder.setNeutralButton("Clear Filters", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPrefManager.getInstance(getContext()).clearFilterSharedPref();
                        Fragment fragment=new RecommendedFragment();
                        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fl_main_activity,fragment);
                        fragmentTransaction.commit();
                    }
                });
                AlertDialog dialog = builder.show();
            }
        });*/
    }

    private void listAppliedOrders() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("suppliers")
               // .child(generateHash(SharedPrefManager.getInstance(getContext()).getEmail()))
                .child(SharedPrefManager.getInstance(getContext()).getNumber())
                .child("appliedfor");

        Log.i("TAG", "onViewCreated: 33");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    APPLIEDORDERS.add(snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.i("TAG", "onViewCreated: 11 size" + APPLIEDORDERS.size());

    }

    private void loadOrders() {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LIST_OF_ORDERS.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    for(DataSnapshot snapshot1:snapshot.child("orders").getChildren()){
                        Order order = snapshot1.getValue(Order.class);
                        LIST_OF_ORDERS.add(order);

                    }
                }
                Log.i("TAG", "onViewCreated: 55" + LIST_OF_ORDERS.size());
                RecommendedAdapter adapter = new RecommendedAdapter(getContext(),LIST_OF_ORDERS,APPLIEDORDERS);
                rvRecommended.setAdapter(adapter);
                pbRecommended.setVisibility(View.GONE);

                cvFilter.setVisibility(View.VISIBLE);
                if(LIST_OF_ORDERS.isEmpty()){
                    cvFilter.setVisibility(View.GONE);
                    tvRecommendedNoOrders.setVisibility(View.VISIBLE);
                }

                /*btFilterRecomended.setVisibility(View.VISIBLE);
                if(LIST_OF_ORDERS.isEmpty()){
                    btFilterRecomended.setVisibility(View.GONE);
                    tvRecommendedNoOrders.setVisibility(View.VISIBLE);
                }*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadOrders(HashMap<String, String> filterMap) {
        final String origin = filterMap.get("filterorigin");
        final String destination = filterMap.get("filterdestination");
        final String truck = filterMap.get("filtertruck");
        final String material = filterMap.get("filtermaterial");
        final String date = filterMap.get("filterdate");
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LIST_OF_ORDERS.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    for(DataSnapshot snapshot1:snapshot.child("orders").getChildren()){
                        Order order = snapshot1.getValue(Order.class);

                        if (origin != null){
                            String Oorigin[] = order.getLoadingPoint().split(" ");
                            for (String s : Oorigin){
                                if (s.equals(origin) || s.equals(origin + ",")){
                                    LIST_OF_ORDERS.add(order);
                                }
                            }
                        }

                        if (destination != null){
                            String Odestination[] = order.getTripDestination().split(" ");
                            for (String s : Odestination){
                                //Toast.makeText(getContext(), destination, Toast.LENGTH_SHORT).show();
                                if (s.equals(destination) || s.equals(destination + ",")){
                                    if (!LIST_OF_ORDERS.contains(order)){
                                        LIST_OF_ORDERS.add(order);
                                    }
                                }
                            }
                        }

                        if(truck != null){
                            String Otruck[]=order.getTruckType().split(" ");
                            if(Otruck[0].equals(truck)){
                                //Toast.makeText(getContext(), Otruck[0], Toast.LENGTH_SHORT).show();
                                if (!LIST_OF_ORDERS.contains(order)){
                                    LIST_OF_ORDERS.add(order);
                                }
                            }
                        }

                        if (material != null){
                            if (material.equals(order.getMaterialType())){
                                if (!LIST_OF_ORDERS.contains(order)){
                                    LIST_OF_ORDERS.add(order);
                                }
                            }
                        }

                        if (date != null){
                            if (date.equals(order.getLoadingDate())){
                                if (!LIST_OF_ORDERS.contains(order)){
                                    LIST_OF_ORDERS.add(order);
                                }
                            }
                        }

                    }
                }

                RecommendedAdapter adapter = new RecommendedAdapter(getContext(),LIST_OF_ORDERS,APPLIEDORDERS);
                rvRecommended.setAdapter(adapter);
                pbRecommended.setVisibility(View.GONE);
               // btFilterRecomended.setVisibility(View.VISIBLE);
                if(LIST_OF_ORDERS.isEmpty()){
                    btFilterRecomended.setVisibility(View.GONE);
                    tvRecommendedNoOrders.setVisibility(View.VISIBLE);
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

    private void updateLabel(Calendar calendar) {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        loadingtime.setText(sdf.format(calendar.getTime()));
    }

   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==LOADING_POINT_REQUEST_CODE){
            if(resultCode==RESULT_OK){
                Place place = PlaceAutocomplete.getPlace(getActivity(),data);
                origin.setText(place.getName());
            } else if (resultCode==PlaceAutocomplete.RESULT_ERROR){
                Status status= PlaceAutocomplete.getStatus(getContext(),data);
                Toast.makeText(getContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode==TRIP_DESTINATION_REQUEST_CODE){
            if(resultCode==RESULT_OK){
                Place place = PlaceAutocomplete.getPlace(getActivity(),data);
                destination.setText(place.getName());
            } else if (resultCode==PlaceAutocomplete.RESULT_ERROR){
                Status status= PlaceAutocomplete.getStatus(getContext(),data);
                Toast.makeText(getContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        }

    }*/
}
