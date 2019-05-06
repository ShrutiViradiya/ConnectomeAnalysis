package Fs6AnlRsltCnvrtr.ResultAnalyzer.Plugin_TextEditor;


import FileChooserFileDrop.FileFilterConstructor.FileFilterConstructor_Extension_ver2;
import PluginBase_v1.ExpectationsAsPlugin;

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
import java.util.ArrayList;

/*
 * 作成日: 2005/08/03
 * http://null.michikusa.jp/misc/editor.html
 * 
 * 
 */

/**
 * テキストエディタ MindmapNote
 */
public class MyTextAreaPanel extends JPanel implements Printable, ExpectationsAsPlugin {

    private JPanel jContentPane = null;
    private ExpectationsAsPluginBase PluginHolder;
    private UndoManager undoManager = new UndoManager();
    private JMenuItem undoMenuItem = null;
    private JMenuItem redoMenuItem = null;
    private JMenuItem cutMenuItem = null;
    private JMenuItem copyMenuItem = null;
    private JMenuItem pasteMenuItem = null;
    private JMenuItem deleteMenuItem = null;
    private WordReplacement wordReplacement;
    private JMenuItem replaceMenuItem = null;
    private JMenuItem findNextMenuItem = null;
    private JMenuItem findPrevMenuItem = null;
    private JMenuItem goToMenuItem = null;
    private JMenuItem selectAllMenuItem = null;
    private JMenuItem fontMenuItem = null;
    private JMenuItem tabSizeMenuItem = null;
    private JMenu colorMenu = null;
    private JMenuItem backgroundMenuItem = null;
    private JMenuItem foregroundMenuItem = null;
    private JMenuItem caretMenuItem = null;
    private JMenuItem selectionMenuItem = null;
    private JMenuItem selectedTextMenuItem = null;
    private JCheckBoxMenuItem wrapCheckBoxMenuItem = null;
    /**
     * 文字コード指定のメニューのテキスト
     */
    String ENCODING_TEXT[] = {"デフォルト(D)", "SJIS", "EUC", "JIS", "UTF-8"};

    /**
     * 文字コード指定のメニューのニーモニック
     */
    int ENCODING_MNEMONIC[] = {KeyEvent.VK_D, KeyEvent.VK_S, KeyEvent.VK_E, KeyEvent.VK_J, KeyEvent.VK_8};


    // ENCODINGS[i] は、ENCODING_TEXT[i] と ENCODING_MNEMONIC[i] に対応
    /**
     * 対応している文字コード
     */
    private final static String ENCODINGS[] = {new InputStreamReader(System.in).getEncoding(), "SJIS", "EUC_JP", "ISO2022JP", "UTF8"};


    /**
     * CenterTextArea の設定ファイル名
     */
    private final static String J_TEXT_AREA_FILE = "CenterTextArea.xml";


    /**
     * CenterTextArea のデフォルトのフォント
     */
    private final static Font DEFAULT_FONT = new Font("Monospaced", Font.PLAIN, 14);

    /**
     * 編集中のファイル
     */
    private File editingFile;

    /**
     * 変更されたかどうかを示すフラグ
     */
    private boolean hasChanged = false;

    /**
     * アプリケーションの起動
     */
    public static void main(String[] args) {
        System.out.println("main : " + SwingUtilities.isEventDispatchThread());

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowTodoList();
            }
        });
    }

    /**
     * ToDoリストの生成と表示を行います。
     */
    private static void createAndShowTodoList() {

        System.out.println("createAndShowTodoList :" + SwingUtilities.isEventDispatchThread());

        JFrame mainFrame = new JFrame("ToDoリスト");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container contentPane = mainFrame.getContentPane();

        // ToDoリストを生成

        contentPane.add(new MyTextAreaPanel(), BorderLayout.CENTER);

        // Windowサイズを調整
        mainFrame.pack();
        // 表示
        mainFrame.setVisible(true);
    }


    /**
     * これはデフォルトのコンストラクタです
     */
    public MyTextAreaPanel() {
        setLayout(new BorderLayout());
        add(getCenterTextArea(), BorderLayout.CENTER);
    }

    /**
     * メニューショートカットのアクセラレータキー
     */
    private final static int MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();


    JTextArea CenterTextArea = null;

    /**
     * 中心領域のスクロールパネルのテキスト領域
     *
     * @return javax.swing.JTextArea
     */
    private JTextArea getCenterTextArea() {
        if (CenterTextArea == null) {
            CenterTextArea = new JTextArea();
            this.setFont(DEFAULT_FONT);
            InputMap inputMap = this.getInputMap();
            ActionMap actionMap = this.getActionMap();
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_I, MASK), "タブの挿入");
            actionMap.put("タブの挿入", new DefaultEditorKit.InsertTabAction());
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_J, MASK), "改行");
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_M, MASK), "改行");
            actionMap.put("改行", new DefaultEditorKit.InsertBreakAction());
            CenterTextArea.addCaretListener(new CaretListener() {
                public void caretUpdate(CaretEvent e) {
                    if (wordReplacement != null) {
                        wordReplacement.reset();
                    }
                    try {
                        int caretPosition = CenterTextArea.getCaretPosition();
                        int lineNum = CenterTextArea.getLineOfOffset(caretPosition);
                        setRightFooterLabelText(lineNum + 1, caretPosition
                                - CenterTextArea.getLineStartOffset(lineNum) + 1);
                    } catch (BadLocationException ble) {
                        JOptionPane.showMessageDialog(jContentPane,
                                "カーソルの位置が不正です", ble.toString(),
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            Document document = CenterTextArea.getDocument();
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
                    updateUndoRedoMenuItem();
                }
            });
            this.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        showJPopupMenu(CenterTextArea, e);
                    }
                }
            });
        }
        return CenterTextArea;
    }


    private JPopupMenu pop;

    private Action action[];


    /**
     * 新規ファイルを開く前に編集していたファイル
     */
    private File prevFile;

    /**
     * 新規ファイルを開きます
     */
    public void openNewFile() {
        if (hasChanged) {
            boolean closed = closeFile();
            if (!closed) {
                return;
            }
        }
        int tabSize = CenterTextArea.getTabSize();
        CenterTextArea.setDocument(new PlainDocument());
        setThisTitle();
        CenterTextArea.setTabSize(tabSize);
        setCenterFooterLabelText();
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
    public void openFile(String encoding) {
        if (hasChanged) {
            boolean closed = closeFile();
            if (!closed) {
                return;
            }
        }
        int tabSize = CenterTextArea.getTabSize();
        File file = chooseFile(true);
        if (file == null) {
            return;
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file), encoding));
            CenterTextArea.read(reader, new PlainDocument());
            reader.close();
            editingFile = file;
            editingFileEncoding = encoding;
            setThisTitle(editingFile);
            CenterTextArea.setTabSize(tabSize);
            setCenterFooterLabelText(file);
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
    public void saveFile() {
        saveFile(ENCODINGS[0]);
    }

    /**
     * 文字コード encoding でファイルを保存します
     *
     * @param encoding 文字コード
     */
    public void saveFile(String encoding) {
        saveFile(encoding, false);
    }

    /**
     * 文字コード encoding でファイルを保存します
     *
     * @param encoding 文字コード
     * @param rename   文字コードを変えずに保存するとき、ファイル名を要求させる場合に真
     */
    public void saveFile(String encoding, boolean rename) {
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
            CenterTextArea.write(writer);
            writer.close();
            editingFileEncoding = encoding;
            hasChanged = false;
            setThisTitle(editingFile);
            setCenterFooterLabelText(editingFile);
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
    public void saveFileWithName() {
        saveFile(ENCODINGS[0], true);
    }

    /**
     * 文字コード encoding でファイルを再読み込みします
     *
     * @param encoding 文字コード
     */
    public void rereadFile(String encoding) {
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
            CenterTextArea.read(reader, new PlainDocument());
            reader.close();
            editingFileEncoding = encoding;
            hasChanged = false;
            setCenterFooterLabelText(editingFile);
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
    public boolean closeFile() {
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
    public File chooseFile(boolean open) {
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
     * CenterTextArea の文書を印刷します
     */
    public void printFile() {
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
            CenterTextArea.print(g2D);
            return Printable.PAGE_EXISTS;
        }
        return Printable.NO_SUCH_PAGE;
    }

    /**
     * アプリケーションを終了させます
     */
    public void exit() {
        boolean closed = closeFile();
        if (closed) {
            setVisible(false);
            CenterTextArea.setText("");
            setThisTitle();
            //ContainerSaverAndLoader.save(CenterTextArea, J_TEXT_AREA_FILE);
            //ContainerSaverAndLoader.save(this, J_FRAME_FILE);
            //System.exit(0);
        }
    }

    @Override
    public void setPluginBase(ExpectationsAsPluginBase PluginBase) {
        this.PluginHolder = PluginBase;
    }


    /**
     * RightFooterLabel と hasChanged と UndoManager をリセットします
     */
    private void reset() {
        setRightFooterLabelText();
        hasChanged = false;
        undoManager = new UndoManager();
        Document document = CenterTextArea.getDocument();
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
                updateUndoRedoMenuItem();
            }
        });
        updateUndoRedoMenuItem();
    }

    /**
     * 変更が行われたように設定します
     */
    public void setChanged() {
        if (!hasChanged) {
            hasChanged = true;
            TitleText = "* " + TitleText;
        }
        PluginHolder.updatePluginInfo();
    }

    /**
     * ウィンドウのタイトルを「無題」にします
     */
    public void setThisTitle() {
        TitleText = "none";
    }

    /**
     * ウィンドウのタイトルを「ファイル file の名前 - アプリケーション名」にします
     *
     * @param file
     */
    public void setThisTitle(File file) {
        TitleText = file.getName() + " - " + getPluginName();
    }

    /**
     * ウィンドウのタイトルを「text - アプリケーション名」にします
     *
     * @param text
     */
    public void setTitleText(String text) {
        TitleText = text + " - " + getPluginName();
    }

    String TitleText;

    /**
     * CenterFooterLabel に「無題」と表示します
     */
    public void setCenterFooterLabelText() {
        CenterFooterLabelTextCandidate = "none";
    }

    /**
     * CenterFooterLabel にファイル file のパス名と文字コード editingFileEncoding を表示します
     *
     * @param file
     */
    public void setCenterFooterLabelText(File file) {
        CenterFooterLabelTextCandidate = file.getPath() + " [" + editingFileEncoding + "]";
    }

    String CenterFooterLabelTextCandidate;

    /**
     * RightFooterLabel に 「1行、1列 」と表示します
     */
    public void setRightFooterLabelText() {
        setRightFooterLabelText(1, 1);
    }

    /**
     * RightFooterLabel に 「row行、column列 」と表示します
     *
     * @param row    行数
     * @param column 列数
     */
    public void setRightFooterLabelText(int row, int column) {
        StringBuffer sb = new StringBuffer();
        sb.append(row);
        sb.append("行、");
        sb.append(column);
        sb.append("列　");
        RightFooterLabelTextCandidate = sb.toString();
    }

    String RightFooterLabelTextCandidate;

    public static String getjTextAreaFile() {
        return J_TEXT_AREA_FILE;
    }

    public UndoManager getUndoManager() {
        return undoManager;
    }

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
     * This method initializes undoMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    public JMenuItem getUndoMenuItem() {
        if (undoManager == null) undoManager = getUndoManager();

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
                        cue.printStackTrace();
                        JOptionPane.showMessageDialog(jContentPane, "戻せません", cue.toString(), JOptionPane.ERROR_MESSAGE);
                    }
                    updateUndoRedoMenuItem();
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
    public JMenuItem getRedoMenuItem() {

        if (undoManager == null) undoManager = getUndoManager();

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
                    updateUndoRedoMenuItem();
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
                    getCenterTextArea().cut();
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
                    getCenterTextArea().copy();
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
                    getCenterTextArea().paste();
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
                    getCenterTextArea().replaceSelection(null);
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
                    if (!CenterTextArea.getText().equals("")) {
                        if (wordReplacement == null) {
                            wordReplacement = WordReplacement.INSTANCE;
                            wordReplacement.setJTextArea(getCenterTextArea());
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
                            && !getCenterTextArea().getText().equals("")) {
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
                            && !getCenterTextArea().getText().equals("")) {
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
                            getCenterTextArea().setCaretPosition(getCenterTextArea()
                                    .getLineStartOffset(rowNum));
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
                    getCenterTextArea().selectAll();
                }
            });
        }
        return selectAllMenuItem;
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
                    fontProperty.setJTextArea(getCenterTextArea());
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
                            choice[getCenterTextArea().getTabSize() / 2 - 1]);
                    if (ans != null) {
                        int tabSize = Integer.parseInt(ans.toString());
                        getCenterTextArea().setTabSize(tabSize);
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
                            "背景色を指定してください", getCenterTextArea().getBackground());
                    if (color != null) {
                        getCenterTextArea().setBackground(color);
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
                            "文字色を指定してください", getCenterTextArea().getForeground());
                    if (color != null) {
                        getCenterTextArea().setForeground(color);
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
                            "カーソルの色を指定してください", getCenterTextArea().getCaretColor());
                    if (color != null) {
                        getCenterTextArea().setCaretColor(color);
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
                            "選択領域の背景色を指定してください", getCenterTextArea().getSelectionColor());
                    if (color != null) {
                        getCenterTextArea().setSelectionColor(color);
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
                            "選択領域の文字色を指定してください", getCenterTextArea()
                                    .getSelectedTextColor());
                    if (color != null) {
                        getCenterTextArea().setSelectedTextColor(color);
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
            wrapCheckBoxMenuItem.setSelected(getCenterTextArea().getLineWrap());
            wrapCheckBoxMenuItem.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    getCenterTextArea().setLineWrap(!getCenterTextArea().getLineWrap());
                }
            });
        }
        return wrapCheckBoxMenuItem;
    }

    /**
     * undo 出来るかどうかと、 redo 出来るかどうかとを調べます
     */
    public void updateUndoRedoMenuItem() {
        undoMenuItem.setEnabled(undoManager.canUndo());
        //getUndoMenuItem().setEnabled(undoManager.canUndo());
        redoMenuItem.setEnabled(undoManager.canRedo());
        //getRedoMenuItem().setEnabled(undoManager.canRedo());
    }


    /**
     * ポップアップメニューを表示します
     *
     * @param jTextArea
     * @param me
     */
    private void showJPopupMenu(final JTextArea jTextArea, final MouseEvent me) {
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
                    jTextArea.replaceSelection(null);
                }
            };
            action[4] = new AbstractAction("全て選択") {
                public void actionPerformed(ActionEvent ae) {
                    jTextArea.selectAll();
                }
            };
            for (int i = 0; i < 5; i++) {
                pop.add(action[i]);
                if (i == 3) {
                    pop.addSeparator();
                }
            }
        }
        boolean flg = !(jTextArea.getSelectedText() == null);
        for (int i = 0; i < 4; i++) {
            if (i != 2) {
                action[i].setEnabled(flg);
            }
        }
        action[4].setEnabled(!jTextArea.getText().equals(""));
        pop.show(jTextArea, me.getX(), me.getY());
    }

    /**
     * 編集中のファイルの文字コード
     */
    private String editingFileEncoding = ENCODINGS[0];


    @Override
    public ArrayList<JMenuItem> getFileMenuItemCandidates() {
        ArrayList<JMenuItem> candidates = new ArrayList<JMenuItem>();
        candidates.add(getNewFileMenuItem());
        candidates.add(getOpenMenuItem());
        candidates.add(getSaveMenuItem());
        candidates.add(getSaveAsMenuItem());
        candidates.add(getEncodeMenu());
        candidates.add(getPrintMenuItem());
        candidates.add(getExitMenuItem());
        return candidates;
    }

    @Override
    public ArrayList<JMenuItem> getEditMenuItemCandidates() {
        ArrayList<JMenuItem> candidates = new ArrayList<JMenuItem>();
        candidates.add(getUndoMenuItem());
        candidates.add(getRedoMenuItem());
        candidates.add(getCutMenuItem());
        candidates.add(getCopyMenuItem());
        candidates.add(getPasteMenuItem());
        candidates.add(getDeleteMenuItem());
        candidates.add(getReplaceMenuItem());
        candidates.add(getFindNextMenuItem());
        candidates.add(getFindPrevMenuItem());
        candidates.add(getGoToMenuItem());
        candidates.add(getSelectAllMenuItem());
        /*
        candidates.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent e) {
                boolean hasSelectedText = !(CenterTextArea.getSelectedText() == null);
                cutMenuItem.setEnabled(hasSelectedText);
                copyMenuItem.setEnabled(hasSelectedText);
                deleteMenuItem.setEnabled(hasSelectedText);
                boolean hasText = !CenterTextArea.getText().equals("");
                replaceMenuItem.setEnabled(hasText);
                findNextMenuItem.setEnabled(hasText);
                findPrevMenuItem.setEnabled(hasText);
            }

            public void menuDeselected(MenuEvent e) {//メニューの選択が解除されたときに呼び出されます。
                //System.out.println("menuDeselected");
                cutMenuItem.setEnabled(true);
                copyMenuItem.setEnabled(true);
                deleteMenuItem.setEnabled(true);
                replaceMenuItem.setEnabled(true);
                findNextMenuItem.setEnabled(false);
                findPrevMenuItem.setEnabled(false);
            }

            public void menuCanceled(MenuEvent e) {
            }
        });
        */
        return candidates;
    }

    @Override
    public ArrayList<JMenuItem> getDispMenuItemCandidates() {
        ArrayList<JMenuItem> candidates = new ArrayList<JMenuItem>();
        candidates.add(getFontMenuItem());
        candidates.add(getTabSizeMenuItem());
        candidates.add(getColorMenu());
        candidates.add(getWrapCheckBoxMenuItem());
        return candidates;
    }

    @Override
    public ArrayList<JMenuItem> getHelpMenuItemCandidates() {
        return null;
    }

    @Override
    public void updateEditMenuItemState() {
        boolean hasSelectedText = !(getCenterTextArea().getSelectedText() == null);
        getCutMenuItem().setEnabled(hasSelectedText);
        getCopyMenuItem().setEnabled(hasSelectedText);
        getDeleteMenuItem().setEnabled(hasSelectedText);
        boolean hasText = !getCenterTextArea().getText().equals("");
        getReplaceMenuItem().setEnabled(hasText);
        getFindNextMenuItem().setEnabled(hasText);
        getFindPrevMenuItem().setEnabled(hasText);
    }

    @Override
    public void resetEditMenuItemState() {
        getCutMenuItem().setEnabled(true);
        getCopyMenuItem().setEnabled(true);
        getDeleteMenuItem().setEnabled(true);
        getReplaceMenuItem().setEnabled(true);
        getFindNextMenuItem().setEnabled(false);
        getFindPrevMenuItem().setEnabled(false);
    }

    @Override
    public String getCenterFooterLabelTextCandidate() {
        return CenterFooterLabelTextCandidate;
    }

    @Override
    public String getRightFooterLabelTextCandidate() {
        return RightFooterLabelTextCandidate;
    }

    @Override
    public String getTitleTextCandidate() {
        return TitleText;
    }

    @Override
    public String getPluginName() {
        return null;
    }

    @Override
    public JPanel getBasePanel() {
        return this;
    }


}
