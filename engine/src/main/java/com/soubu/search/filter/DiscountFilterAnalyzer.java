package com.soubu.search.filter;

import com.alibaba.fastjson.JSONObject;
import com.soubu.search.score.FilterProperties;
import com.soubu.search.score.ScoreConst;
import org.elasticsearch.search.lookup.LeafDocLookup;
import org.elasticsearch.search.lookup.SourceLookup;

public class DiscountFilterAnalyzer extends BaseFilterAnalyzer {

    private double discount;

    @Override
    public void init(FilterProperties filterProperties) {
        super.init(filterProperties);
        JSONObject params = (JSONObject) filterProperties.getParams();
        if (params == null) {
            throw new IllegalStateException("params of DiscountFilterAnalyzer can not be null or empty");
        }
        Double discount = params.getDouble(ScoreConst.DEPOSIT_PRODUCT_DISCOUNT);
        if (discount == null || discount <= 0) {
            throw new IllegalStateException("discount of DiscountFilterAnalyzer can not be null and should bigger than 0");
        }
        this.discount = discount;
    }

    @Override
    public double analyze(SourceLookup source, LeafDocLookup doc) {
        double score = super.analyze(source, doc);
        score *= discount;
        return score;
    }
}
