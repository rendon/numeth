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
package edu.inforscience.math;

/**
 * My Extended Math class.
 */
public class Math {

  public static double PI   = java.lang.Math.PI;
  public static double E    = java.lang.Math.E;


  public static long factorial(int n)
  {
    long f = 1;
    while (n > 1) f *= (n--);

    return f;
  }

  // cos(x) = x^0/0! - x^2/2! + x^4/4! - x^6/6! + ...
  public static double cos(double x, int iterations)
  {
    double sum = 0, sign = 1;
    int n = 0;

    for (int i = 0; i <= iterations; i++) {
      sum += sign * java.lang.Math.pow(x, n)/factorial(n);
      sign *= -1;
      n += 2;
    }

    return sum;
  }

  // sin(x) = x^1/1! - x^3/3! + x^5/5! - x^7/7! + ...
  public static double sin(double x, int iterations)
  {
    double sign = 1, sum = 0;
    int n = 1;
    for (int i = 0; i < iterations; i++) {
      sum += sign * java.lang.Math.pow(x, n)/factorial(n);
      n += 2;
      sign *= -1;
    }

    return sum;
  }

  public static double e(int iterations)
  {
    double euler = 0;
    for (int n = 0; n < iterations; n++)
      euler += 1.0/factorial(n);

    return euler;
  }

  public static double sin(double theta)
  {
    return java.lang.Math.sin(theta);
  }

  public static double cos(double theta)
  {
    return java.lang.Math.cos(theta);
  }

  public static double tan(double theta)
  {
    return java.lang.Math.tan(theta);
  }

  public static double log10(double x)
  {
    return java.lang.Math.log10(x);
  }

  public static double log(double x)
  {
    return java.lang.Math.log(x);
  }




  public static double abs(double x)
  {
    return java.lang.Math.abs(x);
  }

  public static int abs(int x)
  {
    if (x < 0) x = -x;
    return x;
  }

  public static double exp(double x)
  {
    return java.lang.Math.exp(x);
  }

  public static double pow(double x, double p)
  {
    return java.lang.Math.pow(x, p);
  }

  public static double sqrt(double x)
  {
    return java.lang.Math.sqrt(x);
  }

  public static int sign(double n)
  {
    if (n > 0 ) return 1;
    if (n < 0) return -1;

    return 0;
  }

  public static double round(double value, int precision)
  {
    double p = Math.pow(10, precision);
    return java.lang.Math.floor(value * p)/p;
  }
}

