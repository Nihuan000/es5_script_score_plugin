package com.soubu.search.filter;

import com.soubu.search.analyzer.ScoreAnalyzerFactoryImpl;
import com.soubu.search.base.FilterAnalyzer;
import com.soubu.search.base.FilterAnalyzerFactory;
import com.soubu.search.base.ScoreAnalyzer;
import com.soubu.search.base.ScoreAnalyzerFactory;
import com.soubu.search.score.FilterProperties;
import com.soubu.search.score.ScoreProperties;
import org.elasticsearch.index.fielddata.ScriptDocValues;
import org.elasticsearch.search.lookup.LeafDocLookup;
import org.elasticsearch.search.lookup.SourceLookup;

import java.util.ArrayList;
import java.util.List;

/**
 *  基本过滤分析
 * Created by Nihuan on 06/06/18.
 */
public class BaseFilterAnalyzer implements FilterAnalyzer {

    protected FilterAnalyzer next;
    protected ScoreAnalyzer[] scoreAnalyzers;

    private ScoreAnalyzerFactory scoreAnalyzerFactory = ScoreAnalyzerFactoryImpl.getInstance();
    private FilterAnalyzerFactory filterAnalyzerFactory = FilterAnalyzerFactoryImpl.getInstance();

    @Override
    public void init(FilterProperties filterProperties) {
        List<ScoreProperties> scoreProps = filterProperties.getScoreProps();
        if (scoreProps == null) {
            scoreProps = new ArrayList<>(0);
        }
        scoreAnalyzers = new ScoreAnalyzer[scoreProps.size()];
        for (int i = 0; i < scoreAnalyzers.length; i++) {
            ScoreProperties scoreProp = scoreProps.get(i);
            ScoreAnalyzer analyzer = scoreAnalyzerFactory.create(scoreProp);
            scoreAnalyzers[i] = analyzer;
        }
        if (filterProperties.getNext() != null) {
            next = filterAnalyzerFactory.create(filterProperties.getNext());
        }
    }

    @Override
    public double analyze(SourceLookup source, LeafDocLookup doc) {
        double score = 0;
        for (ScoreAnalyzer analyzer : scoreAnalyzers) {
            Object valueObj = doc.get(analyzer.field());
            if (valueObj != null) {
                if (ScriptDocValues.Longs.class.isInstance(valueObj)) {
                    score += analyzer.analyze(((ScriptDocValues.Longs) valueObj).getValue());
                } else if (ScriptDocValues.Doubles.class.isInstance(valueObj)) {
                    score += analyzer.analyze(((ScriptDocValues.Doubles) valueObj).getValue());
                } else {
                    throw new IllegalStateException("Field[" + analyzer.field() + "] is not number field");
                }
            }
        }
        if (next != null) {
            score = nextAnalyzer(source, doc, score);
        }
        return score;
    }

    protected double nextAnalyzer(SourceLookup source, LeafDocLookup doc, double score) {
        score += next.analyze(source, doc);
        return score;
    }
}
