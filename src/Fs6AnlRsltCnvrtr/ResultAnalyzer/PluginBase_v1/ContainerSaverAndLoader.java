package Fs6AnlRsltCnvrtr.ResultAnalyzer.PluginBase_v1;

import java.awt.*;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;

/**
 * 
 * Container の設定の読み込みと保存をするクラス
 * 
 */
public class ContainerSaverAndLoader {
	private ContainerSaverAndLoader() {
	}

	/**
	 * file から設定を読み込みます
	 * 
	 * @param file
	 * @return
	 */
	public static Container load(String file) {
		Container container = null;
		XMLDecoder decoder = null;
		try {
			decoder = new XMLDecoder(new BufferedInputStream(
					new FileInputStream(file)));
			container = (Container) decoder.readObject();
		} catch (FileNotFoundException e) {
			System.err.println("設定ファイル " + file + " が見つかりません");
		} finally {
			if (decoder != null) {
				decoder.close();
			}
		}
		return container;
	}

	/**
	 * file に container の設定を保存します
	 * 
	 * @param container;日本語そうなノコ
	 * @param file
	 */
	public void save(Container container, String file) {
		XMLEncoder encoder = null;
		boolean flag = false;
		try {
			for (int i = 0; i < 10; i++) {
				// ロックファイル
				File tempFile = new File(file + ".tmp");
				if (tempFile.createNewFile()) {
					encoder = new XMLEncoder(new BufferedOutputStream(
							new FileOutputStream(file)));
					encoder.writeObject(container);
					flag = true;
					tempFile.delete();
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// 無視する
				}
			}
		} catch (FileNotFoundException e) {
			System.err.println("設定ファイル " + file + " が見つかりません");
		} catch (IOException e) {
			System.err.println("入出力エラーが発生しました");
		} finally {
			if (encoder != null) {
				encoder.close();
			}
		}
		if (!flag) {
			System.err.println("設定ファイル" + file + "を保存できませんでした");
		}
	}
}
