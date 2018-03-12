package cn.qs.android.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by pangpingfei on 2017/2/6.
 */

public abstract class BaseFragment extends Fragment {

    private ProgressDialog loadingDialog;

    protected Context context;
    protected View mRootView;

    private Unbinder unbinder;

    // 子类实现
    protected abstract int getContentViewId();
    protected abstract void init(Bundle savedInstanceState);
    protected void deInit(){ };

    protected void showLoading(String message) {
        if (loadingDialog == null) {
            loadingDialog = new ProgressDialog(context);
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mRootView = inflater.inflate(getContentViewId(), container, false);

        unbinder = ButterKnife.bind(this, mRootView); //绑定fragment

        this.context = getActivity();

        init(savedInstanceState);

        return mRootView;
    }


    @Override
    public void onDestroy() {

        unbinder.unbind();

        deInit();

        super.onDestroy();
    }

}
