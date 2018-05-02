package com.bbeaggoo.myapplication.singletons;

import com.bbeaggoo.myapplication.datas.ItemObjects;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by junyoung on 2018. 4. 28..
 */

public class FileItemsManager {
    //DB라 생각하자.
    private static ArrayList<ItemObjects> listViewItems;
    private static ArrayList<ItemObjects> listDispItems;

    private static int maxId = -1;

    public static ArrayList<ItemObjects> getFileItemsData(String curPath, ArrayList<String> itemFiles, ArrayList<String> pathFiles) {
        // 현재 device의 file 구조를 읽어, dir, file을 구조화 한다.
        // 각 dir, file들을 ItemObjects로 만든다.
        // ItemObjects에 대해서 FileItemObject, DirItemObject 등으로 구분해야 할 필요도 있을듯.
        //현재 getDirInfo()한 애의 정보를 load
        for(int i=0;i<itemFiles.size();i++){
            ItemObjects item = new ItemObjects();
            item.checked = false;
            item.name = itemFiles.get(i);
            item.path = pathFiles.get(i);
            listViewItems.add(item);
        }

        if (listViewItems != null) {
            Collections.sort(listViewItems, nameComparator);
        }
        listDispItems.addAll(listViewItems);

        return listViewItems;
    }

    private static final Comparator<ItemObjects> nameComparator
            = new Comparator<ItemObjects>() {
        public final int
        compare(ItemObjects a, ItemObjects b) {
            return collator.compare(a.name, b.name);
        }
        private final Collator collator = Collator.getInstance();
    };


}
