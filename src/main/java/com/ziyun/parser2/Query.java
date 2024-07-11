package com.ziyun.parser2;

public class Query {
    private String selectClause;
    private String fromClause;
    private String whereClause;
    private String groupByClause;
    private String returnClause;

    // Constructors
    public Query() { }

    // Getters and Setters
    public String getSelectClause() {
        return selectClause;
    }

    public void setSelectClause(String selectClause) {
        this.selectClause = selectClause;
    }

    public String getFromClause() {
        return fromClause;
    }

    public void setFromClause(String fromClause) {
        this.fromClause = fromClause;
    }

    public String getWhereClause() {
        return whereClause;
    }

    public void setWhereClause(String whereClause) {
        this.whereClause = whereClause;
    }

    public String getGroupByClause() {
        return groupByClause;
    }

    public void setGroupByClause(String groupByClause) {
        this.groupByClause = groupByClause;
    }

    public String getReturnClause() {
        return returnClause;
    }

    public void setReturnClause(String returnClause) {
        this.returnClause = returnClause;
    }

    // Optional: toString() Method for easy printing
    @Override
    public String toString() {
        return "Query{" +
                "selectClause='" + selectClause + '\'' +
                ", fromClause='" + fromClause + '\'' +
                ", whereClause='" + whereClause + '\'' +
                ", groupByClause='" + groupByClause + '\'' +
                ", returnClause='" + returnClause + '\'' +
                '}';
    }
}
