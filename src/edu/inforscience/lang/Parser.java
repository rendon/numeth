/*
  Numeth is simple application to solve many mathematical problems numerically.

  Copyright (C) 2012 Rafael Rendón Pablo <smart.rendon@gmail.com>

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program. If not, see <http://www.gnu.org/licenses/>.
*/
package edu.inforscience.lang;

import java.util.HashMap;
import java.util.TreeSet;
import edu.inforscience.math.Math;

public class Parser {

  private StringBuffer expression;
  private String token;
  private int tokenType;
  private int errorCode;
  private int index;
  private boolean isValidationMode;

  private HashMap<String, Double> variables;
  private HashMap<String, Double> constants;


  private TreeSet<String> functions;

  public static final double EPS                = 1e-8;

  public static final int DELIMITER             = 0x00000001;
  public static final int VARIABLE              = 0x00000002;
  public static final int CONSTANT              = 0x00000003;
  public static final int FUNCTION              = 0x00000004;
  public static final int NUMBER                = 0x00000005;
  public static final int UNDEFINED             = 0x00000006;

  public static final int SUCCESS               = 0x00000000;
  public static final int NO_EXPRESSION         = 0x00000001;
  public static final int LAST_TOKEN_NOT_NULL   = 0x00000002;
  public static final int INVALID_EXPRESSION  = 0x00000003;
  public static final int INVALID_NUMBER      = 0x00000004;
  public static final int INVALID_FUNCTION    = 0x00000005;


  public Parser()
  {
    initialize();
  }
  public Parser(String expression)
  {
    this.expression = new StringBuffer(expression);
    initialize();
  }


  private void initialize()
  {
    variables = new HashMap<String, Double>();
    constants = new HashMap<String, Double>();
    functions = new TreeSet<String>();

    constants.put("pi", Math.PI);
    constants.put("e", Math.E);

    String[] temp = new String[] {"sin", "cos", "tan", "log", "ln", "exp",
                                  "abs", "sqrt"};

    for (String function : temp)
      functions.add(function);

    setErrorCode(SUCCESS);
  }

  public void setVariable(String variable, double value)
  {
    variables.put(variable, value);
  }

  private boolean nextToken()
  {
    int length = expression.length();
    token = "";
    tokenType = UNDEFINED;

    if (index >= length)
      return false;
    while (index < length && expression.charAt(index) == ' ')
      index++;

    if (index < length && isDelimiter(expression.charAt(index))) {
      tokenType = DELIMITER;
      token += expression.charAt(index);
      index++;
    } else if (index < length && Character.isLetter(expression.charAt(index))) {
      while (index < length && !isDelimiter((expression.charAt(index)))) {
        token += expression.charAt(index);
        index++;
      }

      if (functions.contains(token))
        tokenType = FUNCTION;
      else if (constants.get(token) != null)
        tokenType = CONSTANT;
      else
        tokenType = VARIABLE;

    } else if (index < length && Character.isDigit(expression.charAt(index))) {
      while (index < length && !isDelimiter((expression.charAt(index)))) {
        token += expression.charAt(index);
        index++;
      }

      tokenType = NUMBER;
    }

    return true;
  }

  public int getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(int code) {
    errorCode = code;
  }

  public boolean isValidationMode() {
    return isValidationMode;
  }

  public void setValidationMode(boolean validationMode) {
    isValidationMode = validationMode;
  }

  private boolean isDelimiter(char c)
  {
    if ("+-/*%^!=() ".contains("" + c) ||
      c == '\t' || c == '\r')
      return true;

    return false;
  }

  private double sumAndSubtraction()
  {
    String operator;
    double temp;
    double result = productAndDivision();

    while ((operator = token).equals("+") || operator.equals("-")) {
      nextToken();
      temp = productAndDivision();

      if (operator.equals("+"))
        result = result + temp;
      if (operator.equals("-"))
        result = result - temp;
    }

    return result;
  }

  private double productAndDivision()
  {
    String operator;
    double temp, result;

    result = exponentAndFactorial();

    while ((operator = token).equals("*") || operator.equals("/")) {
      nextToken();
      temp = exponentAndFactorial();

      if (operator.equals("*"))
        result = result *  temp;
      if (operator.equals("/")) {
        result = result/temp;
      }
    }

    return result;
  }

  private double exponentAndFactorial()
  {
    double result = 0;

    result = sign();
    if (token.equals("^")) {
      nextToken();
      double p = exponentAndFactorial();
      result = java.lang.Math.pow(result, p);
    } else if (token.equals("!")) {
      result = Math.factorial((int)result);
      nextToken();
    }

    return result;
  }

  private double sign()
  {
    String operator = "";
    double result;

    if ((tokenType == DELIMITER) && token.equals("+")  || token.equals("-")) {
      operator = token;
      nextToken();
    }

    result = subExpression();
    if (operator.equals("-"))
      result = -result;

    return result;
  }

  private double subExpression()
  {
    double result;
    if (token.equals("(")) {
      nextToken();
      result = sumAndSubtraction();
      if (!token.equals(")")) {
        setErrorCode(INVALID_EXPRESSION);
        return 0;
      }
      nextToken();
    } else {
      result = atom();
    }
    return result;
  }

  private double atom()
  {
    double result = 0;
    if (tokenType == NUMBER) {
      result = Double.parseDouble(token);
      nextToken();
    } else if (tokenType == VARIABLE) {
      if (isValidationMode()) {
        result = 0;
      } else {
        result = variables.get(token);
      }
      nextToken();
    } else if (tokenType == CONSTANT) {
      result = constants.get(token);
      nextToken();
    } else if (tokenType == FUNCTION) {
      String function = token;
      nextToken();
      if (!token.equals("(")) {
        setErrorCode(INVALID_FUNCTION);
        return 0;
      }

      double parameter = subExpression();

      if (function.equals("sin")) {
        double theta = parameter%Math.PI;
        result = Math.sin(theta, 15);


        /************** WARNING: PUT SPECIAL ATTENTION HERE *******************/
        int d = (int)((parameter - theta)/Math.PI + EPS); // add eps
        if (d%2 == 1 && parameter > d * Math.PI)
          result = - result;


      } else if (function.equals("cos")) {
        double theta = parameter - parameter%Math.PI;
        result = Math.cos(theta, 15);

        /************** WARNING: PUT SPECIAL ATTENTION HERE *******************/
        int d = (int)((parameter - theta)%Math.PI + EPS);
        if (d%2 == 1 && parameter > d * Math.PI)
          result = -result;
      } else if (function.equals("tan")) {
        result = java.lang.Math.tan(parameter);
      } else if (function.equals("log")) {
        result = java.lang.Math.log10(parameter);
      } else if (function.equals("ln")) {
        result = java.lang.Math.log(parameter);
      } else if (function.equals("abs")) {
        result = java.lang.Math.abs(result);
      } else if (function.equals("exp")) {
        result = java.lang.Math.exp(parameter);
      } else if (function.equals("sqrt")) {
        result = java.lang.Math.sqrt(parameter);
      }

      nextToken();
    } else {
      setErrorCode(INVALID_NUMBER);
      return 0;
    }

    return result;
  }

  public double evaluate()
  {
    String temp = expression.toString();
    double result = evaluate(temp);
    expression = new StringBuffer(temp);

    return result;
  }

  public double evaluate(Function f, String var, double val) {
    setVariable(var, val);
    return evaluate(f.getDefinition());
  }


  public double evaluate(String expression)
  {
    double result = 0;
    this.expression = new StringBuffer(expression);
    index = 0;

    nextToken();
    if (token.equals("")) {
      setErrorCode(NO_EXPRESSION);
      return result;
    }

    result = sumAndSubtraction();

    if (!token.equals("")) {
      setErrorCode(LAST_TOKEN_NOT_NULL);
      return 0;
    }

    return result;
  }

  public boolean validate(String expression)
  {
    setValidationMode(true);
    evaluate(expression);
    setValidationMode(false);
    return getErrorCode() == SUCCESS;
  }


}

