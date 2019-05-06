package Fs6AnlRsltCnvrtr.ResultAnalyzer.ValuePanel;
//-*- mode:java; encoding:utf-8 -*-
// vim:set fileencoding=utf-8:
//http://ateraimemo.com/Swing/TableRowSorter.html

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;

public final class TabelPanelCreator {

    private static final Color EVEN_COLOR = new Color(250, 250, 250);
    private String[] columnNames = {"String", "Integer", "Boolean"};
    private Object[][] data = {
            {"aaa", 12, true}, {"bbb", 5, false},
            {"CCC", 92, true}, {"DDD", 0, false}
    };

    JPanel BasePanel = new JPanel(new BorderLayout());

    public TabelPanelCreator() {


        //table.setAutoCreateRowSorter(true);
        //TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(new TableRowSorter<TableModel>(model));
        //sorter.setSortKeys(java.util.Arrays.asList(new RowSorter.SortKey(0, SortOrder.DESCENDING)));

        TableCellRenderer renderer = new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean isS, boolean hasF, int row, int col) {
                TableCellRenderer r = t.getTableHeader().getDefaultRenderer();
                JLabel l = (JLabel) r.getTableCellRendererComponent(t, v, isS, hasF, row, col);
                l.setForeground(((DefaultRowSorter) table.getRowSorter()).isSortable(t.convertColumnIndexToModel(col)) ? Color.BLACK : Color.GRAY);
                return l;
            }
        };

        TableColumnModel columns = table.getColumnModel();
        for (int i = 0; i < columns.getColumnCount(); i++) {
            TableColumn c = columns.getColumn(i);
            c.setHeaderRenderer(renderer);
            if (i == 0) {
                c.setMinWidth(60);
                c.setMaxWidth(60);
                c.setResizable(false);
            }
        }

        BasePanel.add(new JCheckBox(new AbstractAction("Sortable(1, false)") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBox cb = (JCheckBox) e.getSource();
                ((DefaultRowSorter<?, ?>) table.getRowSorter()).setSortable(1, !cb.isSelected());
                table.getTableHeader().repaint();
            }
        }), BorderLayout.NORTH);

        BasePanel.add(new JButton(new AbstractAction("clear SortKeys") {
            @Override
            public void actionPerformed(ActionEvent e) {
                table.getRowSorter().setSortKeys(null);
            }
        }), BorderLayout.SOUTH);

        BasePanel.add(new JScrollPane(table));

        BasePanel.setPreferredSize(new Dimension(320, 240));
    }


    private final DefaultTableModel model = new DefaultTableModel(data, columnNames) {
        @Override
        public Class<?> getColumnClass(int column) {
            return getValueAt(0, column).getClass();
        }
    };

    private final JTable table = new JTable(model) {
        @Override
        public Component prepareRenderer(TableCellRenderer tcr, int row, int column) {
            Component c = super.prepareRenderer(tcr, row, column);
            if (isRowSelected(row)) {
                c.setForeground(getSelectionForeground());
                c.setBackground(getSelectionBackground());
            } else {
                c.setForeground(getForeground());
                c.setBackground((row % 2 == 0) ? EVEN_COLOR : getBackground());
            }
            return c;
        }
    };


    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }

    public void setData(Object[][] data) {
        this.data = data;
    }


    public static void main(String... args) {


        String rslt_file_path = "C:/Users/issey/Documents/Dropbox/docroot/CorMapper_v1.03/wd_NMLMET_vs_NMLVAL_deep+surf/network_mes_rslt_binary_undirected.txt";


        BufferedReader br = null;

        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(rslt_file_path), "UTF-8"));

            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
            }
        }


        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

    public JPanel getBasePanel() {
        return BasePanel;
    }

    public static void createAndShowGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        JFrame frame = new JFrame("TableRowSorter");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //TabelPanelCreator PanelWithTable = new TabelPanelCreator();

        TabelPanelCreator PanelWithTable = new TabelPanelCreator();

        frame.getContentPane().add(PanelWithTable.getBasePanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
