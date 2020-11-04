package chat3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiServer {
	
	//멤버변수
	static ServerSocket serverSocket = null;
	static Socket socket = null;
	static PrintWriter out = null;
	static BufferedReader in = null;
	static String s = "";
	
	//생성자메소드
	public MultiServer() {
		//실행부 없음
	}
	
	//서버의 초기화를 담당할 메소드
	public static void init() {
		//클라이언트의 이름을 저장
		String name="";		
				
		try {
			//클라이언트의 접속을 대기
			serverSocket = new ServerSocket(9999);
			System.out.println("서버가 시작되었습니다.");
			
			//클라이언트의 접속을 허가
			socket = serverSocket.accept();
			System.out.println(socket.getInetAddress()+":"+socket.getPort());
			
			//클라이언트로 메시지를 보낼 준비(Output 스트림)
			out = new PrintWriter(socket.getOutputStream(), true); 
			//클라이언트가 보내주는 메시지를 읽을 준비(Input 스트림)
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			// 1) 클라이언트가 최초로 전송하는 메시지는 이름
			if(in!=null) {
				name=in.readLine(); 
				// 2) 접속자명을 서버의 콘솔에 출력한다
				System.out.println(name+" 접속");
				// 3) 클라이언트에게 Echo한다				
				out.println("> "+name+"님이 접속했습니다."); //PrintWriter 사용
			}
			
			//클라이언트가 전송하는 메시지를 계속해서 읽어온다.
			while(in != null) {
				s = in.readLine();
				if(s == null) {
					break;
				}
				//읽어온 메시지를 서버의 콘솔에 출력한다.
				System.out.println(name+" ===> "+s);
				//클라이언트에게 Echo한다	
				sendAllMsg(name, s);
			}
			System.out.println("Bye...!!!");
		} 		
		catch (Exception e) {
			System.out.println("예외1: "+e);
//			e.printStackTrace();
		}
		finally {
			try {
				in.close();
				out.close();
				socket.close();
				serverSocket.close();
			}
			catch (Exception e) {
				System.out.println("예외2: "+e);
//				e.printStackTrace();
			}
		}
	}//end of init
	
	//서버가 클라이언트에게 메시지를 Echo해주는 메소드
	public static void sendAllMsg(String name, String msg) {
		try {
			out.println("> "+name+"==> "+msg);
		} 
		catch (Exception e) {
			System.out.println("예외: "+e);
		}
	}//end of sendAllMsg
	
	//main()은 프로그램의 출발점 역할만 담당한다.
	public static void main(String[] args) {
		init();
	}
}
 