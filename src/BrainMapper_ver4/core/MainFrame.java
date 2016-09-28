package BrainMapper_ver4.core;

import BrainMapper_ver4.elements.GraphNode;
import BrainMapper_ver4.info.EastPanel;
import BrainMapper_ver4.info.WestPanel;
import BrainMapper_ver4.utils.FileFilterConstructor_Extension_ver2;
import BrainMapper_ver4.utils.FontProperty;
import BrainMapper_ver4.utils.WordReplacement;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.*;

/*
 * 作成日: 2005/08/03
 * http://null.michikusa.jp/misc/editor.html
 * 
 * 
 */

/**
 * テキストエディタ MainFrame
 */
public class MainFrame extends JFrame implements Printable {

    private JPanel jContentPane = null;
    private JMenuBar jJMenuBar = null;
    private JMenu fileMenu = null;
    private JMenuItem openMenuItem = null;
    private JMenuItem newFileMenuItem = null;
    private JMenuItem saveMenuItem = null;
    private JMenuItem saveAsMenuItem = null;
    private JMenuItem printMenuItem = null;
    private JMenuItem exitMenuItem = null;
    private JMenu encodeMenu = null;
    private JMenu openWithEncodingMenu = null;
    private JMenuItem openWithEncodingMenuItems[] = new JMenuItem[ENCODINGS.length];
    private JMenu rereadWithEncodingMenu = null;
    private JMenuItem rereadWithEncodingMenuItems[] = new JMenuItem[ENCODINGS.length];
    private JMenu saveWithEncodingMenu = null;
    private JMenuItem saveWithEncodingMenuItems[] = new JMenuItem[ENCODINGS.length];
    private JMenu editMenu = null;
    private UndoManager undoManager = new UndoManager();
    private JMenuItem undoMenuItem = null;
    private JMenuItem redoMenuItem = null;
    private JMenuItem cutMenuItem = null;
    private JMenuItem copyMenuItem = null;
    private JMenuItem pasteMenuItem = null;
    private JMenuItem deleteMenuItem = null;
    private WordReplacement wordReplacement;
    private JMenuItem replaceMenuItem = null;
    private JMenuItem replaceMenuItem2 = null;
    private JMenuItem findNextMenuItem = null;
    private JMenuItem findPrevMenuItem = null;
    private JMenuItem goToMenuItem = null;
    private JMenuItem selectAllMenuItem = null;
    private JMenu displayMenu = null;
    private JMenuItem fontMenuItem = null;
    private JMenuItem tabSizeMenuItem = null;
    private JMenu colorMenu = null;
    private JMenuItem backgroundMenuItem = null;
    private JMenuItem foregroundMenuItem = null;
    private JMenuItem caretMenuItem = null;
    private JMenuItem selectionMenuItem = null;
    private JMenuItem selectedTextMenuItem = null;
    private JCheckBoxMenuItem wrapCheckBoxMenuItem = null;
    private JCheckBoxMenuItem statusBarCheckBoxMenuItem = null;
    private JMenu helpMenu = null;
    private JMenuItem helpMenuItem = null;
    private JMenuItem aboutMenuItem = null;
    private GraphNode Node = null;

    private JPanel CenterPanel;
    private GraphFieldScrollPane GraphFieldScrollPane = null;
    private WestPanel west_panel;
    private EastPanel east_panel;
    private JPanel SouthPanel = null;

    private JLabel FootLeftLabel = null;
    private JLabel FootRightLabel = null;

    /**
     * 定数
     */
    private final static String APP_NAME = "BrainMapper";//アプリケーション名
    private final static String VERSION = "0.0.3 beta / Aug 20 2006";//アプリケーションのバージョン
    private final static int MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();//メニューショートカットのアクセラレータキー
    // ENCODINGS[i] は、ENCODING_TEXT[i] と ENCODING_MNEMONIC[i] に対応
    private final static String ENCODINGS[] = {new InputStreamReader(System.in).getEncoding(), "SJIS", "EUC_JP", "ISO2022JP", "UTF8"};//対応している文字コード
    private final static String ENCODING_TEXT[] = {"デフォルト(D)", "SJIS", "EUC", "JIS", "UTF-8"};//文字コード指定のメニューのテキスト
    private final static int ENCODING_MNEMONIC[] = {KeyEvent.VK_D, KeyEvent.VK_S, KeyEvent.VK_E, KeyEvent.VK_J, KeyEvent.VK_8};//文字コード指定のメニューのニーモニック
    private final static String MINDMAP_NODE_LIST = "GraphNodeList.xml";//GraphNodeList の設定ファイル名
    private final static String MINDMAP_FIELD_FILE = "GraphField.xml";//GraphField の設定ファイル名
    private final static String J_FRAME_FILE = "MindMapNoteFrame.xml";//jFrame の設定ファイル名
    public final static Font DEFAULT_FONT = new Font("Monospaced", Font.PLAIN, 14); //GraphNode のデフォルトのフォント

    /**
     * スターター
     *
     * @param args
     */
    public static void main(String[] args) {
        MainFrame frame = (MainFrame) SaverAndLoader.load(J_FRAME_FILE);
        if (frame == null) {
            frame = new MainFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        frame.setVisible(true);
    }

    /**
     * これはデフォルトのコンストラクタです
     */
    public MainFrame() {
        super();
        initialize();
    }

    /**
     * 実質的開始点。
     * initialize() <- setContentpane() <- getJContentePane() <- getCenterScrollPane <- getGraphField()
     * の順で呼ばれてゆく。
     *
     * @return void
     */
    private void initialize() {
        this.setSize(600, 500);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(jFileChooser);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(jContentPane, "エラーが発生しました", e
                    .toString(), JOptionPane.ERROR_MESSAGE);
        }

        /*  */
        this.setContentPane(getJContentPane());//setContentpane() <- getJContentePane() <- getCenterScrollPane <- getGraphField()
        /*  */

        this.setJMenuBar(getJJMenuBar());
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setThisTitle();
        this.setLocationRelativeTo(null);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });
        gField.selectRootNode();
    }

    /**
     * This method initializes jContentPane
     * initialize() <- setContentpane() <- getJContentePane() <- getCenterScrollPane <- getGraphField()
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());

            //jContentPane.add(getCenterScrollPane(), BorderLayout.CENTER);
            jContentPane.add(getCenterPanel(), BorderLayout.CENTER);//中心領域パネルのはめ込み
            jContentPane.add(getWest_panel(), BorderLayout.LINE_START);//西領域パネルのはめ込み
            jContentPane.add(getEastPanel(), BorderLayout.EAST);//東領域パネルのはめ込み

            jContentPane.add(getSouthJPanel(), BorderLayout.SOUTH);
        }
        return jContentPane;
    }


    /**
     *
     */
    private int clickStart_X = 0;
    private int dragged_distance = 0;
    private int delta = 0;
    JScrollPane WestScrollPane, EastScrollPane;
    JTextArea WestTextArea, EastTextArea;


    /**
     * 中心領域パネル
     *
     * @return
     */
    private JPanel getCenterPanel() {
        if (CenterPanel == null) {
            CenterPanel = new JPanel();
            CenterPanel.setLayout(new BorderLayout());
            CenterPanel.setPreferredSize(new Dimension(400, 600));

            /**
             * 中心領域の中心に置くパネル
             */
            CenterPanel.add(getCenterScrollPane(), BorderLayout.CENTER);


            /**
             * 西リサイズバー
             * 中心領域の西に配置するパネル
             */
            JPanel WestResizeBar = new JPanel();
            WestResizeBar.setPreferredSize(new Dimension(4, 600));
            WestResizeBar.setBackground(Color.RED);
            CenterPanel.add(WestResizeBar, BorderLayout.WEST);
            WestResizeBar.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    System.out.println("mousePressed");
                    clickStart_X = e.getXOnScreen();
                    System.out.println("set clickStart_X=" + clickStart_X);
                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    //System.out.println("MouseEntered");
                    setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                }
            });
            WestResizeBar.addMouseMotionListener(new MouseMotionListener() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    System.out.println("mouseDragged");

                    //マウス移動量の計算
                    dragged_distance = (e.getXOnScreen() - clickStart_X);
                    //パネル移動量の設定
                    delta = dragged_distance / 2;
                    System.out.println("delta=" + delta);

                    //西スクロールパネ領域のサイズ設定
                    //west_panel.setSize(new Dimension(west_panel.getWidth() + delta, west_panel.getHeight()));
                    //west_panel.setPreferredSize(new Dimension(west_panel.getWidth() + delta, west_panel.getHeight()));//これがないとテキスト領域が変更された時もとに戻ってしまう。
                    WestScrollPane = west_panel.getWestScrollPane();
                    WestScrollPane.setSize(new Dimension(WestScrollPane.getWidth() + delta, WestScrollPane.getHeight()));
                    WestScrollPane.setPreferredSize(new Dimension(WestScrollPane.getWidth() + delta, WestScrollPane.getHeight()));//これがないとテキスト領域が変更された時もとに戻ってしまう。
                    WestTextArea = west_panel.getWestTextArea();
                    WestTextArea.setSize(new Dimension(WestScrollPane.getWidth() + delta, WestScrollPane.getHeight()));
                    WestTextArea.setPreferredSize(new Dimension(WestScrollPane.getWidth() + delta, WestScrollPane.getHeight()));//これがないとテキスト領域が変更された時もとに戻ってしまう。

                    //中心panel領域のサイズ・位置設定
                    CenterPanel.setSize(new Dimension(CenterPanel.getWidth() - delta, CenterPanel.getHeight()));
                    CenterPanel.setPreferredSize(new Dimension(CenterPanel.getWidth() - delta, CenterPanel.getHeight()));//これがないとテキスト領域が変更された時もとに戻ってしまう。
                    GraphFieldScrollPane.setSize(new Dimension(GraphFieldScrollPane.getWidth() - delta, GraphFieldScrollPane.getHeight()));
                    GraphFieldScrollPane.setPreferredSize(new Dimension(GraphFieldScrollPane.getWidth() - delta, GraphFieldScrollPane.getHeight()));//これがないとテキスト領域が変更された時もとに戻ってしまう。
                    gField.setSize(new Dimension(gField.getWidth() - delta, gField.getHeight()));
                    gField.setPreferredSize(new Dimension(gField.getWidth() - delta, gField.getHeight()));//これがないとテキスト領域が変更された時もとに戻ってしまう。

                    CenterPanel.setBounds(CenterPanel.getX() + delta, CenterPanel.getY(), CenterPanel.getWidth(), CenterPanel.getHeight());

                    CenterPanel.revalidate();//repaint()ではダメ
                    west_panel.revalidate();//repaint()ではダメ

                    clickStart_X = e.getXOnScreen();
                    System.out.println("set clickStart_X=" + clickStart_X);
                }

                @Override
                public void mouseMoved(MouseEvent e) {

                }
            });

            /**
             * 東リサイズバー
             * 中心領域の東に配置するパネル
             */
            JPanel EastResizeBar = new JPanel();
            EastResizeBar.setPreferredSize(new Dimension(4, 600));
            EastResizeBar.setBackground(Color.BLUE);
            CenterPanel.add(EastResizeBar, BorderLayout.EAST);
            EastResizeBar.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    System.out.println("mousePressed");
                    clickStart_X = e.getXOnScreen();
                    System.out.println("set clickStart_X=" + clickStart_X);
                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    //System.out.println("MouseEntered");
                    setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                }
            });
            EastResizeBar.addMouseMotionListener(new MouseMotionListener() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    System.out.println("mouseDragged");

                    //マウス移動量の計算
                    dragged_distance = (e.getXOnScreen() - clickStart_X);
                    //パネル移動量の設定
                    delta = dragged_distance / 2;
                    System.out.println("delta=" + delta);

                    //東スクロールパネ領域のサイズ設定
                    //east_panel.setSize(new Dimension(east_panel.getWidth() - delta, east_panel.getHeight()));
                    //east_panel.setPreferredSize(new Dimension(east_panel.getWidth() - delta, east_panel.getHeight()));//これがないとテキスト領域が変更された時もとに戻ってしまう。
                    EastScrollPane = east_panel.getEastScrollPane();
                    EastScrollPane.setSize(new Dimension(EastScrollPane.getWidth() - delta, EastScrollPane.getHeight()));
                    EastScrollPane.setPreferredSize(new Dimension(EastScrollPane.getWidth() - delta, EastScrollPane.getHeight()));//これがないとテキスト領域が変更された時もとに戻ってしまう。
                    EastTextArea = east_panel.getEastTextArea();
                    EastTextArea.setSize(new Dimension(EastTextArea.getWidth() - delta, EastTextArea.getHeight()));
                    EastTextArea.setPreferredSize(new Dimension(EastTextArea.getWidth() - delta, EastTextArea.getHeight()));//これがないとテキスト領域が変更された時もとに戻ってしまう。

                    //中心panel領域のサイズ・位置設定
                    CenterPanel.setSize(new Dimension(CenterPanel.getWidth() + delta, CenterPanel.getHeight()));
                    CenterPanel.setPreferredSize(new Dimension(CenterPanel.getWidth() + delta, CenterPanel.getHeight()));//これがないとテキスト領域が変更された時もとに戻ってしまう。
                    GraphFieldScrollPane.setSize(new Dimension(GraphFieldScrollPane.getWidth() + delta, GraphFieldScrollPane.getHeight()));
                    GraphFieldScrollPane.setPreferredSize(new Dimension(GraphFieldScrollPane.getWidth() + delta, GraphFieldScrollPane.getHeight()));//これがないとテキスト領域が変更された時もとに戻ってしまう。
                    gField.setSize(new Dimension(gField.getWidth() + delta, gField.getHeight()));
                    gField.setPreferredSize(new Dimension(gField.getWidth() + delta, gField.getHeight()));//これがないとテキスト領域が変更された時もとに戻ってしまう。

                    east_panel.setBounds(EastScrollPane.getX() + delta, EastScrollPane.getY(), EastScrollPane.getWidth(), EastScrollPane.getHeight());

                    CenterPanel.revalidate();//repaint()ではダメ
                    east_panel.revalidate();//repaint()ではダメ

                    clickStart_X = e.getXOnScreen();
                    System.out.println("set clickStart_X=" + clickStart_X);
                }

                @Override
                public void mouseMoved(MouseEvent e) {

                }
            });


        }
        return CenterPanel;
    }

    /**
     * 中心領域のスクロールパネル
     * This method initializes GraphFieldScrollPane
     * <p>
     * initialize() <- setContentpane() <- getJContentePane() <- getCenterScrollPane <- getGraphField()
     *
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getCenterScrollPane() {
        if (GraphFieldScrollPane == null) {
            GraphFieldScrollPane = new GraphFieldScrollPane();//JScrollPane();

            gField = getGraphField();
            GraphFieldScrollPane.setViewportView(gField);

            int InitialPositionX = (int) (gField.getPreferredSize().width - GraphFieldScrollPane.getPreferredSize().width) / 2;
            int InitialPositionY = (int) (gField.getPreferredSize().height - GraphFieldScrollPane.getPreferredSize().height) / 2;
            GraphFieldScrollPane.getViewport().setViewPosition(new Point(InitialPositionX, InitialPositionY));

            GraphFieldScrollPane.setEnabled(true);
        }
        return GraphFieldScrollPane;
    }

    /**
     * initialize() <- setContentpane() <- getJContentePane() <- getCenterScrollPane <- getGraphField()
     */
    private GraphField gField;

    private GraphField getGraphField() {
        System.out.println("---------- getGraphField() ----------");
        if (gField == null) {
            //gField = (GraphField) SaverAndLoader.load(MINDMAP_FIELD_FILE);
            gField = SaverAndLoader.loadGraphElements(this, MINDMAP_FIELD_FILE, MINDMAP_NODE_LIST);
            if (gField == null) {
                System.out.println("MindmapFieldのロードに失敗しました");
                gField = new GraphField(this);//
                gField.setFont(DEFAULT_FONT);
            }
            gField.setMindmapNote(this);//MindmapNoteが保持する各種コンポーネントへのアクセスを確保するため

            /**
             * ActionMap, InputMapへの登録
             */
            InputMap inputMap = gField.getInputMap();
            ActionMap actionMap = gField.getActionMap();

            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_I, MASK), "ノードの挿入");
            actionMap.put("ノードの挿入", new AddNewNodeAction(gField));
        }

        return gField;
    }


    /**
     * 西領域のパネル
     */
    public WestPanel getWest_panel() {
        if (west_panel == null) {
            west_panel = new WestPanel(gField);
        }
        return west_panel;
    }

    /**
     * 東領域のパネル
     */
    private EastPanel getEastPanel() {
        if (east_panel == null) {
            east_panel = new EastPanel(gField);
        }
        return east_panel;
    }


    /**
     * 南領域パネル
     * This method initializes SouthPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getSouthJPanel() {
        if (SouthPanel == null) {
            SouthPanel = new JPanel();
            SouthPanel.setBackground(Color.MAGENTA);
            SouthPanel.setLayout(new BorderLayout());
            SouthPanel.add(getFootLeftLabel(), BorderLayout.CENTER);
            SouthPanel.add(getFootRightLabel(), BorderLayout.EAST);
        }
        return SouthPanel;
    }


    /**
     * アプリケーションを終了させます
     */
    private void exit() {
        System.out.println("exit");
        boolean closed = closeFile();
        System.out.println("closeFile() -> " + closed);
        if (closed) {
            setVisible(false);
            //GraphNode.setText("");
            setThisTitle();
            SaverAndLoader.saveNodes(gField.getNodeList(), gField.getEdgeList(), MINDMAP_NODE_LIST);
            //SaverAndLoader.saveMindmapField(gField, MINDMAP_FIELD_FILE);
            //SaverAndLoader.save(this, J_FRAME_FILE);
            //System.out.println("おわり");
            System.exit(0);
        }
    }

    /**
     * メニューバー関連
     * This method initializes jJMenuBar
     *
     * @return javax.swing.JMenuBar
     */
    private JMenuBar getJJMenuBar() {
        if (jJMenuBar == null) {
            jJMenuBar = new JMenuBar();
            jJMenuBar.add(getFileMenu());
            jJMenuBar.add(getEditMenu());
            jJMenuBar.add(getDisplayMenu());
            jJMenuBar.add(getHelpMenu());
        }
        return jJMenuBar;
    }

    /**
     * This method initializes fileMenu
     *
     * @return javax.swing.JMenu
     */
    private JMenu getFileMenu() {
        if (fileMenu == null) {
            fileMenu = new JMenu();
            fileMenu.setName("ファイル(F)");
            fileMenu.setText("ファイル(F)");
            fileMenu.setMnemonic(KeyEvent.VK_F);
            fileMenu.setActionCommand("ファイル(F)");
            fileMenu.add(getNewFileMenuItem());
            fileMenu.add(getOpenMenuItem());
            fileMenu.add(getSaveMenuItem());
            fileMenu.add(getSaveAsMenuItem());
            fileMenu.add(getEncodeMenu());
            fileMenu.addSeparator();
            fileMenu.add(getPrintMenuItem());
            fileMenu.addSeparator();
            fileMenu.add(getExitMenuItem());
        }
        return fileMenu;
    }

    /**
     * This method initializes newFileMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getNewFileMenuItem() {
        if (newFileMenuItem == null) {
            newFileMenuItem = new JMenuItem();
            newFileMenuItem.setText("新規(N)");
            newFileMenuItem.setActionCommand("新規");
            newFileMenuItem.setMnemonic(KeyEvent.VK_N);
            newFileMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                    KeyEvent.VK_N, MASK));
            newFileMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    openNewFile();
                }
            });
        }
        return newFileMenuItem;
    }

    /**
     * This method initializes openMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getOpenMenuItem() {
        if (openMenuItem == null) {
            openMenuItem = new JMenuItem();
            openMenuItem.setText("開く(O)");
            openMenuItem.setMnemonic(KeyEvent.VK_O);
            openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
                    MASK));
            openMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    openFile(ENCODINGS[0]);
                }
            });
        }
        return openMenuItem;
    }

    /**
     * This method initializes saveMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getSaveMenuItem() {
        if (saveMenuItem == null) {
            saveMenuItem = new JMenuItem();
            saveMenuItem.setText("保存(S)");
            saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                    MASK));
            saveMenuItem.setMnemonic(KeyEvent.VK_S);
            saveMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    saveFile();
                }
            });
        }
        return saveMenuItem;
    }

    /**
     * This method initializes saveAsMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getSaveAsMenuItem() {
        if (saveAsMenuItem == null) {
            saveAsMenuItem = new JMenuItem();
            saveAsMenuItem.setText("名前を付けて保存(A)");
            saveAsMenuItem.setMnemonic(KeyEvent.VK_A);
            saveAsMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    saveFileWithName();
                }
            });
        }
        return saveAsMenuItem;
    }

    /**
     * This method initializes encodeMenu
     *
     * @return javax.swing.JMenu
     */
    private JMenu getEncodeMenu() {
        if (encodeMenu == null) {
            encodeMenu = new JMenu();
            encodeMenu.setText("文字コード指定(E)");
            encodeMenu.setMnemonic(KeyEvent.VK_E);
            encodeMenu.add(getOpenWithEncodingMenu());
            encodeMenu.add(getRereadWithEncodingMenu());
            encodeMenu.add(getSaveWithEncodingMenu());
        }
        return encodeMenu;
    }

    /**
     * This method initializes openWithEncodingMenu
     *
     * @return javax.swing.JMenu
     */
    private JMenu getOpenWithEncodingMenu() {
        if (openWithEncodingMenu == null) {
            openWithEncodingMenu = new JMenu();
            openWithEncodingMenu.setText("開く(O)");
            openWithEncodingMenu.setMnemonic(KeyEvent.VK_O);
            for (int i = 0, n = openWithEncodingMenuItems.length; i < n; i++) {
                openWithEncodingMenu.add(getOpenWithEncodingMenuItem(i));
            }
        }
        return openWithEncodingMenu;
    }

    private JMenuItem getOpenWithEncodingMenuItem(final int i) {
        if (openWithEncodingMenuItems[i] == null) {
            openWithEncodingMenuItems[i] = new JMenuItem();
            openWithEncodingMenuItems[i].setText(ENCODING_TEXT[i]);
            openWithEncodingMenuItems[i].setMnemonic(ENCODING_MNEMONIC[i]);
            openWithEncodingMenuItems[i]
                    .addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            openFile(ENCODINGS[i]);
                        }
                    });
        }
        return openWithEncodingMenuItems[i];
    }

    /**
     * This method initializes rereadWithEncoding
     *
     * @return javax.swing.JMenu
     */
    private JMenu getRereadWithEncodingMenu() {
        if (rereadWithEncodingMenu == null) {
            rereadWithEncodingMenu = new JMenu();
            rereadWithEncodingMenu.setText("再読込(R)");
            rereadWithEncodingMenu.setMnemonic(KeyEvent.VK_R);
            for (int i = 0, n = rereadWithEncodingMenuItems.length; i < n; i++) {
                rereadWithEncodingMenu.add(getRereadWithEncodingMenuItem(i));
            }
        }
        return rereadWithEncodingMenu;
    }

    private JMenuItem getRereadWithEncodingMenuItem(final int i) {
        if (rereadWithEncodingMenuItems[i] == null) {
            rereadWithEncodingMenuItems[i] = new JMenuItem();
            rereadWithEncodingMenuItems[i].setMnemonic(ENCODING_MNEMONIC[i]);
            rereadWithEncodingMenuItems[i].setText(ENCODING_TEXT[i]);
            rereadWithEncodingMenuItems[i]
                    .addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            rereadFile(ENCODINGS[i]);
                        }
                    });
        }
        return rereadWithEncodingMenuItems[i];
    }

    /**
     * This method initializes saveWithEncodingMenu
     *
     * @return javax.swing.JMenu
     */
    private JMenu getSaveWithEncodingMenu() {
        if (saveWithEncodingMenu == null) {
            saveWithEncodingMenu = new JMenu();
            saveWithEncodingMenu.setText("保存(S)");
            saveWithEncodingMenu.setMnemonic(KeyEvent.VK_S);
            for (int i = 0, n = saveWithEncodingMenuItems.length; i < n; i++) {
                saveWithEncodingMenu.add(getSaveWithEncodingMenuItem(i));
            }
        }
        return saveWithEncodingMenu;
    }

    private JMenuItem getSaveWithEncodingMenuItem(final int i) {
        if (saveWithEncodingMenuItems[i] == null) {
            saveWithEncodingMenuItems[i] = new JMenuItem();
            saveWithEncodingMenuItems[i].setText(ENCODING_TEXT[i]);
            saveWithEncodingMenuItems[i].setMnemonic(ENCODING_MNEMONIC[i]);
            saveWithEncodingMenuItems[i]
                    .addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            saveFile(ENCODINGS[i]);
                        }
                    });
        }
        return saveWithEncodingMenuItems[i];
    }

    /**
     * This method initializes printMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getPrintMenuItem() {
        if (printMenuItem == null) {
            printMenuItem = new JMenuItem();
            printMenuItem.setText("印刷(P)");
            printMenuItem.setMnemonic(KeyEvent.VK_P);
            printMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
                    MASK));
            printMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    printFile();
                }
            });
        }
        return printMenuItem;
    }

    /**
     * This method initializes exitMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getExitMenuItem() {
        if (exitMenuItem == null) {
            exitMenuItem = new JMenuItem();
            exitMenuItem.setText("終了(X)");
            exitMenuItem.setMnemonic(KeyEvent.VK_X);
            exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,
                    MASK));
            exitMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    exit();
                }
            });
        }
        return exitMenuItem;
    }

    /**
     * This method initializes editMenu
     *
     * @return javax.swing.JMenu
     */
    private JMenu getEditMenu() {
        if (editMenu == null) {
            editMenu = new JMenu();
            editMenu.setText("編集(E)");
            editMenu.setMnemonic(KeyEvent.VK_E);
            editMenu.add(getUndoMenuItem());
            editMenu.add(getRedoMenuItem());
            editMenu.addSeparator();
            editMenu.add(getCutMenuItem());
            editMenu.add(getCopyMenuItem());
            editMenu.add(getPasteMenuItem());
            editMenu.add(getDeleteMenuItem());
            editMenu.addSeparator();
            editMenu.add(getReplaceMenuItem());
            editMenu.add(getFindNextMenuItem());
            editMenu.add(getFindPrevMenuItem());
            editMenu.add(getGoToMenuItem());
            editMenu.addSeparator();
            editMenu.add(getSelectAllMenuItem());
            editMenu.addMenuListener(new MenuListener() {
                public void menuSelected(MenuEvent e) {
                    boolean hasSelectedText = !(Node.getTextarea().getSelectedText() == null);
                    cutMenuItem.setEnabled(hasSelectedText);
                    copyMenuItem.setEnabled(hasSelectedText);
                    deleteMenuItem.setEnabled(hasSelectedText);
                    boolean hasText = !Node.getTextarea().getText().equals("");
                    replaceMenuItem.setEnabled(hasText);
                    replaceMenuItem2.setEnabled(hasText);
                    findNextMenuItem.setEnabled(hasText);
                    findPrevMenuItem.setEnabled(hasText);
                }

                public void menuDeselected(MenuEvent e) {//メニューの選択が解除されたときに呼び出されます。
                    //System.out.println("menuDeselected");
                    cutMenuItem.setEnabled(true);
                    copyMenuItem.setEnabled(true);
                    deleteMenuItem.setEnabled(true);
                    replaceMenuItem.setEnabled(true);
                    replaceMenuItem2.setEnabled(true);
                    findNextMenuItem.setEnabled(false);
                    findPrevMenuItem.setEnabled(false);
                }

                public void menuCanceled(MenuEvent e) {
                }
            });
        }
        return editMenu;
    }

    /**
     * This method initializes undoMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getUndoMenuItem() {
        if (undoMenuItem == null) {
            undoMenuItem = new JMenuItem();
            undoMenuItem.setText("元に戻す(U)");
            undoMenuItem.setMnemonic(KeyEvent.VK_U);
            undoMenuItem.setActionCommand("元に戻す(U)");
            undoMenuItem.setEnabled(false);
            undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
                    MASK));
            undoMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        undoManager.undo();
                    } catch (CannotUndoException cue) {
                        JOptionPane.showMessageDialog(jContentPane, "戻せません",
                                cue.toString(), JOptionPane.ERROR_MESSAGE);
                    }
                    update();
                }
            });
        }
        return undoMenuItem;
    }

    /**
     * This method initializes redoMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getRedoMenuItem() {
        if (redoMenuItem == null) {
            redoMenuItem = new JMenuItem();
            redoMenuItem.setText("やり直し(R)");
            redoMenuItem.setMnemonic(KeyEvent.VK_R);
            redoMenuItem.setActionCommand("やり直し(R)");
            redoMenuItem.setEnabled(false);
            redoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y,
                    MASK));
            redoMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        undoManager.redo();
                    } catch (CannotRedoException cre) {
                        JOptionPane.showMessageDialog(jContentPane, "やり直せません",
                                cre.toString(), JOptionPane.ERROR_MESSAGE);
                    }
                    update();
                }
            });
        }
        return redoMenuItem;
    }

    /**
     * This method initializes cutMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getCutMenuItem() {
        if (cutMenuItem == null) {
            cutMenuItem = new JMenuItem();
            cutMenuItem.setText("切り取り(T)");
            cutMenuItem.setMnemonic(KeyEvent.VK_T);
            cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
                    MASK));
            cutMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Node.getTextarea().cut();
                }
            });
        }
        return cutMenuItem;
    }

    /**
     * This method initializes copyMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getCopyMenuItem() {
        if (copyMenuItem == null) {
            copyMenuItem = new JMenuItem();
            copyMenuItem.setText("コピー(C)");
            copyMenuItem.setMnemonic(KeyEvent.VK_C);
            copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
                    MASK));
            copyMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Node.getTextarea().copy();
                }
            });
        }
        return copyMenuItem;
    }

    /**
     * This method initializes pasteMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getPasteMenuItem() {
        if (pasteMenuItem == null) {
            pasteMenuItem = new JMenuItem();
            pasteMenuItem.setText("貼り付け(P)");
            pasteMenuItem.setMnemonic(KeyEvent.VK_P);
            pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
                    MASK));
            pasteMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Node.getTextarea().paste();
                }
            });
        }
        return pasteMenuItem;
    }

    /**
     * This method initializes deleteMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getDeleteMenuItem() {
        if (deleteMenuItem == null) {
            deleteMenuItem = new JMenuItem();
            deleteMenuItem.setText("削除(D)");
            deleteMenuItem.setMnemonic(KeyEvent.VK_D);
            deleteMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                    KeyEvent.VK_DELETE, 0));
            deleteMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //Node.getTextarea().replaceSelection(null);
                }
            });
        }
        return deleteMenuItem;
    }

    /**
     * This method initializes replaceMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getReplaceMenuItem() {
        if (replaceMenuItem == null) {
            replaceMenuItem = new JMenuItem();
            replaceMenuItem.setText("検索と置換(F)");
            replaceMenuItem.setMnemonic(KeyEvent.VK_F);
            replaceMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, MASK));
            replaceMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (!Node.getTextarea().getText().equals("")) {
                        if (wordReplacement == null) {
                            wordReplacement = WordReplacement.INSTANCE;
                            wordReplacement.setJTextArea(Node);
                        }
                        wordReplacement.setVisible(true);
                    }
                }
            });
        }
        return replaceMenuItem;
    }


    /**
     * This method initializes findNextMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getFindNextMenuItem() {
        if (findNextMenuItem == null) {
            findNextMenuItem = new JMenuItem();
            findNextMenuItem.setText("次を検索(N)");
            findNextMenuItem.setMnemonic(KeyEvent.VK_N);
            findNextMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                    KeyEvent.VK_F3, 0));
            findNextMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (wordReplacement != null
                            && !Node.getTextarea().getText().equals("")) {
                        wordReplacement.find(true);
                    }
                }
            });
        }
        return findNextMenuItem;
    }

    /**
     * This method initializes findPrevMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getFindPrevMenuItem() {
        if (findPrevMenuItem == null) {
            findPrevMenuItem = new JMenuItem();
            findPrevMenuItem.setMnemonic(KeyEvent.VK_V);
            findPrevMenuItem.setText("前を検索(P)");
            findPrevMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                    KeyEvent.VK_F4, 0));
            findPrevMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (wordReplacement != null
                            && !Node.getTextarea().getText().equals("")) {
                        wordReplacement.find(false);
                    }
                }
            });
        }
        return findPrevMenuItem;
    }

    /**
     * This method initializes goToMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getGoToMenuItem() {
        if (goToMenuItem == null) {
            goToMenuItem = new JMenuItem();
            goToMenuItem.setText("行へ移動(G)");
            goToMenuItem.setMnemonic(KeyEvent.VK_G);
            goToMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G,
                    MASK));
            goToMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    while (true) {
                        try {
                            String ans = JOptionPane.showInputDialog(
                                    jContentPane, "行番号", "行へ移動",
                                    JOptionPane.QUESTION_MESSAGE);
                            if (ans == null) {
                                return;
                            }
                            int rowNum = Integer.valueOf(ans).intValue() - 1;
                            Node.getTextarea().setCaretPosition(Node
                                    .getTextarea().getLineStartOffset(rowNum));
                            return;

                        } catch (NumberFormatException nfe) {
                            JOptionPane.showMessageDialog(jContentPane,
                                    "数字を入力してください", nfe.toString(),
                                    JOptionPane.ERROR_MESSAGE);
                        } catch (BadLocationException ble) {
                            JOptionPane.showMessageDialog(jContentPane,
                                    "行番号が範囲外です", ble.toString(),
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });
        }
        return goToMenuItem;
    }

    /**
     * This method initializes selectAllMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getSelectAllMenuItem() {
        if (selectAllMenuItem == null) {
            selectAllMenuItem = new JMenuItem();
            selectAllMenuItem.setText("全て選択(A)");
            selectAllMenuItem.setMnemonic(KeyEvent.VK_A);
            selectAllMenuItem.setActionCommand("全て選択(A)");
            selectAllMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                    KeyEvent.VK_A, MASK));
            selectAllMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Node.getTextarea().selectAll();
                }
            });
        }
        return selectAllMenuItem;
    }

    /**
     * This method initializes displayMenu
     *
     * @return javax.swing.JMenu
     */
    private JMenu getDisplayMenu() {
        if (displayMenu == null) {
            displayMenu = new JMenu();
            displayMenu.setText("表示(V)");
            displayMenu.setMnemonic(KeyEvent.VK_V);
            displayMenu.add(getFontMenuItem());
            displayMenu.add(getTabSizeMenuItem());
            displayMenu.add(getColorMenu());
            displayMenu.add(getWrapCheckBoxMenuItem());
            displayMenu.add(getStatusBarCheckBoxMenuItem());
        }
        return displayMenu;
    }

    private FontProperty fontProperty;

    /**
     * This method initializes fontMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getFontMenuItem() {
        if (fontMenuItem == null) {
            fontMenuItem = new JMenuItem();
            fontMenuItem.setText("フォント(F)");
            fontMenuItem.setMnemonic(KeyEvent.VK_F);
            fontMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (fontProperty == null) {
                        fontProperty = FontProperty.INSTANCE;
                        fontProperty.setModal(true);
                    }
                    fontProperty.setJTextArea(Node);
                    fontProperty.setVisible(true);
                }
            });
        }
        return fontMenuItem;
    }

    /**
     * This method initializes tabSizeMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getTabSizeMenuItem() {
        if (tabSizeMenuItem == null) {
            tabSizeMenuItem = new JMenuItem();
            tabSizeMenuItem.setText("タブ幅(T)");
            tabSizeMenuItem.setMnemonic(KeyEvent.VK_T);
            tabSizeMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // タブ幅の選択肢は、 2, 4, 6, 8, 10
                    Integer[] choice = new Integer[5];
                    for (int i = 0; i < 5; i++) {
                        choice[i] = new Integer(2 * (i + 1));
                    }
                    Object ans = JOptionPane.showInputDialog(jContentPane,
                            "タブを展開する文字数を選択してください", "タブを展開する文字数",
                            JOptionPane.PLAIN_MESSAGE, null, choice,
                            choice[Node.getTextarea().getTabSize() / 2 - 1]);
                    if (ans != null) {
                        int tabSize = Integer.parseInt(ans.toString());
                        Node.getTextarea().setTabSize(tabSize);
                    }
                }
            });
        }
        return tabSizeMenuItem;
    }

    /**
     * This method initializes colorMenu
     *
     * @return javax.swing.JMenu
     */
    private JMenu getColorMenu() {
        if (colorMenu == null) {
            colorMenu = new JMenu();
            colorMenu.setText("色の指定(C)");
            colorMenu.setMnemonic(KeyEvent.VK_C);
            colorMenu.add(getBackgroundMenuItem());
            colorMenu.add(getForegroundMenuItem());
            colorMenu.addSeparator();
            colorMenu.add(getCaretMenuItem());
            colorMenu.addSeparator();
            colorMenu.add(getSelectionMenuItem());
            colorMenu.add(getSelectedTextMenuItem());
        }
        return colorMenu;
    }

    /**
     * This method initializes backgroundMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getBackgroundMenuItem() {
        if (backgroundMenuItem == null) {
            backgroundMenuItem = new JMenuItem();
            backgroundMenuItem.setText("背景色(B)");
            backgroundMenuItem.setMnemonic(KeyEvent.VK_B);
            backgroundMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Color color = JColorChooser.showDialog(jContentPane,
                            "背景色を指定してください", Node.getBackground());
                    if (color != null) {
                        Node.setBackground(color);
                    }
                }
            });
        }
        return backgroundMenuItem;
    }

    /**
     * This method initializes foregroundMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getForegroundMenuItem() {
        if (foregroundMenuItem == null) {
            foregroundMenuItem = new JMenuItem();
            foregroundMenuItem.setText("文字色(W)");
            foregroundMenuItem.setMnemonic(KeyEvent.VK_W);
            foregroundMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Color color = JColorChooser.showDialog(jContentPane,
                            "文字色を指定してください", Node.getForeground());
                    if (color != null) {
                        Node.setForeground(color);
                    }
                }
            });
        }
        return foregroundMenuItem;
    }

    /**
     * This method initializes caretMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getCaretMenuItem() {
        if (caretMenuItem == null) {
            caretMenuItem = new JMenuItem();
            caretMenuItem.setText("カーソルの色(C)");
            caretMenuItem.setMnemonic(KeyEvent.VK_C);
            caretMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Color color = JColorChooser.showDialog(jContentPane,
                            "カーソルの色を指定してください", Node.getTextarea().getCaretColor());
                    if (color != null) {
                        Node.getTextarea().setCaretColor(color);
                    }
                }
            });
        }
        return caretMenuItem;
    }

    /**
     * This method initializes selectedMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getSelectionMenuItem() {
        if (selectionMenuItem == null) {
            selectionMenuItem = new JMenuItem();
            selectionMenuItem.setText("選択領域の背景色(S)");
            selectionMenuItem.setMnemonic(KeyEvent.VK_S);
            selectionMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Color color = JColorChooser.showDialog(jContentPane,
                            "選択領域の背景色を指定してください", Node.getTextarea().getSelectionColor());
                    if (color != null) {
                        Node.getTextarea().setSelectionColor(color);
                    }
                }
            });
        }
        return selectionMenuItem;
    }

    /**
     * This method initializes selectedTextMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getSelectedTextMenuItem() {
        if (selectedTextMenuItem == null) {
            selectedTextMenuItem = new JMenuItem();
            selectedTextMenuItem.setText("選択領域の文字色(T)");
            selectedTextMenuItem.setMnemonic(KeyEvent.VK_T);
            selectedTextMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Color color = JColorChooser.showDialog(jContentPane,
                            "選択領域の文字色を指定してください", Node
                                    .getTextarea().getSelectedTextColor());
                    if (color != null) {
                        Node.getTextarea().setSelectedTextColor(color);
                    }
                }
            });
        }
        return selectedTextMenuItem;
    }

    /**
     * This method initializes wrapCheckBoxMenuItem
     *
     * @return javax.swing.JCheckBoxMenuItem
     */
    private JCheckBoxMenuItem getWrapCheckBoxMenuItem() {
        if (wrapCheckBoxMenuItem == null) {
            wrapCheckBoxMenuItem = new JCheckBoxMenuItem();
            wrapCheckBoxMenuItem.setMnemonic(KeyEvent.VK_W);
            wrapCheckBoxMenuItem.setText("右端で折り返す(W)");
            //wrapCheckBoxMenuItem.setSelected(GraphNode.getLineWrap());
            wrapCheckBoxMenuItem.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    Node.getTextarea().setLineWrap(!Node.getTextarea().getLineWrap());
                }
            });
        }
        return wrapCheckBoxMenuItem;
    }

    /**
     * This method initializes statusBarCheckBoxMenuItem
     *
     * @return javax.swing.JCheckBoxMenuItem
     */
    private JCheckBoxMenuItem getStatusBarCheckBoxMenuItem() {
        if (statusBarCheckBoxMenuItem == null) {
            statusBarCheckBoxMenuItem = new JCheckBoxMenuItem();
            statusBarCheckBoxMenuItem.setSelected(true);
            statusBarCheckBoxMenuItem.setText("ステータスバー(S)");
            statusBarCheckBoxMenuItem.setMnemonic(KeyEvent.VK_S);
            statusBarCheckBoxMenuItem.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if (statusBarCheckBoxMenuItem.isSelected()) {
                        FootLeftLabel.setVisible(true);
                        FootRightLabel.setVisible(true);
                    } else {
                        FootLeftLabel.setVisible(false);
                        FootRightLabel.setVisible(false);
                    }
                }
            });
        }
        return statusBarCheckBoxMenuItem;
    }

    /**
     * This method initializes helpMenu
     *
     * @return javax.swing.JMenu
     */
    private JMenu getHelpMenu() {
        if (helpMenu == null) {
            helpMenu = new JMenu();
            helpMenu.setText("ヘルプ(H)");
            helpMenu.setMnemonic(KeyEvent.VK_H);
            helpMenu.add(getHelpMenuItem());
            helpMenu.addSeparator();
            helpMenu.add(getAboutMenuItem());
        }
        return helpMenu;
    }

    /**
     * This method initializes helpMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getHelpMenuItem() {
        if (helpMenuItem == null) {
            helpMenuItem = new JMenuItem();
            helpMenuItem.setText("ヘルプ(H)");
            helpMenuItem.setMnemonic(KeyEvent.VK_H);
            helpMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1,
                    0));
            helpMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JOptionPane
                            .showMessageDialog(jContentPane,
                                    "このテキストエディタは基本的な機能しか持たないため、\nヘルプは必要ないと判断して作りませんでした。");
                }
            });
        }
        return helpMenuItem;
    }

    /**
     * This method initializes aboutMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getAboutMenuItem() {
        if (aboutMenuItem == null) {
            aboutMenuItem = new JMenuItem();
            aboutMenuItem.setText(APP_NAME + "について(A)");
            aboutMenuItem.setMnemonic(KeyEvent.VK_A);
            aboutMenuItem.setName(APP_NAME + "について");
            aboutMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(jContentPane, APP_NAME
                            + "\nver. " + VERSION);
                }
            });
        }
        return aboutMenuItem;
    }

    /**
     * 編集中のファイル
     */
    private File editingFile;

    /**
     * 変更されたかどうかを示すフラグ
     */
    private boolean hasChanged = false;


    /**
     * This method initializes FootLeftLabel
     *
     * @return javax.swing.JLabel
     */
    private JLabel getFootLeftLabel() {
        if (FootLeftLabel == null) {
            FootLeftLabel = new JLabel();
            setFootLeftLabelText();
            FootLeftLabel.setName("FootLeftLabel");
        }
        return FootLeftLabel;
    }

    /**
     * This method initializes FootRightLabel
     *
     * @return javax.swing.JLabel
     */
    private JLabel getFootRightLabel() {
        if (FootRightLabel == null) {
            FootRightLabel = new JLabel();
            setFootRightLabelText();
            FootRightLabel.setName("FootRightLabel");
        }
        return FootRightLabel;
    }


    private JPopupMenu pop;

    private Action action[];

    /**
     * ポップアップメニューを表示します
     *
     * @param jTextArea
     * @param me
     */
    private void showJPopupMenu(final GraphNode jTextArea, final MouseEvent me) {
        if (pop == null) {
            pop = new JPopupMenu();
            action = new Action[5];
            action[0] = new DefaultEditorKit.CutAction();
            action[0].putValue(Action.NAME, "切り取り");
            action[1] = new DefaultEditorKit.CopyAction();
            action[1].putValue(Action.NAME, "コピー");
            action[2] = new DefaultEditorKit.PasteAction();
            action[2].putValue(Action.NAME, "貼り付け");
            action[3] = new AbstractAction("削除") {
                public void actionPerformed(ActionEvent ae) {
                    jTextArea.getTextarea().replaceSelection(null);
                }
            };
            action[4] = new AbstractAction("全て選択") {
                public void actionPerformed(ActionEvent ae) {
                    jTextArea.getTextarea().selectAll();
                }
            };
            for (int i = 0; i < 5; i++) {
                pop.add(action[i]);
                if (i == 3) {
                    pop.addSeparator();
                }
            }
        }
        boolean flg = !(jTextArea.getTextarea().getSelectedText() == null);
        for (int i = 0; i < 4; i++) {
            if (i != 2) {
                action[i].setEnabled(flg);
            }
        }
        action[4].setEnabled(!jTextArea.getTextarea().getText().equals(""));
        pop.show(jTextArea, me.getX(), me.getY());
    }

    /**
     * 編集中のファイルの文字コード
     */
    private String editingFileEncoding = ENCODINGS[0];

    /**
     * 新規ファイルを開く前に編集していたファイル
     */
    private File prevFile;

    /**
     * 新規ファイルを開きます
     */
    private void openNewFile() {
        if (hasChanged) {
            boolean closed = closeFile();
            if (!closed) {
                return;
            }
        }
        int tabSize = Node.getTextarea().getTabSize();
        Node.getTextarea().setDocument(new PlainDocument());
        setThisTitle();
        Node.getTextarea().setTabSize(tabSize);
        setFootLeftLabelText();
        reset();
        prevFile = editingFile;
        editingFile = null;
        editingFileEncoding = ENCODINGS[0];
    }

    /**
     * 文字コード encoding でファイルを開きます
     *
     * @param encoding 文字コード
     */
    private void openFile(String encoding) {
        if (hasChanged) {
            boolean closed = closeFile();
            if (!closed) {
                return;
            }
        }
        int tabSize = Node.getTextarea().getTabSize();
        File file = chooseFile(true);
        if (file == null) {
            return;
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file), encoding));
            Node.getTextarea().read(reader, new PlainDocument());
            reader.close();
            editingFile = file;
            editingFileEncoding = encoding;
            setThisTitle(editingFile);
            Node.getTextarea().setTabSize(tabSize);
            setFootLeftLabelText(file);
            reset();
        } catch (FileNotFoundException fnfe) {
            JOptionPane.showMessageDialog(jContentPane, "ファイルが見つかりません", fnfe
                    .toString(), JOptionPane.ERROR_MESSAGE);
        } catch (IOException ie) {
            JOptionPane.showMessageDialog(jContentPane, "入出力エラーが発生しました", ie
                    .toString(), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * デフォルトの文字コードでファイルを保存します
     */
    private void saveFile() {
        System.out.println("ファイル保存");
        saveFile(ENCODINGS[0]);
    }

    /**
     * 文字コード encoding でファイルを保存します
     *
     * @param encoding 文字コード
     */
    private void saveFile(String encoding) {
        saveFile(encoding, false);
    }

    /**
     * 文字コード encoding でファイルを保存します
     *
     * @param encoding 文字コード
     * @param rename   文字コードを変えずに保存するとき、ファイル名を要求させる場合に真
     */
    private void saveFile(String encoding, boolean rename) {
        if (editingFileEncoding.equals(encoding) && !hasChanged && !rename) {
            return;
        }
        if (editingFile == null || rename) {
            File file = chooseFile(false);
            if (file == null) {
                return;
            }
            editingFile = file;
        }
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(editingFile), encoding));
            Node.getTextarea().write(writer);
            writer.close();
            editingFileEncoding = encoding;
            hasChanged = false;
            setThisTitle(editingFile);
            setFootLeftLabelText(editingFile);
        } catch (FileNotFoundException fnfe) {
            JOptionPane.showMessageDialog(jContentPane, "ファイルが見つかりません", fnfe
                    .toString(), JOptionPane.ERROR_MESSAGE);
        } catch (IOException ie) {
            JOptionPane.showMessageDialog(jContentPane, "入出力エラーが発生しました", ie
                    .toString(), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 常にファイル名を要求して、そのファイル名でファイルを保存します
     */
    private void saveFileWithName() {
        saveFile(ENCODINGS[0], true);
    }

    /**
     * 文字コード encoding でファイルを再読み込みします
     *
     * @param encoding 文字コード
     */
    private void rereadFile(String encoding) {
        if (editingFile == null) {
            return;
        }
        if (hasChanged) {
            int ans = JOptionPane.showConfirmDialog(jContentPane,
                    "再読み込みする前に変更を保存する必要があります。\n変更を保存しますか?");
            if (ans == JOptionPane.YES_OPTION) {
                saveFile();
            } else if (ans == JOptionPane.CANCEL_OPTION) {
                return;
            }
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(editingFile), encoding));
            Node.getTextarea().read(reader, new PlainDocument());
            reader.close();
            editingFileEncoding = encoding;
            hasChanged = false;
            setFootLeftLabelText(editingFile);
            reset();
        } catch (FileNotFoundException fnfe) {
            JOptionPane.showMessageDialog(jContentPane, "ファイルが見つかりません", fnfe
                    .toString(), JOptionPane.ERROR_MESSAGE);
        } catch (IOException ie) {
            JOptionPane.showMessageDialog(jContentPane, "入出力エラーが発生しました", ie
                    .toString(), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * ファイルを閉じます
     *
     * @return ファイルが閉じられた場合に真
     */
    private boolean closeFile() {
        if (hasChanged) {
            String str = null;
            if (editingFile == null) {
                str = "無題";
            } else {
                str = editingFile.getName();
            }
            int ans = JOptionPane.showConfirmDialog(jContentPane, "ファイル " + str
                    + " の内容は変更されています。\n変更を保存しますか?");
            if (ans == JOptionPane.YES_OPTION) {
                saveFile();
            } else if (ans == JOptionPane.CANCEL_OPTION
                    || ans == JOptionPane.CLOSED_OPTION) {
                return false;
            }
        }
        return true;
    }

    private JFileChooser jFileChooser = new JFileChooser();

    /**
     * ファイルを選択します
     *
     * @param open ファイルを開く場合に真、ファイルを保存する場合に偽
     * @return 選択したファイル
     */
    private File chooseFile(boolean open) {
        jFileChooser.setCurrentDirectory((editingFile == null) ? prevFile : editingFile);
        jFileChooser.addChoosableFileFilter(FileFilterConstructor_Extension_ver2.getSwingFileFilter("mm", "FreePlanファイル(*.mm)"));
        int ret;
        if (open) {
            ret = jFileChooser.showOpenDialog(jContentPane);

        } else {
            ret = jFileChooser.showSaveDialog(jContentPane);
        }
        if (ret != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        File file = jFileChooser.getSelectedFile();
        if (!open) {
            while (file.exists()) {
                ret = JOptionPane.showConfirmDialog(jContentPane, "ファイル "
                                + file + " は既に存在します。\n上書きしますか?", "保存",
                        JOptionPane.YES_NO_OPTION);
                if (ret == JOptionPane.YES_OPTION) {
                    break;
                } else if (ret == JOptionPane.CLOSED_OPTION) {
                    return null;
                }
                ret = jFileChooser.showSaveDialog(jContentPane);
                file = jFileChooser.getSelectedFile();
                if (ret != JFileChooser.APPROVE_OPTION) {
                    return null;
                }
            }
        }
        return file;
    }

    private PrintRequestAttributeSet aset;

    /**
     * GraphNode の文書を印刷します
     */
    private void printFile() {
        if (aset == null) {
            aset = new HashPrintRequestAttributeSet();
        }
        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setPrintable(this);
        try {
            if (pj.printDialog(aset)) {
                pj.print(aset);
            }
        } catch (PrinterException pe) {
            JOptionPane.showMessageDialog(jContentPane, "プリントエラー", pe
                    .toString(), JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
     * (非 Javadoc)
     * 
     * @see java.awt.print.Printable#print(java.awt.Graphics,
     *      java.awt.print.PageFormat, int)
     */
    public int print(Graphics g, PageFormat format, int pageIndex) {
        if (pageIndex == 0) {
            Graphics2D g2D = (Graphics2D) g;
            g2D.translate(format.getImageableX(), format.getImageableY());
            Node.print(g2D);
            return Printable.PAGE_EXISTS;
        }
        return Printable.NO_SUCH_PAGE;
    }


    /**
     * undo 出来るかどうかと、 redo 出来るかどうかとを調べます
     */
    private void update() {
        undoMenuItem.setEnabled(undoManager.canUndo());
        redoMenuItem.setEnabled(undoManager.canRedo());
    }

    /**
     * FootRightLabel と hasChanged と UndoManager をリセットします
     */
    private void reset() {
        setFootRightLabelText();
        hasChanged = false;
        undoManager = new UndoManager();
        Document document = Node.getTextarea().getDocument();
        document.addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                setChanged();
            }

            public void removeUpdate(DocumentEvent e) {
                setChanged();
            }

            public void changedUpdate(DocumentEvent e) {
            }
        });
        document.addUndoableEditListener(new UndoableEditListener() {
            public void undoableEditHappened(UndoableEditEvent e) {
                undoManager.addEdit(e.getEdit());
                update();
            }
        });
        update();
    }

    /**
     * 変更が行われたように設定します
     */
    private void setChanged() {
        if (!hasChanged) {
            hasChanged = true;
            setTitle("*" + getTitle());
        }
    }

    /**
     * ウィンドウのタイトルを「無題」にします
     */
    private void setThisTitle() {
        setThisTitle("無題");
    }

    /**
     * ウィンドウのタイトルを「ファイル file の名前 - アプリケーション名」にします
     *
     * @param file
     */
    private void setThisTitle(File file) {
        setThisTitle(file.getName());
    }

    /**
     * ウィンドウのタイトルを「text - アプリケーション名」にします
     *
     * @param text
     */
    private void setThisTitle(String text) {
        setTitle(text + " - " + APP_NAME);
    }

    /**
     * FootLeftLabel に「無題」と表示します
     */
    private void setFootLeftLabelText() {
        FootLeftLabel.setText("無題");
    }

    /**
     * FootLeftLabel にファイル file のパス名と文字コード editingFileEncoding を表示します
     *
     * @param file
     */
    private void setFootLeftLabelText(File file) {
        FootLeftLabel.setText(file.getPath() + " [" + editingFileEncoding + "]");
    }

    public void setFootLeftLabelText(String text) {
        FootLeftLabel.setText(text);
    }

    /**
     * FootRightLabel に 「1行、1列 」と表示します
     */
    private void setFootRightLabelText() {
        setFootRightLabelText(1, 1);
    }

    /**
     * FootRightLabel に 「row行、column列 」と表示します
     *
     * @param row    行数
     * @param column 列数
     */
    private void setFootRightLabelText(int row, int column) {
        StringBuffer sb = new StringBuffer();
        sb.append(row);
        sb.append("行、");
        sb.append(column);
        sb.append("列　");
        FootRightLabel.setText(sb.toString());
    }

    public void setFootRightLbelText(String text) {
        FootRightLabel.setText(text);
    }
}
