package Generator;

import java.util.ArrayList;
import java.util.List;

public class Group {

    private List<Column> columns;
    private List<Having> havings;

    public Group() {
        this.columns = new ArrayList<>();
        this.havings = new ArrayList<>();
    }

    public List<Column> getColumns() {
        return columns;
    }

    public List<Having> getHavings() {
        return havings;
    }

    public void addColumn(String colunname){
        this.columns.add(new Column(colunname));
    }

    public void addHaving(String aggregate,String colunname,String oprator,String comparator){
        this.havings.add(new Having(aggregate,colunname,oprator,comparator));
    }
}
