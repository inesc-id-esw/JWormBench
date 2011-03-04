#!/bin/sh

#######################################
# CONSTANTS 
#######################################
WORLD_SIZE=512
ITERATIONS=128
ITERATIONS_INC=16

JAVA_VM="java -cp $CLASSPATH:lib/jwormbench.jar"

#######################################
# SET CLASSPATH
#######################################

# Do it manually before run the script

#######################################
# run bench
#######################################

for wRate in 21 22 23; do
for threads in 1 8 16 24 32 40 48; do
for i in 1 2 3 4 5 6 7 8; do

iterations=$((threads==1?ITERATIONS:ITERATIONS+(threads*ITERATIONS_INC)))
${JAVA_VM} jwormbench.app.ConsoleApp -iterations ${iterations} -sync finelock -threads ${threads} -wRate ${wRate} -world ${WORLD_SIZE} -head 2.16

done
done
done

