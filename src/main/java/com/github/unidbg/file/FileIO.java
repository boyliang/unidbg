package com.github.unidbg.file;

import com.github.unidbg.Emulator;
import com.github.unidbg.ios.struct.kernel.StatFS;
import com.sun.jna.Pointer;
import unicorn.Unicorn;

import java.io.IOException;

public interface FileIO {

    int SEEK_SET = 0;
    int SEEK_CUR = 1;
    int SEEK_END = 2;

    int SIOCGIFCONF = 0x8912;

    void close();

    int write(byte[] data);

    int read(Unicorn unicorn, Pointer buffer, int count);

    int fstat(Emulator emulator, Unicorn unicorn, Pointer stat);
    int fstat(Emulator emulator, StatStructure stat);

    int fcntl(int cmd, long arg);

    int ioctl(Emulator emulator, long request, long argp);

    FileIO dup2();

    int connect(Pointer addr, int addrlen);

    int setsockopt(int level, int optname, Pointer optval, int optlen);

    int sendto(byte[] data, int flags, Pointer dest_addr, int addrlen);

    int lseek(int offset, int whence);

    int ftruncate(int length);

    int getpeername(Pointer addr, Pointer addrlen);

    int shutdown(int how);

    int getsockopt(int level, int optname, Pointer optval, Pointer optlen);

    int getsockname(Pointer addr, Pointer addrlen);

    long mmap2(Unicorn unicorn, long addr, int aligned, int prot, int offset, int length) throws IOException;

    int llseek(long offset_high, long offset_low, Pointer result, int whence);

    int getdents64(Pointer dirp, int count);

    int recvfrom(Unicorn unicorn, Pointer buf, int len, int flags, Pointer src_addr, Pointer addrlen);

    int fstatfs(StatFS statFS);

    String getPath();
}
