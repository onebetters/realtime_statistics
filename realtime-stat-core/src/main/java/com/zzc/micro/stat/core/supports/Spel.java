package com.zzc.micro.stat.core.supports;

import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Administrator
 */
public class Spel {

    private final static ConcurrentHashMap<String, Expression> c = new ConcurrentHashMap<>();

    public static <T> T exec(final String exp, final Object object) {
        final Expression expression = c.computeIfAbsent(exp, k -> new SpelExpressionParser().parseExpression(exp));
        final StandardEvaluationContext context = new StandardEvaluationContext(object);
        return (T) expression.getValue(context);
    }
}
