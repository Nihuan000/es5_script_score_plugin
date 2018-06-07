package com.soubu.search.base;

import com.soubu.search.score.FilterProperties;
import org.elasticsearch.search.lookup.DocLookup;
import org.elasticsearch.search.lookup.LeafDocLookup;
import org.elasticsearch.search.lookup.SourceLookup;

/**
 * generic the analyze of giving ScoreProperties
 * Created by Nihuan on 06/06/18.
 */
public interface FilterAnalyzer {
    void init(FilterProperties scoreProperties);
    double analyze(SourceLookup source, LeafDocLookup doc);
}
