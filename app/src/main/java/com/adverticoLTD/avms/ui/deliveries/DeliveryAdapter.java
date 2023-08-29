package com.adverticoLTD.avms.ui.deliveries;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adverticoLTD.avms.R;
import com.adverticoLTD.avms.data.delivery.DeliveryListDataModel;
import com.adverticoLTD.avms.interfaces.OnItemClick;

import java.util.List;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.DashboardHolder> {


    List<DeliveryListDataModel> arrTLTVisitorList;
    Activity activity;
    OnItemClick onItemClick;

    public DeliveryAdapter(Activity activity, List<DeliveryListDataModel> arrTLTVisitorList, OnItemClick onItemClick) {
        this.activity = activity;
        this.arrTLTVisitorList = arrTLTVisitorList;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public DashboardHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View v = LayoutInflater.from(activity).inflate(R.layout.delivery_row, parent, false);
        return new DashboardHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardHolder fireEvacuationHolder, int position) {

        final DeliveryListDataModel arrItem = arrTLTVisitorList.get(position);

        fireEvacuationHolder.txtFirstName.setText(String.format("%s - %s", arrItem.getCompany_id(), arrItem.getStaff_id()));
        fireEvacuationHolder.txtDateTime.setText(arrItem.getCreated_datetime());
        fireEvacuationHolder.txtConfirmText.setText(arrItem.getStatus().equals("1") ? "Sign" : "Signed");
        fireEvacuationHolder.loutResultView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arrItem.getStatus().equals("1")){
                    onItemClick.OnRecordClickListener(arrItem.getId(),position);
                }

            }
        });

    }


    @Override
    public int getItemCount() {
        return arrTLTVisitorList.size();
    }


    static class DashboardHolder extends RecyclerView.ViewHolder {

        LinearLayout loutResultView;
        TextView txtFirstName;
        TextView txtDateTime;
        TextView txtConfirmText;

        DashboardHolder(@NonNull View itemView) {
            super(itemView);
            this.loutResultView = itemView.findViewById(R.id.loutResultView);
            this.txtFirstName = itemView.findViewById(R.id.txtFirstName);
            this.txtDateTime = itemView.findViewById(R.id.txtDateTime);
            this.txtConfirmText = itemView.findViewById(R.id.txtConfirmText);

        }
    }
}
