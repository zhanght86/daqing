package com.midas.uitls.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.midas.constant.ErrorConstant;
import com.midas.exception.ServiceException;

public class LocalFileOper extends FileOper {

    private Logger logger = LoggerFactory.getLogger(LocalFileOper.class);

    @Override
    public void delete(String srcPath, String workdir) throws ServiceException {
        String path = srcPath + "/" + workdir;
        File directory = new File(path);
        if (directory.exists() && directory.isDirectory()) {
            try {
                FileUtils.deleteDirectory(directory);
            } catch (IOException e) {
                logger.error("删除已存在的目录或文件失败目录为: " + path, e);
                throw new ServiceException(ErrorConstant.CODE4000, "删除已存在的目录或文件失败目录为: " + path);
            }
        }
    }

    @Override
    public BigDecimal copy(String srcPath, String destPath, String file, String targetPath) throws ServiceException {
        return copy(srcPath, destPath, file, targetPath, true);
    }

    public BigDecimal copy(String srcPath, String destPath, String file, String targetPath, boolean isOrgpath)
            throws ServiceException {
        
        Map<String,BigDecimal>  fileSizeMap=new HashMap<String,BigDecimal>();
        File sourceFile = new File(srcPath + "/" + file);
        if (isOrgpath) {
            File p = new File(srcPath);
            targetPath = targetPath + "/" + sourceFile.getParent().replace(p.getAbsolutePath(), "");
        }
        File targetFile = new File(destPath + "/" + targetPath);
        BigDecimal size = new BigDecimal(0);
        try {
            long st = System.currentTimeMillis();
            // File f2 = new File(sourceFile.getParent());

            if (sourceFile.isDirectory()) {
                for (File fs : sourceFile.listFiles()) {
                    BigDecimal currentsize = new BigDecimal(0);
                    currentsize=(new BigDecimal(fs.length()));
                    logger.info("文件名{} size={}", fs.getName(), currentsize);
                    fileSizeMap.put(fs.getName(), currentsize);
                    size = size.add(new BigDecimal(fs.length()));
                }
                FileUtils.copyDirectory(sourceFile,
                        new File(targetFile.getAbsolutePath() + "/" + sourceFile.getName()));
            } else {
                for (File fs : sourceFile.getParentFile().listFiles()) {
                    if (fs.getName().startsWith(sourceFile.getName())) {
                        BigDecimal currentsize = new BigDecimal(0);
                        currentsize=(new BigDecimal(fs.length()));
                        fileSizeMap.put(fs.getName(), currentsize);
                        logger.info("文件名{} size={}", fs.getName(), currentsize);
                        size = size.add(new BigDecimal(fs.length()));
                        FileUtils.copyFileToDirectory(fs, targetFile);
                        logger.info("copy文件从： {} 到 {}, 耗时： {}", fs.getAbsoluteFile(), targetFile.getAbsolutePath(),
                                System.currentTimeMillis() - st);

                    }
                }
            }
            
            
            
             for(File f : targetFile.listFiles()) {
             if(f.isDirectory()) {
             for(File fs : f.listFiles()) {
                 
                 
                 BigDecimal oldSize= fileSizeMap.get(fs.getName());
                 BigDecimal currentSize =  getCurrentSize(fs.getAbsolutePath());
                 logger.info("文件名{} oldsize={}, size={}", fs.getAbsolutePath(), oldSize,currentSize);
             }
           
             } else if (f.getName().startsWith(sourceFile.getName())) {
           
                 BigDecimal oldSize= fileSizeMap.get(f.getName());
                 BigDecimal currentSize =  getCurrentSize(f.getAbsolutePath());
                 logger.info("文件名{} oldsize={}, 当前size={}", f.getAbsolutePath(), oldSize,currentSize);
            
             }
             }
            
            // for(File f : sourceFile.listFiles()) {
            // if(f.isDirectory()) {
            // for(File fs : f.listFiles()) {
            // size = size.add(new BigDecimal(fs.length()));
            // }
            // FileUtils.copyDirectory(f, new File(targetFile.getAbsolutePath()
            // + "/" + f.getName()));
            // } else if (f.getName().startsWith(sourceFile.getName())) {
            // size = size.add(new BigDecimal(f.length()));
            // FileUtils.copyFileToDirectory(f, targetFile);
            // logger.info("copy文件从： {} 到 {}, 耗时： {}", f.getAbsoluteFile(),
            // targetFile.getAbsolutePath(), System.currentTimeMillis() - st);
            // }
            // }
        } catch (IOException e) {
            logger.error("文件copy失败, 源文件为：" + sourceFile.getAbsolutePath(), e);
            throw new ServiceException(ErrorConstant.CODE4000, "文件copy失败, 源文件未找到" + sourceFile.getAbsolutePath());
        }
        return size;
    }
    
    @Override
    public BigDecimal copyV2(String srcPath, String destPath, String file, String targetPath)
            throws ServiceException {
        
        Map<String,BigDecimal>  fileSizeMap=new HashMap<String,BigDecimal>();
        File sourceFile = new File(srcPath + "/" + file);
     
        File targetFile = new File(destPath + "/" + targetPath);
        BigDecimal size = new BigDecimal(0);
        try {
            long st = System.currentTimeMillis();
            // File f2 = new File(sourceFile.getParent());

            if (sourceFile.isDirectory()) {
                for (File fs : sourceFile.listFiles()) {
                    BigDecimal currentsize = new BigDecimal(0);
                    currentsize=(new BigDecimal(fs.length()));
                    logger.info("文件名{} size={}", fs.getName(), currentsize);
                    fileSizeMap.put(fs.getName(), currentsize);
                    size = size.add(new BigDecimal(fs.length()));
                }
                FileUtils.copyDirectory(sourceFile,
                        new File(targetFile.getAbsolutePath() + "/" + sourceFile.getName()));
            } else {
                for (File fs : sourceFile.getParentFile().listFiles()) {
                    if (fs.getName().startsWith(sourceFile.getName())) {
                        BigDecimal currentsize = new BigDecimal(0);
                        currentsize=(new BigDecimal(fs.length()));
                        fileSizeMap.put(fs.getName(), currentsize);
                        logger.info("文件名{} size={}", fs.getName(), currentsize);
                        size = size.add(new BigDecimal(fs.length()));
                        FileUtils.copyFile(fs, targetFile);
                        logger.info("copy文件从： {} 到 {}, 耗时： {}", fs.getAbsoluteFile(), targetFile.getAbsolutePath(),
                                System.currentTimeMillis() - st);

                    }
                }
            }
            
			if (targetFile.isDirectory()) {
				for (File f : targetFile.listFiles()) {
					if (f.isDirectory()) {
						for (File fs : f.listFiles()) {

							BigDecimal oldSize = fileSizeMap.get(fs.getName());
							BigDecimal currentSize = getCurrentSize(fs.getAbsolutePath());
							logger.info("文件名{} oldsize={}, size={}", fs.getAbsolutePath(), oldSize, currentSize);
						}

					} else if (f.getName().startsWith(sourceFile.getName())) {

						BigDecimal oldSize = fileSizeMap.get(f.getName());
						BigDecimal currentSize = getCurrentSize(f.getAbsolutePath());
						logger.info("文件名{} oldsize={}, 当前size={}", f.getAbsolutePath(), oldSize, currentSize);

					}
				}
			}
            
         
        } catch (IOException e) {
            logger.error("文件copy失败, 源文件为：" + sourceFile.getAbsolutePath(), e);
            throw new ServiceException(ErrorConstant.CODE4000, "文件copy失败, 源文件未找到" + sourceFile.getAbsolutePath());
        }
        return size;
    }

    public BigDecimal getCurrentSize(String file) {

        FileInputStream fis = null;
        try {
            File f = new File(file);
            fis = new FileInputStream(f);
         
            logger.info("找到文件{},size={}",f.getName(),f.length());
            return new BigDecimal(f.length());
        } catch (Exception e) {
            logger.error("未找到文件");
        } finally {
            if (null != fis) {
                try {
                    fis.close();
                } catch (IOException e) {
                    logger.error("未找到文件");
                }
            }
        }
        return null;
    }
    // @Override
    // public void copyLocal(String localpath, String destpath, String file,
    // String targetPath) throws ServiceException {
    // File localfile = new File(localpath + "/" + file);
    // File destDir = new File(destpath + "/" + targetPath);
    // try {
    // FileUtils.copyFileToDirectory(localfile, destDir);
    // } catch (IOException e) {
    // logger.error("本地文件copy到远程机器失败", e);
    // throw new ServiceException(ErrorConstant.CODE4000, "文件从:" + localpath +
    // "到目录" + targetPath + "失败");
    // }
    // }

    @Override
    public boolean createFile(String srcpath, String workdir, String filename, String data) throws ServiceException {
        boolean isCreateSucc = false;
        BufferedWriter bw = null;
        File ftmp = new File(srcpath + "/" + workdir + "/temp");
        File f = new File(srcpath + "/" + workdir + "/" + filename);
        try {
            bw = new BufferedWriter(new FileWriter(ftmp));
            bw.write(data);
            bw.flush();
        } catch (Exception e) {
            logger.error("创建文件， 写入内容失败, 文件名为：" + filename, e);
            throw new ServiceException(ErrorConstant.CODE4000, "创建文件失败, 文件为：" + f.getAbsolutePath());
        } finally {
            if (null != bw) {
                try {
                    bw.close();
                } catch (IOException e) {
                }
            }
        }
        isCreateSucc = ftmp.renameTo(f);
        return isCreateSucc;
    }

    public static void main(String[] args) throws Exception {

//        BigDecimal bd = new BigDecimal(1);
//        BigDecimal bd2 = new BigDecimal(10);
//        bd = bd.add(bd2);
        // bd.
//        System.out.println(bd.intValue());

         LocalFileOper fo = new LocalFileOper();
         BigDecimal bd = fo.copyV2("E:/", "f:/", "kinggsoft","test");
         System.out.println(bd.doubleValue());
         LocalFileOper oper = new LocalFileOper();
        
//         Map<String, Object> sourceMachine = new HashMap<String, Object>();
//         sourceMachine.put("sp_value4", "f:/tmp/share");
//        
//         Map<String, Object> targetMachine = new HashMap<String, Object>();
//         targetMachine.put("sp_value4", "f:/tmp/local");
//        
//         oper.copy(sourceMachine, targetMachine, "heshi/ababab.txt",
//         "ababab/S0001");
//        
//         boolean bool = oper.createFile(targetMachine, "/", "MIDASRUN.txt",
//         "BD50\nAAAAAAAAAAAA");
//         System.out.println(bool);

        // File destDir = new File("c:/ab/cd/");

        // FileUtils.copyFileToDirectory(file, destDir);

    }

}
