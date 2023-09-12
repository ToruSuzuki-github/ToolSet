import java.io.File;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

public class RenameCaptureFile{
    //public static void main(String args[]){
    public static void main(String[] args){
        //---------------オプション情報の整理---------------
        Map<String, String> ops_par = new HashMap<>();
        int index_of;
        if(Arrays.asList(args).contains("-h") || Arrays.asList(args).contains("-help")){
            ops_par.put("-h","");
        }
        if(Arrays.asList(args).contains("-s")){
            index_of=Arrays.asList(args).indexOf("-s");
            ops_par.put("-s",args[index_of+1]);
        }
        //---------------ヘルプ表示---------------
        if(ops_par.containsKey("-h")){
            System.out.println("実行形式\njava RenameCaptureFile <rename対象ファイルが属するフォルダのパス> <オプション>");
            System.out.println("オプション");
            System.out.println("-s <フォルダー名> : 修正後のファイルをフォルダーにまとめる");
        }
        else{
            //---------------共通パラメーター---------------
            ArrayList<File> new_file_path_list = new ArrayList<>(); //修正後の新しいファイルパス一覧
            boolean not_change_name_flg=true; //ファイル修正を実施したかのフラグ
            
            //---------------ファイル名の修正---------------
            System.out.println("ファイル名の修正開始");
            
            //修正対象のパスを取得、パスがディレクトリかどうかをチェック
            if(args[0].endsWith("/")){
                args[0]=args[0].substring(0, args[0].length()-1);
            }
            File dir_path = new File(args[0]);
            if(dir_path.isDirectory()){

                // パラメーター設定
                File file_path_list[] = dir_path.listFiles(); //修正対象ディレクトリが含むパスの一覧
                String[] split_file_name; //split後の修正ファイル名
                File new_file_path; //修正後の新しいファイルパス
                
                //パス毎に処理（修正後ファイル名の作成、ファイル名の修正）
                for (File file_path: file_path_list){
                    
                    //パスがファイル以外の時スキップ
                    if(!(dir_path.isFile())){
                        continue;
                    }

                    //パス修正を行ったことを保存
                    not_change_name_flg=false;

                    // 修正後ファイル名を作成
                    split_file_name=file_path.getName().split(" ");
                    if(split_file_name.length>=3){
                        File tmp=new File(args[0]+"/"+split_file_name[split_file_name.length-2]+" "+split_file_name[split_file_name.length-1]);
                        new_file_path=tmp;
                    }
                    else{
                        continue;
                    }
                    
                    // ファイル名の修正
                    if (new_file_path.exists()) {
                        System.out.println("修正後ファイル名が既に利用されているためファイル名を修正できません");
                        System.out.println("修正失敗ファイル："+file_path);
                        System.out.println("重複ファイル："+new_file_path);
                    } else {
                        file_path.renameTo(new_file_path);
                        //new_file_path_list.add(new_file_path);
                    }
                }
                // ファイル名の修正を行わなかったとき出力
                if(not_change_name_flg){
                    System.out.println("指定ディレクトリ内に修正対象のファイルが存在しません");
                }
            }else{
                System.out.println("ディレクトリではありません");
            }
            System.out.println("ファイル名の修正完了");

            //---------------フォルダの作成とファイルの移動（オプション -s）---------------
            if(ops_par.containsKey("-s")){
                System.out.print("\n");
                System.out.println("フォルダの作成とファイルの移動開始");
                // ファイル名の修正を行わなかったときにフォルダの作成も行わない
                if(not_change_name_flg){
                    System.out.println("指定ディレクトリ内に修正対象のファイルが存在しないためフォルダを作成しません");
                }else{
                    //パラメーター
                    Path folder_path = Paths.get(args[0]+"/"+ops_par.get("-s")); //作成フォルダーのパス
                    
                    //フォルダーの存在チェック
                    if(folder_path.toFile().exists()){
                        System.out.println("フォルダ名が既に利用されているためフォルダの作成及びファイルの移動ができません");
                        System.out.println("作成失敗フォルダ："+folder_path);
                    }else{
                        //フォルダーの作成
                        try{
                            Files.createDirectory(folder_path);
                        }catch(IOException e){
                            System.out.println(e);
                        }
                        //ファイルをフォルダーへ移動
                        for(File before_move: new_file_path_list){
                            Path after_move = Paths.get(folder_path.toString()+"/"+before_move.getName().toString());
                            try{
                                Files.move(before_move.toPath(), after_move);
                            }catch(IOException e){
                                System.out.println(e);
                            }
                        }
                    }
                }
                System.out.println("フォルダの作成とファイルの移動完了");
            }
        }
    }
}