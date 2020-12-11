package Generator;


import java.util.ArrayList;
import java.util.List;

public class Query {

    private String sql;
    private boolean distinct;
    private List<Column> columns;
    private List<Table> tables;
    private List<Join> joins;
    private List<Condition> whereConditions;
    private Group group;
    private List<Order> orderBy;
    private int limit;
    private static final String NEW_LINE = System.getProperty("line.separator");


    public Query() {
        this.columns = new ArrayList();
        this.tables = new ArrayList();
        this.joins = new ArrayList();
        this.whereConditions = new ArrayList();
        this.group = new Group();
        this.orderBy = new ArrayList();
        this.limit = -1;
        this.sql = "";

    }

    public Query addColumn(String columnName){
        Column column = new Column(columnName, false, null);
        columns.add(column);
        return this;
    }

    public Query addColumn(String columnName, String columnCode){
        Column column = new Column(columnName, false, columnCode);
        columns.add(column);
        return this;
    }

    public Query addColumn(String columnName, boolean as, String columnCode){
        Column column = new Column(columnName, as, columnCode);
        columns.add(column);
        return this;
    }

    public Query addTable(String tableName, String tableCode, boolean isGeneratedBySQL){
        Table table = new Table(tableName,tableCode,isGeneratedBySQL);
        tables.add(table);
        return this;
    }

    public Query addTable(String tableName, boolean isGeneratedBySQL){
        Table table = new Table(tableName,null, isGeneratedBySQL);
        tables.add(table);
        return this;
    }

    public Query addTable(String tableName, String tableCode){
        Table table = new Table(tableName,tableCode,false);
        tables.add(table);
        return this;
    }

    public Query addTable(String tableName){
        Table table = new Table(tableName,null,false);
        tables.add(table);
        return this;
    }

    public Query addJoin(String joinType,String tableName, String tableCode, String cond1, String cond2, String operator){
        if(!checkJoinType(joinType)){
            throw new IllegalArgumentException("illegal join type");
        }
        Join join = new Join(joinType,tableName,tableCode,cond1,cond2,operator);
        this.joins.add(join);
        return this;
    }

    public Query addJoin(String joinType,String tableName, String tableCode,String using){
        Join join = new Join(joinType,tableName,tableCode,using);
        this.joins.add(join);
        return this;
    }

    public Query addJoin(String joinType,String tableName, String tableCode ){
        Join join = new Join(joinType, tableName,tableCode, (String)null);
        this.joins.add(join);
        return this;
    }

    public Query addWhereCondition(String cond1, String cond2, String operation, boolean isCond1GeneratedBySQL, boolean isCond2GeneratedBySQL){
        Condition condition = new Condition(cond1,cond2,operation,isCond1GeneratedBySQL,isCond2GeneratedBySQL);
        whereConditions.add(condition);
        return this;
    }

    public Query addWhereCondition(String cond1, String cond2, String operation){
        Condition condition = new Condition(cond1,cond2,operation,false,false);
        whereConditions.add(condition);
        return this;
    }

    public Query addGroupBy(String columnName){
        this.group.addColumn(columnName);
        return this;
    }

    public Query addHaving(String aggregate,String colunName,String oprator,String comparator){
        this.group.addHaving(aggregate,colunName,oprator,comparator);
        return this;
    }

    public Query addOrderBy(String orderName, String orderType) {
        if(!checkOrderType(orderType)){
            throw new IllegalArgumentException("illegal order type");
        }
        Order order = new Order(orderName,orderType);
        orderBy.add(order);
        return this;
    }

    public Query addOrderBy(String orderName) {
        Order order = new Order(orderName,null);
        orderBy.add(order);
        return this;
    }

    public Query setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    public String generate(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT ");
        if(this.distinct){
            stringBuilder.append("DISTINCT ");
        }

        String columnStatement = columnArrayToString(this.columns);
        if(columnStatement != null && !"".equals(columnStatement)){
            stringBuilder.append(columnStatement);
        }else{
            stringBuilder.append("* ");
        }

        String tableStatement = tableArrayToString(this.tables);
        if(tableStatement != null && !"".equals(tableStatement)){
            stringBuilder.append(" FROM ");
            stringBuilder.append(tableStatement);
        }else{
            throw new IllegalArgumentException("table name is null");
        }

        String joinStatement = joinArrayToString(joins);
        if(joinStatement != null && !"".equals(joinStatement)){
            stringBuilder.append(joinStatement);
        }

        String conditionStatement = whereConditionArrayToString(this.whereConditions);
        if(conditionStatement != null && !"".equals(conditionStatement)){
            stringBuilder.append(" WHERE ");
            stringBuilder.append(conditionStatement);
        }else{
            stringBuilder.append(" WHERE 1 = 1");
        }

        if(this.group.getColumns().size()>0){
            stringBuilder.append(" GROUP BY ");
            String groupByStatement = groupByPrinter(group);
            stringBuilder.append(groupByStatement);
        }

        String orderByStatement = orderArrayToString(this.orderBy);
        if(this.orderBy != null && !"".equals(orderByStatement)){
            stringBuilder.append(" ORDER BY ");
            stringBuilder.append(orderByStatement);
        }

        if(this.limit>0){
            stringBuilder.append(" LIMIT " + this.limit);
        }

        this.sql = stringBuilder.toString();
        return sql;
    }

    private String columnArrayToString(List<Column> list) {
        StringBuilder stringBuilder = new StringBuilder();
        String separator = ", ";
        for(int i = 0;i<list.size();i++){
            stringBuilder.append(list.get(i).getColumnName());
            if(list.get(i).isAs()){
                stringBuilder.append(" AS " + list.get(i).getColumnCode());
            }else if(list.get(i).getColumnCode()!=null){
                stringBuilder.append(" " + list.get(i).getColumnCode());
            }
            if(i!=list.size()-1){
                stringBuilder.append(separator);
            }
        }
        return stringBuilder.toString().trim();
    }

    private String tableArrayToString(List<Table> list) {
        StringBuilder stringBuilder = new StringBuilder();
        String separator = ", ";
        for(int i = 0;i<list.size();i++){
            stringBuilder.append(tablePrinter(list.get(i)));
            if(i!=list.size()-1){
                stringBuilder.append(separator);
            }
        }
        return stringBuilder.toString().trim();
    }

    private String joinArrayToString(List<Join> list) {
        StringBuilder stringBuilder = new StringBuilder();
        String separator = " ";
        for(int i = 0;i<list.size();i++){
            stringBuilder.append(" " + list.get(i).getJoinType() + " ");
            stringBuilder.append(list.get(i).getTable().getTableName());
            if(list.get(i).getTable().getTableCode()!=null){
                stringBuilder.append(" ");
                stringBuilder.append(list.get(i).getTable().getTableCode());
            }
            if(list.get(i).getOn()!=null){
                stringBuilder.append(onPrinter(list.get(i).getOn()));
            }else if(list.get(i).getUsing()!=null){
                stringBuilder.append(" USING " + list.get(i).getUsing());
            }
            if(i!=list.size()-1){
                stringBuilder.append(separator);
            }
        }
        return stringBuilder.toString();
    }

    private String whereConditionArrayToString(List<Condition> list) {
        StringBuilder stringBuilder = new StringBuilder();
        String separator = " AND ";
        for(int i = 0;i<list.size();i++){

            if(list.get(i).getCond1GeneratedBySQL()){
                stringBuilder.append("(" + list.get(i).getCond1() + ")");
            }else{
                stringBuilder.append(list.get(i).getCond1());
            }

            stringBuilder.append(" " + list.get(i).getOperation() + " ");

            if(list.get(i).getCond2GeneratedBySQL()){
                stringBuilder.append("(" + list.get(i).getCond2() + ")");
            }else{
                stringBuilder.append(list.get(i).getCond2());
            }

            if(i!=list.size()-1){
                stringBuilder.append(separator);
            }
        }
        return stringBuilder.toString().trim();
    }

    private String groupByPrinter(Group group){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0;i<this.group.getColumns().size();i++){
            stringBuilder.append(group.getColumns().get(i).getColumnName());
            if(i!=group.getColumns().size()-1){
                stringBuilder.append(", ");
            }
        }
        if(group.getHavings().size()!=0){
            stringBuilder.append(" HAVING ");
            for(int i = 0;i<this.group.getHavings().size();i++){
                stringBuilder.append(havingPrinter(group.getHavings().get(i)));
                if(i!=group.getColumns().size()-1){
                    stringBuilder.append(" AND ");
                }
            }
        }
        return stringBuilder.toString().trim();
    }

    private String orderArrayToString(List<Order> list) {
        StringBuilder stringBuilder = new StringBuilder();
        String separator = ",";
        for(int i = 0;i<list.size();i++){
            stringBuilder.append(list.get(i).getOrderName());
            if(list.get(i).getOrderType()!=null){
                stringBuilder.append(" " + list.get(i).getOrderType().toString());
            }
            if(i!=list.size()-1){
                stringBuilder.append(separator);
            }
        }
        return stringBuilder.toString().trim();
    }

    private String tablePrinter(Table table){
        StringBuilder stringBuilder = new StringBuilder();
        if(table.getGeneratedBySQL()){
            stringBuilder.append("(" + table.getTableName() + ") ");
            stringBuilder.append(table.getTableCode());
        }else{
            stringBuilder.append(table.getTableName());
            if(table.getTableCode()!=null){
                stringBuilder.append(" ");
                stringBuilder.append(table.getTableCode());
            }
        }
        return stringBuilder.toString();
    }

    private String onPrinter(On on){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" ON ");
        stringBuilder.append(on.getCond1());
        stringBuilder.append(" " + on.getOperation() + " ");
        stringBuilder.append(on.getCond2());
        return stringBuilder.toString();
    }

    private String havingPrinter(Having having){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(having.getAggregate());
        stringBuilder.append("(" + having.getColumn() + ")");
        stringBuilder.append(having.getOprator());
        stringBuilder.append(having.getComparator());
        return stringBuilder.toString();
    }

    private boolean checkJoinType(String joinType){
        if(joinType.toUpperCase().equals("JOIN") || joinType.toUpperCase().equals("INNER JOIN") || joinType.toUpperCase().equals("LEFT JOIN")
        || joinType.toUpperCase().equals("RIGHT JOIN") || joinType.toUpperCase().equals("LEFT OUTER JOIN") || joinType.toUpperCase().equals("RIGHT OUTER JOIN")
        || joinType.toUpperCase().equals("FULL JOIN") || joinType.toUpperCase().equals("FULL OUTER JOIN")){
            return true;
        }
        return false;
    }

    private boolean checkOrderType(String orderType){
        if(orderType.toUpperCase().equals("ASC") || orderType.toUpperCase().equals("DESC")){
            return true;
        }
        return false;
    }
}