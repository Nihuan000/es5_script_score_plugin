package com.soubu.search.analyzer.util;

public class MathUtils {

    /**
     * 解析2点, 获取分段函数y=ax+b的参数a和b, 存入paramsA和paramsB的index位置
     * Created by Nihuan on 06/06/18.
     */
    public static void analyzeUnaryFunction(double x1, double y1, double x2, double y2, int index, double[] paramsA, double[] paramsB) {
        paramsA[index] = (y1 - y2) / (x1 - x2);
        paramsB[index] = y1 - x1 * paramsA[index];
    }

}
