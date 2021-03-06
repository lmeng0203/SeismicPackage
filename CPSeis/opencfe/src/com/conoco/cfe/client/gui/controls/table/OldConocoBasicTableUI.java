///
/// OldConocoBasicTableUI.java
///
///     Date       Author   Alterations
///----------------------------------------------------------------------------
///  5. 08-02-2002 SMCook   Modified isSpecialRedrawCase logic (1.4 migration).
///  4. 10-08-2001 SMCook   Fixed Windows platform paint bug in preparation for
///                          cutting new CD -- commented out logic causing
///                          isSpecialRedrawCase to be 'true' for Windows
///                          platform.  Change is related to shift to 1.1.8.
///  3. 10-04-2001 SMCook   Reinstated this class in code tree (and used in
///                          class ArrayComponent as before).
///

package com.conoco.cfe.client.gui.controls.table;

import com.conoco.shared.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.CellEditor;
import javax.swing.CellRendererPane;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableCellRenderer;

import javax.swing.event.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.awt.event.*;
import java.awt.*;
import javax.swing.plaf.*;
import java.util.EventObject;

import javax.swing.text.*;

public class OldConocoBasicTableUI extends TableUI{

//
// Instance Variables
//

  // The JTable that is delegating the painting to this UI.
  protected JTable table;
  protected CellRendererPane rendererPane;

  // Listeners that are attached to the JTable
  protected KeyListener keyListener;
  protected FocusListener focusListener;
  protected MouseInputListener mouseInputListener;  

  /**
   * Logger info/debug statements.
   */
  private boolean VERBOSE = false;

//
// SMCook added isSpecialRedrawCase flag and constructor
//  to fix platform-specific, JDK-specific JTable rendering bug.
//
// Ascertain os.name and paintComponents accordingly.
//
  private boolean isSpecialRedrawCase=false;

  public OldConocoBasicTableUI() {
    String operatingSys=System.getProperty("os.name");
    if(operatingSys.indexOf("SunOS") >= 0)
      isSpecialRedrawCase=true;
    else if(operatingSys.indexOf("Windows") >= 0)
      isSpecialRedrawCase=true;
    else if(operatingSys.indexOf("Linux") >= 0)
      isSpecialRedrawCase=true;
  }

//
//  Helper class for keyboard actions
//

  private class EnterAction extends NavigationalAction {
    
    private EnterAction(int dx, int dy, boolean toggle, boolean extend, 
                        boolean moveAnchor, boolean inSelection) {
    super(dx, dy, toggle, extend, moveAnchor, inSelection);
    }
        
    public void actionPerformed(ActionEvent e) { 
      ListSelectionModel rsm = table.getSelectionModel(); 
      anchorRow =    rsm.getAnchorSelectionIndex(); 
      leadRow =      rsm.getLeadSelectionIndex(); 

      ListSelectionModel csm = table.getColumnModel().getSelectionModel(); 
      anchorColumn = csm.getAnchorSelectionIndex(); 
      leadColumn =   csm.getLeadSelectionIndex(); 

      int oldAnchorRow = anchorRow; 
      int oldAnchorColumn = anchorColumn; 

      // If there is only one selected cell, there is no point 
      // in trying to stay within the selection - move the 
      // selection instead. 
      boolean noWhereToGo = (selectionSpan(rsm)*selectionSpan(csm) == 1);

      if (!inSelection || noWhereToGo) {
        if (moveAnchor) {        
          if ( anchorRow == (table.getRowCount()-1) ) {
            anchorRow = 0;  
            updateSelection(anchorRow, anchorColumn, false, extend);
          }
          else {
            anchorRow    = anchorRow + dy; 
            anchorColumn = anchorColumn + dx; 
            if (inTableRange(anchorRow, anchorColumn)) { 
              updateSelection(anchorRow, anchorColumn, false, extend);
            }
          }
        }
        else {
          leadRow    = leadRow + dy; 
          leadColumn = leadColumn + dx; 
          if (inTableRange(leadRow, leadColumn)) {
            updateSelection(leadRow, leadColumn, false, extend);
          }
        }
      }
      else {
        anchorRow = anchorRow + dy; 
        anchorColumn = anchorColumn + dx; 
        limitToSelectedRange(false); 
        rsm.setAnchorSelectionIndex(anchorRow); 
        csm.setAnchorSelectionIndex(anchorColumn); 
      }
      if (table.isEditing() && (oldAnchorRow    != rsm.getAnchorSelectionIndex() || 
             oldAnchorColumn != csm.getAnchorSelectionIndex())) {
        table.getCellEditor().stopCellEditing();
      }
      if ( (noWhereToGo) && (table.getRowCount() == 1) ) {
        if ( table.isEditing() ) {
          table.getCellEditor().stopCellEditing();
        }
      }
    }
  }

  private class NavigationalAction extends AbstractAction {
    protected int dx;
    protected int dy;
    protected boolean toggle; 
    protected boolean extend; 
    protected boolean moveAnchor; 
    protected boolean inSelection;
    protected int anchorRow; 
    protected int anchorColumn; 
    protected int leadRow; 
    protected int leadColumn;

    protected NavigationalAction(int dx, int dy, boolean toggle, boolean extend, 
                boolean moveAnchor, boolean inSelection) {
      this.dx = dx;
      this.dy = dy; 
      this.toggle = toggle; 
      this.extend = extend; 
      this.moveAnchor = moveAnchor; 
      this.inSelection = inSelection; 
    }

    protected int clipToRange(int i, int a, int b) { 
      return Math.min(Math.max(i, a), b-1);
    }

     protected void moveWithinTableRange(JTable table, int dx, int dy, boolean changeLead) { 
      if (changeLead) { 
        leadRow = clipToRange(leadRow+dy, 0, table.getRowCount()); 
        leadColumn = clipToRange(leadColumn+dx, 0, table.getColumnCount()); 
       }
       else { 
        anchorRow = clipToRange(anchorRow+dy, 0, table.getRowCount()); 
        anchorColumn = clipToRange(anchorColumn+dx, 0, table.getColumnCount()); 
       }
    }
    
    protected boolean inRange(int index, int min, int max) { 
      return index >= min && index < max; 
    }

    protected int selectionSpan(ListSelectionModel sm) { 
      return sm.getMaxSelectionIndex() - sm.getMinSelectionIndex() + 1;
    }

    protected int compare(int i, ListSelectionModel sm) { 
      return compare(i, sm.getMinSelectionIndex(), sm.getMaxSelectionIndex()+1); 
    }
    
    
    protected int compare(int i, int a, int b) { 
      return (i < a) ? -1 : (i >= b) ? 1 : 0 ; 
    }

    protected boolean moveWithinSelectedRange(JTable table, int dx, int dy, boolean ignoreCarry) {
      ListSelectionModel rsm = table.getSelectionModel(); 
      ListSelectionModel csm = table.getColumnModel().getSelectionModel(); 
        
      int newAnchorRow =    anchorRow + dy;  
      int newAnchorColumn = anchorColumn + dx; 
        
      int rowSgn; 
      int colSgn; 
      int rowCount = selectionSpan(rsm); 
      int columnCount = selectionSpan(csm); 
        
      boolean canStayInSelection = (rowCount * columnCount > 1); 
      if (canStayInSelection) { 
        rowSgn = compare(newAnchorRow, rsm); 
        colSgn = compare(newAnchorColumn, csm); 
      }
      else { 
        rowCount = table.getRowCount(); 
        columnCount = table.getColumnCount(); 
        rowSgn = compare(newAnchorRow, 0, rowCount); 
        colSgn = compare(newAnchorColumn, 0, columnCount); 
      }
        
      anchorRow    = newAnchorRow - rowCount * rowSgn;  
      anchorColumn = newAnchorColumn - columnCount * colSgn; 
        
      if (!ignoreCarry) {
        return moveWithinSelectedRange(table, rowSgn, colSgn, true); 
      }
      return canStayInSelection; 
    }

    protected int indexSign(int index, ListSelectionModel sm) { 
      if (index < sm.getMinSelectionIndex()) {
        return -1; 
      }
      if (index > sm.getMaxSelectionIndex()) {
        return 1; 
      }
      return 0; 
    }

    protected boolean inTableRange(int row, int col) {
      return (inRange(row, 0, table.getRowCount()) &&
              inRange(col, 0, table.getColumnCount())); 
    }

    protected void limitToSelectedRange(boolean ignoreCarry) {
      ListSelectionModel rsm = table.getSelectionModel(); 
      ListSelectionModel csm = table.getColumnModel().getSelectionModel(); 
  
      int rowSgn = indexSign(anchorRow, rsm); 
      int colSgn = indexSign(anchorColumn, csm); 
      anchorRow =    anchorRow - selectionSpan(rsm) * rowSgn;  
      anchorColumn = anchorColumn - selectionSpan(csm) * colSgn; 
      if (!ignoreCarry) {
        anchorRow = anchorRow + colSgn; 
        anchorColumn = anchorColumn + rowSgn; 
        limitToSelectedRange(true); 
      }
    }

    public void actionPerformed(ActionEvent e) { 
      JTable table = (JTable)e.getSource(); 
      ListSelectionModel rsm = table.getSelectionModel(); 
      anchorRow =    rsm.getAnchorSelectionIndex(); 
      leadRow =      rsm.getLeadSelectionIndex(); 

      ListSelectionModel csm = table.getColumnModel().getSelectionModel(); 
      anchorColumn = csm.getAnchorSelectionIndex(); 
      leadColumn =   csm.getLeadSelectionIndex(); 

      int oldAnchorRow = anchorRow; 
      int oldAnchorColumn = anchorColumn; 

      if (!inSelection) { 
        moveWithinTableRange(table, dx, dy, extend); 
        if (!extend) {
          updateSelection( anchorRow, anchorColumn, false, extend);
        }
        else {
          updateSelection(leadRow, leadColumn, false, extend);
        }
      }
      else {
        if (moveWithinSelectedRange(table, dx, dy, false)) { 
          rsm.setAnchorSelectionIndex(anchorRow); 
          csm.setAnchorSelectionIndex(anchorColumn);
        }
        else {
          updateSelection( anchorRow, anchorColumn, false, false); 
        }
      }
  
      if (table.isEditing() && (oldAnchorRow    != rsm.getAnchorSelectionIndex() || 
        oldAnchorColumn != csm.getAnchorSelectionIndex())) {
        table.getCellEditor().stopCellEditing();
      }
    }
  }


/*=================================================================    
Code changed for fixing bug 110
===================================================================


    public void actionPerformed(ActionEvent e) { 
      JTable table = (JTable)e.getSource(); 
      ListSelectionModel rsm = table.getSelectionModel(); 
      anchorRow =    rsm.getAnchorSelectionIndex(); 
      leadRow =      rsm.getLeadSelectionIndex(); 

      ListSelectionModel csm = table.getColumnModel().getSelectionModel(); 
      anchorColumn = csm.getAnchorSelectionIndex(); 
      leadColumn =   csm.getLeadSelectionIndex(); 

      int oldAnchorRow = anchorRow; 
      int oldAnchorColumn = anchorColumn; 

      // If there is only one selected cell, there is no point 
      // in trying to stay within the selection - move the 
      // selection instead. 
      boolean noWhereToGo = (selectionSpan(rsm)*selectionSpan(csm) == 1);

      if (!inSelection || noWhereToGo) {
        if (moveAnchor) {        
          anchorRow    = anchorRow + dy; 
          anchorColumn = anchorColumn + dx; 
          if (inTableRange(anchorRow, anchorColumn)) { 
            updateSelection(anchorRow, anchorColumn, false, extend);
          }
          if ( anchorRow == (table.getRowCount()) ) {

            anchorRow = 0;  
            updateSelection(anchorRow, anchorColumn, false, extend);
          }
        }
        else {
          leadRow    = leadRow + dy; 
          leadColumn = leadColumn + dx; 
          if (inTableRange(leadRow, leadColumn)) {
            updateSelection(leadRow, leadColumn, false, extend);
          }
        }
      }
      else {
        anchorRow = anchorRow + dy; 
        anchorColumn = anchorColumn + dx; 
        limitToSelectedRange(false); 
        rsm.setAnchorSelectionIndex(anchorRow); 
        csm.setAnchorSelectionIndex(anchorColumn); 
      }
      if (table.isEditing() && (oldAnchorRow    != rsm.getAnchorSelectionIndex() || 
             oldAnchorColumn != csm.getAnchorSelectionIndex())) {
        table.getCellEditor().stopCellEditing();
      }
    }
============================================================================*/

//
//  The Table's Key listener
//

  /**
   * This inner class is marked &quot;public&quot; due to a compiler bug.
   * This class should be treated as a &quot;protected&quot; inner class.
   * Instantiate it only within subclasses of BasicTableUI.
   */
  public class KeyHandler implements KeyListener {
    private Component dispatchComponent;
      
    private void setDispatchComponent(KeyEvent e) { 
        Component editorComponent = table.getEditorComponent();
        int row = table.getSelectedRow();
        int column = table.getSelectedColumn();
        Point p = new Point(row, column);
        Point p2 = SwingUtilities.convertPoint(table, p, editorComponent);
        dispatchComponent = SwingUtilities.getDeepestComponentAt(editorComponent, 
                                                                 p2.x, p2.y);
    }
    private boolean repostEvent(KeyEvent e) { 
      if (dispatchComponent == null) {
        return false;
      }
      dispatchComponent.dispatchEvent(e); 
      return true;
    }

    private void setValueIsAdjusting(boolean flag) {
      table.getSelectionModel().setValueIsAdjusting(flag); 
      table.getColumnModel().getSelectionModel().setValueIsAdjusting(flag);
    }
             
    public void keyPressed(KeyEvent e) {       
      int row = table.getSelectedRow();
      int column = table.getSelectedColumn();
      // The autoscroller can generate drag events outside the Table's range. 
      if ((column == -1) || (row == -1)) {
        return;
      }
      if (e.isControlDown()){
        return;
      }
      if (e.isShiftDown()){
        return;
      }
      if (e.getKeyCode() == KeyEvent.VK_UP
         || e.getKeyCode() == KeyEvent.VK_DOWN
         || e.getKeyCode() == KeyEvent.VK_LEFT
         || e.getKeyCode() == KeyEvent.VK_RIGHT) {
        return;
      }
      if (table.editCellAt(row, column, e)) {
        setDispatchComponent(e); 
        repostEvent(e);
      }
      else { 
        table.requestFocus();
      }
      CellEditor editor = table.getCellEditor(); 
      if (editor == null || editor.shouldSelectCell(e)) { 
        setValueIsAdjusting(true);
        updateSelection(row, column, e.isControlDown(), e.isShiftDown());
      }
    }
       
    public void keyReleased(KeyEvent e) { 
      if (e.getKeyCode() == KeyEvent.VK_CONTROL ) {
        setValueIsAdjusting(false);
      }
      if (e.getKeyCode() == KeyEvent.VK_SHIFT )  {
        setValueIsAdjusting(false);
      }
      if (e.getKeyCode() == KeyEvent.VK_UP
         || e.getKeyCode() == KeyEvent.VK_DOWN
         || e.getKeyCode() == KeyEvent.VK_LEFT
         || e.getKeyCode() == KeyEvent.VK_RIGHT) {
          setValueIsAdjusting(true);
      }
     }

    public void keyTyped(KeyEvent e) { 
      if (e.getKeyChar() == '\t' || e.getKeyChar() == '\n' ) {
        return; 
      }
      int selectedRow = table.getSelectedRow();
      int selectedColumn = table.getSelectedColumn();
      if (selectedRow != -1 && selectedColumn != -1 && !table.isEditing()) {
         boolean editing = table.editCellAt(selectedRow, selectedColumn);
         table.requestFocus();
         if (!editing) {
           return;
        }
      }
            
      // Forwarding events this way seems to put the textfield 
      // in a state where it believes it has focus. In reality 
      // the table retains focus - though it is difficult for 
      // a user to tell, since the caret is visible and flashing. 
          
      // Calling table.requestFocus() here, to get the focus back to 
      // the table, seems to have no effect. 
            
      Component editorComp = table.getEditorComponent();
      if (table.isEditing() && editorComp != null) {
        // Have to give the textField the focus temporarily so
        // that it can perform the action.
        char keyChar = e.getKeyChar();
        if (editorComp instanceof JTextField) {
          JTextField textField = (JTextField)editorComp;
          Keymap keyMap = textField.getKeymap();
          KeyStroke key = KeyStroke.getKeyStroke(keyChar, 0);
          Action action = keyMap.getAction(key);
          if (action == null) {
            action = keyMap.getDefaultAction();
          }
          if (action != null) {
            ActionEvent ae = new ActionEvent(textField,
                                   ActionEvent.ACTION_PERFORMED,
                                    String.valueOf(keyChar));
            action.actionPerformed(ae);
            editorComp.repaint();
            e.consume();
          }
        }
      }
    }
  }

//
//  The Table's focus listener
//
  /**
   * This inner class is marked &quot;public&quot; due to a compiler bug.
   * This class should be treated as a &quot;protected&quot; inner class.
   * Instantiate it only within subclasses of BasicTableUI.
   */
  public class FocusHandler implements FocusListener {
     private void repaintAnchorCell() {
      int anchorRow = table.getSelectedRow();
      int anchorColumn = table.getSelectedColumn();
      Rectangle dirtyRect = table.getCellRect(anchorRow, anchorColumn, false);
      table.repaint(dirtyRect);
    }
    public void focusGained(FocusEvent e) {
      Logger.println(this, "focus gained");
      if (table.getRowCount() <= 0){
        return;
      }
      if (   table.getSelectedRow() == -1 || table.getSelectedColumn() == -1 ){
        return;
      }
      repaintAnchorCell();
    }
     public void focusLost(FocusEvent e) {
      Logger.println(this, "focus lost");
      repaintAnchorCell();
    }
  }

//
//  The Table's mouse and mouse motion listeners
//
  /**
   * This inner class is marked &quot;public&quot; due to a compiler bug.
   * This class should be treated as a &quot;protected&quot; inner class.
   * Instantiate it only within subclasses of BasicTableUI.
   */
  public class MouseInputHandler implements MouseInputListener {
    // Workaround for mousePressed bug in AWT 1.1
    private boolean phantomMousePressed = false;
    // Component recieving mouse events during editing. May not be editorComponent.
    private Component dispatchComponent;
    //  The Table's mouse listener methods.
    public void mouseClicked(MouseEvent e) { 
    }
    private void setDispatchComponent(MouseEvent e) { 
      Component editorComponent = table.getEditorComponent();
      Point p = e.getPoint();
      Point p2 = SwingUtilities.convertPoint(table, p, editorComponent);
      dispatchComponent = SwingUtilities.getDeepestComponentAt(editorComponent, 
                                                                 p2.x, p2.y);
    }
    private boolean repostEvent(MouseEvent e) { 
       if (dispatchComponent == null) {
         return false; 
       }
       MouseEvent e2 = SwingUtilities.convertMouseEvent(table, e, dispatchComponent);
       dispatchComponent.dispatchEvent(e2); 
       return true; 
    }

    private void setValueIsAdjusting(boolean flag) {
      table.getSelectionModel().setValueIsAdjusting(flag); 
      table.getColumnModel().getSelectionModel().setValueIsAdjusting(flag); 
    }
        
    public void mousePressed(MouseEvent e) {
      if (!SwingUtilities.isLeftMouseButton(e)) {
        return;
      }
      if (phantomMousePressed == true) {
        return;
      }
      phantomMousePressed = true;

      Point p = e.getPoint();
      int row = table.rowAtPoint(p);
      int column = table.columnAtPoint(p);
      // The autoscroller can generate drag events outside the Table's range. 
      if ((column == -1) || (row == -1)) {
        return;
      }
      if (table.editCellAt(row, column, e)) {
        setDispatchComponent(e); 
        repostEvent(e);
      } 
      else { 
        table.requestFocus();
      }
      CellEditor editor = table.getCellEditor(); 
      if (editor == null || editor.shouldSelectCell(e)) { 
        setValueIsAdjusting(true);
        updateSelection( row, column, e.isControlDown(), e.isShiftDown());  
      }
    }

    /* Adjust the selection if the event was not forwarded 
     * to the editor above *or* the editor declares that it 
     * should change selection even when events are forwarded 
     * to it. 
     */
    public void mouseReleased(MouseEvent e) {
      if (!SwingUtilities.isLeftMouseButton(e)) {
        return;
      }
      if (e.isControlDown()){
        phantomMousePressed = false;
        repostEvent(e); 
        dispatchComponent = null;
        setValueIsAdjusting(true);
      }
      else if (e.isShiftDown()){
        phantomMousePressed = false;
        repostEvent(e); 
        dispatchComponent = null;
        setValueIsAdjusting(true);
      }
      else {
        phantomMousePressed = false;
        repostEvent(e); 
        dispatchComponent = null;
        setValueIsAdjusting(false);
      }
    }
        
    public void mouseEntered(MouseEvent e) {
      dispatchComponent = null;
    }

    public void mouseExited(MouseEvent e) {
      dispatchComponent = null;
    }

    //  The Table's mouse motion listener methods.
    public void mouseMoved(MouseEvent e) {
      dispatchComponent = null;
    }

    public void mouseDragged(MouseEvent e) {
      if (!SwingUtilities.isLeftMouseButton(e)) {
          return;
      }
      repostEvent(e); 

      CellEditor editor = table.getCellEditor();         
      if (editor == null || editor.shouldSelectCell(e)) {
        Point p = e.getPoint();
        int row = table.rowAtPoint(p);
        int column = table.columnAtPoint(p);
        // The autoscroller can generate drag events outside the Table's range. 
        if ((column == -1) || (row == -1)) {
          return;
        }
        updateSelection( row, column, false, true); 
      }
    }
  }
    
//
//  Factory methods for the Listeners
//

  /**
   * Creates the key listener for handling keyboard navigation in the JTable.
   */
  protected KeyListener createKeyListener() {
    return new KeyHandler();
  }

  /**
   * Creates the focus listener for handling keyboard navigation in the JTable.
   */
  protected FocusListener createFocusListener() {
    return new FocusHandler();
  }

  /**
   * Creates the mouse listener for the JTable.
   */
  protected MouseInputListener createMouseInputListener() {
    return new MouseInputHandler();
  }

//
//  The installation/uninstall procedures and support
//
  public static ComponentUI createUI(JComponent c) {
    return new OldConocoBasicTableUI();
  }

  //  Installation
  public void installUI(JComponent c) {
    table = (JTable)c;
    rendererPane = new CellRendererPane();
    table.add(rendererPane);
    installDefaults();
    installListeners();
    installKeyboardActions();
  }

  /**
   * Initialize JTable properties, e.g. font, foreground, and background.
   * The font, foreground, and background properties are only set if their
   * current value is either null or a UIResource, other properties are set
   * if the current value is null.
   *
   * @see #installUI
   */
  protected void installDefaults() {
    LookAndFeel.installColorsAndFont(table, "Table.background",
                                         "Table.foreground", "Table.font");

    Color sbg = table.getSelectionBackground();
    if (sbg == null || sbg instanceof UIResource) {
      table.setSelectionBackground(UIManager.getColor("Table.selectionBackground"));
    }
    Color sfg = table.getSelectionForeground();
    if (sfg == null || sfg instanceof UIResource) {
      table.setSelectionForeground(UIManager.getColor("Table.selectionForeground"));
    }
    Color gridColor = table.getGridColor();
      if (gridColor == null || gridColor instanceof UIResource) {
        table.setGridColor(UIManager.getColor("Table.gridColor"));
      }
    // install the scrollpane border
    Container parent = table.getParent();  // should be viewport
    if (parent != null) {
      parent = parent.getParent();  // should be the scrollpane
      if (parent != null && parent instanceof JScrollPane) {
        LookAndFeel.installBorder((JScrollPane)parent, "Table.scrollPaneBorder");
      }
    }
  }

  /**
   * Attaches listeners to the JTable.
   */
  protected void installListeners() {
    focusListener = createFocusListener();
    keyListener = createKeyListener();
    mouseInputListener = createMouseInputListener();
    table.addFocusListener(focusListener);
    table.addKeyListener(keyListener);
    addMouseListeners();
  }
 
  protected void addMouseListeners() {
    table.addMouseListener(mouseInputListener);
    table.addMouseMotionListener(mouseInputListener);
  }
   
  protected void removeMouseListeners() {
    table.removeMouseListener(mouseInputListener);
    table.removeMouseMotionListener(mouseInputListener);
  }

  private void registerKey(int keyEvent, int mask, int dx, int dy) { 
    boolean toggle = (mask & ActionEvent.CTRL_MASK) != 0;
    boolean extend = (mask & ActionEvent.SHIFT_MASK) != 0; 
    boolean moveAnchor = !extend; 
    registerKey(keyEvent, mask, dx, dy, toggle, extend, moveAnchor, false);
  }

  private void registerKey(int keyEvent, int mask, int dx, int dy, 
                            boolean toggle, boolean extend, 
                            boolean moveAnchor, boolean inSelection) { 
    table.registerKeyboardAction(new NavigationalAction(dx, dy, toggle, 
                                extend, moveAnchor, inSelection),
                                KeyStroke.getKeyStroke(keyEvent, mask), 
                                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
  }

  private void registerEnterKey(int keyEvent, int mask, int dx, int dy, 
                                boolean toggle, boolean extend, 
                                boolean moveAnchor, boolean inSelection) { 
    table.registerKeyboardAction(new EnterAction(dx, dy, toggle, 
                                extend, moveAnchor, inSelection),
                                 KeyStroke.getKeyStroke(keyEvent, mask), 
                                 JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
  }

  /**
   * Register all keyboard actions on the JTable.
   */
  protected void installKeyboardActions() {
    int shift = ActionEvent.SHIFT_MASK; 
    int ctrl =  ActionEvent.CTRL_MASK; 
    
    registerKey(KeyEvent.VK_RIGHT,     0,  1,  0, true, false, true, true);
    registerKey(KeyEvent.VK_LEFT ,     0, -1,  0, true, false, true, true);
    registerKey(KeyEvent.VK_DOWN ,     0,  0,  1, true, false, true, true);
    registerKey(KeyEvent.VK_UP   ,     0,  0, -1, true, false, true, true);
    
    registerKey(KeyEvent.VK_RIGHT, shift,  1,  0);
    registerKey(KeyEvent.VK_LEFT , shift, -1,  0);
    registerKey(KeyEvent.VK_DOWN , shift,  0,  1);
    registerKey(KeyEvent.VK_UP   , shift,  0, -1);

    registerKey(KeyEvent.VK_TAB  ,     0,  1,  0, true, false, true, true);
    registerKey(KeyEvent.VK_TAB  , shift, -1,  0, true, false, true, true);
    registerEnterKey(KeyEvent.VK_ENTER,     0,  0,  1, true, false, true, true);
    registerEnterKey(KeyEvent.VK_ENTER, shift,  0, 1, true, false, true, true); 

    table.registerKeyboardAction(new AbstractAction() {
                                    public void actionPerformed(ActionEvent e) { 
                                      table.selectAll(); 
                                    }
                                  },
                                  KeyStroke.getKeyStroke(KeyEvent.VK_A, ctrl), 
                                  JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT); 

    table.registerKeyboardAction(new AbstractAction() {
                                    public void actionPerformed(ActionEvent e) { 
                                      table.removeEditor(); 
                                    }
                                  },
                                  KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), 
                                  JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT); 
  }

  //  Uninstallation
  public void uninstallUI(JComponent c) {
    uninstallDefaults();
    uninstallListeners();
    uninstallKeyboardActions();
    table.remove(rendererPane);
    rendererPane = null;
    table = null;
  }

  protected void uninstallDefaults() {
  }

  protected void uninstallListeners() {
    table.removeFocusListener(focusListener);
    table.removeKeyListener(keyListener);
    table.removeMouseListener(mouseInputListener);
    table.removeMouseMotionListener(mouseInputListener);
    focusListener = null;
    keyListener = null;
    mouseInputListener = null;
  }

  protected void uninstallKeyboardActions() {
    int shift = ActionEvent.SHIFT_MASK; 
    int ctrl =  ActionEvent.CTRL_MASK; 
    table.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_UP   , 0));
    table.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN , 0));
    table.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0));
    table.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT , 0));

    table.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_UP   , shift));
    table.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN , shift));
    table.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, shift));
    table.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT , shift));

    table.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_TAB  ,     0));
    table.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_TAB  , shift));
    table.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,     0));
    table.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, shift));

    table.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_A     , ctrl));
    table.unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0)); 
  }

  //
  // Size Methods
  //
  private Dimension createTableSize(long width) {
    int height;
    if(isSpecialRedrawCase)    //SMCook
      height = table.getRowCount() * (table.getRowHeight() - 1 +
                      table.getRowMargin()); 
    else
      height = table.getRowCount() * (table.getRowHeight() +
                      table.getRowMargin()); 

    int totalMarginWidth =  table.getColumnModel().getColumnMargin() * 
                      table.getColumnCount(); 
      // Width is always positive. The call to abs() is a workaround for 
      // a bug in the 1.1.6 JIT on Windows. 
    long widthWithMargin = Math.abs(width) + totalMarginWidth;
    if (widthWithMargin > Integer.MAX_VALUE) {
      widthWithMargin = Integer.MAX_VALUE;
    }
    return new Dimension((int)widthWithMargin, height);
  }

  /**
   * Return the minimum size of the table. The minimum height is the 
   * row height (plus inter-cell spacing) times the number of rows. 
   * The minimum width is the sum of the minimum widths of each column 
   * (plus inter-cell spacing).
   */
  public Dimension getMinimumSize(JComponent c) {
//
//SMCook changed so last column is a bit wider 
//
    long width = 0;
    Enumeration enumeration = table.getColumnModel().getColumns();
    TableColumn aColumn = null;
    while (enumeration.hasMoreElements()) {
      aColumn = (TableColumn)enumeration.nextElement();
      width = width + aColumn.getMinWidth();
      //width = width + aColumn.getMinWidth() + 2;  //SMCook
    }
//    if(aColumn != null) width = width + aColumn.getMinWidth()/2;
    return createTableSize(width);
  }

  /**
   * Return the preferred size of the table. The preferred height is the 
   * row height (plus inter-cell spacing) times the number of rows. 
   * The preferred width is the sum of the preferred widths of each column 
   * (plus inter-cell spacing).
   */
  public Dimension getPreferredSize(JComponent c) {
//
//SMCook changed so last column is a bit wider 
//
    long width = 0;
    Enumeration enumeration = table.getColumnModel().getColumns();
    TableColumn aColumn=null;
    while (enumeration.hasMoreElements()) {
      aColumn = (TableColumn)enumeration.nextElement();
      width = width + aColumn.getPreferredWidth();
    }
//    if(aColumn != null) width = width + aColumn.getPreferredWidth()/2;
    return createTableSize(width);
  }

  /**
   * Return the maximum size of the table. The maximum height is the 
   * row height (plus inter-cell spacing) times the number of rows. 
   * The maximum width is the sum of the maximum widths of each column 
   * (plus inter-cell spacing).
   */
  public Dimension getMaximumSize(JComponent c) {
//
//SMCook changed so last column is a bit wider 
//
    long width = 0;
    Enumeration enumeration = table.getColumnModel().getColumns();
    TableColumn aColumn = null;
    while (enumeration.hasMoreElements()) {
      aColumn = (TableColumn)enumeration.nextElement();
      width = width + aColumn.getMaxWidth();
    }
//    if(aColumn != null) width = width + aColumn.getMaxWidth()/2;
    return createTableSize(width);
  }

  //
  //  Paint methods and support
  //
  /** Paint a representation of the <code>table</code> instance
   * that was set in installUI().
   */
  public synchronized void paint(Graphics g, JComponent c) {
    Rectangle oldClipBounds = g.getClipBounds();
    Rectangle clipBounds = new Rectangle(oldClipBounds);
    int tableWidth = table.getColumnModel().getTotalColumnWidth();

    clipBounds.width = Math.min(clipBounds.width, tableWidth);
    g.setClip(clipBounds);

    // Paint the grid
    paintGrid(g);

    // Paint the rows
    int firstIndex = table.rowAtPoint(new Point(0, clipBounds.y));
    int  lastIndex = lastVisibleRow(clipBounds);

    int height;
    if(isSpecialRedrawCase) //SMCook
      height=table.getRowHeight() + table.getRowMargin() - 1;
    else
      height=table.getRowHeight() + table.getRowMargin();

    Rectangle rowRect = new Rectangle(0, 0, tableWidth, height);

    rowRect.y = firstIndex*rowRect.height;
    for (int index = firstIndex; index <= lastIndex; index++) {
      // Paint any rows that need to be painted
      if (rowRect.intersects(clipBounds)) {
        paintRow(g, index);
      }
      rowRect.y += rowRect.height;
    }
    g.setClip(oldClipBounds);
  }

  /*
   * Paints the grid lines within <I>aRect</I>, using the grid
   * color set with <I>setGridColor</I>. Paints vertical lines
   * if <code>getShowVerticalLines()</code> returns true and paints
   * horizontal lines if <code>getShowHorizontalLines()</code>
   * returns true.
   */
  private void paintGrid(Graphics g) {
    g.setColor(table.getGridColor());
    if (table.getShowHorizontalLines()) {
      paintHorizontalLines(g);
    }
    if (table.getShowVerticalLines()) {
      paintVerticalLines(g);
    }
  }

  /*
   * This method paints horizontal lines regardless of whether the
   * table is set to paint one automatically.
   */
   private void paintHorizontalLines(Graphics g) {
     Rectangle r = g.getClipBounds();
     Rectangle rect = r;

     int delta;
     if(isSpecialRedrawCase)
       delta = table.getRowHeight() + table.getRowMargin() - 1;    //SMCook
     else
       delta = table.getRowHeight() + table.getRowMargin();

     int firstIndex = table.rowAtPoint(new Point(0, r.y));
     int  lastIndex = lastVisibleRow(r);
     int y = delta*firstIndex+(delta-1);
     for (int index = firstIndex; index <= lastIndex; index ++) {
       if ((y >= rect.y) && (y <= (rect.y + rect.height))) {
         g.drawLine(rect.x, y, rect.x + rect.width - 1, y);
       }
       y += delta;
     }
   }

   /*
    * This method paints vertical lines regardless of whether the
    * table is set to paint one automatically.
    */
    private void paintVerticalLines(Graphics g) {
      Rectangle rect = g.getClipBounds();

      int x = 0;
      int count = table.getColumnCount();

      int horizontalSpacing;
      if(isSpecialRedrawCase)
        horizontalSpacing=table.getIntercellSpacing().width - 1;  //SMCook for 1.4
      else
        horizontalSpacing=table.getIntercellSpacing().width;

      for (int index = 0; index <= count; index ++) {
        if ((x > 0) && (((x-1) >= rect.x) && ((x-1) <= (rect.x + rect.width)))){
          g.drawLine(x - 1, rect.y, x - 1, rect.y + rect.height - 1);
        }
        if (index < count)
          x += ((TableColumn)table.getColumnModel().getColumn(index)).
                    getWidth() + horizontalSpacing;
      }
    }

    private void paintRow(Graphics g, int row) {
      Rectangle rect = g.getClipBounds();
      int column = 0;
      boolean drawn = false;
      int draggedColumnIndex = -1;
      Rectangle draggedCellRect = null;
      Dimension spacing = table.getIntercellSpacing();
      JTableHeader header = table.getTableHeader();

      Rectangle cellRect = new Rectangle();
      if(isSpecialRedrawCase)
        cellRect.height = table.getRowHeight() + spacing.height - 1;     //SMCook
      else
        cellRect.height = table.getRowHeight() + spacing.height;

      cellRect.y = row * cellRect.height;

      Enumeration enumeration = table.getColumnModel().getColumns();

      // Paint the non-dragged table cells first
      while (enumeration.hasMoreElements()) {
        TableColumn aColumn = (TableColumn)enumeration.nextElement();
        cellRect.width = aColumn.getWidth() + spacing.width;
        if (cellRect.intersects(rect)) {
          drawn = true;
          if ((header == null) || (aColumn != header.getDraggedColumn())) {
            paintCell(g, cellRect, row, column);
          }
          else {
            // Paint a gray well in place of the moving column
            // This would be unnecessary if we drew the grid more cleverly
            g.setColor(table.getParent().getBackground());
            g.fillRect(cellRect.x, cellRect.y, cellRect.width, cellRect.height);
            draggedCellRect = new Rectangle(cellRect);
            draggedColumnIndex = column;
          }
        }
        else {
          if (drawn)
          // Don't need to iterate through the rest
            break;
        }
        cellRect.x += cellRect.width;
        column++;
      }
      
      // paint the dragged cell if we are dragging
      if (draggedColumnIndex != -1 && draggedCellRect != null) {
        draggedCellRect.x += header.getDraggedDistance();
        // Fill the background
        g.setColor(table.getBackground());
        g.fillRect(draggedCellRect.x, draggedCellRect.y,
                       draggedCellRect.width, draggedCellRect.height);
        // paint grid if necessary.
        g.setColor(table.getGridColor());
        int x1 = draggedCellRect.x;
        int y1 = draggedCellRect.y;
        int x2 = x1 + draggedCellRect.width - 1;
        int y2 = y1 + draggedCellRect.height - 1;
        if (table.getShowVerticalLines()) {
          // Left
          // g.drawLine(x1-1, y1, x1-1, y2);
          // Right
          g.drawLine(x2, y1, x2, y2);
        }
        // Bottom
        if (table.getShowHorizontalLines()) {
          g.drawLine(x1, y2, x2, y2);
        }
        // Render the cell value
          paintCell(g, draggedCellRect, row, draggedColumnIndex);
      }
    }

    private void paintCell(Graphics g, Rectangle cellRect, int row, int column) {
      // The cellRect is inset by half the intercellSpacing before painted
      int spacingHeight = table.getRowMargin();
      int spacingWidth  = table.getColumnModel().getColumnMargin();

      // Round so that when the spacing is 1 the cell does not paint obscure lines.
      cellRect.setBounds(cellRect.x + spacingWidth/2, cellRect.y + spacingHeight/2,
                           cellRect.width - spacingWidth, cellRect.height - spacingHeight);

      if (table.isEditing() && table.getEditingRow()==row &&
                                 table.getEditingColumn()==column) {
        Component component = table.getEditorComponent();
        component.setBounds(cellRect);
        component.validate();
      }
      else {
        TableCellRenderer renderer = table.getCellRenderer(row, column);
        Component component = table.prepareRenderer(renderer, row, column);
        if (component.getParent() == null) {
          rendererPane.add(component);
        }
        rendererPane.paintComponent(g, component, table, cellRect.x, cellRect.y,
                                        cellRect.width, cellRect.height, true);
      }
      // Have to restore the cellRect back to it's orginial size
      cellRect.setBounds(cellRect.x - spacingWidth/2, cellRect.y - spacingHeight/2,
                           cellRect.width + spacingWidth, cellRect.height + spacingHeight);
    }

    private int lastVisibleRow(Rectangle clip) {
      int lastIndex = table.rowAtPoint(new Point(0, clip.y + clip.height - 1));
      // If the table does not have enough rows to fill the view we'll get -1.
      // Replace this with the index of the last row.
      if (lastIndex == -1) {
        lastIndex = table.getRowCount() -1;
      }
      return lastIndex;
    }

    private void updateSelectionModel(ListSelectionModel sm, int index,
                                 boolean toggle, boolean extend) {
      if (!extend) {
        if (!toggle) {
          sm.setSelectionInterval(index, index);
         }
        else {
          if (sm.isSelectedIndex(index)) {
            sm.removeSelectionInterval(index, index);
          }
          else {
            sm.addSelectionInterval(index, index);
          }
        }
      }
      else {
        sm.setLeadSelectionIndex(index);
      }
    }

    private void updateSelection(int rowIndex, int columnIndex,
                                   boolean toggle, boolean extend) {
      // Autoscrolling support.
      Rectangle cellRect = table.getCellRect(rowIndex, columnIndex, false);
      if (cellRect != null) {
        table.scrollRectToVisible(cellRect);
      }
      ListSelectionModel rsm = table.getSelectionModel();
      ListSelectionModel csm = table.getColumnModel().getSelectionModel();

      // Update column selection model
      updateSelectionModel(csm, columnIndex, toggle, extend);

      // Update row selection model
      updateSelectionModel(rsm, rowIndex, toggle, extend);
    }
}
