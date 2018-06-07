package com.soubu.search.analyzer;

import com.soubu.search.analyzer.util.MathUtils;
import com.soubu.search.base.ScoreAnalyzer;
import com.soubu.search.score.Entry;
import com.soubu.search.score.ScoreProperties;
import com.soubu.search.score.ScoreType;

/**
 * 分段一元一次函数, 2点之间计算方程 y=ax+b
 *  Created by Nihuan on 06/06/18.
 */
public class SegUnaryScoreAnalyzer implements ScoreAnalyzer {

    private String field;

    private double[] paramsA;
    private double[] paramsB;

    private double[] keys;
    private double leftValue;
    private double rightValue;

    @Override
    public void init(ScoreProperties scoreProperties) {
        if (scoreProperties.getType() != ScoreType.SEG_UNARY) {
            throw new IllegalStateException("SegUnaryScoreAnalyzer don`t support type: " + scoreProperties.getType());
        }
        // field
        if (scoreProperties.getField() != null && !scoreProperties.getField().equals("")) {
            this.field = scoreProperties.getField();
        } else {
            throw new IllegalStateException("SegUnaryScoreAnalyzer field can`t be null or empty");
        }
        // params
        if (scoreProperties.getParam() != null && scoreProperties.getParam().length > 0) {
            Entry[] params = scoreProperties.getParam();
            keys = new double[params.length];
            paramsA = new double[params.length - 1];
            paramsB = new double[paramsA.length];
            double preScore = Double.valueOf(params[0].getValue());
            // left value
            leftValue = preScore;
            keys[0] = Double.valueOf(params[0].getKey());
            for (int i = 1; i < params.length; i++) {
                keys[i] = Double.valueOf(params[i].getKey());
                double score = Double.valueOf(params[i].getValue());
                if (Double.compare(keys[i], keys[i-1]) == 0) {
                    paramsA[i - 1] = 0;
                    paramsB[i - 1] = preScore;
                } else {
                    MathUtils.analyzeUnaryFunction(keys[i - 1],
                            preScore,
                            keys[i],
                            score,
                            i - 1,
                            paramsA,
                            paramsB);
                }
                preScore = score;
            }
            // right value
            rightValue = preScore;
        } else {
            throw new IllegalStateException("SegUnaryScoreAnalyzer param entry list can`t be null or empty");
        }
    }

    @Override
    public String field() {
        return field;
    }

    @Override
    public double analyze(double fieldValue) {
        if (fieldValue < keys[0]) {
            return leftValue;
        } else {
            for (int i = 1; i < keys.length; i++) {
                if (fieldValue < keys[i]) {
                    // ax+b
                    return paramsA[i - 1] * fieldValue + paramsB[i - 1];
                }
            }
        }
        return rightValue;
    }
}
