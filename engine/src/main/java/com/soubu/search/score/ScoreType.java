package com.soubu.search.score;

/**
 * DAY: 按照距离今日的天数给予权重得分, 可以使用BIGGER实现DAY的功能 <br/>
 * DATA_ECHELON: 按照日期分段获取得分, 不衰减, 获取固定值 <br/>
 * BIGGER: 根据field数值获取权重得分, 大于等于给定值的doc获得对应的得分 <br/>
 * SMALLER: 根据field数值获取权重得分, 小于等于给定值的doc获得对应的得分 <br/>
 * SPECIFIC: 根据指定的&lt;key, value&gt; 数值, 返回相应的得分(value) <br/>
 * SEG_UNARY: 使用一元一次分段函数获取得分, 当field的值在给定范围之外时, 取范围边界值
 */
public enum ScoreType {
    DATE, DATA_ECHELON, BIGGER, SMALLER, SPECIFIC, SEG_UNARY;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
