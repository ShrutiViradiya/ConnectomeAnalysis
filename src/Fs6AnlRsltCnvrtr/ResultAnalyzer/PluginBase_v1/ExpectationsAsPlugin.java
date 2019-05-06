package Fs6AnlRsltCnvrtr.ResultAnalyzer.PluginBase_v1;

import Plugin_TextEditor.ExpectationsAsPluginBase;

import javax.swing.*;
import java.util.ArrayList;

/**
 * PluginHolderが考える、Pluginに持っていて欲しい機能。
 * Plungin側はPlunginHolderを具体的には知らないが、Interface部分だけは知っている。
 * PluginHolder側から呼び出して使う。
 *
 * Created by issey on 2017/05/20.
 */
public interface ExpectationsAsPlugin {
    ArrayList<JMenuItem> getFileMenuItemCandidates();
    ArrayList<JMenuItem> getEditMenuItemCandidates();
    ArrayList<JMenuItem> getDispMenuItemCandidates();
    ArrayList<JMenuItem> getHelpMenuItemCandidates();

    void updateEditMenuItemState();
    void resetEditMenuItemState();

    String getCenterFooterLabelTextCandidate();
    String getRightFooterLabelTextCandidate();
    String getTitleTextCandidate();

    String getPluginName();

    JPanel getBasePanel();

    void exit();

    void setPluginBase(ExpectationsAsPluginBase PluginBase);


}
