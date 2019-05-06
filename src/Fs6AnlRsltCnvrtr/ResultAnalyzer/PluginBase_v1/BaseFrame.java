package Fs6AnlRsltCnvrtr.ResultAnalyzer.PluginBase_v1;


import Plugin_TextEditor.EventList;
import Plugin_TextEditor.ExpectationsAsPluginBase;
import Plugin_TextEditor.MyTextAreaPanel;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.*;

/*
 * 作成日: 2005/08/03
 * http://null.michikusa.jp/misc/editor.html
 * 
 * 
 */

/**
 * テキストエディタ MindmapNote
 */
public class BaseFrame extends JFrame implements ExpectationsAsPluginBase {

    private ExpectationsAsPlugin PlugIn = null;
    private JPanel jContentPane = null;
    private JMenuBar jJMenuBar = null;
    private JMenu fileMenu = null;
    private JMenu editMenu = null;
    private JMenu displayMenu = null;
    private JCheckBoxMenuItem statusBarCheckBoxMenuItem = null;
    private JMenu helpMenu = null;
    private JMenuItem helpMenuItem = null;
    private JMenuItem aboutMenuItem = null;
    private JPanel FooterPanel = null;
    private JLabel CenterFooterLabel = null;
    private JLabel RightFooterLabel = null;

    /**
     * アプリケーション名
     */
    private final static String APP_NAME = "MindmapNote";

    /**
     * アプリケーションのバージョン
     */
    private final static String VERSION = "0.0.1 beta / Aug 20 2006";

    /**
     * メニューショートカットのアクセラレータキー
     */
    private final static int MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

    /**
     * jFrame の設定ファイル名
     */
    private final static String J_FRAME_FILE = "jFrame.xml";


     /**
     * 中心領域のスクロールパネル
     *
     * @return javax.swing.JScrollPane
     */
    private JScrollPane CenterScrollPane = null;
    private int DefaultCenterWidth = 200;
    private JScrollPane getCenterScrollPane() {
        if (CenterScrollPane == null) {
            CenterScrollPane = new JScrollPane();

            getPlugIn();

            CenterScrollPane.setViewportView(PlugIn.getBasePanel());
            CenterScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            CenterScrollPane.setEnabled(true);
            CenterScrollPane.setPreferredSize(new Dimension(DefaultCenterWidth-8, 600));
        }
        return CenterScrollPane;
    }


    /**
     * 西領域のスクロールパネル
     */
    private JScrollPane WestScrollPane;
    private JPanel WestInnerPane;
    private int DefaultWestWidth = 100;

    private JScrollPane getWestScrollPane() {
        if (WestScrollPane == null) {

            // 一覧を生成
            WestInnerPane = new JPanel(new BorderLayout());
            WestInnerPane.setBackground(Color.blue);
            WestInnerPane.add(new EventList(), BorderLayout.CENTER);
            WestScrollPane = new JScrollPane();//スクロールバーを付ける

            WestScrollPane.setViewportView(WestInnerPane);
            WestScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            WestScrollPane.setEnabled(true);
            WestScrollPane.setPreferredSize(new Dimension(DefaultWestWidth, 600));
        }

        return WestScrollPane;
    }

    /**
     * 東領域のスクロールパネル
     */
    private JScrollPane EastScrollPane;
    private JPanel EastInnerPane;
    private int DefaultEastWidth = 100;

    private JScrollPane getEastScrollPane() {
        if (EastScrollPane == null) {
            // 一覧を生成
            EastInnerPane = new JPanel(new BorderLayout());
            EastInnerPane.setBackground(Color.blue);
            EastInnerPane.add(new EventList(), BorderLayout.CENTER);
            EastScrollPane = new JScrollPane();//スクロールバーを付ける

            EastScrollPane.setViewportView(EastInnerPane);
            EastScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            EastScrollPane.setEnabled(true);
            EastScrollPane.setPreferredSize(new Dimension(DefaultEastWidth, 600));

        }
        return EastScrollPane;
    }


    /**
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
            if (getPlugIn().getFileMenuItemCandidates() != null) {
                for (JMenuItem item : getPlugIn().getFileMenuItemCandidates()) {
                    fileMenu.add(item);
                }
            }
        }
        return fileMenu;
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
            if (getPlugIn().getEditMenuItemCandidates() != null) {
                for (JMenuItem item : PlugIn.getEditMenuItemCandidates()) {
                    editMenu.add(item);
                }
            }

            editMenu.addMenuListener(new MenuListener() {
                public void menuSelected(MenuEvent e) {
                    for (JMenuItem item : PlugIn.getEditMenuItemCandidates()) {
                    }
                }

                public void menuDeselected(MenuEvent e) {//メニューの選択が解除されたときに呼び出されます。
                }

                public void menuCanceled(MenuEvent e) {
                }
            });

            editMenu.addMenuListener(new MenuListener() {
                public void menuSelected(MenuEvent e) {
                    getPlugIn().updateEditMenuItemState();
                }

                public void menuDeselected(MenuEvent e) {//メニューの選択が解除されたときに呼び出されます。
                    getPlugIn().resetEditMenuItemState();
                }

                public void menuCanceled(MenuEvent e) {
                }
            });
        }

        return editMenu;
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
            if (getPlugIn().getDispMenuItemCandidates() != null) {
                for (JMenuItem item : PlugIn.getDispMenuItemCandidates()) {
                    displayMenu.add(item);
                }
            }
            displayMenu.addSeparator();
            displayMenu.add(getStatusBarCheckBoxMenuItem());
        }
        return displayMenu;
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
                        CenterFooterLabel.setVisible(true);
                        RightFooterLabel.setVisible(true);
                    } else {
                        CenterFooterLabel.setVisible(false);
                        RightFooterLabel.setVisible(false);
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
            if (getPlugIn().getHelpMenuItemCandidates() != null) {
                for (JMenuItem item : PlugIn.getHelpMenuItemCandidates()) {
                    helpMenu.add(item);
                }
            }
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
     * This method initializes FooterPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getFooterPanel() {
        if (FooterPanel == null) {
            FooterPanel = new JPanel();
            FooterPanel.setLayout(new BorderLayout());
            FooterPanel.add(getCenterFooterLabel(), BorderLayout.CENTER);
            FooterPanel.add(getRightFooterLabel(), BorderLayout.EAST);
            getCenterFooterLabel().setText(PlugIn.getCenterFooterLabelTextCandidate());
            getRightFooterLabel().setText(PlugIn.getRightFooterLabelTextCandidate());
        }
        return FooterPanel;
    }

    /**
     * This method initializes CenterFooterLabel
     *
     * @return javax.swing.JLabel
     */
    private JLabel getCenterFooterLabel() {
        if (CenterFooterLabel == null) {
            CenterFooterLabel = new JLabel();
            setCenterFooterLabelText("");
            CenterFooterLabel.setName("CenterFooterLabel");
        }
        return CenterFooterLabel;
    }

    /**
     * This method initializes RightFooterLabel
     *
     * @return javax.swing.JLabel
     */
    private JLabel getRightFooterLabel() {
        if (RightFooterLabel == null) {
            RightFooterLabel = new JLabel();
            setCenterFooterLabelText("");
            RightFooterLabel.setName("RightFooterLabel");
        }
        return RightFooterLabel;
    }


    public static void main(String[] args) {
        BaseFrame frame = (BaseFrame) ContainerSaverAndLoader.load(J_FRAME_FILE);
        if (frame == null) {
            System.out.println("新しいBaseFrameの構築開始。");
            frame = new BaseFrame();//
            //frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        }
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * これはデフォルトのコンストラクタです
     */
    public BaseFrame() {
        super();

        initialize();
    }

    private JFileChooser jFileChooser = new JFileChooser();

    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize() {
        //this.setSize(1200, 600);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(jFileChooser);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(jContentPane, "エラーが発生しました", e
                    .toString(), JOptionPane.ERROR_MESSAGE);
        }

        this.setContentPane(getJContentPane());

        this.setJMenuBar(getJJMenuBar());
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setTitle("");
        this.setLocationRelativeTo(null);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                PlugIn.exit();
                System.out.println("windowClosing");
                Toolkit.getDefaultToolkit().beep();
                Object[] options = {"Save", "Discard", "Cancel"};
                int retValue = JOptionPane.showOptionDialog(
                        BaseFrame.this, "<html>Save: Exit & Save Changes<br>Discard: Exit & Discard Changes<br>Cancel: Continue</html>",
                        "Exit Options", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                if (retValue == JOptionPane.YES_OPTION) {
                    System.out.println("exit");
                    BaseFrame.this.dispose();
                } else if (retValue == JOptionPane.NO_OPTION) {
                    System.out.println("Exit without save");
                    BaseFrame.this.dispose();
                } else if (retValue == JOptionPane.CANCEL_OPTION) {
                    System.out.println("Cancel exit");
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {
                System.out.println("windowClosed");
                System.exit(0); //webstart
            }
        });

    }

    /**
     * This method initializes jContentPane
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());

            jContentPane.add(getCenterBasePanel(), BorderLayout.CENTER);//中心パネル領域のはめ込み
            jContentPane.add(getWestScrollPane(), BorderLayout.LINE_START);//西領域スクロールパネルのはめ込み
            jContentPane.add(getEastScrollPane(), BorderLayout.EAST);//東領域スクロールパネルのはめ込み

            jContentPane.add(getFooterPanel(), BorderLayout.SOUTH);
        }
        return jContentPane;
    }


    private JPanel CenterBasePanel;

    int clickStart_X = 0;
    int dragged_distance = 0;
    int delta = 0;


    /**
     * 中心領域土台パネル（西リサイズバーとCenterScrollPaneと東リサイズバーを乗せるパネル）
     *
     * @return
     */

    private JPanel getCenterBasePanel() {
        if (CenterBasePanel == null) {
            CenterBasePanel = new JPanel();
            CenterBasePanel.setLayout(new BorderLayout());
            CenterBasePanel.setPreferredSize(new Dimension(DefaultCenterWidth, 600));

            CenterBasePanel.add(getCenterScrollPane(), BorderLayout.CENTER);

            /**
             * 西リサイズバー
             */
            final JPanel WestResizeBar = new JPanel();
            WestResizeBar.setPreferredSize(new Dimension(4, 600));
            WestResizeBar.setBackground(Color.RED);
            CenterBasePanel.add(WestResizeBar, BorderLayout.WEST);
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
                    //System.out.println("mouseReleased");
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

                boolean shouldWestResizeBarMove = true;
                Dimension NewWestScrollPaneDimension;
                Dimension NewCenterPaneDimension;
                int MinimumWidth = 1;
                int MaximumWidth = 200;

                @Override
                public void mouseDragged(MouseEvent e) {
                    System.out.println("mouseDragged");
                    System.out.println("WestScrollPane.getWidth()=" + WestScrollPane.getWidth());

                    //マウス移動量の計算
                    dragged_distance = (e.getXOnScreen() - clickStart_X);
                    //パネル移動量の設定
                    delta = dragged_distance / 1;
                    System.out.println("delta=" + delta);

                    //素早くドラッグした時に左端を越えてまで縮小するのを防ぐ機構
                    if (delta * (-1) > WestScrollPane.getWidth()) {
                        delta = -(WestScrollPane.getWidth() - MinimumWidth);
                        System.out.println("修正delta=" + delta);
                    }

                    //素早くドラッグした時に最大幅を越えてまで拡大するのを防ぐ機構
                    if (delta > MaximumWidth - WestScrollPane.getWidth()) {
                        delta = MaximumWidth - WestScrollPane.getWidth();
                        System.out.println("修正delta=" + delta);
                    }

                    //最小幅に達したらリサイズバーを止める機構
                    shouldWestResizeBarMove = true;
                    if (WestScrollPane.getWidth() < MinimumWidth && delta < 0) shouldWestResizeBarMove = false;

                    //最大幅に達したらリサイズバーを止める機構
                    if (WestScrollPane.getWidth() > MaximumWidth && delta > 0) shouldWestResizeBarMove = false;


                    if (shouldWestResizeBarMove) {
                        //西スクロールパネ領域のサイズ設定
                        NewWestScrollPaneDimension = new Dimension(WestScrollPane.getWidth() + delta, WestScrollPane.getHeight());
                        WestScrollPane.setSize(NewWestScrollPaneDimension);
                        WestScrollPane.setPreferredSize(NewWestScrollPaneDimension);//これがないとテキスト領域が変更された時もとに戻ってしまう。
                        WestInnerPane.setSize(NewWestScrollPaneDimension);
                        WestInnerPane.setPreferredSize(NewWestScrollPaneDimension);//これがないとテキスト領域が変更された時もとに戻ってしまう。

                        //中心panel領域のサイズ・位置設定
                        NewCenterPaneDimension = new Dimension(CenterBasePanel.getWidth() - delta, CenterBasePanel.getHeight());
                        CenterBasePanel.setSize(NewCenterPaneDimension);
                        CenterBasePanel.setPreferredSize(NewCenterPaneDimension);
                        CenterScrollPane.setSize(NewCenterPaneDimension);
                        CenterScrollPane.setPreferredSize(NewCenterPaneDimension);

                        CenterBasePanel.setBounds(CenterBasePanel.getX() + delta, CenterBasePanel.getY(), CenterBasePanel.getWidth(), CenterBasePanel.getHeight());

                        CenterBasePanel.revalidate();//repaint()ではダメ
                        WestScrollPane.revalidate();//repaint()ではダメ

                        clickStart_X = e.getXOnScreen();
                        System.out.println("set clickStart_X=" + clickStart_X);
                    }

                }

                @Override
                public void mouseMoved(MouseEvent e) {

                }
            });

            /**
             * 東リサイズバー
             */
            JPanel EastResizeBar = new JPanel();
            EastResizeBar.setPreferredSize(new Dimension(4, 600));
            EastResizeBar.setBackground(Color.BLUE);
            CenterBasePanel.add(EastResizeBar, BorderLayout.EAST);
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

                boolean shouldEastResizeBarMove = true;
                Dimension NewEastScrollPaneDimension;
                Dimension NewCenterPaneDimension;
                int MinimumWidth = 1;
                int MaximumWidth = 200;

                @Override
                public void mouseDragged(MouseEvent e) {
                    System.out.println("mouseDragged");
                    System.out.println("EastScrollPane.getWdth()=" + EastScrollPane.getWidth());

                    //マウス移動量の計算
                    dragged_distance = (e.getXOnScreen() - clickStart_X);
                    //パネル移動量の設定
                    delta = dragged_distance / 1;
                    System.out.println("delta=" + delta);

                    //素早くドラッグした時に右端を越えてまで縮小するのを防ぐ機構
                    if (delta > EastScrollPane.getWidth()) {
                        delta = EastScrollPane.getWidth() - MinimumWidth;
                        System.out.println("修正delta=" + delta);
                    }

                    //素早くドラッグした時に最大幅を越えてまで拡大するのを防ぐ機構
                    if (delta * (-1) > MaximumWidth - EastScrollPane.getWidth()) {
                        delta = MaximumWidth - EastScrollPane.getWidth();
                        System.out.println("修正delta=" + delta);
                    }

                    //最小幅に達したらリサイズバーを止める機構
                    shouldEastResizeBarMove = true;
                    if (EastScrollPane.getWidth() < MinimumWidth && delta > 0) shouldEastResizeBarMove = false;

                    //最大幅に達したらリサイズバーを止める機構
                    if (EastScrollPane.getWidth() > MaximumWidth && delta < 0) shouldEastResizeBarMove = false;

                    if (shouldEastResizeBarMove) {
                        //東スクロールパネ領域のサイズ設定
                        NewEastScrollPaneDimension = new Dimension(EastScrollPane.getWidth() - delta, EastScrollPane.getHeight());
                        EastScrollPane.setSize(NewEastScrollPaneDimension);
                        EastScrollPane.setPreferredSize(NewEastScrollPaneDimension);//これがないとテキスト領域が変更された時もとに戻ってしまう。
                        EastInnerPane.setSize(NewEastScrollPaneDimension);
                        EastInnerPane.setPreferredSize(NewEastScrollPaneDimension);//これがないとテキスト領域が変更された時もとに戻ってしまう。

                        //中心panel領域のサイズ・位置設定
                        NewCenterPaneDimension = new Dimension(CenterBasePanel.getWidth() + delta, CenterBasePanel.getHeight());
                        CenterBasePanel.setSize(NewCenterPaneDimension);
                        CenterBasePanel.setPreferredSize(NewCenterPaneDimension);//これがないとテキスト領域が変更された時もとに戻ってしまう。
                        CenterScrollPane.setSize(NewCenterPaneDimension);
                        CenterScrollPane.setPreferredSize(NewCenterPaneDimension);//これがないとテキスト領域が変更された時もとに戻ってしまう。

                        EastScrollPane.setBounds(EastScrollPane.getX() + delta, EastScrollPane.getY(), EastScrollPane.getWidth(), EastScrollPane.getHeight());

                        CenterBasePanel.revalidate();//repaint()ではダメ
                        EastScrollPane.revalidate();//repaint()ではダメ

                        clickStart_X = e.getXOnScreen();
                        System.out.println("set clickStart_X=" + clickStart_X);
                    }
                }

                @Override
                public void mouseMoved(MouseEvent e) {

                }
            });

        }
        return CenterBasePanel;
    }


    /**
     * プラグインの取得
     * @return
     */
    private ExpectationsAsPlugin getPlugIn() {
        if (PlugIn == null) {
            PlugIn = new MyTextAreaPanel();
            PlugIn.setPluginBase(this);//
        }
        return PlugIn;
    }

    /**
     * ウィンドウのタイトルを「text - アプリケーション名」にします
     *
     * @param text
     */
    public void setTitle(String text) {
        super.setTitle(text + " - " + APP_NAME);
    }

    /**
     * CenterFooterLabel に　文字列（text） を表示する。
     */
    public void setCenterFooterLabelText(String text) {
        CenterFooterLabel.setText(text);
    }

    /**
     * @param text
     */
    public void setRightFooterLabelText(String text) {
        RightFooterLabel.setText(text);
    }

    @Override
    public void updatePluginInfo() {
        setTitle(PlugIn.getTitleTextCandidate());
        setCenterFooterLabelText(PlugIn.getCenterFooterLabelTextCandidate());
        setRightFooterLabelText(PlugIn.getRightFooterLabelTextCandidate());
    }

}
