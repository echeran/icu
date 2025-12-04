---
title: What is ICU?
description: A brief description of International Components for Unicode (ICU)
---

## You're already using it

If you are reading this on a device that can display different languages,
including the language differences across different geographical regions,
then you are very likely using International Components for Unicode (ICU) right now.
This is true whether you are using a mobile device (phone, tablet),
any of the popular web browsers (on desktop/laptop computers and mobile),
and most devices running a major operating system (on desktop/laptop and mobile).

## ICU and Unicode and internationalization

The [Unicode Standard](http://unicode.org/) exists to define a way in which computers communicate multilingual text unambiguously.
There are more [objectives an design principles](https://home.unicode.org/technical-quick-start-guide/) beyond that,
and Unicode also contains additional data and algorithms to those ends.

ICU is Unicode's long-standing software project that provides the executable code,
and bundled Unicode data and CLDR,
in a single software library.
It stays the most up-to-date with data and algorithm changes from both the Unicode standard and CLDR locale-specific data.

In the ecosystem of language technology that is concerned with internationalization ("i18n"), globalization ("g11n"), localization ("l10n"), and translation ("t9n"),
ICU remains one of the most prominent libraries that applications depend on
design their software in appropriate, maintainable ways to serve their end users's multilingual global experiences.

## How ICU gets used

The ICU codebase contains the implementations in Java, C, and C++.
We sometimes refer to the Java implementation as ICU4J,
and to the C and C++ implementation as ICU4C.
The C/C++ implementation allows users of other programming languages
to create their own wrapper libraries of ICU,
such as PyICU being a Python library that exposes ICU functionality
in an easy to use way for Python applications.

In some cases, ICU comes preinstalled and ready to use for application developers,
such as in Android.
In other cases, ICU does not come preinstalled,
but it can be installed and made available through a system package manager,
like in Linux and other Unix-like operating systems.
There are operating systems like Windows, macOS, and iOS that include ICU
internally to support many common operations,
but do not expose that system copy to application developers.
Note, however, that when applications running on those operating systems are using internationalization features,
they may be interacting with ICU indirectly.
Similarly, popular web browsers will come bundled with an internal copy of ICU,
and they make some of ICU's algorithmic functions and data availble indirectly
by using ICU to implement their own embedded Javascript runtime engines.
