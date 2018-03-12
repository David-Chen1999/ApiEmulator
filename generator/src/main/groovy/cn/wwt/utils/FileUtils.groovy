package cn.wwt.utils

import org.springframework.util.FileCopyUtils

/**
 * Created by davidchen on 07/04/2017.
 */
class FileUtils {
    def static copyDirExceptFileExtention(String srcDir,String destDir,String exceptionExtension){
        File fromDir = new File(srcDir)
        File toDir = new File(destDir)
        if(fromDir.exists() && fromDir.isDirectory()){
            if(!toDir.exists()){
                toDir.mkdirs()
            }
            fromDir.listFiles().each { f ->
                if(f.isDirectory()){
                    copyDirExceptFileExtention(f.getAbsolutePath(),destDir + "/" + f.getName())
                }else if(!f.getPath().endsWith(exceptionExtension)){
                    FileCopyUtils.copy(f,new File(toDir,f.getName()))
                }
            }
        }
     }

    def static copyDir(String srcDir,String destDir){
        File fromDir = new File(srcDir)
        File toDir = new File(destDir)
        if(fromDir.exists() && fromDir.isDirectory()){
            if(!toDir.exists()){
                toDir.mkdirs()
            }
            fromDir.listFiles().each { f ->
                if(f.isDirectory()){
                    copyDir(f.getAbsolutePath(),destDir + "/" + f.getName())
                }else{
                    FileCopyUtils.copy(f,new File(toDir,f.getName()))
                }
            }
        }
    }

    def static File makePackage(String classDir,String packageName){
        final file = new File(classDir + "/"+packageName.replace('.', '/'))
        file.mkdirs()
        return file
    }

    def static File makeClass(String classDir, String packageName, String className, String content){
        final file = new File(classDir + "/"+packageName.replace('.', '/'))
        file.mkdirs()
        File dir = FileUtils.makePackage(classDir,packageName)
        new File(dir,className).withPrintWriter("utf-8") { writer ->
            writer.write(content)
        }
        return file
    }

    def static File makeFile(String dir, String fileName, String content){
        final file = new File(dir)
        file.mkdirs()
//        new File(file,fileName).withPrintWriter { writer ->
//            writer.write(content)
//        }
        def stream = new FileOutputStream(new File(file, fileName))
        stream.write(content.getBytes("utf-8"))
        stream.close()
        return file
    }
}
