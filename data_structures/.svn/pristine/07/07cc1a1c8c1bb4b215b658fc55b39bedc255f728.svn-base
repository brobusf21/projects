import java.util.Random;

class Graph 
{
    public int numVertex;
    public Edge edges[];

    public Graph(int numVerteces) 
    {
	int i;
	numVertex = numVerteces;
	edges = new Edge[numVerteces];
	for (i=0; i<numVertex;i++)
	{
	    edges[i] = null;
	}
    }

    public void randomize(double edgePercent) 
    {
	int i,j;
	Random generator = new Random();

	for (i=0; i<numVertex;i++)
	    for (j=0; j<numVertex; j++) 
		if ((generator.nextDouble() < edgePercent) &&
		    (i != j))
		{
		    edges[i] = new Edge(j,edges[i]);
		}
		
    }

    public void print() 
    {
	int i;
	for (i=0; i<numVertex; i++) 
        {
	    System.out.print("Vertex " + i +" Adj. List:");
	    if (edges[i] == null)
            {
		System.out.println("<empty>");
            }
	    else 
            {
		for (Edge tmp = edges[i]; tmp != null; tmp = tmp.next) 
                {
		    System.out.print(" " + tmp.neighbor);
		}
		System.out.println();
	    }
	}
    }
}