package com.hm.iou.uikit.richedittext;

import com.hm.iou.uikit.richedittext.model.EditData;
import com.hm.iou.uikit.richedittext.model.ImageData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by syl on 2018/8/28.
 */

public class RichDataUtil {

    public static String getStringFromEditData(List<EditData> listData) {
        StringBuffer content = new StringBuffer();
        for (EditData itemData : listData) {
            if (itemData.getInputStr() != null) {
                content.append("<p>").append(itemData.getInputStr()).append("</p>");
            } else if (itemData.getImageData() != null) {
                content.append("<img src=\"")
                        .append(itemData.getImageData().getImagePath())
                        .append("\" ")
                        .append("width=\"")
                        .append(itemData.getImageData().getImageWidth())
                        .append("\" ")
                        .append("height=\"")
                        .append(itemData.getImageData().getImageHeight())
                        .append("\" ")
                        .append("/>");
            }
        }
        String result = "<body>" + content.toString() + "</body>";
        return result;
    }

    public static List<EditData> getEditDataFromString(String html) {
        //imagePath可能是本地路径，也可能是网络地址
        html = html.replace("<body>", "");
        html = html.replace("</body>", "");
        List<String> textList = cutStringByImgTag(html);
        List<EditData> editDataList = new ArrayList<>();
        for (int i = 0; i < textList.size(); i++) {
            String text = textList.get(i);
            EditData editData = new EditData();
            if (text.contains("<img") && text.contains("src=")) {
                //imagePath可能是本地路径，也可能是网络地址
                ImageData imageData = new ImageData(getImgSrc(text));
                imageData.setImageWidth(getImgWidth(text));
                imageData.setImageHeight(getImgHeight(text));
                editData.setImageData(imageData);
                editDataList.add(editData);
            } else if (text.contains("<p") && text.contains("</p>")) {
                //imagePath可能是本地路径，也可能是网络地址
                text = text.replace("<p>", "");
                text = text.replace("</p>", "");
                editData.setInputStr(text);
                editDataList.add(editData);
            }
        }
        return editDataList;
    }


}
