package com.hm.iou.uikit.richedittext;

import com.hm.iou.uikit.richedittext.itemview.RichItemData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by syl on 2018/8/28.
 */

public class RichDataUtil {

    public static String getStringFromEditData(List<RichItemData> listData) {
        String result = "";
        return result;
    }

    public static List<RichItemData> getEditDataFromString(String html) {
        List<RichItemData> richItemDataList = new ArrayList<>();
//        for (int i = 0; i < textList.size(); i++) {
//            String text = textList.get(i);
//            RichItemData editData = new RichItemData();
//            if (text.contains("<img") && text.contains("src=")) {
//                //imagePath可能是本地路径，也可能是网络地址
//                ImageData imageData = new ImageData(getImgSrc(text));
//                imageData.setImageWidth(getImgWidth(text));
//                imageData.setImageHeight(getImgHeight(text));
//                editData.setImageData(imageData);
//                richItemDataList.add(editData);
//            } else if (text.contains("<p") && text.contains("</p>")) {
//                //imagePath可能是本地路径，也可能是网络地址
//                text = text.replace("<p>", "");
//                text = text.replace("</p>", "");
//                editData.setInputStr(text);
//                richItemDataList.add(editData);
//            }
//        }
        return richItemDataList;
    }


}
