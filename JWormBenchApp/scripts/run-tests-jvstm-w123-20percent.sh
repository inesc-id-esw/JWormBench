#!/bin/sh

#######################################
# CONSTANTS 
#######################################
WORLD_SIZE=1024
ITERATIONS=4096
ITERATIONS_INC=32

JAVA_VM_DBL="java -cp $CLASSPATH:lib/jwormbench.jar:lib/jvstm-dbl.jar"
JAVA_VM_VSTM=" java -cp $CLASSPATH:lib/jwormbench.jar:lib/jvstm.jar:lib/jwormmodules.jar"

#######################################
# SET CLASSPATH
#######################################

# Do it manually before run the script

#######################################
# run bench
#######################################

for wRate in 21 22 23; do
for threads in 1 2 4 8 10 12 14 16; do
for i in 1 2 3 4 5 6 7 8; do

# iterations=$((threads==1?ITERATIONS:ITERATIONS+(threads*ITERATIONS_INC)))
iterations=$(ITERATIONS)
${JAVA_VM_VSTM} jwormbench.app.ConsoleApp -iterations ${iterations} -sync jvstm -threads ${threads} -wRate ${wRate} -world ${WORLD_SIZE} -head 2.16

done
done
done

for wRate in 21 22 23; do
for threads in 1 2 4 8 10 12 14 16; do
for i in 1 2 3 4 5 6 7 8; do

# iterations=$((threads==1?ITERATIONS:ITERATIONS+(threads*ITERATIONS_INC)))
iterations=$(ITERATIONS)
${JAVA_VM_DBL} jwormbench.app.ConsoleApp -iterations ${iterations} -sync jvstmdbl -threads ${threads} -wRate ${wRate} -world ${WORLD_SIZE} -head 2.16

done
done
done
