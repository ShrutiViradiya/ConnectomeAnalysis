package BrainMapper_ver4.core;

import BrainMapper_ver4.core_support.GraphEdgeList;
import BrainMapper_ver4.core_support.GraphNodeList;
import BrainMapper_ver4.utils.JXPathReadHelper_ver1;
import BrainMapper_ver4.utils.JXPathWriteHelper_ver1;
import org.w3c.dom.Document;

import java.awt.*;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;

/**
 * Container の設定の読み込みと保存をするクラス
 */
public class SaverAndLoader {
    /**
     * コンストラクタ
     */
    private SaverAndLoader() {
    }

    static String SaveFolder = "src/BrainMapper_ver4/savefolder";

    /**
     * file から設定を読み込みます
     *
     * @param file
     * @return
     */
    static Container load(String file) {
        Container container = null;
        XMLDecoder decoder = null;
        try {
            decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(file)));
            container = (Container) decoder.readObject();
        } catch (FileNotFoundException e) {
            System.err.println("設定ファイル " + file + " が見つかりません");
        } catch (NullPointerException nex) {
            System.err.println("container がnullの可能性あり");
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            System.err.println("ArrayIndexOutOfBoundsException");
        } finally {
            if (decoder != null) {
                decoder.close();
            }
        }
        return container;
    }

    static GraphField loadMindmapField(MainFrame main_frame, String MindmapFieldDataFilePath, String NodeListFilePath) {
        System.out.println("---------- Start: loadMindmapField ----------");
        //MindmapFieldDataFileのロード
        GraphField mmField = null;
        File MindmapFieldDataFile = new File(SaveFolder + "/" + MindmapFieldDataFilePath);
        mmField = new GraphField(main_frame);
        mmField.setFont(MainFrame.DEFAULT_FONT);

        File NodeListFile = new File(SaveFolder + "/" + NodeListFilePath);
        Document DomDocument = null;
        //NodeList、EdgeListのロード
        try {
            JXPathReadHelper_ver1 JXPRH = new JXPathReadHelper_ver1(NodeListFile.getAbsolutePath());

            //Nodeのロード
            ArrayList<String> BrainArea_Xs = JXPRH.getListOfTargetElementValue("//brain/node/x");
            ArrayList<String> BrainArea_Ys = JXPRH.getListOfTargetElementValue("//brain/node/y");
            ArrayList<String> BrainArea_Values = JXPRH.getListOfTargetElementValue("//brain/node/value");
            ArrayList<String> BrainArea_IDs = JXPRH.getListOfTargetElementValue("//brain/node/@id");
            GraphNodeList LoadedNodeList = new GraphNodeList();
            System.out.println("BrainArea_Xs.size(): " + BrainArea_Xs.size());
            System.out.println("BrainArea_Values.size(): " + BrainArea_Values.size());
            System.out.println("BrainArea_IDs.size(): " + BrainArea_IDs.size());
            for (int i = 0; i < BrainArea_Xs.size(); i++) {
                GraphNode new_mmnode = new GraphNode(BrainArea_IDs.get(i), BrainArea_Values.get(i));
                new_mmnode.setBounds(Integer.valueOf(BrainArea_Xs.get(i)), Integer.valueOf(BrainArea_Ys.get(i)), 80, 30);
                LoadedNodeList.add(new_mmnode);
            }
            mmField.setNodeList(LoadedNodeList);
            mmField.setAsRootNode(LoadedNodeList.get(0));
            mmField.deployNodes();

            //Edgeのロード
            ArrayList<String> Edge_IDs = JXPRH.getListOfTargetElementValue("//brain/edge/@id");
            ArrayList<String> SrcNodeIDs = JXPRH.getListOfTargetElementValue("//brain/edge/srcNode");
            ArrayList<String> DestNodeIDs = JXPRH.getListOfTargetElementValue("//brain/edge/destNode");
            GraphEdgeList LoadedEdgeList = new GraphEdgeList();
            for (int i = 0; i < SrcNodeIDs.size(); i++) {
                LoadedEdgeList.add(new GraphEdge(Edge_IDs.get(i), LoadedNodeList.getNodeByID(SrcNodeIDs.get(i)), LoadedNodeList.getNodeByID(DestNodeIDs.get(i))));
            }
            mmField.setEdgeList(LoadedEdgeList);
            mmField.deployEdges();

        } catch (FileNotFoundException e) {
            System.err.println("設定ファイル " + NodeListFilePath + " が見つかりません");
        } catch (NullPointerException nex) {
            System.err.println("container がnullの可能性あり");
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            System.err.println("ArrayIndexOutOfBoundsException");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        System.out.println("---------- End: loadMindmapField ----------");
        return mmField;
    }

    /**
     * file に container の設定を保存します
     *
     * @param container;日本語そうなノコ
     * @param file
     */
    static void save(Container container, String file) {
        System.out.println("SaverAndLoader#save()");
        XMLEncoder encoder = null;
        boolean flag = false;
        try {
            for (int i = 0; i < 10; i++) {
                // ロックファイル
                File tempFile = new File(file + ".tmp");
                if (tempFile.createNewFile()) {
                    encoder = new XMLEncoder(new BufferedOutputStream(
                            new FileOutputStream(file)));
                    encoder.writeObject(container);
                    flag = true;
                    tempFile.delete();
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // 無視する
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("設定ファイル " + file + " が見つかりません");
        } catch (IOException e) {
            System.err.println("入出力エラーが発生しました");
        } catch (Exception e) {
            System.err.println("エラー?");
        } finally {
            if (encoder != null) {
                encoder.close();
            }
        }
        if (!flag) {
            System.err.println("設定ファイル" + file + "を保存できませんでした");
        }
    }


    static void saveMindmapField(GraphField mmField, String file) {
        System.out.println("SaverAndLoader#saveMindMapFile()");
        XMLEncoder encoder = null;
        boolean flag = false;
        try {
            //for (int i = 0; i < 10; i++) {
            // ロックファイル
            File tempFile = new File(file + ".tmp");
            if (tempFile.createNewFile()) {
                encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(file)));
                encoder.writeObject(mmField);
                flag = true;
                tempFile.delete();
                //break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // 無視する
            }
            //}
        } catch (FileNotFoundException e) {
            System.err.println("設定ファイル " + file + " が見つかりません");
        } catch (IOException e) {
            System.err.println("入出力エラーが発生しました");
        } finally {
            if (encoder != null) {
                encoder.close();
            }
        }
        if (!flag) {
            System.err.println("設定ファイル" + file + "を保存できませんでした");
        } else {
            System.err.println("設定ファイル" + file + "を保存しました。");

        }
    }

    static void saveNodes(ArrayList<GraphNode> mmNodeList, ArrayList<GraphEdge> mmEdgeList, String filename) {
        System.out.println("SaverAndLoader#saveNodes()");
        boolean flag = false;
        try {
            JXPathWriteHelper_ver1 jxh = new JXPathWriteHelper_ver1();
            Document DomDocument = jxh.createNewDomDocument("brain");
            System.out.println(" XMLへの書き込み...");
            System.out.println("mmNodeList.size(): " + mmNodeList.size());
            for (GraphNode node : mmNodeList) {
                /**
                 * Nodeの保存
                 */
                jxh.appendJXPath(DomDocument, "/brain/node[@id='" + node.getElementID() + "']/x", node.getLeftUpperCornerX());
                jxh.appendJXPath(DomDocument, "/brain/node[@id='" + node.getElementID() + "']/y", node.getLeftUpperCornerY());
                jxh.appendJXPath(DomDocument, "/brain/node[@id='" + node.getElementID() + "']/value", node.getTextarea().getText());
            }
            for (GraphEdge edge : mmEdgeList) {
                /**
                 * Edgeの保存
                 */
                jxh.appendJXPath(DomDocument, "/brain/edge[@id='" + edge.getElementID() + "']/srcNode", edge.getSrcNode().getElementID());
                jxh.appendJXPath(DomDocument, "/brain/edge[@id='" + edge.getElementID() + "']/destNode", edge.getDestNode().getElementID());
            }
            flag = jxh.outputDomDocumentToFile(DomDocument, new File(SaveFolder + "/" + filename));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // 無視する
            }
        } finally {
            if (!flag) {
                System.err.println(" 設定ファイル" + filename + "を保存できませんでした");
            } else {
                System.err.println(" 設定ファイル" + filename + "を保存しました。");
            }
        }

    }
}
