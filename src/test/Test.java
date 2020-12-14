package test;

import Distribute.Executor;
import Generator.Query;
import test.Init.CreateQuery;

import java.sql.*;
import java.util.Scanner;


public class Test {


    public static void main(String args[]) throws SQLException {
        Query query1 = new Query().addColumn("c.country_name").addTable("PERSON","p")
                .addTable("COUNTRY","c").addWhereCondition("c.id","p.country_id","=")
                .addGroupBy("c.country_name").addOrderBy("c.country_name", "DESC");

        System.out.println(query1.generate());
        System.out.println();

        Query query2 = new Query().addColumn("*")
                .addTable(new Query().addColumn("a").addTable("t").addWhereCondition("a","10","<").generate(),false,"tx",true)
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

        Scanner input=new Scanner(System.in);
        int distribute = 0;
        while(true){
            System.out.println();
            System.out.println("type if you want to test it as a distribute data base or MySQL:");
            System.out.println("1 means distribute data base, 2 means only generate SQL for MySQL, 3 to exit");
            distribute = input.nextInt();

            if(distribute==1){
                Query queryUser = new Query();
                queryUser = CreateQuery.createQuery(queryUser);
                Executor executor = new Executor();
                System.out.println(executor.selectExecutor(queryUser));
            }else if(distribute==2){
                Query queryUser = new Query();
                queryUser = CreateQuery.createQuery(queryUser);
                System.out.println(queryUser.generate());
            }else{
                System.out.println("Incorrect input");
            }
        }

    }

}
