package chat1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MultiClient {
	
	public static void main(String[] args) {
		
		System.out.println("이름을 입력하세요: ");
		Scanner scanner = new Scanner(System.in);
		String s_name = scanner.nextLine();
		PrintWriter out = null;
		BufferedReader in =null;
		
		try {
			//클라이언트 실행 시 별도의 접속 IP가 없으면 lockalhost로 고정된다.
			String ServerIP="localhost";
			//실행시 매개변수로 IP주소를 전달
			if(args.length > 0) {
				ServerIP = args[0];
			}
			//IP주소와 포트를 기반으로 Socket개체를 생성하여 서버에 접속요청을 한다.
			Socket socket = new Socket(ServerIP, 9999);
			System.out.println("서버와 연결되었습니다...");
			
			/*
			 InputStreamReader / OutputStreamReader 는
			 바이트스트림과 문자스트림의 상호변환을 제공하는 입출력스트림이다.
			 바이트를 읽어서 지정된 문자인코딩에 따라 문자로 변환하는데 사용된다. 
			 */
			//메시지 주고받을 준비
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			//출력스트림(out.~~)을 통해 서버로 입력된 이름을 전송한다.
			out.println(s_name);
			//서버가 보내준(Echo해 준) 메시지를 라인단위로 읽어 콘솔에 출력한다.
			System.out.println("Recieve: "+in.readLine());			
			
			//스트림과 소켓을 종료한다.
			in.close();
			out.close();
			socket.close();
		} 
		
		catch (Exception e) {
			System.out.println("예외발생 [MultiClient]"+e);
		}
	}
}
