/*
 * http://itpro.nikkeibp.co.jp/article/COLUMN/20060410/234874/
 */
package Fs6AnlRsltCnvrtr.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.regex.Pattern;

/**
 * @author issey
 */
public class FileCopyTools_ver2 {

    String targetFolderPath;

    /**
     * コンストラクタ
     *
     * @param targetFolderPath 転送先
     */
    public FileCopyTools_ver2(String targetFolderPath) {
        this.targetFolderPath = targetFolderPath + "\\";
    }

    /**
     * メソッド
     */
    /**
     * 新しいフォルダを作る。もし存在しなければ作る。
     *
     * @param ParentFolderPath
     * @return
     * @throws Error
     */
    public static String createNewFolder(String ParentFolderPath) throws Exception {
        String msg = "-------createNewFolder-------\n";
        msg += ParentFolderPath + "の作成を試みます。\n";

        File TargetFolder = new File(ParentFolderPath);

        if (TargetFolder.isFile()) {
            msg += "同名のファイル（フォルダではなくファイル）が存在するようです。\n";
            Exception ex = new Exception("同名のファイル（フォルダではなくファイル）が存在するようです。\n");
            throw ex;
        } else if (TargetFolder.exists()) {
            msg += "必要なフォルダは既に存在します。\n";
        } else if (!TargetFolder.exists()) {
            //もし存在しなければ作る
            if (TargetFolder.mkdirs()) {
                msg = "必要なフォルダを新たに作成しました。\n";

            } else {
                msg = "必要なフォルダの作成に失敗しました。\n";
            }
        } else {
            Exception ex = new Exception("不明なエラー\n");
            throw ex;
        }

        return msg;
    }

    /**
     * @param ParentFolderPath
     * @param FileNameWithExtention
     * @param overwrightFlag
     * @return
     * @throws Exception
     */
    public static String createANewEmptyFile(String ParentFolderPath, String FileNameWithExtention, boolean overwrightFlag) throws Exception {

        String msg = "-------createANewEmptyFile-------\n";
        msg += ParentFolderPath + "に新しいファイル" + FileNameWithExtention + "の作成を試みます。\n";
        if (!new File(ParentFolderPath).exists()) {
            //Exception e = new Exception("親フォルダが存在しません。");
            //throw new Exception();
            msg += "親フォルダを作成します。\n";
            msg += createNewFolder(ParentFolderPath);
        } else {
            msg += ParentFolderPath + "に空の新しいファイルを作成します。\n";
        }


        /* **** まずは拡張子を分離 **** */
        String splitTargetLine = FileNameWithExtention;
        String Extension;
        Pattern splitMark = Pattern.compile("\\.");
        String[] splitResult1 = splitMark.split(splitTargetLine);
        if (splitResult1.length > 1) {//拡張子を持っている場合
            Extension = splitResult1[splitResult1.length - 1];
        } else {
            Extension = "";
        }
        //System.out.println("Extension:" + Extension + "\n");

        /* **** 次に拡張子以外を分離 **** */
        String FileNameWithoutExtension = splitResult1[0];
        for (int c = 1; c < splitResult1.length - 1; c++) {
            FileNameWithoutExtension = FileNameWithoutExtension + "." + splitResult1[c];
        }
        //System.out.println("FileNameWithoutExtension:" + FileNameWithoutExtension + "\n");


        /*
         * Fileクラスの構築
         */
        String HeadString = FileNameWithoutExtension;
        String FootString = "." + Extension;
        int addNum = 0;
        File NewFile = new File(ParentFolderPath + "\\" + HeadString + FootString);
        //上書き禁止なら数字を追加した新しいファイル名を提案する
        if (!overwrightFlag) {
            while (NewFile.exists()) {
                addNum++;
                NewFile = new File(ParentFolderPath + "\\" + HeadString + "(" + addNum + ")" + FootString);
            }
        }
        //msg += "新しいファイルの名前: " + NewFile.getName() + "\n" ;

        /* ****新しいファイルの作成**** */
        try {
            //未だ存在しない
            if (!NewFile.exists()) {
                NewFile.createNewFile();
                msg += "新しいファイル " + NewFile.getName() + " を作成しました";
            } else {//既に存在する
                if (NewFile.canWrite()) {//書き込めなる
                    NewFile.createNewFile();
                    msg += "既存のファイル" + NewFile.getName() + "に上書きしました。";
                } else {//書き込めない
                    msg += "新しいファイルの作成に失敗しました。\n既にファイルは存在し読み取り専用である可能性があります。";
                }
            }
        } catch (Exception ex) {
            msg += "新しいファイルの作成に失敗しました。\n";
            msg += " " + ex.getMessage();
        }


        return msg;
    }

    /**
     * @param srcFilePath
     * @param newName
     * @return
     */
    public static boolean reName(String srcFilePath, String newName) {
        File srcFile = new File(srcFilePath);
        if (!srcFile.exists()) {
            System.out.println("リネーム対象となるファイルが存在しません。");
            return false;
        }
        if (newName == "" || newName == null) {
            System.out.println("新しい名前の入力が空です。");
            return false;
        }
        File newFile = new File(srcFile.getParentFile().getAbsolutePath() + "\\" + newName);
        if (newFile.exists()) {
            System.out.println("既にその名前のファイルまたはフォルダが存在します。");
            return false;
        }
        try {
            srcFile.renameTo(newFile);
            return true;
        } catch (SecurityException se) {
            //SecurityException - If a security manager exists and its SecurityManager.checkWrite(java.lang.String) method denies write access to either the old or new pathnames 
            se.printStackTrace();
            return false;
        } catch (NullPointerException ne) {
            //NullPointerException - If parameter dest is null
            ne.printStackTrace();
            return false;
        }
    }

    public static String copy(String srcFilePath, String destFilePath) {
        logBuf = "";
        /* **** 想定されるエラー：コピー元がない **** */
        if (!new File(srcFilePath).exists()) {
            logBuf += "コピー元 " + srcFilePath + " が存在しません";
            return getLogBuf();
        }
        /* **** 想定されるエラー：コピー先にすでにある **** */
        if (new File(destFilePath).exists()) {
            logBuf += "コピー先にすでに " + destFilePath + " が存在します";
            return getLogBuf();
        }
        /* **** 想定されるエラー：コピー先が書き込み不可 **** */
        if (false) {
            logBuf += "コピー先が書き込み不可です";
            return getLogBuf();
        }


        logBuf += "-----FileCopyTools-----\n";

        logBuf += srcFilePath + "のコピーを試みます。\n";
        logBuf += "\n";

        try {
            logBuf += createNewFolder(new File(destFilePath).getParentFile().getAbsolutePath()) + "\n";
            logBuf += createANewEmptyFile(new File(destFilePath).getParentFile().getAbsolutePath(), new File(destFilePath).getName(), true) + "\n";
            copyTransfer(srcFilePath, destFilePath);
            logBuf += "copyに成功" + "\n";
        } catch (Exception e) {
            logBuf += "copyに失敗" + "\n";
            logBuf += e.getMessage() + "\n";

        }

        /* **** ダイアログを表示 **** */
        //JOptionPane.showMessageDialog(null, logBuf);

        return getLogBuf();

    }

    public static String copy_with_overwriting(String srcFilePath, String destFilePath) {
        logBuf = "";
        new File(destFilePath).deleteOnExit();
        if (new File(destFilePath).exists()) {
            logBuf += destFilePath + " の削除に失敗しました";
        }else{
            logBuf += copy(srcFilePath, destFilePath);
        }
        return getLogBuf();
    }

    public static String move(String srcFilePath, String destFilePath) throws IOException {
        copy(srcFilePath, srcFilePath);

        return "";
    }

    static String logBuf = "";

    public static String getLogBuf() {
        return logBuf;
    }

    public static void clearLogBuf() {
        logBuf = "";
    }

    public static void browseFile(String targetFilePath) {
        /* **** フォルダを開く **** */
        //String uriString = "file:/C:/Users/Issey/Documents/Dropbox/【本】/救急_ERの達人/表紙.jpg";
        File TargetFile = new File(targetFilePath);
        String uriString = TargetFile.toURI().toString();
        String[] command = new String[]{"Explorer", "/select," + uriString};
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command); // コマンド実行
        } catch (IOException ex) {
            //Logger.getLogger(RuntimeExeTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * コピー元のパス[srcPath]から、コピー先のパス[destPath]へ ファイルのコピーを行います。
     * コピー処理にはFileChannel#transferToメソッドを利用します。 尚、コピー処理終了後、入力・出力のチャネルをクローズします。
     *
     * @param srcPath  コピー元のパス
     * @param destPath コピー先のパス
     * @throws IOException 何らかの入出力処理例外が発生した場合
     */
    private static boolean copyTransfer(String srcPath, String destPath) throws IOException {
        FileChannel srcChannel = new FileInputStream(srcPath).getChannel();
        FileChannel destChannel = new FileOutputStream(destPath).getChannel();
        try {
            srcChannel.transferTo(0, srcChannel.size(), destChannel);
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            srcChannel.close();
            destChannel.close();
        }

    }

    public static void main(String[] arg) {
        //C_FileCopyMachine_ver1.reName("C:\\temp\\d.txt", "a.txt");

        //createNewFolder("C:\\temp\\tes");

        /*
         try {
         createANewEmptyFile("C:\\temp\\tesss", "hello", true);
         } catch (Exception ex) {
         Logger.getLogger(C_FileCopyMachine_ver1.class.getName()).log(Level.SEVERE, null, ex);
         }
         */


        copy("C:\\temp\\e.txt", "C:\\temp\\f.txt");


    }
}
