// round key generater

public class roundKey
{

public  int[][] genKeys(int key[])
{
	
	int parta[] = new int[28];
	int partb[] = new int[28];
	int pcone[] = new int[56];
	int pctwo[] = new int[48];
	int roundpctwo[] = new int[56];
	int finalRoundKeys[][] = new int[16][48];
	int i,j,k,round,temp;
	int permone[] = 
{ 57,	49,	41,	33,	25,	17,	9,
1,	58,	50,	42,	34,	26,	18,
10,	2,	59,	51,	43,	35,	27,
19,	11,	3,	60,	52,	44,	36,
63,	55,	47,	39,	31,	23,	15,
7,	62,	54,	46,	38,	30,	22,
14,	6,	61,	53,	45,	37,	29,
21,	13,	5,	28,	20,	12,	4
};
	int permtwo[] = 
{ 14,	17,	11,	24,	1,	5,
3,	28,	15,	6,	21,	10,
23,	19,	12,	4,	26,	8,
16,	7,	27,	20,	13,	2,
41,	52,	31,	37,	47,	55,
30,	40,	51,	45,	33,	48,
44,	49,	39,	56,	34,	53,
46,	42,	50,	36,	29,	32
};
	
	
	
	// permuted choice one
	for(i=0;i<56;i++)
	{
		j=permone[i];
		pcone[i]=key[j-1];
	}
	
	// divide into two parts
	for(i=0;i<28;i++)
	parta[i]=pcone[i];
	
	for(i=28,j=0;i<56;i++,j++)	
	partb[j]=pcone[i];	
	
	for(round=0;round<16;round++)
	{
	//  ROUND n STARTS				
	
	if((round==0) || (round==1) || (round==8) || (round==15))				
					//	if round is 1,2,9,16 shift 1 bit, else 2 bits
	{

	// circular left shift on first part
	temp = parta[0];
	for(i=0;i<27;i++)
	{
		parta[i]=parta[i+1];
	}
	parta[27] = temp;

	// circular left shift on second part
	temp = partb[0];
	for(i=0;i<27;i++)
	{
		partb[i]=partb[i+1];
	}
	partb[27] = temp;

	}	// shift of round 1,2,9 or 16 complete
	
	else
	{
		
	// circular left shift 1st bit on first part
	temp = parta[0];
	for(i=0;i<27;i++)
	{
		parta[i]=parta[i+1];
	}
	parta[i] = temp;
	
		
	// circular left shift 2nd bit on first part
	temp = parta[0];
	for(i=0;i<27;i++)
	{
		parta[i]=parta[i+1];
	}
	parta[i] = temp;
	
	// circular left shift 1st bit on second part
	temp = partb[0];
	for(i=0;i<27;i++)
	{
		partb[i]=partb[i+1];
	}
	partb[i] = temp;
		
	// circular left shift 2nd bit on second part
	temp = partb[0];
	for(i=0;i<27;i++)
	{
		partb[i]=partb[i+1];
	}
	partb[i] = temp;
		

	}	// shifting of 2-bits complete
		
	// concat both parts for input to pc-2
	for(i=0;i<28;i++)
	roundpctwo[i]=parta[i];
	
	for(i=0,j=28;i<28;i++,j++)
	roundpctwo[j]=partb[i];
	
	
	// permuted choice two
	for(i=0;i<48;i++)
	{
		j=permtwo[i];
		pctwo[i]=roundpctwo[j-1];
	}
	
	
	// storing round keys
	for(i=0;i<48;i++)
	{
	finalRoundKeys[round][i] = pctwo[i];	
	}
	
	}	// rounds end


	return finalRoundKeys;
}
public static void main(String[] args)
{
roundKey obj = new roundKey();
int key[]= {0,0,0,1,0,0,1,1, 0,0,1,1,0,1,0,0, 0,1,0,1,0,1,1,1, 0,1,1,1,1,0,0,1, 1,0,0,1,1,0,1,1, 1,0,1,1,1,1,0,0, 1,1,0,1,1,1,1,1, 1,1,1,1,0,0,0,1};

int roundKeys[][] = obj.genKeys(key);

System.out.println("***Round Keys***");
for(int i=0;i<16;i++)
{
System.out.println("\n\nRound "+(i+1)+" keys : ");
for(int j=0;j<48;j++)
{
if(j%6==0)
System.out.print(" ");
System.out.print(roundKeys[i][j]);
}
}

}

}