# JWormBench

## About

JWormBench is a port of the WormBench benchmark from C# to Java. This port extends the original benchmark in several
ways, making it more useful as a testbed for evaluating STMs. Moreover, our port, which we called JWormBench, was designed to be easily extensible and to allow easy integration with different STMs.


## WormBench

The WormBench benchmark was built to research new workloads (creating, testing, and running) for TM systems' evaluation. The idea behind WormBench is inspired in the Snake game. In this case the snakes are *worms* formed by a *body* and a *head*, moving in a shared *world* --- matrix of *nodes*. Each *node* has an integer *value* and the worms' *group id* that is over that node (worms belonging to other groups should not cross through each other). 


## JWormBench Remarks

One of the advantages of WormBench is the ability to create new configurations of the *world*, *worms*, and *worm operations*, producing new workloads with complex contention characteristics and different transaction durations and sizes, without modifying its source-code. The JWormBench keeps this approach and adds two new features important for the research of new workloads and evaluation of STM scalability: (1) the ability to specify the proportion between different kinds of operations, and (2) the ability to set the number of worms independently of the number of threads.  

Furthermore, the JWormBench provides a simple API, easy to integrate with any STM implementation in Java. So, anyone may add a new synchronization mechanism (based on STM or other), implementing the appropriate abstract types and providing those implementations to JWormBench via a configuration *Guice module*. In the same way you can also extend JWomBench with new kinds of *worm operations* without modifying the core JWormBench library.