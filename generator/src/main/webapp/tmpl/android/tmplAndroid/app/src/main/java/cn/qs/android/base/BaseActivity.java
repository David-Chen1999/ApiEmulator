package cn.qs.android.base;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import cn.qs.android.utils.ExitUtil;


public abstract class BaseActivity extends AppCompatActivity  {

    private ProgressDialog loadingDialog;

    protected abstract int getContentViewId();
    protected abstract void init(Bundle savedInstanceState);
    protected void deInit(){};

    protected void showLoading(String message) {
        if (loadingDialog == null) {
            loadingDialog = new ProgressDialog(this);
            loadingDialog.setCancelable(false);
        }
        if (loadingDialog.isShowing()) { loadingDialog.dismiss(); }
        loadingDialog.setMessage(message);
        loadingDialog.show();
    }

    protected void dismissLoading() {
        if (loadingDialog == null) { return; }
        if (loadingDialog.isShowing()) { loadingDialog.dismiss(); }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ExitUtil.getInstance().addActivity(this);

        setContentView(getContentViewId());

        ButterKnife.bind(this); // 绑定Activity 必须在setContentView之后

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 设置为竖屏

        init(savedInstanceState);
    }


    @Override
    protected void onDestroy() {
        deInit();

        super.onDestroy();
    }

}
