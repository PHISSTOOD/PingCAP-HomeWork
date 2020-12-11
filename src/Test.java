import Generator.Query;

public class Test {

    public static void main(String args[]){
        Query query1 = new Query().addColumn("c.country_name").addTable("PERSON","p")
                .addTable("COUNTRY","c").addWhereCondition("c.id","p.country_id","=")
                .addGroupBy("c.country_name").addOrderBy("c.country_name", "DESC");

        System.out.println(query1.generate());
        System.out.println();

        Query query2 = new Query().addColumn("*")
                .addTable(new Query().addColumn("a").addTable("t").addWhereCondition("a","10","<").generate(),"tx",true)
                .addWhereCondition("tx.a","10",">");

        //select * from (select a from t where a < 10) tx where tx.a > 10
        System.out.println(query2.generate());
        System.out.println();

        Query query3 = new Query().addColumn("E1.ENAME").addColumn("E1.HIREDATE").addColumn("E2.ENAME").addColumn("E2.HIREDATE")
                .addTable("EMP","E1").addJoin("LEFT JOIN","EMP","E2","E1.MGR","E2.EMPNO","=")
                .addWhereCondition("E1.HIREDATE","E2.HIREDATE","<");

        //SELECT E1.ENAME,E1.HIREDATE,E2.ENAME,E2.HIREDATE
        //FROM EMP E1
        //LEFT JOIN EMP E2
        //ON E1.MGR = E2.EMPNO
        //WHERE E1.HIREDATE <E2.HIREDATE;

        System.out.println(query3.generate());
        System.out.println();

        Query query4 = new Query().addColumn("D.DNAME").addColumn("E.EMPNO").addColumn("E.ENAME").addColumn("DEPTNO")
                .addTable("EMP","E").addJoin("RIGHT JOIN","DEPT","D","DEPTNO");
        //SELECT D.DNAME,E.EMPNO,E.ENAME,DEPTNO
        //FROM EMP E
        //RIGHT JOIN DEPT D
        //USING(DEPTNO);

        System.out.println(query4.generate());
        System.out.println();
    }




}
