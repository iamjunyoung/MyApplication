package com.bbeaggoo.myapplication.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bbeaggoo.myapplication.R;
import com.bbeaggoo.myapplication.datas.ItemObjects;
import com.bbeaggoo.myapplication.itemtouchhelper.ItemTouchHelperListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by user on 2018-03-06.
 */

public class ListRecyclerViewAdapter  extends RecyclerView.Adapter<ViewHolder>
        implements AdapterContract.Model, AdapterContract.View, ItemTouchHelperListener {
    public static final int VIEW_TYPE_NORMAL = 0;
    public static final int VIEW_TYPE_FIRST = 1;

    //ArrayList<ItemObjects> listAllItems=new ArrayList<ItemObjects>();
    //ArrayList<ItemObjects> listDispItems=new ArrayList<ItemObjects>();

    ArrayList<ItemObjects> itemList; // --> listAllItems
    ArrayList<ItemObjects> dispItemList; // --> listDispItems

    //아래처럼 바꾸는게 맞는가?
    //private List itemList;

    private Context context;
    private String TAG = "Solvent";

    public ListRecyclerViewAdapter(Context context, ArrayList<ItemObjects> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    // Added by JY
    // 왜 TodoListener 라는 형태로 만들었을까? (예제앱은)
    public ListRecyclerViewAdapter(Context context) {
        this.context = context;
        itemList = new ArrayList<ItemObjects>();
        dispItemList = new ArrayList<ItemObjects>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        if (viewType == VIEW_TYPE_NORMAL) {
            Log.i("JYN", "hi ");
            view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
            return new MyViewHolders(view);
        } else {
            Log.i("JYN", "hi FirstItemViewHolders ");
            view = LayoutInflater.from(context).inflate(R.layout.item_staggered_first, parent, false);
            return new FirstItemViewHolders(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return VIEW_TYPE_FIRST;
        else return VIEW_TYPE_NORMAL;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bind(context, itemList.get(position));
    }

    private Bitmap resize(int image) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        //출처: http://aroundck.tistory.com/59 [돼지왕 왕돼지 놀이터]
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), image, options);
        return bm;
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    void update(int itemId) {
        int position = -1;
        for (int i = 0; i < itemList.size(); i++) {
            if (itemId == itemList.get(i).getId()) {
                position = i;
                break;
            }
        }
        notifyItemChanged(position);
    }

    void addItem(ItemObjects itemObjects) {
        itemList.add(itemObjects);
        notifyItemInserted(itemList.size() - 1);
    }

    void removeItem(ItemObjects itemObjects) {
        itemList.remove(itemObjects);
        //remove를 구현하려면 equals()를 오버라이드 해야 한다.
        notifyItemRemoved(itemList.indexOf(itemObjects));
    }

    public void clear() {
        itemList.clear();
        notifyDataSetChanged();
    }

/////////

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        //Log.i(TAG, "onItemMove.", new Throwable());
        Log.i(TAG, "onItemMove. fromPosition : "+fromPosition+", toPosition : "+toPosition + "    this obj : " + this);
        Log.i(TAG, "onItemMove. fromItem : "+itemList.get(fromPosition).getName() + ", toItem : "+itemList.get(toPosition).getName());

        if(fromPosition < 0 || fromPosition >= itemList.size() || toPosition < 0 || toPosition >= itemList.size()){
            Log.i(TAG, "onItemMove. return false case    from : " + fromPosition + ",   toPosition : " + toPosition);
            return false;
        }

        ItemObjects fromItem = itemList.get(fromPosition);


        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(itemList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(itemList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);

        return true;
    }

    @Override
    public void onItemRemove(int position) {
        Log.i(TAG, "onItemRemove. pos : " + position + "    item : "+itemList.get(position).getName());

        itemList.remove(position);
        notifyItemRemoved(position);

        //과연 아래 방법 말고는 없는가..
        //notifyDataSetChanged();
    }

    @Override
    public void onItemSwipe(int position) {
        Log.i("JYN", "onItemSwipe. item : "+itemList.get(position).getName());

    }

    @Override
    public void add(ItemObjects itemObjects) {

    }

    @Override
    public ItemObjects remove(int position) {
        return null;
    }

    @Override
    public ItemObjects getPhoto(int position) {
        return null;
    }

    @Override
    public void addItems(List<ItemObjects> list) {
        itemList.addAll(list);
        //notifyDataSetChanged();
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public void clearItem() {
        if (itemList != null) {
            itemList.clear();
        }
    }

    @Override
    public void refreshItemList() {
        notifyDataSetChanged();
    }

    @Override
    public void refreshItemAdded(int position) {

    }

    @Override
    public void refreshItemRemoved(int postion) {

    }

    @Override
    public void refreshItemChanged(int position) {

    }
}
