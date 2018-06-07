package com.soubu.search.score;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class FilterProperties implements Serializable {
    private static final long serialVersionUID = -9136817735351008405L;
    private FilterType type;
    private List<ScoreProperties> scoreProps;
    private FilterProperties next;
    private Map<String, Object> params;

    public FilterType getType() {
        return type;
    }

    public void setType(FilterType type) {
        this.type = type;
    }

    public List<ScoreProperties> getScoreProps() {
        return scoreProps;
    }

    public void setScoreProps(List<ScoreProperties> scoreProps) {
        this.scoreProps = scoreProps;
    }

    public FilterProperties getNext() {
        return next;
    }

    public void setNext(FilterProperties next) {
        this.next = next;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
