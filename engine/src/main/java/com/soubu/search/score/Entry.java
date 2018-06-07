package com.soubu.search.score;

import java.io.Serializable;

public class Entry implements Serializable {
    private static final long serialVersionUID = -6341756041228396874L;
    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entry)) return false;

        Entry entry = (Entry) o;

        if (getKey() != null ? !getKey().equals(entry.getKey()) : entry.getKey() != null) return false;
        return getValue() != null ? getValue().equals(entry.getValue()) : entry.getValue() == null;

    }

    @Override
    public int hashCode() {
        int result = getKey() != null ? getKey().hashCode() : 0;
        result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
        return result;
    }
}