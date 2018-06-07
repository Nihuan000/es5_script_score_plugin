package com.soubu.search.analyzer;

import com.soubu.search.base.ScoreAnalyzer;
import com.soubu.search.base.ScoreAnalyzerFactory;
import com.soubu.search.score.ScoreProperties;
import com.soubu.search.score.ScoreType;

/**
 *  分数计算入口类
 *  Created by Nihuan on 06/06/18.
 */
public class ScoreAnalyzerFactoryImpl implements ScoreAnalyzerFactory {

    private static ScoreAnalyzerFactoryImpl instance = new ScoreAnalyzerFactoryImpl();

    private ScoreAnalyzerFactoryImpl() {}

    public static ScoreAnalyzerFactoryImpl getInstance() {
        return instance;
    }

    @Override
    public ScoreAnalyzer create(ScoreProperties scoreProperties) {
        ScoreAnalyzer analyzer;
        if (scoreProperties.getType() == ScoreType.BIGGER || scoreProperties.getType() == ScoreType.SMALLER) {
            analyzer = new NumberFieldScoreAnalyzer();
        } else if (scoreProperties.getType() == ScoreType.DATE) {
            analyzer = new DateDecayScoreAnalyzer();
        } else if (scoreProperties.getType() == ScoreType.SPECIFIC) {
            analyzer = new SpecificScoreAnalyzer();
        } else if (scoreProperties.getType() == ScoreType.SEG_UNARY) {
            analyzer = new SegUnaryScoreAnalyzer();
        } else {
            throw new IllegalStateException("Unknown score type: " + scoreProperties.getType());
        }
        analyzer.init(scoreProperties);
        return analyzer;
    }
}

