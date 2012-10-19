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

public class FixedPoint {
  public double f(double x)
  {
    return (7 * Math.pow(x, 6)) - Math.pow(x, 3) + (5 * Math.pow(x,2));
  }

  public void solve(double x0, double epsilon, ArrayList<Solution> solutions) {
    double x1;
    while (true) {
      x1 = f(x0);

      if (Math.abs(x1 - x0) < epsilon) {
        solutions.add(new Solution(x0, x1, x1));
        break;
      }

      x0 = x1;
    }
  }
}

