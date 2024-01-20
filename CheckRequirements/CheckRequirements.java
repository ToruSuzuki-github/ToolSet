import java.io.IOException;
import java.nio.file.Files; // 実際のふぁいいるやディレクトリにアクセス
import java.nio.file.Path; // パス名の保持、操作
import java.nio.file.Paths;
//import java.util.Map;
import java.util.HashMap;

public class CheckRequirements{
    //----------------大域変数----------------
    public static String ERROR_MESSAGE="";
    //----------------構成要素の関数----------------
    // 引数チェックの関数
    public static HashMap<String, Path> check_arguments(String[] args){
        // 戻り値
        HashMap<String, Path> requirements_paths = new HashMap<>();
        // 引数の総数
        if(args.length<2){
            ERROR_MESSAGE="Insufficient number of arguments.";
            //System.out.println("Insufficient number of arguments.");
            return requirements_paths;
            //return false;
        }
        // 引数の内容を確認（オプションは別の関数）
        Path my_requirements=Paths.get(args[0]);
        Path truth_requirements=Paths.get(args[1]);
        if(!(Files.exists(my_requirements))){
            ERROR_MESSAGE="The file specified by the first argument does not exist.";
            return requirements_paths;
            //return false;
        }
        if(!(Files.exists(truth_requirements))){
            ERROR_MESSAGE="The file specified by the second argument does not exist.";
            return requirements_paths;
            //return false;
        }
        // 戻り値調整
        requirements_paths.put("my_requirements", my_requirements);
        requirements_paths.put("truth_requirements", truth_requirements);
        return requirements_paths;
    }
    // オプション整理の関数
    /*
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
    */
    
    //----------------基幹の関数----------------
    public static boolean check_requirements(String[] args){
        // パラメーター
        //HashMap<String, Path> ops_par = new HashMap<>();

        // 引数のチェック
        HashMap<String, Path> requirements_paths=check_arguments(args);
        if(requirements_paths.isEmpty()){
            return false;
        }
        Path my_requirements=requirements_paths.get("my_requirements");
        Path truth_requirements=requirements_paths.get("truth_requirements");
        System.out.println(my_requirements);
        System.out.println(truth_requirements);


        // オプションのチェックと整理
        //ops_par=option_clearing()
        return true;

    }

    //----------------メイン関数----------------
    //第1引数：自作requirements.txtのファイルパス
    //第2引数：公式requirements.txtのファイルパス
    //オプション（-o）：出力ファイルの保存先（default="./"）
    public static void main(String[] args){
        if(check_requirements(args)){
            System.out.println("Program Completion");
        }
        else{
            System.out.println("Error: "+ERROR_MESSAGE);
            System.out.println("Type of key. java CheckRequirements --help");
        }
    }
}