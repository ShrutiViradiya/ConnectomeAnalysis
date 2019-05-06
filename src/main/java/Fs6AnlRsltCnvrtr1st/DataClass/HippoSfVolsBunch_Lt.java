package Fs6AnlRsltCnvrtr1st.DataClass;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by issey on 2016/11/15.
 */
public class HippoSfVolsBunch_Lt extends Bunch {

    public static void main(String[] args) {
        //String folder_path_str = "file:/C:/Users/issey/Documents/Dropbox/docroot/VolConSample";
        String folder_path_str = "file:/C:/Users/issey/Documents/Dropbox/docroot/20161124_FS6_ANL_RSLT";

        //コンストラクタ
        HippoSfVolsBunch_Lt bunch = new HippoSfVolsBunch_Lt(folder_path_str);

        System.out.println(bunch.getListOf_SubjectName());
        System.out.println(bunch.getListOf_HATA_Vol());
        System.out.println();

        for (HippoSfVols vols : bunch.getDataListOf_RtHippoSfVols()) {
            System.out.print(vols.getSubjectName() + " ");
        }
    }

    //読み取り対象ファイル絞込用フィルタに使う文字列
    static String FilteringString = "lh.hippoSfVolumes-T1.v10.txt";

    ArrayList<HippoSfVols> DataListOf_RtHippoSfVols = new ArrayList();//HippoSfVolsの束

    ArrayList<String> ListOf_SubjectName = new ArrayList();

    ArrayList<Double> ListOf_HippocampalTail_Vol = new ArrayList();
    ArrayList<Double> ListOf_Subiculum_Vol = new ArrayList();
    ArrayList<Double> ListOf_CA1_Vol = new ArrayList();
    ArrayList<Double> ListOf_HippocampalFissure_Vol = new ArrayList();
    ArrayList<Double> ListOf_Presubiculum_Vol = new ArrayList();
    ArrayList<Double> ListOf_Parasubiculum_Vol = new ArrayList();
    ArrayList<Double> ListOf_MolecularLayerHP_Vol = new ArrayList();
    ArrayList<Double> ListOf_GC_ML_DG_Vol = new ArrayList();
    ArrayList<Double> ListOf_CA3_Vol = new ArrayList();
    ArrayList<Double> ListOf_CA4_Vol = new ArrayList();
    ArrayList<Double> ListOf_Fimbria_Vol = new ArrayList();
    ArrayList<Double> ListOf_HATA_Vol = new ArrayList();
    ArrayList<Double> ListOf_WholeHippocampus_Vol = new ArrayList();

    public HippoSfVolsBunch_Lt(String hippo_sf_vols_folder_path) {
        //スーパークラスのコンストラクト
        super(hippo_sf_vols_folder_path, FilteringString);//これによりReadTargetFilesにデータが格納される

        System.out.println("読み取り対象ファイル:");
        for (File file : ReadTargetFiles) {
            System.out.println("    " + file.getAbsolutePath());
            DataListOf_RtHippoSfVols.add(new HippoSfVols(file.getAbsolutePath()));
        }
        System.out.println("    「*" + FilteringString + "」ファイル数: " + ReadTargetFiles.length);
        System.out.println();

        /**
         * 症例名、各脳領域の体積値のArrayListへの格納
         */
        //aseg_statsファイルの束
        HashMap<String, HippoSfVols.SubFieldInfo> sf_info_map;
        Iterator it_for_sf_info_map_key;
        String subfield_name;
        HippoSfVols.SubFieldInfo a_subfield_info;

        //全右側ファイル束の要素　および　全てのsubfield情報の束の要素　について、値を取り出す。
        for (HippoSfVols data : DataListOf_RtHippoSfVols) {
            System.out.println("Now reading " + data.getFileName());

            //各Subjectがどのような順番で呼び出され処理されているかを記録するためSunjectListを作る
            ListOf_SubjectName.add(data.getSubjectName());

            //各亜領域情報の束を取り出す
            sf_info_map = data.getSubFieldInfoMap();
            it_for_sf_info_map_key = sf_info_map.keySet().iterator();
            while (it_for_sf_info_map_key.hasNext()) {
                subfield_name = (String) it_for_sf_info_map_key.next();
                a_subfield_info = sf_info_map.get(subfield_name);

                if (subfield_name.equals("Hippocampal_tail")) {
                    ListOf_HippocampalTail_Vol.add(a_subfield_info.getVolume());
                } else if (subfield_name.equals("subiculum")) {
                    ListOf_Subiculum_Vol.add(a_subfield_info.getVolume());
                } else if (subfield_name.equals("CA1")) {
                    ListOf_CA1_Vol.add(a_subfield_info.getVolume());
                } else if (subfield_name.equals("hippocampal-fissure")) {
                    ListOf_HippocampalFissure_Vol.add(a_subfield_info.getVolume());
                } else if (subfield_name.equals("presubiculum")) {
                    ListOf_Presubiculum_Vol.add(a_subfield_info.getVolume());
                } else if (subfield_name.equals("parasubiculum")) {
                    ListOf_Parasubiculum_Vol.add(a_subfield_info.getVolume());
                } else if (subfield_name.equals("molecular_layer_HP")) {
                    ListOf_MolecularLayerHP_Vol.add(a_subfield_info.getVolume());
                } else if (subfield_name.equals("GC-ML-DG")) {
                    ListOf_GC_ML_DG_Vol.add(a_subfield_info.getVolume());
                } else if (subfield_name.equals("CA3")) {
                    ListOf_CA3_Vol.add(a_subfield_info.getVolume());
                } else if (subfield_name.equals("CA4")) {
                    ListOf_CA4_Vol.add(a_subfield_info.getVolume());
                } else if (subfield_name.equals("fimbria")) {
                    ListOf_Fimbria_Vol.add(a_subfield_info.getVolume());
                } else if (subfield_name.equals("HATA")) {
                    ListOf_HATA_Vol.add(a_subfield_info.getVolume());
                } else if (subfield_name.equals("Whole_hippocampus")) {
                    ListOf_WholeHippocampus_Vol.add(a_subfield_info.getVolume());
                } else {
                    System.err.println("    " + subfield_name + "は未定義です。");
                }
            }
        }

        System.out.println();
    }

    /* ---------------------------------------------------- */

    public ArrayList<Double> getListOf_CA1_Vol() {
        return ListOf_CA1_Vol;
    }

    public ArrayList<Double> getListOf_CA3_Vol() {
        return ListOf_CA3_Vol;
    }

    public ArrayList<Double> getListOf_CA4_Vol() {
        return ListOf_CA4_Vol;
    }

    public ArrayList<Double> getListOf_Fimbria_Vol() {
        return ListOf_Fimbria_Vol;
    }

    public ArrayList<Double> getListOf_GC_ML_DG_Vol() {
        return ListOf_GC_ML_DG_Vol;
    }

    public ArrayList<Double> getListOf_HATA_Vol() {
        return ListOf_HATA_Vol;
    }

    public ArrayList<Double> getListOf_HippocampalFissure_Vol() {
        return ListOf_HippocampalFissure_Vol;
    }

    public ArrayList<Double> getListOf_HippocampalTail_Vol() {
        return ListOf_HippocampalTail_Vol;
    }

    public ArrayList<Double> getListOf_MolecularLayerHP_Vol() {
        return ListOf_MolecularLayerHP_Vol;
    }

    public ArrayList<Double> getListOf_Parasubiculum_Vol() {
        return ListOf_Parasubiculum_Vol;
    }

    public ArrayList<Double> getListOf_Presubiculum_Vol() {
        return ListOf_Presubiculum_Vol;
    }

    public ArrayList<Double> getListOf_Subiculum_Vol() {
        return ListOf_Subiculum_Vol;
    }

    public ArrayList<Double> getListOf_WholeHippocampus_Vol() {
        return ListOf_WholeHippocampus_Vol;
    }

    /* --------------------------------------------------------- */

    public ArrayList<HippoSfVols> getDataListOf_RtHippoSfVols() {
        return DataListOf_RtHippoSfVols;
    }

    public ArrayList<String> getListOf_SubjectName() {
        return ListOf_SubjectName;
    }
}

