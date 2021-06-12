// © 2016 and later: Unicode, Inc. and others.
// License & terms of use: http://www.unicode.org/copyright.html

#include <iostream>

int main(int argc, char* argv[]) {
    std::cout << "hello from upropdump" << std::endl;
    for (int i=0; i<argc; i++) {
        std::cout << "arg" << i << ": " << argv[i] << std::endl;
    }
}
