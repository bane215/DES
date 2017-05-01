class roundFunc
{

private int expanFunc[] = {32,	1,	2,	3,	4,	5,
	4,	5,	6,	7,	8,	9,
	8,	9,	10,	11,	12,	13,
	12,	13,	14,	15,	16,	17,
	16,	17,	18,	19,	20,	21,
	20,	21,	22,	23,	24,	25,
	24,	25,	26,	27,	28,	29,
	28,	29,	30,	31,	32,	1};
	
private int straightPbox[]={16,7,20,21,29,12,28,17,
	1,15,23,26,5,18,31,10,
	2,8,24,14,32,27,3,9,
	19,13,30,6,22,11,4,25};
	
	
public int[] roundFunction(int roundInput[],int key1[])	
{
	int expan[] = new int[48];
	int xor[] = new int[48]; 
	int boxed[] = new int[32];
	int pboxed[] = new int[32];
	int i,j;
	
	
	// Step 1: EXPANSION P-box (expansion of 32 bit input to 48 bit)
	for(i=0;i<48;i++)
	{
		j=expanFunc[i];
		expan[i]=roundInput[j-1];
	}
	
	// STEP 2: XOR (xor of expanded 48-bit input with 48-bit round key)
	for(i=0;i<48;i++)
	{
		xor[i]=expan[i] ^ key1[i];
	}
	
		
	// STEP 3: S-box (pass 48-bit xor-ed input into s-box to get 32-bit output)
		sbox obj = new sbox();	
		boxed = obj.subBits(xor);
		

	// STEP 4: Straight P-box (permutation of 32-bit input)
	for(i=0;i<32;i++)
	{
		j=straightPbox[i];
		pboxed[i]=boxed[j-1];
	}
	
	return pboxed;	
}
public static void main(String[] args)
{
	
int roundInput[] = {0,0,0,0,0,0,0,1, 0,0,1,0,0,0,1,1, 0,1,0,0,0,1,0,1, 0,1,1,0,0,1,1,1};
int keys[]= {0,0,0,1,0,0,1,1, 0,0,1,1,0,1,0,0, 0,1,0,1,0,1,1,1, 0,1,1,1,1,0,0,1, 1,0,0,1,1,0,1,1, 1,0,1,1,1,1,0,0};

	System.out.println("Input:");
	for(int i=0;i<32;i++)
	{	
	if(i%4==0)
	System.out.print(" ");
	System.out.print(roundInput[i]);
	}

roundFunc obj = new roundFunc();	

int output[] = obj.roundFunction(roundInput,keys);
	
	System.out.println("\nOutput: ");
	for(int i=0;i<32;i++)
	{	
	if(i%4==0)
	System.out.print(" ");
	System.out.print(output[i]);
	}
}
}