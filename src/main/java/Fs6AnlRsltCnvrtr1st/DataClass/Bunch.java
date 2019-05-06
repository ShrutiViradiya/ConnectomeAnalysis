package Fs6AnlRsltCnvrtr1st.DataClass;

import javax.swing.*;
import java.io.File;
import java.io.FilenameFilter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by issey on 2016/11/27.
 */
public class Bunch {

    boolean debug = false;
    String FilteringString;
    //ArrayList DataList = new ArrayList();
    File[] ReadTargetFiles;

    //ファイル名一覧
    ArrayList<String> ListOf_SubjectName = new ArrayList();

    public Bunch(String read_target_folder_path_str, String filtering_string) {
        read_target_folder_path_str = read_target_folder_path_str.replaceAll("\\\\", "/");
        FilteringString = filtering_string;

        URI read_target_folder_path = null;
        try {
            read_target_folder_path = new URI(read_target_folder_path_str);
        } catch (URISyntaxException urise) {
            JOptionPane.showMessageDialog(null, urise.getMessage());
        }

        //aseg、aparcファイルが格納されているフォルダ
        File WorkingDirectory = new File(read_target_folder_path.getPath());
        System.out.println("読み取り対象フォルダ:");
        System.out.println("    " + read_target_folder_path.getPath());
        System.out.println();


        //lh.aparc.DKTatlas.statsファイルフィルタ
        FilenameFilter file_filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.endsWith(FilteringString)) {
                    return true;
                } else {
                    return false;
                }
            }
        };

        ReadTargetFiles = WorkingDirectory.listFiles(file_filter);

    }

    /* -------------------------------------------------------- */

    public ArrayList<String> getListOf_SubjectName() {
        return ListOf_SubjectName;
    }

}
