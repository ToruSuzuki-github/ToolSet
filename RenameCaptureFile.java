import java.io.File;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;


public class RenameCaptureFile{
    public static void main(String args[]){
        System.out.println("ファイル名の修正開始");

        //ディレクトリパスの取得とチェック
        if(args[0].endsWith("/")){
            args[0]=args[0].substring(0, args[0].length()-1);
        }
        File dir_path = new File(args[0]);
        if(dir_path.isDirectory()){

            // ファイル一覧の取得
            File file_path_list[] = dir_path.listFiles();
            //File  = new File("c:\\testNew.txt");
            // rename
            String[] split_file_name;
            File new_file_path;
            
            for (File file_path: file_path_list){

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
                    System.out.println("修正失敗ファイル名："+file_path);
                    System.out.println("重複ファイル名："+new_file_path);
                } else {
                    file_path.renameTo(new_file_path);
                    //System.out.println("succeed in rename");
                }
            }
        }else{
            System.out.println("ディレクトリではありません");
        }
        System.out.println("ファイル名の修正完了");
    }
}