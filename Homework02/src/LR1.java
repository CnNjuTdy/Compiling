import java.util.Vector;
public class LR1 {
	private char 	[]VN		=new char[50];		//非终结符集
	private char 	[]VT		=new char[50];		//终结符集
	private String []F			=new String[50];	//产生式集
	private StringBuffer []FirstVN;				    //非终结符的First集
	private int	[]staStack	=new int[50];		    //状态分析栈
	private char	[]symStack	=new char[50];		//符号分析栈
	private boolean[]VNE;							//非终结符与空串的关系表
	private int 	F_index		=0;					//产生式数组指针
	private int	staStack_index=0;				    //状态栈指针
	private int 	symStack_index=0;				//符号栈指针
	private int 	ERROR		=Integer.MAX_VALUE;	//出错标志
	private char  	emp			='ε';				//空串
	private String error		="x";				//分析表显示的出错标志
	private String acc			="acc";				//分析表显示的分析成功标志
	private Vector<Vector<String>> 	State		=new Vector<Vector<String>>();  //项集  //
	private int [][]Action;						    //Action动作数组
	private int	[][]Goto;						    //Goto动作数组
	private StringBuffer  []bridge1;				//描述项集之间的转换关系，在createLR1()中初始化
	private int	[][]bridge2;

	private LR1() {
		super();
		//输入文法
		F[0]= "LE";
		F[1]= "EE+T";
		F[2]= "ET";
		F[3]= "TT*F";
		F[4]= "TF";
		F[5]= "F(E)";
		F[6]= "Fi";
		for(int i=0;i<7;i++)
			init(F[i]);

		//输入待分析字符
		String s="i(i+i)";
		createAll();//构造各种东西
		displayLR1States();//展示LR1的项集
		displayLR1AnalysisTable();//展示分析表
		analysisInPutString(s);//分析输入串

	}
	//生成所有与文法有关的数据，比如项集，分析表等
	private void createAll(){
		renew();		//如果不是第一次使用，一些数组被改动了，所以要恢复
		addf0();		//添加第0个产生式
		displayInformation(); //先输出输入的信息
		createFirstVN();	//构造各个非终结符的first集合
		createLR1();	//构造LR项集
		createAction();//构造action表
		createGoto();//构造goto表
	}
	//从输入的产生式中提取各种信息元素，包括非终结符和终结符
	private void init(String s){
		for(int i=0;i<s.length();i++){
			char c=s.charAt(i);
			if(c>='A'&&c<='Z'){						//若是大写字母，认为是非终结符
				if(!isInVN(c))						//如果不在里面，就加进去
					VN=String.valueOf(VN).trim().concat(String.valueOf(c)).toCharArray();
			}
			else{									//除大写字母外都认为是终结符
				if(!isInVT(c))						//终结符在不在VT中？
					VT=String.valueOf(VT).trim().concat(String.valueOf(c)).toCharArray();
			}
		}
		F[F_index++]=new String(s);
	}
	//判断C是否在数组VN里面
	private boolean isInVN(char C){
		String s=String.valueOf(VN);
		if(s.contains(String.valueOf(C)))			//C是不是VN数组里的一个元素？
			return true;
		else
			return false;
	}
	//判断c是否在数组VT中
	private boolean isInVT(char c){
		String s=String.valueOf(VT);
		if(s.contains(String.valueOf(c)))			//c是不是VT数组里的一个元素
			return true;
		else
			return false;
	}
	//判断'.'是否在s里面
	private boolean pointIsTheLastOne(String s){
		if(s.indexOf(',')-s.indexOf('.')==1)
			return true;
		else
			return false;
	}
	//显示3个TextArea的信息
	private void displayInformation(){
		StringBuffer temp=new StringBuffer("产生式：  \n");
		for(int i=0;i<F.length;i++){
			if(F[i]!=null){							//如果不是空串
				if(F[i].charAt(0)=='@')
					temp.append("(0)S'"+"==>"+F[0].substring(1)+"\n");//把"@"用S'显示
				else if(F[i].length()>1)
					temp.append("("+i+")"+F[i].charAt(0)+"==>"+F[i].substring(1)+"\n");
				else
					temp.append("("+i+")"+F[i].charAt(0)+"==>"+emp+"\n");
			}
			else
				break;								//如果是的话就跳出循环
		}
		System.out.println(temp);
	}
	//删除一个产生式，此函数将修改VN、VT、F三个数组
	public void delete_a_f(int index){
		String temp=new String(F[index]);
		for(int i=index;F[i]!=null;i++){
			if(F[i+1]==null){
				F[i]=null;
				break;
			}
			else
				F[i]=F[i+1];
		}
		F_index--;
		StringBuffer sb=new StringBuffer();
		for(int j=0;F[j]!=null;j++){
			sb.append(F[j]);
		}
		for(int k=0;k<temp.length();k++){
			char c=temp.charAt(k);
			if(!sb.toString().contains(String.valueOf(c))){
				if(c>='A'&&c<='Z')
					delete_a_vn(c);
				else
					delete_a_vt(c);
			}
		}
	}
	//若字符c在VT中，就把它删除
	private void delete_a_vt(char c){
		for(int i=0;i<VT.length;i++){
			if(c==VT[i]){
				for(int j=i;j<VT.length-1;j++)
					VT[j]=VT[j+1];
				VT=String.valueOf(VT).subSequence(0, VT.length-1).toString().toCharArray();
				break;
			}
		}
	}

	private void addf0(){
		if(F[0].charAt(0)=='@')
			return ;
		for(int i=F_index;i>0;i--)
			F[i]=F[i-1];
		F[0]=("@"+String.valueOf(VN[0]));
		F_index++;
	}
	//若字符c在VN中，就把它删除
	private void delete_a_vn(char c){
		for(int i=0;i<VN.length;i++){
			if(c==VN[i]){
				for(int j=i;j<VN.length-1;j++)
					VN[j]=VN[j+1];
				VN=String.valueOf(VN).subSequence(0, VN.length-1).toString().toCharArray();
				break;
			}
		}
	}
	//求一个字符串的First集，此函数再addPoint函数中调用
	private String createFirst(String s){							//设s=X1X2X3X4...
		StringBuffer bf=new StringBuffer();
		for(int i=0;i<s.length();i++){
			if(isInVT(s.charAt(i))){									//如果最左端是终结符的话
				bf=bf.append(s.charAt(i));
				break;
			}
			//如果Xi(Xi不是最后一个)能推出空串，则First(Xi)-emp属于First(s)
			else if(VNE[String.valueOf(VN).indexOf(s.charAt(i))]&&i!=s.length()-1){
				String temps=FirstVN[String.valueOf(VN).indexOf(s.charAt(i))].toString();
				if(temps.contains(String.valueOf(emp))){				//如果FirstVN[Xi]含有emp，删除之
					FirstVN[String.valueOf(VN).indexOf(s.charAt(i))].deleteCharAt(emp);
					bf=bf.append(FirstVN[String.valueOf(VN).indexOf(s.charAt(i))]);
				}else
					bf=bf.append(FirstVN[String.valueOf(VN).indexOf(s.charAt(i))]);
			}else{//First(Xi)属于First(s)
				bf=bf.append(FirstVN[String.valueOf(VN).indexOf(s.charAt(i))]);
				break;
			}
		}
		return bf.toString();
	}
	//求全体非终结符的First集合，放在FirstVN[]中
	private void createFirstVN(){
		createVN_emptyTable();											//生成非终结符能否产生空串表
		FirstVN=new StringBuffer[VN.length];
		for(int ii=0;ii<FirstVN.length;ii++)
			FirstVN[ii]=new StringBuffer();								//初始化FirstVN
		for(int i=0;i<FirstVN.length;i++)
			if(FirstVN[i].toString().length()==0)							//如果还没求过的话
				myFirstVN(VN[i]);
	}
	//求得一个非终结符的First集，createFirstVN()中调用
	private void myFirstVN(char c){
		String svn=String.valueOf(VN);									//中间变量
		for(int i=1;i<F_index;i++){
			String s=F[i];
			if(c==s.charAt(0)){
				if(s.length()==1){										//空产生式
					FirstVN[svn.indexOf(s.charAt(0))].append(emp);
				}else{
					for(int j=1;j<s.length();j++){						//S=>AB..
						if(isInVT(s.charAt(j))){
							FirstVN[svn.indexOf(s.charAt(0))].append(s.charAt(j));
							break;
						}else if(c!=s.charAt(j)){						//阻止A=>..A..这样的情况，防止死循环
							if(VNE[svn.indexOf(s.charAt(j))]){	//如果A能推出空串，则First(A)-emp属于First(S)
								if(FirstVN[svn.indexOf(s.charAt(j))].toString().length()==0)//如果First(A)还没求过的话
									myFirstVN(s.charAt(j));		//先求出A的first
								if(j!=s.length()-1){			//如果s.charAt(j)不是最后一个非终结符
									//如果First(A)中含有emp应该把空串去掉
									for(int k=0;k<FirstVN[svn.indexOf(s.charAt(j))].length();k++){
										if(FirstVN[svn.indexOf(s.charAt(j))].charAt(k)==emp)
											FirstVN[svn.indexOf(s.charAt(j))]=
													FirstVN[svn.indexOf(s.charAt(j))].deleteCharAt(k);
									}
								}
								FirstVN[svn.indexOf(s.charAt(0))]
										.append(FirstVN[svn.indexOf(s.charAt(j))]);
							}
							else{//如果A不能推出空串，则First(A)属于First(S)
								if(FirstVN[svn.indexOf(s.charAt(j))].toString().length()==0)//如果First(A)还没求过的话
									myFirstVN(s.charAt(j));		//先求出A的first
								FirstVN[String.valueOf(VN).indexOf(s.charAt(0))]
										.append(FirstVN[svn.indexOf(s.charAt(j))]);
								break;							//这个产生式分析完毕
							}
						}else
							break;
					}
				}
			}
		}
	}
	//生成非终结符和空传之间的关系表，即哪些VN能*推导出空串，哪些不能
	private void createVN_emptyTable(){
		VNE	=new boolean[VN.length];									//非终结符与空串的关系表
		String	[]tempF	=(String [])F.clone();							//取得产生式的副本
		for(int i=0;i<VNE.length;i++)
			VNE[i]=false;												//初始化非终结符都不能推出空串
		for(int j=0;j<F_index;j++){										//删除含有终结符的产生式
			String s=tempF[j];
			for(int k=0;k<s.length();k++){
				if(isInVT(s.charAt(k))){								//看看这个产生式中是否含有终结符
					tempF[j]=null;
					break;												//如果含有的话，删除此产生式
				}
			}
		}
		for(int m=0;m<F_index;m++){										//看看哪些产生式可以直接推出空串
			if(tempF[m]!=null&&tempF[m].length()==1){					//空产生式
				VNE[String.valueOf(VN).indexOf(tempF[m].charAt(0))]=true;
				tempF[m]=null;
			}
		}
		for(int n=1;n<F_index;n++){										//不包含添加的S',所以n从1开始
			//处理剩下的形如S==>ABCD..的产生式
			char c;
			if(tempF[n]!=null){
				for(int p=1;p<tempF[n].length();p++){
					c=tempF[n].charAt(p);
					if(!VNE[String.valueOf(VN).indexOf(c)])				//如果VNE[c]！=true，即c推不出空串
						break;
					if(p==tempF[n].length()-1)							//
						VNE[String.valueOf(VN).indexOf(tempF[n].charAt(0))]=true;		//如果ABCD..都能推出空串，则S也能
				}
			}
		}
	}

	private void createLR1(){											//生成LR1项集
		getI0();														//求出第一个项集I0
		for(int j=0;j<State.size();j++){
			Vector vtemp	=(Vector)State.get(j);						//取得上一个状态集合
			String s1		=getAfterPoint(vtemp);						//取得上一个状态集合中的'.'后的所有符号
			for(int m=0;m<s1.length();m++){
				Vector v	=new Vector();								//中间变量
				char c1=s1.charAt(m);									//从s1中取一个字符
				for(int k=0;k<vtemp.size();k++){
					String s2	=vtemp.get(k).toString();
					if(!pointIsTheLastOne(s1)&&
							s2.charAt(s2.indexOf('.')+1)==c1){				//此项不是规约项，可以尝试加入
						String s3=movePoint(s2);						//中间变量
						if(!isInState(s3)&&!isInCurrentState(s3,v)){
							v.addElement(s3);
							for(int g=0;g<v.size();g++){
								s3=v.get(g).toString();
								if(!pointIsTheLastOne(s3)){					//此部分计算与求State0中的新增项集相同
									char c2=s3.charAt(s3.indexOf('.')+1);
									if(isInVN(c2)){							//'.'后面的符号是非终结符吗？
										for(int n=0;n<F_index;n++){
											if(c2==F[n].charAt(0)){
												String s4=addPoint(F[n],s3);
												if(!isInState(s3)&&!isInCurrentState(s4,v))
													v.addElement(s4);
											}
										}
									}
								}
							}
						}
					}
				}
				if(v.size()>0)					//只有v里面加入了项才把整个项集加入到项集数组
					State.add(arrange(v));
			}
		}
		//初始化两个关系桥
		bridge1=new StringBuffer[State.size()];
		for(int s=0;s<State.size();s++)
			bridge1[s]=new StringBuffer();
		bridge2=new int[State.size()][50];
	}
	//获得I0
	private void getI0(){
		VT=String.valueOf(VT).concat(String.valueOf('#')).toCharArray();//添加句子括号'#'
		Vector v0=new Vector();
		v0.addElement(addPoint(F[0]));									//初始化第一个State0项，包括加上",#"
		for(int h=0;h<v0.size();h++){
			String s0=v0.get(h).toString();
			if(!pointIsTheLastOne(s0)){									//判断'.'是不是最后一个符号
				char c0=s0.charAt(s0.indexOf('.')+1);					//取得'.'后面的符号
				if(isInVN(c0)){											//是否在VN[]中
					for(int i=0;i<F_index;i++)
						if(c0==F[i].charAt(0)){
							if(!isInCurrentState(addPoint(F[i],s0),v0))	//防止左递归引起的死循环
								v0.addElement(addPoint(F[i],s0));		//把新增项加到第一个State0中
						}
				}
			}
		}
		//以下arrange(v0)的作用是整理一下得到的项集，主要是合并一下形如A=>string,α
		//和A=>string,β项集，即吧它们合并成A=>string,αβ的形式
		State.add(arrange(v0));											//加入到State中
		//State.add(v0);
	}
	//整理新得到的一个状态，合并其中可以合并的项
	private Vector arrange(Vector v){
		Vector v0=new Vector();
		boolean b[]=new boolean[v.size()];	//用来标记对应的项是否已经被处理过,初始化为都未处理
		for(int m=0;m<b.length;m++)
			b[m]=false;
		String si=new String();
		String sj=new String();
		StringBuffer bf;
		for(int i=0;i<v.size();i++){
			if(b[i]==true)						//如果此项已归并完毕则处理下一项
				continue;
			si=v.get(i).toString();				//获得第i个项
			bf=new StringBuffer(si);
			for(int j=i+1;j<v.size();j++){
				sj=v.get(j).toString();
				//如果si项的逗号之前的部分与sj项的逗号之前的部分相同，则合并它们，
				//即把第二个项逗号之后部分加到第一个项逗号之后
				if(si.substring(0, si.indexOf(String.valueOf(','))).equals
						(sj.substring(0, sj.indexOf(String.valueOf(','))))){
					bf=bf.append(sj.substring(sj.indexOf(String.valueOf(','))+1, sj.length()));
					b[j]=true;					//标记为已处理
				}
			}
			v0.addElement(bf.toString());
		}
		return v0;
	}
	//判断s在当前状态集中是否已经存在
	private boolean isInCurrentState(String s, Vector v){
		for(int i=0;i<v.size();i++){
			if(s.equals(v.get(i).toString()))
				return true;
		}
		return false;
	}
	//生成分析表里的Action部分
	private void createAction(){
		Action=new int[State.size()][VT.length];
		for(int i=0;i<State.size();i++){
			for(int j=0;j<VT.length;j++){
				Action[i][j]=getAction(i,j);
			}
		}
	}
	//生成分析表里的Goto部分
	private void createGoto(){
		Goto=new int[State.size()][VN.length];
		for(int i=0;i<State.size();i++){
			for(int j=0;j<VN.length;j++){
				Goto[i][j]=getGoto(i,j);
			}
		}
	}
	//对输入的一字符串进行分析
	private void analysisInPutString(String s){
		s=s.concat(String.valueOf('#'));					//添加句子括号'#'，以便分析
		int s_index=0;										//次索引指向被分析符号串的头
		int i=0;
		int j=0;
		int k=0;											//中间变量
		int step=0;
		staStack[0]=0;
		symStack[0]='#';									//初始化状态栈与符号栈
		while(true){
			step++;											//步骤数加1
			i=staStack[staStack_index];						//栈顶状态
			j=toColumn(s.charAt(s_index));					//被分析符号头字符在vt数组中的位置
			if(isInVT(s.charAt(s_index))&&j!=ERROR){		//如果是非终结符
				if(Action[i][j]>0){							//如果是移进
					k=Action[i][j];
					if(k==ERROR){
						System.out.println( "分析失败！\n"+"出错原因：在第"+step+
								"步分析到第"+i+"个状态时，"+"当前面临的符号是"+s.charAt(s_index)+
								"，\n而在分析表的Action表中("+i+","+s.charAt(s_index)+")这个位置"+
								"是出错标志！");
						break;
					}
					staStack[++staStack_index]=k;
					symStack[++symStack_index]=s.charAt(s_index++);
					System.out.print("移入\t");
				}
				else if(Action[i][j]<0){					//如果是归约
					k=-Action[i][j];						//按照第k个产生式归约
					staStack_index	-=F[k].length()-1;		//栈指针减去第k个产生式的右部的长度
					symStack_index	-=F[k].length()-1;
					symStack[symStack_index++]=F[k].charAt(0);//把句柄归约
					int temp=Goto[staStack[staStack_index]][toColumn(F[k].charAt(0))];//取得Goto[栈顶状态][刚规约完的VN]
					if(temp==ERROR){
						System.out.println( "分析失败！\n"+"出错原因：在第"+step+
								"步分析到第"+staStack[staStack_index]+"个状态时，"+"当前面临的符号是"+
								F[k].charAt(0)+"，\n而在分析表的Goto表中("+staStack[staStack_index]+","+
								F[k].charAt(0)+")这个位置是出错标志！");
						break;
					}
					staStack[++staStack_index]=temp;
					System.out.print("归约\t");
				}else{
					System.out.println( "分析成功！此符号串是这个文法的句子！");
					break;
				}
				//输出状态
				System.out.print("状态表：");
				for (int l = 0; l < staStack_index; l++) {
					System.out.print(staStack[l]);
				}
				System.out.print("\t\t符号表：");
				for (int l = 0; l < symStack_index; l++) {
					System.out.print(symStack[l]);
				}
				System.out.println();
			}else{
				System.out.println( "分析失败！\n"+"出错原因：在第"+step+
						"步当前面临符号"+s.charAt(s_index)+"不属于该文法的终结符！");
				break;
			}
		}
	}
	//恢复
	private void renew(){
		delete_a_vt('#');								//删掉添加的开始符号'#'
		staStack		=new int[50];
		symStack		=new char[50];
		State			=new Vector();
		staStack_index	=0;
		symStack_index	=0;
	}
	//在产生式S的右部的最前端加上一个'.'
	private String addPoint(String s){
		StringBuffer bf=new StringBuffer(String.valueOf(s.charAt(0)));
		bf.append('.');
		bf.append(s.substring(1));
		if(s.equals(F[0]))								//如果是附加产生式，直接加上",#"
			bf.append(",#");
		return bf.toString();
	}
	//s1是s2的附加项集s2:A->α.Bβ,a	s1:B->.γ,b		其中b属于First(βa)
	private String addPoint(String s1,String s2){
		StringBuffer bf=new StringBuffer(String.valueOf(s1.charAt(0)));
		StringBuffer bf1=new StringBuffer();			//取得βa
		bf.append('.');
		bf.append(s1.substring(1));
		bf.append(',');
		bf1.append(s2.substring(s2.indexOf('.')+2));	//取得βa
		if(bf1.indexOf(String.valueOf(','))==0
				||createFirst(bf1.substring(0, bf1.indexOf(String.valueOf(',')))).contains(String.valueOf(emp))){
			//如果β是空串或者可以推出空串的话，那么逗号后面的都属于b
			bf.append(bf1.substring(bf1.indexOf(String.valueOf(','))+1));
		}else{											//否则的话
			bf1.deleteCharAt(bf1.indexOf(String.valueOf(',')));
			bf.append(createFirst(bf1.toString()));
		}
		return bf.toString();
	}
	//把‘.’向后移一个位置
	private String movePoint(String s){
		char []c=s.toCharArray();
		char temp=s.charAt(s.indexOf('.')+1);			//取得'.'后面的字符，放入中间变量，以便和'.'交换位置
		c[s.indexOf('.')]=temp;
		c[s.indexOf('.')+1]='.';
		return String.valueOf(c);
	}
	//取得一个状态集的','之后的符号集合，以便求出此状态的下一个状态
	private String getAfterPoint(Vector v){
		StringBuffer bf=new StringBuffer();
		for(int i=0;i<v.size();i++){
			String s=v.get(i).toString();
			if(!pointIsTheLastOne(s)){			//'.'不是最后一个字符
				char c=s.charAt(s.indexOf('.')+1);
				if(!bf.toString().contains(String.valueOf(c)))
					bf.append(c);
			}
		}
		return bf.toString();
	}
	//检查一个项是否已存在
	private boolean isInState(String s){
		Vector v=new Vector();
		for(int i=0;i<State.size();i++){
			v=(Vector)State.get(i);
			for(int j=0;j<v.size();j++){
				if(s.equals(v.get(j).toString()))
					return true;
			}
		}
		return false;
	}
	//i为状态行，j为终结符列，确定(i,j)这个位置应该是什么动作（移进，规约，接受，出错）
	private int getAction(int i,int j){
		Vector v=(Vector)State.get(i);					//第i个项集合
		char  c=VT[j];									//第j个终结符，此时VT已添加了句子括号'#'，所以c可以等于'#'
		//以下部分是移进动作
		if(isInAfterPoint(c,v)){						//如果此终结符是这个项集的'.'后面的符号，则检查其移进后的状态
			for(int k=0;k<v.size();k++){				//查找c在这个项集的位置
				String s=v.get(k).toString();
				if((!pointIsTheLastOne(s))&&c==s.charAt(s.indexOf('.')+1)){		//如果'.'不是最后一个符号，移进
					for(int m=0;m<State.size();m++){
						Vector vtemp=(Vector)State.get(m);
						for(int n=0;n<vtemp.size();n++){
							if(movePoint(s).equals(vtemp.get(n).toString())){
								bridge1[i].append(c);
								bridge2[i][bridge1[i].length()-1]=m;
								return m;				//进入m项集,即当前状态经c字符后变成下一个m状态
							}
						}
					}
				}
			}
		}
		for(int p=0;p<v.size();p++){
			if(pointIsTheLastOne(v.get(p).toString())){
				String s1=delete_a_char('.',v.get(p).toString());	//去掉'.'
				String s2=s1.substring(0, s1.indexOf(','));
				for(int q=0;q<F_index;q++){
					if(s2.equals(F[q])){
						if(s1.substring(s1.indexOf(',')).contains(String.valueOf(c)))//规约项逗号后面是否含有c
							return -q;
					}
				}
			}
		}
		return ERROR;						//出错
	}
	//判断一个字符c是否在状态v里项中‘.’之后的字符
	private boolean isInAfterPoint(char c,Vector v){
		String s=getAfterPoint(v);
		if(s.contains(String.valueOf(c)))
			return true;
		else
			return false;
	}
	//返回删除一个源字符串中的指定字符得到的字符串，并不影响源字符串
	private String delete_a_char(char c,String s){
		StringBuffer bf=new StringBuffer(s);
		return bf.subSequence(0, bf.indexOf(String.valueOf(c))).toString().concat(bf.substring(bf.indexOf(String.valueOf(c))+1));
	}
	//i为状态行，j为非终结符列，确定状态i经过一个非终结符j后进入哪个状态
	private int getGoto(int i,int j){
		Vector v=(Vector)State.get(i);					//第i个项集合
		char  c=VN[j];									//第j个非终结符
		if(isInAfterPoint(c,v)){						//如果此非终结符是这个项集的'.'后面的符号，则进行处理
			for(int k=0;k<v.size();k++){				//查找c在这个项集的位置
				String s=v.get(k).toString();
				if((!pointIsTheLastOne(s))&&c==s.charAt(s.indexOf('.')+1)){		//如果c不是最后一个符号
					for(int m=0;m<State.size();m++){
						Vector vtemp=(Vector)State.get(m);
						for(int n=0;n<vtemp.size();n++){
							if(movePoint(s).equals(vtemp.get(n).toString())){
								bridge1[i].append(c);
								bridge2[i][bridge1[i].length()-1]=m;
								return m;				//当前状态经非终结符c字符后进入下一个m状态
							}
						}
					}
				}
			}
		}
		return ERROR;									//出错
	}
	//把待分析字符转换成在VN与VT里的序号
	private int toColumn(char c){
		if(isInVT(c))									//如果是非终结符
			for(int i=0;i<VT.length;i++){
				if(c==VT[i])
					return i;
			}
		else if(isInVN(c))
			for(int j=0;j<VN.length;j++){
				if(c==VN[j])
					return j;
			}
		return ERROR;									//出错
	}
	//显示LR1项集族
	private void displayLR1States(){
		Vector v=new Vector();
		String result=new String();
		String s=new String();				//中间变量
		for(int i=0;i<State.size();i++)	{	//LR1项集
			v=(Vector)State.get(i);
			result+="I"+i+"\t";
			for(int ii=0;ii<v.size();ii++){
				s=v.get(ii).toString();
				if(ii==v.size()-1)
					if(s.charAt(0)=='@')		//把'@'用S'显示
						result+="S'->"+s.substring(1)+"\n";
					else
						result+=s.charAt(0)+
								"->"+s.substring(1)+"\n";//最后一个项只要回车换行，不需要加制表符
				else
				if(s.charAt(0)=='@')		//把'@'用S'显示
					result+="S'==>"+s.substring(1)+"\n\t";
				else
					result+=s.charAt(0)+"==>"+s.substring(1)+"\n\t";
			}
		}
		System.out.println("LR1项集：\n"+result);
	}
	//显示LR1分析表
	private void displayLR1AnalysisTable(){
		String s=new String();				//中间变量
		String result=new String();
		//显示表头
		result+="状 态"+"\t";
		for(int j=0;j<VT.length;j++)		//LR1分析表的第一行中的终结符
			result+=VT[j]+"\t";
		for(int k=0;k<VN.length;k++)		//LR1分析表的第一行中的非终结符
			result+=VN[k]+"\t";
		result+="\n";//换行

		//显示分析表数据
		for(int m=0;m<Action.length;m++){
			result+=m+"\t";
			//动作表部分
			for(int n=0;n<Action[m].length;n++){
				if(Action[m][n]==ERROR)		//出错
					s=error;
				else if(Action[m][n]<0)		//归约
					s="r"+String.valueOf(-Action[m][n]);
				else if(Action[m][n]>0)		//移进
					s="S"+String.valueOf(Action[m][n]);
				else if(Action[m][n]==0)	//接受
					s=acc;
				result+=s+"\t";
			}
			//GoTo表部分
			for(int p=0;p<Goto[m].length;p++){
				if(Goto[m][p]==ERROR)		//出错
					s=error;
				else
					s=String.valueOf(Goto[m][p]);
				result+=s+"\t";
			}
			result+="\n";//换行
		}
		System.out.println("LR1分析表：\n"+result);
	}
	//主函数
	public static void main(String[] args) {
		new LR1();
	}
}
