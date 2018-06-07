package com.soubu.search.analyzer;

import com.soubu.search.base.ScoreAnalyzer;
import com.soubu.search.score.Entry;
import com.soubu.search.score.ScoreProperties;
import com.soubu.search.score.ScoreType;

/**
 * 对于指定的key, 给予对应的value
 *  Created by Nihuan on 06/06/18.
 */
public class SpecificScoreAnalyzer implements ScoreAnalyzer {

    private String field;
    private double[] keys;
    private double[] values;

    @Override
    public void init(ScoreProperties scoreProperties) {
        if (scoreProperties.getType() != ScoreType.SPECIFIC) {
            throw new IllegalStateException("SpecificScoreAnalyzer unsupport type: " + scoreProperties.getType());
        }
        this.field = scoreProperties.getField();
        if (field == null || field.equals("")) {
            throw new IllegalStateException("SpecificScoreAnalyzer field is null");
        }
        Entry[] entries = scoreProperties.getParam();
        if (entries == null || entries.length == 0) {
            throw new IllegalStateException("SpecificScoreAnalyzer entrys is null");
        }
        keys = new double[entries.length];
        values = new double[entries.length];
        for (int i = 0; i < entries.length; i++) {
            keys[i] = Double.valueOf(entries[i].getKey());
            values[i] = Double.valueOf(entries[i].getValue());
        }
    }

    @Override
    public String field() {
        return field;
    }

    @Override
    public double analyze(double fieldValue) {
        for (int i = 0; i < keys.length; i++) {
            if (Double.compare(fieldValue, keys[i]) == 0) {
                return values[i];
            }
        }
        return 0;
    }
}
