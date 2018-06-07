package com.soubu.search.base;

import com.soubu.search.score.ScoreProperties;

/**
 * generic the analyze of giving ScoreProperties
 * Created by Nihuan on 06/06/18.
 */
public interface ScoreAnalyzerFactory {
    ScoreAnalyzer create(ScoreProperties scoreProperties);
}
