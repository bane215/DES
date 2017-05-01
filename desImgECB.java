// get bytes from an image, encrypt using des, render changes into a new image in png form, verify change

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.awt.image.DataBufferByte;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.nio.charset.StandardCharsets;


public class desImgECB extends Component {

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
  
    public static void main(String[] args) throws Exception{

        Scanner sc = new Scanner(System.in); 
        int i,j,k;
	
       BufferedImage img = null;	
       BufferedImage destImg = null;
	
	//String imgPath="F:\\iiitmk\\Sem 2\\Crypto\\Lab\\des\\";
	String imgPath="/home/bane/des/";	

       try {
           //Read in new image file
           img = ImageIO.read(new File(imgPath+"color.jpg"));
       } 
       catch (IOException e){
       }

       if (img == null) {
             System.out.println("No image loaded");
	System.exit(0);
        } 

// create destination image in user space
destImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
Graphics2D graphics = destImg.createGraphics();
graphics.drawRenderedImage(img, null);
graphics.dispose();

// convert dest image to byte arrays
WritableRaster raster   = destImg.getRaster();
DataBufferByte buffer = (DataBufferByte)raster.getDataBuffer();
byte dstimg[]  = buffer.getData();

System.out.println("\nNo. of bits in  src image: "+(dstimg.length*8));  

System.out.println("\nEnter the key: ");
String key = sc.nextLine();
//	System.out.println("\nEntered key: "+key);
int keylen = key.length();
//	System.out.println("\nEntered key length: "+keylen);

// convert key to binary

byte[] keybytes = key.getBytes(StandardCharsets.ISO_8859_1);

// convert message (byte[]) into int[] and pad with 0's if necessary
int mod = (dstimg.length*8) % 64; 
int blk = (dstimg.length*8) / 64; 
int blocks;
int bitNum;
int msgInt[][];			//   plaintext

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
msgInt[i][j] = desImgECB.getBit(dstimg,k++);
}
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


else if((mod<64) && (blk==0))
{
blocks = 1;
bitNum = mod;
msgInt = new int[1][64];

for(i=0;i<mod;i++)
msgInt[0][i] = desImgECB.getBit(dstimg,i);

for(i=mod;i<64;i++)
msgInt[0][i] = 0;

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
msgInt[i][j] = desImgECB.getBit(dstimg,k++);
}
}

for(i=0;i<mod;i++)
msgInt[blocks-1][i] = desImgECB.getBit(dstimg,k++);

for(i=mod;i<64;i++)
msgInt[blocks-1][i] = 0;

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
roundKey[i] = desImgECB.getBit(keybytes,i);

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
roundKey[i] = desImgECB.getBit(keybytes,i);

/*	System.out.println("\nKey converted into int and truncated:\n");
	for(i=0;i<roundKey.length;i++)
	{
	if(i%8==0)
	System.out.print(" ");
	System.out.print(roundKey[i]);
	}
*/
}


/*
// print byte as binary string
for(int i=0;i<dstimg.length;i++)
{
String s1 = String.format("%8s", Integer.toBinaryString(dstimg[i] & 0xFF)).replace(' ', '0');
System.out.print(s1+"\t");
}
*/



// ENCRYPTION
desEncrypt obj = new desEncrypt();
int x= msgInt.length;
//	System.out.println("\nNumber of input blocks for encryption: "+x);
int[][] ct = new int[x][64];
//StringBuilder sb=new StringBuilder();

// encrypt
for(i=0;i<x;i++)
{
ct[i] = obj.encrypt(msgInt[i],roundKey);
}

// convert 2d array into 1d
int oned[] = new int[x*64];
k=0;
for(i=0;i<x;i++)
{
for(j=0;j<64;j++)
{
oned[k++] = ct[i][j];
}
}


// converting into byte[]
byte[] ext = desImgECB.btary(oned);

// substituting extracted raster values with encrypted values

for(i=0;i<dstimg.length;i++)
{
dstimg[i] = ext[i];
}

// save changes into dest image (encrypted) rendered from original image
File outputfile = new File(imgPath+"color_encr.png");
ImageIO.write(destImg,"png",outputfile);
System.out.println("\nEncryption successful!");

	







// DECRYPTION

try {
           //Read in encrypted image file
           img = ImageIO.read(new File(imgPath+"color_encr.png"));
       } 
       catch (IOException e){
       }

       if (img == null) {
             System.out.println("No image loaded");
	System.exit(0);
        } 

// create another destination image in user space
destImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
Graphics2D graphicsE = destImg.createGraphics();
graphicsE.drawRenderedImage(img, null);
graphicsE.dispose();

// convert encrypted image to byte arrays
WritableRaster rasterEncr   = destImg.getRaster();
DataBufferByte bufferEncr = (DataBufferByte)rasterEncr.getDataBuffer();
byte dstimgEncr[]  = bufferEncr.getData();

System.out.println("\nNo. of bits in  encrypted image: "+(dstimgEncr.length*8));  

// convert message (byte[]) into int[] and pad with 0's if necessary
int modE = (dstimgEncr.length*8) % 64; 
int blkE = (dstimgEncr.length*8) / 64; 
int blocksE;
int bitNumE;
int msgIntE[][];			//   plaintext

if(modE==0)
{
blocksE=blkE;
bitNumE = blocksE * 64;
msgIntE = new int[blocksE][64];

k=0;
for(i=0;i<blocksE;i++)
{
for(j=0;j<64;j++)
{
msgIntE[i][j] = desImgECB.getBit(dstimgEncr,k++);
}
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


else if((mod<64) && (blk==0))
{
blocksE = 1;
bitNumE = modE;
msgIntE = new int[1][64];

for(i=0;i<modE;i++)
msgIntE[0][i] = desImgECB.getBit(dstimgEncr,i);

for(i=modE;i<64;i++)
msgIntE[0][i] = 0;

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


else if(((modE>0) && (modE<64)) && (blkE>0))
{
blocksE = blkE+1;
int padE = 64 - modE;
bitNumE = (blocksE-1)*64 + modE;
msgIntE = new int[blocksE][64];

k=0;
for(i=0;i<(blocksE-1);i++)
{
for(j=0;j<64;j++)
{
msgIntE[i][j] = desImgECB.getBit(dstimgEncr,k++);
}
}

for(i=0;i<modE;i++)
msgIntE[blocksE-1][i] = desImgECB.getBit(dstimgEncr,k++);

for(i=modE;i<64;i++)
msgIntE[blocksE-1][i] = 0;

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
msgIntE = new int[1][1];
msgIntE[0][0] = 0;

//	System.out.println("\nThis should never print: \n");
	
}

desDecrypt dobj = new desDecrypt();
int y = msgIntE.length;
int pct[][] = new int[y][64];


// decrypt
for(i=0;i<y;i++)
{
pct[i] = dobj.decrypt(msgIntE[i],roundKey);
}


// convert 2d array into 1d
int onedE[] = new int[y*64];
k=0;
for(i=0;i<y;i++)
{
for(j=0;j<64;j++)
{
onedE[k++] = pct[i][j];
}
}


// converting into byte[]
byte[] extE = desImgECB.btary(onedE);

// substituting extracted raster values with encrypted values

for(i=0;i<dstimgEncr.length;i++)
{
dstimgEncr[i] = extE[i];
}

// save changes into dest image (encrypted) rendered from original image
File outputfileE = new File(imgPath+"color_decr.png");
ImageIO.write(destImg,"png",outputfileE);

System.out.println("\nDecryption successful!");
     }
}
