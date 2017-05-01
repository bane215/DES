class desDecrypt
{

public int[] decrypt(int ct[],int key[])
{
	

	int initPerm[]={58,	50,	42,	34,	26,	18,	10,	2,
	60,	52,	44,	36,	28,	20,	12,	4,
	62,	54,	46,	38,	30,	22,	14,	6,
	64,	56,	48,	40,	32,	24,	16,	8,
	57,	49,	41,	33,	25,	17,	9,	1,
	59,	51,	43,	35,	27,	19,	11,	3,
	61,	53,	45,	37,	29,	21,	13,	5,
	63,	55,	47,	39,	31,	23,	15,	7};
	
	int finalPerm[]={40,	8,	48,	16,	56,	24,	64,	32,
39,	7,	47,	15,	55,	23,	63,	31,
38,	6,	46,	14,	54,	22,	62,	30,
37,	5,	45,	13,	53,	21,	61,	29,
36,	4,	44,	12,	52,	20,	60,	28,
35,	3,	43,	11,	51,	19,	59,	27,
34,	2,	42,	10,	50,	18,	58,	26,
33,	1,	41,	9,	49,	17,	57,	25};
	
	int initPermRes[] = new int[64];
	int roundInputR[] = new int[32];
	int roundInputL[] = new int[32];
	int roundXor[] = new int[32];
	int afterRounds[] = new int[64];
	int finalPermRes[] = new int[64];
	int i=0,j=0,round,revKey;
		

	// key generation
	roundKey obj = new roundKey();
	int keys[][] = obj.genKeys(key);		// 16*48
		
	// initial permutation on ciphertext
	for(i=0;i<64;i++)
	{
		j=initPerm[i];
		initPermRes[i]=ct[j-1];
	}

	// preparing round function input (converting initial permuted 64 bit into two-part 32 bits)
	for(i=0;i<32;i++)
	roundInputL[i]=initPermRes[i];
	

	for(i=32,j=0;i<64;i++,j++)
	roundInputR[j]=initPermRes[i];
				
	// Rounds Start
	for(round=0,revKey=15;round<16;round++,revKey--)
	{
	
	// round function
	roundFunc robj = new roundFunc();
	int roundOut[] = robj.roundFunction(roundInputR,keys[revKey]);
	
	// xor left 32-bit with round function output
	for(i=0;i<32;i++)
	roundXor[i] = roundInputL[i] ^ roundOut[i];
	
	// make rigth left & left right
	for(i=0;i<32;i++)
	roundInputL[i] = roundInputR[i];
		
	for(i=0;i<32;i++)
	roundInputR[i] = roundXor[i];
	
	}	// Rounds complete
	
	// concatenate left and right 32-bit parts after swapping both the parts
	for(i=0;i<32;i++)
	afterRounds[i] = roundInputR[i];
	
	for(i=0,j=32;i<32;i++,j++)
	afterRounds[j] = roundInputL[i];
	
	// final permutation
	for(i=0;i<64;i++)
	{
		j=finalPerm[i];
		finalPermRes[i]=afterRounds[j-1];
	}	
	
	return finalPermRes;
}

public static void main(String[] args)
{

int ct[]= {1,0,0,0,0,1,0,1, 1,1,1,0,1,0,0,0, 0,0,0,1,0,0,1,1, 0,1,0,1,0,1,0,0, 0,0,0,0,1,1,1,1, 0,0,0,0,1,0,1,0, 1,0,1,1,0,1,0,0, 0,0,0,0,0,1,0,1};

int key[]= {0,0,0,1,0,0,1,1, 0,0,1,1,0,1,0,0, 0,1,0,1,0,1,1,1, 0,1,1,1,1,0,0,1, 1,0,0,1,1,0,1,1, 1,0,1,1,1,1,0,0, 1,1,0,1,1,1,1,1,  1,1,1,1,0,0,0,1};

desDecrypt obj = new desDecrypt();

int pt[] = obj.decrypt(ct,key);

System.out.println("\n\n********DES DECRYPTION********");

System.out.println("\n\nCiphertext: ");
for(int i=0;i<64;i++)
{
if(i%8==0)
System.out.print(" ");
System.out.print(ct[i]);
}

System.out.println("\n\nPlaintext: ");
for(int i=0;i<64;i++)
{
if(i%8==0)
System.out.print(" ");
System.out.print(pt[i]);
}

}
}