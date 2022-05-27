// © 2016 and later: Unicode, Inc. and others.
// License & terms of use: http://www.unicode.org/copyright.html
/*
 *******************************************************************************
 * Copyright (C) 1996-2014, International Business Machines Corporation and
 * others. All Rights Reserved.
 *******************************************************************************
 */

package com.ibm.icu.dev.test.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.MissingResourceException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.ibm.icu.dev.test.TestFmwk;
import com.ibm.icu.impl.ICUBinary;
import com.ibm.icu.util.TimeZone;

/**
* Testing class for Trie. Tests here will be simple, since both CharTrie and
* IntTrie are very similar and are heavily used in other parts of ICU4J.
* Codes using Tries are expected to have detailed tests.
* @author Syn Wee Quek
* @since release 2.1 Jan 01 2002
*/
@RunWith(JUnit4.class)
public final class ICUBinaryTest extends TestFmwk
{
    // constructor ---------------------------------------------------

    /**
    * Constructor
    */
    public ICUBinaryTest()
    {
    }

    // public methods -----------------------------------------------

    /**
     * Testing the constructors of the Tries
     */
    @Test
    public void TestReadHeader()
    {
        int formatid = 0x01020304;
        byte array[] = {
            // header size
            0, 0x18,
            // magic numbers
            (byte)0xda, 0x27,
            // size
            0, 0x14,
            // reserved word
            0, 0,
            // bigendian
            1,
            // charset
            0,
            // charsize
            2,
            // reserved byte
            0,
            // data format id
            1, 2, 3, 4,
            // dataVersion
            1, 2, 3, 4,
            // unicodeVersion
            3, 2, 0, 0
        };
        ByteBuffer bytes = ByteBuffer.wrap(array);
        ICUBinary.Authenticate authenticate
                = new ICUBinary.Authenticate() {
                    @Override
                    public boolean isDataVersionAcceptable(byte version[])
                    {
                        return version[0] == 1;
                    }
                };
        // check full data version
        try {
            ICUBinary.readHeader(bytes, formatid, authenticate);
        } catch (IOException e) {
            errln("Failed: Lenient authenticate object should pass ICUBinary.readHeader");
        }
        // no restriction to the data version
        try {
            bytes.rewind();
            ICUBinary.readHeader(bytes, formatid, null);
        } catch (IOException e) {
            errln("Failed: Null authenticate object should pass ICUBinary.readHeader");
        }
        // lenient data version
        array[17] = 9;
        try {
            bytes.rewind();
            ICUBinary.readHeader(bytes, formatid, authenticate);
        } catch (IOException e) {
            errln("Failed: Lenient authenticate object should pass ICUBinary.readHeader");
        }
        // changing the version to an incorrect one, expecting failure
        array[16] = 2;
        try {
            bytes.rewind();
            ICUBinary.readHeader(bytes, formatid, authenticate);
            errln("Failed: Invalid version number should not pass authenticate object");
        } catch (IOException e) {
            logln("PASS: ICUBinary.readHeader with invalid version number failed as expected");
        }
    }


    @Test
    public void TestElangoDatFileTest8() {
/*
 * This adds a .dat file at: <ICU>/icu4j/main/tests/core/src/com/ibm/icu/dev/data/elangorb/test8/test8dtl.dat
 * combines the 2 .res files together
 *
 * Commands to make the .dat file:
 *
 * LD_LIBRARY_PATH=../lib ./icupkg new ~/tmp/icupkg/test8/test8dtl.dat
 * LD_LIBRARY_PATH=../lib ./icupkg -tl -a ~/tmp/genrb/test7/root.res ~/tmp/icupkg/test8/test8dtl.dat
 * LD_LIBRARY_PATH=../lib ./icupkg -tl -a ~/tmp/genrb/test7/en.res ~/tmp/icupkg/test8/test8dtl.dat
 *
 * It also downloads the TZ files for some version of ICU, then it edits, via a hex editor,
 * the zoneinfo64.res file to change the string America/Los_Angeles to America/LostBangles
 *
 *
 * Q: Why are the ICU data files in ICU4J big-endian?  In the ICU4J Eclipse project
 * icu-shared, in the jar at the FS path data/icudata.jar (full path:
 * icu4j/main/shared/data/icudata.jar), the classpath path for all the resources
 * begins with /com/ibm/icu/impl/data/icudt72b/zone/.
 * The User Guide info on ICU Data says that means it's big-endian:
 * https://unicode-org.github.io/icu/userguide/icu_data/#sharing-icu-data-between-platforms
 * So that says the file is big-endian.  But aren't most systems little-endian
 * these days?  I understand that Java 7 NIO with ByteBuffer allows a byte stream
 * to read multi-byte values as LE or BE without extra code (and performantly?),
 * and I haven't yet seen equivalent ICU4C code but I assume it's there, too.
 */


        try {
            // Load a .dat file??

            // path to Java resource on classpath that is a ICU data file (.dat)
            String datPath = "com/ibm/icu/dev/data/elangorb/test8/test8dtl.dat";

            // This way to load a .dat file works
            ByteBuffer datFileBuf1 = ICUBinary.getByteBufferFromInputStreamAndCloseStream(
                    this.getClass().getClassLoader().getResourceAsStream(datPath));

            // However, once you load a .dat file (or any other file) as a ByteBuffer
            // using ICUBinary, there is actually no public way to read the format.
            //
            // A ByteBuffer representing a .res file is interpreted through an instance of
            // ICUResourceBundleReader, and those instances are cached in the singleton variable
            // called CACHE defined in ICUResourceBundleReader.java.  That is of type
            // ReaderCache, which, when it implements the createInstance method for cache key misses,
            // assumes that the resource is an ICU resource (it prepends the key with the ICU
            // data .dat file's classpath directory prefix).
            //
            // A ByteBuffer representing a .dat file is interpreted through DatPackageReader
            // in ICUBinary.java.  But the entire class is a private inner class. It is invoked
            // only when static initialization code in ICUBinary.java creates and populates a private static
            // field `icuDataFiles`, which stores any of the individual resource files and extracted
            // resource files from any .dat files from the path provided by the user in ICUConfig.properties.
            // The contents of such files are only accessed by the public methods
            // ICUBinary.getData* or getRequiredData, which use a private helper method that first loads
            // a requested itemPath from the `icuDataFiles` first, and upon failure continues
            // to looking up data from the ICU resource path.

            // This way to won't work unless the directory in the file system
            // containing the file(s) of interest is specified in
            // ICUConfig.properties and if the files can pass DatPackageReader.validate(),
            // which in turn requires that the resource filename within the .dat file's
            // binary contents begins with ICUData.PACKAGE_NAME.
            //
            // Q: How would I need to set up the resource file paths (directory names)
            // and file names such that the .dat file store the name for the resource
            // such that it begins with ICUData.PACKAGE_NAME (icudt72b.dat)?
            ByteBuffer datFileBuf2 = ICUBinary.getData(datPath);

//            DatPackageReader.validate();

            // modifying the timezone name in zoneinfo64.res from "America/Los_Angeles"
            // to "America/LostBangles" doesn't work -- we suspect it is because that
            // identifier is being used to join some information together, so it is connected
            // to other things in a way that TimeZone.getTimeZone() probably needs somehow
//            TimeZone tz1 = TimeZone.getTimeZone("America/Los_Angeles");
//            TimeZone tz2 = TimeZone.getTimeZone("America/LostBangles");

            // an unchanged time zone in the modified zoneinfo64.res file should still
            // work as before
            TimeZone tz3 = TimeZone.getTimeZone("America/Mexico_City");
        }
        catch (MissingResourceException ex) {
            warnln("could not load test data: " + ex.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void TestElangoDatFileTest9() {
/*
 * This only adds the TZ files for some verison of ICU and edits timezoneTypes.res
 * to change one instance in the file of the string "Australia/Brisbane" to
 * "Aluminium/Briskets".
 * And in metaZones.res, changing "America/Port_of_Spain" to "America/Port_of_Drain".
 *
 * Then, it temporarily sets ICUConfig.properties to point to the directory
 * /usr/local/google/home/elango/oss/icu/icu4j/main/tests/core/src/com/ibm/icu/dev/data/elangorb/test9/
 * and then tries to load data for that time zone id, which should pick it up from a successful reading
 * of the modified files (so long as they are internally consistent).
 *
 * Examples of raw plain text versions of TZ update data is in the icu-data repository at
 * https://github.com/unicode-org/icu-data/blob/main/tzdata/supplemental/*, etc.
 */


        try {
            TimeZone tz1 = TimeZone.getTimeZone("Australia/Brisbane");
            TimeZone tz2 = TimeZone.getTimeZone("America/Port_of_Spain");
        }
        catch (MissingResourceException ex) {
            warnln("could not load test data: " + ex.getMessage());
        }
    }




}
