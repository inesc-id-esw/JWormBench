---
layout: page
---

# JWormBench

JWormBench is a port of the WormBench [1](#zyulkyarov-2008) benchmark from C# to Java. 
This port extends the original benchmark in several ways, making 
it more useful as a testbed for evaluating STMs. Moreover, our port, 
which we called JWormBench, was designed to be easily extensible and 
to allow easy integration with different STMs.

We used the JWormBench to explore the effects on performance of relaxing 
the transparency of an STM [2](#carvalho-ica3pp11-2011).

[<a id="zyulkyarov-2008">1</a>] **WormBench - A Configurable Workload for Evaluating Transactional Memory Systems**,
Ferad Zyulkyarov et al, 2008, Workshop on MEmory performance: DEaling with Applications, 
systems and architecture (MEDEA), In conjunction with PACT Conference.

[<a id="carvalho-ica3pp11-2011">2</a>] **STM with transparent API considered harmful**, 
Fernando Miguel Carvalho, Jo&atilde;o Cachopo, 2011, ICA3PP'11: Proceedings of the 
11th international conference on Algorithms and architectures for parallel 
processing - Volume Part I , Volume Part I

## Maven Repository

Maven repository top-level directory is [here](maven-repo/).

To use software from this repository in your Maven projects, add the following
configuration to the appropriate section in either your project's `pom.xml`
file or your Maven global `settings.xml` file.


    <repositories>
        <repository>
            <id>jwormbench-repository</id>
            <url>http://inesc-id-esw.github.com/jwormbench/maven-repo/</url>
        </repository>
    </repositories>

