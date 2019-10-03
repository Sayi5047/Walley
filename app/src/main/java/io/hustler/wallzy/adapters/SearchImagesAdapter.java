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
import io.hustler.wallzy.model.wallzy.response.ResGetCategoryImages;
import io.hustler.wallzy.model.wallzy.response.ResImageSearch;

public class SearchImagesAdapter extends RecyclerView.Adapter<SearchImagesAdapter.ImageViewHolder> {
    private Activity activity;
    private SearchImagesAdapter.OnItemClcikListener onItemClcikListener;
    private ArrayList<ResImageSearch.TagImage> imagesList;

    public SearchImagesAdapter(Activity activity, SearchImagesAdapter.OnItemClcikListener onItemClcikListener, ArrayList<ResImageSearch.TagImage> imagesList) {
        this.activity = activity;
        this.onItemClcikListener = onItemClcikListener;
        this.imagesList = imagesList;
    }

    public interface OnItemClcikListener {
        void onItemClick(ResImageSearch.TagImage position);
    }

    public void AddData(ArrayList<ResImageSearch.TagImage> resGetCategoryImages) {
        for (ResImageSearch.TagImage resGetCategoryImages1 : resGetCategoryImages) {
            addOne(resGetCategoryImages1);
        }
    }

    public void AddAllData(ArrayList<ResImageSearch.TagImage> resGetCategoryImages) {
        int lastPosition = imagesList.size();
        imagesList.addAll(resGetCategoryImages);
        notifyItemRangeChanged(lastPosition - 1, resGetCategoryImages.size());
    }

    public void addOne(ResImageSearch.TagImage resGetCategoryImages) {
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
    public SearchImagesAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchImagesAdapter.ImageViewHolder(activity.getLayoutInflater().inflate(R.layout.imageview_grid_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchImagesAdapter.ImageViewHolder holder, int position) {
        Glide.with(activity).load(imagesList.get(position).getThumbUrl()).fitCenter().into(holder.imageView);
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