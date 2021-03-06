package org.nest.nestml._visitor;

import org.nest.nestml._ast.ASTExpr;
import org.nest.nestml._symboltable.typechecking.Either;
import org.nest.nestml._symboltable.symbols.TypeSymbol;

import static de.se_rwth.commons.logging.Log.error;
import static org.nest.nestml._symboltable.typechecking.TypeChecker.isBoolean;
import static org.nest.nestml._symboltable.predefined.PredefinedTypes.getBooleanType;


/**
 * @author ptraeder
 */
public class LogicalNotVisitor implements NESTMLVisitor {


  @Override
  public void visit(ASTExpr expr) {
    final Either<TypeSymbol, String> exprTypeE = expr.getExpr().get().getType();

    if (exprTypeE.isError()) {
      expr.setType(exprTypeE);
      return;
    }

    final TypeSymbol exprType = exprTypeE.getValue();

    if (isBoolean(exprType)) {
      expr.setType(Either.value(getBooleanType()));
    }
    else {
      final String errorMsg = CommonsErrorStrings.message(this, exprType.prettyPrint(), expr.get_SourcePositionStart());
      expr.setType(Either.error(errorMsg));
      error(errorMsg, expr.get_SourcePositionStart());
    }

  }

}
