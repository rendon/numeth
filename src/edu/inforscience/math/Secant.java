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

import edu.inforscience.lang.Function;

import java.util.ArrayList;

public class Secant {

  private Function function;
  private static final int MAX_ITERATIONS = 200;

  public Secant(Function f)
  {
    function = f;
  }

  public double f(double x)
  {
    return function.evaluate(x);
  }

  public Solution find(double x0, double x1, double epsilon)
  {
    int iterations = 0;
    while (iterations < MAX_ITERATIONS) {
      double fx = f(x1);

      if (Math.abs(fx) < epsilon)
        return new Solution(x0, x1, x1);

      double temp = x1;
      x1 = x1 - (x1 - x0)/(f(x1) - f(x0)) * f(x1);
      x0 = temp;

      iterations++;
    }

    return null;
  }

  public ArrayList<Solution> solve(double a, double b, double epsilon)
  {
    BruteForce bruteForce = new BruteForce(function);
    ArrayList<Solution> possibleIntervals = bruteForce.solve(a, b);

    ArrayList<Solution> roots = new ArrayList<Solution>();

    for (int i = 0; i < possibleIntervals.size(); i++) {
      Solution sol = possibleIntervals.get(i);
      roots.add(find(sol.getA(), sol.getB(), epsilon));
    }

    return roots;
  }
}

