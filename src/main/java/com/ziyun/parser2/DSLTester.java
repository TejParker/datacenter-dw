package com.ziyun.parser2;


public class DSLTester {
    public static void main(String[] args) {
        String dslQuery = "Select machine_id, sum(energy) from orders.orders where machine_id=#{user_id} AND day=#{date} group by machine_id Return machine_id, energy_consumption   ;";
        Query query = DSLParser.parse(dslQuery);
        System.out.println(query);
    }
}
