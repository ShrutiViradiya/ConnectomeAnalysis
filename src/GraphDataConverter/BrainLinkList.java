package GraphDataConverter;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by issey on 2016/09/27.
 */
public class BrainLinkList {

    static final String BRAIN_LINK_INFO_FILE = "./output_files/edge_list.txt";

    ArrayList<TreeMap<String, Object>> LinkList = null;


    /**
     * コンストラクタ
     */
    public BrainLinkList() {
        loadBrainAreaInfo(BRAIN_LINK_INFO_FILE, "\\s");
    }

    private void loadBrainAreaInfo(String BrainLinkInfoFilePath, String Separator) {
        LinkList = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(new File(BrainLinkInfoFilePath)));
            String line = "";
            int line_number = 0;
            String[] first_line_elements = null;
            String[] other_line_elements = null;
            while ((line = br.readLine()) != null) {
                line_number++;
                if (line_number == 1) {
                    first_line_elements = line.split(Separator);
                } else {
                    other_line_elements = line.split(Separator);
                    TreeMap hm = new TreeMap<>();
                    for (int i = 0; i < first_line_elements.length; i++) {
                        hm.put(first_line_elements[i], other_line_elements[i]);
                    }

                    LinkList.add(hm);
                }
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

    /**
     * Getter, Setter
     * @return
     */
    //public TreeMap<String, Object> getFrom(int AreaNumber) {
    //    return BrainAreaInfoList.get(AreaNumber);
    //}

    public int getSize() {
        return LinkList.size();
    }

    public String[] getColumnNames() {
        Set keys = LinkList.get(0).keySet();
        String[] ColumnNames = new String[keys.size()];
        Iterator it = keys.iterator();
        String ColName = "";
        int index = 0;
        while (it.hasNext()) {
            ColName = (String) it.next();
            ColumnNames[index] = ColName;
            index++;
        }
        return ColumnNames;
    }

    public void showAllCollName() {
        String[] ColNames = getColumnNames();
        System.out.println("----This BrainAreaList Contains ...----");
        for (String ColName : ColNames) {
            System.out.print(ColName + " ");
        }
        System.out.println();
        System.out.println("---------------------------------------");
    }

    public static void main(String[] args) {
        BrainLinkList bal = new BrainLinkList();
        System.out.println(bal.getSize());
        bal.showAllCollName();
        //System.out.println(bal.getArea(4).get("AreaNameJp"));
    }


}
