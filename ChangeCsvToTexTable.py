from sys import argv,stdout
import os
import csv
from glob import glob

def changeCsv(inputFilePath,outputFilePath):
    if os.path.isfile(outputFilePath):
        while True:
            stdout.write('\n出力ファイル名と同じファイルが既に存在します。')
            stdout.write('\n出力予定ファイルのファイルパス:\n'+str(outputFilePath))
            stdout.write('\n上書きして大丈夫な場合『Yes』、処理を辞める場合『No』を入力してください。')
            stdout.flush()
            try:
                inputString=str(input())
            except ValueError:
                stdout.write('\n入力内容が不正です。')
                stdout.flush()
                continue
            if inputString=='Yes':
                break
            elif inputString=='No':
                return False
            else:
                stdout.write('入力内容が不正です。')
                stdout.flush()
                continue
        while True:
            stdout.write('\n本当に上書きしてしまってかまいませんか?\n上書きして大丈夫な場合『Yes』、処理を辞める場合『No』を入力してください。')
            stdout.flush()
            try:
                inputString=str(input())
            except ValueError:
                stdout.write('\n入力内容が不正です。')
                stdout.flush()
                continue
            if inputString=='Yes':
                break
            elif inputString=='No':
                return False
            else:
                continue
    stdout.write('\n'+os.path.basename(inputFilePath)+' 処理中')
    stdout.flush()
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
    stdout.write('\n'+os.path.basename(inputFilePath)+' 処理完了')
    stdout.write('\n出力ファイルのファイルパス:\n'+os.path.splitext(str(os.getcwd()))[0]+'/'+os.path.basename(outputFilePath))
    stdout.flush()
    return True
if __name__=="__main__":
    runFlag=False
    helpFlag=False
    args=argv
    argument_list=["変換元ファイル/ディレクトリパス","変換後ファイル出力先ディレクトリパス"]
    if ('-h' in args) or ('-help' in args):
        stdout.write('\n【本プログラムの処理内容】')
        stdout.write('\n指定したcsvファイルをtexファイルで表を表示するための表記に変更するプログラム。\n出力ファイルの形式は.txtファイル')
        stdout.write('\n\n【本プログラムの実行形式】')
        stdout.write("\nPython3 "+str(args[0]))
        for argument in argument_list:
            stdout.write(" <"+str(argument)+">")
        helpFlag=True
        result=True
    elif len(args)==(len(argument_list)+1):
        inputFilePath=args[1]
        outputDirPath=args[2]
        if os.path.dirname(outputDirPath):
            try:
                if os.path.isfile(inputFilePath):
                    inputFilePathList=[inputFilePath]
                    runFlag=True
                elif os.path.isdir(inputFilePath):
                    inputFilePathList=sorted(glob(inputFilePath+'/*.csv'))
                    runFlag=True
                else:
                    stdout.write('\n変換元ファイルが存在しません')
                    result=False
            except FileNotFoundError:
                stdout.write('\n変換元ファイルが存在しません')
                result=False
        else:
            stdout.write("出力先ディレクトリが存在しません")
            result=False
    else:
        result=False
        stdout.write('\n引数の数が不正')
    if runFlag:
        for inputFilePath in inputFilePathList:
            stdout.write('\n'+os.path.basename(inputFilePath)+' 処理開始')
            stdout.flush()
            outputFilePath=outputDirPath+"/"+os.path.splitext(os.path.basename(inputFilePath))[0]+'.tex.txt'
            os.makedirs(os.path.dirname(outputFilePath),exist_ok=True)
            result=changeCsv(inputFilePath,outputFilePath)
            stdout.write('\n'+os.path.basename(inputFilePath)+' 処理終了')
            stdout.flush()
    if result or helpFlag:
        stdout.write('\n正常終了')
    else:
        stdout.write('\n異常終了')
    stdout.write('\n')
    stdout.flush()