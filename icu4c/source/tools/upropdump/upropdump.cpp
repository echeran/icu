// © 2016 and later: Unicode, Inc. and others.
// License & terms of use: http://www.unicode.org/copyright.html

#include <iostream>
#include "toolutil.h"
#include "uoptions.h"
#include "cmemory.h"
#include "charstr.h"
#include "unicode/uchar.h"
#include "unicode/errorcode.h"
#include "unicode/uniset.h"
#include "unicode/putil.h"
#include "unicode/umutablecptrie.h"
#include "writesrc.h"

U_NAMESPACE_USE

/*
 * Global - verbosity
 */
UBool VERBOSE = FALSE;
UBool QUIET = FALSE;

UBool haveCopyright = TRUE;
UCPTrieType trieType = UCPTRIE_TYPE_SMALL;

void handleError(const ErrorCode& status, const char* context) {
    if (status.isFailure()) {
        std::cerr << "Error: " << context << ": " << status.errorName() << std::endl;
        exit(status.get());
    }
}

void dumpBinaryProperty(UProperty uproperty, FILE* f) {
    ErrorCode status;
    const char* fullPropName = u_getPropertyName(uproperty, U_LONG_PROPERTY_NAME);
    const char* shortPropName = u_getPropertyName(uproperty, U_SHORT_PROPERTY_NAME);
    const USet* uset = u_getBinaryPropertySet(uproperty, status);
    handleError(status, fullPropName);

    fputs("[unicode_set.data]\n", f);
    fprintf(f, "long_name = \"%s\"\n", fullPropName);
    usrc_writeUnicodeSet(f, shortPropName, uset, UPRV_TARGET_SYNTAX_TOML);
}

void dumpEnumeratedProperty(UProperty uproperty, FILE* f) {
    ErrorCode status;
    const char* fullPropName = u_getPropertyName(uproperty, U_LONG_PROPERTY_NAME);
    const char* shortPropName = u_getPropertyName(uproperty, U_SHORT_PROPERTY_NAME);
    const UCPMap* umap = u_getIntPropertyMap(uproperty, status);
    handleError(status, fullPropName);

    fputs("[code_point_map.data]\n", f);
    fprintf(f, "long_name = \"%s\"\n", fullPropName);
    usrc_writeUCPMap(f, shortPropName, umap, uproperty, UPRV_TARGET_SYNTAX_TOML);
    fputs("\n", f);

    LocalUMutableCPTriePointer builder(umutablecptrie_fromUCPMap(umap, status));
    LocalUCPTriePointer utrie(umutablecptrie_buildImmutable(
        builder.getAlias(),
        trieType,
        UCPTRIE_VALUE_BITS_32, // TODO(review): What is the best way to pick the value width?
        status));
    handleError(status, fullPropName);

    fputs("[code_point_trie.struct]\n", f);
    fprintf(f, "long_name = \"%s\"\n", fullPropName);
    usrc_writeUCPTrie(f, shortPropName, utrie.getAlias(), UPRV_TARGET_SYNTAX_TOML);
}

enum {
    OPT_HELP_H,
    OPT_HELP_QUESTION_MARK,
    OPT_COPYRIGHT,
    OPT_VERSION,
    OPT_DESTDIR,
    OPT_VERBOSE,
    OPT_QUIET,

    OPT_COUNT
};

static UOption options[]={
    UOPTION_HELP_H,
    UOPTION_HELP_QUESTION_MARK,
    UOPTION_COPYRIGHT,
    UOPTION_VERSION,
    UOPTION_DESTDIR,
    UOPTION_VERBOSE,
    UOPTION_QUIET,
};

int main(int argc, char* argv[]) {

    U_MAIN_INIT_ARGS(argc, argv);

    /* preset then read command line options */
    options[OPT_DESTDIR].value=u_getDataDirectory();
    argc=u_parseArgs(argc, argv, UPRV_LENGTHOF(options), options);

    if(options[OPT_VERSION].doesOccur) {
        printf("upropdump version %s, ICU tool to write Unicode property .toml files\n",
               U_ICU_DATA_VERSION);
        printf("%s\n", U_COPYRIGHT_STRING);
        exit(0);
    }

    /* error handling, printing usage message */
    if(argc<0) {
        fprintf(stderr,
            "error in command line argument \"%s\"\n",
            argv[-argc]);
    } else if(argc<2) {
        argc=-1;
    }

    if(argc<0 || options[OPT_HELP_H].doesOccur || options[OPT_HELP_QUESTION_MARK].doesOccur) {
        FILE *stdfile=argc<0 ? stderr : stdout;
        fprintf(stdfile,
            "usage: %s [-options] properties...\n"
            "\tdump Unicode property data to .toml files\n"
            "options:\n"
            "\t-h or -? or --help  this usage text\n"
            "\t-V or --version     show a version message\n"
            "\t-c or --copyright   include a copyright notice\n"
            "\t-d or --destdir     destination directory, followed by the path\n"
            "\t-v or --verbose     Turn on verbose output\n"
            "\t-q or --quiet       do not display warnings and progress\n",
            argv[0]);
        return argc<0 ? U_ILLEGAL_ARGUMENT_ERROR : U_ZERO_ERROR;
    }

    /* get the options values */
    haveCopyright = options[OPT_COPYRIGHT].doesOccur;
    const char *destdir = options[OPT_DESTDIR].value;
    VERBOSE = options[OPT_VERBOSE].doesOccur;
    QUIET = options[OPT_QUIET].doesOccur;

    auto firstPropName = argv[1];
    if (strcmp(firstPropName, "planes") == 0) {
        // Create a UCPTrie that represents the Unicode plane number (0-16)
        // for each code point in the Unicode character space.

        UErrorCode errorCode = U_ZERO_ERROR;
        icu::LocalUMutableCPTriePointer mutableTrie(
            umutablecptrie_open(0, 0, &errorCode));
        if (U_FAILURE(errorCode)) { exit(1); }

        const int32_t plane_size = 65536; // 2^16
        for (int8_t plane = 0; plane < 17; plane++) {
            int32_t start = plane * plane_size;
            int32_t end = ((plane + 1) * plane_size) - 1;
            umutablecptrie_setRange(mutableTrie.getAlias(), start, end, plane, &errorCode);
        }

        UCPTrieType type = UCPTRIE_TYPE_SMALL;
        UCPTrieValueWidth valueWidth = UCPTRIE_VALUE_BITS_8;
        UCPTrie *trie = umutablecptrie_buildImmutable(mutableTrie.getAlias(), type, valueWidth, &errorCode);
        UCPMap *umap = reinterpret_cast<UCPMap *>(trie);
        if (U_FAILURE(errorCode)) { exit(1); }

        // duplicating output file code from below
        CharString outFileName;
        if (destdir != nullptr || *destdir != 0) {
            outFileName.append(destdir, errorCode).ensureEndsWithFileSeparator(errorCode);
        }
        outFileName.append("planes", errorCode);
        outFileName.append(".toml", errorCode);
        FILE* f = fopen(outFileName.data(), "w");
        if (f == nullptr) {
            std::cerr << "Unable to open file: " << outFileName.data() << std::endl;
            exit(1);
        }
        if (!QUIET) {
            std::cout << "Writing to: " << outFileName.data() << std::endl;
        }

        ErrorCode status;
        // duplicating logic from dumpEnumeratedProperty to dump UCPTrie but 
        // not associated with an actual Unicode property.
        const char* fullPropName = "planes";
        const char* shortPropName = "planes";
        handleError(status, fullPropName);
        fputs("[code_point_map.data]\n", f);
        fprintf(f, "long_name = \"%s\"\n", fullPropName);
        // fake Unicode property implies UProperty enum for invalid property
        usrc_writeUCPMap(f, shortPropName, umap, UCHAR_INVALID_CODE, UPRV_TARGET_SYNTAX_TOML);
        fputs("\n", f);
        fputs("[code_point_trie.struct]\n", f);
        fprintf(f, "long_name = \"%s\"\n", fullPropName);
        LocalUCPTriePointer utrie(trie);
        usrc_writeUCPTrie(f, shortPropName, utrie.getAlias(), UPRV_TARGET_SYNTAX_TOML);

        exit(0);
    }

    for (int i=1; i<argc; i++) {
        auto propName = argv[i];
        UProperty propEnum = u_getPropertyEnum(propName);
        if (propEnum == UCHAR_INVALID_CODE) {
            std::cerr << "Error: Invalid property alias: " << propName << std::endl;
            exit(1);
        }

        ErrorCode status;
        CharString outFileName;
        if (destdir != nullptr || *destdir != 0) {
            outFileName.append(destdir, status).ensureEndsWithFileSeparator(status);
        }
        outFileName.append(propName, status);
        outFileName.append(".toml", status);
        handleError(status, propName);

        FILE* f = fopen(outFileName.data(), "w");
        if (f == nullptr) {
            std::cerr << "Unable to open file: " << outFileName.data() << std::endl;
            exit(1);
        }
        if (!QUIET) {
            std::cout << "Writing to: " << outFileName.data() << std::endl;
        }

        if (haveCopyright) {
            usrc_writeCopyrightHeader(f, "#", 2021);
        }
        usrc_writeFileNameGeneratedBy(f, "#", propName, "upropdump.cpp");

        if (propEnum < UCHAR_BINARY_LIMIT) {
            dumpBinaryProperty(propEnum, f);
        } else {
            dumpEnumeratedProperty(propEnum, f);
        }

        fclose(f);
    }
}
