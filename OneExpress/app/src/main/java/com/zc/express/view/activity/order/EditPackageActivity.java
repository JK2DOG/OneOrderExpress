package com.zc.express.view.activity.order;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.zc.express.R;
import com.zc.express.bean.Order;
import com.zc.express.bean.Package;
import com.zc.express.model.UserModel;
import com.zc.express.utils.ToastUtils;
import com.zc.express.view.activity.BaseActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 更新包裹信息界面
 * Created by JK2DOG on 2017/7/23.
 */

public class EditPackageActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.title_top)
    TextView mTitleTv;//标题

    @Inject
    UserModel mUserModel;

    @BindView(R.id.et_length)
    EditText mLengthEt;
    @BindView(R.id.et_width)
    EditText mWidthEt;
    @BindView(R.id.et_height)
    EditText mHeightEt;
    @BindView(R.id.et_weight)
    EditText mWeightEt;
    @BindView(R.id.et_value)
    EditText mValueEt;


    private Package mPackage;
    private Order mOrder;


    public static void start(Context context, Package entity) {
        Intent intent = new Intent(context, EditPackageActivity.class);
        intent.putExtra("entity", entity);
        context.startActivity(intent);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_edit_package;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void initViews() {
        mPackage = getIntent().getParcelableExtra("entity");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTitleTv.setText(mPackage.getDescription());//包裹名称
        mLengthEt.append(mPackage.getLength() + "");
        mHeightEt.append(mPackage.getHeight() + "");
        mWidthEt.append(mPackage.getWidth() + "");
        mWeightEt.append(mPackage.getWeight() + "");
        mValueEt.append(mPackage.getValue() + "");

    }


    @OnClick(R.id.cv_update)
    void onClick() {
        String length = mLengthEt.getText().toString().trim();
        String height = mHeightEt.getText().toString().trim();
        String width = mWidthEt.getText().toString().trim();
        String weight = mWeightEt.getText().toString().trim();
        String value = mValueEt.getText().toString().trim();
        if (TextUtils.isEmpty(length) || TextUtils.isEmpty(height) || TextUtils.isEmpty(width) || TextUtils.isEmpty(weight) || TextUtils.isEmpty(value)) {
            ToastUtils.showToast("更新信息不能为空！");
            return;
        }
        mPackage.setLength(Integer.parseInt(length));
        mPackage.setHeight(Integer.parseInt(height));
        mPackage.setWidth(Integer.parseInt(width));
        mPackage.setWeight(Integer.parseInt(weight));
        mPackage.setValue(Integer.parseInt(value));
        List<Package> packages = mOrder.getPackages();
        if (packages!=null&&packages.size()>0){
            for (int i=0;i<packages.size();i++){
                if (packages.get(i).getId()==mPackage.getId()){
                    packages.set(i,mPackage);
                }
            }
        }
      mOrder.setPackages(packages);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
