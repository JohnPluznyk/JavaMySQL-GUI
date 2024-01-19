import javax.swing.JFrame;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
//import javax.swing.table.TableColumn;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class sqlGUI extends JFrame{

    JPanel panel = new JPanel();
    //JList list = new JList<String>();  //will need a get ListMethod to get the list of tables from the database
    JList<String> list;
    JTable table = new JTable();
    JScrollPane sp = new JScrollPane(table);

    DefaultTableModel dt;

//  I think i need to create a method that will upadte the table model so that if tables are different the table model will adapt to the selected table


    public sqlGUI(){  //constructor  //constructing the JFrame
        final int WINDOW_WIDTH = 1000;
        final int WINDOW_HEIGHT = 500;

        setTitle("Java_MySQL.app");
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFocusable(true);
        //
        getList();  //update the list with the tables from databases
        panel.add(list);  //add the list to the panel
        ///////////////////////////Dealing With Table/////////////////////

        panel.add(sp);  //add the table to panel

        ///////////////////////////////////////////////////////
        add(panel);  //add the panel to the frame
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        list.addKeyListener(new KeyAdapter() {  //key listener for the list
            public void keyPressed(KeyEvent e){
                int KeyCode = e.getKeyCode();
                if(KeyCode == KeyEvent.VK_ENTER){
                    String currTable = list.getSelectedValue();
                    System.out.println("Selected: " + list.getSelectedValue());
                    getTable(list.getSelectedValue());  //get the corresponding table 
                    dt = (DefaultTableModel)table.getModel();  //still not working but defiently moving in the right diretion as the table needs to be updated each time enter is pressed
                    //now we want to add rows with the right data
                    getRows(currTable);
                }
            }
        });

    }

    void getList(){
        // need to figure out if I can use trans.java to get the tables and store them in the array
        // String[] tables = {"csc100", "csc200", "csc300", "csc321"}; for testing

        //we want to return the list of tables within the Database
        list = new JList<String>(Trans.listTables());
    }
/* Might not even be necessary
    void updateTable(){
        
    }
*/    

//TODO: FIX getTable / getLables(String x)
    void getTable(String x){  //the parameter should take in the table that the user pressed enter on
        //sp = new JScrollPane(table);
        String[] columns;

        table.setModel(new DefaultTableModel(
            new Object [][]{

            },
            new String []{  //this needs to be set so it gets the parameter of the selected table
                //"null", "null", "null"
                //Trans.getLables(x)  //should return type string  //DEFIENTLY HAVE AN ERROR IN THIS METHOD/
            }
        ));
        dt = (DefaultTableModel)table.getModel();

        columns = Trans.getColumns(x);
        for(int i = 0; i < columns.length; i++){
            dt.addColumn(columns[i]);
        }
    }
    
    void getRows(String x){
        String[][] row = Trans.readAll(x);  // we need to figure out how to store this either 2d array or maybe just 1d array with data in each cell corresponding to their row
        for (int i = 0; i < row.length; i++) {
            dt.addRow(row[i]);
        }
    }    
}