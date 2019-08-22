package io.hustler.wallzy.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import io.hustler.wallzy.R;
import io.hustler.wallzy.adapters.VerticalImagesAdapter;
import io.hustler.wallzy.customviews.VerticalRecyclerView;
import io.hustler.wallzy.model.CategoryImagesDTO;
import io.hustler.wallzy.utils.MessageUtils;

public class CategoriesFragment extends Fragment {
    private static final String TAG = "CategoriesFragment";
    VerticalRecyclerView verticalRv;

    public static CategoriesFragment getInstance() {
        return new CategoriesFragment();

    }

    public CategoriesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        verticalRv = view.findViewById(R.id.verticalRv);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(getActivity().getString(R.string.DB_CAT_NODE));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG, dataSnapshot.toString());
                CategoryImagesDTO categoryImagesDTO = new CategoryImagesDTO();
                ArrayList<CategoryImagesDTO.Category> categoryArrayList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CategoryImagesDTO.Category category = new CategoryImagesDTO.Category();
                    category.setName(snapshot.getKey());
                    category.setCoverImage(snapshot.getValue(String.class));
                    categoryArrayList.add(category);
                }
                categoryImagesDTO.setCategoryArrayList(categoryArrayList);
                VerticalImagesAdapter verticalImagesAdapter = new VerticalImagesAdapter(categoryArrayList, getActivity(),
                        category -> MessageUtils.showDismissableSnackBar(Objects.requireNonNull(getActivity()), verticalRv, category.getName()));
                verticalRv.intiate(verticalImagesAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i(TAG, "Cancelled");

            }
        });
        return view;
    }
}
