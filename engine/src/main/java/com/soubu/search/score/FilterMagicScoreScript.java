package com.soubu.search.score;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.soubu.search.base.FilterAnalyzer;
import com.soubu.search.base.FilterAnalyzerFactory;
import com.soubu.search.filter.FilterAnalyzerFactoryImpl;
import com.soubu.search.score.FilterProperties;
import com.soubu.search.score.FilterType;
import com.soubu.search.score.ScoreConst;
import com.soubu.search.score.ScoreProperties;
import org.elasticsearch.script.AbstractDoubleSearchScript;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FilterMagicScoreScript extends AbstractDoubleSearchScript {

    private double base = 100d;
    private FilterAnalyzer filterAnalyzer;

    private FilterAnalyzerFactory filterAnalyzerFactory = FilterAnalyzerFactoryImpl.getInstance();

    public FilterMagicScoreScript(Map<String, Object> param) {
        reset(param);
    }

    public void reset(Map<String, Object> params) {
        // base
        Object baseObj = params.get(ScoreConst.BASE_KEY);
        if (baseObj != null) {
            if (Number.class.isInstance(baseObj)) {
                base = ((Number) baseObj).doubleValue();
            } else {
                base = Double.valueOf(baseObj.toString());
            }
        }
        String magic = (String) params.get(ScoreConst.MAGIC_KEY);
        JSONObject propsObj = JSON.parseObject(magic);
        FilterProperties filterProp = analyzeProps(propsObj);

        this.filterAnalyzer = filterAnalyzerFactory.create(filterProp);
    }

    FilterProperties analyzeProps(JSONObject propsObj) {
        FilterProperties props = new FilterProperties();
        // type
        props.setType(FilterType.valueOf(propsObj.getString("type")));

        // params
        if (propsObj.containsKey("params") && propsObj.get("params") != null) {
            props.setParams(propsObj.getJSONObject("params"));
        }

        // scoreProps
        if (propsObj.containsKey("scoreProps") && propsObj.get("scoreProps") != null) {
            JSONArray scoreProps = propsObj.getJSONArray("scoreProps");
            ScoreProperties[] propArr = new ScoreProperties[scoreProps.size()];
            for (int i = 0; i < scoreProps.size(); i++) {
                JSONObject propObj = scoreProps.getJSONObject(i);
                ScoreProperties prop = MagicScoreScript.analyzeScoreProps(propObj);
                propArr[i] = prop;
            }
            props.setScoreProps(Arrays.asList(propArr));
        }

        // next
        if (propsObj.containsKey("next") && propsObj.get("next") != null) {
            props.setNext(analyzeProps(propsObj.getJSONObject("next")));
        }
        return props;
    }

    @Override
    public double runAsDouble() {
        double finalScore = filterAnalyzer.analyze(source(), doc());
//        return finalScore + 100;
        return (finalScore / base) + 1;
    }
}
