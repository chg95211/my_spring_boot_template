package com.zsk.template.util;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @description:
 * @author: zsk
 * @create: 2018-12-17 23:06
 **/
public class SpelParser
{
    private static ExpressionParser parser = new SpelExpressionParser();

    //解析el表达式的值
    public static String getKey(String key, String[] parameterNames, Object[] args)
    {
        Expression expression = parser.parseExpression(key);
        EvaluationContext context = new StandardEvaluationContext();

        if (args.length <= 0)
            return null;

        for (int i = 0; i < args.length; i++)
        {
            context.setVariable(parameterNames[i], args[i]);
        }


        return expression.getValue(context, String.class);
    }
}
