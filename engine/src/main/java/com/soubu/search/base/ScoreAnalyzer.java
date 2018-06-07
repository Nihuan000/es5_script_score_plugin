package com.soubu.search.base;

import com.soubu.search.score.ScoreProperties;

/**
 * Deal with different ScoreProperties, compute the result
 * Created by Nihuan on 06/06/18.
 */
public interface ScoreAnalyzer {
    void init(ScoreProperties scoreProperties);
    String field();
    double analyze(double fieldValue);
}
