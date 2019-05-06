/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NetworkTest;

import cytoscape.CyNetwork;
import cytoscape.Cytoscape;
import cytoscape.randomnetwork.ErdosRenyiModel;
import cytoscape.randomnetwork.RandomNetwork;
import cytoscape.task.TaskMonitor;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 正常者群の各経路のFiber数のトリム平均値を代表値とし、この代表値を上回っている経路のEdgeは残し、下回っている経路のEdgeは消去した。
 * こうして出来上がった間引き後のネットワークをBinarizedNetworkとした。
 * このBinarizedNetworkと同数のEdgeを持つランダムネットワークを生成するプログラム。
 *
 * @author issey
 */
public class RandomNetworkMaker_ver3 implements Runnable {
    public static void main(String[] args) {

        RandomNetworkMaker_ver3 RNM1 = new RandomNetworkMaker_ver3("output_files/ArrangedData/TotalEdgeCountList_of_BinarizedNetwork/CORTISOL.txt",
                "output_files/ArrangedData/RandomNetwork_by_TrimedMean/CORTISOL");
        RNM1.doOperation();

        RandomNetworkMaker_ver3 RNM2 = new RandomNetworkMaker_ver3("output_files/ArrangedData/TotalEdgeCountList_of_BinarizedNetwork/NEO.txt",
                "output_files/ArrangedData/RandomNetwork_by_TrimedMean/NEO");
        RNM2.doOperation();

        RandomNetworkMaker_ver3 RNM3 = new RandomNetworkMaker_ver3("output_files/ArrangedData/TotalEdgeCountList_of_BinarizedNetwork/PCLO.txt",
                "output_files/ArrangedData/RandomNetwork_by_TrimedMean/PCLO");
        RNM3.doOperation();

        System.exit(1);

    }


    public RandomNetworkMaker_ver3(String input_file_path, String output_folder_path) {
        this.output_folder_path = output_folder_path;
        this.input_file_path = input_file_path;
    }

    public String output_folder_path;
    public String input_file_path;
    private HashMap<String, String> EdgeCountMap = new HashMap<>();

    public void run() {

    }

    public boolean doOperation() {
        //フィルタリングの結果生成されたGraphmlファイル各々の総Edge数をCollectionに格納
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(new File(input_file_path)));
            String line;
            String[] subline;
            while ((line = br.readLine()) != null) {
                subline = line.split(" ");
                EdgeCountMap.put(subline[0], subline[1]);
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

        /**
         * 変数の準備
         */
        int nodes;
        boolean allowReflexive;
        boolean directed;
        int edges;
        ErdosRenyiModel erm;
        RandomNetwork random_network;

        /**
         * FileteredGraphmlに対応するRandomNetworkを生成する。
         * EdgeCountMapに格納されているファイル名とEdge数を使う。
         * <p>
         */
        Iterator it = EdgeCountMap.keySet().iterator();
        Object key;
        String name_of_newfile = "";
        while (it.hasNext()) {
            key = it.next();
            edges = Integer.valueOf(EdgeCountMap.get(key));
            System.out.println("edges: " + edges);
            nodes = 83;
            allowReflexive = false;
            directed = false;

            //Create the model
            /**
             * ランダムネットワーク生成のコアな部分
             * Erdos Renyi Model
             */
            erm = new ErdosRenyiModel(nodes, edges, allowReflexive, directed);

            //Generate the graph
            random_network = erm.generate();

            network = Cytoscape.getCurrentNetwork();
            network.setTitle(("RandomNetwork_of_" + (String) key));
            network.appendNetwork(random_network.toCyNetwork());

            try {
                int numNodes = network.getNodeCount();

                if (numNodes == 0) {
                    throw new IllegalArgumentException("Network is empty.");
                }

                name_of_newfile = (String) key + ".graphml";
                name_of_newfile = name_of_newfile.replaceAll("Binarized", "RandomNetwork");
                System.out.println("name_of_newfile:" + name_of_newfile);
                this.saveGraph(output_folder_path + "\\" + name_of_newfile);

                //taskMonitor.setPercentCompleted(100);
                //taskMonitor.setStatus("Network successfully saved to:  " + fileName);
            } catch (IllegalArgumentException e) {
                //taskMonitor.setException(e, "Network is Empty.  Cannot be saved.");
            } catch (IOException e) {
                //taskMonitor.setException(e, "Unable to save network.");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Cytoscape.destroyNetwork(network);
        }

        //正しい終わり方がわからない
        return true;

    }

    static private CyNetwork network;
    static private TaskMonitor taskMonitor;

    /**
     * Saves Graph to File.
     * <p>
     *
     * @throws IOException Error Writing to File.
     */
    private boolean saveGraph(String save_file_path) throws Exception {
        FileWriter fileWriter = null;

        File newfile = new File(save_file_path);
        newfile.getParentFile().mkdirs();
        newfile.createNewFile();

        try {
            fileWriter = new FileWriter(newfile);
            final GraphMLWriter writer = new GraphMLWriter(network, fileWriter, taskMonitor);
            writer.write();
        } catch (Exception e) {
            e.printStackTrace();
            return false;

        } finally {
            if (fileWriter != null) {
                fileWriter.close();
                fileWriter = null;
            }
        }

        Object[] ret_val = new Object[3];
        ret_val[0] = network;
        ret_val[1] = newfile.toURI();
        // Currently unused (?)
        ret_val[2] = new Integer(-1);
        Cytoscape.firePropertyChange(Cytoscape.NETWORK_SAVED, null, ret_val);
        return true;
    }

    /**
     * Sets the Task Monitor.
     * <p>
     *
     * @param taskMonitor TaskMonitor Object.
     */
    public void setTaskMonitor(TaskMonitor taskMonitor)
            throws IllegalThreadStateException {
        this.taskMonitor = taskMonitor;
    }


}
