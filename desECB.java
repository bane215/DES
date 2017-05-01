import java.util.Scanner;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
class desECB
{

// get a particular bit from a position in byte array
private static int getBit(byte[] data, int pos) {
      int posByte = pos/8; 
      int posBit = pos%8;
      byte valByte = data[posByte];
      int valInt = valByte>>(8-(posBit+1)) & 0x0001;
      return valInt;
   }

// convert int array of 0's and 1's into byte array
static byte[] btary(int bits[])
{   

byte[] bytes = new byte[bits.length / 8];
for (int i = 0; i < bytes.length; i++) {
    int b = 0;
    for (int j = 0; j < 8; j++)
        b = (b << 1) + bits[i * 8 + j];
    bytes[i] = (byte)b;
}
return bytes;
}


public static void main(String[] args)
{
Scanner sc = new Scanner(System.in);
int i,j,k;

System.out.println("\nEnter the plaintext: ");
String text = sc.nextLine();	
//	System.out.println("\nEntered text: "+text);
int txtlen = text.length();
//	System.out.println("\nEntered text length: "+txtlen);
System.out.println("\nEnter the key: ");
String key = sc.nextLine();
//	System.out.println("\nEntered key: "+key);
int keylen = key.length();
//	System.out.println("\nEntered key length: "+keylen);

// convert message to binary
byte[] txtbytes = text.getBytes(StandardCharsets.ISO_8859_1);
/*	System.out.println("\nSize of byte array having plaintext: "+txtbytes.length);
	System.out.println("\nPlaintext as binary:\n");
	for(byte b: txtbytes)
	{
	String s1 = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
	System.out.print(s1+"\t");
	}
	System.out.println();
*/

// convert key to binary
byte[] keybytes = key.getBytes(StandardCharsets.ISO_8859_1);
/*	System.out.println("\nSize of byte array having key: "+keybytes.length);
	System.out.println("\nKey as binary:\n");
	for(byte b: keybytes)
	{
	String s1 = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
	System.out.print(s1+"\t");
	}
	System.out.println();
*/

// convert message (byte[]) into int[] and pad with 0's if necessary
int mod = (txtbytes.length*8) % 64; 
int blk = (txtbytes.length*8) / 64; 
int blocks;
int bitNum;
int msgInt[][];			//   plaintext
//int padLen=0;

if(mod==0)
{
blocks=blk;
bitNum = blocks * 64;
msgInt = new int[blocks][64];

k=0;
for(i=0;i<blocks;i++)
{
for(j=0;j<64;j++)
{
msgInt[i][j] = desECB.getBit(txtbytes,k++);
}
}

/*	System.out.println("\nPlaintext converted into int and padded (if any): \n");
	for(i=0;i<blocks;i++)
	{
	for(j=0;j<64;j++)
	{
	if(j%8==0)
	System.out.print(" ");
	System.out.print(msgInt[i][j]);
	}
	}
	System.out.println();
*/
}


else if((mod<64) && (blk==0))
{
blocks = 1;
bitNum = mod;
msgInt = new int[1][64];

for(i=0;i<mod;i++)
msgInt[0][i] = desECB.getBit(txtbytes,i);

for(i=mod;i<64;i++)
{
msgInt[0][i] = 0;
//padLen++;
}
/*	System.out.println("\nPlaintext converted into int and padded: \n");
	for(i=0;i<blocks;i++)
	{
	for(j=0;j<64;j++)
	{
	if(j%8==0)
	System.out.print(" ");
	System.out.print(msgInt[i][j]);
	}
	}
	System.out.println();
*/
}


else if(((mod>0) && (mod<64)) && (blk>0))
{
blocks = blk+1;
int pad = 64 - mod;
bitNum = (blocks-1)*64 + mod;
msgInt = new int[blocks][64];

k=0;
for(i=0;i<(blocks-1);i++)
{
for(j=0;j<64;j++)
{
msgInt[i][j] = desECB.getBit(txtbytes,k++);
}
}

for(i=0;i<mod;i++)
msgInt[blocks-1][i] = desECB.getBit(txtbytes,k++);

for(i=mod;i<64;i++)
{
msgInt[blocks-1][i] = 0;
//padLen++;
}
/*	System.out.println("\nPlaintext converted into int and padded: \n");
	for(i=0;i<blocks;i++)
	{
	for(j=0;j<64;j++)
	{
	if(j%8==0)
	System.out.print(" ");
	System.out.print(msgInt[i][j]);
	}
	}
	System.out.println();
*/
}

else
{
msgInt = new int[1][1];
msgInt[0][0] = 0;

//	System.out.println("\nThis should never print: \n");
	
}



// convert key (byte[]) into int[] and pad with 0's if necessary
int keyBit = keybytes.length*8;
int roundKey[] = new int[64];				// roundkey

if(keyBit<64)
{
for(i=0;i<keyBit;i++)
roundKey[i] = desECB.getBit(keybytes,i);

for(i=keyBit;i<64;i++)
roundKey[i] = 0;

/*	System.out.println("\nKey converted into int and padded:\n");
	for(i=0;i<roundKey.length;i++)
	{
	if(i%8==0)
	System.out.print(" ");
	System.out.print(roundKey[i]);
	}
*/
}

else if(keyBit>64)					// drop extra bits
{
for(i=0;i<64;i++)
roundKey[i] = desECB.getBit(keybytes,i);

/*	System.out.println("\nKey converted into int and truncated:\n");
	for(i=0;i<roundKey.length;i++)
	{
	if(i%8==0)
	System.out.print(" ");
	System.out.print(roundKey[i]);
	}
*/
}

// converting txtLen of original message into int array for encryption
byte[] txtlenByte = ByteBuffer.allocate(8).putInt(txtlen).array();
int msgTxtLen[] = new int[64];
for(i=0;i<64;i++)
msgTxtLen[i] = desECB.getBit(txtlenByte,i);

// ENCRYPTION
desEncrypt obj = new desEncrypt();
int x= (msgInt.length)+1;	// one for padLen block
//	System.out.println("\nNumber of input blocks for encryption: "+x);

int[][] ct = new int[x][64];
StringBuilder sb=new StringBuilder();

// encrypt msg length
ct[0] = obj.encrypt(msgTxtLen,roundKey);

// encrypt msg
for(i=1;i<x;i++)
{
ct[i] = obj.encrypt(msgInt[i-1],roundKey);
}

/*	System.out.println("\nBlockwise input values for encryption: ");
	for(i=0;i<x-1;i++)
	{
	System.out.println("\nBlock: "+(i+1));
	for(j=0;j<64;j++)
	{
	if(j%8==0)
	System.out.print(" ");
	System.out.print(msgInt[i][j]);
	}
	}
	
	System.out.println("\n\nBlockwise output values after encryption: ");
	
	for(i=0;i<x;i++)
	{
	System.out.println("\nBlock: "+(i+1));
	for(j=0;j<64;j++)
	{
	if(j%8==0)
	System.out.print(" ");
	System.out.print(ct[i][j]);
	}
	}
*/
// convert into text
for(i=0;i<x;i++)
{
byte[] ext = desECB.btary(ct[i]);
String s = new String(ext,StandardCharsets.ISO_8859_1);
sb.append(s);
}

System.out.println("\nPlaintext: "+text);
System.out.println("\nKey: "+key);
System.out.print("\nCiphertext: ");
System.out.println(sb.substring(0,txtlen));      // displaying only upto the length of plaintext 


// DECRYPTION
desDecrypt dobj = new desDecrypt();
//int y= msgInt.length;
//int[][] ct = new int[x][64];
StringBuilder sbd=new StringBuilder();
String ss = new String(sb);
//	System.out.println("\nOriginal sb: "+sb);
//	System.out.println("\nDecr str: "+ss);

byte[] ctbytes = ss.getBytes(StandardCharsets.ISO_8859_1);
/*	System.out.println("\nDecr str as bytes: \n");
	for(byte b: ctbytes)
	{
	String s1 = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
	System.out.print(s1+"\t");
	}
	System.out.println();
*/
int y = (ctbytes.length*8) / 64;
int ctbytesInt[][] = new int[y][64];
int pct[][] = new int[y][64];

k=0;
for(i=0;i<y;i++)
{
for(j=0;j<64;j++)
{
ctbytesInt[i][j] = desECB.getBit(ctbytes,k++); //System.out.print(desECB.getBit(ctbytes,k-1));
}
}
/*	System.out.println("\nDecr str as int: \n");
	for(i=0;i<y;i++)
	{
	for(j=0;j<64;j++)
	{
	if(j%8==0)
	System.out.print(" ");
	System.out.print(ctbytesInt[i][j]);
	}
	}
	System.out.println();
*/
// decrypt
for(i=0;i<y;i++)
{
pct[i] = dobj.decrypt(ctbytesInt[i],roundKey);
}

/*	System.out.println("\nBlockwise output after decryption of ciphertext: \n");
	for(i=0;i<y;i++)
	{
	for(j=0;j<64;j++)
	{
	if(j%8==0)
	System.out.print(" ");
	System.out.print(pct[i][j]);
	}
	}

*/

// calculate plaintext msg length
byte[] ptMsgLen = desECB.btary(pct[0]);	
ByteBuffer wrapped = ByteBuffer.wrap(ptMsgLen); 
int gotMsgLen = wrapped.getInt();

// convert into text
for(i=1;i<x;i++)
{
byte[] extd = desECB.btary(pct[i]);
String st = new String(extd);
sbd.append(st);
}

String dpt = sbd.substring(0,gotMsgLen);

System.out.println("\nDecrypted Plain text: "+dpt);

}	// main ends
}	// class ends
 
