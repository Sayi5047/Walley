package io.hustler.wallzy.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.hustler.wallzy.R;
import io.hustler.wallzy.activity.ImagesActivity;
import io.hustler.wallzy.constants.WallZyConstants;
import io.hustler.wallzy.model.base.ResCollectionClass;
import io.hustler.wallzy.model.wallzy.response.ResGetAllCollections;
import io.hustler.wallzy.networkhandller.RestUtilities;
import io.hustler.wallzy.utils.MessageUtils;
import io.hustler.wallzy.utils.TextUtils;

public class CollectionsFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    @BindView(R.id.root)
    LinearLayout root;
    private RecyclerView verticalRv;

    public static CollectionsFragment getInstance() {
        return new CollectionsFragment();

    }

    public CollectionsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collections, container, false);
        verticalRv = view.findViewById(R.id.verticalRv);
        ButterKnife.bind(this,view);
        TextUtils.findText_and_applyTypeface(root,getActivity());
        new RestUtilities().getCollection(getActivity(), new RestUtilities.OnSuccessListener() {
            @Override
            public void onSuccess(Object onSuccessResponse) {
                ResGetAllCollections resGetAllCollections = new Gson().fromJson(onSuccessResponse.toString(), ResGetAllCollections.class);
                if (resGetAllCollections.isApiSuccess()) {
                    ArrayList<ResCollectionClass> collectionsArrayList = new ArrayList<>();
                    for (ResCollectionClass collectionClassFromApi : resGetAllCollections.getCollections()) {
                        ResCollectionClass collectionadapterClass = new ResCollectionClass();
                        collectionadapterClass.setImageCount(collectionClassFromApi.getImageCount());
                        collectionadapterClass.setArtistName(collectionClassFromApi.getArtistName());
                        collectionadapterClass.setArtistImage(collectionClassFromApi.getArtistImage());
                        collectionadapterClass.setArtistLink(collectionClassFromApi.getArtistLink());
                        collectionadapterClass.setCurated(true);
                        collectionadapterClass.setId(collectionClassFromApi.getId());
                        collectionadapterClass.setName(collectionClassFromApi.getName());
                        HashMap<Integer, String> coversMap = new HashMap<>();
                        for (int i = 0; i < 3; i++) {
                            coversMap.put(i, collectionClassFromApi.getCovers().get(i));
                        }
                        collectionadapterClass.setCovers(coversMap);


                        collectionsArrayList.add(collectionadapterClass);
                    }
                    setDatToRv(collectionsArrayList);
                }
            }

            @Override
            public void onError(String error) {
                MessageUtils.showDismissableSnackBar(getActivity(), getView(), error);
                Log.e(TAG, "onError: " + error);
            }
        });
        return view;
    }

    private void setDatToRv(ArrayList<ResCollectionClass> collectionClassArrayList) {
        CollectionAdapter collectionAdapter = new CollectionAdapter(getContext(), collectionClassArrayList, new CollectionAdapter.OnItemClickListener() {
            @Override
            public void onClick(ResCollectionClass resCollectionClass, ImageView imageView) {
                Intent intent = new Intent(getActivity(), ImagesActivity.class);
                intent.putExtra(WallZyConstants.INTENT_CAT_NAME, resCollectionClass.getName());
                intent.putExtra(WallZyConstants.INTENT_CAT_IMAGE, resCollectionClass.getCovers().get(0));
                intent.putExtra(WallZyConstants.INTENT_CAT_ID, resCollectionClass.getId());
                intent.putExtra(WallZyConstants.INTENT_IS_CAT, false);
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(Objects.requireNonNull(getActivity())
                        , imageView, getActivity().getString(R.string.transistion_blur_image));
                getActivity().startActivity(intent, optionsCompat.toBundle());
                MessageUtils.showDismissableSnackBar(Objects.requireNonNull(getActivity()), getView(), resCollectionClass.getName());

            }
        });
        verticalRv.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        verticalRv.setAdapter(collectionAdapter);
//        runLayoutAnimation(verticalRv);
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_anim_fall_down);

        recyclerView.setLayoutAnimation(controller);
        Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
}
