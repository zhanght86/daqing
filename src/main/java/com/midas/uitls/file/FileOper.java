package com.midas.uitls.file;

import java.math.BigDecimal;

/**
 * 文件操作
 * 
 * @author arron
 *
 */
public abstract class FileOper {

    /**
     * 删除指定服务器的指定文件
     * 
     * @param machinaMap
     *            系统参数表中的key
     * @param workdir
     *            要删除的路径
     */
    public abstract void delete(String srcPath, String workdir);

    /**
     * 原机器的文件copy到目标机器的指定目录中， 目录结构不改变
     * 
     * @param sourceMachine
     *            源机器 系统参数表中的key
     * @param targetMachine
     *            目标机器 系统参数表中的key
     * @param file
     *            文件路径
     */
    public abstract BigDecimal copy(String srcPath, String destPath, String srcFile, String destFile);

    /**
     * 在目录下创建临时， 把内容写入文件， 修改文件名为真实名称
     * 
     * @param machine
     *            机器 系统参数表中的key
     * @param workdir
     *            目录
     * @param filename
     *            文件名
     * @param data
     *            写入的数据
     */
    public abstract boolean createFile(String srcPath, String workdir, String filename, String data);

}
