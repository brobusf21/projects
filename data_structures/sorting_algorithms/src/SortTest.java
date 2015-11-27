import java.util.Random;

/**
 * Used to test the different sorting methods. 
 * 2 different types of testing:
 * 1) Testing with a Comparable or int array
 * 2) Testing with linked lists
 * @author brandonrobinson
 *
 */
public class SortTest {

	public static void main(String args[]) {
		long startTime, endTime;
		double duration;
		Random randomGenerator = new Random();
		Sort sorter = new Sort();
		int NUMITER = 1000;
		long sT, eT;
		int i, j; 
		double d;
		int listsize = 500000;
		Comparable[] list = new Comparable[listsize];
		//int[] list = new int[listsize]; /* Used for the int[] methods */

		/* 
		 * ***************************************************
		 * Random Sorting for arrays
		 * ***************************************************
		 */
		//		sT = System.currentTimeMillis();
		//		for(i=0;i<NUMITER;i++) {
		//			for (j=0; j< listsize; j++)
		//				list[j] = randomGenerator.nextInt();
		//		}
		//		eT = System.currentTimeMillis();
		//		d = ((double) (eT - sT)) / NUMITER;
		//
		//		startTime = System.currentTimeMillis();
		//		for(i=0;i<NUMITER;i++) {
		//			for (j=0; j< listsize; j++)
		//				list[j] = randomGenerator.nextInt();
		//			sorter.selectionSort(list,0,listsize-1, false);
		//		}
		//		//sorter.print(list);
		//		endTime = System.currentTimeMillis();
		//		duration = ((double) ((endTime - startTime)) / NUMITER) - d;
		//		System.out.println("Time: " +duration);
		/* *************************************************** */
		/* Sorted Sorting for arrays
		 * ***************************************************
		 */
		//				sT = System.currentTimeMillis();
		//				for(i=0;i<NUMITER;i++) {
		//					for (j=0; j< listsize; j++)
		//						list[j] = j;
		//				}
		//				eT = System.currentTimeMillis();
		//				d = ((double) (eT - sT)) / NUMITER;
		//		
		//				startTime = System.currentTimeMillis();
		//				for(i=0;i<NUMITER;i++) {
		//					for (j=0; j< listsize; j++)
		//						list[j] = j;
		//					sorter.optimizedQuicksort(list,0,listsize-1, true);
		//				}
		//				//sorter.print(list);
		//				endTime = System.currentTimeMillis();
		//				duration = ((double) ((endTime - startTime)) / NUMITER) - d;
		//				System.out.println("Time: " +duration);
		/* LINKED LIST SORTING 
		 * *************************************
		 * Random sorting for LL
		 * *************************************
		 */
		//		LLNode headTest = sorter.createLL(new LLNode(52));
		//		sT = System.currentTimeMillis();
		//		LLNode current2 = headTest;
		//		for (j=0; j< listsize; j++) {
		//			LLNode temp2 = new LLNode(randomGenerator.nextInt());
		//			current2.setNext(temp2);
		//			current2 = current2.next();
		//		}
		//		eT = System.currentTimeMillis();
		//		d = ((double) (eT - sT)) / NUMITER;
		//
		//		LLNode head = new LLNode(52);
		//		startTime = System.currentTimeMillis();
		//		LLNode current = head;
		//		for (j=0; j< listsize; j++) {
		//			LLNode temp = new LLNode(randomGenerator.nextInt());
		//			current.setNext(temp);
		//			current = current.next();
		//		}
		//		sorter.insertionSortLL(head, false);
		//		//sorter.printLL(head);
		//		endTime = System.currentTimeMillis();
		//		duration = ((double) ((endTime - startTime)) / NUMITER) - d;
		//		System.out.println("Time: " +duration);

		/* *************************************
		 * Sorted sorting for LL
		 * *************************************
		 */
		//		LLNode headTest = sorter.createLL(new LLNode(0));
		//		sT = System.currentTimeMillis();
		//		LLNode current2 = headTest;
		//		for (j=1; j< listsize; j++) {
		//			LLNode temp2 = new LLNode(j);
		//			current2.setNext(temp2);
		//			current2 = current2.next();
		//		}
		//		eT = System.currentTimeMillis();
		//		d = ((double) (eT - sT)) / NUMITER;
		//
		//		LLNode head = new LLNode(0);
		//		startTime = System.currentTimeMillis();
		//		LLNode current = head;
		//		for (j=1; j< listsize; j++) {
		//			LLNode temp = new LLNode(j);
		//			current.setNext(temp);
		//			current = current.next();
		//		}
		//		sorter.selectionSortLL(head, false);
		//		//sorter.printLL(head);
		//		endTime = System.currentTimeMillis();
		//		duration = ((double) ((endTime - startTime)) / NUMITER) - d;
		//		System.out.println("Time: " +duration);
		//	}
	}
}
