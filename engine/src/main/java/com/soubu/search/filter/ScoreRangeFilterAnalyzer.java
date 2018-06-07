package com.soubu.search.filter;

import com.alibaba.fastjson.JSONObject;
import com.soubu.search.score.FilterProperties;
import com.soubu.search.score.ScoreConst;
import org.elasticsearch.search.lookup.LeafDocLookup;
import org.elasticsearch.search.lookup.SourceLookup;

/**
 * 将score收缩到max与min之间
 * Created by wuchao on 17-5-17.
 */
public class ScoreRangeFilterAnalyzer extends BaseFilterAnalyzer {
    private double max;
    private double min;

    @Override
    public void init(FilterProperties filterProperties) {
        super.init(filterProperties);
        JSONObject params = (JSONObject) filterProperties.getParams();
        if (params == null) {
            throw new IllegalStateException("params of ShopDepositFilterAnalyzer can not be null or empty");
        }
        // max
        {
            max = params.getDouble(ScoreConst.RANGE_FILTER_SCORE_MAX);
        }
        // min
        {
            min = params.getDouble(ScoreConst.RANGE_FILTER_SCORE_MIN);
        }
    }

    @Override
    public double analyze(SourceLookup source, LeafDocLookup doc) {
        double score = super.analyze(source, doc);

        score = score > max ? max : score;
        score = score < min ? min : score;

        return score;
    }

}
