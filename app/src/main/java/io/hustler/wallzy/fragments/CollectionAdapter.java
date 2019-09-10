package io.hustler.wallzy.fragments;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import io.hustler.wallzy.R;
import io.hustler.wallzy.model.base.ResCollectionClass;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder> {
    Context mContext;
    ArrayList<ResCollectionClass> collectionsList;
    OnItemClickListener onItemClickListener;

    public CollectionAdapter(Context mContext, ArrayList<ResCollectionClass> collectionsList, OnItemClickListener onItemClickListener) {
        this.mContext = mContext;
        this.collectionsList = collectionsList;
        this.onItemClickListener = onItemClickListener;
    }

    interface OnItemClickListener {
        void onClick(ResCollectionClass resCollectionClass, ImageView imageView);
    }

    public void addCollection(ResCollectionClass resCollectionClass) {
        if (null == collectionsList) {
            collectionsList = new ArrayList<>();
        }
        collectionsList.add(resCollectionClass);
        notifyItemInserted(collectionsList.size() - 1);
    }

    public void addAll(ArrayList<ResCollectionClass> resCollectionClasses) {
        if (null == collectionsList) {
            collectionsList = new ArrayList<>();
        }
        int lastSize = collectionsList.size();
        collectionsList.addAll(resCollectionClasses);
        notifyItemRangeInserted(lastSize - 1, resCollectionClasses.size());
    }

    public void clearAdapter() {
        if (null != collectionsList) {
            int size = collectionsList.size();
            collectionsList.clear();
            notifyItemRangeRemoved(0, size);
        }

    }

    @NonNull
    @Override
    public CollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_collection_item_layout
                , parent, false);
        return new CollectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionViewHolder holder, int position) {
        ResCollectionClass resCollectionClass = collectionsList.get(position);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.cover1.setClipToOutline(true);
            holder.cover2.setClipToOutline(true);
            holder.cover3.setClipToOutline(true);


        }

//        + "?tr=w-82,h-82"
        if (null != resCollectionClass.getCovers().get(0)) {
            Glide.with(mContext).load(resCollectionClass.getCovers().get(0) ).centerCrop().into(holder.cover1);


        }
        if (null != resCollectionClass.getCovers().get(1)) {
            Glide.with(mContext).load(resCollectionClass.getCovers().get(1) ).centerCrop().into(holder.cover2);

        }
        if (null != resCollectionClass.getCovers().get(2)) {
            Glide.with(mContext).load(resCollectionClass.getCovers().get(2) ).centerCrop().into(holder.cover3);

        }

        holder.CollectionName.setText(resCollectionClass.getName());
        String collectionData = "";
        if (resCollectionClass.getArtistName() != null) {
            collectionData = "Artist curated ";
        }
        collectionData = collectionData + (resCollectionClass.getImageCount() + " Images");
        holder.CollectionCount.setText(collectionData);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != onItemClickListener) {
                    onItemClickListener.onClick(resCollectionClass, holder.cover1);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == collectionsList ? 0 : collectionsList.size();
    }

    public class CollectionViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout rootView;
        private LinearLayout coverLayout;
        private ImageView cover2;
        private ImageView cover1;
        private ImageView cover3;
        private View pichkooView;
        private TextView CollectionName;
        private TextView CollectionCount;

        public CollectionViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.root);
            coverLayout = itemView.findViewById(R.id.cover_layout);
            cover2 = itemView.findViewById(R.id.cover2);
            cover1 = itemView.findViewById(R.id.cover1);
            cover3 = itemView.findViewById(R.id.cover3);
            pichkooView = itemView.findViewById(R.id.pichkoo_view);
            CollectionName = itemView.findViewById(R.id.Collection_name);
            CollectionCount = itemView.findViewById(R.id.Collection_count);

        }
    }
}
