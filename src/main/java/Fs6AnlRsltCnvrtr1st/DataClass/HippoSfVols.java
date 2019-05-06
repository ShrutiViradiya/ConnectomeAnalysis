package Fs6AnlRsltCnvrtr1st.DataClass;

import java.io.*;
import java.util.HashMap;

/**
 * aseg.stasファイルに対応したクラス
 * Created by issey on 2016/11/15.
 */
public class HippoSfVols {

    final String HippoSfVolsFileName;
    final String SubjectName;

    HashMap<String, SubFieldInfo> SubFieldInfoMap = new HashMap();

    public HippoSfVols(String hippo_sf_vols_file_path) {
        hippo_sf_vols_file_path = hippo_sf_vols_file_path.replaceAll("\\\\", "/");
        HippoSfVolsFileName = hippo_sf_vols_file_path.substring(hippo_sf_vols_file_path.lastIndexOf("/") + 1);
        SubjectName = HippoSfVolsFileName.substring(0, HippoSfVolsFileName.indexOf("_"));
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(hippo_sf_vols_file_path), "UTF-8"));

            String line;
            String[] line_words;

            String SubFieldName;
            double Volume;
            SubFieldInfo subFieldInfo;
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                line_words = line.split("\\s+");
                //for (String line_word : line_words) {
                //System.out.println(line_word);
                //}
                SubFieldName = line_words[0];
                Volume = Double.parseDouble(line_words[1]);

                subFieldInfo = new SubFieldInfo(SubFieldName, Volume);
                SubFieldInfoMap.put(SubFieldName, subFieldInfo);
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
    }

    public HashMap<String, SubFieldInfo> getSubFieldInfoMap() {
        return SubFieldInfoMap;
    }

    public String getFileName() {
        return HippoSfVolsFileName;
    }

    public String getSubjectName() {
        return SubjectName;
    }

    class SubFieldInfo {
        String SubFieldName;
        double Volume;

        public SubFieldInfo(String sub_field_name, double volume) {
            this.SubFieldName = sub_field_name;
            this.Volume = volume;
        }

        public String getSubFieldName() {
            return SubFieldName;
        }

        public double getVolume() {
            return Volume;
        }
    }
}
