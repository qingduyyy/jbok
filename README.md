JBOK: Just a Bunch Of Keys
===

[![Build Status](https://travis-ci.com/c-block/jbok.svg?branch=master)](https://travis-ci.com/c-block/jbok)

**JBOK**, to be a type-safe, functional and flexible blockchain testbed.

Go to the [**microsite**](https://c-block.github.io/jbok/) for more information.

WIP.


#test desktop wallet in local envrionment

#test in macOS 
##required jdk 1.8 scala 2.12+

brew update
brew install scala
brew install sbt
yarn

brew install yarn
##enable the SBT plugin optional but recommended adding

addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.0.3")
to ~/.sbt/1.0/plugins/build.sbt (create if not exist)

##compile and test There are two projects appJS, appJVM. open one terminal in iotchain/jbok

sbt
sbt:jbok> project appJS
sbt:jbok-app> dev 
open another termimal in iotchain/jbok

sbt
sbt:jbok> project appJVM
sbt:jbok-app> testOnly *SimuServer* 
##open desktop wallet in google chrome localhost:8080
