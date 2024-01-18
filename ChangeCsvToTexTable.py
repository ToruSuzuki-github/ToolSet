# オプション-fは未実装

from sys import argv,stdout
import os
import csv
from glob import glob

def std_help(args):
    print("Usage: Python3",args[0],"<converted file or dir path>","<output dir path>","[OPTION]...")
    print("Create tex format table from csv file.")
    print("-i, --interactive, When a file with the same name as the file to be output exists, select whether or not to overwrite the file one by one.")
    #print("-f, --folder, All csv files in the folder are to be treated as the target of the letter.")
    print("    --help, display this help and exit.")

def check_file_exist(file_path):
    if os.path.isfile(file_path):
        while True:
            stdout.write('出力ファイル名と同じファイルが既に存在します。\n')
            stdout.write('出力予定ファイルのファイルパス:\n'+str(file_path)+"\n")
            stdout.write('上書きして大丈夫な場合『Y』、処理を辞める場合『N』を入力してください。\n')
            stdout.flush()
            try:
                inputString=str(input())
            except ValueError:
                stdout.write('入力内容が不正です。\n')
                stdout.flush()
                continue
            if inputString=='Y':
                return True
            elif inputString=='N':
                return False
            else:
                stdout.write('入力内容が不正です。\n')
                stdout.flush()
                continue

def change_csv(inputFilePath,outputFilePath,i_flg):
    
    stdout.write('\n'+os.path.basename(inputFilePath))
    stdout.flush()
    if i_flg:
        if not check_file_exist(outputFilePath):
            stdout.write(' quit\n')
            return False
    with open(inputFilePath,'r') as rf:
        fileDataList=csv.reader(rf)
        with open(outputFilePath,'w') as wf:
            #wf.write("\n\\begin{{table}}[t] %表の位置を指定")
		    #wf.write("\n\\%\\centering")
		    #wf.write("\n\\hspace{{-1.5cm}}")
    		#wf.write("\n\\begin{{tabular}}{{}}} %カラムのデータ位置の設定")
            for row in fileDataList:
                for num,element in enumerate(row,start=1):
                    wf.write(element)
                    if num==len(row):
                        wf.write(' \\\\\n')
                    else:
                        wf.write(' & ')
    stdout.write(' change\n')
    stdout.write('output file: '+str(outputFilePath)+"\n")
    stdout.flush()
    return True

def main(args):
    error=[]

    # ヘルプ表示チェック
    if len(args)==2:
        # ヘルプ表示
        if args[1]=="--help":
            std_help(args)
            return error
    # 引数の数チェック
    if len(args)<3 or 4<len(args):
        error.append("引数の数が不正")
        return error

    # 出力先パスのチェック
    if not os.path.isdir(args[2]):
        error.append("出力先ディレクトリパスが不正")
    
    # 変換元パスのチェック
    if os.path.isfile(args[1]):# 変換元がファイルパス
        converted_files=[args[1]]
    elif os.path.isdir(args[1]):# 変換元がディレクトリパス
        converted_files=glob(args[1]+"/*.csv")
    else:# 変換元が存在しない
        error.append("変換元ファイルが存在しない")
    
    # オプションのチェック
    i_flg=False
    if len(args)==4:
        options=args[3:]
        if options[0] in {"-i", "--interactive"}:
            i_flg=True
        else:
            error.append("オプション指定の内容が不正")
    
    # 引数の内容が不正な場合
    if len(error)>0:
        return error
    
    # 実行
    for converted_file in converted_files:
        output_name=os.path.splitext(os.path.basename(converted_file))[0]+".tex"
        change_csv(converted_file, args[2]+"/"+output_name, i_flg)
    return error

# -i	--interactive	削除前に確認する
# "Usage: Python3",args[0],"<converted path>","<output dir path>","[OPTION]..."
if __name__=="__main__":
    error=main(argv)
    if len(error)>0:
        for error_detail in error:
            print("FAILD:",error_detail)
        print("Try \'python3 ",argv[0]," --help\' for more information.")
