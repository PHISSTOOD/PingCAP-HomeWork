package Generator;

public enum JoinType {

    JOIN("JOIN"),
    LEFT_JOIN("LEFT JOIN"),
    RIGHT_JOIN("RIGHT JOIN"),
    FULL_OUTER_JOIN("FULL OUTER JOIN");

    private final String desc;

    private JoinType(String desc){
        this.desc = desc;
    }
    public String getDesc(){
        return desc;
    }

}
