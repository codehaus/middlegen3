/*
 * Copyright (c) 2001, The RdbmsTableFromJdbcMetadataProvider Group
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * - Neither the name of The RdbmsTableFromJdbcMetadataProvider Group nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.codehaus.middlegen.swing;

import javax.swing.ImageIcon;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class represents a relation line. Responsible for drawing the 2D stuff.
 *
 * @author <a href="mailto:aslak.hellesoy at netcom.no">Aslak Hellesøy</a>
 * @version $Id: RelationLine.java,v 1.5 2003/05/26 17:57:27 rinkrank Exp $
 */
public class RelationLine {

   private Stroke _regularStroke = new BasicStroke(2);

   /**
    * Keep track of what self relation we are, if any.
    */
   private int _selfIndex;

   private Stroke _dashedStroke = new BasicStroke(
         2,
         BasicStroke.CAP_BUTT,
         BasicStroke.JOIN_MITER,
         10,
         new float[]{5, 5},
         0
         );

   private boolean _isSelected;

   private JTablePanel _originTable;

   private JTablePanel _targetTable;

   /**
    * The lines from each key column to the main line on the origin table
    */
   private Line2D.Float[] _originColumnLines;
   /**
    * The lines from each key column to the main line on the eastern table
    */
   private Line2D.Float[] _targetColumnLines;
   private Line2D.Float _mainLine;
   private Line2D.Float _selfWestLine;
   private Line2D.Float _selfEastLine;
   private Point2D.Float _originArrowPoint;
   private Point2D.Float _targetArrowPoint;
   private Line2D.Float _originArrowLine1;
   private Line2D.Float _originArrowLine2;
   private Line2D.Float _targetArrowLine1;
   private Line2D.Float _targetArrowLine2;
   private Point2D.Float _originArrowPoint1;
   private Point2D.Float _originArrowPoint2;
   private Point2D.Float _targetArrowPoint1;
   private Point2D.Float _targetArrowPoint2;
   private Point2D.Float _temp;
   private AffineTransform _arrowTransform;
   private Point2D.Float _originCardinalityPoint;
   private Point2D.Float _targetCardinalityPoint;
   private Point2D.Float _originFkPoint;
   private Point2D.Float _targetFkPoint;
   private double _mainLength;
   private boolean _originIsWest;

   /**
    * Average y between all key columns
    */
   private int _originColumnY = 0;

   /**
    * Average y between all key columns
    */
   private int _targetColumnY = 0;

   private int[] _originEdgeY;
   private int[] _targetEdgeY;

    private final boolean _isMany2Many;

   /**
    * JTablePanel -> Integer Contains the number of self relations for a given
    * table.
    */
   private static Map _selfRelationCountMap = new HashMap();

   private static Image _starImage = new ImageIcon(RelationLine.class.getResource("star.gif")).getImage();
   private static Image _oneImage = new ImageIcon(RelationLine.class.getResource("one.gif")).getImage();
   private static Image _starSelectedImage = new ImageIcon(RelationLine.class.getResource("star_white.gif")).getImage();
   private static Image _oneSelectedImage = new ImageIcon(RelationLine.class.getResource("one_white.gif")).getImage();
   private static Image _fkImage = new ImageIcon(RelationLine.class.getResource("fk.gif")).getImage();
   private static RenderingHints _renderHints;

    private org.codehaus.rdbms.ForeignKey _foreignKey;

   /**
    * Creates new RelationLine
    *
    * @param originTable Describe what the parameter does
    * @param targetTable Describe what the parameter does
    * @param foreignKey Describe what the parameter does
    * <pre>
    *
    *  If it's a self relation, draw it differently. We want to draw it around the table.
    *  This is to be able to distinguish several self relations.
    *
    *  The main line which "listens" for mouse clicks, and where cardinality
    *  and directionality icons are drawn.
    *      |
    *      |             >-<  These gaps are increased once for each self relation
    *      v
    *  +-<--------------->-+    v
    *  |                   |    |
    *  | +-<----------->-+ |    ^
    *  | |   +-------+   | |
    *  | |   |       |   | |<---------- The 2 vertical lines only exists for self relations.
    *  | |   +-------+   | |
    *  +-+---|       |---+-+
    *        |       |
    *        |       |
    *        +-------+
    *  </pre>
    */
   public RelationLine(org.codehaus.rdbms.ForeignKey foreignKey, JTablePanel originTable, JTablePanel targetTable) {
       _isMany2Many = false;

      _originTable = originTable;
      _targetTable = targetTable;
       _foreignKey = foreignKey;

      // left table points
      _originEdgeY = new int[_foreignKey.getReferences().size()];
       _targetEdgeY = new int[_originEdgeY.length];
        int i = 0;
       for (Iterator refs = _foreignKey.getReferences().iterator(); refs.hasNext();) {
           org.codehaus.rdbms.Reference reference = (org.codehaus.rdbms.Reference) refs.next();
           org.codehaus.rdbms.Column foreignColumn = reference.getForeignColumn();
           org.codehaus.rdbms.Column localColumn = reference.getPrimaryColumn();
           _originEdgeY[i] = _originTable.getColumnY(foreignColumn);
           _originColumnY += _originEdgeY[i];

          _targetEdgeY[i] = _targetTable.getColumnY(localColumn);
          _targetColumnY += _targetEdgeY[i];

           i++;
      }
      _originColumnY = _originColumnY / _originEdgeY.length;
       _targetColumnY = _targetColumnY / _targetEdgeY.length;

       init();
   }

    public RelationLine(/*Many2Many,*/ JTablePanel leftTable, JTablePanel rightTable) {
        _isMany2Many = true;
        /*
        _log.debug("B:" + _targetRole.getColumnMaps().length);
        _targetEdgeY = new int[_targetRole.getColumnMaps().length];
        for (int i = 0; i < _targetEdgeY.length; i++) {
           _targetEdgeY[i] = _targetTable.getColumnY(_targetRole.getColumnMaps()[i].getPrimaryKey());
           _targetColumnY += _targetEdgeY[i];
        }
        _targetColumnY = _targetColumnY / _targetRole.getColumnMaps().length;
        */
        init();
    }

   private void init() {
      _originColumnLines = createLines(_originEdgeY.length);
      _targetColumnLines = createLines(_targetEdgeY.length);

      _mainLine = new Line2D.Float();

      _originCardinalityPoint = new Point2D.Float();
      _targetCardinalityPoint = new Point2D.Float();

      _originFkPoint = new Point2D.Float();
      _targetFkPoint = new Point2D.Float();

      _originArrowPoint = new Point2D.Float();
      _targetArrowPoint = new Point2D.Float();

      _originArrowLine1 = new Line2D.Float();
      _originArrowLine2 = new Line2D.Float();
      _targetArrowLine1 = new Line2D.Float();
      _targetArrowLine2 = new Line2D.Float();
      _originArrowPoint1 = new Point2D.Float();
      _originArrowPoint2 = new Point2D.Float();
      _targetArrowPoint1 = new Point2D.Float();
      _targetArrowPoint2 = new Point2D.Float();
      _temp = new Point2D.Float();
      _arrowTransform = new AffineTransform();

      // Now update _selfRelationCountMap.
      if (isSelf()) {
         Integer self = (Integer)_selfRelationCountMap.get(_originTable);
         if (self == null) {
            self = new Integer(0);
         }
         _selfIndex = self.intValue();
         _selfRelationCountMap.put(_originTable, new Integer(_selfIndex + 1));

         _selfWestLine = new Line2D.Float();
         _selfEastLine = new Line2D.Float();

      }
      else {
         _selfIndex = -1;
      }
   }


   /**
    * Sets the Selected attribute of the RelationLine object
    *
    * @param isSelected The new Selected value
    */
   public void setSelected(boolean isSelected) {
      _isSelected = isSelected;
   }


   /**
    * Gets the Selected attribute of the RelationLine object
    *
    * @return The Selected value
    */
   public boolean isSelected() {
      return _isSelected;
   }
   public void update() {
      /*
       *  int leftY = _originTable.getY() + _originTable.getColumnY(_originRole.getColumnMap().getPrimaryKey());
       *  int rightY;
       *  if (!_originRole.getRelation().isMany2Many()) {
       *  rightY = _targetTable.getY() + _targetTable.getColumnY(_originRole.getColumnMap().getForeignKey());
       *  }
       *  else {
       *  rightY = _targetTable.getY() + _targetTable.getColumnY(_targetRole.getColumnMap().getPrimaryKey());
       *  }
       */
      int leftY = _originTable.getY() + _originColumnY;
      int rightY = _targetTable.getY() + _targetColumnY;

      // find out which table is farthest west (and east)
      JTablePanel westTable;
      JTablePanel eastTable;
      int westY;
      int eastY;
      Line2D.Float[] westLines;
      Line2D.Float[] eastLines;
      int[] westEdgeY;
      int[] eastEdgeY;

      _originIsWest = _originTable.getX() < _targetTable.getX();

      if (_originIsWest) {
         westTable = _originTable;
         eastTable = _targetTable;
         westY = leftY;
         eastY = rightY;
         westLines = _originColumnLines;
         eastLines = _targetColumnLines;
         westEdgeY = _originEdgeY;
         eastEdgeY = _targetEdgeY;
      }
      else {
         westTable = _targetTable;
         eastTable = _originTable;
         westY = rightY;
         eastY = leftY;
         westLines = _targetColumnLines;
         eastLines = _originColumnLines;
         westEdgeY = _targetEdgeY;
         eastEdgeY = _originEdgeY;
      }

      int mainWestY;
      int mainEastY;
      if (isSelf()) {
         mainWestY = _originTable.getY() - 25 - (_selfIndex * 20);
         mainEastY = mainWestY;
      }
      else {
         mainWestY = westY;
         mainEastY = eastY;
      }

      // find out whether the tables are more or less vertically aligned
      boolean aligned = (eastTable.getX() - westTable.getX()) < eastTable.getWidth() / 2;

      if (isSelf()) {
         aligned = false;
      }
      // TODO: handle different widths

      int westX;
      if (aligned) {
         westX = westTable.getX() - 10;
         for (int i = 0; i < westEdgeY.length; i++) {
            westLines[i].setLine(westX, westY, westTable.getX(), westEdgeY[i] + westTable.getY());
         }
      }
      else {
         westX = westTable.getX() + westTable.getWidth() + 10;
         for (int i = 0; i < westEdgeY.length; i++) {
            westLines[i].setLine(westX, westY, westTable.getX() + westTable.getWidth(), westEdgeY[i] + westTable.getY());
         }
      }
      int eastX = eastTable.getX() - 10;

      for (int i = 0; i < eastEdgeY.length; i++) {
         eastLines[i].setLine(eastX, eastY, eastTable.getX(), eastEdgeY[i] + eastTable.getY());
      }

      _mainLine.setLine(westX, mainWestY, eastX, mainEastY);

      if (isSelf()) {
         _selfWestLine.setLine(westX, mainWestY, westX, westY);
         _selfEastLine.setLine(eastX, mainEastY, eastX, eastY);
      }

      setCardinalityPoints();
   }

   public void paint(Graphics2D g) {
      g.setRenderingHints(_renderHints);
      if (_isMany2Many) {
         g.setStroke(_dashedStroke);
      }
      else {
         g.setStroke(_regularStroke);
      }
      drawLineUnlessItsLengthIsZero(g, _mainLine);

      if (isSelf()) {
         drawLineUnlessItsLengthIsZero(g, _selfWestLine);
         drawLineUnlessItsLengthIsZero(g, _selfEastLine);
      }

      g.setStroke(_regularStroke);
      for (int i = 0; i < _originColumnLines.length; i++) {
         drawLineUnlessItsLengthIsZero(g, _originColumnLines[i]);
      }
      for (int i = 0; i < _targetColumnLines.length; i++) {
         drawLineUnlessItsLengthIsZero(g, _targetColumnLines[i]);
      }
      drawCardinality(g);

      if (_foreignKey.isNavigableA()) {
         drawLineUnlessItsLengthIsZero(g, _originArrowLine1);
         drawLineUnlessItsLengthIsZero(g, _originArrowLine2);
      }
      if (_foreignKey.isNavigableB()) {
         drawLineUnlessItsLengthIsZero(g, _targetArrowLine1);
         drawLineUnlessItsLengthIsZero(g, _targetArrowLine2);
      }

      // draw the fk image
      drawImage(g, _fkImage, _originFkPoint.x - 6, _originFkPoint.y - 6);
   }


   /**
    * @param evt Describe what the parameter does
    * @return Describe the return value
    */
   public boolean selectMaybe(MouseEvent evt) {
      boolean multiplicity = evt.isControlDown();
      boolean cardinality = evt.isShiftDown();
      Point point = evt.getPoint();
      setSelected(_mainLine.intersects(point.getX() - 5, point.getY() - 5, 10, 10));
      if (isSelected()) {
         double distFromP1 = Point2D.distance(_mainLine.getX1(), _mainLine.getY1(), point.getX(), point.getY());

         boolean nearP1 = distFromP1 < _mainLength / 2;
         boolean originChosen = (_originIsWest && nearP1) || (!_originIsWest && !nearP1);

/*
         if (multiplicity) {
            // toggle multiplicity. Let's prevent the user from doing something stupid. Some cardinalities
            // can't be changed. we know best etc.
            if (_foreignKey.isMany2Many()) {
               // don't allow to change cardinality of m:n relationships
               JOptionPane.showMessageDialog(null, "Can't change the cardinality of a many-to-many relationship", "Cardinality", JOptionPane.INFORMATION_MESSAGE);
               return false;
            }
            if(originChosen) {
                // The FK side was selected
                try {
                     _foreignKey.setOriginMany(!_foreignKey.isOriginMany());
                } catch (org.codehaus.rdbms.jdbc.DatabaseException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Cardinality", JOptionPane.INFORMATION_MESSAGE);
                    return false;
                }
            } else {
               JOptionPane.showMessageDialog(null, "Can't change the cardinality of the one-side of a relationship that corresponds to a primary key", "Cardinality", JOptionPane.INFORMATION_MESSAGE);
               return false;
            }
         }
*/
         if (cardinality) {
            // toggle between uni/bidirectional
             if(originChosen) {
                 _foreignKey.setNavigableA(!_foreignKey.isNavigableA());
             } else {
                 _foreignKey.setNavigableB(!_foreignKey.isNavigableB());
             }
         }
         return true;
      }
      else {
         return false;
      }
   }


   /**
    * Sets the CardinalityPoints attribute of the RelationLine object
    *
    * @todo refactor this duplicate code!!!
    */
   private void setCardinalityPoints() {
      // compute vector of length 1
      _mainLength = Point2D.distance(_mainLine.getX1(), _mainLine.getY1(), _mainLine.getX2(), _mainLine.getY2());
      double vx = (_mainLine.getX2() - _mainLine.getX1()) / _mainLength;
      double vy = (_mainLine.getY2() - _mainLine.getY1()) / _mainLength;
      double cardx = 20.0 * vx;
      double cardy = 20.0 * vy;
      double arrowx = 30.0 * vx;
      double arrowy = 30.0 * vy;
      if (_originIsWest) {
         _originArrowPoint.setLocation(_mainLine.getX1() + cardx, _mainLine.getY1() + cardy);
         _targetArrowPoint.setLocation(_mainLine.getX2() - cardx, _mainLine.getY2() - cardy);

         // The left-side arrow head
         _temp.setLocation(_mainLine.getX1() + arrowx, _mainLine.getY1() + arrowy);

         _arrowTransform.setToRotation(Math.PI / 6, _originArrowPoint.getX(), _originArrowPoint.getY());
         _arrowTransform.transform(_temp, _originArrowPoint1);
         _originArrowLine1.setLine(_originArrowPoint.getX(), _originArrowPoint.getY(), _originArrowPoint1.getX(), _originArrowPoint1.getY());

         _arrowTransform.setToRotation(-Math.PI / 6, _originArrowPoint.getX(), _originArrowPoint.getY());
         _arrowTransform.transform(_temp, _originArrowPoint2);
         _originArrowLine2.setLine(_originArrowPoint.getX(), _originArrowPoint.getY(), _originArrowPoint2.getX(), _originArrowPoint2.getY());

         _arrowTransform.setToRotation(Math.PI / 2, _originArrowPoint.getX(), _originArrowPoint.getY());
         _arrowTransform.transform(_temp, _originCardinalityPoint);

         _arrowTransform.setToRotation(-Math.PI / 2, _originArrowPoint.getX(), _originArrowPoint.getY());
         _arrowTransform.transform(_temp, _originFkPoint);

         // The right-side arrow head
         _temp.setLocation(_mainLine.getX2() - arrowx, _mainLine.getY2() - arrowy);

         _arrowTransform.setToRotation(Math.PI / 6, _targetArrowPoint.getX(), _targetArrowPoint.getY());
         _arrowTransform.transform(_temp, _targetArrowPoint1);
         _targetArrowLine1.setLine(_targetArrowPoint.getX(), _targetArrowPoint.getY(), _targetArrowPoint1.getX(), _targetArrowPoint1.getY());

         _arrowTransform.setToRotation(-Math.PI / 6, _targetArrowPoint.getX(), _targetArrowPoint.getY());
         _arrowTransform.transform(_temp, _targetArrowPoint2);
         _targetArrowLine2.setLine(_targetArrowPoint.getX(), _targetArrowPoint.getY(), _targetArrowPoint2.getX(), _targetArrowPoint2.getY());

         _arrowTransform.setToRotation(-Math.PI / 2, _targetArrowPoint.getX(), _targetArrowPoint.getY());
         _arrowTransform.transform(_temp, _targetCardinalityPoint);

         _arrowTransform.setToRotation(Math.PI / 2, _targetArrowPoint.getX(), _targetArrowPoint.getY());
         _arrowTransform.transform(_temp, _targetFkPoint);
      }
      else {
         _originArrowPoint.setLocation(_mainLine.getX2() - cardx, _mainLine.getY2() - cardy);
         _targetArrowPoint.setLocation(_mainLine.getX1() + cardx, _mainLine.getY1() + cardy);

         // The left-side arrow head
         _temp.setLocation(_mainLine.getX2() - arrowx, _mainLine.getY2() - arrowy);

         _arrowTransform.setToRotation(Math.PI / 6, _originArrowPoint.getX(), _originArrowPoint.getY());
         _arrowTransform.transform(_temp, _originArrowPoint1);
         _originArrowLine1.setLine(_originArrowPoint.getX(), _originArrowPoint.getY(), _originArrowPoint1.getX(), _originArrowPoint1.getY());

         _arrowTransform.setToRotation(-Math.PI / 6, _originArrowPoint.getX(), _originArrowPoint.getY());
         _arrowTransform.transform(_temp, _originArrowPoint2);
         _originArrowLine2.setLine(_originArrowPoint.getX(), _originArrowPoint.getY(), _originArrowPoint2.getX(), _originArrowPoint2.getY());

         _arrowTransform.setToRotation(Math.PI / 2, _originArrowPoint.getX(), _originArrowPoint.getY());
         _arrowTransform.transform(_temp, _originCardinalityPoint);

         _arrowTransform.setToRotation(-Math.PI / 2, _originArrowPoint.getX(), _originArrowPoint.getY());
         _arrowTransform.transform(_temp, _originFkPoint);

         // The right-side arrow head
         _temp.setLocation(_mainLine.getX1() + arrowx, _mainLine.getY1() + arrowy);

         _arrowTransform.setToRotation(Math.PI / 6, _targetArrowPoint.getX(), _targetArrowPoint.getY());
         _arrowTransform.transform(_temp, _targetArrowPoint1);
         _targetArrowLine1.setLine(_targetArrowPoint.getX(), _targetArrowPoint.getY(), _targetArrowPoint1.getX(), _targetArrowPoint1.getY());

         _arrowTransform.setToRotation(-Math.PI / 6, _targetArrowPoint.getX(), _targetArrowPoint.getY());
         _arrowTransform.transform(_temp, _targetArrowPoint2);
         _targetArrowLine2.setLine(_targetArrowPoint.getX(), _targetArrowPoint.getY(), _targetArrowPoint2.getX(), _targetArrowPoint2.getY());

         _arrowTransform.setToRotation(-Math.PI / 2, _targetArrowPoint.getX(), _targetArrowPoint.getY());
         _arrowTransform.transform(_temp, _targetCardinalityPoint);

         _arrowTransform.setToRotation(Math.PI / 2, _targetArrowPoint.getX(), _targetArrowPoint.getY());
         _arrowTransform.transform(_temp, _targetFkPoint);
      }
   }


   /**
    * Gets the Self attribute of the RelationLine object
    *
    * @return The Self value
    */
   private final boolean isSelf() {
      return _originTable == _targetTable;
   }


   /**
    * Gets the cardinality Image to display. It's a 1 or a *, black or white.
    */
   private void drawCardinality(Graphics2D g ) {
      // This is quite dirty, but it works
       Image originImage;
       Image targetImage;
      if (g.getColor() == Color.white) {
          originImage = _foreignKey.isManyB() ? _starSelectedImage : _oneSelectedImage;
          targetImage = _oneSelectedImage;
      }
      else {
          originImage = _foreignKey.isManyB() ? _starImage : _oneImage;
          targetImage = _oneImage;
      }

       drawImage(g, originImage, _originCardinalityPoint.x - 8, _originCardinalityPoint.y - 8);
       drawImage(g, targetImage, _targetCardinalityPoint.x - 8, _targetCardinalityPoint.y - 8);
   }

   private final void drawImage(final Graphics2D g, final Image i, final float x, final float y) {
      if (!Float.isNaN(x)) {
         g.drawImage(i, (int)x, (int)y, null);
      }
   }

   private final void drawLineUnlessItsLengthIsZero(final Graphics2D g, final Line2D line) {
      // Any coord is NaN if the length is zero
      if (!Double.isNaN(line.getP1().getX())) {
         g.draw(line);
      }
   }

   private static Line2D.Float[] createLines(int n) {
      Line2D.Float[] result = new Line2D.Float[n];
      for (int i = 0; i < n; i++) {
         result[i] = new Line2D.Float();
      }
      return result;
   }

   static {
      _renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      _renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
   }
}

