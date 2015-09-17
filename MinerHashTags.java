package test;


import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.JFrame;

class Words{
	int count;
	String word;

	/**
	 * @param count
	 * @param word
	 */
	public Words(int count, String word) {
		this.count = count;
		this.word = word;
	}

	/**
	 * 
	 */
	public Words() {
		count=0;
		word=null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Words [count=" + count + ", word=" + word + "]";
	}

	public static void reversedSort(ArrayList<Words> words)
	{
		words.sort(Comparator.comparing((Words x)->x.count).reversed());

	}

}
class Counts{
	int count;
	String hashTag;
	static int noOfCounts=0;
	ArrayList<Words> words; 

	int wordCount;
	public Counts() {
		// TODO Auto-generated constructor stub
		count=0;
		hashTag=null;
		words=null;
	}
	/**
	 * @param count
	 * @param hashTag
	 */
	public Counts(int count, String hashTag) {
		this.count = count;
		this.hashTag = hashTag;
		words=new ArrayList<Words>();

	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		Words.reversedSort(words);
		return "Counts [count=" + count + ", hashTag=" + hashTag + ", words="
		+ words.subList(0, 10) + "]";
	}
	public void updateWords(String string) {
		// TODO Auto-generated method stub
		String[] set=string.split(" ");
		int i=0;
		String word;
		for(i=0;i<set.length;i++)
		{

			word=set[i];

			boolean flag1=false;
			for(Words word1:words)
				if(word.equalsIgnoreCase(word1.word))
				{
					word1.count++;

					flag1=true;
					break;
				}
			if(wordCount<1000&&flag1==false)
			{
				Words temp=new Words(1, word);
				words.add( temp);
				wordCount++;
			}
			else if(flag1==false)
			{
				Words.reversedSort(words);
				//		System.out.println(words);
				words.get(words.size()-1).word=word;
				words.get(words.size()-1).count++;
			}

		}
	}
	public void renewWords(String string) {
		// TODO Auto-generated method stub
		words.removeIf(word->word.count>=0);
		wordCount=0;
		updateWords(string);
	}

	public static void reversedSort(ArrayList<Counts> counts)
	{
		counts.sort(Comparator.comparing((Counts x)->x.count).reversed());

	}
	public void updateCorrectWords(String string) {
		// TODO Auto-generated method stub
		String[] set=string.split(" ");
		int i=0;
		String word;
		for(i=0;i<set.length;i++)
		{

			word=set[i];

			boolean flag1=false;
			for(Words word1:words)
				if(word.equalsIgnoreCase(word1.word))
				{
					word1.count++;

					flag1=true;
					break;
				}
			if(flag1==false)
			{
				Words temp=new Words(1, word);
				words.add( temp);

			}

		}
	}
}
public class MinerHashTags {


	public static void main(String... args) {
		ArrayList<Counts> spaceSavingCount = new ArrayList<Counts>();
		ArrayList<Counts> correctCount=new ArrayList<Counts>();
		try {
			BufferedReader bufferedReader1= new BufferedReader(new FileReader("C:/Academic/tweets/tweets_2013-12-24.csv"));
			BufferedReader bufferedReader2= new BufferedReader(new FileReader("C:/Academic/tweets/tweets_2013-12-25.csv"));
			BufferedReader bufferedReader3= new BufferedReader(new FileReader("C:/Academic/tweets/tweets_2013-12-26.csv"));

			/*while((ch=bufferedReader.read())!=-1)
		{
			if((char)ch=='\n')
			System.out.print((char)ch);}*/
			String line,str;
			String[] fields;
			int i=0;
			int flag=0;
			boolean flag2=false;
			long tweetCount=0;
			while(((line=bufferedReader1.readLine())!=null)||((line=bufferedReader2.readLine())!=null)||((line=bufferedReader3.readLine())!=null))
			{
				if(tweetCount==10000001) break;
				//System.out.println("HI"+tweetCount++);
				fields=null;
				fields= line.split(",");
				flag=0;

				while(fields.length!=26+flag)
				{
					if(fields.length<26+flag)
					{
						if(((str=bufferedReader1.readLine())!=null)||((str=bufferedReader2.readLine())!=null)||((str=bufferedReader3.readLine())!=null))
						{
							line=line.concat(str);
							fields= line.split(",");
							flag=0;
						}	
					}
					if(fields.length>26+flag)
					{

						for(i=0;i<fields.length&&!flag2;i++)
						{

							while(noOfTimes(fields[i],'\"')%2==1)
							{	

								int j;
								for(j=i+1;j<fields.length&&fields[j].equals("NOTAVAILABLE");j++)
									if(j+1==fields.length) {
										flag2=true;break;}
								if(flag2||j==fields.length) break;
								fields[i]=fields[i].concat(","+fields[j]);
								fields[j]="NOTAVAILABLE";
								flag++;
							}
						}
						flag2=false;
					}
				}
				if(fields[4].contains("#"))
				{
					String hashTag;
					String[] hashStrings=fields[4].split("#");
					for(i=1;i<hashStrings.length;i++)
					{
						if(hashStrings[i].split(" ").length==0)
						{
							hashTag=null;
							continue;
						}
						hashTag="#"+hashStrings[i].split(" ")[0];

						boolean flag1=false;
						boolean flagc1=false;
						for(Counts count:spaceSavingCount)
							if(hashTag.equalsIgnoreCase(count.hashTag))
							{
								count.count++;
								count.updateWords(fields[4]);
								flag1=true;
								break;
							}
						for(Counts count : correctCount)
							if(hashTag.equalsIgnoreCase(count.hashTag))
							{
								count.count++;
								count.updateCorrectWords(fields[4]);
								flagc1=true;
								break;
							}

						if(Counts.noOfCounts<1000&&flag1==false)
						{
							Counts temp1=new Counts(1,hashTag);
							temp1.updateWords(fields[4]);
							spaceSavingCount.add(temp1);

							Counts.noOfCounts++;
						}
						else if(flag1==false)
						{
							Counts.reversedSort(spaceSavingCount);
							spaceSavingCount.get(spaceSavingCount.size()-1).hashTag=hashTag;
							spaceSavingCount.get(spaceSavingCount.size()-1).count++;
							spaceSavingCount.get(spaceSavingCount.size()-1).renewWords(fields[4]);

						}
						if(!flagc1)
						{
							Counts temp2=new Counts(1,hashTag);
							temp2.updateCorrectWords(fields[4]);
							correctCount.add(temp2);
						}

					}

				} 
			}
			Counts.reversedSort(spaceSavingCount);
			Counts.reversedSort(correctCount);

			for(Counts count:spaceSavingCount.subList(0, 50))
			{
				System.out.println(count);
			}
			System.out.println("\n");
			for(Counts count:correctCount.subList(0, 50))
			{
				System.out.println(count);
			}

			bufferedReader1.close();
			bufferedReader2.close();
			bufferedReader3.close();
		}

		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		WindowListener wndCloser = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				//System.exit(0);
			}
		};
		JFrame[] f=new JFrame[11];
		int[] counts=new int[10];
		String[] hastags=new String[10];
		String[][] words=new String[10][10];
		int[][] wordCount=new int[10][10];
		f[0]=new JFrame();
		f[0].setSize(400, 300);
		for(int i=0;i<10;i++)
		{
			counts[i]=correctCount.get(i).count;
			hastags[i]=correctCount.get(i).hashTag;
			for(int j=0;j<10;j++)
			{
				words[i][j]=correctCount.get(i).words.get(j).word;
				wordCount[i][j]=correctCount.get(i).words.get(j).count;
			}
		}
		f[0].add(new BarChart(counts,"Correct Top 10 Hashtags",hastags));
		f[0].addWindowListener(wndCloser);
		f[0].setVisible(true);

		for(int z=1;z<11;z++)
		{
			f[z]=new JFrame();
			f[z].setSize(400, 300);

			f[z].add(new BarChart(wordCount[z-1],"Correct Top 10 Words in "+hastags[z-1],words[z-1]));
			f[z].addWindowListener(wndCloser);
			f[z].setVisible(true);
		}


	}

	private static int noOfTimes(String string,char ch) {
		// TODO Auto-generated method stub
		int count=0;
		for(char c :string.toCharArray())
		{
			if(c==ch) count++;
		}
		return count;
	}
}