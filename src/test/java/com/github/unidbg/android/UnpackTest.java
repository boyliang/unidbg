package com.github.unidbg.android;

import com.github.unidbg.Emulator;
import com.github.unidbg.LibraryResolver;
import com.github.unidbg.Module;
import com.github.unidbg.Symbol;
import com.github.unidbg.linux.android.AndroidARMEmulator;
import com.github.unidbg.linux.android.AndroidResolver;
import com.github.unidbg.pointer.UnicornPointer;
import com.github.unidbg.utils.Inspector;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.Random;

public class UnpackTest extends EmulatorTest {

    @Override
    protected LibraryResolver createLibraryResolver() {
        return new AndroidResolver(19);
    }

    public void testShellDemo() throws Exception {
        File elfFile = new File("src/test/resources/unpack/libdemo.so");
        String name = FilenameUtils.getBaseName(elfFile.getName());
        byte[] unpacked = emulator.getMemory().unpack(elfFile);
        File unpackedFile = new File(elfFile.getParentFile(), name + "_unpacked." + FilenameUtils.getExtension(elfFile.getName()));
        FileUtils.writeByteArrayToFile(unpackedFile, unpacked);
        System.out.println(unpackedFile);
    }

    public void testUnpackVerify() throws Exception {
        File elfFile = new File("src/test/resources/unpack/libverify.so");
        String name = FilenameUtils.getBaseName(elfFile.getName());
        byte[] unpacked = emulator.getMemory().unpack(elfFile);
        File unpackedFile = new File(elfFile.getParentFile(), name + "_unpacked." + FilenameUtils.getExtension(elfFile.getName()));
        FileUtils.writeByteArrayToFile(unpackedFile, unpacked);
        System.out.println(unpackedFile);
    }

    public void testUnpackCrackMe() throws Exception {
        File elfFile = new File("src/test/resources/unpack/libhoudini.so");
        byte[] unpacked = emulator.getMemory().unpack(elfFile);
        File unpackedFile = new File(elfFile.getParentFile(), FilenameUtils.getBaseName(elfFile.getName()) + "_unpacked." + FilenameUtils.getExtension(elfFile.getName()));
        FileUtils.writeByteArrayToFile(unpackedFile, unpacked);
        System.out.println(unpackedFile);
    }

    public void testPrintf() throws Exception {
        emulator.getMemory().setCallInitFunction();
        Module module = emulator.getMemory().dlopen("libc.so");
        Number pid = module.callFunction(emulator, "getpid")[0];
        UnicornPointer programName = UnicornPointer.pointer(emulator, module.findSymbolByName("__progname").getAddress());
        assertNotNull(programName);
        module.callFunction(emulator, "printf", "test printf: %d, pid=%d, name=%s\n", new Random().nextInt(), pid, programName.getPointer(0));

        Number[] numbers = module.callFunction(emulator, "__system_property_get", "dns1", new byte[92]);
        System.out.println("dns1 ret=" + numbers[0].intValue() + ", length=" + numbers.length);

        numbers = module.callFunction(emulator, "__system_property_get", "libc.debug.malloc", new byte[92]);
        System.out.println("libc.debug.malloc ret=" + numbers[0].intValue());

        Symbol gMallocDebugLevel = module.findSymbolByName("gMallocDebugLevel");
        assertNotNull(gMallocDebugLevel);
        byte[] data = emulator.getUnicorn().mem_read(gMallocDebugLevel.getAddress(), 4);
        Inspector.inspect(data, "gMallocDebugLevel");
    }

    @Override
    protected Emulator createARMEmulator() {
        return new AndroidARMEmulator(ManagementFactory.getRuntimeMXBean().getName().split("@")[1]);
    }
}
