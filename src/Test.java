import Generator.Query;

public class Test {

    public static void main(String args[]){
        Query query1 = new Query().addColumn("c.country_name").addTable("PERSON","p")
                .addTable("COUNTRY","c").addWhereCondition("c.id","p.country_id","=")
                .addGroupBy("c.country_name").addOrderBy("c.country_name", "DESC");

        System.out.println(query1.generate());

        Query query2 = new Query().addColumn("*")
                .addTable(new Query().addColumn("a").addTable("t").addWhereCondition("a","10","<").generate(),"tx",true)
                .addWhereCondition("tx.a","10",">");

        //select * from (select a from t where a < 10) tx where tx.a > 10
        System.out.println(query2.generate());


    }




}
