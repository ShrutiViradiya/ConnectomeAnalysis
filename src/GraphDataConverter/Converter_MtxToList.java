package GraphDataConverter;

import BrainMapper_ver4.elements.GraphEdge;
import BrainMapper_ver4.elements.GraphEdgeList;
import BrainMapper_ver4.elements.GraphNode;
import BrainMapper_ver4.elements.GraphNodeList;
import basic_tools.CollectionWriterAndReader;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by issey on 2016/09/24.
 */
public class Converter_MtxToList {


    static GraphEdgeList graphEdgeList = new GraphEdgeList();
    static GraphNodeList graphNodeList = new GraphNodeList();

    public static void main(String[] agrs) {
        String file = "C:/Users/issey/Documents/Dropbox/docroot/pipeline4brain_net_orig/connectome_graphml/c01_connectome_with_thresholding_kopt_3061.mtx";
        Converter_MtxToList list_maker = new Converter_MtxToList(file);
        list_maker.writeOut("./output_files/");
    }

    /**
     * コンストラクタ
     *
     * @param adjacent_matrix_file_path
     */
    public Converter_MtxToList(String adjacent_matrix_file_path) {
        System.out.println("Converter_MtxToList");


        String[] node_names = null;
        ArrayList<ArrayList<String>> edge_mtx = new ArrayList<>();

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(new File(adjacent_matrix_file_path)));

            String line;
            int line_num = 0;
            while ((line = br.readLine()) != null) {
                line_num++;
                if (line_num == 1) {
                    //一行目にはノード名が入っている。
                    node_names = line.split("\\s");
                } else {
                    //２行目以降にはweightが入っている。ただし１列目の部分にはノード名が入っている。
                    ArrayList<String> values_list = new ArrayList(Arrays.asList(line.split("\\s")));
                    values_list.remove(0);
                    edge_mtx.add(values_list);
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

        //
        // GraphNodeListを作る
        // Node ID は "1", "2", "3", "4", ...
        // Node ID は "ctx-lh-superiorfrontal", "ctx-rh-isthmuscingulate", …などのLongID
        //
        for (int i = 0; i < node_names.length; i++) {
            //graphNodeList.add(new GraphNode(String.format("%03d", i), node_names[i]));
            //graphNodeList.add(new GraphNode(Integer.toString(i + 1), node_names[i]));
            graphNodeList.add(new GraphNode(node_names[i], Integer.toString(i + 1)));
        }

        //
        // EdgeListを作る
        // Edge
        //
        GraphEdge edge_tmp = null;
        for (int i = 0; i < edge_mtx.size(); i++) {
            for (int j = 0; j < edge_mtx.get(i).size(); j++) {
                System.out.println(" i, j =" + i + "," + j);
                //edge_tmp = new GraphEdge(graphNodeList.getNodeByID(Integer.toString(i + 1)), graphNodeList.getNodeByID(Integer.toString(j + 1)));
                edge_tmp = new GraphEdge(graphNodeList.getNodeByID(node_names[i]), graphNodeList.getNodeByID(node_names[j]));
                edge_tmp.setWeight(Double.parseDouble(edge_mtx.get(i).get(j)));
                edge_tmp.setElementID(node_names[i] + " ---> " + node_names[j]);
                graphEdgeList.add(edge_tmp);
            }
        }

    }


    public GraphEdgeList getGraphEdgeList() {
        return graphEdgeList;

    }

    public GraphNodeList getGraphNodeList() {
        return graphNodeList;
    }


    static void writeOut(String output_folder_path) {
        //
        // 書き出し
        //

        ArrayList<String> strlist = new ArrayList<>();

        // Nodeリスト
        for (GraphNode node : graphNodeList) {
            strlist.add(node.getElementID() + " " + node.getTextarea().getText());
        }
        CollectionWriterAndReader.outputToTextfile(strlist, new File(output_folder_path + "node_list.txt"));

        // Edgeリスト
        strlist = new ArrayList<>();
        Double weight = 0.0;
        for (GraphEdge edge : graphEdgeList) {
            weight = edge.getWeight();
            if (weight > 0.0) {
                strlist.add(edge.getElementID() + " " + edge.getSrcNode().getElementID() + " " + edge.getDestNode().getElementID() + " " + weight);
            }
        }
        CollectionWriterAndReader.outputToTextfile(strlist, new File(output_folder_path + "edge_list.txt"));


    }

}


