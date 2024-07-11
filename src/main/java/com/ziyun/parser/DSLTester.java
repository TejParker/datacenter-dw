package com.ziyun.parser;

public class DSLTester {
    public static void main(String[] args) {
        String dslQuery = "Select uid, sum(money) from orders.orders where uid=#{user_id} and day=#{date} group by uid Return uid, moneys;";
        Query query = DSLParser.parse(dslQuery);
        System.out.println(query);
    }
}
