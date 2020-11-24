package chat7;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MultiServer {
	
	static ServerSocket serverSocket = null;
	static Socket socket = null;
	
	//클라이언트 정보저장을 위한 Map컬렉션생성
	Map<String, PrintWriter> clientMap;
	
	public MultiServer() {
		clientMap=new HashMap<String, PrintWriter>();
		Collections.synchronizedMap(clientMap);
	}
	
	public void init() {
		
		try {
			serverSocket = new ServerSocket(9999);
			System.out.println("서버가 시작되었습니다.");
			
			while(true) {
				socket = serverSocket.accept();
				System.out.println(
						socket.getInetAddress()+"(클라이언트)의 "
						+socket.getPort()+" 포트를 통해 "
						+socket.getLocalAddress()+ "(서버)의 "
						+socket.getLocalPort()+"포트로 연결됨.");
						
				Thread mst = new MultiServerT(socket);
				mst.start();
			}		
		}		
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {				
				serverSocket.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}//end of init

	public static void main(String[] args) {
		MultiServer ms=new MultiServer();
		ms.init();
	}
	
	public void sendAllMsg(String name, String msg, String flag) {
		
		Iterator<String> it = clientMap.keySet().iterator();
		
		while(it.hasNext()) {
			try {
				//컬렉션의 Key는 클라이언트의 접속자명이다.
				String clientName=it.next();				
				
				//각 클라이언트의 PrintWritter객체를 얻어온다.
				PrintWriter it_out
					= (PrintWriter)clientMap.get(clientName);
				
				if(flag.equals("One")) {
					//flag가 One이면 해당 클라이언트에게 전송
					if(name.equals(clientName)) {
						//컬렉션에 저장된 접속자명과 일치하는 경우 메시지 전송
						it_out.println("[귓속말]"+msg);
					}
				}
				else {
					//그 외에는 모든 클라이언트에게 전송
					if(name.equals("")) {
						it_out.println(msg);
					}
					else {
						it_out.println("["+name+"]: "+msg);
					}
				}				
			} 
			catch (Exception e) {
				System.out.println("예외: "+e);
			}
		}		
	}//end of sendAllMsg	
	
	class MultiServerT extends Thread {
		
		Socket socket;
		PrintWriter out = null;
		BufferedReader in = null;
		
		public MultiServerT(Socket socket) {
			this.socket=socket;
			try {
				out=new PrintWriter(this.socket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader
						(this.socket.getInputStream()));
			} 
			catch (Exception e) {
				System.out.println("예외: "+e);
			}
		}//end of MultiServerT
		
		@Override
		public void run() {
			String name = "";
			String s="";
			
			try {
				name=in.readLine();
				
				sendAllMsg("", name+"님이 접속했습니다.", "All");
				clientMap.put(name, out);
				
				System.out.println(name+" 접속");
				System.out.println("현재 접속자 수는 "+clientMap.size()+"명 입니다.");
				
				while(in!=null) {
					s=in.readLine();
					if(s==null) break;
					
					System.out.println(name+" >> "+s);
					
					/*
					 클라이언트가 전송한 메시지가 명렁어인지 판단한다.
					*/					
					if(s.charAt(0)=='/') {
						//만약 /로 시작한다면 명령어이다.
						/*
						 귓속말은 아래와 같이 전송하게 된다.
						 -> /to 대화명 대화내용
						 	: 따라서 split()을 통해 space로 문자열을 분리한다.
						 */
						String[] strArr = s.split(" ");
						/*
						 대화내용에 스페이스가 있는 경우 문장의 끝까지를 출력해야 하므로
						 배열의 크기만큼 반복하면서 문자열을 이어준다.
						 */
						String msgContent="";						
						for(int i=2; i<strArr.length; i++) //i<strArr.length 배열의 1번째 칸 외의 그 뒷부분만
							msgContent += strArr[i]+" ";
							
						if(strArr[0].equals("/to")) {
							//함수 호출시 One이면 한명에게만 메시지 전송
							
							sendAllMsg(strArr[1], msgContent, "One");
						}
					}
					else {
						//만약 /로 시작하지 않으면 일반메시지이다.
						sendAllMsg(name, s, "All"); //All이면 전체 전송
					}
				}				
			} 			
			catch (Exception e) {
				System.out.println("예외: "+e);
			}
			finally {
				clientMap.remove(name);
				sendAllMsg("", name+"님이 퇴장하셨습니다.", "All");
				System.out.println(						
					name+" ["+Thread.currentThread().getName()+"]퇴장");
				System.out.println("현재 접속자 수는 "
						+clientMap.size()+"명 입니다.");				
				try {
					in.close();
					out.close();
					socket.close();
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}//end of run		
	} //end of class MultiServerT	
}
 