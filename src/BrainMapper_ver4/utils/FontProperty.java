package BrainMapper_ver4.utils;

import BrainMapper_ver4.core.GraphNode;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * 
 * 「フォント」のダイアログウィンドウ
 * 
 */
public class FontProperty extends JDialog {

	public static final FontProperty INSTANCE = new FontProperty();
	private JPanel jContentPane = null;
	private JPanel choosePanel = null;
	private JLabel nameLabel = null;
	private JLabel styleLabel = null;
	private JLabel sizeLabel = null;
	private JScrollPane nameScrollPane = null;
	private JList nameList = null;
	private JScrollPane styleScrollPane = null;
	private JList styleList = null;
	private JScrollPane sizeScrollPane = null;
	private JList sizeList = null;
	private JTextField nameTextField = null;
	private JTextField styleTextField = null;
	private JTextField sizeTextField = null;
	private GraphNode jTextArea = null;
	private JTextArea fontTextArea = null;
	private JPanel determinePanel = null;
	private JButton okButton = null;
	private JButton cancelButton = null;
	private JScrollPane jScrollPane = null;

	/**
	 * FontProperty クラスの jTextArea に表示されている文字のフォント
	 */
	private Font font = null;

	/**
	 * すべてのフォントファミリの名前の配列
	 */
	private final String[] fontNames = GraphicsEnvironment
			.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

	// fontStyleNames[i] が FONT_STYLE[i] に対応
	/**
	 * フォントスタイルの文字列
	 */
	private final String[] fontStyleNames = { "標準", "太字", "斜体", "太字 斜体" };

	/**
	 * フォントスタイル
	 */
	private final static int[] FONT_STYLE = { Font.PLAIN, Font.BOLD,
			Font.ITALIC, Font.ITALIC | Font.BOLD };

	/**
	 * フォントの最小の大きさ
	 */
	private final static int MIN_SIZE = 9;

	/**
	 * フォントの最大の大きさ
	 */
	private final static int MAX_SIZE = 24;

	/**
	 * This is the default constructor
	 */
	private FontProperty() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setTitle("フォント");
		this.setSize(450, 350);
		this.setContentPane(getJContentPane());
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
			jContentPane.add(getDeterminePanel(), BorderLayout.SOUTH);
			jContentPane.add(getChoosePanel(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes choosePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getChoosePanel() {
		if (choosePanel == null) {
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			sizeLabel = new JLabel();
			styleLabel = new JLabel();
			nameLabel = new JLabel();
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			choosePanel = new JPanel();
			choosePanel.setLayout(new GridBagLayout());
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			nameLabel.setText("フォント名");
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.gridy = 0;
			styleLabel.setText("スタイル");
			gridBagConstraints3.gridx = 2;
			gridBagConstraints3.gridy = 0;
			sizeLabel.setText("サイズ");
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.gridy = 2;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.weighty = 1.0;
			gridBagConstraints4.fill = GridBagConstraints.BOTH;
			gridBagConstraints5.gridx = 1;
			gridBagConstraints5.gridy = 2;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.weighty = 1.0;
			gridBagConstraints5.fill = GridBagConstraints.BOTH;
			gridBagConstraints6.gridx = 2;
			gridBagConstraints6.gridy = 2;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.weighty = 1.0;
			gridBagConstraints6.fill = GridBagConstraints.BOTH;
			gridBagConstraints8.weightx = 1.0;
			gridBagConstraints8.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.gridy = 1;
			gridBagConstraints9.weightx = 1.0;
			gridBagConstraints9.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints9.gridx = 1;
			gridBagConstraints9.gridy = 1;
			gridBagConstraints10.weightx = 1.0;
			gridBagConstraints10.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints10.gridx = 2;
			gridBagConstraints10.gridy = 1;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.weighty = 1.0;
			gridBagConstraints11.fill = GridBagConstraints.BOTH;
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.gridy = 3;
			gridBagConstraints11.gridwidth = 3;
			gridBagConstraints11.insets = new Insets(10, 10, 10, 10);
			choosePanel.add(nameLabel, gridBagConstraints1);
			choosePanel.add(styleLabel, gridBagConstraints2);
			choosePanel.add(sizeLabel, gridBagConstraints3);
			choosePanel.add(getNameTextField(), gridBagConstraints8);
			choosePanel.add(getStyleTextField(), gridBagConstraints9);
			choosePanel.add(getSizeTextField(), gridBagConstraints10);
			choosePanel.add(getJScrollPane(), gridBagConstraints11);
			choosePanel.add(getNameScrollPane(), gridBagConstraints4);
			choosePanel.add(getStyleScrollPane(), gridBagConstraints5);
			choosePanel.add(getSizeScrollPane(), gridBagConstraints6);
		}
		return choosePanel;
	}

	/**
	 * This method initializes nameScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getNameScrollPane() {
		if (nameScrollPane == null) {
			nameScrollPane = new JScrollPane();
			nameScrollPane.setViewportView(getNameList());
			nameScrollPane.setVisible(true);
		}
		return nameScrollPane;
	}

	/**
	 * This method initializes nameList
	 * 
	 * @return javax.swing.JList
	 */
	private JList getNameList() {
		if (nameList == null) {
			nameList = new JList(fontNames);
			nameList.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					if (!e.getValueIsAdjusting()) {
						JList li = (JList) e.getSource();
						String nameStr = (String) li.getSelectedValue();
						nameTextField.setText(nameStr);
						font = new Font(nameStr, font.getStyle(), font
								.getSize());
						fontTextArea.setFont(font);

					}
				}
			});
		}
		return nameList;
	}

	/**
	 * This method initializes styleScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getStyleScrollPane() {
		if (styleScrollPane == null) {
			styleScrollPane = new JScrollPane();
			styleScrollPane.setViewportView(getStyleList());
		}
		return styleScrollPane;
	}

	/**
	 * This method initializes styleList
	 * 
	 * @return javax.swing.JList
	 */
	private JList getStyleList() {
		if (styleList == null) {
			styleList = new JList(fontStyleNames);
			styleList.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					if (!e.getValueIsAdjusting()) {
						JList li = (JList) e.getSource();
						int selectedIndex = li.getSelectedIndex();
						int style;
						String styleStr;
						if (selectedIndex < 0) {
							selectedIndex = 0;
						}
						style = FONT_STYLE[selectedIndex];
						styleStr = fontStyleNames[selectedIndex];
						styleTextField.setText(styleStr);
						font = new Font(font.getName(), style, font.getSize());
						fontTextArea.setFont(font);
					}
				}
			});
		}
		return styleList;
	}

	/**
	 * This method initializes sizeScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getSizeScrollPane() {
		if (sizeScrollPane == null) {
			sizeScrollPane = new JScrollPane();
			sizeScrollPane.setViewportView(getSizeList());
		}
		return sizeScrollPane;
	}

	/**
	 * This method initializes sizeList
	 * 
	 * @return javax.swing.JList
	 */
	private JList getSizeList() {
		if (sizeList == null) {
			Object[] fontSize = new Object[MAX_SIZE - MIN_SIZE + 1];
			for (int i = MIN_SIZE; i <= MAX_SIZE; i++) {
				fontSize[i - MIN_SIZE] = Integer.toString(i);
			}
			sizeList = new JList(fontSize);
			sizeList.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					if (!e.getValueIsAdjusting()) {
						JList li = (JList) e.getSource();
						int size = li.getSelectedIndex() + MIN_SIZE;
						String sizeStr = Integer.toString(size);
						sizeTextField.setText(sizeStr);
						font = new Font(font.getName(), font.getStyle(), size);
						fontTextArea.setFont(font);
					}
				}
			});
		}
		return sizeList;
	}

	/**
	 * This method initializes nameTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getNameTextField() {
		if (nameTextField == null) {
			nameTextField = new JTextField();
			nameTextField.setEditable(false);
		}
		return nameTextField;
	}

	/**
	 * This method initializes styleTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getStyleTextField() {
		if (styleTextField == null) {
			styleTextField = new JTextField();
			styleTextField.setEditable(false);
		}
		return styleTextField;
	}

	/**
	 * This method initializes sizeTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getSizeTextField() {
		if (sizeTextField == null) {
			sizeTextField = new JTextField();
			sizeTextField.setEditable(false);
		}
		return sizeTextField;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getFontTextArea());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes fontTextArea
	 * 
	 * @return javax.swing.JTextArea
	 */
	private JTextArea getFontTextArea() {
		if (fontTextArea == null) {
			fontTextArea = new JTextArea();
			fontTextArea
					.setText("the quick brown fox jumped over the lazy dogs\n"
							+ "いろはにほへと　イロハニホヘト 伊呂波");
		}
		return fontTextArea;
	}

	/**
	 * This method initializes determinePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getDeterminePanel() {
		if (determinePanel == null) {
			determinePanel = new JPanel();
			determinePanel.add(getOkButton(), null);
			determinePanel.add(getCancelButton(), null);
		}
		return determinePanel;
	}

	/**
	 * This method initializes okButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton();
			okButton.setText("了解");
			getRootPane().setDefaultButton(okButton);
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (font != null) {
						jTextArea.setFont(font);
					}
					setVisible(false);
				}
			});
		}
		return okButton;
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
					setVisible(false);
				}
			};
			// ESCAPE キーを押したとき、cancelButton を押したときと同様の動作をするように設定
			cancelButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
					KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "取消し");
			cancelButton.getActionMap().put("取消し", action);
			cancelButton.setAction(action);
		}
		return cancelButton;
	}

	/**
	 * 引数の jTextArea の情報を nameList、styleList、sizeList に設定します
	 * 
	 * @param jTextArea
	 */
	public void setJTextArea(GraphNode jTextArea) {
		this.jTextArea = jTextArea;
		this.setLocationRelativeTo(jTextArea);
		this.font = jTextArea.getFont();

		// jTextArea のフォント名を取得して、それを nameList に設定
		String nameStr = font.getName();
		nameTextField.setText(nameStr);
		for (int i = 0, n = fontNames.length; i < n; i++) {
			if (fontNames[i].equals(nameStr)) {
				nameList.setSelectedIndex(i);
				nameList.ensureIndexIsVisible(i);
				break;
			}
		}

		// jTextArea のフォントスタイルを取得して、それを styleList に設定
		int fontStyle = font.getStyle();
		styleTextField.setText(Integer.toString(fontStyle));
		String styleName = fontStyleNames[0];
		int styleIndexNum = 0;
		for (int i = 0, n = FONT_STYLE.length; i < n; i++) {
			if (FONT_STYLE[i] == fontStyle) {
				styleIndexNum = i;
				styleName = fontStyleNames[i];
				break;
			}
		}
		styleList.setSelectedIndex(styleIndexNum);
		styleTextField.setText(styleName);

		// jTextArea のフォントサイズを取得して、それを sizeList に設定
		int fontSize = font.getSize();
		int fontIndexNum = fontSize - MIN_SIZE;
		sizeList.setSelectedIndex(fontIndexNum);
		sizeList.ensureIndexIsVisible(fontIndexNum);
	}
} // @jve:decl-index=0:visual-constraint="178,40"
