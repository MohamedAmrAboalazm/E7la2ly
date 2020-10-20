package com.example.android.e7la2ly.Client.ViewBarbersData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.e7la2ly.R;

import java.util.ArrayList;

public class BarbersAdapter extends RecyclerView.Adapter<BarbersAdapter.viewHolder> {

    ArrayList<BarberData> barberData;
    Context context;

    public BarbersAdapter(Context c, ArrayList<BarberData> barberData) {
        this.barberData = barberData;
        this.context = c;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.barber_list_item, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, final int pos) {
        final BarberData item = barberData.get(pos);
        viewHolder.salonName.setText(item.getSalonname());
        viewHolder.barberName.setText(item.getBarbername());
        Glide.with(context)
                .load(item.getBarberimagelink()).fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.refresh).apply(new RequestOptions().circleCrop())
                .error(R.drawable.default_profile_image)
                .into(viewHolder.barberImage);

    }

    @Override
    public int getItemCount() {
        if (barberData == null) return 0;
        return barberData.size();
    }





    public class viewHolder extends RecyclerView.ViewHolder {
        TextView salonName;
        TextView barberName;
        ImageView barberImage;


        public viewHolder(@NonNull View view) {
            super(view);
            salonName = view.findViewById(R.id.salon_name);
            barberName = view.findViewById(R.id.barber_name);
            barberImage=view.findViewById(R.id.barber_image);



        }

    }


}
