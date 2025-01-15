---
layout: default
title: Trying MF 2.0 Final Candidate
nav_order: 3
parent: Formatting Messages
grand_parent: Formatting
---
<!--
© 2023 and later: Unicode, Inc. and others.
License & terms of use: http://www.unicode.org/copyright.html
-->

# Trying MF 2.0 Final Candidate
{: .no_toc }

## Contents
{: .no_toc .text-delta }

1. TOC
{:toc}

---

# Hello `MessageFormat2`




## C++ Linux & macOS


1. Prepare a sandbox folder
    ```sh
    export ICU_SANDBOX=~/hello_icu_mf2
    mkdir $ICU_SANDBOX
    cd $ICU_SANDBOX
    ```

1. Build ICU4C (you only need to do this once)


    ```sh
    git clone https://github.com/unicode-org/icu.git
    pushd icu/icu4c/source

    # Run this and choose the platform and toolchain you prefer
    ./runConfigureICU --help

    # if macOS
    ./runConfigureICU macOS/gcc
    # else if Linux  (gcc is just an example, there are 5 Linux options)
    ./runConfigureICU Linux/gcc
    # end

    export DESTDIR=$ICU_SANDBOX/icu_release
    make -j8 releaseDist
    popd
    ```

1. Create a minimal C++ file here (in the $$ICU_SANDBOX folder).
Let's call it `hello_mf2.cpp`
    ```cpp
    // hello_mf2.cpp
    #include <iostream>

    #include "unicode/utypes.h"
    #include "unicode/calendar.h"
    #include "unicode/errorcode.h"
    #include "unicode/locid.h"
    #include "unicode/messageformat2.h"

    using namespace icu;

    int main() {
       ErrorCode errorCode;
       UParseError parseError;

       icu::Calendar* cal(Calendar::createInstance(errorCode));
       cal->set(2025, Calendar::JANUARY, 28);
       UDate date = cal->getTime(errorCode);

       message2::MessageFormatter::Builder builder(errorCode);
       message2::MessageFormatter mf = builder
           .setPattern("Hello {$user}, today is {$now :date style=long}!", parseError, errorCode)
           .setLocale(Locale("en_US"))
           .build(errorCode);

       std::map<UnicodeString, message2::Formattable> argsBuilder;
       argsBuilder["user"] = message2::Formattable("John");
       argsBuilder["now"] = message2::Formattable::forDate(date);
       message2::MessageArguments arguments(argsBuilder, errorCode);

       icu::UnicodeString result = mf.formatToString(arguments, errorCode);
       std::string strResult;
       result.toUTF8String(strResult);
       std::cout << strResult << std::endl;
    }
    ```

1. Build your application and run it
    ```sh
    g++ hello_mf2.cpp -I$DESTDIR/usr/local/include -std=c++17 -L$DESTDIR/usr/local/lib -licuuc -licudata -licui18n

    # if macOS
    DYLD_LIBRARY_PATH=$DESTDIR/usr/local/lib ./a.out
    # else if Linux
    LD_LIBRARY_PATH=$DESTDIR/usr/local/lib ./a.out
    # end
    ```

## C++ Windows


WORK in PROGRESS


## Java

We will assume that if you are interested in testing a pre-release Java library
you already have (or know how to install) a JDK, Apache Maven, git,
know how to create a project in your favorite IDE, and so on.

### What you need

* JDK, any version between 8 and 23
* Apache Maven

### Instructions

1. Create a new Maven project
    ```sh
    mvn archetype:generate -DgroupId=com.mycompany.app -DartifactId=hello_icu_mf2 -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.5 -DinteractiveMode=false

    cd hello_icu_mf2
    ```

1. Edit the `pom.xml` file

    1. The project created as above uses the Java 17 version.
    If you are using a lower version then change `<maven.compiler.release>` property to whatever Java version you are using.
        ```sh
        # Example on how to set the Java version to 11
        mvn versions:set-property -Dproperty=maven.compiler.release -DnewVersion=11
        ```

    1. Add this to `<dependencies>`
        ```xml
        <dependency>
          <groupId>com.ibm.icu</groupId>
          <artifactId>icu4j</artifactId>
          <version>77.0.1-SNAPSHOT</version>
        </dependency>
        ```
        **Warning:** make sure it is done in `dependencies`, not in `dependencyManagement / dependencies`

1. Change the `src/test/java/com/mycompany/app/AppTest.java` file
    1. Add a new test method
        ```java
        @Test
        public void testMessageFormat2() {
           MessageFormatter mf2 = MessageFormatter.builder()
               .setLocale(Locale.US)
               .setPattern("Hello {$user}, today is {$now :date style=long}!")
               .build();
           Calendar cal = Calendar.getInstance();
           cal.set(2025, 0, 28);


           Map<String, Object> arguments = new HashMap<>();
           arguments.put("user", "John");
           arguments.put("now", cal);
           System.out.println(mf2.formatToString(arguments));
        }
        ```
    1. Add imports
        ```java
        import java.util.Date;
        import java.util.HashMap;
        import java.util.Locale;
        import java.util.Map;
        import com.ibm.icu.message2.MessageFormatter;
        ```

1. Now run the tests
    ```sh
    mvn package -q
    ```

1. This will output
    ```
    Hello John, today is January 13, 2025!
    ```
