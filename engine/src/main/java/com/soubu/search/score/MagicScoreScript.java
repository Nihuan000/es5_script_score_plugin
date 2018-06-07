package com.soubu.search.score;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.soubu.search.analyzer.ScoreAnalyzerFactoryImpl;
import com.soubu.search.base.ScoreAnalyzer;
import com.soubu.search.base.ScoreAnalyzerFactory;
import com.soubu.search.score.Entry;
import com.soubu.search.score.ScoreConst;
import com.soubu.search.score.ScoreProperties;
import com.soubu.search.score.ScoreType;
import org.elasticsearch.index.fielddata.ScriptDocValues;
import org.elasticsearch.script.AbstractDoubleSearchScript;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * use List&lt;ScoreProperties&gt; to deal with score <br><br/>
 * Created by wuchao on 10/02/17.
 */
public class MagicScoreScript extends AbstractDoubleSearchScript {

    private double base = 100d;

    private ScoreAnalyzerFactory analyzerFactory = ScoreAnalyzerFactoryImpl.getInstance();
    private List<ScoreAnalyzer> scoreAnalyzers;

    public MagicScoreScript(Map<String, Object> params) {
        if (params == null || params.isEmpty() || !params.containsKey(ScoreConst.MAGIC_KEY)) {
            throw new IllegalStateException("MagicScoreScript params can`t be null");
        }
        reset(params);
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
        // magic params
        String magic = (String) params.get(ScoreConst.MAGIC_KEY);
        JSONArray props = JSON.parseArray(magic);
        if (scoreAnalyzers == null) {
            scoreAnalyzers = new ArrayList<>(props.size() + 1);
        } else {
            scoreAnalyzers.clear();
        }
        for (int i = 0; i < props.size(); i++) {
            JSONObject prop = props.getJSONObject(i);
            ScoreProperties scoreProperties = analyzeScoreProps(prop);
            ScoreAnalyzer analyzer = analyzerFactory.create(scoreProperties);
            scoreAnalyzers.add(analyzer);
        }
    }

    public static ScoreProperties analyzeScoreProps(JSONObject prop) {
        ScoreProperties scoreProperties = new ScoreProperties();
        scoreProperties.setType(ScoreType.valueOf(prop.getString("type")));
        scoreProperties.setField(prop.getString("field"));
        JSONArray entrys = prop.getJSONArray("param");
        Entry[] param = new Entry[entrys.size()];
        scoreProperties.setParam(param);
        for (int ii = 0; ii < entrys.size(); ii++) {
            JSONObject entry = entrys.getJSONObject(ii);
            Entry et = new Entry();
            et.setKey(entry.getString("key"));
            et.setValue(entry.getString("value"));
            param[ii] = et;
        }
        return scoreProperties;
    }

    @Override
    public double runAsDouble() {
//        if (((ScriptDocValues.Longs)doc().get("add_time")).getValue() == 1487125473) {
//            int i = 0;
//        }
        double finalScore = 0;
        for (ScoreAnalyzer analyzer : scoreAnalyzers) {
            Object valueObj = doc().get(analyzer.field());
            if (valueObj != null) {
                if (ScriptDocValues.Longs.class.isInstance(valueObj)) {
                    finalScore += analyzer.analyze(((ScriptDocValues.Longs) valueObj).getValue());
                } else if (ScriptDocValues.Doubles.class.isInstance(valueObj)) {
                    finalScore += analyzer.analyze(((ScriptDocValues.Doubles) valueObj).getValue());
                } else {
                    throw new IllegalStateException("Field[" + analyzer.field() + "] is not number field");
                }
            }
        }
        if (finalScore > 280) {
            int iii = 0;
        }
        return (finalScore / base) + 1;
    }

}
