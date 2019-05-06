/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Fs6AnlRsltCnvrtr.Util;


import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author issey
 */
public class CollectionWriterAndReader_ver7 {
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

        //addToOdsfile(sample_treemap, new File("C:\\Temp\\test.ods"), "newsheet");

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
        CollectionWriterAndReader_ver7 tmo = new CollectionWriterAndReader_ver7();
        TreeMap tm = loadToTreeMap(new File("C:\\Temp\\test.txt"));

        Iterator it = tm.keySet().iterator();
        while (it.hasNext()) {
            Object key = it.next();
            System.out.println((String) key + ":" + (String) tm.get(key));
        }

    }

    /**
     * TreeMap→テキストファイル
     *
     * @param file
     */
    public static void outputToTextfile(TreeMap<String, String> tm, File file) {
        file.getParentFile().mkdirs();
        try {
            file.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(CollectionWriterAndReader_ver7.class.getName()).log(Level.SEVERE, null, ex);
        }

        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.getAbsolutePath()), "UTF-8"));

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
     * ArrayList→テキストファイル
     */
    public static boolean outputToTextfile(ArrayList al, File textfile) {
        textfile.getParentFile().mkdirs();
        try {
            textfile.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(CollectionWriterAndReader_ver7.class.getName()).log(Level.SEVERE, null, ex);
        }

        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(textfile.getAbsolutePath()), "UTF-8"));

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
     * HashSet→テキストファイル
     *
     * @param file
     */
    public static void outputToTextfile(HashSet<String> hs, File file) {
        file.getParentFile().mkdirs();
        try {
            file.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(CollectionWriterAndReader_ver7.class.getName()).log(Level.SEVERE, null, ex);
        }

        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.getAbsolutePath()), "UTF-8"));

            Iterator it = hs.iterator();
            Object obj;
            while (it.hasNext()) {
                obj = it.next();
                bw.write((String) obj + "\n");
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
     * HashSet→テキストファイル
     *
     * @param file
     */
    public static void outputToTextfile(HashMap<String, String> hm, File file) {
        file.getParentFile().mkdirs();
        try {
            file.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(CollectionWriterAndReader_ver7.class.getName()).log(Level.SEVERE, null, ex);
        }

        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.getAbsolutePath()), "UTF-8"));

            Iterator it = hm.keySet().iterator();
            Object obj;
            while (it.hasNext()) {
                obj = it.next();
                bw.write((String) obj +"\t" + hm.get(obj) +"\n");
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
     * Object型配列→テキストファイル
     */
    public static boolean outputToTextfile(Object[] values, File textfile) {
        textfile.getParentFile().mkdirs();
        try {
            textfile.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(CollectionWriterAndReader_ver7.class.getName()).log(Level.SEVERE, null, ex);
        }

        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(textfile.getAbsolutePath()), "UTF-8"));

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
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file.getAbsolutePath()), "UTF-8"));

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
            br = new BufferedReader(new InputStreamReader(new FileInputStream(textfile.getAbsolutePath()), "UTF-8"));

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
        ArrayList<String> values = loadToArrayList(textfile);
        Double[] result_double = new Double[values.size()];
        for (int i = 0; i < values.size(); i++) {
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
            br = new BufferedReader(new InputStreamReader(new FileInputStream(textfile.getAbsolutePath()), "UTF-8"));

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

        return str_buf.toString().substring(0, str_buf.toString().lastIndexOf(" "));
    }


}
