// © 2016 and later: Unicode, Inc. and others.
// License & terms of use: http://www.unicode.org/copyright.html

#include <iostream>
#include "toolutil.h"
#include "uoptions.h"
#include "cmemory.h"
#include "unicode/uchar.h"
#include "unicode/errorcode.h"
#include "unicode/uniset.h"
#include "unicode/putil.h"

U_NAMESPACE_USE

/*
 * Global - verbosity
 */
UBool VERBOSE = FALSE;
UBool QUIET = FALSE;

UBool haveCopyright=TRUE;

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

    std::cout << "hello from upropdump" << std::endl;
    for (int i=0; i<argc; i++) {
        std::cout << "arg" << i << ": " << argv[i] << std::endl;
    }

    ErrorCode status;
    const USet* whitespace = u_getBinaryPropertySet(UCHAR_WHITE_SPACE, status);
    const UnicodeSet* uniset = UnicodeSet::fromUSet(whitespace);
    for (int32_t i=0; i<uniset->getRangeCount(); i++) {
        std::cout << "range: " << uniset->getRangeStart(i) << " - " << uniset->getRangeEnd(i) << std::endl;
    }
}
