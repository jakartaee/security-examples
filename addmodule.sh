#!/bin/bash
#
# Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
#
# This program and the accompanying materials are made available under the
# terms of the Eclipse Distribution License v. 1.0, which is available at
# http://www.eclipse.org/org/documents/edl-v10.php.
#
# SPDX-License-Identifier: BSD-3-Clause
#

export name=${1?Specify a module name}

mkdir $name &&
cat > $name/pom.xml <<EOF
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <parent>
    <groupId>net.java.jsr375</groupId>
    <artifactId>jsr375-proposals</artifactId>
    <version>1.0-SNAPSHOT</version>
  </parent>

  <artifactId>$name</artifactId>

</project>
EOF

mkdir -p $name/src/main/java/org/flyfishee &&
mkdir -p $name/src/main/java/org/secured &&
perl -i -pe 's,^( *)(</modules>),$1  <module>$ENV{name}</module>\n$1$2,' pom.xml &&
echo "# $name" > $name/README.adoc
git add $name &&
git commit -m "Add $name" $name pom.xml
