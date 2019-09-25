package io.hustler.wallzy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;

import io.hustler.wallzy.R;

public class OnBoardAdapter extends RecyclerView.Adapter<OnBoardAdapter.OnBoardViewHolder> {
    Context context;
    private ArrayList<String> imageUrls;
    private ArrayList<String> messages;

    public OnBoardAdapter(Context context, ArrayList<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
        fillMessages();
    }

    private void fillMessages() {
        messages = new ArrayList<>();
        messages.add("Hi there Welcome To Wallzy");
        messages.add("Hundreds Of Wallpapers");
        messages.add("Save them , download them");
        messages.add("Images curated from Best Designers");
        messages.add("Earn money from your art works");
    }

    @NonNull
    @Override
    public OnBoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.onboard_item, parent, false);
        return new OnBoardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnBoardViewHolder holder, int position) {
        holder.textView.setText(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class OnBoardViewHolder extends RecyclerView.ViewHolder {
        ImageView bgImage;
        TextView textView;
        LottieAnimationView lottieView;

        public OnBoardViewHolder(@NonNull View itemView) {
            super(itemView);
            bgImage = itemView.findViewById(R.id.background_image);
            lottieView= itemView.findViewById(R.id.animview);
            textView = itemView.findViewById(R.id.Message);
        }
    }
}
