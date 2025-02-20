## Collected 0 commit(s) to exclude
<!--
Copyright (C) 2021 and later: Unicode, Inc. and others.
License & terms of use: http://www.unicode.org/copyright.html
-->
Commit Report
=============

Environment:
- Now: 2025-02-20T15:00:36.868219
- Latest Commit: https://github.com/unicode-org/icu/commit/2339ae4c0d2b94716124fabb4f77fadd049189e7
- Jira Query: `project=ICU AND fixVersion=77.1`
- Rev Range: `release-76-1..upstream/main`
- Authenticated: `Yes`

-----
-----
_(anything between the above two lines is an error)_

Total problem(s): 6

## Table Of Contents
Note: empty categories are omitted.
- _Closed Issues with No Commit_
- [Closed Issues with Commit Policy Problems](#closed-issues-with-commit-policy-problems) 2
- _Commits without Jira Issue Tag_
- _Commits with Jira Issue Not Found_
- [Commits with Open Jira Issue](#commits-with-open-jira-issue) 6
- _Issue is under Review_
- _Excluded Commits_

## Problem Categories
### Closed Issues with Commit Policy Problems
[🔝Top](#table-of-contents)

_2 item(s)_
Tip: Fixed tickets should have resolution 'Fixed by Other Ticket' or 'Fixed'.
Duplicate tickets should have their fixVersion tag removed.

- [ICU-22977](https://unicode-org.atlassian.net/browse/ICU-22977): `CI fails for all windows-msvc- targets`
	- _Closed Issues with Commit Policy Problems_
	- Assigned to Mihai Nita
	- Status: Done
	- Resolution: Done
	- Fix Version: 77.1
	- Component(s): build_c
	- INTERNAL ERROR: resolution ID# 10026 is unknown, not in all_resolutions[]. Fix the commit checker.
- [ICU-22967](https://unicode-org.atlassian.net/browse/ICU-22967): `Work towards removing the need for git lfs`
	- _Closed Issues with Commit Policy Problems_
	- Assigned to Mihai Nita
	- Status: Done
	- Resolution: Done
	- Fix Version: 77.1
	- Component(s): team_processes_tools
	- INTERNAL ERROR: resolution ID# 10026 is unknown, not in all_resolutions[]. Fix the commit checker.

### Commits with Open Jira Issue
[🔝Top](#table-of-contents)

_6 item(s)_
Tip: Consider closing the ticket if it is fixed.

#### Open Issues by Component

 - **format_message**: [ICU-22907](#issue-icu-22907)
 - **icuio**: [ICU-22808](#issue-icu-22808)
 - **others**: [ICU-22921](#issue-icu-22921) [ICU-22920](#issue-icu-22920)
 - **team_processes_tools**: [ICU-22922](#issue-icu-22922)
 - **time_calc**: [ICU-23043](#issue-icu-23043)


#### Issue ICU-22808

_Jira issue is open_
- [ICU-22808](https://unicode-org.atlassian.net/browse/ICU-22808): `ICU4C u_fgetcx incorrect documentation`
	- Assigned to Rich Gillam
	- Status: Accepted
	- Fix Version: 77.1
	- Component(s): icuio

##### Commits with Issue ICU-22808

- [b81956e](https://github.com/unicode-org/icu/commit/b81956ebcc96d99a4aa0c134c7b6c4225b976570) [ICU-22808](https://unicode-org.atlassian.net/browse/ICU-22808) `Correct a factually incorrect doc comment.`
	- Authored by David Barts <david.w.barts@gmail.com>
	- Committed at 2025-02-12T16:37:33-08:00

#### Issue ICU-22907

_Jira issue is open_
- [ICU-22907](https://unicode-org.atlassian.net/browse/ICU-22907): `MF2: ICU4C doesn't implement test functions from default registry`
	- Assigned to Tim Chevalier
	- Status: Accepted
	- Fix Version: 77.1
	- Component(s): format_message

##### Commits with Issue ICU-22907

- [7b8110f](https://github.com/unicode-org/icu/commit/7b8110f0039fe137425f7b6acd7f5f9217cc32ed) [ICU-22907](https://unicode-org.atlassian.net/browse/ICU-22907) `MF2: Finish updating spec tests and implement required test functions`
	- Authored by Tim Chevalier <tjc@igalia.com>
	- Committed at 2025-02-08T21:42:03-06:00

#### Issue ICU-22920

_Jira issue is open_
- [ICU-22920](https://unicode-org.atlassian.net/browse/ICU-22920): `ICU 77 code warnings/version updates`
	- Assigned to Markus Scherer
	- Status: Accepted
	- Fix Version: 77.1
	- Component(s): others

##### Commits with Issue ICU-22920

- [5798405](https://github.com/unicode-org/icu/commit/579840539f37374e680477424e369200505c9131) [ICU-22920](https://unicode-org.atlassian.net/browse/ICU-22920) `Bump the github-actions group across 1 directory with 2 updates`
	- Authored by Elango Cheran <elango@unicode.org>
	- Committed at 2025-02-19T19:54:43+01:00

- [c9ca3cd](https://github.com/unicode-org/icu/commit/c9ca3cd5544eab3992a449aca1675c920c8140d0) [ICU-22920](https://unicode-org.atlassian.net/browse/ICU-22920) `Migrate from setup-bazelisk to setup-bazel.`
	- Authored by Fredrik Roubert <roubert@google.com>
	- Committed at 2025-02-11T19:27:07+01:00

- [fb64693](https://github.com/unicode-org/icu/commit/fb64693c281b97b2c7ce9619fda32f03a9e18235) [ICU-22920](https://unicode-org.atlassian.net/browse/ICU-22920) `Avoid "return by const value" antipattern`
	- Authored by Arthur O'Dwyer <arthur.j.odwyer@gmail.com>
	- Committed at 2025-01-30T10:58:25-08:00

- [ba012a7](https://github.com/unicode-org/icu/commit/ba012a74a11405a502b6890e710bfb58cef7a2c7) [ICU-22920](https://unicode-org.atlassian.net/browse/ICU-22920) `Fix raw type warnings in icu4j tests: charset, common_tests, translit`
	- Authored by Mihai Nita <nmihai_2000@yahoo.com>
	- Committed at 2024-12-17T16:14:38-08:00

- [4ff5d6a](https://github.com/unicode-org/icu/commit/4ff5d6a0703f733eb34b329a443e221858c230bb) [ICU-22920](https://unicode-org.atlassian.net/browse/ICU-22920) `Fix raw type warnings in icu4j core tests`
	- Authored by Mihai Nita <nmihai_2000@yahoo.com>
	- Committed at 2024-12-15T01:18:47-08:00

- [e38ac30](https://github.com/unicode-org/icu/commit/e38ac306bc139b9e64eea8bbbc83899876be620f) [ICU-22920](https://unicode-org.atlassian.net/browse/ICU-22920) `fix exhaustive tests for likely subtags failure ICU-22976`
	- Authored by Craig Cornelius <cwcornelius@gmail.com>
	- Committed at 2024-11-21T14:47:56-08:00

- [842899d](https://github.com/unicode-org/icu/commit/842899d81ae464700aa8413117131fc225710946) [ICU-22920](https://unicode-org.atlassian.net/browse/ICU-22920) `Only run Maven cache workflow on the upstream repo`
	- Authored by Elango Cheran <elango@unicode.org>
	- Committed at 2024-11-05T15:16:00-08:00

#### Issue ICU-22921

_Jira issue is open_
- [ICU-22921](https://unicode-org.atlassian.net/browse/ICU-22921): `ICU 77 docs minor fixes`
	- Assigned to Markus Scherer
	- Status: Accepted
	- Fix Version: 77.1
	- Component(s): others

##### Commits with Issue ICU-22921

- [3b2bd4b](https://github.com/unicode-org/icu/commit/3b2bd4b19dbcdb4cf1b0d94a1c4968d586b639ab) [ICU-22921](https://unicode-org.atlassian.net/browse/ICU-22921) `Fix some javadoc issues`
	- Authored by Mihai Nita <nmihai_2000@yahoo.com>
	- Committed at 2025-02-18T13:58:16-08:00

- [16e50b2](https://github.com/unicode-org/icu/commit/16e50b260f25847ecefc48ac4d9a4537b2c130ac) [ICU-22921](https://unicode-org.atlassian.net/browse/ICU-22921) `Fix broken link in userguide`
	- Authored by Taichi Haradaguchi <20001722@protonmail.com>
	- Committed at 2025-01-24T15:11:34-08:00

- [2c5e021](https://github.com/unicode-org/icu/commit/2c5e021f6d33bb5d3c091a4abf61ab5ccf15f93b) [ICU-22921](https://unicode-org.atlassian.net/browse/ICU-22921) `Add howto guide to try MF 2.0 final candidate draft impls`
	- Authored by Elango Cheran <elango@unicode.org>
	- Committed at 2025-01-15T14:22:57-08:00

- [ba5cf31](https://github.com/unicode-org/icu/commit/ba5cf31f770058ad1d3a3502de7a084a2cfd0862) [ICU-22921](https://unicode-org.atlassian.net/browse/ICU-22921) `Add windows script doing jar extraction`
	- Authored by Mihai Nita <nmihai_2000@yahoo.com>
	- Committed at 2025-01-08T13:54:42-08:00

- [d9d09db](https://github.com/unicode-org/icu/commit/d9d09db2a7167eea2b5749836ef32cdd45303fdd) [ICU-22921](https://unicode-org.atlassian.net/browse/ICU-22921) `remove redundant PR checklist item`
	- Authored by Markus Scherer <markus.icu@gmail.com>
	- Committed at 2024-12-26T11:07:30-08:00

- [0295105](https://github.com/unicode-org/icu/commit/02951053b45f270c04ffeac6e0787dd2313bbd23) [ICU-22921](https://unicode-org.atlassian.net/browse/ICU-22921) `Document a way to remove unused includes from command line`
	- Authored by Mihai Nita <nmihai_2000@yahoo.com>
	- Committed at 2024-12-12T12:22:31-08:00

- [791a052](https://github.com/unicode-org/icu/commit/791a052f8ef96805674d2558dcbc8f749f98dc71) [ICU-22921](https://unicode-org.atlassian.net/browse/ICU-22921) `fix link from gitdev to ci exhaustive tests`
	- Authored by Markus Scherer <markus.icu@gmail.com>
	- Committed at 2024-12-09T09:55:54-08:00

- [2ba362f](https://github.com/unicode-org/icu/commit/2ba362fa3b7cb8b4f4c35b016ec1c490e590b86a) [ICU-22921](https://unicode-org.atlassian.net/browse/ICU-22921) `Update PR template`
	- Authored by Elango Cheran <elango@unicode.org>
	- Committed at 2024-11-22T14:06:21-08:00

- [cd9fada](https://github.com/unicode-org/icu/commit/cd9fada30ceb3ee7c69718b938ffebb5e2e76a6b) [ICU-22921](https://unicode-org.atlassian.net/browse/ICU-22921) `PR template: move standing issues up to TODO section`
	- Authored by Markus Scherer <markus.icu@gmail.com>
	- Committed at 2024-11-22T10:00:06-08:00

- [ee8a94e](https://github.com/unicode-org/icu/commit/ee8a94e0f1de498eb196434887dc05d8c23af211) [ICU-22921](https://unicode-org.atlassian.net/browse/ICU-22921) `Rename README.md in .github`
	- Authored by Mihai Nita <nmihai_2000@yahoo.com>
	- Committed at 2024-10-17T12:44:32-07:00

#### Issue ICU-22922

_Jira issue is open_
- [ICU-22922](https://unicode-org.atlassian.net/browse/ICU-22922): `ICU 77rc BRS`
	- Assigned to Markus Scherer
	- Status: Accepted
	- Fix Version: 77.1
	- Component(s): team_processes_tools

##### Commits with Issue ICU-22922

- [2339ae4](https://github.com/unicode-org/icu/commit/2339ae4c0d2b94716124fabb4f77fadd049189e7) [ICU-22922](https://unicode-org.atlassian.net/browse/ICU-22922) `Organize import statements`
	- Authored by Mihai Nita <nmihai_2000@yahoo.com>
	- Committed at 2025-02-20T12:56:18-08:00

- [cfc208f](https://github.com/unicode-org/icu/commit/cfc208f5460d3f5e510830f0813f9bc1fb5f7a20) [ICU-22922](https://unicode-org.atlassian.net/browse/ICU-22922) `ICU4C RC APIChangeReport updates`
	- Authored by sven-oly <cwcornelius@gmail.com>
	- Committed at 2025-02-19T16:50:14-08:00

- [b42e725](https://github.com/unicode-org/icu/commit/b42e7250f0bde441b7d0f1a727d5a33aef629647) [ICU-22922](https://unicode-org.atlassian.net/browse/ICU-22922) `ICU4J RC APIChangeReport.html`
	- Authored by sven-oly <cwcornelius@gmail.com>
	- Committed at 2025-02-19T16:47:37-08:00

- [d69d452](https://github.com/unicode-org/icu/commit/d69d4523824fc1eae51bd3b3ffee9a12988e5909) [ICU-22922](https://unicode-org.atlassian.net/browse/ICU-22922) `BRS_77_RC: Update version number to 77.1-SNAPSHOT`
	- Authored by Mihai Nita <nmihai_2000@yahoo.com>
	- Committed at 2025-02-19T15:59:57-08:00

- [66c6771](https://github.com/unicode-org/icu/commit/66c6771a03487d3e7ec50af000d8df89381cc237) [ICU-22922](https://unicode-org.atlassian.net/browse/ICU-22922) `Update ICU4J status`
	- Authored by sven-oly <cwcornelius@gmail.com>
	- Committed at 2025-02-18T16:23:49-08:00

- [03021a9](https://github.com/unicode-org/icu/commit/03021a98a0131e1ee8b1c5354f5692c0cc75dc29) [ICU-22922](https://unicode-org.atlassian.net/browse/ICU-22922) `BRS77 update license file`
	- Authored by yumaoka <y.umaoka@gmail.com>
	- Committed at 2025-02-11T11:59:20-05:00

- [c4acc44](https://github.com/unicode-org/icu/commit/c4acc449ff589f52ca922152f4da7900bfcc6018) [ICU-22922](https://unicode-org.atlassian.net/browse/ICU-22922) `initial ICU 77 download page; incomplete`
	- Authored by Markus Scherer <markus.icu@gmail.com>
	- Committed at 2025-02-06T16:09:06-08:00

- [f9ee689](https://github.com/unicode-org/icu/commit/f9ee689d7a0ffda54dd8ed1f45e76204983b02d7) [ICU-22922](https://unicode-org.atlassian.net/browse/ICU-22922) `Promoted all @draft ICU 75 APIs to @stable ICU 75.`
	- Authored by Rich Gillam <richard_gillam@apple.com>
	- Committed at 2025-02-05T13:02:56-08:00

- [0ceea4b](https://github.com/unicode-org/icu/commit/0ceea4bee93f45f859e90609314aff414431b5b7) [ICU-22922](https://unicode-org.atlassian.net/browse/ICU-22922) `Integrate CLDR 47 release alpha2, part 3, data files`
	- Authored by DraganBesevic <dragan@unicode.org>
	- Committed at 2025-02-03T09:31:43-08:00

- [c6e1c09](https://github.com/unicode-org/icu/commit/c6e1c09dbdce32be0c08eb8e30e9d392987ed3a6) [ICU-22922](https://unicode-org.atlassian.net/browse/ICU-22922) `Integrate CLDR 47 release alpha2, part 2, locale fallback binary files`
	- Authored by DraganBesevic <dragan@unicode.org>
	- Committed at 2025-02-03T09:31:43-08:00

- [9cbc8d7](https://github.com/unicode-org/icu/commit/9cbc8d7fe4d2ba0a357820c918c3c737bdeea5be) [ICU-22922](https://unicode-org.atlassian.net/browse/ICU-22922) `Integrate CLDR 47 release alpha2, part 1, binary files`
	- Authored by DraganBesevic <dragan@unicode.org>
	- Committed at 2025-02-03T09:31:43-08:00

- [95afc45](https://github.com/unicode-org/icu/commit/95afc45afa3f1e9419ba134437698e1f11927f5a) [ICU-22922](https://unicode-org.atlassian.net/browse/ICU-22922) `Integrate CLDR 47 release alpha1, part 5, updated unit test, again`
	- Authored by DraganBesevic <dragan@unicode.org>
	- Committed at 2025-01-30T13:45:15-08:00

- [4e1d9b3](https://github.com/unicode-org/icu/commit/4e1d9b30a583ddafb5a6b51d9d987902f8bc9278) [ICU-22922](https://unicode-org.atlassian.net/browse/ICU-22922) `Integrate CLDR 47 release alpha1, part 4, updated unit test`
	- Authored by DraganBesevic <dragan@unicode.org>
	- Committed at 2025-01-30T13:45:15-08:00

- [d49c124](https://github.com/unicode-org/icu/commit/d49c1242f9ba75a84e61c994a7c4c1286148d193) [ICU-22922](https://unicode-org.atlassian.net/browse/ICU-22922) `Integrate CLDR 47 release alpha1, part 3, source files`
	- Authored by DraganBesevic <dragan@unicode.org>
	- Committed at 2025-01-30T13:45:15-08:00

- [06c2096](https://github.com/unicode-org/icu/commit/06c2096fea5dbc73814342679a2c93d1a1fec6a1) [ICU-22922](https://unicode-org.atlassian.net/browse/ICU-22922) `Integrate CLDR 47 release alpha1, part 2, data files`
	- Authored by DraganBesevic <dragan@unicode.org>
	- Committed at 2025-01-30T13:45:15-08:00

- [90e3e1e](https://github.com/unicode-org/icu/commit/90e3e1e8822536cc0494f0094bb5aa866eb9dd80) [ICU-22922](https://unicode-org.atlassian.net/browse/ICU-22922) `Integrate CLDR 47 release alpha1, part 1, binary files`
	- Authored by DraganBesevic <dragan@unicode.org>
	- Committed at 2025-01-30T13:45:15-08:00

- [fae4512](https://github.com/unicode-org/icu/commit/fae4512d33f49615ae501f15374654aca5901f9a) [ICU-22922](https://unicode-org.atlassian.net/browse/ICU-22922) `ICU BRS 77: front-load update version to 77.0.1`
	- Authored by Mihai Nita <nmihai_2000@yahoo.com>
	- Committed at 2024-12-10T19:15:05-08:00

- [3b9c0fc](https://github.com/unicode-org/icu/commit/3b9c0fc4a5f6115276fc2bb551bcf550221b83ed) [ICU-22922](https://unicode-org.atlassian.net/browse/ICU-22922) `Integrate CLDR 46.1 beta1 to ICU main, part 3: ICU code/test mods`
	- Authored by Peter Edberg <pedberg@unicode.org>
	- Committed at 2024-12-09T13:08:14-08:00

- [e2581fd](https://github.com/unicode-org/icu/commit/e2581fd1acd49e92c3ac140cde872d1071e52cc9) [ICU-22922](https://unicode-org.atlassian.net/browse/ICU-22922) `Integrate CLDR 46.1 beta1 to ICU main, part 2: source data/test generated or copied from CLDR`
	- Authored by Peter Edberg <pedberg@unicode.org>
	- Committed at 2024-12-09T13:08:14-08:00

- [c3929d1](https://github.com/unicode-org/icu/commit/c3929d15951700948042576162516c200c30580f) [ICU-22922](https://unicode-org.atlassian.net/browse/ICU-22922) `Integrate CLDR 46.1 beta1 to ICU main, part 1: binary data`
	- Authored by Peter Edberg <pedberg@unicode.org>
	- Committed at 2024-12-09T13:08:14-08:00

- [d6f8a14](https://github.com/unicode-org/icu/commit/d6f8a14f8c377f2f0b3917b04179e7ac617c7e49) [ICU-22922](https://unicode-org.atlassian.net/browse/ICU-22922) `Merge maint/maint-76 to main (#3270)`
	- Authored by Shane F. Carr <shane@unicode.org>
	- Committed at 2024-11-19T10:35:26-08:00

- [700c5e3](https://github.com/unicode-org/icu/commit/700c5e36a1e96a5672eb5a07b36c1fc41fef016b) [ICU-22922](https://unicode-org.atlassian.net/browse/ICU-22922) `Merge maint/maint-76 to main (#3258)`
	- Authored by Shane F. Carr <shane@unicode.org>
	- Committed at 2024-11-11T17:33:19-08:00

- [698217e](https://github.com/unicode-org/icu/commit/698217ef630fb8f2a88f1bbeec778f9fc5bf4d84) [ICU-22922](https://unicode-org.atlassian.net/browse/ICU-22922) `ICU 76 final release`
	- Authored by Markus Scherer <markus.icu@gmail.com>
	- Committed at 2024-10-24T16:18:07-07:00

- [3cd97ad](https://github.com/unicode-org/icu/commit/3cd97add1ee482a5b7753103f80173e46997194f) [ICU-22922](https://unicode-org.atlassian.net/browse/ICU-22922) `migrate download index`
	- Authored by Markus Scherer <markus.icu@gmail.com>
	- Committed at 2024-09-30T17:05:04-07:00

#### Issue ICU-23043

_Jira issue is open_
- [ICU-23043](https://unicode-org.atlassian.net/browse/ICU-23043): ` ASSERT: 0 <= value && value < symbolsCount in ChineseCalendar`
	- No assignee!
	- Status: Accepted
	- Fix Version: 77.1
	- Component(s): time_calc

##### Commits with Issue ICU-23043

- [59af52b](https://github.com/unicode-org/icu/commit/59af52bb6f967bc5061bc3f6a91063de88e4aa78) [ICU-23043](https://unicode-org.atlassian.net/browse/ICU-23043) `Propogate error in Calendar.`
	- Authored by Frank Tang <ftang@chromium.org>
	- Committed at 2025-02-11T15:14:27-08:00


