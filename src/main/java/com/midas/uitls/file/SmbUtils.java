package com.midas.uitls.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.midas.constant.ErrorConstant;
import com.midas.exception.ServiceException;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

/**
 * netbios 共享
 * 
 * @author arron
 *
 */
public class SmbUtils {

    private Logger logger = LoggerFactory.getLogger(SmbUtils.class);

    // 源共享目录
    private String smbUrl       = "";
    // 目标共享目录
    private String targetSmbUrl = "";

    /**
     * 远端copy到本地
     * 
     * @param url
     */
    public SmbUtils(String url) {
        smbUrl = url;
    }

    /**
     * 远端copy到远端
     * 
     * @param url
     */
    public SmbUtils(String url, String targetSmbUrl) {
        smbUrl = url;
        this.targetSmbUrl = targetSmbUrl;
    }

    /**
     * smb目录: 远端copy到本地
     * 
     * @param ip
     *            IP地址
     * @param username
     *            用户名
     * @param password
     *            密码
     */
    public SmbUtils(String ip, String username, String password) {
        StringBuffer sb = new StringBuffer("smb://");
        sb.append(username);
        sb.append(":");
        sb.append(password);
        sb.append("@");
        sb.append(ip);
        sb.append("/");
        targetSmbUrl = sb.toString();
    }

    /**
     * 远端copy到远端使用
     * 
     * @param sourceIp
     *            源地址
     * @param sourceUsername
     *            源用户名
     * @param sourcePassword
     *            源密码
     * @param targetIp
     *            目标地址
     * @param targetUsername
     *            目标用户名
     * @param targetPassword
     *            目标密码
     */
    public SmbUtils(String sourceIp, String sourceUsername, String sourcePassword, String targetIp,
            String targetUsername, String targetPassword) {
        StringBuffer sb = new StringBuffer("smb://");
        sb.append(sourceUsername);
        sb.append(":");
        sb.append(sourcePassword);
        sb.append("@");
        sb.append(sourceIp);
        sb.append("/");
        smbUrl = sb.toString();

        StringBuffer targetSb = new StringBuffer("smb://");
        targetSb.append(targetUsername);
        targetSb.append(":");
        targetSb.append(targetPassword);
        targetSb.append("@");
        targetSb.append(targetIp);
        targetSb.append("/");
        targetSmbUrl = targetSb.toString();
    }

    /**
     * 远程smb共享文件copy到本地目录
     * 
     * @param sourceFile
     * @param targetFile
     */
    public int copyFileSTL(String sourceFile, String targetFile) throws ServiceException {
        int size = 0;
        InputStream fi = null;
        FileOutputStream fo = null;
        try {
            SmbFile smbFile = new SmbFile(targetSmbUrl + "//" + sourceFile);
            if (smbFile == null || !smbFile.exists()) {
                logger.error("文件不存在");
                throw new ServiceException(ErrorConstant.CODE1000, "文件不存在{" + smbUrl + "}/{" + sourceFile + "}");
            }
            fi = smbFile.getInputStream();
            fo = new FileOutputStream(targetFile);
            byte[] data = new byte[1024];
            int i;
            while ((i = fi.read(data)) != -1) {
                fo.write(data, 0, i);
                size += i;
            }
            return size;
        } catch (ServiceException e) {
            throw e;
        } catch (IOException e) {
            throw new ServiceException(ErrorConstant.CODE1000,
                    "文件copy失败, 源文件：" + smbUrl + sourceFile + ", 目标文件：" + targetFile, e);
        } finally {
            try {
                if (fi != null) {
                    fi.close();
                }
                if (fo != null) {
                    fo.close();
                }
            } catch (IOException e) {
            }
        }
    }

    /**
     * 远程smb共享文件copy到本地目录
     * 
     * @param sourceFile
     * @param targetFile
     */
    public int copyFileLTS(String sourceFile, String targetFile) throws ServiceException {
        InputStream fi = null;
        OutputStream fo = null;
        int size = 0;
        try {
            File file = new File(sourceFile);
            if (!file.exists()) {
                throw new ServiceException(ErrorConstant.CODE2000, "本地文件不存在");
            }
            // 读取本地文件
            fi = new FileInputStream(sourceFile);
            // 远程文件
            SmbFile smbFile = new SmbFile(targetSmbUrl + targetFile);
            fo = smbFile.getOutputStream();
            byte[] data = new byte[1024];
            int i;
            while ((i = fi.read(data)) != -1) {
                fo.write(data, 0, i);
                size += i;
            }
            fo.flush();
            return size;
        } catch (IOException e) {
            throw new ServiceException(ErrorConstant.CODE1000,
                    "本地copy到远程失败, 源文件：" + sourceFile + ", 目标文件：" + smbUrl + targetFile, e);
        } finally {
            try {
                if (fi != null) {
                    fi.close();
                }
                if (fo != null) {
                    fo.close();
                }
            } catch (IOException e) {
            }
        }
    }

    /**
     * 远程smb共享文件copy到本地目录
     * 
     * @param localDir
     *            本地固定路径
     * @param sourceFile
     *            文件路径
     * @param targetFile
     *            目标路面
     */
    public int copyFileLTS(String localDir, String sourceFile, String targetFile) throws ServiceException {
        File file = new File(localDir + "/" + sourceFile);
        targetFile = targetFile + "/" + file.getParent().replace("\\", "/").replace(localDir, "");
        return copyFileLTS(localDir + "/" + sourceFile, targetFile);
    }

    /**
     * 远程smb共享文件copy到远程目录
     * 
     * @param sourceFile
     * @param targetFile
     */
    public void copyFileSTS(String sourceFile, String targetPath) throws ServiceException {
        try {
            SmbFile smbFile = new SmbFile(smbUrl + sourceFile);
            smbFile.connect();
            String path = targetSmbUrl + targetPath;
            createPath(targetPath);
            SmbFile smbTargetFile = new SmbFile(path + "/" + smbFile.getName());
            smbFile.copyTo(smbTargetFile);
        } catch (IOException e) {
            throw new ServiceException(ErrorConstant.CODE1000,
                    "远程对远程copy失败, 源文件：" + smbUrl + sourceFile + ", 目标文件：" + targetSmbUrl + targetPath, e);
        }
    }

    /**
     * 远程对远程的copy文件
     * 
     * @param sourceFile
     *            源文件
     * @param targetPath
     *            目标目录
     * @param isOriginalPath
     *            是否保持原有的目录深度， true 保持， false 不保持
     */
    public void copyFileSTS(String sourceFile, String targetPath, boolean isOriginalPath) {
        if (isOriginalPath) {
            try {
                SmbFile smbFile = new SmbFile(smbUrl + sourceFile);
                String path = smbFile.getParent().replace(smbUrl, "");
                targetPath = targetPath + "/" + path;
            } catch (Exception e) {
                logger.error("创建目录失败", e);
                throw new ServiceException(ErrorConstant.CODE2000, "目录创建失败");
            }
        }
        copyFileSTS(sourceFile, targetPath);
    }

    /**
     * 远程共享copy到远程共享
     * 
     * @param sourcePath
     *            源文件路径
     * @param targetPath
     *            目标路径
     * @param filename
     *            文件名
     */
    public void copyFileSTS(String sourcePath, String targetPath, String filename) {
        copyFileSTS(sourcePath + "/" + filename, targetPath);
    }

    public void createFile(String file) throws Exception {
        SmbFile smbfile = new SmbFile(targetSmbUrl + file);
        String s = smbfile.getParent();

        SmbFile smbfilepath = new SmbFile(s);
        if (!smbfilepath.exists()) {
            smbfilepath.mkdirs();
        }
        if (!smbfile.exists()) {
            smbfile.createNewFile();
        }
    }

    public OutputStream createFileAndWrite(String file) throws Exception {
        createFile(file);
        SmbFile smbfile = new SmbFile(targetSmbUrl + file);
        return smbfile.getOutputStream();
    }

    public void rename(String file, String newFile) {
        try {
            SmbFile smbfile = new SmbFile(targetSmbUrl + file);
            if (!smbfile.exists()) {
                throw new ServiceException(ErrorConstant.CODE2000, "文件不存在");
            }
            SmbFile newSmbfile = new SmbFile(targetSmbUrl + newFile);
            smbfile.renameTo(newSmbfile);
        } catch (ServiceException e) {
            logger.error("文件未找到", e);
        } catch (MalformedURLException e) {
            logger.error("路径创建失败", e);
        } catch (SmbException e) {
            logger.error("路径创建失败", e);
        }
    }

    public void delete(String file) throws Exception {
        try {
            SmbFile smbFile = new SmbFile(targetSmbUrl + file);
            if(smbFile.exists()) {
                smbFile.delete();
            }
        } catch (Exception e) {
            logger.error("删除文件或者目录失败", e);
            throw e;
        }
    }

    /**
     * 给目标服务器创建目录
     * 
     * @param targetPath
     */
    private void createPath(String targetPath) {
        try {
            SmbFile smbfile = new SmbFile(targetSmbUrl + targetPath);
            if (!smbfile.exists()) {
                smbfile.mkdirs();
            }
        } catch (MalformedURLException e) {
            logger.error("路径创建失败", e);
        } catch (SmbException e) {
            logger.error("路径创建失败", e);
        }
    }

    // 共享创建目录

    public static void main(String[] args) throws Exception {
        // String url = "\\\\192.168.0.245\\admin";
        // String file = "客户需求.txt";
        // File f = new File(url + "/" + file);
        // System.out.println(f.exists());
        // BufferedReader br = new BufferedReader(new FileReader(f));
        //
        // String line = br.readLine();
        // while(line != null) {
        // System.out.println(line);
        // line = br.readLine();
        // }
        // System.out.println(br.readLine());

        // long st = System.currentTimeMillis();
        // SmbUtils sut = new SmbUtils("192.168.0.245", "admin", "jvcnas");
        // sut.copyFileSTL("/work/CentOS-6.6-i386-bin-DVD1.iso",
        // "c:/CentOS-6.6-i386-bin-DVD1.iso");
        // System.out.println("耗时:" + (System.currentTimeMillis() - st));
        // 耗时:2039079
        // 耗时:818618
        // 文件copy失败,
        // 源文件：smb://admin:jvcnas@192.168.0.19/vss\\NavicatforMySQL.zip,
        // 目标文件：c:/\R20151125000041\vss\\
        // File f = new File("\\\\192.168.0.225\\admin");

        // SmbUtils su = new SmbUtils("192.168.0.19", "admin", "jvcnas");
        // su.copyFileSTL("电子组/20140825LOG.rar", "c:\\20140825LOG.rar3");
        // TODO 小bug， 不能copy目录了
        // SmbUtils su = new SmbUtils("192.168.0.19", "admin", "jvcnas",
        // "192.168.0.224", "admin", "jvcnas");
        // su.copyFileSTS("电子组/20140825LOG.rar", "/share");
        // su.copyFileSTS("/DataSource/DB_Full/20130509/Src/classic/zzzz/Andaluza(Danza).ram",
        // "/share/S0010", true);

        SmbUtils su = new SmbUtils("192.168.0.222", "admin", "jvcnas");
        // su.createFile("/share/aaaa.txt");
        // su.rename("/share/aaaa.txt", "/share/bbb.txt");
        su.delete("share/ab/");
//        OutputStream os = su.createFileAndWrite("/share/bbb.txt");
//        os.write("刻录任务".getBytes("GB2312"));
//        su.rename("/share/bbb.txt", "/share/bbbbbb.txt");
//        os.close();
        //
        // su.copyFileLTS("c:/", "R20151125000046/电子组/20140825LOG.rar",
        // "/share/ab/cd");

        // for(String s : f.list()) {
        // System.out.println(">>"+s);
        // }
    }

}
