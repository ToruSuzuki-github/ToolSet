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
    
    //----------------構成要素の関数----------------
    // オプション整理の関数
    public static Map<String, String> option_clearing(String[] args){
        Map<String, String> ops_par = new HashMap<>();
        int index_of;
        if(Arrays.asList(args).contains("-h") || Arrays.asList(args).contains("-help")){
            ops_par.put("-h","");
        }
        if(Arrays.asList(args).contains("-s")){
            index_of=Arrays.asList(args).indexOf("-s");
            ops_par.put("-s",args[index_of+1]);
        }
        return ops_par;
    }
    // help表示の関数
    public static void std_help(){
        System.out.println("実行形式\njava RenameCaptureFile <rename対象ファイルが属するフォルダのパス> <オプション>");
        System.out.println("オプション");
        System.out.println("-s <フォルダー名> : 修正後のファイルをフォルダーにまとめる");
    }
    // 第1引数をディレクトリパスに変換
    public static File make_dir_path(String origin_str){
        if(origin_str.endsWith("/")){
            origin_str=origin_str.substring(0, origin_str.length()-1);
        }
        File dir_path = new File(origin_str);
        return dir_path;
    }
    // ファイル名の修正
    public static ArrayList<File> rename_file(File dir_path){
        System.out.println("Start Rename");
        
        // パラメーター設定
        ArrayList<File> new_file_path_arraylist = new ArrayList<>(); //修正後の新しいファイルパス一覧
        File file_path_list[] = dir_path.listFiles(); //修正対象ディレクトリが含むパスの一覧
        String[] split_file_name; //split後の修正ファイル名
        File new_file_path; //修正後の新しいファイルパス
        
        //パス毎に処理（修正後ファイル名の作成、ファイル名の修正）
        for (File file_path: file_path_list){
            System.out.print("- "+file_path.toString());
            
            //パスがファイル以外の時スキップ
            if(!(file_path.isFile())){
                System.out.println(" error ) It's not a file.");
                continue;
            }

            //パスが.png以外の時スキップ
            String file_name=file_path.getName();
            if(!(file_name.substring(file_name.lastIndexOf(".")).equals(".png"))){
                System.out.println(" error ) Not a capture file.");
                continue;
            }

            // 修正後ファイル名を作成
            split_file_name=file_name.split(" ");
            if(split_file_name.length>=3){
                File tmp=new File(dir_path.toString()+"/"+split_file_name[split_file_name.length-2]+" "+split_file_name[split_file_name.length-1]);
                new_file_path=tmp;
            }
            else{
                System.out.println(" error ) Failure to make of the new file name.");
                continue;
            }
            
            // ファイル名の修正
            if (new_file_path.exists()) {
                System.out.println(" error ) Duplicate new file name.");
                System.out.println("修正後ファイル名が既に利用されているためファイル名を修正できません");
                System.out.println("修正失敗ファイル："+file_path);
                System.out.println("重複ファイル："+new_file_path);
            } else {
                file_path.renameTo(new_file_path);
                new_file_path_arraylist.add(new_file_path);
                System.out.println(" changes to \""+new_file_path.toString()+"\".");
            }
        }
        System.out.println("End Rename");
        return new_file_path_arraylist;
    }
    // フォルダー作成&ファイル移動
    public static boolean make_folder_move_file(Path folder_path, ArrayList<File> new_file_path_arraylist){
        System.out.println("\nStart Make Folder and Move File");
        
        //フォルダーの存在チェック
        if(folder_path.toFile().exists()){
            System.out.println("フォルダ名が既に利用されているためフォルダの作成及びファイルの移動ができません");
            System.out.println("作成失敗フォルダ："+folder_path);
            return false;
        }else{
            //フォルダーの作成
            try{
                Files.createDirectory(folder_path);
            }catch(IOException e){
                System.out.println(e);
            }
            //ファイルをフォルダーへ移動
            for(File before_move: new_file_path_arraylist){
                Path after_move = Paths.get(folder_path.toString()+"/"+before_move.getName().toString());
                try{
                    Files.move(before_move.toPath(), after_move);
                }catch(IOException e){
                    System.out.println(e);
                }
            }
            System.out.println("End Make Folder and Move File");
            return true;
        }
    }
    
    //----------------基幹の関数----------------
    public static boolean rename_capture_file(String[] args){
        //パラメーター
        Map<String, String> ops_par = new HashMap<>();
        File dir_path;
        ArrayList<File> new_file_path_arraylist = new ArrayList<>(); //修正後の新しいファイルパス一覧
        
        //----------------基本的な機能----------------
        //オプションの整理
        ops_par=option_clearing(args);
        
        //オプションで-h指定の時help表示
        if(ops_par.containsKey("-h")){
            std_help();
            return false;
        }

        // ディレクトリパスの作成とチェック
        dir_path=make_dir_path(args[0]);
        if(!(dir_path.isDirectory())){
            System.out.println("ディレクトリではありません");
            return false;
        }

        // ファイル名の修正
        new_file_path_arraylist=rename_file(dir_path);
        if(new_file_path_arraylist.size()<=0){
            System.out.println("修正対象のファイルが存在しません");
            return false;
        }

        //----------------オプション機能----------------
        // フォルダー作成&ファイル移動
        if(ops_par.containsKey("-s")){
            // フォルダーパスの作成
            Path folder_path = Paths.get(dir_path.toString()+"/"+ops_par.get("-s")); //作成フォルダーのパス
            // フォルダー作成&ファイル移動
            if(!(make_folder_move_file(folder_path, new_file_path_arraylist))){
                return false;
            }
        }

        return true;
    }
    
    //----------------メイン関数----------------
    public static void main(String[] args){
        if(rename_capture_file(args)){
            System.out.println("Program Completion");
        }
        else{
            System.out.println("Program Failure");
        }
    }
}