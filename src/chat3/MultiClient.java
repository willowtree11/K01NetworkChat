package chat3;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MultiClient {
	
	public static void main(String[] args) {
		
		//클라이언트의 접속자명을 입력한다.
		System.out.println("이름을 입력하세요: ");
		Scanner scanner = new Scanner(System.in);
		String s_name = scanner.nextLine();
		PrintWriter out = null;
		//서버의 메시지를 읽어오는 기능을 Receiver클래스로 옮김.
		//BufferedReader in =null;
		
		try {
			/*
			 C:\bin>java chat3.MultiClient 접속할 IP주소
			 	=>위와 같은 명령어를 통해 접속하면 로컬이 아닌 특정 서버로 접속 가능
			 */
			String ServerIP="localhost";
			if(args.length > 0) {
				ServerIP = args[0];
			}
			Socket socket = new Socket(ServerIP, 9999);
			System.out.println("서버와 연결되었습니다...");
			
			//서버에서 보내는 메시지를 읽어올 Receiver쓰레드 객체생성 및 시작
			Thread receiver=new Receiver(socket);
			//setDaemon(true) 선언이 없으므로 독립쓰레드로 생성된다.
			receiver.start();
			
			out = new PrintWriter(socket.getOutputStream(), true);
			out.println(s_name); //최초로 접속자의 이름을 전송
			
			//이름 전송 이후에는 메시지를 지속적으로 전송함
			while(out!=null) {
				try {
					//종료문 지정
					String s2=scanner.nextLine();
					if(s2.equals("q")||s2.equals("Q")) {
						break;
					}
					//메시지 서버로 보내기
					else {
						out.println(s2);
					}
				} 
				catch (Exception e) {
					System.out.println("예외: "+e);
				}
			}
			//while문의 루프를 탈출하면 스트림 및 '소켓'을 즉시 종료한다.
			out.close();
			socket.close();
		} 
		
		catch (Exception e) {
			System.out.println("예외발생 [MultiClient]"+e);
		}
	}
}
