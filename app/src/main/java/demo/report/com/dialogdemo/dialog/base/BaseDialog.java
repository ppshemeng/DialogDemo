package demo.report.com.dialogdemo.dialog.base;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.util.Log;

import demo.report.com.dialogdemo.dialog.IDialog;
import demo.report.com.dialogdemo.dialog.builder.IDialogBuilder;
import demo.report.com.dialogdemo.dialog.helper.BaseDialogHelper;

/**
 * Created by yangjian-ds3 on 2018/3/21.
 */

public abstract class BaseDialog <D extends IDialogBuilder<D>> extends Dialog implements IDialog<D> {

    private Context mContext;

    public BaseDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public BaseDialog(@NonNull Context context, @StyleRes int themeResId) {

        super(context, themeResId);
        this.mContext = context;
    }

    public void setBuilder(D data){

        initDialog(mContext,data);
    }
    @Override
    public void initDialog(Context context, D data) {
        BaseDialogHelper<D> mHelper = onCreateHelper(context,data);
        if(mHelper == null){
            BaseDialogHelper<D> mDefaultHelper = onCreateDefaultHelp(context,data);
            if(mDefaultHelper == null){
                throw new NullPointerException("onCreateHelper and onCreateDefaultHelp fail by which mHelper is null");
            }else{
                if(mDefaultHelper.getContextView() == null){
                    throw new NullPointerException("mDefaultHelper fail by which view is null in the helper");
                } else{
                    setContentView(mDefaultHelper.getContextView());
                    mDefaultHelper.setBuilder(data,this);
                }
            }
        }else{
            if(mHelper.getContextView() == null){
                throw new NullPointerException("ContextView fail by which view is null in the helper");
            }
            setContentView(mHelper.getContextView());
            mHelper.setBuilder(data,this);
        }
    }

    @Override
    public BaseDialogHelper<D> onCreateHelper(Context context, D data) {

        if(data != null && data.getHelperClass() != null){
            Class<? extends BaseDialogHelper<D>> cls = data.getHelperClass();
            //参数类型数组
            Class[] parameterTypes={Context.class};
            //根据参数类型获取相应的构造函数
            try {
                java.lang.reflect.Constructor<? extends BaseDialogHelper<D>> constructor = cls.getConstructor(parameterTypes);
                Object[] parameters={context};
                BaseDialogHelper<D> h = constructor.newInstance(parameters);
                return h;
            } catch (Exception e) {
                //e.printStackTrace();
                Log.d("BaseDialog","the helper in the diaolg is wrong");
            }
        }
        return null;
    }

    /**
     * set the default helper
     * @param context
     * @param data
     * @return
     */
    public abstract BaseDialogHelper<D> onCreateDefaultHelp(Context context, D data);

}
