package io.hustler.wallzy.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import io.hustler.wallzy.R;
import io.hustler.wallzy.Room.Domains.CategoryTable;

public class VerticalImagesAdapter extends RecyclerView.Adapter<VerticalImagesAdapter.VerticalViewHolder> {

    private ArrayList<CategoryTable> categoryArrayList = new ArrayList<>();
    private Activity activity;
    private OnChildClickListener onChildClickListener;

    public VerticalImagesAdapter(ArrayList<CategoryTable> categoryArrayList, Activity activity, OnChildClickListener onChildClickListener) {
        this.categoryArrayList = categoryArrayList;
        this.activity = activity;
        this.onChildClickListener = onChildClickListener;
    }


    public interface OnChildClickListener {
        void onCLick(CategoryTable category, ImageView imageView);
    }

    @NonNull
    @Override
    public VerticalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VerticalViewHolder
                (LayoutInflater.from(activity).inflate(R.layout.rv_category_vertical_item, parent, false));
    }

    public void setData(ArrayList<CategoryTable> newData) {
        for (CategoryTable newCategory : newData) {
            if (!this.categoryArrayList.contains(newCategory)) {
                this.categoryArrayList.add(newCategory);
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull VerticalViewHolder holder, int position) {
        CategoryTable category = categoryArrayList.get(position);
        holder.categoryName.setText(category.getCollectionname());
        Glide.with(activity).load(category.getCoverImage()).centerCrop().into(holder.categoryImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onChildClickListener != null) {
                    onChildClickListener.onCLick(category, holder.categoryImage);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryArrayList == null ? 0 : categoryArrayList.size();
    }

    class VerticalViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryImage;
        TextView categoryName;

        VerticalViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.cat_image);
            categoryName = itemView.findViewById(R.id.cat_name);
        }
    }
}
