package com.cn.frame.http.retrofit;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.cn.frame.data.Tasks;
import com.cn.frame.http.listener.ResponseListener;
import com.cn.frame.widgets.dialog.LoadingDialog;

import java.util.Objects;

import io.reactivex.disposables.Disposable;

/**
 * Observer加入加载框
 *
 * @param <T>
 * @author dundun
 */
public class AbstractLoadViewObserver<T> extends AbstractDataObserver<T> {
    private boolean mShowDialog, mCancelAble;
    private LoadingDialog loadingDialog;
    private Context mContext;
    private Disposable d;

    public AbstractLoadViewObserver(Context context, Tasks task, ResponseListener listener) {
        this(context, false, task, listener);
    }

    public AbstractLoadViewObserver(Context context, boolean mShowDialog, Tasks task, ResponseListener listener) {
        this(context, mShowDialog, true, task, listener);
    }

    public AbstractLoadViewObserver(Context context, boolean showDialog, boolean cancelAble, Tasks task,
            ResponseListener listener) {
        mContext = context;
        mShowDialog = showDialog;
        mCancelAble = cancelAble;
        super.setParams(task, listener);
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.d = d;
        if (!isConnected(mContext)) {
            Toast.makeText(mContext, "网络无连接", Toast.LENGTH_SHORT).show();
            if (d.isDisposed()) {
                d.dispose();
            }
        }
        else {
            if (loadingDialog == null && mShowDialog) {
                loadingDialog = new LoadingDialog(mContext);
                loadingDialog.setCancelable(mCancelAble);
                loadingDialog.setCanceledOnTouchOutside(mCancelAble);
                loadingDialog.show();
            }
        }
    }

    @Override
    public void onError(Throwable e) {
        if (d.isDisposed()) {
            d.dispose();
        }
        hidDialog();
        super.onError(e);
    }

    @Override
    public void onComplete() {
        if (d.isDisposed()) {
            d.dispose();
        }
        hidDialog();
        super.onComplete();
    }

    private void hidDialog() {
        if (loadingDialog != null && mShowDialog) { loadingDialog.dismiss(); }
        loadingDialog = null;
    }

    /**
     * 是否有网络连接，不管是wifi还是数据流量
     *
     * @return 网络
     */
    private static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = Objects.requireNonNull(cm).getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        return info.isAvailable();
    }
}

