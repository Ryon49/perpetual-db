
package edu.uci.ics.perpetual.util.deparser;

import edu.uci.ics.perpetual.expression.ExpressionVisitor;
import edu.uci.ics.perpetual.expression.ExpressionVisitorAdapter;
import edu.uci.ics.perpetual.schema.Table;
import edu.uci.ics.perpetual.statement.delete.Delete;
import edu.uci.ics.perpetual.statement.select.Join;

/**
 * A class to de-parse (that is, tranform from JSqlParser hierarchy into a string) a
 * {@link edu.uci.ics.perpetual.statement.delete.Delete}
 */
public class DeleteDeParser {

    protected StringBuilder buffer = new StringBuilder();
    private ExpressionVisitor expressionVisitor = new ExpressionVisitorAdapter();

    public DeleteDeParser() {
    }

    /**
     * @param expressionVisitor a {@link ExpressionVisitor} to de-parse expressions. It has to share
     * the same<br>
     * StringBuilder (buffer parameter) as this object in order to work
     * @param buffer the buffer that will be filled with the select
     */
    public DeleteDeParser(ExpressionVisitor expressionVisitor, StringBuilder buffer) {
        this.buffer = buffer;
        this.expressionVisitor = expressionVisitor;
    }

    public StringBuilder getBuffer() {
        return buffer;
    }

    public void setBuffer(StringBuilder buffer) {
        this.buffer = buffer;
    }

    public void deParse(Delete delete) {
        buffer.append("DELETE");
        if (delete.getTables() != null && delete.getTables().size() > 0) {
            for (Table table : delete.getTables()) {
                buffer.append(" ").append(table.getFullyQualifiedName());
            }
        }
        buffer.append(" FROM ").append(delete.getTable().toString());

        if (delete.getJoins() != null) {
            for (Join join : delete.getJoins()) {
                if (join.isSimple()) {
                    buffer.append(", ").append(join);
                } else {
                    buffer.append(" ").append(join);
                }
            }
        }

        if (delete.getWhere() != null) {
            buffer.append(" WHERE ");
            delete.getWhere().accept(expressionVisitor);
        }

        if (delete.getOrderByElements() != null) {
            new OrderByDeParser(expressionVisitor, buffer).deParse(delete.getOrderByElements());
        }
        if (delete.getLimit() != null) {
            new LimitDeparser(buffer).deParse(delete.getLimit());
        }

    }

    public ExpressionVisitor getExpressionVisitor() {
        return expressionVisitor;
    }

    public void setExpressionVisitor(ExpressionVisitor visitor) {
        expressionVisitor = visitor;
    }
}
