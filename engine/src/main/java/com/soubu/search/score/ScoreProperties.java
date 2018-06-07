package com.soubu.search.score;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 用于存放打分配置信息
 */
public class ScoreProperties implements Serializable {
    private static final long serialVersionUID = -7008326334879619744L;
    private ScoreType type;
    private String field;
    private Entry[] param;

    public ScoreType getType() {
        return type;
    }

    public void setType(ScoreType type) {
        this.type = type;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Entry[] getParam() {
        return param;
    }

    public void setParam(Entry[] param) {
        this.param = param;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScoreProperties)) return false;

        ScoreProperties that = (ScoreProperties) o;

        if (getType() != that.getType()) return false;
        if (getField() != null ? !getField().equals(that.getField()) : that.getField() != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(getParam(), that.getParam());

    }

    @Override
    public int hashCode() {
        int result = getType() != null ? getType().hashCode() : 0;
        result = 31 * result + (getField() != null ? getField().hashCode() : 0);
        result = 31 * result + Arrays.hashCode(getParam());
        return result;
    }


}
