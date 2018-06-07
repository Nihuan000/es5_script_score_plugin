package com.soubu.search.analyzer;

import com.soubu.search.base.ScoreAnalyzer;
import com.soubu.search.score.Entry;
import com.soubu.search.score.ScoreProperties;
import com.soubu.search.score.ScoreType;

/**
 * 根据日期时间倒序给予得分, 分段函数
 * Created by Nihuan on 06/06/18.
 */
public class DateDecayScoreAnalyzer implements ScoreAnalyzer {

    private String field;
    private double[] paramA;
    private double[] paramB;
    private long[] seconds;
    private long nowSecond;

    @Override
    public void init(ScoreProperties scoreProperties) {
        reset(scoreProperties);
    }

    private void reset(ScoreProperties scoreProperties) {
        nowSecond = System.currentTimeMillis() / 1000;
        if (scoreProperties.getType() != ScoreType.DATE) {
            throw new IllegalStateException("DateDecayScoreAnalyzer not support type: " + scoreProperties.getType());
        }
        // field
        if (scoreProperties.getField() != null && !scoreProperties.getField().equals("")) {
            this.field = scoreProperties.getField();
        } else {
            throw new IllegalStateException("DateDecayScoreAnalyzer field can`t be null or empty");
        }
        // compute date piecewise function
        if (scoreProperties.getParam() != null && scoreProperties.getParam().length > 0) {
            seconds = new long[scoreProperties.getParam().length];
            paramA = new double[seconds.length + 1];
            paramB = new double[paramA.length];
            // first
            paramA[0] = 0;
            double preScore = Double.valueOf(scoreProperties.getParam()[0].getValue());
            paramB[0] = preScore;
            // not first
            for (int i = 1; i < seconds.length; i++) {
                Entry entry = scoreProperties.getParam()[i];
                seconds[i] = Long.valueOf(entry.getKey());
                double score = Double.valueOf(entry.getValue());
                computeSimpleFunction(preScore, score, seconds[i-1], seconds[i], i);
                preScore = score;
            }
            // after all
            // a = (x1 - x2) / (y2 - y1) * y1 * y2
            // b = (a / y1) - x1
            double y1 = preScore;
            double y2 = preScore * 0.9;
            double x1 = seconds[seconds.length - 1];
            double x2 = x1 * 2;
            paramA[paramA.length - 1] = (x1 - x2) / (y2 - y1) * y1 * y2;
            paramB[paramB.length - 1] = (paramA[paramA.length - 1] / y1) - x1;
        } else {
            throw new IllegalStateException("DateDecayScoreAnalyzer param entry list can`t be null or empty");
        }
    }

    private void computeSimpleFunction(double y1, double y2, double x1, double x2, int i) {
        paramA[i] = (y1 - y2) / (x1 - x2);
        paramB[i] = y1 - x1 * paramA[i];
    }

    private double computerSimple(double a, double b, double x) {
        return a * x + b;
    }

    private double computeDecay(double a, double b, double x) {
        return a / (x + b);
    }

    @Override
    public String field() {
        return field;
    }

    @Override
    public double analyze(double fieldValue) {
//        if (fieldValue > 1486619840) {
//            fieldValue++;
//        }
        fieldValue = nowSecond - fieldValue;
        for (int i = 0; i < seconds.length; i++) {
            if (fieldValue < seconds[i]) {
                double score = computerSimple(paramA[i], paramB[i], fieldValue);
                return score;
            }
        }
        double score = computeDecay(paramA[paramA.length - 1], paramB[paramB.length - 1], fieldValue);
        return score;
    }
}
