package com.ziyun.parser2;


public class DSLTester {
    public static void main(String[] args) {
        String dslQuery = "Select uid, sum(money) from orders.orders where uid=#{user_id} AND day=#{date} group by uid Return uid, moneys   ;";
        Query query = DSLParser.parse(dslQuery);
        System.out.println(query);
    }
}
