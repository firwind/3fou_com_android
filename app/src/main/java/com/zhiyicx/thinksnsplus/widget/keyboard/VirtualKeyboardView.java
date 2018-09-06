package com.zhiyicx.thinksnsplus.widget.keyboard;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhiyicx.common.utils.recycleviewdecoration.GridDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author: huwenyong
 * date: 2018/9/5 17:56
 * description:
 * version:
 */

public class VirtualKeyboardView extends RecyclerView implements MultiItemTypeAdapter.OnItemClickListener {


    private List<Map<String, String>> datas;
    private KeyBoardListener mKeyBoardListener;

    public void setKeyBoardListener(KeyBoardListener mKeyBoardListener) {
        this.mKeyBoardListener = mKeyBoardListener;
    }



    public VirtualKeyboardView(Context context) {
        this(context,null);
    }

    public VirtualKeyboardView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public VirtualKeyboardView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initView();
    }


    /**
     * 初始化view
     */
    private void initView() {

        setBackgroundResource(R.color.keyboard_stroke);

        setOverScrollMode(OVER_SCROLL_NEVER);

        int lineSize = getContext().getResources().getDimensionPixelSize(R.dimen.dp0_5);
        setLayoutManager(new GridLayoutManager(getContext(),3));
        addItemDecoration(new GridDecoration(lineSize,lineSize));

        datas = getKeyBoardData();
        CommonAdapter mAdapter = new CommonAdapter<Map<String, String>>(getContext(),R.layout.grid_item_virtual_keyboard,datas) {
            @Override
            protected void convert(com.zhy.adapter.recyclerview.base.ViewHolder holder, Map<String, String> stringStringMap, int position) {
                holder.getTextView(R.id.btn_keys).setText(stringStringMap.get("name"));
                holder.getTextView(R.id.btn_keys).setSelected(position == 9);
                holder.getImageViwe(R.id.iv_delete).setVisibility(position == 11 ? View.VISIBLE:View.GONE);
                holder.getImageViwe(R.id.iv_delete).setSelected(true);
            }
        };
        mAdapter.setOnItemClickListener(this);
        setAdapter(mAdapter);
    }

    /**
     * 初始化数据源
     * @return
     */
    private List<Map<String, String>> getKeyBoardData(){
        ArrayList<Map<String, String>> valueList = new ArrayList<>();

        // 初始化按钮上应该显示的数字
        for (int i = 1; i < 13; i++) {
            Map<String, String> map = new HashMap<>();
            if (i < 10) {
                map.put("name", String.valueOf(i));
            } else if (i == 10) {
                map.put("name", "");
            } else if (i == 11) {
                map.put("name", String.valueOf(0));
            } else if (i == 12) {
                map.put("name", "");
            }
            valueList.add(map);
        }

        return valueList;

    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        if(null != mKeyBoardListener){
            if(position == 11){
                mKeyBoardListener.onDeleteClickedListener();
            }else if(position != 9){
                int number = 0;
                try {
                    number = Integer.parseInt(datas.get(position).get("name"));
                }catch (Exception e){}
                mKeyBoardListener.onNumberClickedListener(number);
            }
        }
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }


    public interface KeyBoardListener{

        void onNumberClickedListener(int num);

        void onDeleteClickedListener();

        void onKeyBoardDismissListener();

    }

}
