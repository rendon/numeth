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

public class Math {
  public static long factorial(int n)
  {
    long f = 1;
    while (n > 1) f *= (n--);

    return f;
  }

  public static double cos(double x, int iterations)
  {
    double cosine = 1;
    boolean minus = true;

    for (int n = 2; n <= iterations; n += 2) {
      if (minus)
        cosine -= java.lang.Math.pow(x, n)/factorial(n);
      else
        cosine += java.lang.Math.pow(x, n)/factorial(n);

      minus = !minus;
    }

    return cosine;
  }

  public static double e(int iterations)
  {
    double euler = 0;
    for (int n = 0; n < iterations; n++)
      euler += 1.0/factorial(n);

    return euler;
  }

}

