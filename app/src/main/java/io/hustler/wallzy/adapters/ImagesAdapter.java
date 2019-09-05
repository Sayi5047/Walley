package io.hustler.wallzy.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import io.hustler.wallzy.R;
import io.hustler.wallzy.model.base.ResponseImageClass;
import io.hustler.wallzy.model.wallzy.response.ResGetCategoryImages;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImageViewHolder> {
    Activity activity;
    OnItemClcikListener onItemClcikListener;
    ArrayList<ResponseImageClass> imagesList;

    public ImagesAdapter(Activity activity, OnItemClcikListener onItemClcikListener, ArrayList<ResponseImageClass> imagesList) {
        this.activity = activity;
        this.onItemClcikListener = onItemClcikListener;
        this.imagesList = imagesList;
    }

    public interface OnItemClcikListener {
        void onItemClick(int position);
    }

    public void AddData(ArrayList<ResponseImageClass> resGetCategoryImages) {
        for (ResponseImageClass resGetCategoryImages1 : resGetCategoryImages) {
            addOne(resGetCategoryImages1);
        }
    }

    public void addOne(ResponseImageClass resGetCategoryImages) {
        imagesList.add(resGetCategoryImages);
        notifyItemInserted(imagesList.size() - 1);
    }

    public void remove(ResGetCategoryImages resGetCategoryImages) {
        int position = imagesList.indexOf(resGetCategoryImages);
        if (position > -1) {
            imagesList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {

    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageViewHolder(activity.getLayoutInflater().inflate(R.layout.imageview_grid_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Glide.with(activity).load(imagesList.get(position).getThumbUrl()).centerCrop().into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClcikListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imagesList == null ? 0 : imagesList.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
;