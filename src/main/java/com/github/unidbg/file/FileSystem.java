package com.github.unidbg.file;

import java.io.File;

public interface FileSystem {

    String DEFAULT_ROOT_FS = "target/rootfs/default";
    String DEFAULT_WORK_DIR = "unidbg_work";

    File getRootDir();
    File createWorkDir(); // 当设置了rootDir以后才可用，为rootDir/unidbg_work目录

    FileResult open(String pathname, int oflags);
    void unlink(String path);

    /**
     * @return <code>true</code>表示创建成功
     */
    boolean mkdir(String path);

}
