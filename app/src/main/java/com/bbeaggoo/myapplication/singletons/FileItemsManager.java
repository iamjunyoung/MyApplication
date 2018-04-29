package com.bbeaggoo.myapplication.singletons;

import com.bbeaggoo.myapplication.datas.ItemObjects;

import java.util.List;

/**
 * Created by junyoung on 2018. 4. 28..
 */

public class FileItemsManager {
    //DB라 생각하자.
    private static List<ItemObjects> listViewItems;
    private static int maxId = -1;

    public static List<ItemObjects> getFileItemsData(){
        return listViewItems;
    }

}
