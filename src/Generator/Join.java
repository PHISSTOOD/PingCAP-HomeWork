package Generator;

public class Join {
    private String joinType;
    private Table table;
    private On on;
    private String using;

    public Join(String joinType, Table table, On on) {
        this.joinType = joinType;
        this.table = table;
        this.on = on;
        this.using = null;
    }

    public Join(String joinType, Table table, String using) {
        this.joinType = joinType;
        this.table = table;
        this.on = null;
        this.using = using;
    }

    public String getJoinType() {
        return joinType;
    }

    public Table getTable() {
        return table;
    }

    public On getOn() {
        return on;
    }

    public String getUsing(){
        return this.using;
    }
}
