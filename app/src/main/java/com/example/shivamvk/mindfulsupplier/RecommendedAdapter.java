package com.example.shivamvk.mindfulsupplier;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RecommendedAdapter extends RecyclerView.Adapter<RecommendedAdapter.ViewHolder> {

    private Context context;
    List<Order> LIST_OF_ORDERS;
    List<String> APPLIEDORDERS;

    public RecommendedAdapter(Context context, List<Order> LIST_OF_ORDERS,List<String> APPLIEDORDERS) {
        this.context = context;
        this.LIST_OF_ORDERS = LIST_OF_ORDERS;
        this.APPLIEDORDERS = APPLIEDORDERS;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_order_item_layout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final Order order = LIST_OF_ORDERS.get(i);

        Log.i("TAG", "onBindViewHolder: "+LIST_OF_ORDERS.get(0));
        final String orderid = generateHash(order.getLoadingPoint(),
                order.getTripDestination(),
                order.getTruckType(),
                order.getMaterialType(),
                order.getLoadingDate(),
                order.getLoadingTime());

        String loadingPoint = order.getLoadingPoint();

        Log.i("TAG", "onViewCreated: 11 loading "+loadingPoint);
        String destinationPoint = order.getTripDestination();

        /*String trucktype = order.getTruckType();

        char chartruck = trucktype.charAt(0);

        switch (chartruck){
            case 'O':
                viewHolder.ivTruckType.setImageResource(R.drawable.open);
                break;
            case 'C':
                viewHolder.ivTruckType.setImageResource(R.drawable.container);
                break;
            case 'T':
                viewHolder.ivTruckType.setImageResource(R.drawable.trailer);
        }*/

      /*  if(loadingPoint.length() > 27){
            char loading[] = loadingPoint.toCharArray();
            loading[24] = '.';
            loading[25] = '.';
            loading[26] = '.';
            loadingPoint = String.valueOf(loading);
        }

        if (destinationPoint.length() >27){
            char destination[] = destinationPoint.toCharArray();
            destination[24] = '.';
            destination[25] = '.';
            destination[26] = '.';
            destinationPoint = String.valueOf(destination);
        }*/

        if(loadingPoint.length() > 34){
            char loading[] = loadingPoint.toCharArray();
            loading[34] = '.';
            loading[32] = '.';
            loading[33] = '.';
            loadingPoint = String.valueOf(loading);
        }

        if (destinationPoint.length() > 34){
            char destination[] = destinationPoint.toCharArray();
            destination[34] = '.';
            destination[32] = '.';
            destination[33] = '.';
            destinationPoint = String.valueOf(destination);
        }

        viewHolder.tvLoadingPoint.setText(loadingPoint);
      //  viewHolder.tvOrderId.setText("Order Id: " + orderid);
        viewHolder.tvTripDestination.setText(destinationPoint);
       // viewHolder.tvTruckType.setText(order.getTruckType());
       // viewHolder.tvMaterialType.setText(order.getMaterialType());
        viewHolder.tvLoadingDate.setText(order.getLoadingDate());
        viewHolder.tvLoadingTime.setText(order.getLoadingTime());
       // viewHolder.tvRemarks.setText(order.getRemarks());

        String supplier = null;

        if (order.getCompleted().equals("Yes")){
            supplier = order.getSupplier();
        }

        if (APPLIEDORDERS.contains(orderid) && order.getCompleted().equals("No")){
            viewHolder.btApply.setText("Applied");
            viewHolder.btApply.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            viewHolder.btApply.setBackgroundColor(context.getResources().getColor(R.color.grey));
            viewHolder.btApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("You have already applied for this order.");
                    builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //do nothing
                        }
                    });
                    builder.show();
                }
            });
        } else if (APPLIEDORDERS.contains(orderid) && order.getCompleted().equals("Yes") && supplier.equals(SharedPrefManager.getInstance(context).getNumber())){
            viewHolder.btApply.setText("Accepted");
            viewHolder.btApply.setBackgroundColor(context.getResources().getColor(R.color.darkgreen));
        } else if (APPLIEDORDERS.contains(orderid) && order.getCompleted().equals("Yes") && !supplier.equals(SharedPrefManager.getInstance(context).getNumber())){
            viewHolder.btApply.setText("Rejected");
            viewHolder.btApply.setBackgroundColor(context.getResources().getColor(R.color.red));
        } else if (order.getCompleted().equals("Yes") && !APPLIEDORDERS.contains(orderid)){
            viewHolder.linearLayout.setVisibility(View.GONE);
            ViewGroup.MarginLayoutParams layoutParams =
                    (ViewGroup.MarginLayoutParams) viewHolder.cardView.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, 0);
            viewHolder.cardView.requestLayout();
        } else {
            viewHolder.btApply.setText("APPLY");
            viewHolder.btApply.setTextColor(context.getResources().getColor(android.R.color.white));
            viewHolder.btApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Are you sure, you want to apply for this order?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final ProgressDialog dialog = new ProgressDialog(context);
                                dialog.setMessage("Applying...");
                                dialog.setCancelable(false);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.show();
                                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("suppliers")
                                        .child(SharedPrefManager.getInstance(context).getNumber())
                                        .child("appliedfor")
                                        .child(generateHash(order.getLoadingPoint(),
                                                order.getTripDestination(),
                                                order.getTruckType(),
                                                order.getMaterialType(),
                                                order.getLoadingDate(),
                                                order.getLoadingTime()));

                                reference.setValue(order)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    DatabaseReference reference1 = FirebaseDatabase.getInstance()
                                                            .getReference("users")
                                                            .child(order.getOrderby())
                                                            .child("orders")
                                                            .child(generateHash(order.getLoadingPoint(),
                                                                    order.getTripDestination(),
                                                                    order.getTruckType(),
                                                                    order.getMaterialType(),
                                                                    order.getLoadingDate(),
                                                                    order.getLoadingTime()))
                                                            .child("appliedby")
                                                            .child(generateHash(SharedPrefManager.getInstance(context).getEmail()));
                                                    reference1.setValue(SharedPrefManager.getInstance(context).getNumber());
                                                    dialog.dismiss();
                                                } else {
                                                    dialog.dismiss();
                                                    Toast.makeText(context, "Please check your internet connection!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //do nothing
                            }
                        });
                        AlertDialog dialog = builder.show();



                }
            });
        }



    }

    @Override
    public int getItemCount() {
        return LIST_OF_ORDERS.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        private TextView tvLoadingPoint,tvTripDestination,tvLoadingDate,tvLoadingTime;
               // tvTruckType,tvMaterialType,tvRemarks,tvOrderId;
        private Button btApply;
        private CardView cardView;
        private LinearLayout linearLayout;
        private ImageView ivTruckType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //ivTruckType = itemView.findViewById(R.id.iv_order_item_truck_type);

            tvLoadingPoint = itemView.findViewById(R.id.tv_order_item_loading_point);
            tvTripDestination = itemView.findViewById(R.id.tv_order_item_destination_point);
            //tvTruckType = itemView.findViewById(R.id.tv_order_item_truck_type);
           // tvMaterialType = itemView.findViewById(R.id.tv_order_item_material_type);
            tvLoadingDate = itemView.findViewById(R.id.tv_order_item_loading_date);
            tvLoadingTime = itemView.findViewById(R.id.tv_order_item_loading_time);
           // tvRemarks = itemView.findViewById(R.id.tv_order_item_remarks);
            //tvOrderId = itemView.findViewById(R.id.tv_order_item_order_id);
              cardView = itemView.findViewById(R.id.cv_order_item_layout);
             linearLayout = itemView.findViewById(R.id.ll_order_item_layout);

            btApply = itemView.findViewById(R.id.bt_order_item_status);
        }
    }

   /* private String generateHash(String s, String s1, String s2, String s3, String s4) {
        int hash = 21;
        String main = s + s1 + s2 + s3 + s4;
        for (int i = 0; i < main.length(); i++) {
            hash = hash*31 + main.charAt(i);
        }
        if (hash < 0){
            hash = hash * -1;
        }
        return hash + "";
    }*/

    private String generateHash(String s, String s1, String s2, String s3, String s4, String s5) {
        int hash = 21;
        String main = s + s1 + s2 + s3 + s4 + s5;
        for (int i = 0; i < main.length(); i++) {
            hash = hash*31 + main.charAt(i);
        }
        if (hash < 0){
            hash = hash * -1;
        }
        return hash + "";
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
