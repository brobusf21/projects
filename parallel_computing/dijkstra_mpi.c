/* Brandon Robinson
 * CS220 / Pacheco
 * 28 October 2015
 * Project 3
 * 
 * File:     dijkstra_mpi.c
 *
 * Purpose:  Implement Dijkstra's algorithm using MPI communication for solving 
 *           the single-source shortest path problem:  find the length of 
 *           the shortest path between a specified vertex and all other vertices 
 *           in a directed graph. 
 *
 * Compile:  mpicc -g -Wall -o dijkstra_mpi dijkstra_mpi.c
 *           (If user wants to see debug output, include -DDEBUG in command line)
 *
 * Run:      mpiexec -n <p> ./dijkstra_mpi (on lab machines)
 *           csmpiexec -n <p> ./dijkstra_mpi (on the penguin cluster)
 *
 * Input:    n:    the number of rows and the number of columns 
 *                 in the matrix
 *           mat:  the matrix:  note that INFINITY should be
 *                 input as 1000000
 * Output:   The submatrix assigned to each process and the
 *           complete matrix printed from process 0.  Both
 *           print "i" instead of 1000000 for infinity.
 *
 * Notes:
 * 1.  It is assumed that n is divisible by p and that the matrix has 0's
 *     along it's diagnonal.
 * 2.  The number of processes, p, should evenly divide n.
 * 3.  You should free the MPI_Datatype object created by
 *     the program with a call to MPI_Type_free:  see the
 *     main function.
 * 4.  Example:  Suppose the matrix is
 *
 *        0 1 2 3
 *        4 0 5 6 
 *        7 8 0 9
 *        8 7 6 0
 *
 *     Then if there are two processes, the matrix will be
 *     distributed as follows:
 *
 *        Proc 0:  0 1    Proc 1:  2 3
 *                 4 0             5 6
 *                 7 8             0 9
 *                 8 7             6 0
 * 5. If user wants to see debug output, just include the -DDEBUG flag as an 
 *    option.
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <mpi.h>

#define MAX_STRING 10000
#define INFINITY 1000000

/* Input functions ---------------------------------------------------*/
int Read_n(int my_rank, MPI_Comm comm);
MPI_Datatype Build_blk_col_type(int n, int loc_n);
void Read_matrix(int loc_mat[], int n, int loc_n, 
      MPI_Datatype blk_col_mpi_t, int my_rank, MPI_Comm comm);

/* Output functions --------------------------------------------------*/
void Print_dists(int loc_dist[], int loc_n, int n, int my_rank, 
	             MPI_Comm comm);
void Print_paths(int loc_pred[], int loc_n, int n, int my_rank, 
	             MPI_Comm comm);

/* Computational functions -------------------------------------------*/
void Dijkstra(int mat[], int loc_dist[], int loc_pred[], int n, int loc_n, 
	          int my_rank, MPI_Comm comm);
int Global_vertex(int loc_u, int my_rank, int loc_n);
int Find_min_dist(int loc_dist[], int loc_known[], int loc_n, int my_rank, 
	              MPI_Comm comm);
int Global_min_vertex(int loc_dist[], int loc_u, int my_rank, int loc_n, 
	                  int* glbl_min_dist_p, MPI_Comm comm);

/* DEBUG functions --------------------------------------------*/
void Print_matrix(int loc_mat[], int n, int loc_n, 
	              MPI_Datatype blk_col_mpi_t, int my_rank, MPI_Comm comm);
void Print_local_matrix(int loc_mat[], int n, int loc_n, int my_rank);
void Print_loc_array(int loc_mat[], int loc_n, int my_rank);


/*--------------------------------------------------------------------*/
int main(int argc, char* argv[]) {
   int *loc_mat, *loc_dist, *loc_pred;
   int n, loc_n, p, my_rank;
   MPI_Comm comm;
   MPI_Datatype blk_col_mpi_t;
#  ifdef DEBUG
   int i, j;
#  endif

   MPI_Init(&argc, &argv);
   comm = MPI_COMM_WORLD;
   MPI_Comm_size(comm, &p);
   MPI_Comm_rank(comm, &my_rank);
   
   n = Read_n(my_rank, comm);
   loc_n = n/p;
   loc_mat = malloc(n*loc_n*sizeof(int));
   loc_dist = malloc(loc_n*sizeof(int));
   loc_pred = malloc(loc_n*sizeof(int));

#  ifdef DEBUG
   printf("Proc %d > p = %d, n = %d, loc_n = %d\n",
         my_rank, p, n, loc_n);

   /* This ensures that the matrix elements are initialized when */
   /* debugging.  It shouldn't be necessary */
   for (i = 0; i < n; i++)
      for (j = 0; j < loc_n; j++)
         loc_mat[i*loc_n + j] = -1;
#  endif   
   
   /* Build the special MPI_Datatype before doing matrix I/O */
   blk_col_mpi_t = Build_blk_col_type(n, loc_n);
   Read_matrix(loc_mat, n, loc_n, blk_col_mpi_t, my_rank, comm);

#  ifdef DEBUG   
   Print_local_matrix(loc_mat, n, loc_n, my_rank);
   Print_matrix(loc_mat, n, loc_n, blk_col_mpi_t, my_rank, comm);
#  endif

   Dijkstra(loc_mat, loc_dist, loc_pred, n, loc_n, my_rank, comm);

   Print_dists(loc_dist, loc_n, n, my_rank, comm);
   Print_paths(loc_pred, loc_n, n, my_rank, comm);
   

   free(loc_mat);
   free(loc_dist);
   free(loc_pred);

   /* When you're done with the MPI_Datatype, free it */
   MPI_Type_free(&blk_col_mpi_t);

   MPI_Finalize();
   return 0;
}  /* main */

 /*-------------------------------------------------------------------
 * Function:    Dijkstra
 *
 * Purpose:     Apply Dijkstra's algorithm to the local matrices.
 *
 * In args:     local_mat: each processes portion of matrix
 *              loc_dist:  each processes local_dist array
 *              loc_pred:  each processes local_pred array 
 *              n:         the number of vertices
 *              loc_n:     n/p number of vertices
 *              my_rank:   process rank
 *              comm:      Communicator consisting of all the processes
 *
 * Out args:    dist:      dist[v] = distance 0 to v.
 *              pred:      pred[v] = predecessor of v on a 
 *                         shortest path 0->v.
 */
void Dijkstra(int loc_mat[], int loc_dist[], int loc_pred[], int n, 
	          int loc_n, int my_rank, MPI_Comm comm) {
   int i, loc_v, *loc_known, new_dist, u, loc_u, glbl_min_dist;

   /* known[v] = true, if the shortest path 0->v is known */
   /* known[v] = false, otherwise                         */
   loc_known = malloc(loc_n*sizeof(int));

   /* Initialization */
   for (loc_v = 0; loc_v < loc_n; loc_v++) {
      loc_dist[loc_v] = loc_mat[0*loc_n + loc_v];
      loc_pred[loc_v] = 0;
      loc_known[loc_v] = 0;
   }
   if (my_rank == 0) loc_known[0] = 1;

   #  ifdef DEBUG
   printf("Loc_pred[] before looping inside process %d:\n", my_rank);
   Print_loc_array(loc_pred, loc_n, my_rank);
   printf("Loc_dist[] before looping inside process %d:\n", my_rank);
   Print_loc_array(loc_dist, loc_n, my_rank);
   printf("Loc_known[] before looping inside process %d:\n", my_rank);
   Print_loc_array(loc_known, loc_n, my_rank);
   #  endif
      
   /* On each pass find an additional vertex */
   /* whose distance to 0 is known           */
   for (i = 1; i < n; i++) {
	   loc_u = Find_min_dist(loc_dist, loc_known, loc_n, my_rank, comm);
	   u = Global_min_vertex(loc_dist, loc_u, my_rank, loc_n, &glbl_min_dist,
	                         comm);
       # ifdef DEBUG
       printf("Process %d: loc_u = %d, i = %d, u = %d\n",
              my_rank, loc_u, i, u);
       # endif
       if (u/loc_n == my_rank)
	       loc_known[loc_u] = 1;
       for (loc_v = 0; loc_v < loc_n; loc_v++) {
	       if (!loc_known[loc_v]) {
	           new_dist = glbl_min_dist + loc_mat[u*loc_n + loc_v];
	           if(new_dist < loc_dist[loc_v]) {
	               loc_dist[loc_v] = new_dist;
	               loc_pred[loc_v] = u;
           		}
       		}
       	}
	}
	#  ifdef DEBUG
    printf("Loc_pred[] after looping inside process %d:\n", my_rank);
    Print_loc_array(loc_pred, loc_n, my_rank);
    printf("Loc_dist[] after looping inside process %d:\n", my_rank);
    Print_loc_array(loc_dist, loc_n, my_rank);
    printf("Loc_known[] after looping inside process %d:\n", my_rank);
    Print_loc_array(loc_known, loc_n, my_rank);
    #  endif 

    free(loc_known);
}  /* Dijkstra */


/*-------------------------------------------------------------------
 * Function:    Find_min_dist
 * Purpose:     Find the vertex u with minimum distance to 0
 *              (dist[u]) among the vertices whose distance 
 *              to 0 is not known.
 * In args:     loc_dist:  loc_dist[v] = current estimate of distance
 *                         0->v
 *              loc_known: whether the minimum distance 0-> is known
 *              loc_n:     the total number of vertices
 *              my_rank:   process rank
 *              comm:      Communicator consisting of all the processes
 * 
 * Ret val:     loc_u:     The vertex u whose distance to 0, dist[u]
 *                         is a minimum among vertices whose distance
 *                         to 0 is not known.
 */
int Find_min_dist(int loc_dist[], int loc_known[], int loc_n, 
	              int my_rank, MPI_Comm comm) {
	int loc_u, loc_min_dist, loc_v;

	loc_u = -1;
	loc_min_dist = INFINITY+1;
	for (loc_v = 0; loc_v < loc_n; loc_v++) {
	    if (!loc_known[loc_v]) {
	        if (loc_dist[loc_v] < loc_min_dist) {
	            loc_u = loc_v;
	            loc_min_dist = loc_dist[loc_v];
         	}
      	}
   	}
   return loc_u;
}

/*------------------------------------------------------------------- 
 * Function:    Global_min_vertex
 *
 * Purpose:     Find the "global" minimum vertex u by taking a global
 *              minimum using MPI_Allreduce.
 * In args:     loc_dist:  dist[v] = current estimate of distance
 *              loc_u: local u value
 *              my_rank: process rank
 *              loc_n:  the total number of vertices
 *              glbl_min_dist_p: this is a pointer to an int that will 
 *              save the minimum distance from the global vertex
 *              comm: Communicator consisting of all the processes
 *
 * Ret val:     u: global vertex number of the vertex with 
 *              the minimum dist in glbl_min[1]
 */
int Global_min_vertex(int loc_dist[], int loc_u, int my_rank, int loc_n, 
	                  int* glbl_min_dist_p, MPI_Comm comm) {
   
   int my_min[2], glbl_min[2], u;

   if (loc_u == -1) {
      my_min[0] = INFINITY+1;
      my_min[1] = -1;
   } else {
      my_min[0] = loc_dist[loc_u];
      my_min[1] = Global_vertex(loc_u, my_rank, loc_n);
   }

   #ifdef DEBUG
   printf("Process %d: Distance from 0 to %d = %d\n", my_rank, loc_u, 
   	       my_min[0]);
   printf("Process %d: Global_vertex = %d\n", my_rank, my_min[1]);
   #endif

   MPI_Allreduce(my_min, glbl_min, 1, MPI_2INT, MPI_MINLOC, comm);

   u = glbl_min[1];

   #ifdef DEBUG
   printf("Process %d: Global min distance = %d\n", my_rank, glbl_min[0]);
   printf("Process %d: Global min vertex w/ this distance = %d\n", 
   	       my_rank, glbl_min[1]);
   #endif

   *glbl_min_dist_p = glbl_min[0];

   return u;
}  /* Find_min_dist */

/*------------------------------------------------------------------- 
 * Function: Global_vertex
 * 
 * Purpose:  Helper function that calculates the global vertex
 *
 * Ret val:  glbl_vertex: The global vertex of the local vertex
 */
int Global_vertex(int loc_u, int my_rank, int loc_n) {
	int glbl_vertex = loc_u + (my_rank*loc_n);
	return glbl_vertex;
}

/*---------------------------------------------------------------------
 * Function:  Read_n
 * Purpose:   Read in the number of rows in the matrix on process 0
 *            and broadcast this value to the other processes
 * In args:   my_rank:  the calling process' rank
 *            comm:     Communicator containing all calling processes
 * Ret val:   n:        the number of rows in the matrix
 */
int Read_n(int my_rank, MPI_Comm comm) {
   int n;

   if (my_rank == 0) {
      printf("Please enter n for an (nxn) matrix: \n");
      scanf("%d", &n);
   }
   MPI_Bcast(&n, 1, MPI_INT, 0, comm);
   return n;
}  /* Read_n */


/*---------------------------------------------------------------------
 * Function:  Build_blk_col_type
 * Purpose:   Build an MPI_Datatype that represents a block column of
 *            a matrix
 * In args:   n:  number of rows in the matrix and the block column
 *            loc_n = n/p:  number cols in the block column
 * Ret val:   blk_col_mpi_t:  MPI_Datatype that represents a block
 *            column
 */
MPI_Datatype Build_blk_col_type(int n, int loc_n) {
   MPI_Aint lb, extent;
   MPI_Datatype block_mpi_t;
   MPI_Datatype first_bc_mpi_t;
   MPI_Datatype blk_col_mpi_t;

   MPI_Type_contiguous(loc_n, MPI_INT, &block_mpi_t);
   MPI_Type_get_extent(block_mpi_t, &lb, &extent);

   MPI_Type_vector(n, loc_n, n, MPI_INT, &first_bc_mpi_t);
   MPI_Type_create_resized(first_bc_mpi_t, lb, extent,
         &blk_col_mpi_t);
   MPI_Type_commit(&blk_col_mpi_t);

   MPI_Type_free(&block_mpi_t);
   MPI_Type_free(&first_bc_mpi_t);

   return blk_col_mpi_t;
}  /* Build_blk_col_type */

/*---------------------------------------------------------------------
 * Function:  Read_matrix
 * Purpose:   Read in an nxn matrix of ints on process 0, and
 *            distribute it among the processes so that each
 *            process gets a block column with n rows and n/p
 *            columns
 * In args:   n:  the number of rows in the matrix and the submatrices
 *            loc_n (n/p):  the number of columns in the submatrices
 *            blk_col_mpi_t:  the MPI_Datatype used on process 0
 *            my_rank:  the caller's rank in comm
 *            comm:  Communicator consisting of all the processes
 * Out arg:   loc_mat:  the calling process' submatrix (needs to be 
 *               allocated by the caller)
 */
void Read_matrix(int loc_mat[], int n, int loc_n, 
      MPI_Datatype blk_col_mpi_t, int my_rank, MPI_Comm comm) {
   int* mat = NULL, i, j;

   if (my_rank == 0) {
      printf("Please enter matrix values:\n");  
      mat = malloc(n*n*sizeof(int));
      for (i = 0; i < n; i++)
         for (j = 0; j < n; j++)
            scanf("%d", &mat[i*n + j]);
   }

   MPI_Scatter(mat, 1, blk_col_mpi_t,
      loc_mat, n*loc_n, MPI_INT, 0, comm);

   if (my_rank == 0) {  
      free(mat);
   }
}  /* Read_matrix */


/*---------------------------------------------------------------------
 * Function:  Print_local_matrix
 * Purpose:   Store a process' submatrix as a string and print the
 *            string.  Printing as a string reduces the chance 
 *            that another process' output will interrupt the output.
 *            from the calling process.
 * In args:   loc_mat:  the calling process' submatrix
 *            n:  the number of rows in the submatrix
 *            loc_n:  the number of cols in the submatrix
 *            my_rank:  the calling process' rank
 */
void Print_local_matrix(int loc_mat[], int n, int loc_n, int my_rank) {
   char temp[MAX_STRING];
   char *cp = temp;
   int i, j;

   sprintf(cp, "Proc %d >\n", my_rank);
   cp = temp + strlen(temp);
   for (i = 0; i < n; i++) {
      for (j = 0; j < loc_n; j++) {
         if (loc_mat[i*loc_n + j] == INFINITY)
            sprintf(cp, " i ");
         else
            sprintf(cp, "%2d ", loc_mat[i*loc_n + j]);
         cp = temp + strlen(temp);
      }
      sprintf(cp, "\n");
      cp = temp + strlen(temp);
   }

   printf("%s\n", temp);
}  /* Print_local_matrix */


/*---------------------------------------------------------------------
 * Function:  Print_matrix
 * Purpose:   Print the matrix that's been distributed among the 
 *            processes.
 * In args:   loc_mat:       the calling process' submatrix
 *            n:             number of rows in the matrix and the submatrices
 *            loc_n:         the number of cols in the submatrix
 *            blk_col_mpi_t: MPI_Datatype used on process 0 to
 *                           receive a process' submatrix
 *            my_rank:       the calling process' rank
 *            comm:          Communicator consisting of all the processes
 */
void Print_matrix(int loc_mat[], int n, int loc_n,
      MPI_Datatype blk_col_mpi_t, int my_rank, MPI_Comm comm) {
   int* mat = NULL, i, j;

   if (my_rank == 0) mat = malloc(n*n*sizeof(int));
   MPI_Gather(loc_mat, n*loc_n, MPI_INT,
         mat, 1, blk_col_mpi_t, 0, comm);
   if (my_rank == 0) {
      for (i = 0; i < n; i++) {
         for (j = 0; j < n; j++)
            if (mat[i*n + j] == INFINITY)
               printf(" i ");
            else
               printf("%2d ", mat[i*n + j]);
         printf("\n");
      }
      free(mat);
   }
}  /* Print_matrix */

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

   /*-------------------------------------------------------------------
 * Function:    Print_dists
 * Purpose:     Print the length of the shortest path from 0 to each
 *              vertex
 * In args:     n:  the number of vertices
 *              dist:  distances from 0 to each vertex v:  dist[v]
 *                 is the length of the shortest path 0->v
 */
void Print_dists(int loc_dist[], int loc_n, int n, int my_rank, 
	             MPI_Comm comm) {

   int* global_dist = NULL, v;

   if (my_rank == 0) global_dist = malloc(n*sizeof(int));
   MPI_Gather(loc_dist, loc_n, MPI_INT, global_dist, loc_n, MPI_INT, 0, comm);
   if (my_rank == 0) {
   		 printf("\nThe distance from 0 to each vertex is:\n");
         printf("  v    dist 0->v\n");
         printf("----   ---------\n");
   
      for (v = 1; v < n; v++)
         printf("%3d       %4d\n", v, global_dist[v]);
      printf("\n");
   
      free(global_dist);
   }
} /* Print_dists */  


/*-------------------------------------------------------------------
 * Function:    Print_paths
 * Purpose:     Print the shortest path from 0 to each vertex
 * In args:     n:  the number of vertices
 *              pred:  list of predecessors:  pred[v] = u if
 *                 u precedes v on the shortest path 0->v
 */
void Print_paths(int loc_pred[], int loc_n, int n, int my_rank, 
	             MPI_Comm comm) {

   int v, w, count, i;
   int* path = NULL;

   if (my_rank == 0) path =  malloc(n*sizeof(int));
   MPI_Gather(loc_pred, loc_n, MPI_INT, path, loc_n, MPI_INT, 0, comm);
   if (my_rank == 0) {
   	  printf("The shortest path from 0 to each vertex is:\n");
      printf("  v     Path 0->v\n");
      printf("----    ---------\n");
      for (v = 1; v < n; v++) {
         printf("%3d:    ", v);
         count = 0;
         w = v;
         while (w != 0) {
            path[count] = w;
            count++;
            w = path[w];
         }
         printf("0 ");
         for (i = count-1; i >= 0; i--)
            printf("%d ", path[i]);
         printf("\n");
      }
   free(path);    
   }
}  /* Print_paths */

