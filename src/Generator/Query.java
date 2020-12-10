package Generator;


public class Query {

    private String sql;
    private boolean distinct;
    private Column[] columns;
    private Table[] tables;
    private Condition[] conditions;
    // groupBy
    // having
    private String orderBy;
    private int limit;


    public Query(Column[] columns, Table[] tables, Condition[] conditions,String orderBy, int limit) {
        this.columns = columns;
        this.tables = tables;
        this.conditions = conditions;
        this.orderBy = orderBy;
        this.limit = limit;
        this.sql = toQuery();

    }

    public Column[] getColumns() {
        return columns;
    }

    public Table[] getTables() {
        return tables;
    }

    public Condition[] getConditions() {
        return conditions;
    }

    public String getSql() {
        return sql;
    }


    public String toQuery(){
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

        String conditionStatement = conditionArrayToString(this.conditions);
        if(conditionStatement != null && !"".equals(conditionStatement)){
            stringBuilder.append(" WHERE ");
            stringBuilder.append(conditionStatement);
        }

        if(this.orderBy != null){
            stringBuilder.append(" ORDER BY " + this.orderBy);
        }

        if(this.limit>0){
            stringBuilder.append(" LIMIT " + this.limit);
        }

        return stringBuilder.toString();
    }

    public static String columnArrayToString(Column[] array) {
        StringBuilder stringBuilder = new StringBuilder();
        String separator = ",";
        for(int i = 0;i<array.length;i++){
            stringBuilder.append(array[i].getColumnName());
            if(i!=array.length-1){
                stringBuilder.append(separator);
            }
        }
        return stringBuilder.toString().trim();
    }

    public static String tableArrayToString(Table[] array) {
        StringBuilder stringBuilder = new StringBuilder();
        String separator = ",";
        for(int i = 0;i<array.length;i++){
            if(array[i].getGeneratedBySQL()){
                stringBuilder.append("(" + array[i].getTableName() + ") ");
                stringBuilder.append(array[i].getTableCode());
            }else{
                stringBuilder.append(array[i].getTableName());
                if(array[i].getTableCode()!=null){
                    stringBuilder.append(" ");
                    stringBuilder.append(array[i].getTableCode());
                }
            }
            if(i!=array.length-1){
                stringBuilder.append(separator);
            }
        }
        return stringBuilder.toString().trim();
    }

    public static String conditionArrayToString(Condition[] array) {
        StringBuilder stringBuilder = new StringBuilder();
        String separator = " AND ";
        for(int i = 0;i<array.length;i++){

            if(array[i].getCond1GeneratedBySQL()){
                stringBuilder.append("(" + array[i].getCond1() + ")");
            }else{
                stringBuilder.append(array[i].getCond1());
            }

            stringBuilder.append(" " + array[i].getOperation() + " ");

            if(array[i].getCond2GeneratedBySQL()){
                stringBuilder.append("(" + array[i].getCond2() + ")");
            }else{
                stringBuilder.append(array[i].getCond2());
            }

            if(i!=array.length-1){
                stringBuilder.append(separator);
            }
        }
        return stringBuilder.toString().trim();
    }

}