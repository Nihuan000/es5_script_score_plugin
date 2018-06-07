package com.soubu.search.analyzer;

import com.soubu.search.base.ScoreAnalyzer;
import com.soubu.search.score.Entry;
import com.soubu.search.score.ScoreProperties;
import com.soubu.search.score.ScoreType;

/**
 * 阶段性给予得分
 * Created by Nihuan on 06/06/18.
 * Bigger的参数类型
 * 100, 75, 50, 25, 0
 * 10, 8, 5, 3, 1
 *
 *
 */
public class NumberFieldScoreAnalyzer implements ScoreAnalyzer {
    private Compare compare;
    private String field;
    private double[] values;
    private double[] scores;

    @Override
    public void init(ScoreProperties scoreProperties) {
        // compare
        if (scoreProperties.getType() == ScoreType.BIGGER) {
            compare = Compare.BIGGER;
        } else if (scoreProperties.getType() == ScoreType.SMALLER) {
            compare = Compare.SMALLER;
        } else {
            throw new IllegalStateException("NumberFieldScoreAnalyzer not support type: " + scoreProperties.getType());
        }
        // field
        if (scoreProperties.getField() != null && !scoreProperties.getField().equals("")) {
            this.field = scoreProperties.getField();
        } else {
            throw new IllegalStateException("NumberFieldScoreAnalyzer field can`t be null or empty");
        }
        // entry List
        if (scoreProperties.getParam() != null && scoreProperties.getParam().length > 0) {
            values = new double[scoreProperties.getParam().length];
            scores = new double[values.length];
            for (int i = 0; i < values.length; i++) {
                Entry entry = scoreProperties.getParam()[i];
                values[i] = Double.valueOf(entry.getKey());
                scores[i] = Double.valueOf(entry.getValue());
            }
        } else {
            throw new IllegalStateException("NumberFieldScoreAnalyzer param entry list can`t be null or empry");
        }

    }

    @Override
    public double analyze(double fieldValue) {
        // values 从大到小排列, 如果fieldValue比最大值小, 则返回默认值
        if (compare == Compare.BIGGER) {
            if (Double.compare(fieldValue, values[values.length - 1]) < 0) {
                return 0;
            }
        } else if (compare == Compare.SMALLER) {
            if (Double.compare(fieldValue, values[values.length - 1]) > 0) {
                return 0;
            }
        }

        for (int i = 0; i < values.length; i++) {
            if (compare.compare(fieldValue, values[i])) {
                return scores[i];
            }
        }
        return 0;
    }

    enum Compare {
        BIGGER() {
            @Override
            public boolean compare(double first, double second) {
                return Double.compare(first, second) >= 0;
            }
        },
        SMALLER() {
            @Override
            public boolean compare(double first, double second) {
                return Double.compare(first, second) <= 0;
            }
        };

        public abstract boolean compare(double first, double second);
    }

    public String field() {
        return field;
    }
}
