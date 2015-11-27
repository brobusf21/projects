/*
 * Brandon Robinson
 * CS 245 / Project 3
 * 20 April 2015
 */
public class Sort implements SortInterface {

	/**
	 * Empty constructor
	 */
	public Sort() {	
	}

	/**
	 * Insertion sort iterates through the provided array, locating the lowest element and then 
	 * moving it furthest most left position. 
	 * @param Comparable[] array
	 * @param int lowindex Lower position
	 * @param int highindex High position
	 * @param boolean reversed False: regular sorting. True: inverse sorting.
	 */
	@Override
	public void insertionSort(Comparable[] array, int lowindex, int highindex, boolean reversed) {
		int i, j;
		Comparable current;
		for(i = lowindex; i < highindex+1; i++) {
			current = array[i];
			if (reversed == true) {
				for (j=i-1; j >=lowindex && array[j].compareTo(current) == -1; j--)
					array[j+1] = array[j];
			} else {
				for (j=i-1; j >=lowindex && array[j].compareTo(current) == 1; j--)
					array[j+1] = array[j];
			}
			array[j+1] = current;
		}
	}

	/**
	 * Selection sort locates the lowest element and then swaps it with any elements that are lower
	 * in the rest of the list.
	 * @param Comparable[] array
	 * @param int lowindex Lower position.
	 * @param int highindex High position.
	 * @param boolean reversed False: regular sorting. True: inverse sorting.
	 */
	@Override
	public void selectionSort(Comparable[] array, int lowindex, int highindex, boolean reversed) {
		int i,j;
		int lowest;
		Comparable tmp;
		for (i=lowindex; i < highindex; i++) {
			lowest = i;
			for (j=i+1; j < highindex+1; j++)
				if (reversed == true) {
					if (array[lowest].compareTo(array[j]) == -1)
						lowest = j;
				} else {
					if (array[lowest].compareTo(array[j]) == 1)
						lowest = j;
				}
			tmp = array[i];
			array[i] = array[lowest];
			array[lowest] = tmp;
		}
	}

	/**
	 * Shell sort utilizes insertion sort to do most of the work by calling a private helper method.
	 * It is considered to be an in position sorting algorithm. It starts off by comparing elements 
	 * furthest from one another or what is called the gap. The gap is reduced as the algorithm is run.
	 * @param Comparable[] array
	 * @param int lowindex Lower position
	 * @param int highindex High position
	 * @param boolean reversed False: regular sorting. True: inverse sorting.
	 */
	@Override
	public void shellSort(Comparable[] array, int lowindex, int highindex, boolean reversed) {
		int increment, offset, k, gap = 0;
		for (k = 1; k < highindex; k= (k*2)+1) {
			gap = k;
		}
		for (increment = gap; increment >  0; increment = increment / 2) {
			for (offset=0; offset < increment; offset++) {
				shellInsertionSort(array, offset, increment, highindex, reversed);
			}
		}
	}

	/**
	 * Helper function to shellSort().
	 * @param Comparable[] array
	 * @param int lowindex Lower position
	 * @param int highindex High position
	 * @param boolean reversed False: regular sorting. True: inverse sorting.
	 */
	private void shellInsertionSort(Comparable[] A, int offset, int increment, int highindex, boolean reversed) {
		int i,j;
		for (i=offset + increment; i<highindex+1; i=i+increment) {
			Comparable insert = A[i];
			if (reversed == true) {
				for (j=i-increment; j>=0 && A[j].compareTo(insert) == -1; j = j - increment) {
					A[j+increment] = A[j];
				}
			} else {
				for (j=i-increment; j>=0 && A[j].compareTo(insert) == 1; j = j - increment) {
					A[j+increment] = A[j];
				}
			}
			A[j+increment] = insert;
		}
	}

	/**
	 * Bucket Sort utilizes an array that has an evenly distributed number of buckets. Each bucket 
	 * contains a linked list that stores different values based on the number.
	 * @param int[] array
	 * @param int lowindex Lower position
	 * @param int highindex High position
	 * @param boolean reversed False: regular sorting. True: inverse sorting.
	 */
	@Override
	public void bucketSort(int[] array, int lowindex, int highindex, boolean reversed) {
		int i;
		int max = 0;
		int range = ((highindex + 1) - lowindex);
		int buckets = range / 2 ;
		LLNode bucketList[] = new LLNode[buckets]; 

		for (i=lowindex; i < highindex + 1; i++) {
			if (array[i] > max) 
				max = array[i];
		}
		for (int j = lowindex; j < highindex+1; j++)	{
			int value = array[j];
			LLNode node = new LLNode(value);
			int location = Math.abs(value * buckets / (max+1));
			//System.out.println(location);
			LLNode head = bucketList[location];
			LLNode temp = head;
			bucketList[location] = add(temp, node, reversed);
		}
		int a = lowindex;
		if (reversed == false) {
			for (int k = 0; k < bucketList.length; k++) {
				while (bucketList[k] != null) {
					array[a] = (Integer) bucketList[k].elem();
					a++;
					bucketList[k] = bucketList[k].next();
				}
			}
		} else {
			for (int k = bucketList.length-1; k >=0; k--) {
				while (bucketList[k] != null) {
					array[a] = (Integer) bucketList[k].elem();
					a++;
					bucketList[k] = bucketList[k].next();
				}
			}
		}
	}

	/**
	 * Helper function to bucketSort().
	 * @param current Pointer to head of list.
	 * @param node New node.
	 * @param reversed True or false boolean.
	 * @return
	 */
	private LLNode add(LLNode current, LLNode node, boolean reversed) {
		if ( reversed == false) {
			if (current == null) { // null
				current = node;
			} else if (current.elem().compareTo(node.elem()) == 1){ // Head larger
				node.setNext(current);
				current = node;
			} else {
				LLNode prev = current;
				LLNode next = current.next();
				while (next != null) {
					if (prev.elem().compareTo(node.elem()) == 1) {
						break;
					}
					prev = prev.next();
					next = next.next();
				}
				node.setNext(next);
				prev.setNext(node);
			}
		} else {
			if (current == null) {
				current = node;
			} else if (current.elem().compareTo(node.elem()) == -1){
				node.setNext(current);
				current = node;
			} else {
				LLNode prev = current;
				LLNode next = current.next();
				while (next != null) {
					if (prev.elem().compareTo(node.elem()) == -1) {
						break;
					}
					prev = prev.next();
					next = next.next();
				}
				node.setNext(next);
				prev.setNext(node);
			}
		}
		return current;
	}

	/**
	 * Heap Sort utilizes a helper function to compare elements. 
	 * @param Comparable[] array
	 * @param int lowindex Lower position
	 * @param int highindex High position
	 * @param boolean reversed False: regular sorting. True: inverse sorting.
	 */
	@Override
	public void heapSort(Comparable[] array, int lowindex, int highindex, boolean reversed) {
		int index = (highindex - lowindex);

		for (int i = index / 2; i >= 0; i--) {
			heapHelper(array, i, index, reversed);
		}
		for(int i = index; i > 0; i--) {
			swap(array, 0, i);
			index--;
			heapHelper(array, 0, index, reversed);
		}
	}

	private void heapHelper(Comparable[] array, int i, int index, boolean reversed) {
		int greatest = i;
		int leftIndex = i*2;
		int rightIndex = leftIndex + 1;
		if (reversed == false) {
			if (leftIndex <= index && array[leftIndex].compareTo(array[greatest]) == 1) {
				greatest = leftIndex;
			}
			if (rightIndex <= index && array[rightIndex].compareTo(array[greatest]) == 1) {
				greatest = rightIndex;
			}
			if (greatest != i) {
				swap(array, i, greatest);
				heapHelper(array, greatest, index, reversed);
			}
		} else {
			if (leftIndex <= index && array[leftIndex].compareTo(array[greatest]) == -1) {
				greatest = leftIndex;
			}
			if (rightIndex <= index && array[rightIndex].compareTo(array[greatest]) == -1) {
				greatest = rightIndex;
			}
			if (greatest != i) {
				swap(array, i, greatest);
				heapHelper(array, greatest, index, reversed);
			}
		}
	}
	/**
	 * Quick sort sorts a Comparable[] array first by calculating the size of the array and 
	 * then determining it's pivot. Using that pivot, the function sorts the array. It finds 
	 * its median by calling midOf3 method which picks 3 elements and calculates the median. 
	 * If the list is very short, special cases are used to sort the list to prevent any errors. 
	 * @param Comparable[] array
	 * @param int lowindex Lower position
	 * @param int highindex High position
	 * @param boolean reversed False: regular sorting. True: inverse sorting.
	 */
	@Override
	public void quicksort(Comparable[] array, int lowindex, int highindex, boolean reversed) {
		int size = (highindex - lowindex) + 1;
		if (size <= 3) {
			shortSort(array, size, lowindex, highindex);
		} else {
			int i = lowindex;
			int j = highindex;
			int pivot = (Integer) midOf3(array, lowindex, highindex);
			if (reversed == false) {
				while (i <= j) {
					while (array[i].compareTo(pivot) == -1) {
						i++;
					}
					while (array[j].compareTo(pivot) == 1) {
						j--;
					}
					if (i <= j) {
						swap(array, i, j);
						i++;
						j--;
					}
				}
			} else {
				while (array[i].compareTo(pivot) == 1) {
					i++;
				}
				while (array[j].compareTo(pivot) == -1) {
					j--;
				}
				if (i <= j) {
					swap(array, i, j);
					i++;
					j--;
				}
			}
			if (lowindex < j)
				quicksort(array, lowindex, j, reversed);
			if (i < highindex)
				quicksort(array, i, highindex, reversed);
		}
	}

	/**
	 * Helper function to quick sort. Sorts smaller arrays with conditions.
	 * @param array
	 * @param size
	 * @param lowindex
	 * @param highindex
	 */
	private void shortSort(Comparable[] array, int size, int lowindex, int highindex) {
		if (size <= 1) {
			return;
		}
		if (size == 2) {
			if (array[lowindex].compareTo(array[highindex]) == 1) {
				swap(array, lowindex, highindex);
			}
			return;
		} else {
			if (array[lowindex].compareTo(array[highindex-1]) == 1)
				swap(array, lowindex, highindex-1);
			if (array[lowindex].compareTo(array[highindex]) == 1) 
				swap(array, lowindex, highindex);
			if (array[highindex-1].compareTo(array[highindex]) == 1) 
				swap(array, highindex-1, highindex);
		}
	}
	/**
	 * Basic swap function.
	 * @param array
	 * @param low
	 * @param high
	 * @return
	 */
	private Comparable[] swap(Comparable[] array, int low, int high) {
		Comparable temp = array[low];
		array[low] = array[high];
		array[high] = temp;
		return array;
	}

	/**
	 * Median of three method that picks first, middle, and end elements and 
	 * calculates the median. Used for locating the correct pivot. 
	 * @param array
	 * @param low
	 * @param high
	 * @return
	 */
	private Comparable midOf3(Comparable[] array, int low, int high) {
		int middle = (low+high) / 2;
		if (array[low].compareTo(array[middle]) == 1) 
			swap(array, low, high);
		if (array[low].compareTo(array[high]) == 1)
			swap(array, low, high);
		if (array[middle].compareTo(array[high]) == 1)
			swap(array, middle, high);
		swap(array, middle, high-1);
		return array[high-1];
	}

	/**
	 * Radix sort sorts the int array by using the number of elements as the base and then
	 * finding k. K is the max number of digits that are in the array. Once k is found, 
	 * the method can then utilize a counting array to sort the elements and put them back
	 * into a new array. 
	 * @param int[] array
	 * @param int lowindex Lower position
	 * @param int highindex High position
	 * @param boolean reversed False: regular sorting. True: inverse sorting.
	 */
	@Override
	public void radixSort(int[] array, int lowindex, int highindex, boolean reversed) {
		int n = (highindex - lowindex) + 1;
		int[] count = new int[n];
		int[] B = new int[n];
		int k = findK(array, lowindex, highindex);

		for(int i=0, rtok = 1; i<k; i++, rtok *= n) {
			for (int b = 0; b < count.length; b++) 
				count[b] = 0;
			for(int j=lowindex; j<highindex+1; j++) {
				count[Math.abs((array[j]/rtok)%n)]++;
			}
			if (reversed == false) {
				for(int j=lowindex+1; j<n; j++)
					count[j] = count[j-1] + count[j];
			} else if (reversed == true) {
				for(int j=count.length-2; j>=0; j--)
					count[j] = count[j+1] + count[j];
			}
			for(int j=highindex; j>=0; j--){
				B[--count[Math.abs((array[j]/rtok)%n)]] = array[j];
			}
			for(int j=lowindex; j<highindex+1; j++)
				array[j] = B[j];
		}
	}
	
	/**
	 * Private helper function that finds K first by finding the max 
	 * number and then counting how many digits it is. 
	 * @param array
	 * @param low
	 * @param high
	 * @return
	 */
	private int findK(int[] array, int low, int high) {
		int max = 0;
		for (int i = low; i < high+1; i++) {
			if (array[i] > max) {
				max = array[i];
			}
		}
		int count = 0; 
		while (max != 0) {
			max /= 10;
			count++;
		}
		return count;
	}

	/**
	 * This selection sort method sorts linked list elements. It is very similiar to
	 * the original selection sort however it iterates through a linked list rather than 
	 * an array.
	 * @param LLNode 
	 * @param boolean reversed False: regular sorting. True: inverse sorting.
	 */
	@Override
	public LLNode selectionSortLL(LLNode head, boolean reversed) {
		for (LLNode current = head; current != null; current = current.next()) {
			LLNode min = current;
			for (LLNode current2 = current; current2 != null; current2 = current2.next()) {
				if (reversed == false) {
					if (min.elem().compareTo(current2.elem()) == 1)  {
						min = current2;
					}
				} else {
					if (min.elem().compareTo(current2.elem()) == -1)  {
						min = current2;
					}
				}
			}
			LLNode temp = new LLNode((Integer) current.elem());
			current.setElem(min.elem());
			min.setElem(temp.elem());
		}
		return head;
	}

	/**
	 * This method is not complete and was accidently overlooked. 
	 */
	@Override
	public LLNode mergeSortLL(LLNode current, boolean reversed) {
		return null;
	}

	/**
	 * This insertion sort method sorts linked list elements. It is very similiar 
	 * to the original insertion sort with very small adjustemnts to accomodate
	 * nodes rather than array elements. 
	 * @param LLNode 
	 * @param boolean reversed False: regular sorting. True: inverse sorting.
	 */
	@Override
	public LLNode insertionSortLL(LLNode current, boolean reversed) {
		LLNode head = current;
		LLNode insertionPtr = head;
		current = current.next();

		while (current != null) {
			insertionPtr = head;
			while (insertionPtr != current) {
				if (reversed == false) {
					if (insertionPtr.elem().compareTo(current.elem()) == 1) {
						Comparable temp = current.elem();
						current.setElem(insertionPtr.elem());
						insertionPtr.setElem(temp);
					} else {
						insertionPtr = insertionPtr.next();
					}
				} else {
					if (insertionPtr.elem().compareTo(current.elem()) == -1) {
						Comparable temp = current.elem();
						current.setElem(insertionPtr.elem());
						insertionPtr.setElem(temp);
					} else {
						insertionPtr = insertionPtr.next();
					}
				}
			}
			current = current.next();
		}
		return head;
	}

	/**
	 * This optimized quick sort method utilizes an insertion sort that is further discussed in the provided PDF. 
	 * It uses a threshold of size 10 to stop recursion and utilize an insertion sort method to sort arrays
	 * that are equal to or less than size 10. 
	 * @param Comparable[] array
	 * @param int lowindex Lower position
	 * @param int highindex High position
	 * @param boolean reversed False: regular sorting. True: inverse sorting.
	 */
	@Override
	public void optimizedQuicksort(Comparable[] array, int lowindex, int highindex, boolean reversed) {
		int size = (highindex - lowindex) + 1;
		if (size <= 11) {
			insertionSort(array, lowindex, highindex, reversed);
		} else {
			if (lowindex < highindex) {
				int pivot = partition(array, lowindex, highindex);
				optimizedQuicksort(array, lowindex, pivot-1, reversed);
				optimizedQuicksort(array, pivot+1, highindex, reversed);
			}
		}
	}
	/**
	 * Helper function to optimizedQuickSort. Partitions the array.
	 * @param A
	 * @param low
	 * @param high
	 * @return
	 */
	private int partition(Comparable A[], int low, int high) {
		Comparable pivot;
		Comparable tmp;
		int max = high;
		int mid = (low + high) / 2;
		tmp = A[mid];
		A[mid] = A[high];
		A[high] = tmp;
		pivot = A[high];
		low--;
		do {
			while(A[++low].compareTo(pivot) == -1);
			while((low < high) && (A[--high].compareTo(pivot) == 1));
			tmp = A[low];
			A[low] = A[high];
			A[high] = tmp;
		} while (low < high);
		tmp = A[low];
		A[low] = A[max];
		A[max] = tmp;
		return low;
	}
}
