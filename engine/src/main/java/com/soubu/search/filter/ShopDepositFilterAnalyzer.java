package com.soubu.search.filter;

import org.elasticsearch.index.fielddata.ScriptDocValues;
import org.elasticsearch.search.lookup.LeafDocLookup;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.soubu.search.score.FilterProperties;
import com.soubu.search.score.ScoreConst;
import org.elasticsearch.search.lookup.SourceLookup;

public class ShopDepositFilterAnalyzer extends BaseFilterAnalyzer {
    private double threshold;
    private String depositTimeField = "deposit_time";
    private int[] keys;
    private int[] values;

    private long currentSecond;

    @Override
    public void init(FilterProperties filterProperties) {
        super.init(filterProperties);
        JSONObject params = (JSONObject) filterProperties.getParams();
        if (params == null) {
            throw new IllegalStateException("params of ShopDepositFilterAnalyzer can not be null or empty");
        }
        // field
        String field = params.getString(ScoreConst.SHOP_DEPOSIT_TIME_FIELD);
        if (field != null) {
            depositTimeField = field;
        }
        // threshold
        this.threshold = params.getIntValue(ScoreConst.SHOP_THRESHOLD);
        
        // entries
        JSONArray entryArray = params.getJSONArray(ScoreConst.SHOP_ENTRIES);
        if (entryArray == null || entryArray.isEmpty()) {
            throw new IllegalStateException("Entry[] entryArray of ShopDepositFilterAnalyzer can not be null or empty");
        }
        keys = new int[entryArray.size()];
        values = new int[keys.length];
        int preKey = -1;
        int preValue = 101;
        for (int i = 0; i < keys.length; i++) {
            JSONObject entry = entryArray.getJSONObject(i);
            keys[i] = entry.getIntValue("key");
            if (keys[i] <= preKey) {
                throw new IllegalStateException("Keys of ShopDepositFilterAnalyzer must be bigger and bigger");
            }
            preKey = keys[i];
            values[i] = entry.getIntValue("value");
            if (values[i] > 100 || values[i] < 0 || values[i] > preValue) {
                throw new IllegalStateException("values of ShopDepositFilterAnalyzer must be smaller and smaller and between 0 - 100");
            }
        }
        reset();
    }

    public void reset() {
        currentSecond = System.currentTimeMillis() / 1000;
    }

    @Override
    public double analyze(SourceLookup source, LeafDocLookup doc) {
        double score = super.analyze(source, doc);
        long deposit = ((ScriptDocValues.Longs) doc.get("deposit")).getValue();
        if (deposit == 0) {
            return score;
        }
        if (score >= threshold) {
            return score;
        }
        long depositTime = ((ScriptDocValues.Longs) doc.get(depositTimeField)).getValue();
        long pastTime = currentSecond - depositTime;
        int value = 0;
        for (int i = 0; i < keys.length; i++) {
            if (pastTime <= keys[i]) {
                value = values[i];
                break;
            }
        }
        return (threshold * value / 100) + ((100 - value) * score / 100);
    }

}
