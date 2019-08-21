package io.hustler.wallzy.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import io.hustler.wallzy.R;

public class HorizantalImagesAdapter extends RecyclerView.Adapter<HorizantalImagesAdapter.HorizontalViewHolder> {
    private Activity context;

    private ArrayList<String> ints = new ArrayList<>();

    public HorizantalImagesAdapter(Activity context) {
        this.context = context;
    }

    @NonNull
    @Override
    public HorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HorizontalViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_horizontal_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalViewHolder holder, int position) {
        Glide.with(context).load(ints.get(position)).into(holder.item);
    }

    @Override
    public int getItemCount() {
        return ints == null ? 0 : ints.size();
    }

    public void setData(ArrayList<String> ints) {
        this.ints = ints;
        notifyDataSetChanged();
    }

    class HorizontalViewHolder extends RecyclerView.ViewHolder {
        ImageView item;

        HorizontalViewHolder(@NonNull View itemView) {
            super(itemView);
           item= itemView.findViewById(R.id.list_item_background);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                item.setClipToOutline(true);
            }

        }
    }
}
