
package edu.uci.ics.perpetual.statement.create.view;

import java.util.List;
import edu.uci.ics.perpetual.schema.Table;
import edu.uci.ics.perpetual.statement.Statement;
import edu.uci.ics.perpetual.statement.StatementVisitor;
import edu.uci.ics.perpetual.statement.select.PlainSelect;
import edu.uci.ics.perpetual.statement.select.Select;

/**
 * A "CREATE VIEW" statement
 */
public class CreateView implements Statement {

    private Table view;
    private Select select;
    private boolean orReplace = false;
    private List<String> columnNames = null;
    private boolean materialized = false;
    private ForceOption force = ForceOption.NONE;
    private TemporaryOption temp = TemporaryOption.NONE;

    @Override
    public void accept(StatementVisitor statementVisitor) {
        statementVisitor.visit(this);
    }

    /**
     * In the syntax tree, a view looks and acts just like a Table.
     *
     * @return The name of the view to be created.
     */
    public Table getView() {
        return view;
    }

    public void setView(Table view) {
        this.view = view;
    }

    /**
     * @return was "OR REPLACE" specified?
     */
    public boolean isOrReplace() {
        return orReplace;
    }

    /**
     * @param orReplace was "OR REPLACE" specified?
     */
    public void setOrReplace(boolean orReplace) {
        this.orReplace = orReplace;
    }

    public Select getSelect() {
        return select;
    }

    public void setSelect(Select select) {
        this.select = select;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    public boolean isMaterialized() {
        return materialized;
    }

    public void setMaterialized(boolean materialized) {
        this.materialized = materialized;
    }

    public ForceOption getForce() {
        return force;
    }

    public void setForce(ForceOption force) {
        this.force = force;
    }

    public TemporaryOption getTemporary() {
        return temp;
    }

    public void setTemporary(TemporaryOption temp) {
        this.temp = temp;
    }

    @Override
    public String toString() {
        StringBuilder sql = new StringBuilder("CREATE ");
        if (isOrReplace()) {
            sql.append("OR REPLACE ");
        }
        switch (force) {
            case FORCE:
                sql.append("FORCE ");
                break;
            case NO_FORCE:
                sql.append("NO FORCE ");
                break;
        }
        
        if (temp != TemporaryOption.NONE) {
            sql.append(temp.name()).append(" ");
        }
        
        if (isMaterialized()) {
            sql.append("MATERIALIZED ");
        }
        sql.append("VIEW ");
        sql.append(view);
        if (columnNames != null) {
            sql.append(PlainSelect.getStringList(columnNames, true, true));
        }
        sql.append(" AS ").append(select);
        return sql.toString();
    }
}
