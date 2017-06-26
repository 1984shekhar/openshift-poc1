package com.navitas.ospoc.common.file;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;

import com.navitas.ospoc.common.file.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;


public class FileUtilsTest {

    private static final Logger LOG = LoggerFactory.getLogger(FileUtilsTest.class);

    @Test
    public void TC050_streamToString_whenInvoked_thenReturnsString() {
        LOG.info("TC050_streamToString_whenInvoked_thenReturnsString");

        String origStr = "Mary had a little lamb,\\nIt fell out of a tree and broke its watch.";
        InputStream is = new ByteArrayInputStream(origStr.getBytes());
        String resultStr = FileUtils.streamToString(is);
        Assert.assertNotNull("streamToString returned null!",resultStr);
        Assert.assertEquals("String didn't survive translation from InputStream",origStr,resultStr);
    }

    @Test
    public void TC060_readFile_whenInvokedWithFileRef_thenReturnStringWithFileContents() {
        LOG.info("TC060_readFile_whenInvokedWithFileRef_thenReturnStringWithFileContents");
        String resultStr = FileUtils.readFile("test-data/file-utils/FileUtilsTest060_input.txt");
        Assert.assertNotNull("streamToString returned null!",resultStr);
        Assert.assertTrue("String didn't survive translation from InputStream",
                resultStr.matches("Mary had a little lamb,[\\n\\r]+Its fleece was sort of spotty,[\\n\\r]+Then a cat ate it.[\\n\\r]*"));

    }

}
