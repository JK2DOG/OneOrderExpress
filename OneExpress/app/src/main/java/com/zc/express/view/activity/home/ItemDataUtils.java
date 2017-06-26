package com.zc.express.view.activity.home;



import com.zc.express.R;
import com.zc.express.bean.ItemBean;

import java.util.ArrayList;
import java.util.List;


public final class ItemDataUtils {

    private ItemDataUtils() {
    }

    public static List<ItemBean> getItemBeans() {
        List<ItemBean> itemBeans = new ArrayList<>();
        itemBeans.add(new ItemBean(R.mipmap.menu_left_info, "个人信息", false));
        itemBeans.add(new ItemBean(R.mipmap.menu_left_service, "服务条款", false));
        itemBeans.add(new ItemBean(R.mipmap.menu_left_about, "关于我们", false));
//        itemBeans.add(new ItemBean(R.mipmap.sidebar_album, "我的相册", false));
//        itemBeans.add(new ItemBean(R.mipmap.sidebar_file, "我的文件", false));
        return itemBeans;
    }

}
