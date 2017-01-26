package com.radioteria.utils.jade;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class JadeUtil {

    public Object eval(String expression) {
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression(expression);
        return exp.getValue();
    }

    public long round(double number) {
        return Math.round(number);
    }

}
