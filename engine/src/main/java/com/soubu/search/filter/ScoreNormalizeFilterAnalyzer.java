package com.soubu.search.filter;

import com.alibaba.fastjson.JSONObject;
import com.soubu.search.score.FilterProperties;
import com.soubu.search.score.ScoreConst;
import org.elasticsearch.search.lookup.LeafDocLookup;
import org.elasticsearch.search.lookup.SourceLookup;

/**
 * 将得分归一化, 默认归一化以300为满分, 最终界限为[-100, 300]
 */
public class ScoreNormalizeFilterAnalyzer extends BaseFilterAnalyzer {
    private double normalMax;
    private double normalMin;

    private double scoreMax;
    private double scoreMin;

    @Override
    public void init(FilterProperties filterProperties) {
        super.init(filterProperties);
        JSONObject params = (JSONObject) filterProperties.getParams();
        if (params == null) {
            throw new IllegalStateException("params of ShopDepositFilterAnalyzer can not be null or empty");
        }
        // normal max
        {
            scoreMax = params.getDouble(ScoreConst.NORMALIZE_FILTER_SCORE_MAX);
            if (scoreMax == 0) {
                throw new IllegalStateException("Score max can`t be 0");
            }
        }
        // normal min
        {
            scoreMin = params.getDouble(ScoreConst.NORMALIZE_FILTER_SCORE_MIN);
            if (scoreMin == 0) {
                throw new IllegalStateException("Score min can`t be 0");
            }
        }
        // score max
        {
            normalMax = params.getDouble(ScoreConst.NORMALIZE_FILTER_NORMAL_MAX);
        }
        // score min
        {
            normalMin = params.getDouble(ScoreConst.NORMALIZE_FILTER_NORMAL_MIN);
        }
    }

    @Override
    public double analyze(SourceLookup source, LeafDocLookup doc) {
        double score = super.analyze(source, doc);
        // range
        if (score > scoreMax) {
            score = scoreMax;
        } else if (score < scoreMin) {
            score = scoreMin;
        }
        // normalize
        if (score > 0) {
            score = score * normalMax / scoreMax;
        } else if (score < 0) {
            score = score * normalMin / scoreMin;
        }
        return score;
    }
}
