class sbox
{

int[] toBits(int i)
{
int j,p;
String s= Integer.toBinaryString(i);
//System.out.println("\n "+i+" as binary: "+s);
int l = s.length();
int len = 4-l;

int a[] = new int[4];

if(l!=4)
{
for(j=0;j<len;j++)
a[j] = 0;
for(p=0;p<l;p++)
a[j++] = Character.getNumericValue((s.charAt(p)));
}
else
{
for(j=0;j<l;j++)
a[j] =  Character.getNumericValue((s.charAt(j)));
}
//System.out.print("padded binary: ");
//for(int x:a) System.out.print(x);
return a;
}

private int sboxes[][][] = {	{	{14,  4, 13,  1,  2, 15, 11,  8,  3, 10,  6, 12,  5,  9,  0,  7},
		 	{0, 15,  7,  4, 14,  2, 13,  1, 10,  6, 12, 11,  9,  5,  3,  8},
		 	{4,  1, 14,  8, 13,  6,  2, 11, 15, 12,  9,  7,  3, 10,  5,  0},
			{15, 12,  8,  2,  4,  9,  1,  7,  5, 11,  3, 14, 10,  0,  6, 13}	},

		 {	{15,  1,  8, 14,  6, 11,  3,  4,  9,  7,  2, 13, 12,  0,  5, 10},
		 	{3, 13,  4,  7, 15,  2,  8, 14, 12,  0,  1, 10,  6,  9, 11,  5},
		 	{0, 14,  7, 11, 10,  4, 13,  1,  5,  8, 12,  6,  9,  3,  2, 15},
			{13,  8, 10,  1,  3, 15,  4,  2, 11,  6,  7, 12,  0,  5, 14,  9}	},

		 {	{10,  0,  9, 14,  6,  3, 15,  5,  1, 13, 12,  7, 11,  4,  2,  8},
			{13,  7,  0,  9,  3,  4,  6, 10,  2,  8,  5, 14, 12, 11, 15,  1},
			{13,  6,  4,  9,  8, 15,  3,  0, 11,  1,  2, 12,  5, 10, 14,  7},
			{1, 10, 13,  0,  6,  9,  8,  7,  4, 15, 14,  3, 11,  5,  2, 12}	},

		 {	{7, 13, 14,  3,  0,  6,  9, 10,  1,  2,  8,  5, 11, 12,  4, 15},
			{13,  8, 11,  5,  6, 15,  0,  3,  4,  7,  2, 12,  1, 10, 14,  9},
			{10,  6,  9,  0, 12, 11,  7, 13, 15,  1,  3, 14,  5,  2,  8,  4},
			{3, 15,  0,  6, 10,  1, 13,  8,  9,  4,  5, 11, 12,  7,  2, 14}	},

		 {	 {2, 12,  4,  1,  7, 10, 11,  6,  8,  5,  3, 15, 13,  0, 14,  9},
			 {14, 11,  2, 12,  4,  7, 13,  1,  5,  0, 15, 10,  3,  9,  8,  6},
		 	 {4,  2,  1, 11, 10, 13,  7,  8, 15,  9, 12,  5,  6,  3,  0, 14},
			 {11,  8, 12,  7,  1, 14,  2, 13,  6, 15,  0,  9, 10,  4,  5,  3}	},

		 {	{12,  1, 10, 15,  9,  2,  6,  8,  0, 13,  3,  4, 14,  7,  5, 11},
			{10, 15,  4,  2,  7, 12,  9,  5,  6,  1, 13, 14,  0, 11,  3,  8},
		 	{9, 14, 15,  5,  2,  8, 12,  3,  7,  0,  4, 10,  1, 13, 11,  6},
			{4,  3,  2, 12,  9,  5, 15, 10, 11, 14,  1,  7,  6,  0,  8, 13}	},

		 {	 {4, 11,  2, 14, 15,  0,  8, 13,  3, 12,  9,  7,  5, 10,  6,  1},
			 {13,  0, 11,  7,  4,  9,  1, 10, 14,  3,  5, 12,  2, 15,  8,  6},
			 {1,  4, 11, 13, 12,  3,  7, 14, 10, 15,  6,  8,  0,  5,  9,  2},
			 {6, 11, 13,  8,  1,  4, 10,  7,  9,  5,  0, 15, 14,  2,  3, 12}	},

		 { 	{13,  2,  8,  4,  6, 15, 11,  1, 10,  9,  3, 14,  5,  0, 12,  7},
		 	{1, 15, 13,  8, 10,  3,  7,  4, 12,  5,  6, 11,  0, 14,  9,  2},
			{7, 11,  4,  1,  9, 12, 14,  2,  0,  6, 10, 13, 15,  3,  5,  8},
		 	{2,  1, 14,  7,  4, 10,  8, 13, 15, 12,  9,  0,  3,  5,  6, 11}	}	};

public int[] subBits(int in[])
{
	int bin=0,i,j,k=-1;
	int temp,rem;
	char ch;
	int a[][] = new int[8][6];
	int b[] = new int[8];
	int boxed[] = new int[32];

	// divide 48 bit input into 8*6 for each s-boxes
	for(i=0;i<8;i++)
	{
	for(j=0;j<6;j++)
	{
	a[i][j] = in[++k];
	}
	}
	
/*	// print input 48-bits as 8*6
	System.out.println("\nInput 48-bits as 8*6:\n");
	for(int m=0;m<8;m++)
	{
	System.out.print("\t");
	for(int n=0;n<6;n++)
	System.out.print(a[m][n]);
	}
*/	
	// substitute bytes
	for(i=0;i<8;i++)
	{
	int r = a[i][0]*2 + a[i][5]*1;
	int c = a[i][1]*8 + a[i][2]*4 + a[i][3]*2 + a[i][4]*1;
	b[i] = sboxes[i][r][c];
	}	
	
	// convert values into binary
	sbox ob = new sbox();
	k=-1;
	for(i=0;i<8;i++)
	{
	int r[] = ob.toBits(b[i]);
	
/*	System.out.print("\ni = "+i+"\tb[i] = "+b[i]+"\tb[i] in binary = ");
	for(int l:r)
	System.out.print(l);
*/	
	for(j=0;j<4;j++)
	boxed[++k]=r[j];
	}
	
	
	return boxed; 
}
public static void main(String[] args)
{
sbox obj = new sbox();
int inp[] = {1,0,1,0,0,0,1,0, 1,0,0,1,1,1,0,1, 1,0,0,1,1,0,0,0, 0,0,1,1,0,1,0,1, 1,0,0,1,1,1,0,1, 1,0,0,1,1,0,0,0};

System.out.println("Input: ");
for(int i =0;i<48;i++)
{
if(i%6==0)
System.out.print("  ");
System.out.print(inp[i]);
}

int out[] = obj.subBits(inp);

System.out.println("\nOutput: ");
for(int i =0;i<32;i++)
{
if(i%4==0)
System.out.print("  ");
System.out.print(out[i]);
}

}
}