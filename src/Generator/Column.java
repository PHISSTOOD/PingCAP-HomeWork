package Generator;

public class Column {
    private String columnName;
    private boolean isOringinal;

    public Column(String columnName, boolean isOringinal){
        this.columnName = columnName;
        this.isOringinal = isOringinal;
    }

    public String getColumnName() {
        return columnName;
    }

    public boolean getOringinal() {
        return isOringinal;
    }
}
