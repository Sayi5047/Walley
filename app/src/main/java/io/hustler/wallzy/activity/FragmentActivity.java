package io.hustler.wallzy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.hustler.wallzy.R;
import io.hustler.wallzy.constants.WallZyConstants;
import io.hustler.wallzy.fragments.ProfileFragment;
import io.hustler.wallzy.utils.TextUtils;

public class FragmentActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fragment_frame)
    FrameLayout fragmentFrame;
    FragmentManager supportFragentManager;
    FragmentTransaction fragmentTransaction;
    @BindView(R.id.root)
    RelativeLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        ButterKnife.bind(this);
        supportFragentManager = getSupportFragmentManager();
        fragmentTransaction = supportFragentManager.beginTransaction();
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        TextUtils.findText_and_applyTypeface(root, FragmentActivity.this);
        getIntentData();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        int fragmentNumber = intent.getIntExtra(WallZyConstants.INTENT_FRAGMENT_ACTIVITY_FRAGMENT_NUMBER, 0);
        switch (fragmentNumber) {
            case 0: {
                callProfileFragment();
            }
            break;
        }

    }

    private void callProfileFragment() {
        fragmentTransaction.add(R.id.fragment_frame, ProfileFragment.getInstance());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
