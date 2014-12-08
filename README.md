android-unused-resources
========================

<b>Introduction</b>

이 프로젝트는 https://code.google.com/p/android-unused-resources/ 에서 몇가지 기능을 추가한 버전입니다.
사용법에 대한 좀더 자세한 사항은 <a href="http://lsit81.tistory.com/entry/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%EC%82%AC%EC%9A%A9%ED%95%98%EC%A7%80-%EC%95%8A%EB%8A%94-%EB%A6%AC%EC%86%8C%EC%8A%A4-%EC%9E%90%EB%8F%99-%EC%A0%9C%EA%B1%B0">blog</a>를 방문하여 주시기 바랍니다.

<b>추가된 기능</b>
   - 사용되지 않는 리소스 자동 삭제 기능
   - android studio용 프로젝트도 사용할 수있도록 수정 (2014.12.08)

<b>사용법</b>
- java -Dfile.encoding=UTF-8 -jar AndroidUnusedResources.jar [프로젝트 root 경로] [자동 삭제 옵션 true | false] <br />
최소 2번 이상 실행해서 더 이상 삭제할 게 없을때까지 동작시켜야 리소스가 정상 제거됩니다.

- 프로젝트 경로 옵션 설명<br />
EClipse 프로젝트 : AndroidManifest.xml이 존재하는 경로 지정.<br />
Android Studio 프로젝트 : build.gradle이 존재하는 경로 지정.<br />
(android { ... } 에 대하여 정의된 build.gradle이여야 함.)


Copyright 및 License는 android-unused-resources 프로젝트와 동일합니다.
- Apache License, Version 2.0


Andround Unused Resources is a Java application that will scan your project for unused resources. Unused resources needlessly take up space, increase the build time, and clutter the IDE's autocomplete list.  To use it, ensure your working directory is the root of your Android project, and run:  java -jar AndroidUnusedResources.jar
