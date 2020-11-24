package chat5;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiServer {
	
	//멤버변수
	static ServerSocket serverSocket = null;
	static Socket socket = null;
	
	//기본생성자
	public MultiServer() {
		//실행부 없음
	}
	
	//채팅 서버 초기화 메소드
	public void init() {
		
		try {
			serverSocket = new ServerSocket(9999);
			System.out.println("서버가 시작되었습니다.");
			
			/*
			 1명의 클라이언트가 접속할 때 마다 접속을 허용(accept()) 해주고
			 동시에 MultiServerT 쓰레드를 생성한다.
			 해당 쓰레드는 1명의 클라이언트가 전송하는 메시지를 읽어서
			 Echo해주는 역할을 담당한다.
			 */
			while(true) {
				socket = serverSocket.accept();
				
				//내부클래스의 객체 생성 및 쓰레드 시작
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
	
	/*
	 chat4 까지는 init()이 static이었으나, 
	 chat5부터는 일반적인 메소드로 변경되었다.
	 따라서 객체를 생성 후 호출하는 방식으로 변경된다.	
	 */
	public static void main(String[] args) {
		MultiServer ms=new MultiServer();
		ms.init();
	}
	
	//내부클래스
	/*
	 init()에 기술되었던 <스트림 생성 후 메시지 읽기/쓰기> 파트
	 => 내부클래스로 이동
	 */
	class MultiServerT extends Thread {
		
		Socket socket;
		PrintWriter out = null;
		BufferedReader in = null;
		
		/*
		 MultiServerT 생성자: 1명의 클라이언트가 접속할 때 생성했던 Socket객체를
		 매개변수로 받아 이를 기반으로 입출력 스트림을 생성하고 있다.
		 */
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
		
		/*
		 쓰레드로 동작할 run()에서는 클라이언트의 접속자명과 메시지를 
		 지속적으로 읽어 Echo해주는 역할을 하고 있다.
		 */
		@Override
		public void run() {
			String name = "";
			String s="";
			
			try {
				//접속자명을 Echo
				if(in!=null) {
					name=in.readLine();
					
					System.out.println(name+" 접속");
					out.println("> "+name+"님이 접속했습니다");
				}
				//메시지를 Echo
				while(in!=null) {
					s=in.readLine();
					if(s==null) break;
					
					System.out.println(name+" >> "+s);
					sendAllMsg(name, s);
				}				
			} 			
			catch (Exception e) {
				System.out.println("예외: "+e);
			}
			finally {
				System.out.println(Thread.currentThread().getName()+" 종료");
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
		
		//클라이언트에게 서버의 메시지를 Echo해줌
		public void sendAllMsg(String name, String msg) {
			try {
				out.println("> "+name+"==> "+msg);
			} 
			catch (Exception e) {
				System.out.println("예외: "+e);
			}
		}//end of sendAllMsg	
	} //end of class MultiServerT	
}
 