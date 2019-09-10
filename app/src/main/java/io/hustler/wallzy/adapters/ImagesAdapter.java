package io.hustler.wallzy.adapters;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.hustler.wallzy.R;
import io.hustler.wallzy.model.base.ResponseImageClass;
import io.hustler.wallzy.model.wallzy.response.ResGetCategoryImages;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImageViewHolder> {
    private Activity activity;
    private OnItemClcikListener onItemClcikListener;
    private ArrayList<ResponseImageClass> imagesList;

    public ImagesAdapter(Activity activity, OnItemClcikListener onItemClcikListener, ArrayList<ResponseImageClass> imagesList) {
        this.activity = activity;
        this.onItemClcikListener = onItemClcikListener;
        this.imagesList = imagesList;
    }

    public interface OnItemClcikListener {
        void onItemClick(ResponseImageClass position);
    }

    public void AddData(ArrayList<ResponseImageClass> resGetCategoryImages) {
        for (ResponseImageClass resGetCategoryImages1 : resGetCategoryImages) {
            addOne(resGetCategoryImages1);
        }
    }

    public void AddAllData(ArrayList<ResponseImageClass> resGetCategoryImages) {
        int lastPosition = imagesList.size();
        imagesList.addAll(resGetCategoryImages);
        notifyItemRangeChanged(lastPosition - 1, resGetCategoryImages.size());
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

    @Override
    public long getItemId(int position) {
        return imagesList.get(position).getId();

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
//        Picasso.get().load(imagesList.get(position).getUrl() + "?tr=w-400,h-400").into(holder.imageView);
//        ););+ "?tr=w-200,h-200"
        Glide.with(activity).load(imagesList.get(position).getUrl() ).fitCenter().into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClcikListener.onItemClick(imagesList.get(position));
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