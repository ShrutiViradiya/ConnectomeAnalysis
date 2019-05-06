package Fs6AnlRsltCnvrtr.ResultAnalyzer.Plugin_TextEditor;

/**
 * Pluginが考える、PluginHolderに持っていて欲しい機能。
 * Plungin側はPlunginHolder具体的には知らないが、Interface部分だけは知っている。
 * Plugin側から呼び出して使う。
 *
 * Created by issey on 2017/05/20.
 */
public interface ExpectationsAsPluginBase {

    /**
     * Pluginの状態を表示する部分を書き換えるメソッド
     */
    void updatePluginInfo();

}
