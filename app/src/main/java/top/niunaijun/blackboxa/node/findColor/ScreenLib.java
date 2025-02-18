package top.niunaijun.blackboxa.node.findColor;

import static top.niunaijun.blackboxa.node.GlobalVariableHolder.mHeight;
import static top.niunaijun.blackboxa.node.GlobalVariableHolder.mWidth;

import java.util.ArrayList;

public class ScreenLib {

    //多点找色
    public static int[] findColor(int mainColor, String subColors, double distance, int x1, int y1, int x2, int y2) {
        GBData.getImageBitmap();
        return GBData.MultiPointFindColor(mainColor, subColors, distance, x1, y1, x2, y2);
    }
    //多点找色（优化）
    public static int[] findColor(int[] mainColor, ArrayList<Integer[]> subColors, double distance, int x1, int y1, int x2, int y2) {
        GBData.getImageBitmap();
        return GBData.MultiPointFindColor(mainColor, subColors, distance, x1, y1, x2, y2);
    }
    //百分比找色范围
    public static int[] findColor(int mainColor, String subColors, double distance, double x1, double y1, double x2, double y2) {
        return findColor(mainColor, subColors, distance, (int)(x1*mWidth), (int)(y1*mHeight), (int)(x2*mWidth), (int)(y2*mHeight));
    }
    //百分比找色范围（优化）
    public static int[] findColor(int[] mainColor, ArrayList<Integer[]> subColors, double distance, double x1, double y1, double x2, double y2) {
        return findColor(mainColor, subColors, distance, (int)(x1*mWidth), (int)(y1*mHeight), (int)(x2*mWidth), (int)(y2*mHeight));
    }

}