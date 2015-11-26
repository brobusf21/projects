/* Brandon Robinson
 * CS 220 / Pacheco
 * 21 October 2015
 * Homework 7
 *
 * File:     one_d_io.c
 * Purpose:  Implement I/O functions that will be useful in an
 *           MPI implementation of Dijkstra's algorithm.
 *           Specifically, it implements a function that will
 *           output as a string a process' part of a distributed
 *           one-dimensional array.  This makes it more likely that
 *           printing the subarray assigned to one process will
 *           be printed without interruption by another process.
 *           It also implements a function that gathers a
 *           one-dimensional array onto process 0 and prints
 *           it out.
 *
 * Compile:  mpicc -g -Wall -o one_d_io one_d_io.c
 * Run:      mpiexec -n <p> ./one_d_io (on lab machines)
 *           csmpiexec -n <p> ./one_d_io (on the penguin cluster)
 *
 * Input:    n:  the number of elements in the global,
 *               one-dimensional array.
 * Output:   The subarray assigned to each process and the
 *           complete array printed from process 0.
 *
 * Notes:
 * 1.  The number of processes, p, should evenly divide n.
 * 2.  The array is generated on process 0 using the random
 *     number generator random, and distributed to the
 *     processes.
 * 3.  The array has a block distribution:
 *
 *        Proc 0 <- elements 0, 1, ..., n/p-1
 *        Proc 1 <- elements n/p, n/p+1, ..., 2n/p-1
 *               ...
 *        Proc p-1 <- elements (p-1)n/p, ..., n-1
 *
 * 4.  Example:  Suppose the array is
 *
 *        1 2 3 4 5 6 7 8
 *
 *     Then if there are four processes, the array will be
 *     distributed as follows:
 *
 *        Proc 0:  1 2
 *        Proc 1:  3 4
 *        Proc 2:  4 5
 *        Proc 3:  6 7
 *
 * 5.  If the DEBUG compile flag is set, the global array is
 *     printed by process 0 before it is distributed.
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <mpi.h>

#define MAX_STRING  10000
#define INFINITY 1000000

/* Largest element in array is 20 */
#define MAX_ELEMENT 20

int Read_n(int my_rank, MPI_Comm comm);
void Get_array(int loc_arr[], int n, int loc_n, int my_rank,
               MPI_Comm comm);
void Print_loc_array(int loc_arr[], int loc_n, int my_rank);
void Print_array(int loc_arr[], int n, int loc_n, int my_rank,
                 MPI_Comm comm);

int main(int argc, char* argv[]) {
    int *loc_arr;
    int n, loc_n, p, my_rank;
    MPI_Comm comm;
    
    MPI_Init(&argc, &argv);
    comm = MPI_COMM_WORLD;
    MPI_Comm_size(comm, &p);
    MPI_Comm_rank(comm, &my_rank);
    
    n = Read_n(my_rank, comm);
    loc_n = n/p;
    loc_arr = malloc(loc_n*sizeof(int));
    
    
#  ifdef DEBUG
    printf("Proc %d > p = %d, n = %d, loc_n = %d\n",
           my_rank, p, n, loc_n);
#  endif
    
    Get_array(loc_arr, n, loc_n, my_rank, comm);
    Print_loc_array(loc_arr, loc_n, my_rank);
    Print_array(loc_arr, n, loc_n, my_rank, comm);
    
    free(loc_arr);
    
    MPI_Finalize();
    return 0;
}  /* main */


/*---------------------------------------------------------------------
 * Function:  Read_n
 * Purpose:   Read in the number of rows in the arrrix on process 0
 *            and broadcast this value to the other processes
 * In args:   my_rank:  the calling process' rank
 *            comm:  Communicator containing all calling processes
 * Ret val:   n:  the number of rows in the arrrix
 */
int Read_n(int my_rank, MPI_Comm comm) {
    int n;
    
    if (my_rank == 0)
        scanf("%d", &n);
    MPI_Bcast(&n, 1, MPI_INT, 0, comm);
    return n;
}  /* Read_n */


/*---------------------------------------------------------------------
 * Function:  Get_array
 * Purpose:   Generate an n-element array on process 0 and
 *            distribute it among the processes so that each
 *            process gets a block of n/p elements
 * In args:   n:  the number of elements in the global array
 *            loc_n = n/p:  the number of elements in each
 *               process' subarray
 *            my_rank:  the caller's rank in comm
 *            comm:  Communicator consisting of all the processes
 * Out arg:   loc_arr:  the calling process' subarray (needs to be
 *               allocated by the caller)
 */
void Get_array(int loc_arr[], int n, int loc_n, int my_rank,
               MPI_Comm comm) {
    int* arr = NULL, i;
    
    if (my_rank == 0) {
        arr = malloc(n*sizeof(int));
        srandom(1);
        for (i = 0; i < n; i++)
            arr[i] = (random() % MAX_ELEMENT) + 1;
#     ifdef DEBUG
        Print_loc_array(arr, n, my_rank);
#     endif
    }
    
    MPI_Scatter(arr, loc_n, MPI_INT,
                loc_arr, loc_n, MPI_INT, 0, comm);
    
    if (my_rank == 0) free(arr);
}  /* Get_array */


/*---------------------------------------------------------------------
 * Function:  Print_loc_array
 * Purpose:   Store a process' subarray as a string and print the
 *            string.  Printing as a string reduces the chance
 *            that another process' output will interrupt the output
 *            from the calling process.
 * In args:   loc_arr:  the calling process' subarray
 *            loc_n:  the number of elements in the subarray
 *            my_rank:  the calling process' rank
 */
void Print_loc_array(int loc_mat[], int loc_n, int my_rank) {
    char temp[MAX_STRING];
    char *cp = temp;
    int i;
    
    sprintf(cp, "Proc %d >\n", my_rank);
    cp = temp + strlen(temp);
    for (i = 0; i < loc_n; i++) {
        if (loc_mat[i] == INFINITY)
            sprintf(cp, " i ");
        else
            sprintf(cp, "%2d ", loc_mat[i]);
        cp = temp + strlen(temp);
        sprintf(cp, "\n");
        cp = temp + strlen(temp);
    }
    
    printf("%s\n", temp);
    
    
}  /* Print_local_array */


/*---------------------------------------------------------------------
 * Function:  Print_array
 * Purpose:   Print the array that's been distributed among the
 *            processes.
 * In args:   loc_arr:  the calling process' subarray
 *            n:  number of elements in the array
 *            loc_n:  the number of elements in each process' subarray
 *            my_rank:  the calling process' rank
 *            comm:  Communicator consisting of all the processes
 */
void Print_array(int loc_arr[], int n, int loc_n, int my_rank, MPI_Comm comm) {
    int* mat = NULL, i;
    
    
    if (my_rank == 0) mat = malloc(n*sizeof(int));
    MPI_Gather(loc_arr, loc_n, MPI_INT, mat, loc_n, MPI_INT, 0, comm);
    if (my_rank == 0) {
        for (i = 0; i < n; i++) {
            if (mat[i] == INFINITY)
                printf(" i ");
            else
                printf("%2d ", mat[i]);
            printf("\n");
        }
        free(mat);
    }
    
    
}  /* Print_array */