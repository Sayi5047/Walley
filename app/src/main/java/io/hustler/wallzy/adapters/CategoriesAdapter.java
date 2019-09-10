package io.hustler.wallzy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.hustler.wallzy.R;
import io.hustler.wallzy.model.wallzy.response.BaseCategoryClass;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.VerticalViewHolder> {

    private ArrayList<BaseCategoryClass> categoryArrayList = new ArrayList<>();
    private Context activity;
    private OnChildClickListener onChildClickListener;

    public CategoriesAdapter(ArrayList<BaseCategoryClass> categoryArrayList, Context activity, OnChildClickListener onChildClickListener) {
        this.categoryArrayList = categoryArrayList;
        this.activity = activity;
        this.onChildClickListener = onChildClickListener;
    }


    public interface OnChildClickListener {
        void onCLick(BaseCategoryClass category, ImageView imageView);
    }

    @NonNull
    @Override
    public VerticalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VerticalViewHolder
                (LayoutInflater.from(activity).inflate(R.layout.rv_category_vertical_item, parent, false));
    }

    public void setData(ArrayList<BaseCategoryClass> newData) {
        categoryArrayList.clear();
        notifyDataSetChanged();
        for (BaseCategoryClass newCategory : newData) {
            this.categoryArrayList.add(newCategory);
            notifyDataSetChanged();
        }
    }

    public void clearAdapter() {
        if (null != categoryArrayList) {
            categoryArrayList.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull VerticalViewHolder holder, int position) {
        BaseCategoryClass category = categoryArrayList.get(position);
        holder.categoryName.setText(category.getName());
        Glide.with(activity).load(category.getCover()+"?tr=w-250,h-200").centerCrop().into(holder.categoryImage);
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
