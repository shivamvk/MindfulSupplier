package com.example.shivamvk.mindfulsupplier;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ActiveLoadsAdapter extends RecyclerView.Adapter<ActiveLoadsAdapter.ViewHolder> {

    private List<Order> APPLIED_ORDERS;
    private Context context;

    private static final int POD_FRONT = 1;
    private static final int POD_BACK = 2;
    private static final int POD_OTHER_DOCS = 3;

    public ActiveLoadsAdapter(List<Order> APPLIED_ORDERS, Context context) {
        this.APPLIED_ORDERS = APPLIED_ORDERS;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_order_item_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final Order order = APPLIED_ORDERS.get(i);
/*
        final String orderid = generateHash(
                order.getLoadingPoint(),
                order.getTripDestination(),
                order.getTruckType(),
                order.getMaterialType(),
                order.getLoadingDate()
        );*/

        final String orderid = generateHash(order.getLoadingPoint(),
                order.getTripDestination(),
                order.getTruckType(),
                order.getMaterialType(),
                order.getLoadingDate(),
                order.getLoadingTime());

        String loadingPoint = order.getLoadingPoint();
        String destinationPoint = order.getTripDestination();

       /* String trucktype = order.getTruckType();

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
        }
*/
        /*if(loadingPoint.length() > 27){
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

       // viewHolder.tvOrderId.setText("Order Id: " + orderid);
        viewHolder.tvLoadingPoint.setText(loadingPoint);
        viewHolder.tvTripDestination.setText(destinationPoint);
        viewHolder.tvLoadingDate.setText(order.getLoadingDate());
        viewHolder.tvLoadingTime.setText(order.getLoadingTime());
       // viewHolder.tvTruckType.setText(order.getTruckType());
        //viewHolder.tvMaterialType.setText(order.getMaterialType());
        //viewHolder.tvRemarks.setText(order.getRemarks());

        if (order.getCompleted().equals("No")){
            viewHolder.btApply.setText("Pending");
            viewHolder.btApply.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
           // viewHolder.btApply.setTextColor(context.getResources().getColor(R.color.grey));
        } else if (order.getCompleted().equals("Yes")){
            viewHolder.btApply.setText("Rejected");
            viewHolder.btApply.setBackgroundColor(context.getResources().getColor(R.color.red));
          //  viewHolder.btApply.setTextColor(context.getResources().getColor(R.color.grey));
        } else if (order.getCompleted().equals("Accepted")){
            viewHolder.btApply.setText("Accepted");
            viewHolder.btApply.setBackgroundColor(context.getResources().getColor(R.color.darkgreen));
          //  viewHolder.btApply.setTextColor(context.getResources().getColor(R.color.grey));

           /* viewHolder.btPOD.setVisibility(View.VISIBLE);
            viewHolder.btPOD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, UploadPOD.class);
                    intent.putExtra("orderid", orderid);
                    context.startActivity(intent);
                }
            });*/
        }
        viewHolder.btPOD.setVisibility(View.VISIBLE);
        viewHolder.btPOD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show();
               /* AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view1 = LayoutInflater.from(context).inflate(R.layout.dialog_upload_pod_layout,null,false);
                builder.setView(view1);

                RelativeLayout rlFront,rlback,rlOtherDocs;
                rlFront = view1.findViewById(R.id.rl_upload_pod_front);
                rlback = view1.findViewById(R.id.rl_upload_pod_back);
                rlOtherDocs = view1.findViewById(R.id.rl_upload_pod_other_docs);

                ImageView ivFront = view1.findViewById(R.id.iv_upload_pod_front);
                ImageView ivBack = view1.findViewById(R.id.iv_upload_pod_back);
                ImageView ivOtherDocs = view1.findViewById(R.id.iv_upload_pod_other_docs);

                rlFront.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       // Toast.makeText(context, "hello11", Toast.LENGTH_SHORT).show();
                        context.startActivity(new Intent(context,UploadPOD.class));

                    }
                });

                rlback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, "hello222", Toast.LENGTH_SHORT).show();
                    }
                });

                rlOtherDocs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, "hello333333", Toast.LENGTH_SHORT).show();
                    }
                });

               // builder.setMessage("Proof Of Delivery");

                AlertDialog alertDialog;
                alertDialog = builder.create();
                alertDialog.show();
                alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                */
                Intent intent = new Intent(context, UploadPOD.class);
                intent.putExtra("orderid", orderid);
                context.startActivity(intent);
            }
        });
    }

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


    @Override
    public int getItemCount() {
        return APPLIED_ORDERS.size();
       // return 2;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvLoadingPoint,tvTripDestination,tvLoadingDate,tvLoadingTime;
                //,tvTruckType,tvMaterialType,tvRemarks,tvOrderId;
        private Button btApply,btPOD;
        private CardView cardView;

        private ImageView ivTruckType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

           // ivTruckType = itemView.findViewById(R.id.iv_active_order_item_truck_type);

           // tvOrderId = itemView.findViewById(R.id.tv_active_order_item_order_id);
            tvLoadingPoint = itemView.findViewById(R.id.tv_order_item_loading_point);
            tvTripDestination = itemView.findViewById(R.id.tv_order_item_destination_point);
           // tvTruckType = itemView.findViewById(R.id.tv_active_order_item_truck_type);
           // tvMaterialType = itemView.findViewById(R.id.tv_active_order_item_material_type);
            tvLoadingDate = itemView.findViewById(R.id.tv_order_item_loading_date);
            tvLoadingTime = itemView.findViewById(R.id.tv_order_item_loading_time);
          //  tvRemarks = itemView.findViewById(R.id.tv_active_order_item_remarks);

            btApply = itemView.findViewById(R.id.bt_order_item_status);

            cardView = itemView.findViewById(R.id.cv_order_item_layout);

            btPOD = itemView.findViewById(R.id.bt_order_item_upload_pod);

        }
    }


}
