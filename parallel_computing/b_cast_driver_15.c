/* Brandon Robinson
 * CS220 / Pacheco
 * 12 October 2015
 * Homework 6
 *
 * File:      b_cast_driver_15.c
 * Purpose:   Driver for MPI tree structured broadcast function.
 * Input:     A single int
 * Output:    Value received by each process.
 *
 * Algorithm: 
 *    1. Process 0 reads in an it
 *    2. Processes call Bcast
 *    3. Each process prints value it received
 *
 * Compile:  mpicc -g -Wall -o bd b_cast_driver_15.c
 * Run:      mpiexec -n <number of processes> bd
 *
 * Input: After running program, please enter an int when prompted.
 *
 * Notes:
 *    1. Since each process is printing it's own data, the order 
 *       of the output will be unpredictable. 
 *    2. This program only works when p is a power of 2.
 *    3. Communication example:
 *       0->1
 *       0->2, 1->3
 *       0->4, 1->5, 2->6, 3->7
 *       etc.
 */
#include <stdio.h>
#include <stdlib.h>
#include <mpi.h>

int Bcast(int in_val, int my_rank, int p, MPI_Comm comm);

int main(int argc, char* argv[]) {
   int p, my_rank;
   MPI_Comm comm;
   int in_val;

   MPI_Init(&argc, &argv);
   comm = MPI_COMM_WORLD;
   MPI_Comm_size(comm, &p);
   MPI_Comm_rank(comm, &my_rank);

   if (my_rank == 0) {
      printf("Enter an int\n");
      scanf("%d", &in_val);
   }

   Bcast(in_val, my_rank, p, comm);

   MPI_Finalize();
   return 0;
}  /* main */


/*-----------------------------------------------------------------*/
/* Function:    Bcast
 * Purpose:     Broadcast a value from process 0 to all the other
 *              processes
 *
 * Input args:  in_val = the value to be broadcast from process 0
 *              my_rank = process's rank
 *              p = number of processes
 *              comm = communicator
 * Return val:  on each process the value broadcast by process 0
 *
 * Notes:
 *    1.  Uses tree structured communication.
 *    2.  The pairing of the processes is done using bitwise
 *        exclusive or.  Here's a table showing the rule for
 *        for bitwise exclusive or
 *           X Y X^Y
 *           0 0  0
 *           0 1  1
 *           1 0  1
 *           1 1  0
 *        Here's a table showing the process pairing with 8
 *        processes (r = my_rank, other column heads are 
 *        bitmask)
 *           r     001 010 100 
 *           -     --- --- ---
 *           0 000 001 010 100  
 *           1 001 000 011 101
 *           2 010  x  000 110
 *           3 011  x  001 111
 *           4 100  x   x  000
 *           5 101  x   x  001
 *           6 110  x   x  010
 *           7 111  x   x  011
 */
int Bcast(int in_val, int my_rank, int p, MPI_Comm comm) {
   int        partner;
   int		  iters = 0;
   unsigned   action = 2; /* Defines which p's send and recv */
   unsigned   bitmask = 1;

   while (bitmask < p) {
      if (my_rank < action) {
         partner = my_rank ^ bitmask; /* Compute partner rank */
         if (my_rank < (action / 2)) {
            MPI_Send(&in_val, 1, MPI_INT, partner, 0, comm);
            printf("Process %d > Iter %d, Partner = %d, Sent %d\n", 
            	my_rank, iters, partner, in_val);
         } else {
            MPI_Recv(&in_val, 1, MPI_INT, partner, 0, comm, 
                  MPI_STATUS_IGNORE); 
            printf("Process %d > Iter %d, Partner = %d, Received %d\n", 
            	my_rank, iters, partner, in_val);
         }
      } else {
         printf("Process %d > Iter %d, Inactive\n", my_rank, iters);
      }
      action <<= 1; /* Update bitmask */ 
      bitmask <<= 1; /* Update bitmask */
      iters++;
   }
   return in_val;
}  /* Bcast */