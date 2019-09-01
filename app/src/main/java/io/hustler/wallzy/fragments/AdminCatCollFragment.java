package io.hustler.wallzy.fragments;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.hustler.wallzy.R;
import io.hustler.wallzy.networkhandller.RestUtilities;
import io.hustler.wallzy.utils.MessageUtils;

import static android.app.Activity.RESULT_OK;

public class AdminCatCollFragment extends Fragment {
    private int PICK_IMAGE_COLLECTION = 1;
    private ArrayList<String> selectedImagesArrayList;

    @BindView(R.id.uploadcatcollbtn)
    Button uploadcatcollbtn;
    @BindView(R.id.image1)
    ImageView image1;
    @BindView(R.id.image2)
    ImageView image2;
    @BindView(R.id.image3)
    ImageView image3;
    @BindView(R.id.imagesLayout)
    LinearLayout imagesLayout;
    @BindView(R.id.cat_btn_rd)
    RadioButton catBtnRd;
    @BindView(R.id.col_btn_rd)
    RadioButton colBtnRd;
    @BindView(R.id.rd_group)
    RadioGroup rdGroup;
    @BindView(R.id.name_input_layout)
    EditText nameInputLayout;
    @BindView(R.id.text_input_layout)
    TextInputLayout textInputLayout;
    @BindView(R.id.upload_cat_coll_layout)
    RelativeLayout uploadCatCollLayout;
    private String TAG = this.getClass().getSimpleName();

    public AdminCatCollFragment() {
    }


    public static AdminCatCollFragment newInstance() {
        AdminCatCollFragment fragment = new AdminCatCollFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_cat_coll, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @OnClick({R.id.uploadcatcollbtn, R.id.imagesLayout, R.id.cat_btn_rd, R.id.col_btn_rd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.uploadcatcollbtn:
                new RestUtilities().uploadImageToIK(selectedImagesArrayList.get(0), "TESTFROMAPP", getActivity(), new RestUtilities.OnSuccessListener() {
                    @Override
                    public void onSuccess(Object onSuccessResponse) {
                        Log.i(TAG, "onSuccess: " + onSuccessResponse.toString());
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
                break;
            case R.id.imagesLayout:
                launchGallery();
                break;

            case R.id.cat_btn_rd:
                image2.setVisibility(View.GONE);
                image3.setVisibility(View.GONE);
                break;
            case R.id.col_btn_rd:
                image2.setVisibility(View.VISIBLE);
                image3.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void launchGallery() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
//            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//        }
//
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_COLLECTION);
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        startActivityForResult(intent, PICK_IMAGE_COLLECTION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        try {

            if (requestCode == PICK_IMAGE_COLLECTION && resultCode == RESULT_OK && null != data) {
                selectedImagesArrayList = new ArrayList<>();
                if (data.getData() != null) {
                    /*USER SELECTS SINGLE IMAGE*/
                    selectedImagesArrayList.add(getSingleImageLocatioFromGalley(data.getData()));
                    Glide.with(Objects.requireNonNull(getActivity())).load(selectedImagesArrayList.get(0)).centerCrop().into(image1);
                } else if (null != data.getClipData()) {
                    /*USER SELECTS MULTIPLE IMAGES*/
                    ClipData clipData = data.getClipData();
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        selectedImagesArrayList.add(getSingleImageLocatioFromGalley(item.getUri()));
                    }
                    Glide.with(Objects.requireNonNull(getActivity())).load(selectedImagesArrayList.get(0)).centerCrop().into(image1);
                    Glide.with(Objects.requireNonNull(getActivity())).load(selectedImagesArrayList.get(1)).centerCrop().into(image2);
                    Glide.with(Objects.requireNonNull(getActivity())).load(selectedImagesArrayList.get(2)).centerCrop().into(image3);
                    MessageUtils.showShortToast(getActivity(), "Selected images are" + clipData.getItemCount());
                }

            } else {
                MessageUtils.showShortToast(getActivity(), "You haven't picked Image");

            }
        } catch (Exception e) {
            MessageUtils.showShortToast(getActivity(), "Something went wrong");
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    private String getSingleImageLocatioFromGalley(Uri data) {
        String[] filepathColumn = {MediaStore.Images.Media.DATA};
        assert data != null;
        Cursor cursor = Objects.requireNonNull(getActivity())
                .getContentResolver()
                .query(data, filepathColumn,
                        null, null, null);
        assert cursor != null;
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filepathColumn[0]);
        String imagelocation = cursor.getString(columnIndex);
        cursor.close();
        return imagelocation;
    }

}
