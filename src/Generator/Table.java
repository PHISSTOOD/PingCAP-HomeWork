package Generator;

public class Table {

    private String tableName;
    private String tableCode;
    private boolean isGeneratedBySQL;

    public Table(String tableName, String tableCode, boolean isGeneratedBySQL){
        this.tableName = tableName;
        this.tableCode = tableCode;
        this.isGeneratedBySQL = isGeneratedBySQL;
    }

    public Table(String tableName, boolean isGeneratedBySQL){
        this.tableName = tableName;
        this.isGeneratedBySQL = isGeneratedBySQL;
    }

    public String getTableName() {
        return tableName;
    }

    public String getTableCode() {
        return tableCode;
    }

    public boolean getGeneratedBySQL() {
        return isGeneratedBySQL;
    }

}
