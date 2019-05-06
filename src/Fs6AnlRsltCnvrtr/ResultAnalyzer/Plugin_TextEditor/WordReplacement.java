package Fs6AnlRsltCnvrtr.ResultAnalyzer.Plugin_TextEditor;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.Pattern;

/**
 * 「検索と置換」のダイアログウィンドウ
 */
public class WordReplacement extends JDialog {

    static final WordReplacement INSTANCE = new WordReplacement();
    protected JPanel jContentPane = null;
    JPanel basePanel = null;
    private JPanel footerPanel = null;
    JLabel searchLabel = null;
    JTextField searchTextField = null;
    JLabel replaceLabel = null;
    JTextField replaceTextField = null;
    private JCheckBox UpperLowerCheckBox = null;
    private JPanel jPanel2 = null;
    private JRadioButton upRadioButton = null;
    private JRadioButton downRadioButton = null;
    private JButton searchNextButton = null;
    private JButton replaceAndSearchButton = null;
    private JButton replaceAllButton = null;
    private JButton cancelButton = null;

    /**
     * This is the default constructor
     */
    public WordReplacement() {
        super();
        initialize();
    }

    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize() {
        this.setResizable(false);
        this.setTitle("検索と置換");
        this.setSize(450, 200);
        this.setContentPane(getJContentPane());
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                hasSearched = false;
            }
        });
    }

    /**
     * This method initializes jContentPane
     *
     * @return javax.swing.JPanel
     */
    JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.setPreferredSize(new Dimension(500, 200));
            jContentPane.add(getCenterPanel(), BorderLayout.CENTER);
            jContentPane.add(getFooterPanel(), BorderLayout.SOUTH);
        }
        return jContentPane;
    }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getCenterPanel() {
        if (basePanel == null) {
            replaceLabel = new JLabel();
            searchLabel = new JLabel();
            basePanel = new JPanel();
            searchLabel.setText("検索する文字列");
            replaceLabel.setText("置換後の文字列");
            basePanel.add(searchLabel, null);
            basePanel.add(getSearchTextField(), null);
            basePanel.add(replaceLabel, null);
            basePanel.add(getReplaceTextField(), null);
            basePanel.add(getJCheckBox(), null);
            basePanel.add(getJPanel2(), null);
        }
        return basePanel;
    }

    /**
     * This method initializes jPanel1
     *
     * @return javax.swing.JPanel
     */
    private JPanel getFooterPanel() {
        if (footerPanel == null) {
            footerPanel = new JPanel();
            footerPanel.add(getSearchNextButton(), null);
            footerPanel.add(getReplaceAndSearchButton(), null);
            footerPanel.add(getReplaceAllButton(), null);
            footerPanel.add(getCancelButton(), null);
        }
        return footerPanel;
    }

    /**
     * This method initializes searchTextField
     *
     * @return javax.swing.JTextField
     */
    JTextField getSearchTextField() {
        if (searchTextField == null) {
            searchTextField = new JTextField();
            searchTextField.setPreferredSize(new Dimension(300, 20));
        }
        return searchTextField;
    }

    /**
     * This method initializes replaceTextField
     *
     * @return javax.swing.JTextField
     */
    JTextField getReplaceTextField() {
        if (replaceTextField == null) {
            replaceTextField = new JTextField();
            replaceTextField.setPreferredSize(new Dimension(300, 20));
        }
        return replaceTextField;
    }

    /**
     * This method initializes jCheckBox
     *
     * @return javax.swing.JCheckBox
     */
    JCheckBox getJCheckBox() {
        if (UpperLowerCheckBox == null) {
            UpperLowerCheckBox = new JCheckBox();
            UpperLowerCheckBox.setText("大文字と小文字を区別する(C)");
            UpperLowerCheckBox.setMnemonic(KeyEvent.VK_C);
        }
        return UpperLowerCheckBox;
    }

    /**
     * This method initializes jPanel2
     *
     * @return javax.swing.JPanel
     */
    JPanel getJPanel2() {
        if (jPanel2 == null) {
            TitledBorder titledBorder1 = BorderFactory
                    .createTitledBorder(
                            null,
                            "検索する方向",
                            TitledBorder.DEFAULT_JUSTIFICATION,
                            TitledBorder.DEFAULT_POSITION,
                            null, null);
            jPanel2 = new JPanel();
            jPanel2.setBorder(titledBorder1);
            JRadioButton upRadioButton = getUpRadioButton();
            JRadioButton downRadioButton = getDownRadioButton();
            downRadioButton.setSelected(true);
            jPanel2.add(upRadioButton, null);
            jPanel2.add(downRadioButton, null);
            ButtonGroup buttonGroup = new ButtonGroup();
            buttonGroup.add(upRadioButton);
            buttonGroup.add(downRadioButton);
        }
        return jPanel2;
    }

    /**
     * This method initializes upRadioButton
     *
     * @return javax.swing.JRadioButton
     */
    private JRadioButton getUpRadioButton() {
        if (upRadioButton == null) {
            upRadioButton = new JRadioButton();
            upRadioButton.setText("上へ(U)");
            upRadioButton.setMnemonic(KeyEvent.VK_U);
        }
        return upRadioButton;
    }

    /**
     * This method initializes downRadioButton
     *
     * @return javax.swing.JRadioButton
     */
    private JRadioButton getDownRadioButton() {
        if (downRadioButton == null) {
            downRadioButton = new JRadioButton();
            downRadioButton.setText("下へ(D)");
            downRadioButton.setMnemonic(KeyEvent.VK_D);
            downRadioButton.setSelected(true);
        }
        return downRadioButton;
    }

    /**
     * This method initializes searchNextButton
     *
     * @return javax.swing.JButton
     */
    private JButton getSearchNextButton() {
        if (searchNextButton == null) {
            searchNextButton = new JButton();
            searchNextButton.setText("次を検索(F)");
            searchNextButton.setMnemonic(KeyEvent.VK_F);
            getRootPane().setDefaultButton(searchNextButton);
            searchNextButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    search();
                }
            });
        }
        return searchNextButton;
    }

    /**
     * 検索済みかどうかを示すフラグ
     */
    boolean hasSearched = false;

    /**
     * This method initializes replaceAndSearchButton
     *
     * @return javax.swing.JButton
     */
    private JButton getReplaceAndSearchButton() {
        if (replaceAndSearchButton == null) {
            replaceAndSearchButton = new JButton();
            replaceAndSearchButton.setText("置換して次に(R)");
            replaceAndSearchButton.setMnemonic(KeyEvent.VK_R);
            replaceAndSearchButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (hasSearched) {
                        replace();
                        hasSearched = false;
                    } else {
                        search();
                        hasSearched = true;
                    }
                }
            });
        }
        return replaceAndSearchButton;
    }

    /**
     * This method initializes replaceAllButton
     *
     * @return javax.swing.JButton
     */
    private JButton getReplaceAllButton() {
        if (replaceAllButton == null) {
            replaceAllButton = new JButton();
            replaceAllButton.setText("すべて置換(A)");
            replaceAllButton.setMnemonic(KeyEvent.VK_A);
            replaceAllButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    replaceAll();
                }
            });
        }
        return replaceAllButton;
    }

    /**
     * This method initializes cancelButton
     *
     * @return javax.swing.JButton
     */
    private JButton getCancelButton() {
        if (cancelButton == null) {
            cancelButton = new JButton();
            cancelButton.setText("取消し");
            Action action = new AbstractAction("取消し") {
                public void actionPerformed(ActionEvent ae) {
                    hasSearched = false;
                    setVisible(false);
                }
            };
            cancelButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                    KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "取消し");
            cancelButton.getActionMap().put("取消し", action);
            cancelButton.setAction(action);
        }
        return cancelButton;
    }

    private JTextArea jTextArea;

    /**
     * 文字列を置換します
     */
    private void replace() {
        String searchingWord = searchTextField.getText();
        String replacingWord = replaceTextField.getText();
        String str = jTextArea.getSelectedText();
        if (str == null) {
            return;
        }
        if (str.equals(searchingWord)
                || (str.equalsIgnoreCase(searchingWord) && !UpperLowerCheckBox
                .isSelected())) {
            jTextArea.replaceSelection(replacingWord);
        }

    }

    /**
     * すべての文字列を置換します
     */
    private void replaceAll() {
        String searchingWord = searchTextField.getText();
        if (searchingWord.equals("")) {
            return;
        }
        String replacingWord = replaceTextField.getText();
        String str = jTextArea.getText();
        if (UpperLowerCheckBox.isSelected()) {
            if (str.indexOf(searchingWord) < 0) {
                JOptionPane.showMessageDialog(jContentPane, "見つかりません",
                        "見つかりません", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // J2SE 5.0以降用コード
            // jTextArea.setText(Pattern.compile(searchingWord, Pattern.LITERAL)
            // .matcher(str).replaceAll(replacingWord));
            jTextArea.setText(str.replaceAll("\\Q" + searchingWord,
                    replacingWord));
        } else {
            if (str.toLowerCase().indexOf(searchingWord.toLowerCase()) < 0) {
                JOptionPane.showMessageDialog(jContentPane, "見つかりません",
                        "見つかりません", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // J2SE 5.0以降用コード
            // jTextArea.setText(Pattern.compile(searchingWord,
            // Pattern.LITERAL | Pattern.CASE_INSENSITIVE).matcher(str)
            // .replaceAll(replacingWord));
            jTextArea.setText(Pattern.compile("\\Q" + searchingWord,
                    Pattern.CASE_INSENSITIVE).matcher(str).replaceAll(
                    replacingWord));
        }
    }

    /**
     * 文字列を検索します
     */
    private void search() {
        search(downRadioButton.isSelected());
    }

    /**
     * 最後まで検索したかどうかを示すフラグ
     */
    private boolean searchedAll = false;

    /**
     * 文字列を検索します
     *
     * @param isSelected 下方向に検索する場合、真
     */
    private void search(boolean isSelected) {
        String word = searchTextField.getText();
        if (word.equals("")) {
            return;
        }
        String str = jTextArea.getText();
        if (searchedAll) {
            setTitle("検索と置換");
            searchedAll = false;
        }
        if (!UpperLowerCheckBox.isSelected()) {
            str = str.toLowerCase();
            word = word.toLowerCase();
        }
        if (isSelected) {
            int num = jTextArea.getCaretPosition();
            if (str.indexOf(word) < 0) {
                JOptionPane.showMessageDialog(jContentPane, "見つかりません",
                        "見つかりません", JOptionPane.WARNING_MESSAGE);
                return;
            }
            num = str.indexOf(word, num);
            if (num < 0) {
                num = str.indexOf(word);
                setTitle(getTitle() + "(最後まで検索したので戻ります)");
                searchedAll = true;
            }
            jTextArea.select(num, num + word.length());
        } else {
            if (str.lastIndexOf(word) < 0) {
                JOptionPane.showMessageDialog(jContentPane, "見つかりません",
                        "見つかりません", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int num = jTextArea.getSelectionStart() - 1;
            num = str.lastIndexOf(word, num);
            if (num < 0) {
                num = str.lastIndexOf(word);
                setTitle(getTitle() + "(最後まで検索したので戻ります)");
                searchedAll = true;
            }
            jTextArea.select(num, num + word.length());
        }
    }

    /**
     * 文字列を検索します
     *
     * @param next 次を検索する場合、真
     */
    void find(boolean next) {
        search(!(next ^ downRadioButton.isSelected()));
    }

    /**
     * 置換に関するフィールドの値をリセットします
     */
    void reset() {
        hasSearched = false;
    }

    /**
     * jTextArea を設定します
     *
     * @param jTextArea
     */
    void setJTextArea(JTextArea jTextArea) {
        this.jTextArea = jTextArea;
        this.setLocationRelativeTo(jTextArea);
    }
}
