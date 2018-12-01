
package edu.uci.ics.perpetual.util.deparser;

import edu.uci.ics.perpetual.statement.create.view.AlterView;
import edu.uci.ics.perpetual.statement.select.PlainSelect;
import edu.uci.ics.perpetual.statement.select.SelectVisitor;

/**
 * A class to de-parse (that is, tranform from JSqlParser hierarchy into a string) a
 * {@link edu.uci.ics.perpetual.statement.create.view.AlterView}
 */
public class AlterViewDeParser {

    protected StringBuilder buffer;
    private SelectVisitor selectVisitor;

    /**
     * @param buffer the buffer that will be filled with the select
     */
    public AlterViewDeParser(StringBuilder buffer) {
        SelectDeParser selectDeParser = new SelectDeParser();
        selectDeParser.setBuffer(buffer);
        ExpressionDeParser expressionDeParser = new ExpressionDeParser(selectDeParser, buffer);
        selectDeParser.setExpressionVisitor(expressionDeParser);
        selectVisitor = selectDeParser;
        this.buffer = buffer;
    }

    public AlterViewDeParser(StringBuilder buffer, SelectVisitor selectVisitor) {
        this.buffer = buffer;
        this.selectVisitor = selectVisitor;
    }

    public void deParse(AlterView alterView) {
        if(alterView.isUseReplace()){
            buffer.append("REPLACE ");
        }else{
            buffer.append("ALTER ");
        }
        buffer.append("VIEW ").append(alterView.getView().getFullyQualifiedName());
        if (alterView.getColumnNames() != null) {
            buffer.append(PlainSelect.getStringList(alterView.getColumnNames(), true, true));
        }
        buffer.append(" AS ");

        alterView.getSelectBody().accept(selectVisitor);
    }

    public StringBuilder getBuffer() {
        return buffer;
    }

    public void setBuffer(StringBuilder buffer) {
        this.buffer = buffer;
    }
}
