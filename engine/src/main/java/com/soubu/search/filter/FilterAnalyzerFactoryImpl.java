package com.soubu.search.filter;

import com.soubu.search.base.FilterAnalyzer;
import com.soubu.search.base.FilterAnalyzerFactory;
import com.soubu.search.score.FilterProperties;
import com.soubu.search.score.FilterType;

public class FilterAnalyzerFactoryImpl implements FilterAnalyzerFactory {

    private static FilterAnalyzerFactoryImpl instance = new FilterAnalyzerFactoryImpl();
    private FilterAnalyzerFactoryImpl() {}

    public static FilterAnalyzerFactoryImpl getInstance() {
        return instance;
    }

    @Override
    public FilterAnalyzer create(FilterProperties filterProperties) {
        FilterAnalyzer analyzer;
        if (filterProperties.getType() == FilterType.FILTER_BASE) {
            analyzer = new BaseFilterAnalyzer();
        } else if (filterProperties.getType() == FilterType.FILTER_SHOP_DEPOSIT) {
            analyzer = new ShopDepositFilterAnalyzer();
        } else if (filterProperties.getType() == FilterType.FILTER_DISCOUNT) {
            analyzer = new DiscountFilterAnalyzer();
        } else if (filterProperties.getType() == FilterType.FILTER_SCORE_NORMALIZE) {
            analyzer = new ScoreNormalizeFilterAnalyzer();
        } else if (filterProperties.getType() == FilterType.FILTER_SCORE_RANGE) {
            analyzer = new ScoreRangeFilterAnalyzer();
        } else {
            throw new IllegalStateException("Unknow type: " + filterProperties.getType());
        }
        analyzer.init(filterProperties);
        return analyzer;
    }

}
