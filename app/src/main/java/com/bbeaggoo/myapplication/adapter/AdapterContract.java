package com.bbeaggoo.myapplication.adapter;


import com.bbeaggoo.myapplication.datas.ItemObjects;
import com.bbeaggoo.myapplication.listener.OnItemClickListener;

import java.util.List;
/**
 * Created by junyoung on 2018. 3. 13..
 */

public interface AdapterContract{
    interface Model {
        void add(ItemObjects itemObjects);
        ItemObjects remove(int position);
        ItemObjects get(int position);
        void addItems(List<ItemObjects> list);
        int getSize();
        void clearItem();
    }

    interface View {
        void refreshItemList();
        void refreshItemAdded(int position);
        void refreshItemRemoved(int postion);
        void refreshItemChanged(int position);
        void setOnClickListener(OnItemClickListener onClickListener);
    }


}
