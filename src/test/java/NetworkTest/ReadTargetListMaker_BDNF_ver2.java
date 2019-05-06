/**
 * http://www.jopendocument.org/
 * http://www.jopendocument.org/docs/index.html
 * http://www.jopendocument.org/start.html
 * <p>
 */
package NetworkTest;

import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * 本研究のサンプルデータの格納されたODSファイルから条件に合うデータを取り出すためのクラス。
 * jOpenDocumentというクラスライブラリ（http://www.jopendocument.org/start_spreadsheet_2.html）を使っている。
 * <p>
 * @author issey
 */
public class ReadTargetListMaker_BDNF_ver2 {

    //private static final String ods_file_path = "C:\\Users\\issey\\Documents\\Dropbox\\NetbeansProjects_xps14z\\About_ConnectomeAnalysis\\ods_files\\DataOfSamples.ods";
    private static final String ods_file_path = "ods_files/DataOfSamples.ods";

    //private static final String read_target_file_path_head = "C:\\Users\\issey\\Documents\\Dropbox\\NetbeansProjects_xps14z\\About_ConnectomeAnalysis\\20150831_CM_ResultData\\connectome_freesurferaparc\\";
    private static final String read_target_file_path_head = "C:\\Users\\issey\\Documents\\Dropbox\\NetbeansProjects_xps14z\\About_ConnectomeAnalysis\\input_files\\20150831_CM_ResultData\\connectome_freesurferaparc\\";
    private static final String read_target_file_path_foot = "_connectome.graphml";
    
    protected static ArrayList<String> watanabeIDs = new ArrayList<>();//「col = 1」：watanabeID
    protected static ArrayList<String> binarizedCOMTs = new ArrayList<>();//「col = 14」
    protected static ArrayList<String> binarizedBDNFs = new ArrayList<>();//「col = 10」
    protected static ArrayList<String> binarizedT182Cs = new ArrayList<>();//「col = 18」
    protected static ArrayList<String> binarizedG1287As = new ArrayList<>();//「col = 22」
    protected static ArrayList<String> NMLs = new ArrayList<>();//「col = 5」：NML
    protected static ArrayList<String> MDDs = new ArrayList<>(); //「col = 6」：MDD
    protected static ArrayList<String> SEXs = new ArrayList<>(); //「col = 4」：SEX
    protected static ArrayList<String> AGEs = new ArrayList<>();//「col = 3」：AGE
    protected static ArrayList<String> Graphml_Availabilities = new ArrayList<>();//col = 7, Graphmlファイルが存在するか否か
    protected static ArrayList<String> COMT_Availabilities = new ArrayList<>();//col=13, COMTデータが使えるか否か
    protected static ArrayList<String> BDNF_Availabilities = new ArrayList<>();//col=9, BDNFデータが使えるか否か
    protected static ArrayList<String> T182C_Availabilities = new ArrayList<>();//col=17, T182Cデータが使えるか否か
    protected static ArrayList<String> G1287A_Availabilities = new ArrayList<>();//col=21, G1287Aデータが使えるか否か

    private static ArrayList<String> BDNF_relating_Graphml_List_ALL;
    private static ArrayList<String> BDNF_relating_Graphml_List_ALLwithGG;
    private static ArrayList<String> BDNF_relating_Graphml_List_ALLwithGAorAA;
    private static ArrayList<String> BDNF_relating_Graphml_List_NMLandMDD;
    private static ArrayList<String> BDNF_relating_Graphml_List_NML;
    private static ArrayList<String> BDNF_relating_Graphml_List_MDD;
    private static ArrayList<String> BDNF_relating_Graphml_List_MDDwithGG;
    private static ArrayList<String> BDNF_relating_Graphml_List_MDDwithGAorAA;
    private static ArrayList<String> BDNF_relating_Graphml_List_NMLwithGG;
    private static ArrayList<String> BDNF_relating_Graphml_List_NMLwithGAorAA;

    private String head_str;//watanabeIDの前方に結合する文字列
    private String foot_str;//watanabeIDの後方に結合する文字列

    /**
     * コントラクタ
     */
    public ReadTargetListMaker_BDNF_ver2() {
        this(read_target_file_path_head, read_target_file_path_foot);
    }

    /**
     * 【例】<br>
     * ◆head_str :
     * "C:\\Users\\issey\\Documents\\Dropbox\\NetbeansProjects_xps14z\\About_ConnectomeAnalysis\\20150831_CM_ResultData\\connectome_freesurferaparc\\"<br>
     * ◆foot_str : "_connectome.graphml"<br>
     * <p>
     * @param head_str
     * @param foot_str
     */
    public ReadTargetListMaker_BDNF_ver2(String head_str, String foot_str) {
        this.head_str = head_str;
        this.foot_str = foot_str;

        //Load the sheet
        Sheet sheet = null;
        try {
            sheet = SpreadSheet.createFromFile(new File(ods_file_path)).getSheet(0);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //ODSファイルの内容のうち必要なものをロードする
        for (int Row = 3; Row < 1000; Row++) {
            //基準となる列
            watanabeIDs.add(sheet.getCellAt(1, Row).getTextValue());
            //
            binarizedCOMTs.add(sheet.getCellAt(14, Row).getTextValue());//GG=1, GAorAA=2
            binarizedBDNFs.add(sheet.getCellAt(10, Row).getTextValue());//GG=1, GAorAA=2;
            binarizedT182Cs.add(sheet.getCellAt(18, Row).getTextValue());//TT=1, CTorCC=2;
            binarizedG1287As.add(sheet.getCellAt(22, Row).getTextValue());//GG=1, GAorAA=2;
            NMLs.add(sheet.getCellAt(5, Row).getTextValue());
            MDDs.add(sheet.getCellAt(6, Row).getTextValue());
            SEXs.add(sheet.getCellAt(4, Row).getTextValue());
            AGEs.add(sheet.getCellAt(3, Row).getTextValue());
            Graphml_Availabilities.add(sheet.getCellAt(7, Row).getTextValue());
            COMT_Availabilities.add(sheet.getCellAt(13, Row).getTextValue());
            BDNF_Availabilities.add(sheet.getCellAt(9, Row).getTextValue());
            T182C_Availabilities.add(sheet.getCellAt(17, Row).getTextValue());
            G1287A_Availabilities.add(sheet.getCellAt(21, Row).getTextValue());
            //System.out.println("watanabeID: " + watanabeID);
            if (watanabeIDs.get(watanabeIDs.size() - 1).equals("")) {
                break;
            }
        }

        //格納先をリセット
        BDNF_relating_Graphml_List_ALL = new ArrayList<>();
        BDNF_relating_Graphml_List_ALLwithGG = new ArrayList<>();
        BDNF_relating_Graphml_List_ALLwithGAorAA = new ArrayList<>();
        BDNF_relating_Graphml_List_NMLandMDD = new ArrayList<>();
        BDNF_relating_Graphml_List_NML = new ArrayList<>();
        BDNF_relating_Graphml_List_MDD = new ArrayList<>();
        BDNF_relating_Graphml_List_MDDwithGG = new ArrayList<>();
        BDNF_relating_Graphml_List_MDDwithGAorAA = new ArrayList<>();
        BDNF_relating_Graphml_List_NMLwithGG = new ArrayList<>();
        BDNF_relating_Graphml_List_NMLwithGAorAA = new ArrayList<>();

        //遺伝子型でグループ分けする
        //String head_str = "C:\\Users\\issey\\Documents\\Dropbox\\NetbeansProjects_xps14z\\About_ConnectomeAnalysis\\20150831_CM_ResultData\\connectome_freesurferaparc\\";
        //String foot_str = "_connectome.graphml";
        for (int i = 0; i < watanabeIDs.size(); i++) {

            //Graphmlファイルが存在し、BDNF遺伝子情報があるもののうち
            if (Graphml_Availabilities.get(i).equals("1") && BDNF_Availabilities.get(i).equals("1")) {
                BDNF_relating_Graphml_List_NMLandMDD.add(head_str + watanabeIDs.get(i) + foot_str);
                BDNF_relating_Graphml_List_ALL.add(head_str + watanabeIDs.get(i) + foot_str);

                /**
                 * BDNF
                 */
                //BDNF遺伝子情報のあるNML群
                if (NMLs.get(i).equals("1")) {
                    //System.out.println("NML: " + watanabeIDs.get(i));
                    BDNF_relating_Graphml_List_NML.add(head_str + watanabeIDs.get(i) + foot_str);

                    if (binarizedBDNFs.get(i).equals("1")) {//GG=1, GAorAA=2;
                        BDNF_relating_Graphml_List_NMLwithGG.add(head_str + watanabeIDs.get(i) + foot_str);
                        BDNF_relating_Graphml_List_ALLwithGG.add(head_str + watanabeIDs.get(i) + foot_str);
                    }
                    if (binarizedBDNFs.get(i).equals("2")) {//GG=1, GAorAA=2;
                        BDNF_relating_Graphml_List_NMLwithGAorAA.add(head_str + watanabeIDs.get(i) + foot_str);
                        BDNF_relating_Graphml_List_ALLwithGAorAA.add(head_str + watanabeIDs.get(i) + foot_str);
                    }
                }

                //BDNF遺伝子情報のあるMDD群
                if (MDDs.get(i).equals("1")) {
                    BDNF_relating_Graphml_List_MDD.add(head_str + watanabeIDs.get(i) + foot_str);

                    if (binarizedBDNFs.get(i).equals("1")) {//GG=1, GAorAA=2;
                        BDNF_relating_Graphml_List_MDDwithGG.add(head_str + watanabeIDs.get(i) + foot_str);
                        BDNF_relating_Graphml_List_ALLwithGG.add(head_str + watanabeIDs.get(i) + foot_str);
                    }
                    if (binarizedBDNFs.get(i).equals("2")) {//GG=1, GAorAA=2;
                        BDNF_relating_Graphml_List_MDDwithGAorAA.add(head_str + watanabeIDs.get(i) + foot_str);
                        BDNF_relating_Graphml_List_ALLwithGAorAA.add(head_str + watanabeIDs.get(i) + foot_str);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {

        ReadTargetListMaker_BDNF_ver2 rtlm = new ReadTargetListMaker_BDNF_ver2();

        //テスト出力
        System.out.println("Array_of_MDDwithGG: ");
        for (String str : rtlm.getGraphmlfilePathes_of_MDDwithGG()) {
            System.out.println("    " + str);
        }
        System.out.println("");
        System.out.println("Array_of_MDDwithGAorAA: ");
        for (String str : rtlm.getGraphmlfilePathes_of_MDDwithGAorAA()) {
            System.out.println("    " + str);
        }
        System.out.println("");
        System.out.println("Array_of_NMLwithGG: ");
        for (String str : rtlm.getGraphmlfilePathes_of_NMLwithGG()) {
            System.out.println("    " + str);
        }
        System.out.println("");
        System.out.println("Array_of_NMLwithGAorAA: ");
        for (String str : rtlm.getGraphmlfilePathes_of_NMLwithGAorAA()) {
            System.out.println("    " + str);
        }
    }

    public ArrayList<String> getGraphmlfilePathes_of_NMLandMDD() {
        return BDNF_relating_Graphml_List_NMLandMDD;
    }

    public ArrayList<String> getGraphmlfilePathes_of_ALL() {
        return BDNF_relating_Graphml_List_ALL;
    }

    public ArrayList<String> getGraphmlfilePathes_of_ALLwithGG() {
        return BDNF_relating_Graphml_List_ALLwithGG;
    }

    public ArrayList<String> getGraphmlfilePathes_of_ALLwithGAorAA() {
        return BDNF_relating_Graphml_List_ALLwithGAorAA;
    }

    public ArrayList<String> getGraphmlfilePathes_of_NML() {
        return BDNF_relating_Graphml_List_NML;
    }

    public ArrayList<String> getGraphmlfilePathes_of_MDD() {
        return BDNF_relating_Graphml_List_MDD;
    }

    public ArrayList<String> getGraphmlfilePathes_of_MDDwithGG() {
        return BDNF_relating_Graphml_List_MDDwithGG;
    }

    public ArrayList<String> getGraphmlfilePathes_of_MDDwithGAorAA() {
        return BDNF_relating_Graphml_List_MDDwithGAorAA;
    }

    public ArrayList<String> getGraphmlfilePathes_of_NMLwithGG() {
        return BDNF_relating_Graphml_List_NMLwithGG;
    }

    public ArrayList<String> getGraphmlfilePathes_of_NMLwithGAorAA() {
        return BDNF_relating_Graphml_List_NMLwithGAorAA;
    }
}
