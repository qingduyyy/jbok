JBOK: Just a Bunch Of Keys
===

[![Build Status](https://travis-ci.com/c-block/jbok.svg?branch=master)](https://travis-ci.com/c-block/jbok)

**JBOK**, to be a type-safe, functional and flexible blockchain testbed.

Go to the [**microsite**](https://c-block.github.io/jbok/) for more information.

WIP.

# test desktop wallet in macOS
## required
jdk 1.8
scala 2.12+
```
brew update
brew install scala
brew install sbt
```
yarn 
```
brew install yarn
```
## enable the SBT plugin
optional but recommended
adding
```
addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.0.3")
```
to ```~/.sbt/1.0/plugins/build.sbt``` (create if not exist)

## compile and test
There are two projects appJS,  appJVM.
open one terminal in ```iotchain/jbok```
```
sbt
```
```
sbt:jbok> project appJS
sbt:jbok-app> dev 
```
open another termimal in ```iotchain/jbok```

```
sbt
```
```
sbt:jbok> project appJVM
sbt:jbok-app> testOnly *SimuServer* 
```
## open desktop wallet in google chrome
[localhost:8080](http://localhost:8080)

# test desktop wallet in centOS
## required
jdk 1.8  
scala 2.12+
sbt  
yarn  
### install scala
* download and unzip 
```
wget https://downloads.lightbend.com/scala/2.12.7/scala-2.12.7.tgz
tar -xzvf scala-2.12.7.tgz
```
* configure the environment parameters
*find the bin folder directory*, in my case it is ```/root/apps/jdk1.8.0_191/bin```
```
echo "export PATH=$PATH:/root/apps/jdk1.8.0_191/bin" > /etc/profile
```
* check if your installation succeed
```
$ scala -version
Scala code runner version 2.12.7 -- Copyright 2002-2018, LAMP/EPFL and Lightbend, Inc.
``` 
### install sbt
``` 
curl https://bintray.com/sbt/rpm/rpm | sudo tee /etc/yum.repos.d/bintray-sbt-rpm.repo
yum install sbt
```
*enable the SBT plugin*
optional but recommended, adding
```
addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.0.3")
```
to ```~/.sbt/1.0/plugins/build.sbt``` (create if not exist)
### install yarn
```
curl --silent --location https://dl.yarnpkg.com/rpm/yarn.repo | sudo tee /etc/yum.repos.d/yarn.repo
```
## compile and test
There are two projects appJS,  appJVM.
open one terminal in ```iotchain/jbok```
```
sbt
```
```
sbt:jbok> project appJS
sbt:jbok-app> dev 
```
open another termimal in ```iotchain/jbok```

```
sbt
```
```
sbt:jbok> project appJVM
sbt:jbok-app> testOnly *SimuServer* 
```
## open desktop wallet in google chrome
[localhost:8080](http://localhost:8080)
