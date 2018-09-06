package com.zhiyicx.thinksnsplus.widget.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.password.findpassword.FindPasswordActivity;
import com.zhiyicx.thinksnsplus.widget.keyboard.VirtualKeyboardView;

import java.util.ArrayList;
import java.util.List;

/**
 * author: huwenyong
 * date: 2018/9/6 9:59
 * description:
 * version:
 */

public class InputPayPwdDialog extends HBaseDialog implements VirtualKeyboardView.KeyBoardListener {

    private View[] views = new View[6];
    private List<Integer> numbers = new ArrayList<>();
    private OnInputOkListener mOnInputOkListener;

    public void setOnInputOkListener(OnInputOkListener mOnInputOkListener) {
        this.mOnInputOkListener = mOnInputOkListener;
    }

    public InputPayPwdDialog(Activity context, boolean cancelOnTouchOutside) {
        super(context, R.layout.dialog_input_pay_pwd, cancelOnTouchOutside);

        setGravity(Gravity.BOTTOM);
        views[0] = getView(R.id.v1);
        views[1] = getView(R.id.v2);
        views[2] = getView(R.id.v3);
        views[3] = getView(R.id.v4);
        views[4] = getView(R.id.v5);
        views[5] = getView(R.id.v6);

        ((VirtualKeyboardView) getView(R.id.view_keyboard)).setKeyBoardListener(this);
        getView(R.id.tv_forget_password).setOnClickListener(v -> FindPasswordActivity.startSettingPayPasswordActivity(mContext));
        getView(R.id.ll_input).setOnClickListener(v -> getView(R.id.view_keyboard).setVisibility(View.VISIBLE));
        getView(R.id.iv_close).setOnClickListener(v -> getDialog().dismiss());

    }

    @Override
    public void showDialog() {
        super.showDialog();
        //重置
        numbers.clear();
        updateInputState();

    }

    @Override
    public void onNumberClickedListener(int num) {
        numbers.add(num);
        updateInputState();

        if(numbers.size() == 6){
            if(null != mOnInputOkListener){
                StringBuilder builder = new StringBuilder();
                for (int data : numbers) {
                    builder.append(data);
                }
                mOnInputOkListener.onInputOk(builder.toString());
            }
            getView(R.id.v6).postDelayed(() -> dismissDialog(),300);
        }
    }

    @Override
    public void onDeleteClickedListener() {
        if (numbers.size() != 0){
            numbers.remove(numbers.size() - 1);
            updateInputState();
        }
    }

    @Override
    public void onKeyBoardDismissListener() {
        getView(R.id.view_keyboard).setVisibility(View.INVISIBLE);
    }


    private void updateInputState(){
        for (int i = 0; i < views.length; i++) {
            views[i].setVisibility(i < numbers.size() ? View.VISIBLE : View.INVISIBLE );
        }
    }

    public interface OnInputOkListener{
        void onInputOk(String str);
    }

}
