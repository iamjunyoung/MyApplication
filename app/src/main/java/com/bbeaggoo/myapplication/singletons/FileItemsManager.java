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

    public static List<ItemObjects> getFileItemsData() {
        // 현재 device의 file 구조를 읽어, dir, file을 구조화 한다.
        // 각 dir, file들을 ItemObjects로 만든다.
        // ItemObjects에 대해서 FileItemObject, DirItemObject 등으로 구분해야 할 필요도 있을듯.
        return listViewItems;
    }

}
