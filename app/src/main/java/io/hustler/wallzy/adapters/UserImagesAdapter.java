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
import io.hustler.wallzy.model.wallzy.response.ResponseUserImageClass;

public class UserImagesAdapter extends RecyclerView.Adapter<UserImagesAdapter.ImageViewHolder> {
    private Activity activity;
    private UserImagesAdapter.OnItemClcikListener onItemClcikListener;
    private ArrayList<ResponseUserImageClass> imagesList;

    public UserImagesAdapter(Activity activity, UserImagesAdapter.OnItemClcikListener onItemClcikListener, ArrayList<ResponseUserImageClass> imagesList) {
        this.activity = activity;
        this.onItemClcikListener = onItemClcikListener;
        this.imagesList = imagesList;
    }

    public interface OnItemClcikListener {
        void onItemClick(ResponseUserImageClass position);
    }

    public void AddData(ArrayList<ResponseUserImageClass> resGetCategoryImages) {
        for (ResponseUserImageClass resGetCategoryImages1 : resGetCategoryImages) {
            addOne(resGetCategoryImages1);
        }
    }

    public void addAllData(ArrayList<ResponseUserImageClass> resGetCategoryImages) {
        int lastPosition = imagesList.size();
        imagesList.addAll(resGetCategoryImages);
        notifyItemRangeChanged(lastPosition>0?lastPosition - 1:lastPosition, resGetCategoryImages.size());
    }

    public void addOne(ResponseUserImageClass resGetCategoryImages) {
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

    public void removeAll() {
        if (null != imagesList) {
            if (0 < imagesList.size()) {
                int lastPosition = imagesList.size();
                imagesList.clear();
                notifyItemRangeRemoved(0, lastPosition);
            }
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
    public UserImagesAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserImagesAdapter.ImageViewHolder(activity.getLayoutInflater().inflate(R.layout.imageview_grid_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserImagesAdapter.ImageViewHolder holder, int position) {
//        Picasso.get().load(imagesList.get(position).getUrl() + "?tr=w-400,h-400").into(holder.imageView);
//        ););+ "?tr=w-200,h-200"
        Glide.with(activity).load(imagesList.get(position).getUrl()).fitCenter().into(holder.imageView);
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