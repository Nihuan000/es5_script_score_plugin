package com.soubu.search.base;

import com.soubu.search.score.FilterProperties;

/**
 * generic the analyze of giving ScoreProperties
 * Created by Nihuan on 06/06/18.
 */

public interface FilterAnalyzerFactory {
    FilterAnalyzer create(FilterProperties filterProperties);
}
