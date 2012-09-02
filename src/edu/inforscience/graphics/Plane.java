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
package edu.inforscience.graphics;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.math.MathContext;

public class Plane extends JPanel implements MouseListener,
                                             MouseWheelListener,
                                             MouseMotionListener {
  private int maxX;
  private int maxY;
  private int centerX;
  private int centerY;
  private int factorIndex;

  private double pixelSize;
  private double gridInterval;
  private double[] factors;

  private final double REAL_WIDTH  = 10;
  private final double REAL_HEIGHT = 10;

  private boolean firstTime;
  private boolean showAxis;

  public Plane()
  {
    addMouseListener(this);
    addMouseWheelListener(this);
    firstTime = true;

    // Grid drawing settings
    gridInterval = 0.5;
    factors = new double[] {2, 2, 2.5};
    factorIndex = 0;
    setShowAxis(true);
  }


  @Override
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D)g;

    if (firstTime) {
      initGraphics();
      firstTime = false;
    }

    drawAxes(g2d);
    drawGrid(g2d);
    g2d.setStroke(new BasicStroke(1));
    drawLine(g2d, ix(0), iy(0), ix(3), iy(2));
    drawLine(g2d, ix(3), iy(2), ix(6), iy(0));
    drawLine(g2d, ix(6), iy(0), ix(0), iy(0));
  }


  void putPixel(Graphics2D g2d, int x, int y)
  {
    g2d.drawLine(x, y, x, y);
  }

  void drawLine(Graphics2D g2d, int xP, int yP, int xQ, int yQ)
  {
    int c, m, d = 0, x = xP, y = yP;
    int dx = xQ - xP, dy = yQ - yP;
    int xIncrement = 1, yIncrement = 1;

    if (dx < 0) {
      xIncrement = -1;
      dx = -dx;
    }

    if (dy < 0) {
      yIncrement = -1;
      dy = -dy;
    }

    if (dy <= dx) {
      c = dx + dx;
      m = dy + dy;
      if (xIncrement < 0) dx++;

      while (true) {
        putPixel(g2d, x, y);
        if (x == xQ) break;
        x += xIncrement;
        d += m;

        if (d >= dx) {
          y += yIncrement;
          d -= c;
        }
      }
    } else {
      c = dy + dy;
      m = dx + dx;

      if (yIncrement < 0) dy++;
      while (true) {
        putPixel(g2d, x, y);
        if (y == yQ) break;
        y += yIncrement;
        d += m;

        if (d >= dy) {
          x += xIncrement;
          d -= c;
        }
      }
    }
  }

  /**
   * Returns whether to draw or not the axis.
   * @return true if axis should be drawn.
   */
  public boolean isShowAxis() { return showAxis; }

  /**
   * Sets the showAxes property.
   * @param showAxis the new setting
   */
  public void setShowAxis(boolean showAxis)
  {
    this.showAxis = showAxis;
  }

  /**
   * Initialize the variables needed to use isotropic mapping mode(Computer
   * Graphics for Java Programmers, 2nd. Edition, Leen Ammeraaland, Kang Zhang).
   */
  protected void initGraphics()
  {
    maxX = getWidth() - 1;
    maxY = getHeight() - 1;

    centerX = maxX/2;
    centerY = maxY/2;
    pixelSize = Math.max(REAL_WIDTH/maxX, REAL_HEIGHT/maxY);

  }

  /**
   * Returns n rounded to the nearest integer.
   * @param n a double number
   * @return an integer rounded to the nearest integer
   */
  int round(double n)
  {
    return (int)Math.floor(n + 0.5);
  }

  /**
   * Returns the device-coordinate of x.
   * @param x x-coordinate in logical-coordinates
   * @return an integer with the device-coordinate of x
   */
  protected int ix(double x)
  {
    return round(centerX + x/pixelSize);
  }

  /**
   * Returns the device-coordinate of y.
   * @param y y-coordinate in logical-coordinates
   * @return an integer with the device-coordinate of y
   */
  protected int iy(double y)
  {
    return round(centerY - y/pixelSize);
  }

  /**
   * Returns the device-coordinate of x using a particular pixel size.
   * @param x x-coordinate in logical-coordinates
   * @param ps pixel size
   * @return an integer with the device-coordinate of x
   */
  public int ix(double x, double ps)
  {
    return round(centerX + x/ps);
  }

  /**
   * Returns the device-coordinate of y using a particular pixel size.
   * @param y y-coordinate in logical-coordinates
   * @param ps pixel size
   * @return an integer with the device-coordinate of y
   */
  public int iy(double y, double ps)
  {
    return round(centerY - y / ps);
  }


  public double fx(int x) { return (double)(x - centerX) * pixelSize; }
  public double fy(int y) { return (double)(centerY - y) * pixelSize; }


  /**
   * Returns real with the specified precision.
   * @param real a real number
   * @param precision precision in digits of the output
   * @return real with the specified precision
   */
  public double setPrecision(double real, int precision)
  {
    BigDecimal decimal = new BigDecimal(real, new MathContext(precision));
    return decimal.doubleValue();
  }


  /**
   * Zooms out the plane ten percent with origin in mouse click.
   * @param mx X coordinate of mouse click.
   * @param my Y coordinate of mouse click.
   */
  public void zoomOut(int mx, int my)
  {
    double ps = pixelSize;
    Point2D previous = new Point2D(fx(mx), fy(my));
    pixelSize += pixelSize/10;

    int dx = ix(previous.x()) - ix(previous.x(), ps);
    int dy = iy(previous.y()) - iy(previous.y(), ps);

    centerX -= dx;
    centerY -= dy;

    repaint();
  }

  /**
   * Zooms in the plane ten percent with origin in mouse click.
   * @param mx X coordinate of mouse click.
   * @param my Y coordinate of mouse click.
   */
  public void zoomIn(int mx, int my)
  {
    double ps = pixelSize;
    Point2D previous = new Point2D(fx(mx), fy(my));
    pixelSize -= pixelSize/10;

    int dx = ix(previous.x()) - ix(previous.x(), ps);
    int dy = iy(previous.y()) - iy(previous.y(), ps);

    centerX -= dx;
    centerY -= dy;

    repaint();
  }

  /**
   * Restore the original scale.
   */
  public void resetZoom()
  {
    initGraphics();
    repaint();
  }


  /**
   * Draw grid in the plane.
   * @param g2d Graphics2D object
   */
  public void drawGrid(Graphics2D g2d)
  {
    double left = fx(0);
    double top = fy(0);
    double right = fx(getWidth() - 1);
    double bottom = fy(getHeight() - 1);

    int w = ix(gridInterval) - ix(0);
    if (w < 50) {
      gridInterval *= factors[factorIndex];
      factorIndex = (factorIndex + 1)%factors.length;
    } else if (w > 150) {
      factorIndex = (factorIndex - 1 + factors.length)%factors.length;
      gridInterval /= factors[factorIndex];
    }

    int cX = ix(0);
    int interval = ix(gridInterval) - cX;
    int mod = cX % interval;
    double startX = fx(mod) - (fx(mod) % gridInterval) - gridInterval;

    int cY = iy(0);
    interval = iy(gridInterval) - cY;
    mod = cY % interval;
    double startY = fy(mod) - (fy(mod) % gridInterval) + gridInterval;

    Stroke dash = new BasicStroke(0.5f, BasicStroke.CAP_SQUARE,
                                  BasicStroke.JOIN_MITER, 10,
                                  new float[] {8,4}, 0);


    g2d.setStroke(dash);
    g2d.setColor(new Color(140, 140, 140));
    for (double i = startX; i <= right; i += gridInterval)
      if (ix(i) != ix(0) || !isShowAxis())
        g2d.drawLine(ix(i), iy(top), ix(i), iy(bottom));

    for (double i = startY; i >= bottom; i -= gridInterval)
      if (iy(i) != iy(0) || !isShowAxis())
        g2d.drawLine(ix(left), iy(i), ix(right), iy(i));

  }// End of drawGrid()

  /**
   * Draw axes in the plane.
   * @param g2d a Graphics2D object
   */
  public void drawAxes(Graphics2D g2d)
  {
    double left = fx(0);
    double top = fy(0);
    double right = fx(getWidth() - 1);
    double bottom = fy(getHeight() - 1);

    int w = ix(gridInterval) - ix(0);
    if (w < 50) {
      gridInterval *= factors[factorIndex];
      factorIndex = (factorIndex + 1)%factors.length;
    } else if (w > 150) {
      factorIndex = (factorIndex - 1 + factors.length)%factors.length;
      gridInterval /= factors[factorIndex];
    }

    int cX = ix(0);
    int interval = ix(gridInterval) - cX;
    int mod = cX % interval;
    double startX = fx(mod) - (fx(mod) % gridInterval) - gridInterval;

    int cY = iy(0);
    interval = iy(gridInterval) - cY;
    mod = cY % interval;
    double startY = fy(mod) - (fy(mod) % gridInterval) + gridInterval;

    g2d.setStroke(new BasicStroke(1f));
    g2d.setColor(Color.BLACK);

    for (double i = startX; i <= right; i += gridInterval) {
      if (ix(i) == ix(0)) {
        g2d.drawLine(ix(i), iy(top), ix(i), iy(bottom));

        //Draws arrows of y axis
        g2d.drawLine(ix(i) - 7, iy(top) + 7, ix(i), iy(top));
        g2d.drawLine(ix(i) + 7, iy(top) + 7, ix(i), iy(top));
        g2d.drawString("y", ix(i) - 20, iy(top) + 10);

        g2d.drawLine(ix(i) - 7, iy(bottom) - 7, ix(i), iy(bottom));
        g2d.drawLine(ix(i) + 7, iy(bottom) - 7, ix(i), iy(bottom));
        g2d.drawString("-y", ix(i) - 20, iy(bottom) - 10);
      } else {
        g2d.drawString("" + setPrecision(i, 5), ix(i) + 5, iy(0) + 15);
      }
      g2d.drawLine(ix(i), iy(0), ix(i), iy(0) + 12);
    }

    for (double i = startY; i >= bottom; i -= gridInterval) {
      if (iy(i) == iy(0)) {
        g2d.drawLine(ix(left), iy(i), ix(right), iy(i));

        //Draw arrows of x axis
        g2d.drawLine(ix(left) + 7, iy(i) - 7, ix(left), iy(i));
        g2d.drawLine(ix(left) + 7, iy(i) + 7, ix(left), iy(i));
        g2d.drawString("-x", ix(left) + 5, iy(i) - 10);

        g2d.drawLine(ix(right) - 7, iy(i) - 7, ix(right), iy(i));
        g2d.drawLine(ix(right) - 7, iy(i) + 7, ix(right), iy(i));
        g2d.drawString("x", ix(right) - 5, iy(i) - 10);
      } else {
        g2d.drawString("" + setPrecision(i, 5), ix(0) + 10, iy(i) - 5);
      }
      g2d.drawLine(ix(0), iy(i), ix(0) + 12, iy(i));
    }

  }// End of drawAxes()










  /* MouseListener methods. */
  @Override
  public void mouseReleased(MouseEvent event) { }

  @Override
  public void mouseExited(MouseEvent event) { }

  @Override
  public void mouseClicked(MouseEvent event) { }

  @Override
  public void mouseEntered(MouseEvent event) { }

  @Override
  public void mousePressed(MouseEvent event) { }

  /* MouseMotionListener methods. */
  @Override
  public void mouseDragged(MouseEvent e) { }

  @Override
  public void mouseMoved(MouseEvent e) { }

  /* MouseWheelListener methods. */

  /**
   * Controls zoom direction.
   * @param event a MouseWheelEvent object
   */
  @Override
  public void mouseWheelMoved(MouseWheelEvent event) {
    int rotation = event.getWheelRotation();
    int x = event.getX();
    int y = event.getY();

    if (rotation > 0)
      zoomIn(x, y);
    else
      zoomOut(x, y);
  }
}


