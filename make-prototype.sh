#!/usr/bin/env bash

set -e

sbt ofsted-prototype/fullOptJS

target=./ofsted-prototype/target/scala-2.12/ofsted-prototype-opt.js

mkdir -p ofsted-prototype-dist
cp -r ofsted-prototype/page/* ofsted-prototype-dist
cp $target ofsted-prototype-dist/
sed -i -e 's|ofsted-prototype-fastopt[.]js|ofsted-prototype-opt.js|' ofsted-prototype-dist/govuk.html

zip -r ofsted-prototype-dist.zip ofsted-prototype-dist
rm -r ofsted-prototype-dist
