/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basic_tools;

import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author issey
 */
public class CollectionWriterAndReader_ver4 {
    /**
     * テストメソッド
     *
     * @param args
     */
    public static void main(String[] args) {
        add_test(args);
        //output_test(args);
        //load_test(args);
    }

    //テストメソッド
    public static void add_test(String[] args) {
        TreeMap sample_treemap = new TreeMap();
        sample_treemap.put("00", "姓\t名\tせい\tめい");
        sample_treemap.put("01", "上田\t一生\tうえだ");
        sample_treemap.put("02", "横瀬\t友紀子\tよこせ\tゆきこ");

        addToOdsfile(sample_treemap, new File("C:\\Temp\\test.ods"), "newsheet");

        ArrayList al = new ArrayList();
        al.add("あ");
        al.add("い");
        al.add("う");
        al.add("え");
        al.add("お");
    }

    //テストメソッド
    public static void output_test(String[] args) {
        TreeMap sample_treemap = new TreeMap();
        sample_treemap.put("00", "姓\t名\tせい\tめい");
        sample_treemap.put("01", "上田\t一生\tうえだ");
        sample_treemap.put("02", "横瀬\t友紀子\tよこせ\tゆきこ");

        outputToTextfile(sample_treemap, new File("C:\\Temp\\test.txt"));
        outputToOdsfile(sample_treemap, new File("C:\\Temp\\test.ods"), "newsheet");

        ArrayList al = new ArrayList();
        al.add("あ");
        al.add("い");
        al.add("う");
        al.add("え");
        al.add("お");
        outputToTextfile(al, new File("C:\\Temp\\あいうえお.txt"));
    }


    //テストメソッド
    public static void load_test(String[] args) {
        //テキストファイルからの読み込み
        CollectionWriterAndReader_ver4 tmo = new CollectionWriterAndReader_ver4();
        TreeMap tm = loadToTreeMap(new File("C:\\Temp\\test.txt"));

        Iterator it = tm.keySet().iterator();
        while (it.hasNext()) {
            Object key = it.next();
            System.out.println((String) key + ":" + (String) tm.get(key));
        }

    }

    /**
     * @param file
     */
    public static void outputToTextfile(TreeMap<String, String> tm, File file) {
        file.getParentFile().mkdirs();
        try {
            file.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(CollectionWriterAndReader_ver4.class.getName()).log(Level.SEVERE, null, ex);
        }

        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new FileWriter(file));

            Iterator it = tm.keySet().iterator();
            Object key;
            while (it.hasNext()) {
                key = it.next();
                bw.write((String) key + "\t" + (String) tm.get(key) + "\n");
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
            }
        }

    }

    /**
     * TreeMape→ODSファイル
     *
     * @param tm
     * @param odsfile
     */
    public static void outputToOdsfile(TreeMap<String, String> tm, File odsfile, String sheetname) {
        String[] title_strings = null;
        Object[][] data = null;

        Iterator it = tm.keySet().iterator();
        Object key;
        String value;
        String[] values;
        int row = 0;
        while (it.hasNext()) {
            key = it.next();
            value = (String) tm.get(key);
            values = value.split("\t");
            if (row == 0) {//1行目は列タイトルとして処理
                title_strings = new String[values.length + 1];
                title_strings[0] = (String) (key);
                for (int i = 1; i < values.length + 1; i++) {
                    title_strings[i] = values[i - 1];
                }
                data = new Object[tm.size()][values.length + 1];

            } else {//2行目以降はデータとして処理
                data[row - 1][0] = (String) (key);
                for (int i = 1; i < values.length + 1; i++) {
                    data[row - 1][i] = values[i - 1];
                }

            }
            row++;
        }

        TableModel model = new DefaultTableModel(data, title_strings);
        try {
            //create a ods file;
            SpreadSheet.createEmpty(model).saveAs(odsfile);
        } catch (IOException ex) {
            Logger.getLogger(CollectionWriterAndReader_ver4.class.getName()).log(Level.SEVERE, null, ex);
        }

        //シートに名前をつける
        Sheet sheet = null;
        try {
            sheet = SpreadSheet.createFromFile(odsfile).getSheet(0);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        sheet.setName(sheetname);
        try {
            sheet.getODDocument().saveAs(odsfile);
            System.out.println(odsfile.getAbsolutePath() + "へ書き出しました。");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * TreeMape→ODSファイル
     *
     * @param tm
     * @param odsfile
     */
    public static void outputToOdsfile(HashMap<String, String> tm, File odsfile, String sheetname) {
        String[] title_strings = null;
        Object[][] data = null;

        Iterator it = tm.keySet().iterator();
        Object key;
        String value;
        String[] values;
        int row = 0;
        while (it.hasNext()) {
            key = it.next();
            value = (String) tm.get(key);
            values = value.split("\t");
            if (row == 0) {//1行目は列タイトルとして処理
                title_strings = new String[values.length + 1];
                title_strings[0] = (String) (key);
                for (int i = 1; i < values.length + 1; i++) {
                    title_strings[i] = values[i - 1];
                }
                data = new Object[tm.size()][values.length + 1];

            } else {//2行目以降はデータとして処理
                data[row - 1][0] = (String) (key);
                for (int i = 1; i < values.length + 1; i++) {
                    data[row - 1][i] = values[i - 1];
                }

            }
            row++;
        }

        TableModel model = new DefaultTableModel(data, title_strings);
        try {
            //create a ods file;
            SpreadSheet.createEmpty(model).saveAs(odsfile);
        } catch (IOException ex) {
            Logger.getLogger(CollectionWriterAndReader_ver4.class.getName()).log(Level.SEVERE, null, ex);
        }

        //シートに名前をつける
        Sheet sheet = null;
        try {
            sheet = SpreadSheet.createFromFile(odsfile).getSheet(0);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        sheet.setName(sheetname);
        try {
            sheet.getODDocument().saveAs(odsfile);
            System.out.println(odsfile.getAbsolutePath() + "へ書き出しました。");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * TreeMapを既存ODSファイルへ追記する。
     * 対象ODSファイルがない場合は新しいファイルを作成する。
     * 対象シートがない場合は新しいシートを作成する。
     *
     * @param tm
     * @param odsfile
     */
    public static void addToOdsfile(TreeMap<String, String> tm, File odsfile, String sheetname) {
        //出力先ファイルの存在チェック
        if (!odsfile.exists()) {
            System.out.println(odsfile.getName() + "が存在しません");
            //System.exit(1);
            System.out.println("新規作成");
            outputToOdsfile(tm, odsfile, sheetname);
        } else {
            System.out.println(odsfile.getName() + "は存在します。");

            //引数のTreeMapをObject型の二次元配列へ変換する
            Object[][] data = null;
            String[] title_strings = null;
            Iterator it = tm.keySet().iterator();
            Object key;
            String value;
            String[] values;
            int row = 0;
            while (it.hasNext()) {
                key = it.next();
                value = (String) tm.get(key);
                values = value.split("\t");
                if (row == 0) {//1行目は列タイトルとして処理
                    title_strings = new String[values.length + 1];//keyが1列目に格納される
                    title_strings[0] = (String) (key);
                    for (int i = 0; i < values.length; i++) {
                        title_strings[i + 1] = values[i];
                    }
                    data = new Object[tm.size() - 1][values.length + 1];

                } else {//2行目以降はデータとして処理
                    data[row - 1][0] = (String) (key);
                    for (int i = 0; i < values.length; i++) {
                        data[row - 1][i + 1] = values[i];
                    }

                }
                row++;
            }


            //既存のファイルの既存のシートの１行目を取得し、タイトル行として妥当かどうか検証
            Sheet sheet = null;
            try {
                sheet = SpreadSheet.createFromFile(odsfile).getSheet(sheetname);
            } catch (NullPointerException nex) {
                System.out.println("シートの取得に失敗した可能性があります。(A)");
                //sheet = SpreadSheet.createFromFile(odsfile).getSheet(0);
                System.exit(1);
            } catch (IOException ex) {
                ex.printStackTrace();
                System.exit(1);
            }
            int Count_of_Columns = 0;
            try {
                Count_of_Columns = sheet.getColumnCount();
                System.out.println(Count_of_Columns);
            } catch (NullPointerException nex) {
                System.out.println("指定したシートが存在しないためにシートの取得に失敗した可能性があります。(B)");
                System.out.println("新しいシート" + sheetname + "を加えます。");
                try {
                    sheet = SpreadSheet.createFromFile(odsfile).getSheet(0).getODDocument().addSheet(sheetname);
                    sheet.setColumnCount(title_strings.length);
                    sheet.setRowCount(1);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.exit(1);
                }
                for (int col = 0; col < title_strings.length; col++) {
                    sheet.setValueAt(title_strings[col], col, 0);
                }
            }
            String[] existing_column_titles = new String[Count_of_Columns];
            for (int i = 0; i < Count_of_Columns - 1; i++) {
                existing_column_titles[i] = sheet.getValueAt(i, 0).toString();
            }
            for (int i = 0; i < Count_of_Columns - 1; i++) {
                System.out.println("title_strings[" + i + "]:" + title_strings[i]);
                System.out.println("existing_column_titles[" + i + "]:" + existing_column_titles[i]);
                if (title_strings[i].equals(existing_column_titles[i])) {
                    System.out.println(title_strings[i] + "...OK");
                } else {
                    System.out.println("見出し行の文字列が一致しません。書き出しを中止します。");
                    System.exit(1);
                }
            }

            /**
             * データを追記してゆく
             */
            int last_row_number = 0;
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                try {
                    if (sheet.getValueAt(0, i).toString().equals("")) {
                        last_row_number = i - 1;
                        break;
                    }
                } catch (IndexOutOfBoundsException ioobe) {
                    System.out.println("最終行を越えて値を取り出そうとした可能性あり");
                    last_row_number = i - 1;
                    break;
                }
            }
            System.out.println("last_row_number: " + last_row_number);
            for (int data_row = 0; data_row < data.length; data_row++) {
                System.out.println((last_row_number + data_row) + "から" + 1 + "行を" + 1 + "回複製する。");
                sheet.duplicateRows(last_row_number + data_row, 1, 1);

                for (int data_col = 0; data_col < data[0].length; data_col++) {
                    System.out.print("data[" + data_row + "][" + data_col + "]:" + data[data_row][data_col] + "を");
                    System.out.println(" 列" + data_col + ", 行" + (last_row_number + data_row) + " へ書き込み");
                    sheet.setValueAt(data[data_row][data_col], data_col, last_row_number + data_row + 1);
                }

            }

            //上書き保存
            try {
                sheet.getODDocument().saveAs(odsfile);
                System.out.println(odsfile.getAbsolutePath() + "へ書き出しました。");
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.exit(1);
            }
        }
    }

    /**
     * TreeMapを既存ODSファイルへ追記する。
     * 対象ODSファイルがない場合は新しいファイルを作成する。
     * 対象シートがない場合は新しいシートを作成する。
     *
     * @param tm
     * @param odsfile
     */
    public static void addToOdsfile(HashMap<String, String> tm, File odsfile, String sheetname) {
        //出力先ファイルの存在チェック
        if (!odsfile.exists()) {
            System.out.println(odsfile.getName() + "が存在しません");
            //System.exit(1);
            System.out.println("新規作成");
            outputToOdsfile(tm, odsfile, sheetname);
        } else {
            System.out.println(odsfile.getName() + "は存在します。");

            //引数のTreeMapをObject型の二次元配列へ変換する
            String[][] data = null;
            String[] title_strings = null;
            Iterator it = tm.keySet().iterator();
            Object key;
            String value;
            String[] values;
            int row = 0;
            while (it.hasNext()) {
                key = it.next();
                value = (String) tm.get(key);
                values = value.split("\t");
                if (row == 0) {//1行目は列タイトルとして処理
                    title_strings = new String[values.length + 1];//keyが1列目に格納される
                    title_strings[0] = (String) (key);
                    for (int i = 0; i < values.length; i++) {
                        title_strings[i + 1] = values[i];
                    }
                    data = new String[tm.size() - 1][values.length + 1];

                } else {//2行目以降はデータとして処理
                    data[row - 1][0] = (String) (key);
                    for (int i = 0; i < values.length; i++) {
                        data[row - 1][i + 1] = values[i];
                    }

                }
                row++;
            }


            //既存のファイルの既存のシートの１行目を取得し、タイトル行として妥当かどうか検証
            Sheet sheet = null;
            try {
                sheet = SpreadSheet.createFromFile(odsfile).getSheet(sheetname);
            } catch (NullPointerException nex) {
                System.out.println("シートの取得に失敗した可能性があります。(A)");
                //sheet = SpreadSheet.createFromFile(odsfile).getSheet(0);
                System.exit(1);
            } catch (IOException ex) {
                ex.printStackTrace();
                System.exit(1);
            }
            int Count_of_Columns = 0;
            try {
                Count_of_Columns = sheet.getColumnCount();
                System.out.println(Count_of_Columns);
            } catch (NullPointerException nex) {
                System.out.println("指定したシートが存在しないためにシートの取得に失敗した可能性があります。(B)");
                System.out.println("新しいシート" + sheetname + "を加えます。");
                try {
                    sheet = SpreadSheet.createFromFile(odsfile).getSheet(0).getODDocument().addSheet(sheetname);
                    sheet.setColumnCount(title_strings.length);
                    sheet.setRowCount(1);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.exit(1);
                }
                for (int col = 0; col < title_strings.length; col++) {
                    sheet.setValueAt(title_strings[col], col, 0);
                }
            }
            String[] existing_column_titles = new String[Count_of_Columns];
            for (int i = 0; i < Count_of_Columns - 1; i++) {
                existing_column_titles[i] = sheet.getValueAt(i, 0).toString();
            }
            for (int i = 0; i < Count_of_Columns - 1; i++) {
                System.out.println("title_strings[" + i + "]:" + title_strings[i]);
                System.out.println("existing_column_titles[" + i + "]:" + existing_column_titles[i]);
                if (title_strings[i].equals(existing_column_titles[i])) {
                    System.out.println(title_strings[i] + "...OK");
                } else {
                    System.out.println("見出し行の文字列が一致しません。書き出しを中止します。");
                    System.exit(1);
                }
            }

            /**
             * データを追記してゆく
             */
            int last_row_number = 0;
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                try {
                    if (sheet.getValueAt(0, i).toString().equals("")) {
                        last_row_number = i - 1;
                        break;
                    }
                } catch (IndexOutOfBoundsException ioobe) {
                    System.out.println("最終行を越えて値を取り出そうとした可能性あり");
                    last_row_number = i - 1;
                    break;
                }
            }
            System.out.println("last_row_number: " + last_row_number);
            for (int data_row = 0; data_row < data.length; data_row++) {
                System.out.println((last_row_number + data_row) + "から" + 1 + "行を" + 1 + "回複製する。");
                sheet.duplicateRows(last_row_number + data_row, 1, 1);

                for (int data_col = 0; data_col < data[0].length; data_col++) {
                    System.out.print("data[" + data_row + "][" + data_col + "]:" + data[data_row][data_col] + "を");
                    System.out.println(" 列" + data_col + ", 行" + (last_row_number + data_row) + " へ書き込み");

                    try {
                        sheet.setValueAt(Double.valueOf(data[data_row][data_col]), data_col, last_row_number + data_row + 1);
                    }catch(NumberFormatException nfe){
                        sheet.setValueAt(data[data_row][data_col], data_col, last_row_number + data_row + 1);
                    }
                }

            }

            //上書き保存
            try {
                sheet.getODDocument().saveAs(odsfile);
                System.out.println(odsfile.getAbsolutePath() + "へ書き出しました。");
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.exit(1);
            }
        }
    }

    /**
     * ArrayList→テキストファイル
     */
    public static boolean outputToTextfile(ArrayList al, File textfile) {
        textfile.getParentFile().mkdirs();
        try {
            textfile.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(CollectionWriterAndReader_ver4.class.getName()).log(Level.SEVERE, null, ex);
        }

        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new FileWriter(textfile));

            String line;
            for (Object obj : al) {
                bw.write(obj.toString() + "\n");
            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return false;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                return false;
            }
        }
        System.out.println(textfile.getAbsolutePath() + "へ書き出しました。");
        return true;
    }

    /**
     * Object型配列→テキストファイル
     */
    public static boolean outputToTextfile(Object[] values, File textfile) {
        textfile.getParentFile().mkdirs();
        try {
            textfile.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(CollectionWriterAndReader_ver4.class.getName()).log(Level.SEVERE, null, ex);
        }

        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new FileWriter(textfile));

            String line;
            for (Object obj : values) {
                bw.write(obj.toString() + "\n");
            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return false;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                return false;
            }
        }
        return true;
    }


    /**
     * テキストファイル→TreeMap。テキストファイル内の各行を
     * 各要素として保持するTreeMapを作るメソッド。
     * Keyは１つ目のタブで区切られた左側の文字列、Valueは右側の文字列。
     *
     * @param file
     */
    public static TreeMap loadToTreeMap(File file) {
        TreeMap<String, String> tm = new TreeMap<String, String>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            String[] splitedline;
            String key_str = "";
            String value_str = "";

            while ((line = br.readLine()) != null) {
                splitedline = line.split("\t");
                key_str = splitedline[0];
                for (int i = 1; i < splitedline.length; i++) {
                    value_str = value_str + splitedline[i] + "\t";
                }
                value_str = value_str.substring(0, value_str.length() - 1);
                tm.put(key_str, value_str);
                value_str = "";
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
        return tm;
    }

    /**
     * テキストファイル→ArrayList。テキストファイル内の各行を各要素として保持するArrayListを作るメソッド。
     */
    public static ArrayList<String> loadToArrayList(File textfile) {
        ArrayList<String> al = new ArrayList();

        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(textfile));

            String line;
            while ((line = br.readLine()) != null) {
                al.add(line);
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            System.exit(1);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
            }
        }
        return al;
    }

/**
     * テキストファイル→Double[]。テキストファイル内の各行を各要素として保持するArrayListを作るメソッド。
     */
    public static Double[] loadToDoubleArray(File textfile) {
        ArrayList<String> values =  loadToArrayList(textfile);
        Double[] result_double = new Double[values.size()];
        for(int i=0; i<values.size();i++){
            result_double[i] = Double.parseDouble(values.get(i));
        }
        return result_double;
    }

    /**
     * テキストファイル→String。テキストファイル内の各行を半角スペース区切りで結合して一つの文字列として返すメソッド。
     */
    public static String loadToString(File textfile) {
        StringBuffer str_buf = new StringBuffer();

        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(textfile));

            String line;
            while ((line = br.readLine()) != null) {
                str_buf.append(line + " ");
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            System.exit(1);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
            }
        }

        return str_buf.toString().substring(0, str_buf.toString().lastIndexOf(" ") );
    }


}
