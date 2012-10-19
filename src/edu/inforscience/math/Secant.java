/*
    Numeth is simple application to solve many mathematical problems numerically.

    Copyright (C) 2012 Rafael Rendon Pablo <smart.rendon@gmail.com>

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

import java.util.ArrayList;

public class Secant {

  public double f(double x)
  {
    return -(3.79377 * Math.pow(x, 3)) +
      (16.2965 * Math.pow(x, 2)) -
      (21.963 * x) + 9.36 ;
  }

  public void solve(double x0, double x1, double epsilon,
                    int iterations, ArrayList<Solution> solutions) {
    while (true) {
      double fx = f(x1);
      if (Math.abs(fx) < epsilon) {
        solutions.add(new Solution(x0, x1, x1));
        break;
      }

      double temp = x1;
      x1 = x1 - (x1 - x0)/(f(x1) - f(x0)) * f(x1);
      x0 = temp;
      iterations--;

      if (iterations == 0)
        break;
    }
  }
}
