/* Brandon Robinson
 * CS220 / Pacheco
 * November 3 2015
 * Homework #9 Part 1
 * 
 * File: pth_daxpy.c
 *
 * Purpose:  Pthreads program that computes DAXPY 
 *           ("Double precision Alpha X Plus Y").
 *
 * Input:
 *     thread_count: # of threads entered on the command line
 *
 *
 * Output:
 *     y: the array that contains the computed values
 *
 * Compile: gcc -g -Wall -o pth_daxpy pth_daxpy.c -lpthread
 * Usage:
 *    ./pth_daxpy <thread_count>
 *
 * Notes:  
 *       1. It is assumed that n is evenly divided by thread count.
 */ 
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

/* Global Variables */
int thread_count;
int n;
double alpha; 
double* x;
double* y; 


/* Serial functions */
void Usage(char* prog_name);
void Read_array(char* prompt, double A[]);
void Print_array(char* title, double A[]);

/* Parallel function */
void *DAXPY(void* rank);


/*------------------------------------------------------------------*/
int main(int argc, char* argv[]) {
   long       thread;
   pthread_t* thread_handles;

   if (argc != 2) Usage(argv[0]);
   thread_count = strtol(argv[1], NULL, 10);
   thread_handles = malloc(thread_count*sizeof(pthread_t));

   printf("Enter n\n");
   scanf("%d", &n);
   
   x = malloc(n*sizeof(double)); /* one array */
   y = malloc(n*sizeof(double)); /* second array */ 

   
   Read_array("Enter the first matrix", x);
   
   Read_array("Enter the second matrix", y);
   
   printf("Please enter a numerical value for alpha\n");
   scanf("%lf", &alpha);
   
   for (thread = 0; thread < thread_count; thread++)
      pthread_create(&thread_handles[thread], NULL,
         DAXPY, (void*) thread);

   for (thread = 0; thread < thread_count; thread++)
      pthread_join(thread_handles[thread], NULL);
   
   
   Print_array("The answer is", y);

   free(x);
   free(y);
   free(thread_handles);

   return 0;
}  /* main */

 /*------------------------------------------------------------------
  * Function:       DAXPY
  * Purpose:        Computes "Double precision Alpha X Plus Y." 
  * In arg:         rank
  * Global in vars: A, x, m, n, thread_count
  * Global out var: y
  * Notes: Each thread calculates it's own block distribution.
  */
 void *DAXPY(void* rank) {
    long my_rank = (long) rank;
    int local_n = n/thread_count; 
    int my_first_row = my_rank*local_n;
    int my_last_row = my_first_row + local_n - 1;

    for (int i = my_first_row; i <= my_last_row; i++) {
      y[i] += alpha*x[i];
    }     

    return NULL;
 }  /* Pth_mat_vect */

/*------------------------------------------------------------------
 * Function:    Read_array
 * Purpose:     Read in the array
 * In args:     prompt, A
 * Global out var: x or y
 */
void Read_array(char* prompt, double A[]) {
   int i;

   printf("%s\n", prompt);
   for (i = 0; i < n; i++) 
      scanf("%lf", &A[i]);
  }  /* Read_matrix */
 
/*------------------------------------------------------------------
 * Function:    Print_array
 * Purpose:     Print a vector
 * In args:     title, y
 */
void Print_array(char* title, double A[]) {
   int i;

   printf("%s\n", title);
   for (i = 0; i < n; i++) {
      printf("%4.1f ", A[i]); 
   }
   printf("\n");
}  /* Print_vector */

/*------------------------------------------------------------------
 * Function:  Usage
 * Purpose:   print a message showing what the command line should
 *            be, and terminate
 * In arg :   prog_name
 */
void Usage (char* prog_name) {
   fprintf(stderr, "usage: %s <thread_count>\n", prog_name);
   exit(0);
}  /* Usage */
  
   